package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // make the app no night mode

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int TimeOut = 2000; // the timeout interval

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // intent is used to switch activities
                Intent intent = new Intent(SplashScreen.this, HomePage.class);
                // now invoke the intent
                startActivity(intent);
                finish(); // the current activity wil get finished
            }
        }, TimeOut);
    }
}