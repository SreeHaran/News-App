package com.example.newsapp;

public class News {
    private String mHeadlines;
    private String mSectionName;
    private String mDate;
    private String mTime;
    private String mAuthor;
    private String mWebURL;

    public News(String sectionName, String headlines, String date, String time, String author, String webURL) {
        mHeadlines = headlines;
        mSectionName = sectionName;
        mDate = date;
        mTime = time;
        mAuthor = author;
        mWebURL = webURL;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmHeadlines() {
        return mHeadlines;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmWebURL() {
        return mWebURL;
    }
}
