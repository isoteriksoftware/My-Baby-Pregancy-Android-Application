package com.isoterik.android.mybaby.fragments.contraction_timer;

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
    private TextView labelTotalContractions;
    private TextView labelTotalDuration;
    private TextView labelTotalSessions;
    private TextView labelContractionsPerSession;
    private TextView labelTotalRest;
    private Button btnCLearData;

    private String[] contractionsData;

    private Bundle savedState = null;

    public TimelineFragment(){}
    public  TimelineFragment (String[] contractionsData)
    {
        this.contractionsData = contractionsData;
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
        contractionsData = null;
    }

    private Bundle saveState()
    {
        Bundle state = new Bundle();
        state.putStringArray("cached_data", contractionsData);
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
        View root = inflater.inflate(R.layout.fragment_contraction_timer_timeline, container, false);

        if (savedInstanceState != null && savedState == null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
            contractionsData = savedState.getStringArray("cached_data");
        savedState = null;

        labelContractionsPerSession = root.findViewById(R.id.colContractionsPerSession);
        labelTotalDuration = root.findViewById(R.id.colTotalContractionDuration);
        labelTotalContractions = root.findViewById(R.id.colTotalContractions);
        labelTotalSessions = root.findViewById(R.id.colTotalContractionSessions);
        labelTotalRest = root.findViewById(R.id.colTotalContractionRest);
        btnCLearData = root.findViewById(R.id.btnClearContractionData);
        btnCLearData.setOnClickListener(v ->
        {
            String message = getString(R.string.confirm_clear_contraction_timer_data_text);
            String title = getString(R.string.confirm_clear_contraction_timer_data_title);
            AlertUtil.showConfirmDialog(getContext(), title, message, () ->
            {
                FileUtil.clearContractionsData(getContext());
                restart();
            });
        });

        int[] contractionsStats = PregnancyDataProvider.getContractionsStats(contractionsData);
        int totalContractions = contractionsStats[PregnancyDataProvider.TOTAL_CONTRACTIONS_INDEX];
        String totalDuration = TimerUtil.formatDurationSeconds(contractionsStats[PregnancyDataProvider.TOTAL_CONTRACTIONS_DURATION_INDEX]);
        String totalRest = TimerUtil.formatDurationSeconds(contractionsStats[PregnancyDataProvider.TOTAL_CONTRACTIONS_REST_INDEX]);
        int totalSessions = contractionsStats[PregnancyDataProvider.TOTAL_CONTRACTIONS_SESSIONS_INDEX];
        int contractionsPerSession = contractionsStats[PregnancyDataProvider.TOTAL_CONTRACTIONS_PER_SESSION_INDEX];

        labelTotalContractions.setText(String.valueOf(totalContractions));
        labelTotalDuration.setText(totalDuration);
        labelTotalRest.setText(totalRest);
        labelTotalSessions.setText(String.valueOf(totalSessions));
        labelContractionsPerSession.setText(String.valueOf(contractionsPerSession));

        return root;
    }

    private void restart()
    {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }
}
