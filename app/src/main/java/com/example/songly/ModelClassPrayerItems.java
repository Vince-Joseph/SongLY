package com.example.songly;

public class ModelClassPrayerItems {
//    public static final int LayoutOne = 0;
//    public static final int LayoutTwo = 1;


    // this variable ViewType specifies which out of the two layouts is expected in the given item
    private int viewType;

    public ModelClassPrayerItems(int layoutType)
    {
        this.viewType = layoutType;
    }
    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
