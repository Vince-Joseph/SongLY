package com.example.songly;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
//
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;


public class PrayerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    PDFView pdfView;
    BottomNavigationView navView;
    boolean visible = true;
//    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        pdfView = findViewById(R.id.prayerPdf);
        pdfView.fromAsset("prayer/HolyMassPrayer.pdf")
                .enableDoubletap(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .nightMode(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .onTap(hideThings())
                .load();

         navView = findViewById(R.id.nav_view);
        // changing default selection of bottom bar
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(true);
    }

    private OnTapListener hideThings() {
        
        return new OnTapListener() {
            @Override
            public boolean onTap(MotionEvent e) {
                if(visible)
                {
                    navView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                    visible = false;
                }
                else
                {
                    visible = true;
                    getSupportActionBar().show();
                    navView.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int menuId = item.getItemId();
        Intent intent;

        switch(menuId)
        {
            case R.id.navigation_list:{
                intent = new Intent(PrayerActivity.this, ListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.navigation_song:
                intent = new Intent(PrayerActivity.this, HomePage.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}