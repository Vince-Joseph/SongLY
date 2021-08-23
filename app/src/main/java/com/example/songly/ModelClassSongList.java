package com.example.songly;

public class ModelClassSongList {

    private String textViewSongTitle;
    private String fileName;
    private String folderName;
    private String engName;
    private String album, singers, year, chord;


    ModelClassSongList (String fileName,
                        String malayalamTitle,
                        String engName,
                        String folderName,
                        String album,
                        String singers,
                        String year,
                        String chord)
    {

        this.fileName = fileName;
        this.textViewSongTitle=malayalamTitle;
        this.folderName = folderName;
        this.engName = engName;
        this.album = album;
        this.singers = singers;
        this.year = year;
        this.chord = chord;
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

    public String getAlbum() {
        return album;
    }

    public String getSingers() {
        return singers;
    }

    public String getChord() {
        return chord;
    }

    public String getYear() {
        return year;
    }
}