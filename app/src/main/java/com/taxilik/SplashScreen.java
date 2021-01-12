package com.taxilik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        startVerification();
    }

    private void verifyConnection(){
        if(isNetworkConnected() ){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Intent i = new Intent();

            if(currentUser!=null)i.setClass(this,ClientActivity.class);
            else i.setClass(this,LoginActivity2.class);

            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(this, "Please check your connection !", Toast.LENGTH_SHORT).show();
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