package com.example.songly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentPrayer extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerViewExtraPrayer; // recycler for holding the applied song names
    FloatingActionButton floatingActionButton;
    SharedPreferences sharedPreferences;
    AdapterAppliedList adapterAppliedList; // adapter for the alert box list
    List<ModalFullSearch> listOfSongs = new ArrayList<>();
    boolean visible;
    public FragmentPrayer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LyricsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPrayer newInstance(String param1, String param2) {
        FragmentPrayer fragment = new FragmentPrayer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        visible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prayer_01, container, false);

        // path to the list of applied songs to show in the alert box
        File requiredPathOfAppliedList = view.getContext().getExternalFilesDir("appliedList");

        // pdf view which displays the mass prayer
        PDFView pdfView = view.findViewById(R.id.prayerPdfTab);
        pdfView.fromAsset("prayer/kurbaana.pdf")
                .enableDoubletap(true)
                .pageFitPolicy(FitPolicy.BOTH)
                .nightMode(false)
                .scrollHandle(new DefaultScrollHandle(view.getContext()))
                .load();

        if (requiredPathOfAppliedList != null)
        {
            if (requiredPathOfAppliedList.listFiles() == null) // folder has no files
                Toast.makeText(view.getContext(), "No 'applied' file available",
                        Toast.LENGTH_SHORT).show();
            else
            {

            try {
                FileReader fileReader = new FileReader(new File(requiredPathOfAppliedList,
                        "applied_list.txt"));
                BufferedReader bufferedReader = new BufferedReader(fileReader); // creates buffer of chars
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    String[] splited = line.split("[,]", 0);

                    // now create new object of Modal class and add it to the list
                    // we are re-using ModelFullSearch here
                    ModalFullSearch modalFullSearch = new ModalFullSearch(
                            splited[0], // page start
                            splited[1].trim(), // page end
                            splited[2].trim(), // eng title
                            splited[3].trim(), // malayalam title
                            splited[4].trim(), // song's chord
                            splited[5].trim(), // song's song link
                            splited[6].trim() // song's karaoke
                    );
                    listOfSongs.add(modalFullSearch);
                }
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }

        } else
            Toast.makeText(view.getContext(), "No applied file", Toast.LENGTH_SHORT).show();

//------------------------------- Floating action button ---------------------------------------

        floatingActionButton = view.findViewById(R.id.prayerFloatingButton);
        // upon clicking floating button, show the alert box
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(view.getContext());
                View view = getLayoutInflater().inflate(R.layout.applied_list_box, null);
                adapterAppliedList = new AdapterAppliedList(listOfSongs,
                        view.getContext());

                recyclerViewExtraPrayer = view.findViewById(R.id.recyclerAppliedList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
                recyclerViewExtraPrayer.setLayoutManager(linearLayoutManager);
                toggleEmptyMessage(view);
                recyclerViewExtraPrayer.setAdapter(adapterAppliedList);

                // upon clicking clear all button, clear the list of songs and display empty message
                ImageView clearIcon = view.findViewById(R.id.clearIcon);
                clearIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            // delete all the contents from the file
                            PrintWriter printWriter = new PrintWriter(
                                    requiredPathOfAppliedList.toString() + "//applied_list.txt");
                            printWriter.write("");
                            printWriter.close();

                            listOfSongs.clear(); // clear the arraylist of song names
                            adapterAppliedList.notifyDataSetChanged();
                            toggleEmptyMessage(view);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // upon clicking add new songs button
                ImageView addNewSongs = view.findViewById(R.id.addSongsIcon);
                addNewSongs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FullSearch.class);
                        intent.putExtra("mode", "on");
                        intent.putExtra("from", "PrayerWithTab");
                        startActivity(intent);
                    }
                });

                // build the alert box
                ab.setView(view);
                AlertDialog alert = ab.create();
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert.show();// show the dialog box
            }
        });

        sharedPreferences = view.getContext().getSharedPreferences("SongLY", Context.MODE_PRIVATE);
//                 check whether some songs have been added newly
        if (sharedPreferences.getString("selected", null).equals("on")) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("selected_songs", null);
            Type type = new TypeToken<ArrayList<ModalFullSearch>>() {
            }.getType();
            ArrayList<ModalFullSearch> storedList = gson.fromJson(json, type);

            if (storedList != null) {
                File toBeRead = new File(requiredPathOfAppliedList.toString() + "//applied_list.txt");
                for (ModalFullSearch md : storedList) {
//
                    listOfSongs.add(md);
                    FileWriter writeToFile = null;
                    try {
                        writeToFile = new FileWriter(toBeRead, true);
                        writeToFile.write(
                                md.getPageStart() + ",\t\t\t"
                                        + md.getPageEnd() + ",\t\t\t"
                                        + md.getEnglishTitle() + ",\t\t\t"
                                        + md.getMalayalamTitle() + ",\t\t\t"
                                        + md.getChord() + ",\t\t\t"
                                        + md.getSong() + ",\t\t\t"
                                        + md.getKaraoke() + "\n"
                        );

                        writeToFile.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else
                Toast.makeText(view.getContext(), "stored list is null !!", Toast.LENGTH_SHORT).show();
        }
//                else
//                    Toast.makeText(this, "Note that storedlist is empty", Toast.LENGTH_LONG).show();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_songs", "");
        editor.putString("selected", "off");
        editor.apply();
        return view;
    }

    private void toggleEmptyMessage(View view) {
        TextView textView = view.findViewById(R.id.emptyMessage);
        if(listOfSongs.size()>0)
        {
            recyclerViewExtraPrayer.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            visible = false;
        }
        else
        {
            recyclerViewExtraPrayer.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            visible = true;
        }

    }

//    private OnTapListener hideThings() {
//
//        return new OnTapListener() {
//            @Override
//            public boolean onTap(MotionEvent e) {
//                if(visible)
//                {
//                    navView.setVisibility(View.GONE);
//                    linearLayout.setVisibility(View.GONE);
//                    pdfLayout.startAnimation(AnimationUtils.loadAnimation(linearLayout.getContext(),
//                            R.anim.trans_upwards));
//                    visible = false;
//                }
//                else
//                {
//                    visible = true;
//                    navView.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.VISIBLE);
//                    pdfLayout.startAnimation(AnimationUtils.loadAnimation(linearLayout.getContext(),
//                            R.anim.trans_downwards));
//                }
//                return false;
//            }
//        };
//    }

}