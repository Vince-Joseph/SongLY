package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView; // recycler for holding the folders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_activity);

        recyclerView = findViewById(R.id.homeScreenRecycler);
        ArrayList<String> folderNames = new ArrayList<>(); // folder-names arraylist
        folderNames.add("Entrance");
        folderNames.add("Pslam");
        folderNames.add("Gospel");
        folderNames.add("Offering");
        folderNames.add("Osana");
        folderNames.add("Elavation");
        folderNames.add("Communion");
        folderNames.add("Others");

        // adapter object
        AdapterHomePage adapterHomePage = new AdapterHomePage(this, folderNames);
        // layout of recycler
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(adapterHomePage); // set the adapter for recycler


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // changing default selection of bottom bar
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int menuId = item.getItemId();
        Intent intent;

        switch(menuId)
        {
            // no comparison for homepage because we are at home page

            case R.id.navigation_list:{
                intent = new Intent(HomePage.this, ListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.navigation_prayer:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); // search menu inflation
        inflater.inflate(R.menu.search_menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId())
        {
            // on clicking search icon go to whole songs search page
            case R.id.searchIconHome:
            {
                i = new Intent(HomePage.this, FullSearch.class);
                i.putExtra("mode","off");
                startActivity(i);
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }

    }
}