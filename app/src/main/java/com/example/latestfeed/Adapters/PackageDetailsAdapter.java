package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.R;

import java.util.ArrayList;

public class PackageDetailsAdapter extends RecyclerView.Adapter<PackageDetailsAdapter.PackageViewHolder> {
    private ArrayList<ArrayList<String>> detailsList;

    public PackageDetailsAdapter(ArrayList<ArrayList<String>> insertList) {
        this.detailsList = insertList;
    }

    public void insertDate(ArrayList<ArrayList<String>> insertList) {
        //This function will package_add_white new data to RecyclerView
        MyDiffUtilCallback<ArrayList<String>> diffUtilCallback = new MyDiffUtilCallback<>(detailsList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        detailsList.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateData(ArrayList<ArrayList<String>> insertList) {
        //This function will clear old data from RecyclerView
        MyDiffUtilCallback<ArrayList<String>> diffUtilCallback = new MyDiffUtilCallback<>(detailsList, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        detailsList.clear();
        detailsList = insertList;
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        ArrayList<String> details = detailsList.get(position);
        int size = details.size();
        if (0 < size && details.get(0) != null) {
            holder.date.setText(details.get(0));
            if (1 < size && details.get(1) != null) {
                holder.description.setText(details.get(1));
                if (2 < size && details.get(2) != null) {
                    holder.unit.setText(details.get(2));
                    if (3 < size && details.get(3) != null) {
                        holder.city.setText(details.get(3));
                    }
                }
            }
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.package_details_design;
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {

        TextView date, description, unit, city;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.package_detail_date);
            date.setText("");
            unit = itemView.findViewById(R.id.package_detail_unit);
            unit.setText("");
            description = itemView.findViewById(R.id.package_detail_description);
            description.setText("");
            city = itemView.findViewById(R.id.package_detail_city);
            city.setText("");
        }
    }

}
