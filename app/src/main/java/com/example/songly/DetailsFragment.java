package com.example.songly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // filename
    // foldername
    // chord
    // song
    // karaoke link
    String details[];

    public DetailsFragment() {
        // Required empty public constructor
    }

    public DetailsFragment(String details[]) {
//        Log.d("The string is:", details);
        this.details =  details;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TextView songLink = view.findViewById(R.id.song_link);
        TextView karaokeLink = view.findViewById(R.id.karaoke_link);
        TextView chord = view.findViewById(R.id.root_chord);

        chord.setText(details[2].trim());
        songLink.setText(details[3].trim());
        karaokeLink.setText(details[4].trim());

        songLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!songLink.getText().equals("Un Available"))
                    gotoUrl(details[3].trim());
            }
        });

        karaokeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!karaokeLink.getText().equals("Un Available"))
                    gotoUrl(details[4].trim());
            }
        });

        return view;
    }

    private void gotoUrl(String text) {

        Uri uri = Uri.parse(text);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}