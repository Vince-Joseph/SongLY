package com.example.songly;

import android.content.Context;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HelperClass {

    // this method helps to read all the available titles from all text files and create a list
    // containing all song titles
    public ArrayList<ModalFullSearch> fillSongTitles(Context context) {
        // setting up the malayalam font face from assets/font folder
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/MLKR0NTT.TTF");

        ArrayList<ModalFullSearch> fullListOfSongs = new ArrayList<>(); // create an arraylist


        // array containing id of text files in res/raw folder
        int [] listOfFiles = {
                R.raw.entrance,
                R.raw.psalms,
                R.raw.gospel,
                R.raw.offering,
                R.raw.osana,
                R.raw.adoration,
                R.raw.communion,
                R.raw.others
        };

//        Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();

        for(int id: listOfFiles)
        {
            String data = "";
            // open the text from 'res/raw' folder
            InputStream is = context.getResources().openRawResource(id);
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
                                        splited[4].trim(), // chord
                                        splited[5].trim(), // song link
                                        splited[6].trim(), // karaoke link
                                        typeface));
                    }
                    is.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return fullListOfSongs;
    }

}
