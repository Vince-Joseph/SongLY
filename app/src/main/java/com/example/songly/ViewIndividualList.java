package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ViewIndividualList extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        AdapterIndividualList.OnClickAction{

    private RecyclerView individualSetRecycler;
    List<ModalFullSearch> songNames;
    AdapterIndividualList adapter;
    ActionMode actionMode;
    Activity activity = ViewIndividualList.this;
    BottomNavigationView navView;
    Toolbar toolbar;
    ImageView addIcon;
    RelativeLayout relativeLayoutEmpty;
    TextView textViewEmpty;
    ImageView imageViewEmpty;
    Intent intent;
    int countOfLines = 0; // to store the number of lines read from the file

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;
    Gson gson;
    String json;

    String currentFileName;
    private List<ModalFullSearch> fullListOfSongs; // to store full list of songs (from all categories)
    HelperClass helperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_individual_list);
        sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
        currentFileName = sharedPreferences.getString("file_name", null);

//        Toast.makeText(this, currentFileName, Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar_lists);
        setSupportActionBar(toolbar); // replace appbar with custome toolbar
        toolbar.setTitle("File");
        ImageView editIcon = findViewById(R.id.editIconToolbar);
        editIcon.setVisibility(View.INVISIBLE);

        navView = findViewById(R.id.nav_view);

        // changing default selection of bottom bar
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().findItem(R.id.navigation_list).setChecked(false);
        navView.getMenu().findItem(R.id.navigation_song).setChecked(false);
        navView.getMenu().findItem(R.id.navigation_prayer).setChecked(false);

        // this layout will be shown if there are no lists
        relativeLayoutEmpty = findViewById(R.id.relative_empty);
        textViewEmpty = findViewById(R.id.default_text);
        imageViewEmpty = findViewById(R.id.empty_icon);
        textViewEmpty.setText("Add some songs");
        imageViewEmpty.setImageResource(R.drawable.add_comment); // change the image icon

        individualSetRecycler = findViewById(R.id.list_recycler);

        songNames = new ArrayList<>(); // to temp store the list names
        helperClass = new HelperClass();

        checkPermission(); // check for storage permissions
        fullListOfSongs = new ArrayList<>();
        fullListOfSongs.addAll( helperClass.fillSongTitles(this) ); // fill the list with all song's titles
        setUpRecycler();

        toggleDefaultText(); // toggle the view if there are no lists present

        adapter.setActionModeReceiver((AdapterIndividualList.OnClickAction) activity);

        addIcon = findViewById(R.id.addIcon); // icon to add new song to the list
        // when clicked on new list icon, show the activity to search and add songs to the list
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the search activity
                intent = new Intent(getApplicationContext(), FullSearch.class);
                intent.putExtra("mode", "on");
                startActivity(intent);
            }
        });

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

        // android/data/com.example.songly/files/lists
        File requiredPath = getExternalFilesDir("lists");

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
                                    splited[0].trim(), // filename
                                    splited[1].trim(), // eng title
                                    splited[2].trim(), // malayalam title
                                    splited[3].trim() // folder name
                                     ));

                }
                fileReader.close();

                ArrayList<ModalFullSearch> storedList = new ArrayList<>();

                if(sharedPreferences.getString("selected", null).equals("on"))
                {
                    gson = new Gson();
                    json = sharedPreferences.getString("selected_songs", null);
                    Type type = new TypeToken<ArrayList<ModalFullSearch>>(){}.getType();
                    storedList = gson.fromJson(json, type);

                    if(storedList != null)
                    {
//                        Toast.makeText(this, "haredprefs", Toast.LENGTH_SHORT).show();
                        for (ModalFullSearch md: storedList)
                        {
                            songNames.add(md);
                            FileWriter writeToFile = new FileWriter(toBeRead, true);
                            writeToFile.write(
                                    md.getFileName()+",\t\t\t"
                                    +md.getEnglishTitle()+",\t\t\t"
                                    +md.getMalayalamTitle()+",\t\t\t"
                                    +md.getFolderName()+"\n"
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


                if(countOfLines>0)
                {
//                  sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    gson = new Gson();
                    json = gson.toJson(songNames);
                    editor.putString("existing_songs", json);
                    editor.apply();
                }
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

//            Log.d("value of counter", Integer.toString(countOfLines));
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

    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int menuId = item.getItemId();
        Intent i = null;

        switch(menuId)
        {
            case R.id.navigation_list:
            {
                i = new Intent(getApplicationContext(),ListActivity.class);
                startActivity(i);
                break;
            }
            case R.id.navigation_song:
            {
                i = new Intent(getApplicationContext(),HomePage.class);
                startActivity(i);
                break;
            }
            case R.id.navigation_prayer:
//                Toast.makeText(this, "Prayer button", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void removeSongFromList() {

        ArrayList<ModalFullSearch> toBeDeleted = new ArrayList<>();
        toBeDeleted.addAll(adapter.getSelected());

        // android/data/com.example.songly/files/lists
        File requiredPath = getExternalFilesDir("lists");

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
                                        songNames.get(i).getFileName()+",\t\t\t"
                                                +songNames.get(i).getEnglishTitle()+",\t\t\t"
                                                +songNames.get(i).getMalayalamTitle()+",\t\t\t"
                                                +songNames.get(i).getFolderName()+"\n"
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

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.selection_menu, menu);
            MenuItem editItem = menu.findItem(R.id.edit_icon);
            editItem.setVisible(false); // make the edit icon invisible
            navView.setVisibility(View.GONE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
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
            actionMode = null;
            adapter.removeAllSelections();
            navView.setVisibility(View.VISIBLE);
            toggleDefaultText();
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