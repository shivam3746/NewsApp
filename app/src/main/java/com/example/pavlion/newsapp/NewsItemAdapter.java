package com.example.pavlion.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    public NewsItemAdapter(Context context, ArrayList<NewsItem> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        NewsItem currentNews = getItem(position);
        TextView newsTitle = listItemView.findViewById(R.id.title_text_view);
        String title = currentNews.getmTitle();
        newsTitle.setText(title);


        TextView newsCategory = listItemView.findViewById(R.id.category_text_view);
        String category = currentNews.getmCategory();
        newsCategory.setText(category);

        TextView newsDate = listItemView.findViewById(R.id.date_text_view);
        String date = currentNews.getmDate();
        newsDate.setText(date);

        TextView newsAuthor =listItemView.findViewById(R.id.author_text_view);
        String author = currentNews.getmAuthor();
        newsAuthor.setText(author);

        if(author.equals("")){
            author = getContext().getString(R.string.noAuthor);
        }

        newsAuthor.setText(author);

        return listItemView;
    }
}