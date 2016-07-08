package datastack.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andy on 2016/7/4.
 */
public class APIWebService extends AsyncTask<String, Integer, String[]> {

    @Override
    protected String[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendPath("daily")
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", params[1])
                    .appendQueryParameter("cnt", "7")
                    .appendQueryParameter("appid", "78170570150dea5a3cec32c9eb56473a")
                    .appendQueryParameter("lang", params[2])
                    .build();
            URL url = new URL(builder.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                forecastJsonStr = null;
            }
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            if (reader == null) {
                forecastJsonStr = null;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainActivity", "Error closing stream", e);
                }
            }
        }

        WeatherDataParser wdp = new WeatherDataParser();
        try {
            return wdp.getWeatherDataFromJson(forecastJsonStr, 7);
        } catch (JSONException e) {
            Log.e("JsonParser", "JSON PARSER ERROR", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if(result != null){
            MainActivity.mForecastAdapter.clear();
            for(String dayForecastStr : result){
                MainActivity.mForecastAdapter.add(dayForecastStr);
            }
        }
    }
}

