package com.example.latestfeed.IHerb;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.latestfeed.Adapters.MyDiffUtilCallback;
import com.example.latestfeed.Entities.App;
import com.example.latestfeed.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class IHerbItemAdapter extends RecyclerView.Adapter<IHerbItemAdapter.TrackedItemViewHolder> {
    private ArrayList<IHerbItem> iHerbItemList;
    private OnIHerbItemListener mOnIHerbItemListener;
    private boolean isDark;

    public IHerbItemAdapter(ArrayList<IHerbItem> insertList, OnIHerbItemListener onIHerbItemListener, boolean isDark) {
        this.iHerbItemList = insertList;
        this.mOnIHerbItemListener = onIHerbItemListener;
        this.isDark = isDark;
    }

    public void insertData(ArrayList<IHerbItem> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<IHerbItem> diffUtilCallback = new MyDiffUtilCallback<>(iHerbItemList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        iHerbItemList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<IHerbItem> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<IHerbItem> diffUtilCallback = new MyDiffUtilCallback<>(iHerbItemList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        iHerbItemList.clear();
        iHerbItemList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public TrackedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new TrackedItemViewHolder(view, mOnIHerbItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackedItemViewHolder holder, int position) {
        if(position == iHerbItemList.size()) {
            if(isDark) {
                Picasso.get().load(R.drawable.package_add_white).into(holder.add);
            } else {
                Picasso.get().load(R.drawable.package_add_black).into(holder.add);
            }
        } else {
            IHerbItem iHerbItem = iHerbItemList.get(position);
            Picasso.get().load(iHerbItem.getImgUrl()).error(R.drawable.placeholder).into(holder.image);
            holder.title.setText(iHerbItem.getTitle());
            holder.brand.setText(iHerbItem.getBrand());
            holder.currentPrice.setText(iHerbItem.getCurrentPrice() + "₪");
            holder.standardPrice.setText(iHerbItem.getStandardPrice() + "₪");
            holder.minPrice.setText(iHerbItem.getMinPrice() + "₪");
            holder.maxPrice.setText(iHerbItem.getMaxPrice() + "₪");
            if(iHerbItem.getDiscount() >= 20) {
                holder.discount.setText("-" + String.valueOf(iHerbItem.getDiscount()) + "%");
                int color = (Integer) new ArgbEvaluator().evaluate((float)iHerbItem.getDiscount() / 100, Color.LTGRAY, Color.BLACK);
                holder.discount.setTextColor(color);
            }
            if(iHerbItem.isRefreshFail()) {
                holder.refreshFail.setImageResource(R.drawable.ic_baseline_circle_24);
            }
            if(iHerbItem.getStock()) {
                holder.availabilityImage.setImageResource(R.drawable.ic_baseline_done_24);
            } else {
                holder.availabilityImage.setImageResource(R.drawable.ic_baseline_close_24);
            }
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return (position == iHerbItemList.size()) ? R.layout.add_iherb_item_design : R.layout.iherb_item_design;
    }

    @Override
    public int getItemCount() {
        return iHerbItemList.size() + 1;
    }

    public static class TrackedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        ImageView add, refreshFail;
        ImageView image, availabilityImage;
        TextView title, brand, currentPrice, standardPrice, minPrice, maxPrice, discount;
        CardView cardView;
        OnIHerbItemListener onIHerbItemListener;

        public TrackedItemViewHolder(@NonNull View itemView, OnIHerbItemListener onIHerbItemListener) {
            super(itemView);
            add = itemView.findViewById(R.id.add_iherb_item_image);
            cardView = itemView.findViewById(R.id.package_cardview);
            image = itemView.findViewById(R.id.iherb_item_image);
            availabilityImage = itemView.findViewById(R.id.item_availability);
            refreshFail = itemView.findViewById(R.id.item_refresh);
            title = itemView.findViewById(R.id.item_title);
            brand = itemView.findViewById(R.id.item_brand);
            discount = itemView.findViewById(R.id.item_discount);
            currentPrice = itemView.findViewById(R.id.item_current_price);
            standardPrice = itemView.findViewById(R.id.item_standard_price);
            minPrice = itemView.findViewById(R.id.item_min_price);
            maxPrice = itemView.findViewById(R.id.item_max_price);
            this.onIHerbItemListener = onIHerbItemListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onIHerbItemListener.onIHerbItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            onIHerbItemListener.onIHerbItemLongClick(getAdapterPosition());
            return false;
        }
    }

    public interface OnIHerbItemListener {
        void onIHerbItemClick(int position);
        void onIHerbItemLongClick(int position);
    }
}
