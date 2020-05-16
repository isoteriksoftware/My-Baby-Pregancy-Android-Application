package com.isoterik.android.mybaby.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.isoterik.android.mybaby.R;

public class TapTargetUtil
{
    public static TapTarget viewTarget (View view, String title, String description, int targetRadius, boolean cancelable)
    {
        return TapTarget.forView(view, title, description)
                .dimColor(android.R.color.black)
                .drawShadow(true)
                .transparentTarget(true)
                .targetRadius(targetRadius)
                .cancelable(cancelable);
    }

    public static TapTarget viewTarget (View view, String title, String description, boolean cancelable)
    {
        view.measure(0, 0);
        int radius = Misc.pxToDp(view.getContext(), view.getWidth()/2);
        return  viewTarget(view, title, description, radius, cancelable);
    }

    public static TapTarget viewTarget (View view, String title, String description)
    { return  viewTarget(view, title, description, true); }

    public static void show (Activity activity, TapTarget target, TapTargetView.Listener listener)
    {
        TapTargetView.showFor(activity, target, listener);
    }

    public static void show (Activity activity, TapTarget target)
    {
        TapTargetView.showFor(activity, target);
    }

    public static TapTargetSequence sequence (Activity activity, TapTargetSequence.Listener listener, TapTarget... targets)
    {
        return new TapTargetSequence(activity).targets(targets).listener(listener);
    }

    public static TapTarget overflowTarget (Toolbar toolbar, String title, String description, int targetRadius, boolean cancelable)
    {
        return TapTarget.forToolbarOverflow(toolbar, title, description)
                .targetRadius(targetRadius)
                .cancelable(cancelable)
                .dimColor(android.R.color.black)
                .drawShadow(true);
    }

    public static TapTarget menuItemTarget (Toolbar toolbar, int menuId, String title, String description, int targetRadius, boolean cancelable)
    {
        return TapTarget.forToolbarMenuItem(toolbar, menuId, title, description)
                .targetRadius(targetRadius)
                .cancelable(cancelable)
                .dimColor(android.R.color.black)
                .drawShadow(true);
    }

