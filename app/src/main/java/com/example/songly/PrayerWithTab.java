package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrayerWithTab extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem tabPrayer, tabExtra;
    ViewPager viewPager;
    PageAdapterPrayer pageAdapterPrayer;
    Intent intent;
    public static File requiredPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parayer_with_tab);

        requiredPath = getExternalFilesDir("prayer");;
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
}