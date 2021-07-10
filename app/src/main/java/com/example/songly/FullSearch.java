package com.example.songly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FullSearch extends AppCompatActivity
    implements AdapterFullSearch.SongTitleClicked{


    private AdapterFullSearch adapter;
    private List<ModalFullSearch> fullListOfSongs; // to store full list of songs (from all categories)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_songs_whole);

        searchView = findViewById(R.id.searchWholeSongs);
        searchView.setIconified(false); // make the search view focused by default

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


        fillSongTitles(); // fill the list with all song's titles
        setUpRecyclerView();
    }

    private void fillSongTitles() {
        // setting up the malayalam font face from assets/font folder
        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/MLKR0NTT.TTF");

        fullListOfSongs = new ArrayList<>(); // create an arraylist
        String data = "";
        InputStream is = null;

        // open the entrance text from 'res/raw' folder
        is = this.getResources().openRawResource(R.raw.entrance);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        if(is != null)
        {
            // try to read each line from the file
            try
            {   // read each row from file
                while((data = bufferedReader.readLine()) != null)
                {
                    // split it into words by using comma as a delimiter
                    String[] splited = data.split("[,]", 0);

                    // now create new object of Modal class and add it to the list
                    fullListOfSongs.add(
                            new ModalFullSearch(
                            splited[0].trim(), // filename
                            splited[1].trim(), // eng title
                            splited[2].trim(), // malayalam title
                            splited[3].trim(), // folder name
                            typeface));
                }
                is.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.wholeListRecycler);

        // recycler view's layout manager - linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AdapterFullSearch(fullListOfSongs, this);

        recyclerView.setAdapter(adapter); // set recycler view's adapter
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override // upon clicking song title - this is a method from interface 'SongTitleClicked'
    public void songTitleClicked(int position) {
        // intent to go to the tabbed lyrics activity - to view the lyrics
        Intent intent = new Intent(getApplicationContext(), TabbedLyricsView.class);

        // pass the position of the song to Lyrics activity
        intent.putExtra("fileName", fullListOfSongs.get(position).getFileName());
        intent.putExtra("folderName", fullListOfSongs.get(position).getFolderName());

        // now invoke the activity
        startActivity(intent);
    }
}