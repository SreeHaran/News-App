package com.example.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {


    static StringBuilder authorBuilder = new StringBuilder();

    private QueryUtils() {
    }

    private static URL createUrl(String StringUrl) {
        URL url = null;

        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            Log.e("Query Utils", "unable to create url", e);
        }
        return url;
    }

    public static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("QueryUtils", "problem with requesting JSON", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<News> extractNews(String newsJson) {

        ArrayList<News> newsArrayList = new ArrayList<>();
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }
        try {
            JSONObject root = new JSONObject(newsJson);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject resultIndex = results.getJSONObject(i);
                String sectionName = resultIndex.optString("sectionName");
                String webTitle = resultIndex.optString("webTitle");
                String webUrl = resultIndex.optString("webUrl");
                String publicationDate = resultIndex.optString("webPublicationDate");

                String[] date_time = publicationDate.split("T", 2);
                String date = date_time[0];
                String time = date_time[1];

                if (resultIndex.optJSONArray("tags") != null) {
                    JSONArray tags = resultIndex.optJSONArray("tags");

                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject tagsIndex = tags.optJSONObject(j);
                        String authorName = tagsIndex.optString("firstName");
                        if (authorName != null) {
                            if (authorBuilder.length() == 0) {
                                authorBuilder.append(authorName);
                            } else {
                                authorBuilder.append(", ");
                                authorBuilder.append(authorName);
                            }
                        }
                    }
                }
                authorBuilder.append("");
                String author = authorBuilder.toString();

                authorBuilder.delete(0, authorBuilder.length());

                News news = new News(sectionName, webTitle, date, time, author, webUrl);
                newsArrayList.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        // Return the list of news
        return newsArrayList;
    }

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e("Query Utils", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> news = extractNews(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }

}
