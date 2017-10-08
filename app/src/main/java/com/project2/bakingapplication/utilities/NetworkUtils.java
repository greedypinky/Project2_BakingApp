package com.project2.bakingapplication.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * NetworkUtils
 */
public class NetworkUtils {

    private final static String TAG = NetworkUtils.class.getSimpleName();
    // To fetch recipe data
    // https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    private final static String RECIPE_PATH = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private final static String RECIPE_JSON = "baking.json";
    public final static String RECIPE_URL = RECIPE_PATH + RECIPE_JSON;

    private final static String DELIMITER = "//A";

    /**
     * getRecipeURL
     *
     * @return URL URL for Recipe
     */
    public static URL getRecipeURL() {

        Uri uri = Uri.parse(RECIPE_URL);
        URL recipeURL = null;

        try {
            recipeURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        return recipeURL;
    }


    /**
     * gerRespsonseFromHttp
     *
     * @param url
     * @return response
     * @throws IOException
     */
    public static String getResponseFromHttp(URL url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream responseInputStream = conn.getInputStream();
        String response = null;

        try {

            Scanner scanner = new Scanner(responseInputStream);
            scanner.useDelimiter(DELIMITER);
            if (scanner.hasNext()) {
                response = scanner.next();
                return response;
            } else {
                return null;
            }

        } finally {
            conn.disconnect();
        }

    }

}
