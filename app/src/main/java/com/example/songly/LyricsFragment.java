package com.example.songly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LyricsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LyricsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String fileName, folderName;
    PDFView pdfView;

    public LyricsFragment() {
        // Required empty public constructor
    }

    public LyricsFragment(String fileName, String folderName) {
        this.fileName = fileName;
        this.folderName = folderName;
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
    public static LyricsFragment newInstance(String param1, String param2) {
        LyricsFragment fragment = new LyricsFragment();
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
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        // load the required pdf lyrics from file
        pdfView = view.findViewById(R.id.lyricsFragment);
        pdfView.fromAsset(folderName+"/"+fileName+".pdf")
                .enableDoubletap(true)
                .pageFitPolicy(FitPolicy.WIDTH)
                .nightMode(false)
                .disableLongpress()
                .load();

        pdfView.setMaxZoom(1.3f);
        pdfView.setMidZoom(1);

        return view;
    }
}