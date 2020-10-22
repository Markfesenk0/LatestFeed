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
import com.example.latestfeed.Entities.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    ArrayList<Song> songs;
    OnSongListener mOnSongListener;

    public SongAdapter(ArrayList<Song> songs, OnSongListener onSongListener) {
        this.songs = songs;
        this.mOnSongListener = onSongListener;
    }

    public void insertDate(ArrayList<Song> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<Song> diffUtilCallback = new MyDiffUtilCallback<>(songs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        songs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<Song> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<Song> diffUtilCallback = new MyDiffUtilCallback<>(songs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        songs.clear();
        songs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SongViewHolder(view, mOnSongListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        try {
            Picasso.get().load(song.getImgUrl()).into(holder.image);
        } catch (IllegalArgumentException e) {
            Picasso.get().load(R.drawable.placeholder).into(holder.image);
            System.out.println(e.getMessage());
        }
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.category.setText(song.getCategory());
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.song_design;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, artist, category;
        OnSongListener onSongListener;

        public SongViewHolder(@NonNull View itemView, OnSongListener onSongListener) {
            super(itemView);
            image = itemView.findViewById(R.id.song_image);
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);
            category = itemView.findViewById(R.id.song_category);
            this.onSongListener = onSongListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSongListener.onSongClick(getAdapterPosition());
        }
    }

    public interface OnSongListener {
        void onSongClick(int position);
    }
}
