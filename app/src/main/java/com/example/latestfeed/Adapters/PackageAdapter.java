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

import com.example.latestfeed.IsraelPost.Entities.MyPackage;
import com.example.latestfeed.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {
    private ArrayList<MyPackage> packageList;
    private OnPackageListener mOnPackageListener;
    private boolean isDark;

    public PackageAdapter(ArrayList<MyPackage> insertList, OnPackageListener onPackageListener, boolean isDark) {
        this.packageList = insertList;
        this.mOnPackageListener = onPackageListener;
        this.isDark = isDark;
    }

    public void insertDate(ArrayList<MyPackage> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<MyPackage> diffUtilCallback = new MyDiffUtilCallback<>(packageList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        packageList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<MyPackage> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<MyPackage> diffUtilCallback = new MyDiffUtilCallback<>(packageList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        packageList.clear();
        packageList = (ArrayList<MyPackage>) insertList.clone();
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PackageViewHolder(view, mOnPackageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        if(position == packageList.size()) {
            if(isDark) {
                Picasso.get().load(R.drawable.package_add_white).into(holder.add);
            } else {
                Picasso.get().load(R.drawable.package_add_black).into(holder.add);
            }
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss", Locale.US);
            MyPackage myPackage = packageList.get(position);
            if (myPackage.getStatus() == 1) {
                Picasso.get().load(R.drawable.package_on_way).into(holder.image);
            } else if (myPackage.getStatus() == 2) {
                Picasso.get().load(R.drawable.package_complete).into(holder.image);
            } else {
                Picasso.get().load(R.drawable.package_error).into(holder.image);
            }
            holder.title.setText(myPackage.getTitle());
            holder.number.setText(myPackage.getResult().getBarcode());
            holder.description.setText(myPackage.getLastStatus());
            holder.updateTime.setText(sdf.format(myPackage.getUpdateTime()));
            holder.cardView.setCardBackgroundColor(myPackage.getColor());
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return (position == packageList.size()) ? R.layout.add_package_design : R.layout.package_design;
    }

    @Override
    public int getItemCount() {
        return packageList.size() + 1;
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        ImageView add;
        TextView title, number, description, updateTime;
        CardView cardView;
        OnPackageListener onPackageListener;

        public PackageViewHolder(@NonNull View itemView, OnPackageListener onPackageListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.package_cardview);
            add = itemView.findViewById(R.id.add_package_image);
            image = itemView.findViewById(R.id.package_image);
            title = itemView.findViewById(R.id.package_title);
            number = itemView.findViewById(R.id.package_number);
            description = itemView.findViewById(R.id.package_description);
            updateTime = itemView.findViewById(R.id.package_update_time);
            this.onPackageListener = onPackageListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPackageListener.onPackageClick(getAdapterPosition());
        }
    }

    public interface OnPackageListener {
        void onPackageClick(int position);
    }
}
