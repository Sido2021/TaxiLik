package com.taxilik;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class LoginActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword;
    Button loginButton;
    TextView registerNow , forgetPassword;
    ImageView showHidePasswordImageView ;
    ProgressDialog pdDialog;

    SharedPreferences sharedPreferences ;

    String URL_host = "https://omega-store.000webhostapp.com/getProfile.php";

    boolean passwordVisible = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserInfo",
                MODE_PRIVATE);

        registerNow = findViewById(R.id.text_view_register);
        forgetPassword = findViewById(R.id.forget_password);
        editTextEmail =  findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        loginButton=findViewById(R.id.button_login);
        showHidePasswordImageView = findViewById(R.id.show_hide_password);

        pdDialog= new ProgressDialog(LoginActivity2.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register=new Intent(LoginActivity2.this,RegisterActivity2.class);
                startActivity(register);
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgetPasswordDialog();
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showHidePasswordImageView.setVisibility(View.VISIBLE);
                }
                else{
                    showHidePasswordImageView.setVisibility(View.GONE);
                }
            }
        });

        showHidePasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordVisible) {
                    showHidePasswordImageView.setImageResource(R.drawable.ic_visible_on);
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordVisible=false;
                }
                else {
                    showHidePasswordImageView.setImageResource(R.drawable.ic_visible_off);
                    editTextPassword.setTransformationMethod(null);
                    passwordVisible=true;
                }
                editTextPassword.setSelection(editTextPassword.getText().length());
            }
        });
    }

    private void openForgetPasswordDialog() {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.layout_forget_password);

        final EditText forgetPasswordEmail = d.findViewById(R.id.forget_password_email);
        Button buttonRestPassword = d.findViewById(R.id.button_reset_password);

        buttonRestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(forgetPasswordEmail.getText().toString())) {
                    editTextEmail.setError("Required.");
                } else {
                    editTextEmail.setError(null);
                    resetPassword(forgetPasswordEmail.getText().toString());
                    d.dismiss();
                }
            }
        });

        d.show();
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity2.this, "Email sent !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        pdDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            if(user.isEmailVerified()){
                                getProfile(user);
                            }
                            else {
                                Toast.makeText(LoginActivity2.this, "Please verify your email !",
                                        Toast.LENGTH_SHORT).show();
                                pdDialog.dismiss();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pdDialog.dismiss();
                        }
                    }
                });
    }


    private void getProfile(final FirebaseUser currentUser)
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_host,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("0")){
                                int id = jsonObject.getInt("id");
                                String firstName = jsonObject.getString("first_name");
                                String lastName = jsonObject.getString("last_name");
                                String phone = jsonObject.getString("phone");
                                String image = jsonObject.getString("image");
                                int type = jsonObject.getInt("type") ;
                                String email = currentUser.getEmail() ;
                                CurrentUser = new User(id,firstName,lastName,phone,image,type,email);
                                saveUser();
                            }
                            else{
                                mAuth.signOut();
                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity2.this,"Error !"+e,Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity2.this,"Error !"+error,Toast.LENGTH_LONG).show();
                mAuth.signOut();
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_key",currentUser.getUid());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity2.this);
        requestQueue.add(stringRequest);
    }


    private void saveUser() {
        sharedPreferences.edit().putInt("id",CurrentUser.getId()).commit();
        sharedPreferences.edit().putString("firstName",CurrentUser.getFirstName()).commit();
        sharedPreferences.edit().putString("lastName",CurrentUser.getLastName()).commit();
        sharedPreferences.edit().putString("phone",CurrentUser.getPhone()).commit();
        sharedPreferences.edit().putString("image",CurrentUser.getImage()).commit();
        sharedPreferences.edit().putInt("type",CurrentUser.getType()).commit();



        Toast.makeText(LoginActivity2.this, "Welcome "+CurrentUser.getFirstName()+" "+CurrentUser.getLastName(),
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        if(CurrentUser.getType()==1)intent.setClass(LoginActivity2.this,ClientActivity.class);
        else intent.setClass(LoginActivity2.this,DriverActivity.class);
        startActivity(intent);
        finish();
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

        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}