package com.isoterik.android.mybaby;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.isoterik.android.mybaby.utils.AlertUtil;
import com.isoterik.android.mybaby.utils.PreferencesUtil;
import com.isoterik.android.mybaby.utils.TapTargetUtil;

public class MainActivity extends AppCompatActivity
{

    private CardView cardKickCounter;
    private CardView cardPregnancyTracker;
    private CardView cardContractionTimer;
    private CardView cardPregnancyTips;

    private Toolbar toolbar;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        scrollView = findViewById(R.id.mainScrollView);

        cardPregnancyTracker = findViewById(R.id.cardPregnancyTracker);
        cardPregnancyTracker.setOnClickListener(this::onCardClicked);

        cardKickCounter = findViewById(R.id.cardBabyKickCounter);
        cardKickCounter.setOnClickListener(this::onCardClicked);

        cardContractionTimer = findViewById(R.id.cardContractionTimer);
        cardContractionTimer.setOnClickListener(this::onCardClicked);

        cardPregnancyTips = findViewById(R.id.cardPregnancyTips);
        cardPregnancyTips.setOnClickListener(this::onCardClicked);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->
        {
            if (PreferencesUtil.firstTimeLaunched(this))
                showMainGuides();
        }, 500);
    }

    private void showMainGuides()
    {
        TextView kickCounterText = cardKickCounter.findViewById(R.id.cardKickCounterText);
        TextView pregnancyTrackerText = cardPregnancyTracker.findViewById(R.id.cardPregnancyTrackerText);
        TextView contractionTimerText = cardContractionTimer.findViewById(R.id.cardContractionTimerText);
        TextView pregnancyTipsText = cardPregnancyTips.findViewById(R.id.cardPregnancyTipsText);
        TapTargetUtil.showMainGuides(this, scrollView, pregnancyTrackerText, kickCounterText, contractionTimerText,
                pregnancyTipsText);
    }

    private void onCardClicked (View v)
    {
        try
        {
            switch (v.getId())
            {
                case R.id.cardPregnancyTracker:
                    Intent intent = new Intent(this, PregnancyTrackerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardBabyKickCounter:
                    intent = new Intent(this, BabyKickCounterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardContractionTimer:
                    intent = new Intent(this, ContractionTimerActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardPregnancyTips:
                    intent = new Intent(this, PregnancyTipsActivity.class);
                    startActivity(intent);
                    break;
            }

        } catch (Exception e)
        {
            StringBuilder builder = new StringBuilder();
            builder.append("An error occurred, please screenshot and DM\n\n\n")
                    .append("Message: ").append(e.getMessage());
            AlertUtil.showAlert(this, "ERROR!", builder.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
       getMenuInflater().inflate(R.menu.main_menu, menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuItemGuide:
                showMainGuides();
                break;
            case R.id.menuItemAbout:
                showAboutDialog();
                break;
            case R.id.menuItemDisclaimer:
                showDisclaimerDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog()
    {
        View dialogView = getLayoutInflater().inflate(R.layout.about_dialog, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        dialogView.findViewById(R.id.exitDialog).setOnClickListener(v ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDisclaimerDialog()
    {
        View dialogView = getLayoutInflater().inflate(R.layout.disclaimer_dialog, null);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        dialogView.findViewById(R.id.exitDialog).setOnClickListener(v ->
        {
            dialog.dismiss();
        });

        dialog.show();
    }
}

























