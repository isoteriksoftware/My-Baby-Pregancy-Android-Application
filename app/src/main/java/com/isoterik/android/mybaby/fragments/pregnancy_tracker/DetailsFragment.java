package com.isoterik.android.mybaby.fragments.pregnancy_tracker;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.PreferencesUtil;
import com.isoterik.android.mybaby.utils.PregnancyDataProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DetailsFragment extends Fragment
{
    private View cachedRootView;
    private boolean instantiated;

    private TextView labelDueDate;
    private TextView labelWeeklyUpdate;
    private TextView labelBabyLength;
    private TextView labelBabyWeight;
    private TextView labelBabyFruitSize;
    private TextView labelCurrentWeek;
    private ImageView babyFruitImage;

    private String[] babyWeeklyStats;
    private String[] weeklyUpdates;
    private Drawable fruitImage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        if (instantiated)
            return cachedRootView;

        View root = inflater.inflate(R.layout.fragment_pregnancy_tracker_details, container, false);
        cachedRootView = root;

        labelBabyFruitSize = root.findViewById(R.id.labelBabyFruitSize);
        labelBabyLength = root.findViewById(R.id.labelBabyLength);
        labelBabyWeight = root.findViewById(R.id.labelBabyWeight);
        labelWeeklyUpdate = root.findViewById(R.id.labelWeeklyUpdate);
        labelDueDate = root.findViewById(R.id.labelDueDate);
        labelCurrentWeek = root.findViewById(R.id.labelCurrentWeek);
        babyFruitImage = root.findViewById(R.id.babyFruitImage);

        babyWeeklyStats = getResources().getStringArray(R.array.baby_weekly_stats_array);
        weeklyUpdates = getResources().getStringArray(R.array.baby_weekly_update_array);
        updateDisplays();

        instantiated = true;
        return root;
    }

    private void updateDisplays()
    {
        Calendar deliveryDate = PregnancyDataProvider.getBabyDueDate(getContext());
        int pregnancyWeeks = PregnancyDataProvider.getPregnancyWeeks(getContext());
        int week = PregnancyDataProvider.validatePregnancyWeeksIndex(pregnancyWeeks);
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.due_date_format));

        String[] babyWeekStats = babyWeeklyStats[week].split(" ");
        String babyLength = babyWeekStats[0].replace("-", " ");
        String babyWeight = babyWeekStats[1].replace("-", " ");
        String babyFruitSize = babyWeekStats[2].replace("-", " ");

        int[] imagesIds = Misc.getResourcesIdArray(getContext(), R.array.fruits);
        fruitImage = getResources().getDrawable(imagesIds[PregnancyDataProvider.validatePregnancyWeeksIndex(PregnancyDataProvider.getPregnancyWeeks(getContext()))]);

        labelCurrentWeek.setText(String.format(getString(R.string.current_week_format), pregnancyWeeks));
        labelDueDate.setText(dateFormat.format(deliveryDate.getTime()));
        labelBabyLength.setText(babyLength);
        labelBabyWeight.setText(babyWeight);
        labelBabyFruitSize.setText(babyFruitSize);
        labelWeeklyUpdate.setText(weeklyUpdates[week]);
        babyFruitImage.setImageDrawable(fruitImage);
    }
}























