package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullSearch extends AppCompatActivity
    implements AdapterFullSearch.SongTitleClicked{


    private AdapterFullSearch adapter;
    private List<ModalFullSearch> fullListOfSongs; // to store full list of songs (from all categories)
    SearchView searchView;
    ImageView tickMark;
    Intent intent;
    HelperClass helperClass;
    SharedPreferences sharedPreferences;
    Gson gson;
    SharedPreferences.Editor editor;
    String mode;

    public static List<ModalFullSearch> checkedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_songs_whole);

        searchView = findViewById(R.id.searchWholeSongs);
        tickMark = findViewById(R.id.tickMarkSelection);

        searchView.setIconified(false); // make the search view focused by default
//        searchView.requestFocus();
        helperClass = new HelperClass();

        intent = getIntent();
        mode = intent.getStringExtra("mode");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(mode.equals("off"))
                {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
//                    finish();
                }
                else
                {
                    intent = new Intent(getApplicationContext(), ViewIndividualList.class);
                    startActivity(intent);
//                    finish();
                }
                finish();
                return false;
            }
        });

        // if select songs mode is off then don't show the check boxes, else show 'em
        if(mode.equals("off"))
            tickMark.setVisibility(View.GONE);
        else
            tickMark.setVisibility(View.VISIBLE);

        checkedList = new ArrayList<>();
        // when some actions happens on search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override // upon search text change
            public boolean onQueryTextChange(String newText) {
                // filter the list using new search keyword
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        tickMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(getApplicationContext(), ViewIndividualList.class);
                sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                gson = new Gson();
//                Toast.makeText(FullSearch.this, Integer.toString(checkedList.size()), Toast.LENGTH_SHORT).show();
                String json = gson.toJson(checkedList);
                editor.putString("selected_songs", json);
                editor.putString("selected", "on");
                editor.apply();
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        fullListOfSongs = new ArrayList<>();
        fullListOfSongs.addAll( helperClass.fillSongTitles(this) ); // fill the list with all song's titles
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.wholeListRecycler);
        ArrayList<ModalFullSearch> storedList;
        String json;

        if(mode.equals("off"))
        {
            storedList = null;
        }
        else
        {
            sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
            gson = new Gson();
            json = sharedPreferences.getString("existing_songs", null);
            Type type = new TypeToken<ArrayList<ModalFullSearch>>(){}.getType();
            storedList = gson.fromJson(json, type);
        }


        if(storedList == null)
        {
//            Toast.makeText(this, "No data saved in sharedpreferences", Toast.LENGTH_SHORT).show();
            storedList = new ArrayList<>();
        }
        else
        {

            for(ModalFullSearch md: storedList)
            {
                for(int i = 0; i< fullListOfSongs.size(); i++)
                    if(fullListOfSongs.get(i).getEnglishTitle().equals(md.getEnglishTitle()))
                    {
                        fullListOfSongs.remove(i);
                        i--;
                    }
            }
        }
        // recycler view's layout manager - linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AdapterFullSearch(fullListOfSongs, this,
                mode, storedList, this);


        recyclerView.setAdapter(adapter); // set recycler view's adapter
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override // upon clicking song title - this is a method from interface 'SongTitleClicked'
    public void songTitleClicked(int position) {


        // intent to go to the tabbed lyrics activity - to view the lyrics
        Intent intent = new Intent(getApplicationContext(), TabbedLyricsView.class);

        Bundle bundle = new Bundle();

        // we are passing an array of strings to TabbedLyricsView
        bundle.putStringArray("contents", new String[]{
                fullListOfSongs.get(position).getFileName(),
                fullListOfSongs.get(position).getFolderName(),
                fullListOfSongs.get(position).getChord(),
                fullListOfSongs.get(position).getSong(),
                fullListOfSongs.get(position).getKaraoke()
        });
        intent.putExtras(bundle);


        // now invoke the activity
        startActivity(intent);
        finish();
    }
}