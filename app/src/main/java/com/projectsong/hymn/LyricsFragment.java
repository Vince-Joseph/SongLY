package com.example.songly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

/*

  This Fragment displays the actual song lyrics.
  Bottom nav is disabled for this fragment.
  The pdfView allows two zoom levels, not the default 3.
  All the lyrics are fetched from a single pdf located in  assets/songs/songs.pdf
 */


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link LyricsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class LyricsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    int pageStart, pageEnd;
    PDFView pdfView;

    public LyricsFragment() {
        // Required empty public constructor
    }

    public LyricsFragment(String pageStart, String pageEnd) {
        this.pageStart = Integer.parseInt(pageStart);
        this.pageEnd = Integer.parseInt(pageEnd);
    }
// --Commented out by Inspection START (18-09-2021 05:47 PM):
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment LyricsFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static LyricsFragment newInstance(String param1, String param2) {
//        LyricsFragment fragment = new LyricsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
// --Commented out by Inspection STOP (18-09-2021 05:47 PM)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        // load the required pdf lyrics from file
        pdfView = view.findViewById(R.id.lyricsFragment);

        // check whether starpage and endpage are equal or not
        if(pageStart == pageEnd) // only one page
        {
            pdfView.fromAsset("songs/songs.pdf")
                    .pages(pageStart-1)
                    .enableDoubletap(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(false)
                    .disableLongpress()
                    .load();

        }
        else if(pageEnd == pageStart+1) // 2 pages
        {
            pdfView.fromAsset("songs/songs.pdf")
                    .pages(pageStart-1, pageStart)
                    .enableDoubletap(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(false)
                    .disableLongpress()
                    .load();

        }
        else if(pageEnd == pageStart+2) // 3 pages
        {
            pdfView.fromAsset("songs/songs.pdf")
                    .pages(pageStart-1, pageStart, pageStart+1)
                    .enableDoubletap(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(false)
                    .disableLongpress()
                    .load();

        }
        else if(pageEnd == pageStart+3) // 4 pages
        {
            pdfView.fromAsset("songs/songs.pdf")
                    .pages(pageStart-1, pageStart, pageStart+1, pageStart+2)
                    .enableDoubletap(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(false)
                    .disableLongpress()
                    .load();

        }
        else // 5 pages or more
        {
            pdfView.fromAsset("songs/songs.pdf")
                    .pages(pageStart-1, pageStart, pageStart+1, pageStart+3, pageEnd-1)
                    .enableDoubletap(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(false)
                    .disableLongpress()
                    .load();
        }

        pdfView.setMaxZoom(1.3f);
        pdfView.setMidZoom(1);

        return view;
    }
}