package com.isoterik.android.mybaby.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoterik.android.mybaby.R;

public class InfoFragment extends Fragment
{
    private String header;

    @ArrayRes private int infoArrayResource;

    private String[] infoArray;

    private Bundle savedState = null;

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void setInfoArrayResource(int infoArrayResource)
    {
        this.infoArrayResource = infoArrayResource;
    }

    public static InfoFragment create (String header, @ArrayRes int infoArrayResource)
    {
        InfoFragment fragment = new InfoFragment();
        fragment.setHeader(header);
        fragment.setInfoArrayResource(infoArrayResource);
        return fragment;
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
        header = null;
        infoArray = null;
    }

    private Bundle saveState()
    {
        Bundle state = new Bundle();
        state.putStringArray("cached_info_array", infoArray);
        state.putString("cached_header", header);
        return state;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_info, container, false);

        if (savedInstanceState != null && savedState == null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
        {
            infoArray = savedState.getStringArray("cached_info_array");
            header = savedState.getString("cached_header");
        }
        else
        {
            infoArray = getResources().getStringArray(infoArrayResource);
        }
        savedState = null;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemsAdapter());

        return recyclerView;
    }

    private class ItemsAdapter extends RecyclerView.Adapter<InfoFragment.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new InfoFragment.ViewHolder(getLayoutInflater().inflate(R.layout.info_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            holder.bindModel(header, infoArray[position]);
        }

        @Override
        public int getItemCount()
        {
            return infoArray.length;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView headerTxt;
        private TextView infoTxt;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            headerTxt = itemView.findViewById(R.id.infoHeader);
            infoTxt = itemView.findViewById(R.id.infoData);
        }

        public void bindModel (String header, String info)
        {
            headerTxt.setText(header);
            infoTxt.setText(info);
        }
    }
}

































