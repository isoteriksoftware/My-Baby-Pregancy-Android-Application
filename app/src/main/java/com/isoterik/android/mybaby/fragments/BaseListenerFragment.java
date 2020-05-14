package com.isoterik.android.mybaby.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.isoterik.android.mybaby.BaseActivity;
import com.isoterik.android.mybaby.utils.ActivityEventsListener;

public class BaseListenerFragment extends Fragment implements ActivityEventsListener
{
    protected ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((BaseActivity)getActivity()).setEventsListener(this);
    }

    @Override
    public void onBackpressed(AppCompatActivity activity) {

    }

    @Override
    public void onRequestGuide(ViewPager viewPager) {

    }

    @Override
    public void onViewPagerSet(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }
}
