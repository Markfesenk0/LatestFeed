package com.example.latestfeed;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.latestfeed.Entities.Song;

import java.util.ArrayList;

public class MyDiffUtilCallback<T> extends DiffUtil.Callback {

//    private T obj;
    private ArrayList<T> oldList;
    private ArrayList<T> newList;

    public MyDiffUtilCallback(ArrayList<T> oldList, ArrayList<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return (oldList.get(oldItemPosition).equals(newList.get(newItemPosition)));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

