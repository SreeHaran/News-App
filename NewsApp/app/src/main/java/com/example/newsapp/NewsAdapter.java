package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.news_list_item, parent,
                            false);
        }

        News currentNews = getItem(position);

        TextView section = convertView.findViewById(R.id.section_name);
        section.setText(currentNews.getmSectionName());

        TextView headlines = convertView.findViewById(R.id.headlines_names);
        headlines.setText(currentNews.getmHeadlines());

        TextView date = convertView.findViewById(R.id.date);
        date.setText(currentNews.getmDate());

        TextView time = convertView.findViewById(R.id.time);
        time.setText(currentNews.getmTime());

        TextView author = convertView.findViewById(R.id.author_name);
        author.setText(currentNews.getmAuthor());

        return convertView;
    }
}
