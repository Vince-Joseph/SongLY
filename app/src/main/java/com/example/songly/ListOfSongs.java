package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListOfSongs extends AppCompatActivity  implements
        BottomNavigationView.OnNavigationItemSelectedListener
        , Adapter.SongClickInterface{


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClassSongList> songsList;
    Adapter adapter;
    SearchView searchView;
    Typeface typeface;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

//        searchView = findViewById(R.id.searchIcon);

        typeface = Typeface.createFromAsset(getAssets(),"font/MLKR0NTT.TTF");

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list, R.id.navigation_song, R.id.navigation_prayer)
                .build();

        // changing default selection of bottom bar
        navView.setOnNavigationItemSelectedListener(this);


        Intent intent = getIntent();
        String fileName = intent.getStringExtra("folder");
        String activityTitle = fileName;
        fileName = fileName.toLowerCase();

        setTitle(activityTitle);
        initData(fileName);
        initRecyclerView();


    }


    @Override
    // handle the bottom navigation item click event
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int menuId = item.getItemId();
        Intent intent;
        switch(menuId)
        {
            case R.id.navigation_song: {
                intent = new Intent(getApplicationContext(), HomePage.class);
                searchView.setQuery("", false);
                searchView.clearFocus();
//                searchView.setIconified(true);
                startActivity(intent);
                break;
            }

            case R.id.navigation_list:{
                intent = new Intent(getApplicationContext(), ListActivity.class);
                searchView.setQuery("", false);
                searchView.clearFocus();
//                searchView.setIconified(true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
            }
            case R.id.navigation_prayer:
            {
                intent = new Intent(getApplicationContext(), PrayerActivity.class);
                searchView.setQuery("", false);
                searchView.clearFocus();
//                searchView.setIconified(true);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);
        super.onStart();
    }

    @Override
    public void onBackPressed() {
//        temp_intent = new Intent(getApplicationContext(), HomePage.class);
//        temp_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(temp_intent);
//        ListOfSongs.this.finish();
        super.onBackPressed();
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.songListRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Adapter(songsList, this, typeface);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initData(String fileName) {
        songsList = new ArrayList<>();

        String data = "";
        InputStream is = null;



        if(fileName.equals("entrance"))
            is = this.getResources().openRawResource(R.raw.entrance);
        else if(fileName.equals("pslam"))
            is = this.getResources().openRawResource(R.raw.pslam);
        else if(fileName.equals("gospel"))
            is = this.getResources().openRawResource(R.raw.gospel);
        else if(fileName.equals("offering"))
            is = this.getResources().openRawResource(R.raw.offering);
        else if(fileName.equals("osana"))
            is = this.getResources().openRawResource(R.raw.osana);
        else if(fileName.equals("elavation"))
            is = this.getResources().openRawResource(R.raw.elavation);
        else if(fileName.equals("communion"))
            is = this.getResources().openRawResource(R.raw.communion);
        else if(fileName.equals("others"))
            is = this.getResources().openRawResource(R.raw.others);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        if(is != null)
        {

            try {

                    while((data = bufferedReader.readLine()) != null)
                    {
                        String[] splited = data.split("[,]", 0);
                        songsList.add(new ModelClassSongList(
                                splited[0], // filename
                                splited[2].trim(), // mal title
                                splited[1].trim(), // eng title
                                splited[3].trim(), // song's folder
                                splited[4].trim(), // song's album
                                splited[5].trim(), // song's singers
                                splited[6].trim(), // song's year
                                splited[7].trim() // song's chord
                                ));

                    }
                    is.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void songClicked(int position) {
        // intent is used to switch activities
//        Intent intent = new Intent(getApplicationContext(), ViewLyrics.class);
        Intent intent = new Intent(getApplicationContext(), TabbedLyricsView.class);
        Bundle bundle = new Bundle();

        // we are passing an array of strings to TabbedLyricsView
        bundle.putStringArray("contents", new String[]{
                songsList.get(position).getFileName(),
                songsList.get(position).getFolderName(),
                songsList.get(position).getAlbum(),
                songsList.get(position).getSingers(),
                songsList.get(position).getYear(),
                songsList.get(position).getChord()
        });
        intent.putExtras(bundle);

        searchView.setQuery("", false);
        searchView.clearFocus();
//        searchView.setIconified(true);

//        // now invoke the intent
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu); // inflating menu/search_menu.xml

        MenuItem searchItem = menu.findItem(R.id.searchIcon); // finding search icon

        searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
//        searchView.setFocusable(true);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); // change the search icon in keyboard

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

}