package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.IsraelPost.Entities.Parcel;
import com.example.latestfeed.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.ParcelViewHolder> {
    private ArrayList<Parcel> parcelList;
    private OnParcelListener mOnParcelListener;
    private boolean isDark;

    public ParcelAdapter(ArrayList<Parcel> insertList, OnParcelListener onParcelListener, boolean isDark) {
        this.parcelList = insertList;
        this.mOnParcelListener = onParcelListener;
        this.isDark = isDark;
    }

    public void insertData(ArrayList<Parcel> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<Parcel> diffUtilCallback = new MyDiffUtilCallback<>(parcelList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        parcelList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<Parcel> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<Parcel> diffUtilCallback = new MyDiffUtilCallback<>(parcelList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        parcelList.clear();
        parcelList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ParcelViewHolder(view, mOnParcelListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelViewHolder holder, int position) {
        if(position == parcelList.size()) {
            if(isDark) {
                Picasso.get().load(R.drawable.package_add_white).into(holder.add);
            } else {
                Picasso.get().load(R.drawable.package_add_black).into(holder.add);
            }
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss", Locale.US);
            Parcel parcel = parcelList.get(position);
            if (parcel.getStatus() == 1) {
                Picasso.get().load(R.drawable.package_on_way).into(holder.image);
            } else if (parcel.getStatus() == 2) {
                Picasso.get().load(R.drawable.package_complete).into(holder.image);
            } else {
                Picasso.get().load(R.drawable.package_error).into(holder.image);
            }
            holder.title.setText(parcel.getTitle());
            holder.number.setText(parcel.getTrackingNumber());
            holder.description.setText(parcel.getLastStatus());
            holder.updateTime.setText(sdf.format(parcel.getUpdateTime()));
            holder.cardView.setCardBackgroundColor(parcel.getColor());
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return (position == parcelList.size()) ? R.layout.add_package_design : R.layout.package_design;
    }

    @Override
    public int getItemCount() {
        return parcelList.size() + 1;
    }

    public static class ParcelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        ImageView image;
        ImageView add;
        TextView title, number, description, updateTime;
        CardView cardView;
        OnParcelListener onParcelListener;

        public ParcelViewHolder(@NonNull View itemView, OnParcelListener onParcelListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.package_cardview);
            add = itemView.findViewById(R.id.add_package_image);
            image = itemView.findViewById(R.id.package_image);
            title = itemView.findViewById(R.id.package_title);
            number = itemView.findViewById(R.id.package_number);
            description = itemView.findViewById(R.id.package_description);
            updateTime = itemView.findViewById(R.id.package_update_time);
            this.onParcelListener = onParcelListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onParcelListener.onParcelClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onParcelListener.onParcelLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnParcelListener {
        void onParcelClick(int position);
        void onParcelLongClick(int position);
    }
}
