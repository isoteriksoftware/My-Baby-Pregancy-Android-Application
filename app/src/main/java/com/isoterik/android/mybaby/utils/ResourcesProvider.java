package com.isoterik.android.mybaby.utils;

import android.graphics.drawable.Drawable;

public class ResourcesProvider
{
    private static Drawable[] tipsImages;

    public static void setTipsImages (Drawable[] tipsImages)
    { ResourcesProvider.tipsImages = tipsImages; }

    public static Drawable[] getTipsImages()
    { return tipsImages; }
}