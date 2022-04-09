package com.example.latestfeed.IHerb.JVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.latestfeed.IHerb.IHerbItem;

import java.util.List;

@Dao
public interface IHerbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IHerbItem iHerbItem);

    @Delete
    void delete(IHerbItem iHerbItem);

    @Update
    void update(IHerbItem iHerbItem);

    @Query("SELECT * FROM iherb_database ORDER BY dateAdded DESC")
    LiveData<List<IHerbItem>> getAllPackages();

    @Query("SELECT * FROM iherb_database")
    List<IHerbItem> getAllToRefresh();
}
