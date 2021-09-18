package com.example.songly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This file is a Fragment which displays the list of Karososa prayers.
 * This comes as the second tab of PrayerWithTab activity.
 * To display the list of karososa prayers, we've used a recyclerview.
 *
 */
public class FragmentExtra extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerViewExtraPrayer; // recycler for holding the extra prayer names
    public FragmentExtra() {
        // Required empty public constructor
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
    public static FragmentExtra newInstance(String param1, String param2) {
        FragmentExtra fragment = new FragmentExtra();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prayer_02, container, false);
//        requiredPath = getExternalFilesDir("prayer");;
        // implement required code

        recyclerViewExtraPrayer = view.findViewById(R.id.prayerExtraRecycler);

        // adapter object - accepts the prayer names arraylist
        AdapterExtraPrayers adapterExtraPrayers = new AdapterExtraPrayers(setUpRecycler());
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewExtraPrayer.setLayoutManager(layoutManager);
        recyclerViewExtraPrayer.hasFixedSize();
        recyclerViewExtraPrayer.setAdapter(adapterExtraPrayers); // set the adapter for recycler

        return view;
    }

    // create the list of karososa prayers from raw/extraprayerlist file
    // !!!!!!!!!!!!! we are re-using ModalFullSearch for creating objects   !!!!!!!!!!!!!!!!!!!!!!!!!!
    List<ModalFullSearch> setUpRecycler()
    {
        List<ModalFullSearch> temp = new ArrayList<>();
        String line;
        InputStream is = this.getResources().openRawResource(R.raw.extraprayerlist); // karosoosa list file

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        if(is != null)
        {

            try {

                while((line = bufferedReader.readLine()) != null)
                {
                    String[] splited = line.split("[,]", 0);
                    temp.add(
                            new ModalFullSearch(
                                    splited[2].trim(), // page start
                                    splited[3].trim(), // page end
                                    splited[1].trim(), // english title
                                    splited[0] // malayalam Title
                            ));
                }
                is.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return temp;
    }
}
