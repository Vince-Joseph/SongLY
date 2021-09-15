package com.example.songly;

public class ModelClassSongList {

    private final String textViewSongTitle;
    private final String startPage;
    private final String endPage;
    private final String engName;
    private final String song;
    private final String karaoke;
    private final String chord;


    ModelClassSongList (String startPage,
                        String endPage,
                        String engName,
                        String malayalamTitle,
                        String chord,
                        String song,
                        String karaoke)
    {

        this.startPage = startPage;
        this.endPage = endPage;
        this.engName = engName;
        this.textViewSongTitle=malayalamTitle;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
    }

    public String getEngName() {
        return engName;
    }

    public String getEndPage() {
        return endPage;
    }

    public String getStartPage() {
        return startPage;
    }

    public String getTextViewSongTitle() {
        return textViewSongTitle;
    }

    public String getSong() {
        return song;
    }

    public String getKaraoke() {
        return karaoke;
    }

    public String getChord() {
        return chord;
    }
}