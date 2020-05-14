package com.isoterik.android.mybaby;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.isoterik.android.mybaby.utils.ActivityEventsListener;

public class BaseActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{
    protected ActivityEventsListener eventsListener;
    protected ViewPager viewPager;
    protected TabLayout tabs;
    protected MenuItem actionShowGuide;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);
        tabs = findViewById(R.id.tabs);
    }

    public void setEventsListener (ActivityEventsListener eventsListener)
    {
        this.eventsListener = eventsListener;
    }

    @Override
    public void onBackPressed()
    {
        if (eventsListener != null)
            eventsListener.onBackpressed(this);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        actionShowGuide = toolbar.getMenu().add(R.string.guide);
        actionShowGuide.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        actionShowGuide.setIcon(R.drawable.ic_help_outline_white_36dp);
        actionShowGuide.setOnMenuItemClickListener(item ->
        {
            if (eventsListener != null && viewPager.getCurrentItem() == 0)
                eventsListener.onRequestGuide(viewPager);
            return true;
        });

        // We are setting it up here because this method is called after the listener is set!
        if (eventsListener != null)
            eventsListener.onViewPagerSet(viewPager);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        finish();
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position)
    {
        if (actionShowGuide == null)
            return;

        if (position == 0)
        {
            actionShowGuide.setEnabled(true);
            actionShowGuide.setVisible(true);
        }
        else
        {
            actionShowGuide.setEnabled(false);
            actionShowGuide.setVisible(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
