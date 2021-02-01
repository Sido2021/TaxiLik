package com.taxilik.client.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.taxilik.Data;
import com.taxilik.Feedback;
import com.taxilik.R;
import com.taxilik.User;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class EditClientProfileActivity extends AppCompatActivity {
    Button update;
    TextView fullname_field,username_field ;

    ImageView userPofile ;
    TextInputEditText fname_profile,lname_profile,email_profile,phone_profile;
    String fname,lname,email,phone;
    String URL_UPDATE= "https://omega-store.000webhostapp.com/updateUser.php";

    ProgressDialog pdDialog;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profile_edit_fragment);


        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        pdDialog= new ProgressDialog(this);
        pdDialog.setTitle("wait...");
        pdDialog.setCancelable(false);

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

                if(fname.isEmpty()||lname.isEmpty()||
                        phone.isEmpty())
                {
                    Toast.makeText(EditClientProfileActivity.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else{
                    updateUser();
                }

            }
        });
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

    private void updateUser() {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("0")){
                                sharedPreferences.edit().putString("firstName",CurrentUser.getFirstName()).commit();
                                sharedPreferences.edit().putString("lastName",CurrentUser.getLastName()).commit();
                                sharedPreferences.edit().putString("phone",CurrentUser.getPhone()).commit();

                                CurrentUser.setFirstName(fname);
                                CurrentUser.setLastName(lname);
                                CurrentUser.setPhone(phone);
                                Toast.makeText(EditClientProfileActivity.this, "updated", Toast.LENGTH_SHORT).show();
                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            pdDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(EditClientProfileActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(EditClientProfileActivity.this, "Error !", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",String.valueOf(CurrentUser.getId()));
                params.put("first_name", fname);
                params.put("last_name",lname);
                params.put("phone",phone);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditClientProfileActivity.this);
        requestQueue.add(stringRequest);
    }
}
