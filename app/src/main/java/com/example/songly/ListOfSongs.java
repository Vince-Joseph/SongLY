package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This Activity displays the list of songs belonging to a particular category.
 * The search functionality will search for a particular song of that particular category.
 * We've used recycler view to display the list of songs.
 * Upon clicking individual song, the user will be directed to corresponding lyrics.
 *
 */
public class ListOfSongs extends AppCompatActivity  implements
        Adapter.SongClickInterface{

    RecyclerView recyclerView; // to contain the list of songs
    LinearLayoutManager layoutManager;
    // we are re-using ModalFullSearch instead of creating new Modal class
    List<ModalFullSearch> songsList; // holds list of all songs in this particular category read from file
    Adapter adapter;
    ImageView searchViewButton;
    TextView pageTitle;
    BottomNavigationView navView;
    SearchView searchCategory;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

        searchViewButton = findViewById(R.id.searchIcon); // search button
        pageTitle = findViewById(R.id.pageTitle); // page title (hides when search is clicked)

        searchCategory = findViewById(R.id.searchCategory); // (hidden searchView - search bar)

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_list, R.id.navigation_song, R.id.navigation_prayer)
                .build();

        // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId();
                if (menuId == R.id.navigation_song) {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                } else if (menuId == R.id.navigation_list) {
                    intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                    // control the transition animations
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                } else if (menuId == R.id.navigation_prayer) {
                    startActivity(new Intent(getApplicationContext(), PrayerWithTab.class));
                }
                return true;
            }
        });

        // upon search query text change
        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener()
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

        // upon closing the searchView, hide it and display other icons and page title
        searchCategory.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                pageTitle.startAnimation(AnimationUtils.loadAnimation(searchCategory.getContext(),
                        R.anim.slide_from_left));
                searchViewButton.startAnimation(AnimationUtils.loadAnimation(searchCategory.getContext(),
                        R.anim.slide_from_left));
                searchCategory.setVisibility(View.GONE);
                searchViewButton.setVisibility(View.VISIBLE);
                pageTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });

        // display the search bar and hide other things with some transitions
        searchViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchCategory.startAnimation(AnimationUtils.loadAnimation(searchCategory.getContext(),
                        R.anim.slide_from_right));
                searchCategory.setIconified(false);
                searchCategory.setVisibility(View.VISIBLE);
                searchViewButton.setVisibility(View.GONE);

                pageTitle.startAnimation(AnimationUtils.loadAnimation(searchCategory.getContext(),
                        R.anim.slide_to_left));
                pageTitle.setVisibility(View.GONE);
            }
        });

        // receive the respective filename from which list of songs of that particular category have to be read
        intent = getIntent();
        String fileName = intent.getStringExtra("fileName");
        pageTitle.setText(fileName); // set the title first

        // then convert the fileName to all lower cases
        fileName = fileName.toLowerCase();
        initData(fileName);
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);
        super.onStart();
    }

    // setting up the recycler view
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.songListRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new Adapter(songsList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initData(String fileName) {
        songsList = new ArrayList<>();

        String data = "";
        InputStream is = null;

        // find respective filename in res/raw folder
        if(fileName.equals("entrance"))
            is = this.getResources().openRawResource(R.raw.entrance);
        else if(fileName.equals("psalms"))
            is = this.getResources().openRawResource(R.raw.psalms);
        else if(fileName.equals("gospel"))
            is = this.getResources().openRawResource(R.raw.gospel);
        else if(fileName.equals("offering"))
            is = this.getResources().openRawResource(R.raw.offering);
        else if(fileName.equals("osana"))
            is = this.getResources().openRawResource(R.raw.osana);
        else if(fileName.equals("adoration"))
            is = this.getResources().openRawResource(R.raw.adoration);
        else if(fileName.equals("communion"))
            is = this.getResources().openRawResource(R.raw.communion);
        else if(fileName.equals("others"))
            is = this.getResources().openRawResource(R.raw.others);


        if(is != null)
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            try {
                    // read each song name from the file and store it in the list as an obj
                    while((data = bufferedReader.readLine()) != null)
                    {
                        String[] splited = data.split("[,]", 0);
                        songsList.add(new ModalFullSearch(
                                splited[0], // page start
                                splited[1].trim(), // page end
                                splited[2].trim(), // eng title
                                splited[3].trim(), // malayalam title
                                splited[4].trim(), // song's chord
                                splited[5].trim(), // song's song link
                                splited[6].trim() // song's karaoke
                                ));
                    } // close while
                    is.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        } // close of if
    }

    @Override
    public void songClicked(int position) {
        // intent is used to switch activities
        intent = new Intent(getApplicationContext(), TabbedLyricsView.class);
        Bundle bundle = new Bundle();

        // we are passing an array of strings to TabbedLyricsView
        bundle.putStringArray("contents", new String[]{
                songsList.get(position).getPageStart(),
                songsList.get(position).getPageEnd(),
                songsList.get(position).getChord(),
                songsList.get(position).getSong(),
                songsList.get(position).getKaraoke()
        });
        intent.putExtras(bundle);

       // now invoke the intent
        startActivity(intent);
    }

// -------------------------- Toolbar menu is commented and replaced with cutome layouts ----------------------

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu); // inflating menu/search_menu.xml
//
//        MenuItem searchItem = menu.findItem(R.id.searchIcon); // finding search icon
//
//        searchView = (SearchView) searchItem.getActionView();
//        searchView.setIconifiedByDefault(false);
//        searchView.requestFocus();
////        searchView.setFocusable(true);
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE); // change the search icon in keyboard
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//        return true;
//    }

}