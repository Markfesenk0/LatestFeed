package com.example.latestfeed.IsraelPost.JVVM;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.latestfeed.IsraelPost.Entities.Parcel;

import java.util.List;

public class ParcelRepository {

    private ParcelDao parcelDao;
    private LiveData<List<Parcel>> packages;

    public ParcelRepository(Application application) {
        ParcelDB database = ParcelDB.getInstance(application);
        parcelDao = database.parcelDao();
        packages = parcelDao.getAllPackages();
    }

    public void insert(Parcel parcel) {
        new InsertParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void delete(Parcel parcel) {
        new DeleteParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void update(Parcel parcel) {
        new UpdateParcelAsyncTask(parcelDao).execute(parcel);
    }

    public LiveData<List<Parcel>> getAllPackages() {
        return packages;
    }

    public List<Parcel> getAllToRefresh() {
        return parcelDao.getAllToRefresh();
    }

    private static class InsertParcelAsyncTask extends android.os.AsyncTask<Parcel, Void, Void> {

        private ParcelDao parcelDao;

        private InsertParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.insert(parcels[0]);
            return null;
        }
    }

    private static class DeleteParcelAsyncTask extends android.os.AsyncTask<Parcel, Void, Void> {

        private ParcelDao parcelDao;

        private DeleteParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.delete(parcels[0]);
            return null;
        }
    }

    private static class UpdateParcelAsyncTask extends android.os.AsyncTask<Parcel, Void, Void> {

        private ParcelDao parcelDao;

        private UpdateParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.update(parcels[0]);
            return null;
        }
    }
}
