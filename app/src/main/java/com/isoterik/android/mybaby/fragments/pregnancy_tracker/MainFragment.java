package com.isoterik.android.mybaby.fragments.pregnancy_tracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.isoterik.android.mybaby.BaseActivity;
import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.fragments.BaseListenerFragment;
import com.isoterik.android.mybaby.utils.ActivityEventsListener;
import com.isoterik.android.mybaby.utils.AlertUtil;
import com.isoterik.android.mybaby.utils.FileUtil;
import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.NotificationsUtil;
import com.isoterik.android.mybaby.utils.PreferencesUtil;
import com.isoterik.android.mybaby.utils.PregnancyDataProvider;
import com.isoterik.android.mybaby.utils.TapTargetUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainFragment extends BaseListenerFragment
{
    private View cachedRootView;
    private boolean instantiated;

    /* For initial empty fragment Layout */
    private Button btnStartTracker;

    /* For main fragment layout */
    private TextView labelPregnancyWeeks;
    private Button btnStopTracker;
    private TextView labelLMPDate;
    private Button btnChangeLMPDate;
    private ScrollView scrollView;

    private Calendar lmpCalendar;

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
        View root = null;

        boolean hasPregnancyTrackerData = PreferencesUtil.hasPregnancyTrackerData(getContext());

        if (hasPregnancyTrackerData)
            root = inflater.inflate(R.layout.fragment_pregnancy_tracker_main, container, false);
        else
            root = inflater.inflate(R.layout.fragment_pregnancy_tracker_main_no_data, container, false);

        cachedRootView = root;

        lmpCalendar = PreferencesUtil.lmpDate(getContext());

        if (hasPregnancyTrackerData)
        {
            scrollView = root.findViewById(R.id.trackerScrollView);
            labelPregnancyWeeks = root.findViewById(R.id.labelPregnancyWeeks);
            labelLMPDate = root.findViewById(R.id.labelLMPDate);
            btnStopTracker = root.findViewById(R.id.btnStopPregnancyTracker);
            btnStopTracker.setOnClickListener(v ->
            {
                String message = getString(R.string.confirm_stop_pregnancy_tracker);
                String title = getString(R.string.confirm_stop_pregnancy_tracker_title);
                AlertUtil.showConfirmDialog(getContext(), title, message, () ->
                {
                    PreferencesUtil.hasPregnancyTrackerData(getContext(), false);
                    PreferencesUtil.babyDue(getContext(), false);
                    NotificationsUtil.cancelAllNotificationTasks(getContext());
                    FileUtil.reset(getContext());
                    restart();
                });
            });

            btnChangeLMPDate = root.findViewById(R.id.btnChangeLMPDate);
            btnChangeLMPDate.setOnClickListener(this::onChangeLMPDate);
            updateDisplay();
        }
        else
        {
            btnStartTracker = root.findViewById(R.id.btnStartPregnancyTracker);
            btnStartTracker.setOnClickListener(v ->
            {
                String message = getString(R.string.confirm_start_pregnancy_tracking);
                String title = getString(R.string.confirm_start_pregnancy_tracking_title);
                AlertUtil.showConfirmDialog(getContext(), title, message, () ->
                {
                    changeLMPDate(!PreferencesUtil.firstPregnancyTrackingAfterLMP(getContext()));
                });
            });
        }

        instantiated = true;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (PreferencesUtil.hasPregnancyTrackerData(getContext()))
        {
            if (PreferencesUtil.firstPregnancyTrackingAfterLMP(getContext()))
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() ->
                {
                    TapTargetUtil.showPregnancyTrackerAfterLMPGuides(getActivity(), scrollView, labelPregnancyWeeks, btnStopTracker, labelLMPDate,
                            btnChangeLMPDate, viewPager);
                }, 300);
            }
        }
        else
        {
            if (PreferencesUtil.firstPregnancyTrackingInitial(getContext()))
            {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() ->
                {
                    TapTargetUtil.showPregnancyTrackerInitialGuides(getActivity(), btnStartTracker);
                }, 300);
            }
        }
    }

    private boolean validLMP (Calendar calendar)
    {
        Calendar now = Calendar.getInstance();
        long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
        int days = (int)TimeUnit.MILLISECONDS.toDays(diff);

        if (days >= Integer.parseInt(getString(R.string.max_pregnancy_days)))
            return false;

        return (days > 0);
    }

    private void updateDisplay()
    {
        int pregnancyWeeks = PregnancyDataProvider.getPregnancyWeeks(getContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.last_lmp_date_format));

        String weekText = pregnancyWeeks > 1 ? "weeks" : "week";
        String weeksDisplay = String.format(getString(R.string.pregnancy_weeks_format), pregnancyWeeks, weekText);
        String lmpDateDisplay = dateFormat.format(lmpCalendar.getTime());

        labelPregnancyWeeks.setText(weeksDisplay);
        labelLMPDate.setText(lmpDateDisplay);
    }

    private void onChangeLMPDate (View v)
    {
        String message = getString(R.string.confirm_change_lmp_date);
        String title = getString(R.string.confirm_change_lmp_date_title);
        AlertUtil.showConfirmDialog(getContext(), title, message, () ->
        {
            changeLMPDate(true);
        });
    }

    private void changeLMPDate (boolean changeTab)
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, day) ->
                {
                    lmpCalendar.set(year, month, day);

                    if (validLMP(lmpCalendar))
                    {
                        PreferencesUtil.hasPregnancyTrackerData(getContext(), true);
                        PreferencesUtil.babyDue(getContext(), false);
                        PreferencesUtil.lmpDate(getContext(), lmpCalendar);
                        NotificationsUtil.reset(getContext());
                        restart(changeTab ? 1 : -1);
                    }
                    else
                    {
                        AlertUtil.showAlert(getContext(), getString(R.string.error_message_header), getString(R.string.error_invalid_lmp_date));
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    private void restart (int currentTab)
    {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        intent.putExtra(Misc.EXTRA_CURRENT_TAB, currentTab);
        startActivity(intent);
    }

    private void restart()
    {
        restart(-1);
    }

    @Override
    public void onBackpressed(AppCompatActivity activity)
    {
        activity.finish();
    }

    @Override
    public void onRequestGuide(ViewPager viewPager)
    {
        if (PreferencesUtil.hasPregnancyTrackerData(getContext()))
        {
            TapTargetUtil.showPregnancyTrackerAfterLMPGuides(getActivity(), scrollView, labelPregnancyWeeks, btnStopTracker, labelLMPDate,
                    btnChangeLMPDate, viewPager);
        }
        else
        {
            TapTargetUtil.showPregnancyTrackerInitialGuides(getActivity(), btnStartTracker);
        }
    }
}



































