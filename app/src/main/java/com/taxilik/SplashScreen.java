package com.taxilik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Driver;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    FirebaseUser currentUser ;
    SharedPreferences sharedPreferencesUser ;
    SharedPreferences sharedPreferencesCar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharedPreferencesUser = getSharedPreferences("UserInfo",MODE_PRIVATE);


        if(currentUser!=null)loadUserFromSharedPreferences();
        startVerification();
    }

    private void verifyConnection(){
        if(isNetworkConnected() ){

            Intent i = new Intent();
            if(currentUser!=null){
                loadUserFromSharedPreferences();
                if(Data.CurrentUser.getType()==1)
                    i.setClass(this,ClientActivity.class);
                else
                    i.setClass(this,DriverActivity.class);
            }
            else i.setClass(this,LoginActivity2.class);

            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(this, "Please check your connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserFromSharedPreferences() {
        int id = sharedPreferencesUser.getInt("id",0);
        String firstName =sharedPreferencesUser.getString("firstName","");
        String lastName = sharedPreferencesUser.getString("lastName","") ;
        String phone = sharedPreferencesUser.getString("phone","") ;
        String image = sharedPreferencesUser.getString("image","") ;
        int type = sharedPreferencesUser.getInt("type",0);

        Data.CurrentUser = new User(id,firstName,lastName,phone,image,type,currentUser.getEmail());

        if(type==2){
            sharedPreferencesCar = getSharedPreferences("CarInfo",MODE_PRIVATE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void startVerification(){
        final Runnable r = new Runnable() {
            public void run() {
                verifyConnection();
            }
        };
        handler.postDelayed(r, 2000);
    }
}