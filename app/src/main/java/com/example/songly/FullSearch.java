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
        helperClass = new HelperClass();

        intent = getIntent();
        mode = intent.getStringExtra("mode");

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
            }
        });
        fullListOfSongs = new ArrayList<>();
        fullListOfSongs.addAll( helperClass.fillSongTitles(this) ); // fill the list with all song's titles
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
        ArrayList<ModalFullSearch> storedList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
        gson = new Gson();
        String json = sharedPreferences.getString("existing_songs", null);
        Type type = new TypeToken<ArrayList<ModalFullSearch>>(){}.getType();
        storedList = gson.fromJson(json, type);

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
                mode,
                storedList, this);


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
        finish();
    }
}