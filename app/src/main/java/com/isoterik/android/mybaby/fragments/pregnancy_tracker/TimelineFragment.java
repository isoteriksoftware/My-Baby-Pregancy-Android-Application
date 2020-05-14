package com.isoterik.android.mybaby.fragments.pregnancy_tracker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.PregnancyDataProvider;

public class TimelineFragment extends Fragment
{
    private View cachedRootView;
    private boolean instantiated;

    private String[] pregnancyWeeklyData;
    private Drawable[] weeklyBabyImages;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        pregnancyWeeklyData = getResources().getStringArray(R.array.pregnancy_week_data_array);

        int[] weeklyBabyImagesIds = Misc.getResourcesIdArray(getContext(), R.array.baby_weekly_images);
        weeklyBabyImages = new Drawable[weeklyBabyImagesIds.length];
        for (int i = 0; i < weeklyBabyImagesIds.length; i++)
            weeklyBabyImages[i] = getResources().getDrawable(weeklyBabyImagesIds[i]);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        if (instantiated)
            return cachedRootView;

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemsAdapter());
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.scrollToPosition(PregnancyDataProvider.validatePregnancyWeeksIndex(PregnancyDataProvider.getPregnancyWeeks(getContext())));
        cachedRootView = recyclerView;

        instantiated = true;
        return recyclerView;
    }

    class ItemsAdapter extends RecyclerView.Adapter<TimelineFragment.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.pregnancy_tracker_timeline_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            String week = String.format(getString(R.string.pregnancy_week_format), position + 1);
            String data = pregnancyWeeklyData[position];
            holder.bindModel(week, data, weeklyBabyImages[position]);
        }

        @Override
        public int getItemCount()
        {
            return pregnancyWeeklyData.length;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView weekBabyImage;
        private TextView labelWeek;
        private TextView labelInfo;

        public ViewHolder (@NonNull View itemView)
        {
            super(itemView);
            weekBabyImage = itemView.findViewById(R.id.weekBabyImage);
            labelWeek = itemView.findViewById(R.id.labelTimelineWeek);
            labelInfo = itemView.findViewById(R.id.labelTimelineWeekData);
        }

        public void bindModel (String week, String data, Drawable weekBabyImageDrawable)
        {
            weekBabyImage.setImageDrawable(weekBabyImageDrawable);
            labelWeek.setText(week);
            labelInfo.setText(data);
        }
    }
}




































