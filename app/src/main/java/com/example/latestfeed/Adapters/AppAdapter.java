package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.R;
import com.example.latestfeed.Entities.App;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    ArrayList<App> apps;
    private OnAppListener mOnAppListener;

    public AppAdapter(ArrayList<App> apps, OnAppListener onAppListener) {
        this.apps = apps;
        this.mOnAppListener = onAppListener;
    }

    public void insertDate(ArrayList<App> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<App> diffUtilCallback = new MyDiffUtilCallback<>(apps, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        apps.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<App> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<App> diffUtilCallback = new MyDiffUtilCallback<>(apps, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        apps.clear();
        apps.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AppViewHolder(view, mOnAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        App app = apps.get(position);
        try {
            Picasso.get().load(app.getImgUrl()).into(holder.image);
        } catch (IllegalArgumentException e) {
            Picasso.get().load(R.drawable.placeholder).into(holder.image);
            System.out.println(e.getMessage());
        }
        holder.title.setText(app.getTitle());
        holder.artist.setText(app.getArtist());
        holder.price.setText(app.getPrice());
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.app_design;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title, artist, price;
        OnAppListener onAppListener;

        public AppViewHolder(@NonNull View itemView, OnAppListener onAppListener) {
            super(itemView);
            image = itemView.findViewById(R.id.app_image);
            title = itemView.findViewById(R.id.app_title);
            artist = itemView.findViewById(R.id.app_artist);
            price = itemView.findViewById(R.id.app_price);

            this.onAppListener = onAppListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAppListener.onAppClick(getAdapterPosition(), title.getText().toString());
        }
    }

    public interface OnAppListener{
        void onAppClick(int position, String title);
    }
}
