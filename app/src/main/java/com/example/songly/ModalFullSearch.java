package com.example.songly;

import android.graphics.Typeface;

import java.lang.reflect.Type;

public class ModalFullSearch implements Comparable<ModalFullSearch> {

    private String malayalamTitle;
    private String englishTitle;
    private  String fileName;
    private  String folderName;

    private String song, karaoke, chord;

    Typeface typeface;

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName,
                           String chord, String song, String karaoke, Typeface typeface) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
        this.typeface = typeface;
    }

    public ModalFullSearch(String fileName, String englishTitle, String malayalamTitle,
                           String folderName,
                           String chord, String song, String karaoke) {
//        this.imageResource = imageResource;
        this.fileName = fileName;
        this.folderName = folderName;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
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

    public String getKaraoke() {
        return karaoke;
    }

    public String getSong() {
        return song;
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