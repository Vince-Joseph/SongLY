package com.example.songly;

import android.graphics.Typeface;

import java.lang.reflect.Type;

public class ModalFullSearch implements Comparable<ModalFullSearch> {

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

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
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


    // to sort two objects based on english titles
    @Override
    public int compareTo(ModalFullSearch o) {
        return (this.getEnglishTitle().compareToIgnoreCase(o.getEnglishTitle()));
    }
}