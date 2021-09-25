package com.example.songly;

import android.graphics.Typeface;

/**
 * This modal class is used to hold the items of the individual_song_item view.
 * This class represents/holds the variables required to set up a single song card.
 */
public class ModalFullSearch implements Comparable<ModalFullSearch> {

    //******************************************************************************************
    // we are re-using this modal class at many places. So take care before editing or deleting
    //******************************************************************************************
    private final String malayalamTitle;
    private final String englishTitle;
    private final String pageStart;
    private final String pageEnd;

    private final String song;
    private final String karaoke;
    private final String chord;

    Typeface typeface;

    public ModalFullSearch(String pageStart, String pageEnd, String englishTitle, String malayalamTitle,
                           String chord, String song, String karaoke, Typeface typeface) {
//        this.imageResource = imageResource;
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.englishTitle = englishTitle;
        this.malayalamTitle = malayalamTitle;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
        this.typeface = typeface;
    }

    public ModalFullSearch(String pageStart, String pageEnd,String englishTitle, String malayalamTitle,
                           String chord, String song, String karaoke) {
//        this.imageResource = imageResource;
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
    }

    // this constructor is used in extra prayer activity
    public ModalFullSearch(String pageStart, String pageEnd,String englishTitle, String malayalamTitle) {
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.chord = "";
        this.song = "";
        this.karaoke = "";
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public String getPageStart() {
        return pageStart;
    }

    public String getPageEnd() {
        return pageEnd;
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