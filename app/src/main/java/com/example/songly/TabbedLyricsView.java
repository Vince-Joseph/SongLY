package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TabbedLyricsView extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem tabLyrics, tabDetails;
    ViewPager viewPager;
    PageAdapterLyrics pageAdapterLyrics;

    String lyrics, details;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_lyrics_view);

        tabLayout = findViewById(R.id.tabLayoutViewLyrics);
        tabLyrics = findViewById(R.id.tab_lyrics);
        tabDetails = findViewById(R.id.tab_details);

        viewPager = findViewById(R.id.viewPagerLyrics);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");
        String folderName = intent.getStringExtra("folderName");

        readLyrics(folderName, fileName); // read the content from given folder/filename.txt

        // create page adapter object with required parameters
        // a. lyrics read from the file
        // b. details read from the file
        // c. typeface object
        pageAdapterLyrics = new PageAdapterLyrics(getSupportFragmentManager(), tabLayout.getTabCount(),
                lyrics,
                details,
                typeface);

        viewPager.setAdapter(pageAdapterLyrics);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        // listens for scroll or page change
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void readLyrics(String folderName, String fileName) {
        InputStream is = null;
        lyrics="No data found";
        details = "No details found";
        typeface = null;
        Boolean flag = true;

        try
        {
            is = getAssets().open(folderName+"/"+fileName+".txt");
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            lyrics = "";
            details = "";
            int i = 0;
            while((line = bufferedReader.readLine()) != null)
            {
                if( i == 0)
                    details += line;
                else
                    lyrics += line+"\n";

                i++;
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
            lyrics = "File not found!";
            flag = false;
        }

        if(flag)
        {
            typeface = Typeface.createFromAsset(getAssets(),"font/MLKR0NTT.TTF");
//            textViewLyrics.setTypeface(typeface);
        }

//        textViewLyrics.setText(lyrics);

    }
}