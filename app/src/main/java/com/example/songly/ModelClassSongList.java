package com.example.songly;

public class ModelClassSongList {

    private String textViewSongTitle;
    private String fileName;
    private String folderName;
    private String engName;
    private String song, karaoke, chord;


    ModelClassSongList (String fileName,
                        String malayalamTitle,
                        String engName,
                        String folderName,
                        String chord,
                        String song,
                        String karaoke)
    {

        this.fileName = fileName;
        this.textViewSongTitle=malayalamTitle;
        this.folderName = folderName;
        this.engName = engName;
        this.chord = chord;
        this.song = song;
        this.karaoke = karaoke;
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