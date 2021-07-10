package com.example.songly;

import android.util.Log;

public class ModelClassSongList {

    private String textViewSongTitle;
    private String fileName;
    private String folderName;
    private String engName;


    ModelClassSongList (String fileName, String malayalamTitle, String engName, String folderName)
    {

        this.fileName = fileName;
        this.textViewSongTitle=malayalamTitle;
        this.folderName = folderName;
        this.engName = engName;
//        Log.d("SOng title is", engName);
    }

    public String getEngName() {
        return engName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTextViewSongTitle() {
        return textViewSongTitle;
    }



}