package com.isoterik.android.mybaby.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isoterik.android.mybaby.R;

public class NoDataFragment extends Fragment
{
    private View cachedRootView;
    private boolean instantiated;

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

        View root = inflater.inflate(R.layout.fragment_no_data, container, false);
        cachedRootView = root;

        return root;
    }
}
