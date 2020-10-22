package com.example.latestfeed.IsraelPost.JVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.latestfeed.IsraelPost.Entities.Parcel;

import java.util.List;

public class ParcelViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> parcels;


    public ParcelViewModel(@NonNull Application application) {
        super(application);
        repository = new ParcelRepository(application);
        parcels = repository.getAllPackages();
    }

    public void insert(Parcel parcel) {
        repository.insert(parcel);
    }

    public void delete(Parcel parcel) {
        repository.delete(parcel);
    }

    public void update(Parcel parcel) {
        repository.update(parcel);
    }

    public LiveData<List<Parcel>> getAllPackages() {
        return parcels;
    }

    public List<Parcel> getAllToRefresh() {
        return repository.getAllToRefresh();
    }
}
