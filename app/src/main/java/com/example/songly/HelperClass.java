package com.example.songly;

import android.content.Context;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HelperClass {
    public ArrayList<ModalFullSearch> fillSongTitles(Context context) {
        // setting up the malayalam font face from assets/font folder
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/MLKR0NTT.TTF");

        ArrayList<ModalFullSearch> fullListOfSongs = new ArrayList<>(); // create an arraylist
        String data = "";
        InputStream is = null;

        // open the entrance text from 'res/raw' folder
        is = context.getResources().openRawResource(R.raw.entrance);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

        if(is != null)
        {
            // try to read each line from the file
            try
            {   // read each row from file
                while((data = bufferedReader.readLine()) != null)
                {
                    // split it into words by using comma as a delimiter
                    String[] splited = data.split("[,]", 0);

                    // now create new object of Modal class and add it to the list
                    fullListOfSongs.add(
                            new ModalFullSearch(
                                    splited[0].trim(), // filename
                                    splited[1].trim(), // eng title
                                    splited[2].trim(), // malayalam title
                                    splited[3].trim(), // folder name
                                    typeface));
                }
                is.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return fullListOfSongs;
    }
}
