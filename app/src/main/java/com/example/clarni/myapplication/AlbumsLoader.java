package com.example.clarni.myapplication;

import android.content.Context;

import java.util.ArrayList;
import android.content.AsyncTaskLoader;





public class AlbumsLoader extends AsyncTaskLoader<ArrayList<AlbumsModel>> {

    private String mUrl;
    private int requestCode;

    public AlbumsLoader(Context context, String mUrl, int requestCode) {
        super(context);
        this.mUrl = mUrl;
        this.requestCode = requestCode;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<AlbumsModel> loadInBackground() {
        if(mUrl == null)
            return null;

        return Utils.fetchAlbumData(mUrl,requestCode);
    }
}
