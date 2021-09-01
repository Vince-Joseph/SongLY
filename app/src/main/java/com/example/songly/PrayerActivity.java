package com.example.songly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;


public class PrayerActivity extends AppCompatActivity{

    PDFView pdfView;
    BottomNavigationView navView;
    boolean visible = true;
    Intent intent;
    boolean checkScrollingUp = false;
    LinearLayout linearLayout, pdfLayout, layoutBottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);

        setTitle("Prayer");
        pdfView = findViewById(R.id.prayerPdf);
        pdfView.fromAsset("prayer/HolyMassPrayer.pdf")
                .enableDoubletap(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .nightMode(false)
                .scrollHandle(new DefaultScrollHandle(this))
                .onTap(hideThings())
                .load();

        linearLayout = findViewById(R.id.headingLayout);
        pdfLayout = findViewById(R.id.pdfLayout);
        layoutBottomBar = findViewById(R.id.layoutBottomBar);

        navView = findViewById(R.id.nav_view);
        // changing default selection of bottom bar
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(true);
        // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId();

                switch(menuId)
                {
                    case R.id.navigation_list:{
                        intent = new Intent(PrayerActivity.this, ListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        break;
                    }
                    case R.id.navigation_song:
                        intent = new Intent(PrayerActivity.this, HomePage.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

//        linearLayout = findViewById(R.id.headingLayout);
//        pdfView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//                if(scrollY > 0)
//                {
//                    linearLayout.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.trans_downwards));
//                    checkScrollingUp = false;
//                }
//                else
//                {
//                    if(! checkScrollingUp)
//                    {
//                        linearLayout.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.trans_upwards));
//                        checkScrollingUp = true;
//                    }
//                }
//            }
//        });
    }

    // toggle the action bar and bottom nav bar visibility upon single tap
    private OnTapListener hideThings() {
        
        return new OnTapListener() {
            @Override
            public boolean onTap(MotionEvent e) {
                if(visible)
                {
                    navView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    pdfLayout.startAnimation(AnimationUtils.loadAnimation(linearLayout.getContext(),
                            R.anim.trans_upwards));
//                    linearLayout.animate()
//                            .setDuration(500)
//                            .translationY(-180);
//
//                    pdfLayout.animate()
//                            .setDuration(500)
//                            .translationY(-180);


//                    layoutBottomBar.setAlpha(1f);
//                    layoutBottomBar.animate()
//                            .setDuration(500)
//                            .alpha(0f)
//                            .translationY(200)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    layoutBottomBar.setVisibility(View.GONE);
////                                    navView.setVisibility(View.GONE);
//                                }
//                            });

                    visible = false;
                }
                else
                {
                    visible = true;
                    navView.setVisibility(View.VISIBLE);
//                    linearLayout.startAnimation(AnimationUtils.loadAnimation(linearLayout.getContext(),
//                            R.anim.trans_downwards));
                    linearLayout.setVisibility(View.VISIBLE);
                    pdfLayout.startAnimation(AnimationUtils.loadAnimation(linearLayout.getContext(),
                            R.anim.trans_downwards));

//                    pdfLayout.animate().translationY(0);
//                    linearLayout.setVisibility(View.VISIBLE);
//                    linearLayout.animate()
//                            .setDuration(500)
//                            .translationY(0);
//                    linearLayout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        };
    }

    // whenever PrayerActivity is made visible, set the navigation prayer item checked
    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(true);
        super.onStart();
    }
}