package com.goomer.ps.legacy;

import android.os.AsyncTask;

import com.goomer.ps.domain.model.MenuItem;

import java.util.List;

public class LegacyAsyncTask extends AsyncTask<Void, Void, List<MenuItem>> {

    public interface Listener {
        void onLoaded(List<MenuItem> items);
        void onFailed(Exception e);
    }

    private final LegacyDataSource dataSource;
    private final Listener listener;
    private Exception error;

    public LegacyAsyncTask(LegacyDataSource dataSource, Listener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    protected List<MenuItem> doInBackground(Void... voids) {
        final List<MenuItem>[] resultHolder = new List[1];
        try {
            Thread.sleep(500);
            dataSource.loadMenu(new LegacyDataSource.Callback() {
                @Override
                public void onSuccess(List<MenuItem> items) {
                    resultHolder[0] = items;
                }

                @Override
                public void onError(Exception e) {
                    error = e;
                }
            });
            return resultHolder[0];
        } catch (Exception e) {
            error = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<MenuItem> menuItems) {
        if (error != null) {
            listener.onFailed(error);
        } else {
            listener.onLoaded(menuItems);
        }
    }
}
