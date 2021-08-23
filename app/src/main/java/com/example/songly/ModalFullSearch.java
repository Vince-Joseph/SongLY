package com.example.songly;

import android.graphics.Typeface;

import java.lang.reflect.Type;

public class ModalFullSearch implements Comparable<ModalFullSearch> {

    private String malayalamTitle;
    private String englishTitle;
    private  String fileName;
    private  String folderName;

    private String album, singers, year, chord;

    Typeface typeface;

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName,
                           String album, String singers, String year,
                           String chord, Typeface typeface) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.album = album;
        this.singers = singers;
        this.year = year;
        this.chord = chord;
        this.typeface = typeface;
    }

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName,String album, String singers, String year,
                           String chord) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.album = album;
        this.singers = singers;
        this.year = year;
        this.chord = chord;
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

    public String getAlbum() {
        return album;
    }

    public String getSingers() {
        return singers;
    }

    public String getYear() {
        return year;
    }

    public String getChord() {
        return chord;
    }

    // to sort two objects based on english titles
    @Override
    public int compareTo(ModalFullSearch o) {
        return (this.getEnglishTitle().compareToIgnoreCase(o.getEnglishTitle()));
    }
}