package com.isoterik.android.mybaby.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public interface ActivityEventsListener
{
    void onBackpressed (AppCompatActivity activity);

    void onRequestGuide (ViewPager viewPager);

    void onViewPagerSet (ViewPager viewPager);
}
