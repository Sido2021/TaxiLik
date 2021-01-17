package com.taxilik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity2 extends AppCompatActivity {


    EditText editTextEmail ,editTextPassword ,editTextPasswordConfirmation, editTextFirstName,editTextLastName,editTextPhoneNumber;
    Button buttonSignUp;

    ProgressDialog pdDialog;

    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://taxilik.appspot.com");
    StorageReference storageRef ;

    ImageView imageViewProfile ;
    Uri selectedImage ;
    Uri uploadedImage ;


    String URL_REGISTER = "https://omega-store.000webhostapp.com/register.php";

    public static int PERMISSION_REQUEST_CODE = 101;
    public static int PICK_IMAGE_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();

        pdDialog= new ProgressDialog(RegisterActivity2.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextEmail = findViewById(R.id.edit_text_email_register);
        editTextPassword = findViewById(R.id.edit_text_password_register);
        editTextPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation_register);
        imageViewProfile = findViewById(R.id.profile_image_signup);


        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGallery();
            }
        });
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            uploadImageAndSaveInfo();
                            sendVerificationEmail(user);
                        } else {
                            Toast.makeText(RegisterActivity2.this, "non", Toast.LENGTH_SHORT).show();
                        }
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private void showProgressBar() {
        pdDialog.show();
    }

    private void hideProgressBar() {
        pdDialog.hide();
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }
        String firstName = editTextFirstName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            editTextFirstName.setError("Required.");
            valid = false;
        } else {
            editTextFirstName.setError(null);
        }
        String lastName = editTextLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            editTextLastName.setError("Required.");
            valid = false;
        } else {
            editTextLastName.setError(null);
        }
        String phoneNumber = editTextPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            editTextPhoneNumber.setError("Required.");
            valid = false;
        } else {
            editTextPhoneNumber.setError(null);
        }

        String password2 = editTextPasswordConfirmation.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            editTextPasswordConfirmation.setError("Required.");
            valid = false;
        } else {
            editTextPasswordConfirmation.setError(null);
        }


        if (!password.equals(password2)) {
            editTextPasswordConfirmation.setError("confirmation failed!");
            valid = false;
        } else {
            editTextPasswordConfirmation.setError(null);
        }

        return valid;
    }
    
    private void sendVerificationEmail(final FirebaseUser user){
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity2.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity2.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    private void Register(final String userKey)
    {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                                Intent login = new Intent(RegisterActivity2.this,LoginActivity2.class);
                                startActivity(login);
                                finish();
                            }
                            if(success.equals("2")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }
                            if(success.equals("3")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                if(uploadedImage!=null) {
                    params.put("image",uploadedImage.getEncodedPath());
                }
                else
                    params.put("image","");

                params.put("user_key",userKey);
                params.put("first_name",editTextFirstName.getText().toString());
                params.put("last_name",editTextLastName.getText().toString());
                params.put("phone",editTextPhoneNumber.getText().toString());
                params.put("type","1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showGallery() {
        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadGallery();
        } else {
            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    private void loadGallery() {
        Intent choose = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choose, PICK_IMAGE_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGallery();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = data.getData();
                Picasso.get().load(selectedImage).into(imageViewProfile);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void uploadImageAndSaveInfo(){

        final FirebaseUser user = mAuth.getCurrentUser();
        if(selectedImage !=null){

            imageViewProfile.setDrawingCacheEnabled(true);
            imageViewProfile.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageViewProfile.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();


            StorageReference filepath=storageRef.child("Images").child(selectedImage.getLastPathSegment());

            final UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Register(user.getUid());
                    Log.e("Profile:","Failed...");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadedImage = taskSnapshot.getUploadSessionUri();
                    Register(user.getUid());
                }
            });

        }
        else {
            Register(user.getUid());
        }

    }
}