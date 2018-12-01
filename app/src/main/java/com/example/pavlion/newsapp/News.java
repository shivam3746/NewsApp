package com.example.pavlion.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class News extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {
    private NewsItemAdapter mAdapter;
    private TextView mNoContentTextView;
    private static String REQUEST_URL = "http://content.guardianapis.com/search?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ListView newsListView = findViewById(R.id.list_item);
        mNoContentTextView = findViewById(R.id.noContent_text_view);
        newsListView.setEmptyView(mNoContentTextView);
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());

        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsItem currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                Intent i2 = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(i2);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mNoContentTextView.setText(R.string.internetErrorMsg);
        }
    }


    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), getString(R.string.settings_min_news_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String section = sharedPreferences.getString(getString(R.string.settings_section_news_key), getString(R.string.settings_section_news_default));

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "84ad1010-7c77-45d4-b72b-b524ab17b100");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", minNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        if (!section.equals(getString(R.string.settings_section_news_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }

        return new HttpLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> news) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mNoContentTextView.setText(R.string.contentErrorMsg);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent in = new Intent(this, Settings.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



}