package com.example.songly;

import android.graphics.Typeface;

import java.lang.reflect.Type;

public class ModalFullSearch {

    private String malayalamTitle;
    private String englishTitle;
    private  String fileName;
    private  String folderName;

    Typeface typeface;

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName, Typeface typeface) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.typeface = typeface;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public String getMalayalamTitle() {
        return malayalamTitle;
    }
}