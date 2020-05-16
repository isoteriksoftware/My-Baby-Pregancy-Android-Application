package com.isoterik.android.mybaby.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.ArrayRes;

public class Misc
{
    public static final String EXTRA_CURRENT_TAB = "extra_current_tab";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    public static int[] getResourcesIdArray (Context context, @ArrayRes int arrayResourceId)
    {
        TypedArray typedArray = context.getResources().obtainTypedArray(arrayResourceId);
        int[] items = new int[typedArray.length()];

        for (int i = 0; i < typedArray.length(); i++)
            items[i] = typedArray.getResourceId(i, -1);

        typedArray.recycle();
        return items;
    }

    public static int pxToDp (Context context, int px)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}




























