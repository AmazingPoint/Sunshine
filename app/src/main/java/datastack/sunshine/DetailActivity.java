package datastack.sunshine;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailActivity extends AppCompatActivity  {

    private ShareActionProvider mShareActionProvider;
    private String mForecastStr;
    private static final String FORCAST_SHARE_TITLE = "#SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView textView = (TextView) findViewById(R.id.detail_textview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        Intent intent = getIntent();
        if(intent != null &&  intent.hasExtra(intent.EXTRA_TEXT)){
             mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(mForecastStr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr+FORCAST_SHARE_TITLE);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.action_settings){
            Intent intent = new Intent(getApplication(), SettingsActivity.class)
                    .putExtra(Intent.EXTRA_REFERRER_NAME, getApplicationInfo());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
