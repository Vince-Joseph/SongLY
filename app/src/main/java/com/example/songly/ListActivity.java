package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.os.Environment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity lists all the user created lists.
 * Each of these lists are stored as individual files in the memory.
 * This activity required file permissions.
 */
public class ListActivity extends AppCompatActivity
        implements AdapterList.OnClickAction{

    private RecyclerView listSetRecycler; // recycler view for displaying list names
    List<String> listNames;
    AdapterList adapter;
    final File currentDir = Environment.getExternalStorageDirectory();
    File requiredPath;
    String newFileName;
    ActionMode actionMode;
    final Activity activity = ListActivity.this;
    BottomNavigationView navView;
    ImageView  imageViewEmpty,addIcon;
    TextView  pageTitle;
    RelativeLayout relativeLayoutEmpty;
    MenuItem editItem;
    Intent intent;
    SharedPreferences sharedPreferences; // sharedprefs to store some temp data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

// -------------------------- Toolbar has been replaced with custome icons -------------------------

//        toolbar = findViewById(R.id.toolbar_lists);
//        setSupportActionBar(toolbar); // replace appbar with custom toolbar


// ------------------ Bottom Navigation controls --------------------------------------

        navView = findViewById(R.id.nav_view);
        pageTitle = findViewById(R.id.pageTitle);
        // changing default selection of bottom bar
        navView.getMenu().findItem(R.id.navigation_list).setChecked(true);

        // Control the bottom navigation buttons
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int menuId = item.getItemId();


                if (menuId == R.id.navigation_song) {
                    intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else if (menuId == R.id.navigation_prayer) {
                    intent = new Intent(ListActivity.this, PrayerWithTab.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                return true;
            }
        });
// ------------------ Navigation controls ends here --------------------------------------


        // android/data/com.example.songly/files/lists
        requiredPath = getExternalFilesDir("lists"); // path where list names are stored


        // this layout will be shown if there are no lists present
//-------------------------------------------------------------------------
        relativeLayoutEmpty = findViewById(R.id.relative_empty);
        imageViewEmpty = findViewById(R.id.empty_icon);
        imageViewEmpty.setImageResource(R.drawable.empty_list);


//------------------------- Hide toolbar icons - replaced with custom icon----------------------------------
//        editIconToolbar = findViewById(R.id.editIconToolbar); // edit list name icon
//        sortIcon = findViewById(R.id.sortIcon); // sort songs icon(for list of songs activity)
//
//        // hide both of them initially.
//        sortIcon.setVisibility(View.GONE);
//        editIconToolbar.setVisibility(View.INVISIBLE);
//        ImageView searchIcon = findViewById(R.id.searchIcon);
//        searchIcon.setVisibility(View.GONE);
// ------------------------------------------------------------------------------------------------

        listSetRecycler = findViewById(R.id.list_recycler);
        listNames = new ArrayList<>(); // to temp store the list names
        newFileName = null;

        checkPermission(); // check for storage permissions
        setUpRecycler(); // setup the lists

        toggleDefaultText(); // toggle the empty list indicating view if there are no lists present

        adapter.setActionModeReceiver((AdapterList.OnClickAction) activity);

        addIcon = findViewById(R.id.addListIcon);// icon to add new list
        // when clicked on new list icon, show the custom alert box
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

    }

    private void toggleDefaultText() {
        if(adapter.listNames.size() > 0) // if some lists are present then
        {
            listSetRecycler.setVisibility(View.VISIBLE); // make the recycler view visible
            relativeLayoutEmpty.setVisibility(View.GONE); // empty content notification layout - gone
        }
        else
        {
            relativeLayoutEmpty.setVisibility(View.VISIBLE);//empty content notification layout- visible
            listSetRecycler.setVisibility(View.GONE); // make the recycler view invisible
        }
    }

    // check storage permission
    private void checkPermission() {

        // if permission is not granted then ask for it
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                // return back to home screen
                intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                finish(); // we are finishing the activity to pop it out from the stack such that
                // at a later stage, when we open the page, we get updated items, not some old views

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // setup the list recycler view with existing lists
    private void setUpRecycler() {

        if(requiredPath != null)
        {
            for (File f: requiredPath.listFiles()) // get all files from the 'lists' directory
                if(f.isFile()) // if current item is a file then
                {
                    String fname = f.getName(); // get its name

                 // ----------  remove the .txt string from filename ----------
                    int index = fname.lastIndexOf('.');
                    fname = fname.substring(0, index);
                 //-------------------------------------------------------------
                    listNames.add(fname); // add each filename to arraylist
                }


// ------------------------ Gridlayout has been replaced with linear layout ------------------------

              // set the layout manager of list recycler view
//                GridLayoutManager gridLayoutManager =
//                        new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL,
//                                false);
// ------------------------------------------------------------------------------------------------


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                listSetRecycler.setLayoutManager(linearLayoutManager);

                // set the adapter
                adapter = new AdapterList(this , listNames);
                listSetRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                toggleDefaultText();
        }
        else
        {
            Toast.makeText(this, "No such directory", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Every time ListActivity gets displayed, check the navigation button of bottom bar
     */
    @Override
    protected void onStart() {
        navView.getMenu().findItem(R.id.navigation_list).setChecked(true);
        super.onStart();
    }

    // show alert for creation of new list
    public void showAlert()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(ListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.custome_alert_dialog, null);
        EditText newListName = (EditText) view.findViewById(R.id.new_list_name);
        Button createBtn = (Button) view.findViewById(R.id.create_button);

        ab.setView(view);
        AlertDialog alert = ab.create();

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();// show the dialog box

        // upon clicking create button
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if provided new list name is not empty then
                if(!(newListName.getText().toString().isEmpty()))
                {
                    newFileName = newListName.getText().toString();
                    if(currentDir.listFiles() == null)
                        alert.dismiss();
                    else
                    {
                        // check for duplicate file names
                        for (File f: currentDir.listFiles()) // get all files from the 'lists' dierctory
                            if(f.isFile() && newFileName.equals(f.getName()))
                            {
                                Toast.makeText(ListActivity.this, "List already present !", Toast.LENGTH_LONG).show();
                                alert.dismiss();
                                return; // return back
                            }
                        alert.dismiss();
                    }
                    createFile(newFileName); // create new file with given new name
                }
            }
        });
    }

    // show confirmation dialog upon deleting a list
    public void showConfirmDialog(ActionMode mode)
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(ListActivity.this);
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
                removeFile(); // call the method to remove the list
                alert.dismiss();
                mode.finish(); // finish action mode
                actionMode = null;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                mode.finish(); // finish action mode
                actionMode = null;
            }
        });
    }

    // method to remove specified list
    private void removeFile() {
        ArrayList<String> toBeDeleted = new ArrayList<>();
        toBeDeleted.addAll(adapter.getSelected()); // get all selected lists for deletion

        if(requiredPath != null)
        {
            boolean flag = false;
            if(requiredPath.listFiles() == null) // folder has no files
            {
                Toast.makeText(this, "No lists available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // for each list name to be deleted, delete corresponding file
               for(String fileName: toBeDeleted)
               {
                    File fdelete = new File(requiredPath.getPath()+"/"+fileName+".txt");
                    if(fdelete.exists()) // if file exists then
                    {
                        fdelete.delete(); // delete the file
                        flag = true;
                    }
               }

               if(flag)
                  Toast.makeText(activity, "Lists(s) deleted", Toast.LENGTH_SHORT).show();

             }
            adapter.deleteFileNames(); // remove the deleted list names from full list
        }
        else
            Toast.makeText(this, "No such directory", Toast.LENGTH_SHORT).show();
    }

    private void createFile(String newFileName) {
        newFileName.trim();
        File newFile = new File(currentDir, newFileName+".txt");

        try
        {
            // check for duplication of list
            File someFile = new File(getExternalFilesDir("lists"), newFileName+".txt");
            if(someFile.exists())
            {
                Toast.makeText(this, "List is already present", Toast.LENGTH_LONG).show();

            }
            else // if no duplication then
            {
                someFile.createNewFile();
                listNames.add(newFileName); // add new list name to existing listnames arraylist
                toggleDefaultText(); // toggle empty screen if necessary

                // save new list name to shared preferences
                sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                sharedPreferences.edit().putString("file_name", newFileName+".txt").apply();

            }
        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, newFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }


//--------------------------The action mode ---------------------------------------

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            // upon action mode creation, display the action mode menu on action bar
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.selection_menu, menu);
            editItem = menu.findItem(R.id.edit_icon);
            navView.setVisibility(View.GONE); // now the bottom nav bar is invisible
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            addIcon.setVisibility(View.INVISIBLE);
            pageTitle.setVisibility(View.INVISIBLE);
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int itemId = item.getItemId();
            if (itemId == R.id.delete_icon) {  // upon clicking delete icon of action mode menu
                showConfirmDialog(mode);
                return true;
            } else if (itemId == R.id.select_all) {
                adapter.selectAll();
                mode.setTitle("Selected: " + adapter.listNames.size());
                return true;
            } else if (itemId == R.id.deselect_all) {
                adapter.clearAll(true);
                actionMode.finish(); // finish the action mode after de-selecting all songs
                actionMode = null;
                return true;
            } else if (itemId == R.id.edit_icon) {
                renameFileTo(adapter.getSelected().get(0));
                mode.finish(); // finish the action mode after editing a list name
                actionMode = null;
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.removeAllSelections();
            navView.setVisibility(View.VISIBLE); // bring back the bottom nav bar
            addIcon.setVisibility(View.VISIBLE);
            pageTitle.setVisibility(View.VISIBLE);
            toggleDefaultText(); // toggle empty view
        }
    };

    // upon long pressing a list, start the action mode
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
            if (selected == 0) // when selected items count=0, finish action mode
                actionMode.finish();
            else
            {
                actionMode.setTitle("Selected: " + selected); // else update count of selected list items

                // toggle edit button
                if(selected > 1)  editItem.setVisible(false);
                else editItem.setVisible(true);
            }

            return true;
        }
        return false;
    }

//-------------------------- ACTION MODE completes here ------------------------------

    private void renameFileTo(String currentName) {

        AlertDialog.Builder ab = new AlertDialog.Builder(ListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.rename_box, null);
        EditText newListName = (EditText) view.findViewById(R.id.new_list_name);
        Button renameBtn = (Button) view.findViewById(R.id.rename_button);

        ab.setView(view);
        AlertDialog alert = ab.create();

        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();// show the dialog box

        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(newListName.getText().toString().isEmpty()))
                {
                    String newFileName = newListName.getText().toString();

                    if(requiredPath != null)
                    {
                        File from = new File(requiredPath, currentName+".txt"); // old name
                        File to = new File(requiredPath, newFileName+".txt");
                        from.renameTo(to);

                        adapter.updateNames(currentName, newFileName); // update list name in arraylist
                    }
                    else
                        Toast.makeText(ListActivity.this, "No such directory", Toast.LENGTH_SHORT).show();

                    alert.dismiss();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}