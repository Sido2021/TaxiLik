package com.taxilik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextEmail ,editTextPassword ,editTextPasswordConfirmation, editTextFirstName,editTextLastName,editTextPhoneNumber;
    Button buttonSignUp;
    String firstName ,lastName , phoneNumber , email,password,passwordConfirmation;
    ProgressDialog pdDialog;
    String URL_REGISTER = "https://omega-store.000webhostapp.com/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextEmail=findViewById(R.id.edit_text_email_register);
        editTextPassword=findViewById(R.id.edit_text_password_register);
        editTextPasswordConfirmation=findViewById(R.id.edit_text_password_confirmation_register);

        buttonSignUp =findViewById(R.id.button_sign_up);

        pdDialog= new ProgressDialog(RegistrationActivity.this);
        pdDialog.setTitle("Registering please wait...");
        pdDialog.setCancelable(false);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=editTextEmail.getText().toString().trim();
                password=editTextPassword.getText().toString().trim();
                passwordConfirmation=editTextPasswordConfirmation.getText().toString().trim();
                firstName=editTextFirstName.getText().toString().trim();
                lastName=editTextLastName.getText().toString().trim();
                phoneNumber=editTextPhoneNumber.getText().toString().trim();

                if(email.isEmpty()||password.isEmpty()||passwordConfirmation.isEmpty()||
                        firstName.isEmpty()||lastName.isEmpty()||phoneNumber.isEmpty() ||
                !password.equals(passwordConfirmation))
                {
                    Toast.makeText(RegistrationActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else{
                    Register();
                }
            }
        });
    }
    private void Register()
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
                                Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                                Intent login = new Intent(RegistrationActivity.this,LoginActivity.class);
                                startActivity(login);
                                finish();
                            }
                            if(success.equals("0")){
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
                params.put("email",email);
                params.put("password",password);
                params.put("nom",firstName);
                params.put("prenom",lastName);
                params.put("tele",phoneNumber);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
