package com.example.latestfeed.IHerb.JVVM;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.latestfeed.IHerb.IHerbItem;

import java.util.List;

public class IHerbRepository {

    private IHerbDao IHerbDao;
    private LiveData<List<IHerbItem>> iHerbItems;

    public IHerbRepository(Application application) {
        IHerbDB database = IHerbDB.getInstance(application);
        IHerbDao = database.iHerbDao();
        iHerbItems = IHerbDao.getAllPackages();
    }

    public void insert(IHerbItem iHerbItem) {
        new InsertIHerbItemAsyncTask(IHerbDao).execute(iHerbItem);
    }

    public void delete(IHerbItem iHerbItem) {
        new DeleteIHerbItemAsyncTask(IHerbDao).execute(iHerbItem);
    }

    public void update(IHerbItem iHerbItem) {
        new UpdateIHerbItemAsyncTask(IHerbDao).execute(iHerbItem);
    }

    public LiveData<List<IHerbItem>> getAllIHerbItems() {
        return iHerbItems;
    }

    public List<IHerbItem> getAllToRefresh() {
        return IHerbDao.getAllToRefresh();
    }

    private static class InsertIHerbItemAsyncTask extends AsyncTask<IHerbItem, Void, Void> {

        private IHerbDao IHerbDao;

        private InsertIHerbItemAsyncTask(IHerbDao IHerbDao) {
            this.IHerbDao = IHerbDao;
        }

        @Override
        protected Void doInBackground(IHerbItem... iHerbItems) {
            IHerbDao.insert(iHerbItems[0]);
            return null;
        }
    }

    private static class DeleteIHerbItemAsyncTask extends AsyncTask<IHerbItem, Void, Void> {

        private IHerbDao IHerbDao;

        private DeleteIHerbItemAsyncTask(IHerbDao IHerbDao) {
            this.IHerbDao = IHerbDao;
        }

        @Override
        protected Void doInBackground(IHerbItem... iHerbItems) {
            IHerbDao.delete(iHerbItems[0]);
            return null;
        }
    }

    private static class UpdateIHerbItemAsyncTask extends AsyncTask<IHerbItem, Void, Void> {

        private IHerbDao IHerbDao;

        private UpdateIHerbItemAsyncTask(IHerbDao IHerbDao) {
            this.IHerbDao = IHerbDao;
        }

        @Override
        protected Void doInBackground(IHerbItem... iHerbItems) {
            IHerbDao.update(iHerbItems[0]);
            return null;
        }
    }
}
