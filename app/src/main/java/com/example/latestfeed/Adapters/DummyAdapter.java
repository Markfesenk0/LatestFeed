package com.example.latestfeed.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latestfeed.R;
import com.example.latestfeed.Entities.TypeEnum;

import java.util.ArrayList;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.DummyViewHolder> {

    private TypeEnum type;
    private ArrayList<String> dummyList;

    public DummyAdapter(TypeEnum type) {
        this.type = type;
        this.dummyList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            dummyList.add("a");
        }
    }

    @NonNull
    @Override
    public DummyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (this.type == TypeEnum.NEWS) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_news_design, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_design, parent, false);
        }
        return new DummyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DummyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return dummyList.size();
    }

    public static class DummyViewHolder extends RecyclerView.ViewHolder {

        public DummyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
