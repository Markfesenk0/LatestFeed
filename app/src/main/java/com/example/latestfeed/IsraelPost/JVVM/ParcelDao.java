package com.example.latestfeed.IsraelPost.JVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.latestfeed.IsraelPost.Entities.Parcel;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ParcelDao {

    @Insert
    void insert(Parcel parcel);

    @Delete
    void delete(Parcel parcel);

    @Update
    void update(Parcel parcel);

    @Query("SELECT * FROM package_table ORDER BY status DESC")
    LiveData<List<Parcel>> getAllPackages();

    @Query("SELECT * FROM package_table WHERE status < 2")
    List<Parcel> getAllToRefresh();
}
