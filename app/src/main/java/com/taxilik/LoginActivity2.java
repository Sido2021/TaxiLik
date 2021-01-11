package com.taxilik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextEmail,editTextPassword;
    Button loginButton;
    TextView registerNow;
    ImageView showHidePasswordImageView ;
    ProgressDialog pdDialog;


    boolean passwordVisible = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        registerNow = findViewById(R.id.text_view_register);
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

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            if(user.isEmailVerified()){
                                Intent login = new Intent(LoginActivity2.this,ClientActivity.class);
                                startActivity(login);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity2.this, "Please verify your email !",
                                        Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                        hideProgressBar();
                    }
                });
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


    private void showProgressBar() {
        pdDialog.show();
    }

    private void updateUI() {
        hideProgressBar();
    }

    private void hideProgressBar() {
        pdDialog.hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI();
    }
}