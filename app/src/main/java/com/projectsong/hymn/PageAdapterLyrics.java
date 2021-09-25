package com.example.songly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 *
 * This is an adapter which manages the Fragments of the TabbedLyricsView Activity
 *
 */
public class PageAdapterLyrics extends FragmentPagerAdapter {
    final int tabCount;

    // startPag, eendPage, chord, song link, karaoke link
    final String[] contents; // stores the passed data

    public PageAdapterLyrics(@NonNull @NotNull FragmentManager fm, int behavior,
                             String[] contents) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.contents = contents;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {                                    // pageStart   pageEnd
            case 0: return new LyricsFragment(contents[0], contents[1]);
            case 1: return new DetailsFragment(contents); // pass all the data
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
