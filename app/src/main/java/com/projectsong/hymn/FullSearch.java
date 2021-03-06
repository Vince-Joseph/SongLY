package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity is intended to display the whole list of songs available from all categories.
 * This activity has two modes.
 * 1. User can search the entire list of songs for a particular song and click on it to view its lyrics
 * 2. User can select songs and press the tick mark to add the selected songs to a list
 */
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
    String mode, from;

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
        from = intent.getStringExtra("from");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(mode.equals("off"))
                {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                }
                else
                {
                    if(from.equals("ViewIndividualList"))
                        intent = new Intent(getApplicationContext(), ViewIndividualList.class);
                    else
                        intent = new Intent(getApplicationContext(), PrayerWithTab.class);
                }
                startActivity(intent);
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
                if(from.equals("ViewIndividualList"))
                    intent = new Intent(getApplicationContext(), ViewIndividualList.class);
                else
                    intent = new Intent(getApplicationContext(), PrayerWithTab.class);

                sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                gson = new Gson();
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
                mode, this);


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
                fullListOfSongs.get(position).getPageStart(),
                fullListOfSongs.get(position).getPageEnd(),
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