package com.isoterik.android.mybaby.fragments.contraction_timer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.fragments.BaseListenerFragment;
import com.isoterik.android.mybaby.utils.AlertUtil;
import com.isoterik.android.mybaby.utils.FileUtil;
import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.PreferencesUtil;
import com.isoterik.android.mybaby.utils.TapTargetUtil;
import com.isoterik.android.mybaby.utils.TimerUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainFragment extends BaseListenerFragment
{
    private ScrollView scrollView;

    private TimerUtil timer;

    private TextView labelStatus;
    private TextView labelTime;
    private TextView labelContractions;
    private TextView labelStartTime;
    private TextView labelRecentContractionTime;
    private CardView cardStartTimer;
    private ImageView  cardStartTimerImage;
    private Button btnResetTimer;
    private Button btnFinishTiming;

    private Drawable drawableTimerOn, drawableTimerOff;

    private long startTimeMillis;
    private Date startDate;
    String manualTimeFormat;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat fullTimeFormat;
    private SimpleDateFormat simpleDateFormat;
    private int contractions;
    private long timeElapsedMillis;
    private long timeFinishedMillis;

    private List<Integer> contractionsDuration;
    private List<Integer> restDuration;

    private File sessionDataFile;

    private Bundle savedState = null;

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
        state.putString("cached_status_txt", labelStatus.getText().toString());
        state.putString("cached_timer_text", labelTime.getText().toString());
        state.putString("cached_contractions", labelContractions.getText().toString());
        state.putLong("cached_start_time", timer.getStartDate().getTimeInMillis());
        state.putString("cached_start_text", labelStartTime.getText().toString());
        state.putString("cached_recent_text", labelRecentContractionTime.getText().toString());
        state.putBoolean("cached_is_resting", (cardStartTimerImage.getDrawable() == drawableTimerOn));
        return state;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        manualTimeFormat = getString(R.string.manual_time_format);
        timeFormat = new SimpleDateFormat(getString(R.string.time_format));
        fullTimeFormat = new SimpleDateFormat(getString(R.string.time_format_full));
        simpleDateFormat = new SimpleDateFormat(getString(R.string.simple_date_format));

        contractionsDuration = new ArrayList<>();
        restDuration = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_contraction_timer_main, container, false);
        scrollView = (ScrollView)root;

        drawableTimerOn = getContext().getDrawable(R.drawable.ic_timer_white_24dp);
        drawableTimerOff = getContext().getDrawable(R.drawable.ic_timer_off_white_24dp);

        labelStatus = root.findViewById(R.id.labelContractionTimerStatus);
        labelTime = root.findViewById(R.id.labelContractionTimerTime);
        labelContractions = root.findViewById(R.id.labelContractions);
        labelStartTime = root.findViewById(R.id.labelStartContractionTimerTime);
        labelRecentContractionTime = root.findViewById(R.id.labelRecentContractionTime);
        cardStartTimer = root.findViewById(R.id.cardStartContractionTiming);
        cardStartTimerImage = cardStartTimer.findViewById(R.id.cardStartContractionTimingImage);
        cardStartTimerImage.setImageDrawable(drawableTimerOn);
        cardStartTimer.setOnClickListener(v ->
        {
            if (cardStartTimerImage.getDrawable() == drawableTimerOn)
            {
                if (!timer.isTimerRunning())
                {
                    Calendar now = Calendar.getInstance(Locale.getDefault());
                    startDate = now.getTime();
                    labelStartTime.setText(fullTimeFormat.format(startDate));
                }

                labelStatus.setText(getString(R.string.status_contraction_timer_in_progress));
                cardStartTimerImage.setImageDrawable(drawableTimerOff);

                contractions += 1;
                labelContractions.setText(String.valueOf(contractions));
                Calendar now = Calendar.getInstance(Locale.getDefault());
                labelRecentContractionTime.setText(fullTimeFormat.format(now.getTime()));

                if (timer.getStartTime() != 0) // if startTimeMillis is 0, it means there is no rest to record yet
                {
                    long elapsed = System.currentTimeMillis() - timer.getStartTime();
                    int restSeconds = (int)Math.ceil(TimeUnit.MILLISECONDS.toSeconds(elapsed));
                    restDuration.add(restSeconds);
                }

                timer.stop();
                timer.start();
            }
            else
            {
                cardStartTimerImage.setImageDrawable(drawableTimerOn);

                long elapsed = System.currentTimeMillis() - timer.getStartTime();
                int restSeconds = (int)Math.ceil(TimeUnit.MILLISECONDS.toSeconds(elapsed));
                contractionsDuration.add(restSeconds);

                labelStatus.setText(getString(R.string.status_resting_in_progress));
                timer.stop();
                timer.start();
            }
        });

        btnResetTimer = root.findViewById(R.id.btnResetContractionTimer);
        btnResetTimer.setOnClickListener(v ->
        {
            if (!timer.isTimerRunning())
                return;

            String message = getString(R.string.confirm_reset_contraction_timer);
            String title = getString(R.string.confirm_reset_contraction_timer_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                resetTimer();
            });
        });

        btnFinishTiming = root.findViewById(R.id.btnFinishContractionTiming);
        btnFinishTiming.setOnClickListener(v ->
        {
            if (!timer.isTimerRunning())
                return;

            timeFinishedMillis = timeElapsedMillis;
            String message = getString(R.string.confirm_finish_contraction_timer);
            String title = getString(R.string.confirm_finish_contraction_timer_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                finishTiming();
            });
        });

        if (savedInstanceState != null && savedState == null && timer != null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(savedState.getLong("cached_start_time"));
            startDate = calendar.getTime();

            labelStatus.setText(savedState.getString("cached_status_txt"));
            labelContractions.setText(savedState.getString("cached_contractions"));
            labelStartTime.setText(savedState.getString("cached_start_text"));
            labelRecentContractionTime.setText(savedState.getString("cached_recent_text"));
            labelTime.setText(savedState.getString("cached_timer_text"));

            if (savedState.getBoolean("cached_is_resting"))
                cardStartTimerImage.setImageDrawable(drawableTimerOn);
            else
                cardStartTimerImage.setImageDrawable(drawableTimerOff);
        }
        else
        {
            timer = new TimerUtil((millis, hours, minutes, seconds) ->
            {
                timeElapsedMillis = millis;
                labelTime.setText(String.format(manualTimeFormat, TimerUtil.forceTwoDigits((int)hours),
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
            if (PreferencesUtil.firstContractionTiming(getContext()))
                TapTargetUtil.showContractionsTimerGuides(getActivity(), scrollView, labelStatus, labelTime,
                        labelContractions, cardStartTimer, labelStartTime, labelRecentContractionTime, btnResetTimer,
                        btnFinishTiming);
        }, 300);
    }

    @Override
    public void onDetach()
    {
        if (timer.isTimerRunning())
            timer.stop();

        super.onDetach();
    }

    private void resetTimer()
    {
        timer.stop();
        contractions = 0;
        contractionsDuration.clear();
        restDuration.clear();

        labelStatus.setText(R.string.initial_contraction_timer_status_message);
        labelTime.setText(R.string.initial_timer_value);
        labelStartTime.setText(R.string.initial_timer_value);
        labelRecentContractionTime.setText(R.string.initial_timer_value);
        labelContractions.setText("0");
        cardStartTimerImage.setImageDrawable(drawableTimerOn);
    }

    private void finishTiming()
    {
        Calendar now = Calendar.getInstance(Locale.getDefault());
        String date = simpleDateFormat.format(now.getTime());
        String startTime = fullTimeFormat.format(startDate);

        // calculate how long it was since the Finish button was first clicked
        int secondsElapsed = (int)Math.ceil(TimeUnit.MILLISECONDS.toSeconds(timeFinishedMillis));
        if (secondsElapsed > 0)
        {
            if (cardStartTimerImage.getDrawable() == drawableTimerOn) // the user was resting
                restDuration.add(secondsElapsed);
            else
                contractionsDuration.add(secondsElapsed); // contraction timing was On
        }

        int totalDuration = 0;
        for (int dur : contractionsDuration)
            totalDuration += dur;

        int totalRestDuration = 0;
        for (int dur : restDuration)
            totalRestDuration += dur;

        String duration = TimerUtil.formatDurationSeconds(totalDuration);
        String restDuration = TimerUtil.formatDurationSeconds(totalRestDuration);

        if (sessionDataFile == null)
            sessionDataFile = FileUtil.newContractionFile(getContext());

        if (!FileUtil.writeContractionData(sessionDataFile, date, startTime, duration, restDuration, contractions))
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
        resetTimer();
        TapTargetUtil.showContractionsTimerGuides(getActivity(), scrollView, labelStatus, labelTime,
                labelContractions, cardStartTimer, labelStartTime, labelRecentContractionTime, btnResetTimer,
                btnFinishTiming);
    }
}
























