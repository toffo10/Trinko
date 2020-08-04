package com.enricot44.publast.service.repository;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.enricot44.publast.utilities.JSONConverter;
import com.enricot44.publast.view.adapter.CocktailAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchCocktailTask extends AsyncTask<String, Void, String> {

    private CocktailAdapter cocktailAdapter;

    public SearchCocktailTask(CocktailAdapter cocktailAdapter) {
        this.cocktailAdapter = cocktailAdapter;
    }

    @Override
    protected String doInBackground(String... strings) {
        return retrieveCocktails(strings[0]);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        cocktailAdapter.getItems().clear();
        cocktailAdapter.getItems().addAll(JSONConverter.retrieveCocktailsByJSON(response));
        cocktailAdapter.setThumbnailImage();
        cocktailAdapter.notifyDataSetChanged();
    }

    private static String retrieveCocktails(String cocktailName) {
        BufferedReader reader = null;
        InputStream inputStream = null;
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(buildCocktailUrl(cocktailName));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            // Start the query
            urlConnection.connect();
            int response = urlConnection.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            return builder.toString();
        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    private static String buildCocktailUrl(String cocktailName) {
        // Base URL for the Books API.
        final String BOOK_BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?";

        // Parameter for the search string
        final String QUERY_PARAM = "s";

        // Build up the query URI, limiting results to 5 printed books.
        Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, cocktailName)
                .build();

        return builtURI.toString();
    }

}
