package com.isoterik.android.mybaby.fragments.baby_kick_counter;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.isoterik.android.mybaby.BaseActivity;
import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.fragments.BaseListenerFragment;
import com.isoterik.android.mybaby.utils.ActivityEventsListener;
import com.isoterik.android.mybaby.utils.AlertUtil;
import com.isoterik.android.mybaby.utils.FileUtil;
import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.PreferencesUtil;
import com.isoterik.android.mybaby.utils.TapTargetUtil;
import com.isoterik.android.mybaby.utils.TimerUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends BaseListenerFragment
{
    private View cachedRootView;

    private ScrollView scrollView;

    private TextView labelCounterStatus;
    private TextView labelCounterTimer;
    private CardView cardBabyKicked;
    private TextView labelKicks;
    private TextView labelStartKickTime;
    private TextView labelRecentKickTime;
    private Button btnResetKickCounter;
    private Button btnFinishKickCounting;

    private TimerUtil timer;
    private Calendar calendar;
    private Date startDate;
    String manualTimeFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat fullTimeFormat;
    private SimpleDateFormat simpleDateFormat;
    private int kicks;
    private long timeElapsedMillis;
    private long timeFinishedMillis;

    private File sessionDataFile;

    private Bundle savedState = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        manualTimeFormat = getString(R.string.manual_time_format);
        timeFormat = new SimpleDateFormat(getString(R.string.time_format));
        fullTimeFormat = new SimpleDateFormat(getString(R.string.time_format_full));
        simpleDateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (timer.isTimerRunning())
            outState.putBundle("cached_bundle", (savedState != null) ? savedState : saveState());
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        if (timer.isTimerRunning())
            savedState = saveState();
    }

    private Bundle saveState()
    {
        Bundle state = new Bundle();
        state.putString("cached_status_txt", labelCounterStatus.getText().toString());
        state.putString("cached_timer_text", labelCounterTimer.getText().toString());
        state.putString("cached_kicks", labelKicks.getText().toString());
        state.putLong("cached_start_time", timer.getStartDate().getTimeInMillis());
        state.putString("cached_start_text", labelStartKickTime.getText().toString());
        state.putString("cached_recent_text", labelRecentKickTime.getText().toString());
        return state;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_baby_kick_counter_main, container, false);
        scrollView = (ScrollView)root;
        cachedRootView = root;

        labelCounterStatus = root.findViewById(R.id.labelKickCounterStatus);
        labelCounterTimer = root.findViewById(R.id.labelKickCounterTimer);
        labelKicks = root.findViewById(R.id.labelKicks);
        labelStartKickTime = root.findViewById(R.id.labelStartKickTime);
        labelRecentKickTime = root.findViewById(R.id.labelRecentKickTime);


        cardBabyKicked = root.findViewById(R.id.cardBabyKicked);
        cardBabyKicked.setOnClickListener(v ->
        {
            if (!timer.isTimerRunning())
            {
                timer.start();
                labelCounterStatus.setText(getString(R.string.status_kick_counter_in_progress));

                Calendar now = Calendar.getInstance(Locale.getDefault());
                startDate = now.getTime();
                labelStartKickTime.setText(fullTimeFormat.format(startDate));
            }

            kicks += 1;
            labelKicks.setText(String.valueOf(kicks));

            Calendar now = Calendar.getInstance(Locale.getDefault());
            labelRecentKickTime.setText(fullTimeFormat.format(now.getTime()));
        });

        btnResetKickCounter = root.findViewById(R.id.btnResetKickCounter);
        btnResetKickCounter.setOnClickListener(v ->
        {
            if (!timer.isTimerRunning())
                return;

            String message = getString(R.string.confirm_reset_kick_counter);
            String title = getString(R.string.confirm_reset_kick_counter_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                resetCounter();
            });
        });

        btnFinishKickCounting = root.findViewById(R.id.btnFinishKickCounting);
        btnFinishKickCounting.setOnClickListener(v ->
        {
            if (!timer.isTimerRunning())
                return;

            timeFinishedMillis = timeElapsedMillis;
            String message = getString(R.string.confirm_finish_kick_counter);
            String title = getString(R.string.confirm_finish_kick_counter_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                finishCounting();
            });
        });

        if (savedInstanceState != null && savedState == null && timer != null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
        {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(savedState.getLong("cached_start_time"));
            startDate = calendar.getTime();

            labelCounterStatus.setText(savedState.getString("cached_status_txt"));
            labelKicks.setText(savedState.getString("cached_kicks"));
            labelStartKickTime.setText(savedState.getString("cached_start_text"));
            labelRecentKickTime.setText(savedState.getString("cached_recent_text"));
            labelCounterTimer.setText(savedState.getString("cached_timer_text"));
        }
        else
        {
            timer = new TimerUtil((millis, hours, minutes, seconds) ->
            {
                timeElapsedMillis = millis;
                labelCounterTimer.setText(String.format(manualTimeFormat, TimerUtil.forceTwoDigits((int)hours),
                        TimerUtil.forceTwoDigits((int)minutes), TimerUtil.forceTwoDigits((int)seconds)));
            });
        }
        savedState = null;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->
        {
            if (PreferencesUtil.firstBabyKickCounting(getContext()))
                TapTargetUtil.showKickCounterGuides(getActivity(), scrollView, labelCounterStatus, labelCounterTimer,
                        cardBabyKicked, labelKicks, labelStartKickTime, labelRecentKickTime, btnResetKickCounter, btnFinishKickCounting);
        }, 300);
    }

    @Override
    public void onDetach()
    {
        timer.stop();
        super.onDetach();
    }

    private void resetCounter()
    {
        timer.stop();
        kicks = 0;

        labelCounterStatus.setText(getString(R.string.initial_kick_counter_status_message));
        labelCounterTimer.setText(getString(R.string.initial_timer_value));
        labelStartKickTime.setText(getString(R.string.initial_timer_value));
        labelRecentKickTime.setText(getString(R.string.initial_timer_value));
        labelKicks.setText("0");
    }

    private void finishCounting()
    {
        Calendar now = Calendar.getInstance(Locale.getDefault());
        String date = simpleDateFormat.format(now.getTime());
        String startTime = fullTimeFormat.format(startDate);
        String duration = TimerUtil.formatDuration(timeFinishedMillis);

        if (sessionDataFile == null)
            sessionDataFile = FileUtil.newKickFile(getContext());

        if (!FileUtil.writeKickData(sessionDataFile, date, startTime, duration, kicks))
        {
            AlertUtil.showAlert(getContext(), getString(R.string.error_message_header), getString(R.string.error_save_data_failed));
        }
        else
        {
            sessionDataFile = null;
            restart(1);
        }
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
    public void onBackpressed (AppCompatActivity activity)
    {
        if (timer.isTimerRunning())
        {
            String message = getString(R.string.confirm_exit_text);
            String title = getString(R.string.confirm_exit_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                timer.stop();
                activity.finish();
            });
        }
        else
            activity.finish();
    }

    @Override
    public void onRequestGuide(ViewPager viewPager)
    {
        resetCounter();
        TapTargetUtil.showKickCounterGuides(getActivity(), scrollView, labelCounterStatus, labelCounterTimer,
                cardBabyKicked, labelKicks, labelStartKickTime, labelRecentKickTime, btnResetKickCounter, btnFinishKickCounting);
    }
}

































