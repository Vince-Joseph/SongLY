package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
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

public class ListOfSongs extends AppCompatActivity  implements
        Adapter.SongClickInterface{

//1, 3, altharayil anuthapamode,
// AÄ¯mcbnÂ AëXm]tamsS AWntNÀ¶nSmw,
// Fm,
// https://www.youtube.com/watch?v=hr6jrLUYnh0,	https://www.youtube.com/watch?v=c3bh-0XYV6o

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClassSongList> songsList;
    Adapter adapter;
    ImageView searchViewButton;
    TextView pageTitle;
    Typeface typeface;
    BottomNavigationView navView;
    SearchView searchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

        searchViewButton = findViewById(R.id.searchIcon);

        pageTitle = findViewById(R.id.pageTitle);
        searchCategory = findViewById(R.id.searchCategory);
        typeface = Typeface.createFromAsset(getAssets(),"font/MLKR0NTT.TTF");

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
                Intent intent;
                if (menuId == R.id.navigation_song) {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                } else if (menuId == R.id.navigation_list) {
                    intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                } else if (menuId == R.id.navigation_prayer) {
                    intent = new Intent(getApplicationContext(), PrayerWithTab.class);
                    startActivity(intent);
                }
                return true;
            }
        });

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

//                pageTitle.animate()
//                        .translationX(-100)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation, boolean isReverse) {
//                                pageTitle.setVisibility(View.GONE);
//                            }
//                        });


            }
        });


        Intent intent = getIntent();
        String fileName = intent.getStringExtra("folder");
        String activityTitle = fileName;
        fileName = fileName.toLowerCase();

        pageTitle.setText(activityTitle);
        initData(fileName);
        initRecyclerView();
    }


    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_song).setChecked(true);
        super.onStart();
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
                                splited[0], // page start
                                splited[1].trim(), // page end
                                splited[2].trim(), // eng title
                                splited[3].trim(), // malayalam title
                                splited[4].trim(), // song's chord
                                splited[5].trim(), // song's song link
                                splited[6].trim() // song's karaoke
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
                songsList.get(position).getStartPage(),
                songsList.get(position).getEndPage(),
                songsList.get(position).getChord(),
                songsList.get(position).getSong(),
                songsList.get(position).getKaraoke()
        });
        intent.putExtras(bundle);

//        searchView.setQuery("", false);
//        searchView.clearFocus();
//        searchView.setIconified(true);

//        // now invoke the intent
        startActivity(intent);
//        closeSearchAction(searchText);
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