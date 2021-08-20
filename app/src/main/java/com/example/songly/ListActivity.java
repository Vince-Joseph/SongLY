package com.example.songly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        AdapterList.OnClickAction{

    private RecyclerView listSetRecycler;
    List<String> listNames;
    AdapterList adapter;
    File currentDir = Environment.getExternalStorageDirectory();
    String newFileName;
    ActionMode actionMode;
    Activity activity = ListActivity.this;
    BottomNavigationView navView;
    Toolbar toolbar;
    ImageView editIconToolbar;
    ImageView addIcon;
    RelativeLayout relativeLayoutEmpty;
    TextView textViewEmpty;
    ImageView imageViewEmpty, sortIcon;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        toolbar = findViewById(R.id.toolbar_lists);
        setSupportActionBar(toolbar); // replace appbar with custome toolbar

        navView = findViewById(R.id.nav_view);
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_list, R.id.navigation_song, R.id.navigation_prayer)
//                .build();

        // changing default selection of bottom bar
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().findItem(R.id.navigation_list).setChecked(true);

        // this layout will be shown if there are no lists
        relativeLayoutEmpty = findViewById(R.id.relative_empty);
        textViewEmpty = findViewById(R.id.default_text);
        imageViewEmpty = findViewById(R.id.empty_icon);
        textViewEmpty.setText("No lists available, create new one");
        imageViewEmpty.setImageResource(R.drawable.empty_list);

        editIconToolbar = findViewById(R.id.editIconToolbar);
        sortIcon = findViewById(R.id.sortIcon);

        sortIcon.setVisibility(View.GONE);
        editIconToolbar.setVisibility(View.INVISIBLE);

        listSetRecycler = findViewById(R.id.list_recycler);

        listNames = new ArrayList<>(); // to temp store the list names
        newFileName = null;

        checkPermission(); // check for storage permissions
        setUpRecycler();

        toggleDefaultText(); // toggle the view if there are no lists present

        adapter.setActionModeReceiver((AdapterList.OnClickAction) activity);

        addIcon = findViewById(R.id.addIcon); // icon to add new list
        // when clicked on new list icon, show the custome alert box
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
                finish(); // we are finishing the activity to pop it out from the stack such that
                // at a later stage, when we open the page, we get updated items, not some old views

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setUpRecycler() {

        // android/data/com.example.songly/files/lists
        File requiredPath = getExternalFilesDir("lists");

        if(requiredPath != null)
        {
                for (File f: requiredPath.listFiles()) // get all files from the 'lists' dierctory
                    if(f.isFile())
                    {
                        String fname = f.getName();

                        // remove the .txt string from filename
                        int index = fname.lastIndexOf('.');
                        fname = fname.substring(0, index);

                        listNames.add(fname); // add each filename to arraylist
                    }


                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL,
                                false);
                listSetRecycler.setLayoutManager(gridLayoutManager);

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

    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        int menuId = item.getItemId();
        Intent i = null;

        switch(menuId)
        {
            case R.id.navigation_song:
            {
                i = new Intent(getApplicationContext(),HomePage.class);
                startActivity(i);
                finish();

                break;
            }
            case R.id.navigation_prayer:
//                Toast.makeText(this, "Prayer button", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        deleteIcon.setVisibility(View.VISIBLE);
//        addIcon.setVisibility(View.GONE);
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.list_menu, menu); // inflating menu/list_menu.xml
//
////        MenuItem addNewList = menu.findItem(R.id.plus_icon); // finding add icon
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////        Intent i;
//        switch (item.getItemId())
//        {
//            case R.id.plus_icon:
//            {
//                showAlert();
////                Toast.makeText(this, newFileName == null?"null":newFileName, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            default: return super.onOptionsItemSelected(item);
//        }
////        return  super.onOptionsItemSelected(item);
//
//    }

    public void showAlert()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(ListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.custome_alert_dialog, null);
        EditText newListName = (EditText) view.findViewById(R.id.new_list_name);
        Button createBtn = (Button) view.findViewById(R.id.create_button);
//        Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);

        ab.setView(view);
        AlertDialog alert = ab.create();

        //
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();// show the dialog box

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(newListName.getText().toString().isEmpty()))
                {
                    newFileName = newListName.getText().toString();
                    if(currentDir.listFiles() == null) {
                        alert.dismiss();
                        createFile(newFileName);
                        return;
                    }
                    else
                    {
                        for (File f: currentDir.listFiles()) // get all files from the 'lists' dierctory
                            if(f.isFile() && newFileName.equals(f.getName()))
                            {
                                Toast.makeText(ListActivity.this, "List already present !", Toast.LENGTH_LONG).show();
                                alert.dismiss();
                                return;
                            }

                        alert.dismiss();
                        createFile(newFileName);
//                        finish();
//                        overridePendingTransition(0, 0);
//                        startActivity(getIntent());
//                        overridePendingTransition(0, 0);
//                        setUpRecycler();
                    }

                }
            }
        });
