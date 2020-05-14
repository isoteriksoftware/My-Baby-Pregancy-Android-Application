package com.isoterik.android.mybaby.fragments.baby_kick_counter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.utils.AlertUtil;
import com.isoterik.android.mybaby.utils.FileUtil;
import com.isoterik.android.mybaby.utils.PregnancyDataProvider;
import com.isoterik.android.mybaby.utils.TimerUtil;

public class TimelineFragment extends Fragment
{
    private View cachedRootView;
    private boolean instantiated;

    private TextView labelTotalKicks;
    private TextView labelTotalDuration;
    private TextView labelTotalSessions;
    private TextView labelKicksPerSession;
    private Button btnCLearData;

    private String[] kicksData;

    private Bundle savedState = null;

    public TimelineFragment(){}
    public  TimelineFragment (String[] kicksData)
    {
        this.kicksData = kicksData;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBundle("cached_bundle", (savedState != null) ? savedState : saveState());
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        savedState = saveState();
        kicksData = null;
    }

    private Bundle saveState()
    {
        Bundle state = new Bundle();
        state.putStringArray("cached_data", kicksData);
        return state;
    }

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
        View root = inflater.inflate(R.layout.fragment_baby_kick_counter_timeline, container, false);

        if (savedInstanceState != null && savedState == null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
            kicksData = savedState.getStringArray("cached_data");
        savedState = null;

        labelKicksPerSession = root.findViewById(R.id.colKicksPerSession);
        labelTotalDuration = root.findViewById(R.id.colTotalKickDuration);
        labelTotalKicks = root.findViewById(R.id.colTotalKicks);
        labelTotalSessions = root.findViewById(R.id.colTotalKickSessions);
        btnCLearData = root.findViewById(R.id.btnClearKicksData);
        btnCLearData.setOnClickListener(v ->
        {
            String message = getString(R.string.confirm_clear_kick_counter_data_text);
            String title = getString(R.string.confirm_clear_kick_counter_data_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                FileUtil.clearKicksData(getContext());
                restart();
            });
        });

        int[] kicksStats = PregnancyDataProvider.getKicksStats(kicksData);
        int totalKicks = kicksStats[PregnancyDataProvider.TOTAL_KICKS_INDEX];
        String totalDuration = TimerUtil.formatDurationSeconds(kicksStats[PregnancyDataProvider.TOTAL_KICKS_DURATION_INDEX]);
        int totalSessions = kicksStats[PregnancyDataProvider.TOTAL_KICKS_SESSIONS_INDEX];
        int kicksPerSession = kicksStats[PregnancyDataProvider.TOTAL_KICKS_PER_SESSION_INDEX];

        labelTotalKicks.setText(String.valueOf(totalKicks));
        labelTotalDuration.setText(totalDuration);
        labelTotalSessions.setText(String.valueOf(totalSessions));
        labelKicksPerSession.setText(String.valueOf(kicksPerSession));

        return root;
    }

    private void restart()
    {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}



























