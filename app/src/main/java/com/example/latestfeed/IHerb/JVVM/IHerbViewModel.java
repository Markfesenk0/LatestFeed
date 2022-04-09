package com.example.latestfeed.IHerb.JVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.latestfeed.IHerb.IHerbItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class IHerbViewModel extends AndroidViewModel {

    private IHerbRepository repository;
    private LiveData<List<IHerbItem>> iHerbItems;

    public IHerbViewModel(@NonNull Application application) {
        super(application);
        repository = new IHerbRepository(application);
        iHerbItems = repository.getAllIHerbItems();
    }

    public void insert(IHerbItem iHerbItem) {
        repository.insert(iHerbItem);
    }

    public void delete(IHerbItem iHerbItem) {
        repository.delete(iHerbItem);
    }

    public void update(IHerbItem iHerbItem) {
        repository.update(iHerbItem);
    }

    public LiveData<List<IHerbItem>> getAllIHerbItems() {
        return iHerbItems;
    }

    public List<IHerbItem> getAllToRefresh() {
        return repository.getAllToRefresh();
    }
}
