package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * This activity holds two fragments namely LyricsFragment and DetailsFragment
 * Bottom nav is disabled for this activity
 *
 */
public class TabbedLyricsView extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabLyrics, tabDetails;
    ViewPager viewPager;
    PageAdapterLyrics pageAdapterLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_lyrics_view);

        tabLayout = findViewById(R.id.tabLayoutViewLyrics);
        tabLyrics = findViewById(R.id.tab_lyrics);
        tabDetails = findViewById(R.id.tab_details);

        viewPager = findViewById(R.id.viewPagerLyrics);


        Bundle bundle = this.getIntent().getExtras();

        // startPage
        // endPage
        // chord
        // song link
        // karaoke link
        String [] contents = bundle.getStringArray("contents");


        // create page adapter object with required parameters
        // a. lyrics read from the file
        // b. details read from the file
        // c. typeface object
        pageAdapterLyrics = new PageAdapterLyrics(getSupportFragmentManager(), tabLayout.getTabCount(),
                contents
                );

        viewPager.setAdapter(pageAdapterLyrics);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                // notify if tabs gets changed
                if(tab.getPosition() == 0 || tab.getPosition() == 1)
                    pageAdapterLyrics.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//
//                // notify if tabs gets changed
//                if(tab.getPosition() == 0 || tab.getPosition() == 1)
//                    pageAdapterLyrics.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        // listens for scroll or page change
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}