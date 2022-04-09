package com.example.latestfeed.IHerb.JVVM;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.latestfeed.IHerb.IHerbItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

@Database(entities = {IHerbItem.class}, version = 2, exportSchema = false)
@TypeConverters({IHerbDB.Converters.class})
public abstract class IHerbDB extends RoomDatabase {

    private static IHerbDB instance;

    public abstract IHerbDao iHerbDao();

    public static synchronized IHerbDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    IHerbDB.class,
                    "iherb_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    public static class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }

        @TypeConverter
        public static ArrayList<ArrayList<String>> fromString(String value) {
            Type listType = new TypeToken<ArrayList<ArrayList<String>>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String fromArrayList(ArrayList<ArrayList<String>> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }
}
