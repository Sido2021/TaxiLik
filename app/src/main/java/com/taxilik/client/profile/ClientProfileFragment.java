package com.taxilik.client.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.taxilik.R;

import org.json.JSONObject;

public class ClientProfileFragment extends AppCompatActivity {
    String URL_host = "https://taxiliktestv.000webhostapp.com/profile.php";
    String profile_id="tp3364ecdcg1runtn0t04jsd8d8smvsrb68xb8rzu0ktudzsbk6aygoouley2fp4p7otqefv7o28ten5gy9qnffgs302wt979qtsy0mlci6ss6tf36mg2mf1tr4oq7r7z0us9bremve0oi2ci8cqy50oqrq2pby7d5u26rqjlg6r5zv9g1tgg5tpeyu9fbirzfc33jlwox71galbu2qdwe7rby1vjla771mjiqenetd7k7tjiqzfds3mik";
    String URL_Profile = URL_host+"?id="+profile_id;
    TextView fullname_field,username_field,fname_prof,lname_prof,email_prof,phone_prof;
    ;
    Button edit_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profile_fragment);
        fullname_field=findViewById(R.id.fullname_field);
        username_field=findViewById(R.id.username_field);
        fname_prof=findViewById(R.id.fname_prof);
        lname_prof=findViewById(R.id.lname_prof);
        email_prof=findViewById(R.id.email_prof);
        phone_prof=findViewById(R.id.phone_prof);

        edit_profile=findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intant=new Intent(ClientProfileFragment.this,ClientProfileFragmentEdit.class);
                startActivity(intant);
                finish();
                return;
            }
        });
        profile();

    }

    private void setEditText(String nom,String prenom,String email,String phone){
        fullname_field.setText(nom+" "+prenom);
        username_field.setText(nom+"_"+prenom+"@");
        fname_prof.setText(nom);
        lname_prof.setText(prenom);
        email_prof.setText(email);
        phone_prof.setText(phone);
    }
    private void profile()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_Profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            //1-cas d'une base de donnee
//                            JSONObject jsonObject = new JSONObject(response);
//                            String id = jsonObject.getString("id");
//                            String nom = jsonObject.getString("nom");
//                            String prenom = jsonObject.getString("prenom");
//                            String email=jsonObject.getString("email");
//                            String phone=jsonObject.getString("phone");
////                            setEditText(nom,prenom,email,phone);
//                            Toast.makeText(ClientProfileFragment.this, nom+" - "+prenom, Toast.LENGTH_SHORT).show();

                            //2-pour tester only
                             setEditText("YASSINE","MEROUANE","yassinemer05@gmail.com","+212 643427501");

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"profile error 1-"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"profile error 2-"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}