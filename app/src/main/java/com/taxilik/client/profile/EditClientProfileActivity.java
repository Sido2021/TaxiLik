package com.taxilik.client.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.taxilik.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class EditClientProfileActivity extends AppCompatActivity {
    Button update;
    TextView fullname_field2,username_field2;
    TextView fullname_field,username_field ;

    ImageView userPofile ;
    TextInputEditText fname_profile,lname_profile,email_profile,phone_profile;
    String fname,lname,email,phone;
    String URL_UPDATE= "https://.000webhostapp.com/ ....";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profile_edit_fragment);


        fullname_field=findViewById(R.id.fullname_field);
        username_field=findViewById(R.id.username_field);
        fname_profile=findViewById(R.id.fname_profile);
        lname_profile=findViewById(R.id.lname_profile);
        email_profile=findViewById(R.id.email_profile);
        phone_profile=findViewById(R.id.phone_profile);
        userPofile = findViewById(R.id.profile_image);
        loadInformation();

        update=findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=email_profile.getText().toString().trim();
                fname=fname_profile.getText().toString().trim();
                lname=lname_profile.getText().toString().trim();
                phone=phone_profile.getText().toString().trim();

                if(email.isEmpty()||fname.isEmpty()||lname.isEmpty()||
                        phone.isEmpty())
                {
                    Toast.makeText(EditClientProfileActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else{
                    Update();
                }

            }
        });
}
    private void Update()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"your data are updated",Toast.LENGTH_LONG).show();

                                Intent login = new Intent(EditClientProfileActivity.this, ClientProfileFragment.class);
                                startActivity(login);
                                finish();
                            }
                            if(success.equals("0")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                            if(success.equals("3")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"update Error !"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Update Error !"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("firstName",fname);
                params.put("lastName",lname);
                params.put("phone",phone);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadInformation(){
        fullname_field.setText(CurrentUser.getFirstName().concat(" "+CurrentUser.getLastName()));
        username_field.setText(CurrentUser.getFirstName().concat("_"+CurrentUser.getLastName()+"@"));

        fname_profile.setText(CurrentUser.getFirstName());
        lname_profile.setText(CurrentUser.getLastName());
        email_profile.setText(CurrentUser.getEmail());
        phone_profile.setText(CurrentUser.getPhone());
        Picasso.get().load(CurrentUser.getImage()).into(userPofile);
    }
}
