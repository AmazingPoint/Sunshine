package datastack.sunshine;

import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by andy on 2016/7/4.
 */
public class WeatherDataParser {

    static final String LOG_TAG = "WeatherDataParser";

    public String getReadableDateString(long time){
        SimpleDateFormat shortenedDataFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDataFormat.format(time);
    }

    private String formatHighLows(double high, double low){
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);
        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
        throws JSONException{

        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATUE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DESCRIPTION = "description";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        Time dayTime = new Time();
        dayTime.setToNow();
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        dayTime = new Time();

        String[] resultStrs = new String[numDays];
        for(int i=0; i<weatherArray.length(); i++){
            String day;
            String description;
            String highAndLow;

            JSONObject dayForecast = weatherArray.getJSONObject(i);
            long dateTime;
            dateTime = dayTime.setJulianDay(julianStartDay+i);
            day = getReadableDateString(dateTime);

            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATUE);
            Double high = temperatureObject.getDouble(OWM_MAX);
            Double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s: resultStrs){
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }

        return resultStrs;
    }

}
