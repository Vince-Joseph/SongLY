package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * The splash screen
 */

public class SplashScreen extends AppCompatActivity {
    TextView textView1, textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // force night mode off

        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        textView1 = findViewById(R.id.appname);
        textView2 = findViewById(R.id.subtitle);

        // create animation objects
        Animation fadeUp = AnimationUtils.loadAnimation(this, R.anim.fade_up_splash);
        Animation fadeDown = AnimationUtils.loadAnimation(this, R.anim.fade_down_splash);

        // set the animation effects to textviews
        textView1.startAnimation(fadeDown);
        textView2.startAnimation(fadeUp);

        int TimeOut = 1200; // the timeout interval

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // intent is used to switch activities
                Intent intent = new Intent(SplashScreen.this, HomePage.class);
                // now invoke the intent
                startActivity(intent);

                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
                finish(); // the current activity wil get finished, will be popped out from activity stack
            }
        }, TimeOut);
    }

}