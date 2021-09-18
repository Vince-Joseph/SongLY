package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * The splash screen. This is the launcher activity.
 *
 */

public class SplashScreen extends AppCompatActivity {
    TextView textView1, textView2, textView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        if(Build.VERSION.SDK_INT <= 23) // for Lollipop or lower
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // force night mode off

        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        textView1 = findViewById(R.id.appname);
        textView2 = findViewById(R.id.subtitle);
        textView3 = findViewById(R.id.subtitle02);

        // create animation objects


        Animation fadeDown = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fade_down_splash);
        Animation fadeUp = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fade_up_splash);
        textView1.startAnimation(fadeDown);
        textView2.startAnimation(fadeUp);
        textView3.setVisibility(View.INVISIBLE);

        fadeUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation custom = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.zoom_in_author);
                textView3.setVisibility(View.VISIBLE);
                textView3.startAnimation(custom);
            }
        });

        int TimeOut = 1700; // the timeout interval

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