    public static void showMainGuides (Activity activity, NestedScrollView scrollView, TextView pregTrackerTxt, TextView kickCounterTxt, TextView conTimerTxt,
                                       TextView pregTipsTxt)
    {
        if (true)
        return;

        boolean cancelable = !PreferencesUtil.firstTimeLaunched(activity);
        Resources res = activity.getResources();

        TapTarget t1 = viewTarget(pregTrackerTxt, res.getString(R.string.main_guide_pregnancy_tracker_title), res.getString(R.string.main_guide_pregnancy_tracker_desc),
                cancelable).transparentTarget(false);
        TapTarget t2 = viewTarget(kickCounterTxt, res.getString(R.string.main_guide_kick_counter_title), res.getString(R.string.main_guide_kick_counter_desc),
                cancelable).transparentTarget(false);
        TapTarget t3 = viewTarget(conTimerTxt, res.getString(R.string.main_guide_contraction_timer_title), res.getString(R.string.main_guide_contraction_timer_desc),
                cancelable).transparentTarget(false);
        TapTarget t4 = viewTarget(pregTipsTxt, res.getString(R.string.main_guide_pregnancy_tips_title), res.getString(R.string.main_guide_pregnancy_tips_desc),
                cancelable).transparentTarget(false);

        sequence(activity, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish()
            {
                PreferencesUtil.firstTimeLaunched(activity, false);
                restartActivity(activity);
            }

            @Override
            public void onSequenceStep (TapTarget lastTarget, boolean targetClicked)
            {
                if (lastTarget == null)
                    return;

                View view = null;
                if (lastTarget == t1)
                    view = pregTrackerTxt;
                else if (lastTarget == t2)
                    view = kickCounterTxt;
                else if (lastTarget == t3)
                    view = conTimerTxt;
                else
                    view = pregTipsTxt;

                int scroll = (int)view.getY();
                scrollView.fling(scroll * 5);
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) { restartActivity(activity); }
        }, t1, t2, t3, t4).continueOnCancel(!cancelable).start();
    }

    public static void showKickCounterGuides (Activity activity, ScrollView scrollView, View status, View timer, View foot, View kicks,
                                              View start, View recent, View reset, View finish)
    {
        if (true)
            return;

        boolean cancelable = !PreferencesUtil.firstBabyKickCounting(activity);
        Resources res = activity.getResources();

        TapTarget t1 = viewTarget(status, res.getString(R.string.kick_counter_guide_status_title), res.getString(R.string.kick_counter_guide_status_desc),
                cancelable).targetRadius(Misc.pxToDp(activity, (int)(status.getMeasuredWidth()/2.5f)));
        TapTarget t2 = viewTarget(timer, res.getString(R.string.kick_counter_guide_timer_title), res.getString(R.string.kick_counter_guide_timer_desc),
                cancelable);
        TapTarget t3 = viewTarget(foot, res.getString(R.string.kick_counter_guide_foot_title), res.getString(R.string.kick_counter_guide_foot_desc),
                cancelable);
        TapTarget t4 = viewTarget(kicks, res.getString(R.string.kick_counter_guide_kicks_title), res.getString(R.string.kick_counter_guide_kicks_desc),
                cancelable);
        TapTarget t5 = viewTarget(start, res.getString(R.string.kick_counter_guide_start_title), res.getString(R.string.kick_counter_guide_start_desc),
                cancelable);
        TapTarget t6 = viewTarget(recent, res.getString(R.string.kick_counter_guide_recent_title), res.getString(R.string.kick_counter_guide_recent_desc),
                cancelable);
        TapTarget t7 = viewTarget(reset, res.getString(R.string.kick_counter_guide_reset_title), res.getString(R.string.kick_counter_guide_reset_desc),
                cancelable);
        TapTarget t8 = viewTarget(finish, res.getString(R.string.kick_counter_guide_finish_title), res.getString(R.string.kick_counter_guide_finish_desc),
                cancelable);

        sequence(activity, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish()
            {
                PreferencesUtil.firstBabyKickCounting(activity, false);
            }

            @Override
            public void onSequenceStep (TapTarget lastTarget, boolean targetClicked)
            {
                if (lastTarget == null)
                    return;

                View view = foot;
                int scroll = (int)view.getY();
                scrollView.fling(scroll * 5);
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) { }
        }, t1, t2, t3, t4, t5, t6, t7, t8).continueOnCancel(!cancelable).start();
    }

    public static void showContractionsTimerGuides (Activity activity, ScrollView scrollView, View status, View timer, View contractions, View timerTrigger,
                                              View start, View recent, View reset, View finish)
    {
        if (true)
            return;

        boolean cancelable = !PreferencesUtil.firstContractionTiming(activity);
        Resources res = activity.getResources();

        TapTarget t1 = viewTarget(status, res.getString(R.string.contraction_timer_guide_status_title), res.getString(R.string.contraction_timer_guide_status_desc),
                cancelable).targetRadius(Misc.pxToDp(activity, (int)(status.getMeasuredWidth()/2.5f)));
        TapTarget t2 = viewTarget(timer, res.getString(R.string.contraction_timer_guide_timer_title), res.getString(R.string.contraction_timer_guide_timer_desc),
                cancelable);
        TapTarget t3 = viewTarget(timerTrigger, res.getString(R.string.contraction_timer_guide_timerIcon_title), res.getString(R.string.contraction_timer_guide_timerIcon_desc),
                cancelable);
        TapTarget t4 = viewTarget(contractions, res.getString(R.string.contraction_timer_guide_contractions_title), res.getString(R.string.contraction_timer_guide_contractions_desc),
                cancelable);
        TapTarget t5 = viewTarget(start, res.getString(R.string.contraction_timer_guide_start_title), res.getString(R.string.contraction_timer_guide_start_desc),
                cancelable);
        TapTarget t6 = viewTarget(recent, res.getString(R.string.contraction_timer_guide_recent_title), res.getString(R.string.contraction_timer_guide_recent_desc),
                cancelable);
        TapTarget t7 = viewTarget(reset, res.getString(R.string.contraction_timer_guide_reset_title), res.getString(R.string.contraction_timer_guide_reset_desc),
                cancelable);
        TapTarget t8 = viewTarget(finish, res.getString(R.string.contraction_timer_guide_finish_title), res.getString(R.string.contraction_timer_guide_finish_desc),
                cancelable);

        sequence(activity, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish()
            {
                PreferencesUtil.firstContractionTiming(activity, false);
            }

            @Override
            public void onSequenceStep (TapTarget lastTarget, boolean targetClicked)
            {
                if (lastTarget == null)
                    return;

                View view = timerTrigger;
                int scroll = (int)view.getY();
                scrollView.fling(scroll * 5);
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) { }
        }, t1, t2, t4, t3, t5, t6, t7, t8).continueOnCancel(!cancelable).start();
    }

    public static void showPregnancyTrackerInitialGuides (Activity activity, View start)
    {
        if (true)
            return;

        boolean cancelable = !PreferencesUtil.firstPregnancyTrackingInitial(activity);
        Resources res = activity.getResources();

        TapTarget t = viewTarget(start, res.getString(R.string.pregnancy_tracker_initial_guide_title), res.getString(R.string.pregnancy_tracker_initial_guide_desc),
                cancelable);

        show(activity, t, new TapTargetView.Listener()
        {
            @Override
            public void onTargetClick(TapTargetView view)
            {
                super.onTargetClick(view);
                PreferencesUtil.firstPregnancyTrackingInitial(activity, false);
            }
        }
        );
    }

    public static void showPregnancyTrackerAfterLMPGuides (Activity activity, View weeks, View stop, View lmp, View changeLMP,
                                                           ViewPager viewPager)
    {
        if (true)
            return;

        boolean cancelable = !PreferencesUtil.firstPregnancyTrackingAfterLMP(activity);
        Resources res = activity.getResources();

        TapTarget t1 = viewTarget(weeks, res.getString(R.string.pregnancy_tracker_lmp_weeks_guide_title), res.getString(R.string.pregnancy_tracker_lmp_weeks_guide_desc),
                cancelable);
        TapTarget t2 = viewTarget(stop, res.getString(R.string.pregnancy_tracker_lmp_stop_guide_title), res.getString(R.string.pregnancy_tracker_lmp_stop_guide_desc),
                cancelable);
        TapTarget t3 = viewTarget(lmp, res.getString(R.string.pregnancy_tracker_lmp_lmpVal_guide_title), res.getString(R.string.pregnancy_tracker_lmp_lmpVal_guide_desc),
                cancelable);
        TapTarget t4 = viewTarget(changeLMP, res.getString(R.string.pregnancy_tracker_lmp_changeLMP_guide_title), res.getString(R.string.pregnancy_tracker_lmp_changeLMP_guide_desc),
                cancelable);

        sequence(activity, new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish()
            {
                PreferencesUtil.firstPregnancyTrackingAfterLMP(activity, false);
                viewPager.setCurrentItem(1);
            }

            @Override
            public void onSequenceStep (TapTarget lastTarget, boolean targetClicked)
            {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) { }
        }, t1, t2, t3, t4).continueOnCancel(!cancelable).start();
    }

    private static void restartActivity (Activity activity)
    {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }
}



































