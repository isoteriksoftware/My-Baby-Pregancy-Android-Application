package com.isoterik.android.mybaby;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isoterik.android.mybaby.utils.Misc;
import com.isoterik.android.mybaby.utils.PregnancyDataProvider;
import com.isoterik.android.mybaby.utils.ResourcesProvider;

public class PregnancyTipsActivity extends AppCompatActivity
{
    private String[] pregnancyTips;
    private Drawable[] tipsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_tips);

        pregnancyTips = getResources().getStringArray(R.array.pregnancy_tips);
        int[] imagesIds = Misc.getResourcesIdArray(this, R.array.tips_images);
        tipsImages = new Drawable[imagesIds.length];
        for (int i = 0; i < imagesIds.length; i++)
            tipsImages[i] = getResources().getDrawable(imagesIds[i]);

        RecyclerView recyclerView = findViewById(R.id.pregnancyTipsList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ItemsAdapter());

        Intent intent = getIntent();
        int currentItem = intent.getIntExtra(Misc.EXTRA_CURRENT_ITEM, -1);
        if (currentItem == -1)
            currentItem = PregnancyDataProvider.validatePregnancyWeeksIndex(PregnancyDataProvider.getPregnancyWeeks(this));

        recyclerView.scrollToPosition(currentItem);
    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }

    private class ItemsAdapter extends RecyclerView.Adapter<PregnancyTipsActivity.ViewHolder>
    {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            return new PregnancyTipsActivity.ViewHolder(getLayoutInflater().inflate(R.layout.pregnancy_tips_row, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            String week = String.format(getString(R.string.pregnancy_week_format), position + 1);
            Spanned data = Html.fromHtml(pregnancyTips[position]);
            holder.bindModel(week, data, tipsImages[position]);
        }

        @Override
        public int getItemCount()
        {
            return pregnancyTips.length;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView header;
        private TextView data;
        private ImageView image;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            header = itemView.findViewById(R.id.labelTipWeek);
            data = itemView.findViewById(R.id.labelTipWeekData);
            image = itemView.findViewById(R.id.tipImage);
        }

        public void bindModel (String headerTxt, Spanned dataTxt, Drawable tipImage)
        {
            header.setText(headerTxt);
            data.setText(dataTxt);
            image.setImageDrawable(tipImage);
        }
    }
}









































