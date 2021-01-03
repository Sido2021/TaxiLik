package com.taxilik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    String is_signed_in="";
    SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        is_signed_in = mPreferences.getString("issignedin","false");
        if(is_signed_in.equals("true"))
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }

        finish();
    }
}