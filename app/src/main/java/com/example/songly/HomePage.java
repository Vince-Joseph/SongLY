package com.example.songly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerView; // recycler for holding the folders
    BottomNavigationView navView;
    int counter = 0; // for counting how much time back button has been pressed

    Intent intent;
    Toolbar toolbar;
    ImageView im1, im2, im3, searchIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        toolbar = findViewById(R.id.toolbar_lists);
        setSupportActionBar(toolbar); // replace appbar with custom toolbar

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle("Home");

        im1 = findViewById(R.id.editIconToolbar);
        im2 = findViewById(R.id.addIcon);
        im3 = findViewById(R.id.sortIcon);
        searchIcon = findViewById(R.id.searchIcon);

        im1.setVisibility(View.INVISIBLE);
        im2.setVisibility(View.INVISIBLE);
        im3.setVisibility(View.INVISIBLE);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair pair;
                pair = new Pair<View, String>(v, "searchTransition");

                ActivityOptions activityOptions =
                        ActivityOptions.makeSceneTransitionAnimation(HomePage.this, pair);

                intent = new Intent(HomePage.this, FullSearch.class);
                intent.putExtra("mode","off"); // the select mode is off now
                startActivity(intent, activityOptions.toBundle());
//                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.homeScreenRecycler);

        // folder-names arraylist
        ArrayList<String> folderNames = new ArrayList<String>(
                Arrays.asList("Entrance","Pslam","Gospel","Offering","Osana","Elavation","Communion","Others"));


        // adapter object - accepts the folder names arraylist
        AdapterHomePage adapterHomePage = new AdapterHomePage(this, folderNames);
        // layout of recycler - Grid layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapterHomePage); // set the adapter for recycler

        // bottom navigation bar
        navView = findViewById(R.id.nav_view);
        // changing default selection of bottom bar
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);


         // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId(); // get the id of the button clicked


                switch(menuId)
                {
                    // no comparison for homepage because we are at home page
                    case R.id.navigation_list:// goto list activity
                    {
                        intent = new Intent(HomePage.this, ListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        break;
                    }
                    case R.id.navigation_prayer:  // goto prayer activity
                    {
                        intent = new Intent(HomePage.this, PrayerActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });

    }

    /**
     * When ever HomePage gets displayed to user, set the HomeButton of bottom nav bar as checked
     */
    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);
        super.onStart();
    }

    @Override
    public void onBackPressed() {

        if(counter >= 2)
            super.onBackPressed();
        else if(counter == 1)
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        counter++;
    }

    /**
     * Set the search icon at the top right corner
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater(); // search menu inflation
//        inflater.inflate(R.menu.search_menu_home, menu);
//        return true;
//    }
//
//    // upon clicking search icon
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId())
//        {
//            // on clicking search icon go to whole songs search page
//            case R.id.searchIconHome:
//            {
//                Pair[] pairs = new Pair[1];
//                pairs[0] = new Pair<View, String>(item.getActionView(), "searchTransition");
//
//                ActivityOptions activityOptions =
//                        ActivityOptions.makeSceneTransitionAnimation(this, pairs);
//
//                intent = new Intent(HomePage.this, FullSearch.class);
//                intent.putExtra("mode","off"); // the select mode is off now
//                startActivity(intent, activityOptions.toBundle());
//                overridePendingTransition(R.anim.zoom_in_search_bar_home, R.anim.static_animation);
//                return true;
//            }
//            default: return super.onOptionsItemSelected(item);
//        }
//
//    }
}