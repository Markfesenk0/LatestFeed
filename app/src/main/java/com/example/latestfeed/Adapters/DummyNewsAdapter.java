package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.Entities.News;
import com.example.latestfeed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DummyNewsAdapter extends RecyclerView.Adapter<DummyNewsAdapter.DummyNewsViewHolder> {

    ArrayList<String> dummyNewsList;

    public DummyNewsAdapter() {
        this.dummyNewsList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            dummyNewsList.add("a");
        }
    }

    @NonNull
    @Override
    public DummyNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_news_design, parent, false);
        return new DummyNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DummyNewsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dummyNewsList.size();
    }

    public static class DummyNewsViewHolder extends RecyclerView.ViewHolder {

        public DummyNewsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
