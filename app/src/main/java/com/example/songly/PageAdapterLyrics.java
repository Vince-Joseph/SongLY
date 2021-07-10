package com.example.songly;

import android.content.Context;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PageAdapterLyrics extends FragmentPagerAdapter {
    int tabCount;
    String lyrics, details;
    Typeface typeface;

    public PageAdapterLyrics(@NonNull @NotNull FragmentManager fm, int behavior,
                             String lyrics,
                             String details,
                             Typeface typeface) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.lyrics = lyrics;
        this.details = details;
        this.typeface = typeface;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {


        switch (position)
        {
            case 0: return new LyricsFragment(lyrics, typeface);
            case 1: return new DetailsFragment(details);

        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
