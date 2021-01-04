package com.taxilik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity2 extends AppCompatActivity {


    EditText editTextEmail ,editTextPassword ,editTextPasswordConfirmation, editTextFirstName,editTextLastName,editTextPhoneNumber;
    Button buttonSignUp;

    ProgressDialog pdDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        pdDialog= new ProgressDialog(RegisterActivity2.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextEmail = findViewById(R.id.edit_text_email_register);
        editTextPassword = findViewById(R.id.edit_text_password_register);
        editTextPasswordConfirmation = findViewById(R.id.edit_text_password_confirmation_register);

        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(editTextEmail.getText().toString(),editTextPassword.getText().toString());
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
                            sendVerificationEmail(user);
                            Intent login = new Intent(RegisterActivity2.this,LoginActivity2.class);
                            startActivity(login);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity2.this, "non", Toast.LENGTH_SHORT).show();
                        }
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        hideProgressBar();
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
}