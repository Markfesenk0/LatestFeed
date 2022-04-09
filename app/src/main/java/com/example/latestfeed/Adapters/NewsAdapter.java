package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.Entities.News;
import com.example.latestfeed.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<News> newsList;
    private OnNewsListener mOnNewsListener;

    public NewsAdapter(ArrayList<News> insertList, OnNewsListener onNewsListener) {
        this.newsList = insertList;
        this.mOnNewsListener = onNewsListener;
    }

    public void insertDate(ArrayList<News> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<News> diffUtilCallback = new MyDiffUtilCallback<>(newsList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        newsList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<News> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<News> diffUtilCallback = new MyDiffUtilCallback<>(newsList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        newsList.clear();
        newsList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new NewsViewHolder(view, mOnNewsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        try {
            Picasso.get().load(news.getImgUrl()).into(holder.image);
        } catch (IllegalArgumentException e) {
            Picasso.get().load(R.drawable.placeholder).into(holder.image);
            System.out.println(e.getMessage());
        }
        holder.title.setText(news.getTitle());
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.news_design;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        OnNewsListener onNewsListener;

        public NewsViewHolder(@NonNull View itemView, OnNewsListener onNewsListener) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            this.onNewsListener = onNewsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNewsListener.onNewsClick(getAdapterPosition());
        }
    }

    public interface OnNewsListener{
        void onNewsClick(int position);
    }
}
