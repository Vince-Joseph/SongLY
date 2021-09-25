package com.example.songly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

/**
 *
 * This activity holds two fragments, FragmentPrayer and FragmentExtra.
 * The activity is intended to display the mass prayers and karososa prayers in two tabs.
 *
 */
public class PrayerWithTab extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem tabPrayer, tabExtra;
    ViewPager viewPager;
    PageAdapterPrayer pageAdapterPrayer;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parayer_with_tab);



//        requiredPath = getExternalFilesDir("prayer");
        tabLayout = findViewById(R.id.tabPrayer);
        tabPrayer = findViewById(R.id.tab_prayer);
        tabExtra = findViewById(R.id.tab_extra);

        viewPager = findViewById(R.id.viewPagerPrayer);


        pageAdapterPrayer = new PageAdapterPrayer(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pageAdapterPrayer);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                // notify if tabs gets changed
                if(tab.getPosition() == 0 || tab.getPosition() == 1)
                    pageAdapterPrayer.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // listens for scroll or page change
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

// ---------------------- Bottom navigation bar --------------------------------------------

        // bottom navigation bar
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // changing default selection of bottom bar
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(true);


        // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId(); // get the id of the button clicked


                // no comparison for homepage because we are at home page
                if (menuId == R.id.navigation_list) {// goto list activity
                    intent = new Intent(PrayerWithTab.this, ListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                } else if (menuId == R.id.navigation_song) {  // goto prayer activity
                    intent = new Intent(PrayerWithTab.this, HomePage.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    startActivity(intent);
                }
                return true;
            }
        });
// ------------------------------- Bottom nav ends here ------------------------------------------------

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull String name, @NonNull @NotNull Context context, @NonNull @NotNull AttributeSet attrs) {
//        checkPermission(); // check for storage permission
        return super.onCreateView(name, context, attrs);
    }

    // check storage permission
    private void checkPermission() {

        // if permission is not granted then ask for it
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    // when request is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {

        if(requestCode == 1)
        {
            if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                // return back to home screen
                intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                finish(); // we are finishing the activity to pop it out from the stack such that
                // at a later stage, when we open the page, we get updated items, not some old views

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}