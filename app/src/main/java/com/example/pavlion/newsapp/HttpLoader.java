package com.example.pavlion.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class HttpLoader extends AsyncTaskLoader<List<NewsItem>> {

     private String mUrl;
    //"http://content.guardianapis.com/search?show-tags=contributor&api-key=84ad1010-7c77-45d4-b72b-b524ab17b100";

    public HttpLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<NewsItem> newsList = QueryManager.fetchNewsData(mUrl);
        return newsList;
    }
}