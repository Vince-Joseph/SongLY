package com.example.songly;

import android.graphics.Typeface;

public class ModalExtraPrayers {

    private  String malayalamTitle;
    private  String englishTitle;
    private  String pageStart;
    private  String pageEnd;
    Typeface typeface;

    public ModalExtraPrayers(String malayalamTitle,
              String englishTitle,
              String pageStart,
              String pageEnd){

        this.malayalamTitle = malayalamTitle;
        this.englishTitle = englishTitle;
        this.pageStart = pageStart;
        this.pageEnd = pageEnd;
    }

    public String getPageStart() {
        return pageStart;
    }

    public String getPageEnd() {
        return pageEnd;
    }

    public String getMalayalamTitle() {
        return malayalamTitle;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }
}
