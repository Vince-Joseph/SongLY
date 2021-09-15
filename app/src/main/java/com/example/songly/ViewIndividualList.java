package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewIndividualList extends AppCompatActivity
        implements AdapterIndividualList.OnClickAction{

    private RecyclerView individualSetRecycler;
    List<ModalFullSearch> songNames;
    AdapterIndividualList adapter;
    ItemTouchHelper itemTouchHelper;
    ActionMode actionMode;
    final Activity activity = ViewIndividualList.this;
    BottomNavigationView navView;
    ImageView addIcon,  sortIcon, applyIcon;
    RelativeLayout relativeLayoutEmpty;
    TextView textViewEmpty, pageTitle;
    ImageView imageViewEmpty;
    Intent intent;
    int countOfLines = 0; // to store the number of lines read from the file

    int songsSortedInOrder = -1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    Gson gson;
    String json;
    String currentFileName;
    HelperClass helperClass;
    File requiredPath, requiredPathOfAppliedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_individual_list);
        sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
        currentFileName = sharedPreferences.getString("file_name", null);

//        Toast.makeText(this, currentFileName, Toast.LENGTH_SHORT).show();


// --------------------------- Toolbar has been replaced ------------------------------------

//        toolbar = findViewById(R.id.toolbar_lists);
//        setSupportActionBar(toolbar); // replace appbar with custome toolbar
//        toolbar.setTitle("File");
//        toolbar.inflateMenu(R.menu.bottom_nav_menu);
// ------------------------------------------------------------------------------------------


        sortIcon = findViewById(R.id.sortSongsIcon);
        applyIcon = findViewById(R.id.applyIcon);
        pageTitle = findViewById(R.id.pageTitle);
        navView = findViewById(R.id.nav_view);

        // changing default selection of bottom bar;
        navView.getMenu().findItem(R.id.navigation_list).setChecked(true);
        navView.getMenu().findItem(R.id.navigation_song).setChecked(false);
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(false);

        // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId();

                if (menuId == R.id.navigation_list) {
                    intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (menuId == R.id.navigation_song) {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                    finish();
                } else if (menuId == R.id.navigation_prayer) {
                    intent = new Intent(getApplicationContext(), PrayerWithTab.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

//------------------------------------------------------------------------------------------
        // this layout will be shown if there are no lists
        relativeLayoutEmpty = findViewById(R.id.relative_empty);
        textViewEmpty = findViewById(R.id.default_text);
        imageViewEmpty = findViewById(R.id.empty_icon);

        // nothing but using string resource value from @string/add_new_songs
        textViewEmpty.setText(getResources().getString(R.string.add_new_songs));
        imageViewEmpty.setImageResource(R.drawable.add_comment); // change the image icon
//--------------------------------------------------------------------------------------------

        // android/data/com.example.songly/files/lists
        requiredPath = getExternalFilesDir("lists");


        individualSetRecycler = findViewById(R.id.list_recycler);

        songNames = new ArrayList<>(); // to temp store the list names
        helperClass = new HelperClass();

        // to store full list of songs (from all categories)
        List<ModalFullSearch> fullListOfSongs = new ArrayList<>();

        checkPermission(); // check for storage permissions, each time activity get displayed
        fullListOfSongs.addAll( helperClass.fillSongTitles(this) ); // fill the list with all song's titles
        setUpRecycler();
        adapter.setActionModeReceiver((AdapterIndividualList.OnClickAction) activity);

        toggleDefaultText(); // toggle the view if there are no lists present


        addIcon = findViewById(R.id.addSongsIcon); // icon to add new song to the list
        // when clicked on new list icon, show the activity to search and add songs to the list
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Pair pair;
                pair = new Pair<View, String>(v, "searchTransition");

                ActivityOptions activityOptions =
                        ActivityOptions.makeSceneTransitionAnimation(ViewIndividualList.this, pair);

                // call the search activity
                intent = new Intent(getApplicationContext(), FullSearch.class);
                intent.putExtra("mode", "on");
                intent.putExtra("from", "ViewIndividualList");
                startActivity(intent, activityOptions.toBundle());
//                overridePendingTransition(R.anim.zoom_in, R.anim.static_animation);
                finish();
            }
        });

        // upon clicking sort song names icon, toggle ascending order sort and descending order sort
        sortIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(songNames.size() != 0)
                {
                    if(songsSortedInOrder == 1)
                    {
                        Collections.sort(songNames, Collections.reverseOrder());
                        songsSortedInOrder = 0;
                    }
                    else if(songsSortedInOrder == 0)
                    {
                        Collections.sort(songNames);
                        songsSortedInOrder = 1;
                    }
                    else
                    {
                        songsSortedInOrder = 1;
                        Collections.sort(songNames);
                    }

                    adapter.notifyDataSetChanged();

                    // android/data/com.example.songly/files/lists
                    File requiredPath = getExternalFilesDir("lists");
                    if(requiredPath != null)
                    {
                        File toWrite;
                        toWrite = new File(requiredPath, currentFileName);
                        try
                        {
                            countOfLines = 0;
                            FileWriter writeToFile = new FileWriter(toWrite);

                            // write sorted song names to file
                            for (ModalFullSearch md: songNames)
                            {
                                // startPage
                                // endPage
                                // chord
                                // song link
                                // karaoke link
                                writeToFile.write(
                                        md.getPageStart()+",\t\t\t"
                                                +md.getPageEnd()+",\t\t\t"
                                                +md.getEnglishTitle()+",\t\t\t"
                                                +md.getMalayalamTitle()+",\t\t\t"
                                                +md.getChord()+",\t\t\t"
                                                +md.getSong()+",\t\t\t"
                                                +md.getKaraoke()+"\n"
                                );

                                countOfLines++;
                            }
                            writeToFile.close();
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(getApplicationContext(), "IO exception", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No such directory", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        applyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songNames.size() == 0)
                    Toast.makeText(ViewIndividualList.this, "Empty list can't be applied !",
                            Toast.LENGTH_LONG).show();
                else
                {
//                    editor = sharedPreferences.edit();
//                    editor.putString("applied_list", currentFileName);
//                    editor.apply();
                    requiredPathOfAppliedList = getExternalFilesDir("appliedList");
                    if(requiredPathOfAppliedList != null)
                    {
                        if(requiredPathOfAppliedList.listFiles() == null) // folder has no files
                        {
                            Toast.makeText(ViewIndividualList.this, "No files present", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            File toBeWrite = new File(requiredPathOfAppliedList,
                                    "applied_list.txt");
                            FileWriter writeToFile = null;
                            try
                            {
                                boolean flag;
                                writeToFile = new FileWriter(toBeWrite);
                                for (int i = 0; i< songNames.size(); i++)
                                {

                                        writeToFile.write(
                                                songNames.get(i).getPageStart()+",\t\t\t"
                                                        +songNames.get(i).getPageEnd()+",\t\t\t"
                                                        +songNames.get(i).getEnglishTitle()+",\t\t\t"
                                                        +songNames.get(i).getMalayalamTitle()+",\t\t\t"
                                                        +songNames.get(i).getChord()+",\t\t\t"
                                                        +songNames.get(i).getSong()+",\t\t\t"
                                                        +songNames.get(i).getKaraoke()+"\n"
                                        );
                                }
                                writeToFile.close();

                                AlertDialog.Builder ab = new AlertDialog.Builder(ViewIndividualList.this);
                                View view = getLayoutInflater().inflate(R.layout.confirm_box_layout, null);
                                Button deleteBtn = (Button) view.findViewById(R.id.delete_button);
                                Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);
                                TextView mainTitle = view.findViewById(R.id.mainTitle);
                                TextView subTitle = view.findViewById(R.id.subTitle);
                                mainTitle.setText("Applied successfully");
                                subTitle.setText("This list has been applied to the prayer section");
                                cancelBtn.setVisibility(View.GONE);
                                deleteBtn.setText("Ok");
                                deleteBtn.setBackgroundColor(Color.GRAY);
                                ab.setView(view);
                                AlertDialog alert = ab.create();
                                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alert.show();// show the dialog box
                                deleteBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();
                                    }
                                });

                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        });

        itemTouchHelper = new ItemTouchHelper(handleDragDrop);
        itemTouchHelper.attachToRecyclerView(individualSetRecycler);
    }

    private void toggleDefaultText() {
        if(adapter.songNames.size() > 0) // if some lists are present then
        {
            individualSetRecycler.setVisibility(View.VISIBLE); // make the recycler view visible
            relativeLayoutEmpty.setVisibility(View.GONE); // empty content notification layout - gone
        }
        else
        {
            relativeLayoutEmpty.setVisibility(View.VISIBLE);//empty content notification layout- visible
            individualSetRecycler.setVisibility(View.GONE); // make the recycler view invisible
        }
    }

    // check storage permission
    private void checkPermission() {

        if(Build.VERSION.SDK_INT >= 23) // for Loypop or higher
        {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    // when request is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {

        if(requestCode == 1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                setUpRecycler(); // setup recycler view
            else
            {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setUpRecycler() {


        if(requiredPath != null)
        {

            intent = getIntent();
            File toBeRead;

            toBeRead = new File(requiredPath, currentFileName);

            FileReader fileReader = null; // reads the file
            try
            {
                fileReader = new FileReader(toBeRead);
                BufferedReader bufferedReader = new BufferedReader(fileReader); // creates buffer of chars
                String line;

                while ((line = bufferedReader.readLine())!=null)
                {

                    countOfLines++;
//                    Log.d("Here", "here counter executed");
                    String[] splited = line.split("[,]", 0);

                    // now create new object of Modal class and add it to the list
                    songNames.add(
                            new ModalFullSearch(
                                    splited[0], // page start
                                    splited[1].trim(), // page end
                                    splited[2].trim(), // eng title
                                    splited[3].trim(), // malayalam title
                                    splited[4].trim(), // song's chord
                                    splited[5].trim(), // song's song link
                                    splited[6].trim() // song's karaoke
                                     ));

                }
                fileReader.close();

                ArrayList<ModalFullSearch> storedList;

                if(sharedPreferences.getString("selected", null).equals("on"))
                {
                    gson = new Gson();
                    json = sharedPreferences.getString("selected_songs", null);
                    Type type = new TypeToken<ArrayList<ModalFullSearch>>(){}.getType();
                    storedList = gson.fromJson(json, type);

                    if(storedList != null)
                    {
//                        Toast.makeText(this, "sharedprefs", Toast.LENGTH_SHORT).show();
                        for (ModalFullSearch md: storedList)
                        {
//                            // this has to be checked 'cause we are calling setupRecycler from onStartActivity, not onCreate
//                            if(!(songNames.contains(md)))
//                            {
                                songNames.add(md);
                                FileWriter writeToFile = new FileWriter(toBeRead, true);
                                writeToFile.write(
                                        md.getPageStart()+",\t\t\t"
                                        +md.getPageEnd()+",\t\t\t"
                                        +md.getEnglishTitle()+",\t\t\t"
                                        +md.getMalayalamTitle()+",\t\t\t"
                                        +md.getChord()+",\t\t\t"
                                        +md.getSong()+",\t\t\t"
                                        +md.getKaraoke()+"\n"
                                );

                                writeToFile.close();
                                countOfLines++;
                        }
                    }
                    else
                        Toast.makeText(this, "stored list is null !!", Toast.LENGTH_SHORT).show();
                }
//                else
//                    Toast.makeText(this, "Note that storedlist is empty", Toast.LENGTH_LONG).show();


                editor = sharedPreferences.edit();
                editor.putString("selected_songs", "");
                editor.putString("selected", "off");
                editor.apply();
                if(countOfLines>0)
                {
//                  sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                    gson = new Gson();
                    json = gson.toJson(songNames);
                    editor.putString("existing_songs", json);
                }
                else
                {
                    editor.putString("existing_songs", "");
                }
                editor.apply();
            }
            catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "No such file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            catch (IOException e)
            {
                Toast.makeText(getApplicationContext(), "IO exception", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(this);
            individualSetRecycler.setLayoutManager(linearLayoutManager);

            adapter = new AdapterIndividualList(this , songNames);
            individualSetRecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            toggleDefaultText();
        }
        else
        {
            Toast.makeText(this, "No such directory", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_list).setChecked(true);
        super.onStart();
    }

    private void removeSongFromList() {

        ArrayList<ModalFullSearch> toBeDeleted = new ArrayList<>();
        toBeDeleted.addAll(adapter.getSelected());


        if(requiredPath != null)
        {
            if(requiredPath.listFiles() == null) // folder has no files
            {
                Toast.makeText(this, "No lists available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                File toBeWrite = new File(requiredPath, sharedPreferences.getString("file_name", null));

                FileWriter writeToFile = null;

                    try
                    {
                        boolean flag;
                        writeToFile = new FileWriter(toBeWrite);
                        for (int i = 0; i< songNames.size(); i++)
                        {
                            flag = true;
                            for (int j = 0; j< toBeDeleted.size(); j++)
                            {
                                if(songNames.get(i).getEnglishTitle().equals(
                                        toBeDeleted.get(j).getEnglishTitle()) )
                                {
                                   flag = false;
                                }

                            }
                            if(flag)
                            {
                                writeToFile.write(
                                        songNames.get(i).getPageStart()+",\t\t\t"
                                           +songNames.get(i).getPageEnd()+",\t\t\t"
                                           +songNames.get(i).getEnglishTitle()+",\t\t\t"
                                           +songNames.get(i).getMalayalamTitle()+",\t\t\t"
                                           +songNames.get(i).getChord()+",\t\t\t"
                                           +songNames.get(i).getSong()+",\t\t\t"
                                           +songNames.get(i).getKaraoke()+"\n"
                                );
                            }
                            else
                            {
                                songNames.remove(i);
                                countOfLines--;
                                i--;
                            }
                        }
                        writeToFile.close();
//                        adapter.songNames.notify();
                        adapter.deleteFileNames();
                        actionMode.finish();
                        actionMode = null;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
        else
            Toast.makeText(this, "No such directory", Toast.LENGTH_SHORT).show();
    }

    public void showConfirmDialog(ActionMode mode)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(ViewIndividualList.this);
        View view = getLayoutInflater().inflate(R.layout.confirm_box_layout, null);
        Button deleteBtn = (Button) view.findViewById(R.id.delete_button);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);

        ab.setView(view);
        AlertDialog alert = ab.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();// show the dialog box

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSongFromList();
                editor = sharedPreferences.edit();
                gson = new Gson();
                json = gson.toJson(songNames);
                editor.putString("existing_songs", json);
                editor.apply();
                mode.finish();
                alert.dismiss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode.finish();
                alert.dismiss();

            }
        });

    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.selection_menu, menu);
            MenuItem editItem = menu.findItem(R.id.edit_icon);
            editItem.setVisible(false); // make the edit icon invisible for this activity

//            MenuItem applyIcon = menu.findItem(R.id.apply_icon);
//            applyIcon.setVisible(false); // make the apply icon invisible for this activity

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            pageTitle.setVisibility(View.INVISIBLE);
            navView.setVisibility(View.GONE);
            sortIcon.setVisibility(View.INVISIBLE);
            addIcon.setVisibility(View.INVISIBLE);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.delete_icon: {
                    showConfirmDialog(mode);
                    return true;
                }
                case R.id.select_all:
                {
                    adapter.selectAll();
                    return true;
                }
                case R.id.deselect_all:
                {
                    adapter.clearAll(true);
                    actionMode.finish();
                    actionMode = null;
                    return true;
                }

                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            pageTitle.setVisibility(View.VISIBLE);
            actionMode = null;
            adapter.removeAllSelections();
            navView.setVisibility(View.VISIBLE);
            sortIcon.setVisibility(View.VISIBLE);
            addIcon.setVisibility(View.VISIBLE);
            toggleDefaultText();
        }
    };

    // handle the drag and drop functionality of the items

    final ItemTouchHelper.SimpleCallback handleDragDrop = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0
    ) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAbsoluteAdapterPosition();
            int toPosition = target.getAbsoluteAdapterPosition();

//            if(actionMode != null)
//            {
//                adapter.clearAll(true);
//                actionModeCallback.onDestroyActionMode(actionMode);
////                actionMode.finish();
//
//            }

            Collections.swap(songNames, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            // android/data/com.example.songly/files/lists
            File requiredPath = getExternalFilesDir("lists");
            if(requiredPath != null)
            {
                File toWrite;
                toWrite = new File(requiredPath, currentFileName);
                try
                {
                    countOfLines = 0;
                    FileWriter writeToFile = new FileWriter(toWrite);

                    // write song names to file
                    for (ModalFullSearch md: songNames)
                    {

                        writeToFile.write(
                                md.getPageStart()+",\t\t\t"
                                        +md.getPageEnd()+",\t\t\t"
                                        +md.getEnglishTitle()+",\t\t\t"
                                        +md.getMalayalamTitle()+",\t\t\t"
                                        +md.getChord()+",\t\t\t"
                                        +md.getSong()+",\t\t\t"
                                        +md.getKaraoke()+"\n"
                        );

                        countOfLines++;
                    }
                    writeToFile.close();
                }
                catch (IOException e)
                {
                    Toast.makeText(getApplicationContext(), "IO exception", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No such directory", Toast.LENGTH_SHORT).show();
            }



            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    public boolean onLongClickAction()
    {
        int selected = adapter.getSelected().size();
        if (actionMode == null && selected == 0)
        {
            actionMode = startActionMode(actionModeCallback);
            actionMode.setTitle("Selected: " + (selected+1));
            return true;
        }

        return false;

    }

    public boolean onClickAction() {
        int selected = adapter.getSelected().size();

        if( actionMode != null)
        {
            if (selected == 0)
                actionMode.finish();
            else
            {
                actionMode.setTitle("Selected: " + selected);

                if(selected > 1)
                {
                    // disable edit button
                }
            }
            return true;
        }
        return false;
    }
}