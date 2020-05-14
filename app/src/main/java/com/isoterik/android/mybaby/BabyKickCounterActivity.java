package com.isoterik.android.mybaby;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.isoterik.android.mybaby.adapters.BabyKickCounterAdapter;
import com.isoterik.android.mybaby.utils.Misc;

public class BabyKickCounterActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        BabyKickCounterAdapter adapter = new BabyKickCounterAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_flash_on_white_36dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_format_list_bulleted_white_36dp);
        tabs.getTabAt(2).setIcon(R.drawable.ic_timeline_white_24dp);
        tabs.getTabAt(3).setIcon(R.drawable.ic_info_outline_white_36dp);
        tabs.getTabAt(0).getIcon().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN));
        tabs.getTabAt(1).getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        tabs.getTabAt(2).getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
        tabs.getTabAt(3).getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));

        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager)
        {
            @Override
            public void onTabReselected (TabLayout.Tab tab)
            {
                super.onTabReselected(tab);
            }

            @Override
            public void onTabSelected (@NonNull TabLayout.Tab tab)
            {
                tab.getIcon().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN));
            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab)
            {
                tab.getIcon().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            }
        });

        Intent intent = getIntent();
        int currentTab = intent.getIntExtra(Misc.EXTRA_CURRENT_TAB, -1);
        if (currentTab != -1)
            viewPager.setCurrentItem(currentTab, true);
    }
}
