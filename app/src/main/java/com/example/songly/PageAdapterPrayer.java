package com.example.songly;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class PageAdapterPrayer  extends FragmentPagerAdapter {

    final int tabCount;


    public PageAdapterPrayer(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.tabCount = behavior;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0: return new FragmentPrayer();
            case 1: return new FragmentExtra();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
