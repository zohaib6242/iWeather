package com.superior.iweather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.superior.iweather.adapters.ForcastsAdapter;
import com.superior.iweather.R;
import com.superior.iweather.helper.Constants;
import com.superior.iweather.models.Forcast;
import com.superior.iweather.net.DataFetchListener;
import com.superior.iweather.net.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tvLocationName;
    private ProgressDialog loading = null;
    private ArrayList<Forcast> forcasts;
    private ListView forcastListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocationName = findViewById(R.id.tv_location_name);
        forcastListView = findViewById(R.id.forcast_list);

        SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String weatherData = (shared.getString(Constants.PREF_KEY_WEATHER_DATA, ""));

        String locationName = (shared.getString(Constants.PREF_KEY_LOCATION_NAME, ""));
        tvLocationName.setText(locationName);

        if( ! weatherData.equals(""))
        {
            parseWeatherData(weatherData);
        }
        else
        {
            fetchWeatherUpdate();
        }

        findViewById(R.id.iv_search_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.clear();
                editor.commit();

                startActivity(new Intent(getBaseContext() , SearchActivity.class));
                finish();
            }
        });

        findViewById(R.id.refresh_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchWeatherUpdate();
            }
        });
    }

    private void fetchWeatherUpdate()
    {
        SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String locationID = (shared.getString(Constants.PREF_KEY_LOCATION_ID, ""));

        String locationName = (shared.getString(Constants.PREF_KEY_LOCATION_NAME, ""));
        tvLocationName.setText(locationName);

        if( ! locationID.equals(""))
        {
            loading = new ProgressDialog(this);
            loading.setCancelable(false);
            loading.setMessage("Fetching weather...");
            loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loading.show();

            WebService.getInstance().getWeatherForcast(locationID, new DataFetchListener() {
                @Override
                public void onFailure(String error) {
                    loading.dismiss();
                    loading = null;
                    Toast.makeText(getBaseContext() , error , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    loading = null;
                    parseWeatherData(response);
                }
            });
        }
    }

    private void parseWeatherData(String weatherJson)
    {
        forcasts = new ArrayList<>();
        try {
            JSONObject weatherObject = new JSONObject(weatherJson);
            JSONArray dailyForecasts = weatherObject.getJSONArray("DailyForecasts");

            for(int i = 0 ; i < dailyForecasts.length() ; i++)
            {
                int minTemp;
                int maxTemp;
                String dateTemp;
                String dayTempDescription;
                String nightTempDescription;
                int dayIconCode;
                int nightIconCode;

                minTemp = dailyForecasts.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Minimum").getInt("Value");
                maxTemp = dailyForecasts.getJSONObject(i).getJSONObject("Temperature").getJSONObject("Maximum").getInt("Value");
                dateTemp = dailyForecasts.getJSONObject(i).getString("Date");
                dayTempDescription = dailyForecasts.getJSONObject(i).getJSONObject("Day").getString("IconPhrase");
                nightTempDescription = dailyForecasts.getJSONObject(i).getJSONObject("Night").getString("IconPhrase");
                dayIconCode = dailyForecasts.getJSONObject(i).getJSONObject("Day").getInt("Icon");
                nightIconCode = dailyForecasts.getJSONObject(i).getJSONObject("Night").getInt("Icon");

                forcasts.add(new Forcast(minTemp,maxTemp,dateTemp,dayTempDescription,nightTempDescription,dayIconCode,nightIconCode));

                SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString( Constants.PREF_KEY_WEATHER_DATA , weatherJson);
                editor.commit();
            }

            if( forcasts.size() > 0 )
            {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        forcastListView.setAdapter(new ForcastsAdapter( forcasts , MainActivity.this));
                    }
                });
            }
        }catch (Exception ex)
        {
            Toast.makeText(getBaseContext() , ex.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}
