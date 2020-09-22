package com.example.latestfeed;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.latestfeed.Entities.News;

import java.util.ArrayList;

public class NewsViewModel extends AndroidViewModel {
    private LiveData<ArrayList<News>> newsList;

    public NewsViewModel(@NonNull Application application) {
        super(application);
    }
}
