package datastack.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* For my teacher from UdaCity:
* Hello, Thanks for your time!
* I come from Chinese, So some Services Witch are from google That I can not enjoin.
* So, I done some a little chance from our app.
* */

public class MainActivity extends AppCompatActivity {

    public static ArrayAdapter<String> mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{weatherDataBunding();}
        catch(JSONException e){e.printStackTrace();}
        updateListView();
        updateActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            updateWeather();
        }
        if(id == R.id.action_settings){
            intent2Setting();
        }
        if(id == R.id.action_location){
            share2Other();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *Setting ListView
     */
    private void updateListView()
    {
        ListView listView = (ListView) findViewById(
                R.id.listview_forecast);
        if(listView != null) {
            listView.setAdapter(this.mForecastAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String forecast = mForecastAdapter.getItem(position);
                    Intent intent = new Intent(getApplication(), DetailActivity.class).
                            putExtra(Intent.EXTRA_TEXT, forecast);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     *Setting ActionBar
     */
    private void updateActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.ic_launcher);
        }
    }


    /**
     * go settings activity!
     */
    private void intent2Setting(){
        Intent intent = new Intent(getApplication(), SettingsActivity.class)
                .putExtra(Intent.EXTRA_REFERRER_NAME, getApplicationInfo());
        startActivity(intent);
    }

    /**
     *Share to Other(sms wechat facebook ...)
     */
    private void share2Other(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String location = sharedPrefs.getString(
                getString(R.string.pref_key_location),
                getString(R.string.pref_default_location));

        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q",location).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setData(geoLocation);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }


    private void weatherDataBunding() throws JSONException {
        String[] forecastArray = {};
        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray));
        this.mForecastAdapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast
        );
    }


    private void updateWeather(){
        APIWebService aws = new APIWebService();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String location = prefs.getString(getString(R.string.pref_key_location),getString(R.string.pref_default_location));
        String tempUnits = prefs.getString(getString(R.string.pref_key_units), getString(R.string.pref_default_units));
        String lang = prefs.getString(getString(R.string.pref_key_language), getString(R.string.pref_default_language));
        aws.execute(location, tempUnits, lang);
    }

}
