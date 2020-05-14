package com.isoterik.android.mybaby.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.fragments.InfoFragment;
import com.isoterik.android.mybaby.fragments.NoDataFragment;
import com.isoterik.android.mybaby.fragments.pregnancy_tracker.DetailsFragment;
import com.isoterik.android.mybaby.fragments.pregnancy_tracker.MainFragment;
import com.isoterik.android.mybaby.fragments.pregnancy_tracker.TimelineFragment;
import com.isoterik.android.mybaby.utils.PreferencesUtil;

public class PregnancyTrackerAdapter extends FragmentPagerAdapter
{
    private final Context context;
    private final boolean hasData;

    public PregnancyTrackerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        this.context = context;
        hasData = PreferencesUtil.hasPregnancyTrackerData(context);
    }

    @Override
    public Fragment getItem (int position)
    {
        Fragment fragment = new NoDataFragment();

        if (position == 0)
            fragment = new MainFragment();
        else if (position == 1 && hasData)
            fragment = new DetailsFragment();
        else if (position == 2)
            fragment = new TimelineFragment();
        else if (position == 3)
            fragment = InfoFragment.create(context.getString(R.string.pregnancy_tracker_info_header), R.array.pregnancy_tracker_info_array);

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