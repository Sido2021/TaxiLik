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

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    FirebaseUser currentUser ;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);

        if(currentUser!=null)loadUserFromSharedPreferences();
        startVerification();
    }

    private void verifyConnection(){
        if(isNetworkConnected() ){

            Intent i = new Intent();

            if(currentUser!=null){
                loadUserFromSharedPreferences();
                i.setClass(this,ClientActivity.class);
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
        int id = sharedPreferences.getInt("id",0);
        String firstName =sharedPreferences.getString("firstName","");
        String lastName = sharedPreferences.getString("lastName","") ;
        String phone = sharedPreferences.getString("phone","") ;
        int type = sharedPreferences.getInt("type",0);

        Data.CurrentUser = new User(id,firstName,lastName,phone,type,currentUser.getEmail());
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