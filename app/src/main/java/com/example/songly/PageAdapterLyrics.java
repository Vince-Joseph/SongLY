package com.example.songly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class PageAdapterLyrics extends FragmentPagerAdapter {
    int tabCount;

    // filename
    // folder name
    // chord
    // song link
    // karaoke link
    String  contents[];

    public PageAdapterLyrics(@NonNull @NotNull FragmentManager fm, int behavior,
                             String contents[]) {
        super(fm, behavior);
        this.tabCount = behavior;
        this.contents = contents;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {                                    // filename   folder name
            case 0: return new LyricsFragment(contents[0], contents[1]);
            case 1: return new DetailsFragment(contents);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
