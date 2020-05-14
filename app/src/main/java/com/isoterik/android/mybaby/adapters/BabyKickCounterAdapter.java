package com.isoterik.android.mybaby.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.fragments.InfoFragment;
import com.isoterik.android.mybaby.fragments.NoDataFragment;
import com.isoterik.android.mybaby.utils.FileUtil;

public class BabyKickCounterAdapter extends FragmentPagerAdapter
{
    private final Context context;
    private String[] data;

    public BabyKickCounterAdapter (Context context, FragmentManager fm)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem (int position)
    {
        Fragment fragment = null;

        if (position == 0)
            fragment = new com.isoterik.android.mybaby.fragments.baby_kick_counter.MainFragment();
        else if (position == 1)
        {
            data = FileUtil.readKicksData(context);
            if (data != null && data.length != 0)
                fragment = new com.isoterik.android.mybaby.fragments.baby_kick_counter.DetailsFragment(data);
            else
                fragment = new NoDataFragment();
        }
        else if (position == 2)
        {
            if (data != null && data.length != 0)
                fragment = new com.isoterik.android.mybaby.fragments.baby_kick_counter.TimelineFragment(data);
            else
                fragment = new NoDataFragment();
        }
        else
            fragment = InfoFragment.create(context.getString(R.string.baby_kick_counter_info_header), R.array.baby_kick_counter_info_array);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position)
    {
        return "";
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}
