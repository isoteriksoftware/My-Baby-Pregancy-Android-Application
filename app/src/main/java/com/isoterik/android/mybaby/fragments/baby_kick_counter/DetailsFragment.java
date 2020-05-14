package com.isoterik.android.mybaby.fragments.baby_kick_counter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoterik.android.mybaby.R;
import com.isoterik.android.mybaby.utils.FileUtil;

public class DetailsFragment extends Fragment
{
    private String[] kicksData;

    private Bundle savedState = null;

    public DetailsFragment()
    {}

    public DetailsFragment (String[] kicksData)
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
        View root = inflater.inflate(R.layout.fragment_baby_kick_counter_details, container, false);

        if (savedInstanceState != null && savedState == null)
            savedState = savedInstanceState.getBundle("cached_bundle");
        if (savedState != null)
            kicksData = savedState.getStringArray("cached_data");
        savedState = null;

        RecyclerView dataList = root.findViewById(R.id.kicksDataList);
        dataList.setHasFixedSize(true);
        dataList.setLayoutManager(new LinearLayoutManager(getContext()));
        dataList.setAdapter(new ItemsAdapter());

        return root;
    }

    class ItemsAdapter extends RecyclerView.Adapter<DetailsFragment.ViewHolder>
    {

        @NonNull
        @Override
        public DetailsFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new DetailsFragment.ViewHolder(getLayoutInflater().inflate(R.layout.baby_kick_counter_details_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DetailsFragment.ViewHolder holder, int position)
        {
            String[] data = kicksData[position].split(FileUtil.LINE_DATA_SEPARATOR);
            holder.bindModel(data);
        }

        @Override
        public int getItemCount()
        {
            return kicksData.length;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView labelDate;
        private TextView labelStart;
        private TextView labelDuration;
        private TextView labelKicks;

        public ViewHolder (@NonNull View itemView)
        {
            super(itemView);
            labelDate = itemView.findViewById(R.id.labelKickDataDate);
            labelStart = itemView.findViewById(R.id.labelKickDataStart);
            labelDuration = itemView.findViewById(R.id.labelKickDataDuration);
            labelKicks = itemView.findViewById(R.id.labelKickDataKicks);
        }

        public void bindModel (String[] data)
        {
            labelDate.setText(data[0]);
            labelStart.setText(data[1]);
            labelDuration.setText(data[2]);
            labelKicks.setText(data[3]);
        }
    }
}
