//        ab.setTitle("Enter new list name");
//
//        EditText editText = new EditText(ListActivity.this);
//        ab.setView(editText);
//
//        ab.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(ListActivity.this, editText.getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(ListActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

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
                removeFile();
                alert.dismiss();
                mode.finish();
                actionMode = null;
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                mode.finish();
                actionMode = null;
            }
        });

    }

    private void removeFile() {
        ArrayList<String> toBeDeleted = new ArrayList<>();
        toBeDeleted.addAll(adapter.getSelected());

        // android/data/com.example.songly/files/lists
        File requiredPath = getExternalFilesDir("lists");

        if(requiredPath != null)
        {
            boolean flag = false;
            if(requiredPath.listFiles() == null) // folder has no files
            {
                Toast.makeText(this, "No lists available", Toast.LENGTH_SHORT).show();
            }
            else
            {
                File[] existingFiles =requiredPath.listFiles();
               for(String fileName: toBeDeleted)
               {
                    File fdelete = new File(requiredPath.getPath()+"/"+fileName+".txt");
//                   Toast.makeText(this, fdelete.getPath(), Toast.LENGTH_SHORT).show();
                    if(fdelete.exists())
                    {
                        fdelete.delete();
                        flag = true;
                    }
               }

               if(flag)
                  Toast.makeText(activity, "Lists(s) deleted", Toast.LENGTH_SHORT).show();

             }
            adapter.deleteFileNames();
        }
        else
            Toast.makeText(this, "No such directory", Toast.LENGTH_SHORT).show();
    }

    private void createFile(String newFileName) {
        newFileName.trim();
//        Path path = Paths.get(currentDir.getAbsolutePath()+"\\"+newFileName+".txt");
//        String p = currentDir.getAbsolutePath()+"//"+newFileName+".txt";
        File newFile = new File(currentDir, newFileName+".txt");

        try
        {
//            Path p = Files.createFile(path);
//            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//                Toast.makeText(this, "Mounted sd card", Toast.LENGTH_SHORT).show();

            File someFile = new File(getExternalFilesDir("lists"), newFileName+".txt");
            if(someFile.exists())
            {
                Toast.makeText(this, "List is already present", Toast.LENGTH_LONG).show();

            }
            else
            {
                someFile.createNewFile();
                listNames.add(newFileName);
                toggleDefaultText();
//                Intent intent = new Intent(getApplicationContext(), ViewIndividualList.class);
//                startActivity(intent);
//                intent.putExtra("fileName", newFileName);
                sharedPreferences = getSharedPreferences("SongLY", MODE_PRIVATE);
                sharedPreferences.edit().putString("file_name", newFileName+".txt").apply();

            }

//            Toast.makeText(this, someFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

//                Toast.makeText(this, "No such file", Toast.LENGTH_SHORT).show();

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
//        Toast.makeText(this, newFileName+".txt", Toast.LENGTH_SHORT).show();
    }



    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.selection_menu, menu);
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
                    mode.setTitle("Selected: "+adapter.listNames.size());
                    return true;
                }
                case R.id.deselect_all:
                {
                    adapter.clearAll(true);
                    actionMode.finish();
                    actionMode = null;
                    return true;
                }
                case R.id.edit_icon:
                {
                    if(adapter.getSelected().size() > 1)
                    {
                        Toast.makeText(activity, "Please select only one list item", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        renameFileTo(adapter.getSelected().get(0));
                    }
                    mode.finish();
                    actionMode = null;
                    return  true;
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

    private void renameFileTo(String currentName) {

        AlertDialog.Builder ab = new AlertDialog.Builder(ListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.rename_box, null);
        EditText newListName = (EditText) view.findViewById(R.id.new_list_name);
        Button renameBtn = (Button) view.findViewById(R.id.rename_button);
//        Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);

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

                    // android/data/com.example.songly/files/lists
                    File requiredPath = getExternalFilesDir("lists");

                    if(requiredPath != null)
                    {
//                        Toast.makeText(ListActivity.this, adapter.getSelected().get(0), Toast.LENGTH_SHORT).show();
                        File from = new File(requiredPath, currentName+".txt"); // old name
                        File to = new File(requiredPath, newFileName+".txt");
                        from.renameTo(to);

                        adapter.updateNames(currentName, newFileName);
                    }
                    else
                        Toast.makeText(ListActivity.this, "No such directory", Toast.LENGTH_SHORT).show();

                    alert.dismiss();
                }

            }
        });
    }


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