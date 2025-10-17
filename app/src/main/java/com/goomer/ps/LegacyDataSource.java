package com.goomer.ps;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class LegacyDataSource {

    public interface Callback {
        void onSuccess(List<MenuItem> items);
        void onError(Exception e);
    }

    private final Context context;
    private final Gson gson = new Gson();

    public LegacyDataSource(Context context) {
        this.context = context.getApplicationContext();
    }

    public void loadMenu(Callback callback) {
        try {
            String json = readAsset("menu.json");
            Type listType = new TypeToken<List<MenuItem>>() {}.getType();
            List<MenuItem> items = gson.fromJson(json, listType);
            callback.onSuccess(items);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    private String readAsset(String name) throws IOException {
        AssetManager am = context.getAssets();
        InputStream is = am.open(name);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } finally {
            is.close();
        }
    }
}
