package com.superior.iweather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.superior.iweather.models.City;
import com.superior.iweather.adapters.CityListAdapter;
import com.superior.iweather.helper.Constants;
import com.superior.iweather.net.DataFetchListener;
import com.superior.iweather.R;
import com.superior.iweather.net.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ProgressDialog loading = null;
    private EditText etKeyword;
    private Button btnSearch;
    private ListView listView;
    private CityListAdapter cityListAdapter;
    private ArrayList<City> cityArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String locationID = (shared.getString(Constants.PREF_KEY_LOCATION_ID, ""));
        if( ! locationID.equals(""))
        {
            startActivity(new Intent(getBaseContext() , MainActivity.class));
            finish();
        }

        listView = findViewById(R.id.city_list_view);
        etKeyword = findViewById(R.id.et_keyword);
        btnSearch = findViewById(R.id.btn_search);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SharedPreferences shared = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString( Constants.PREF_KEY_LOCATION_ID , cityArrayList.get(i).getId());
                editor.commit();

                City city = cityArrayList.get(i);
                editor.putString( Constants.PREF_KEY_LOCATION_NAME , city.getName() + ", " + city.getState() + ", "+ city.getCountry());
                editor.commit();

                startActivity(new Intent(getBaseContext() , MainActivity.class));
                finish();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keywords = etKeyword.getText().toString();
                if(keywords.length() > 0)
                {
                    fetchCities(keywords);
                }
                else
                {
                    Toast.makeText(getBaseContext() , "ENTER KEYWORDS TO SEARCH" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchCities(String keywords) {

        cityArrayList = null;
        cityArrayList = new ArrayList<>();

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage("Fetching list...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();

        WebService.getInstance().getCitiesList(keywords, new DataFetchListener() {
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
                Log.e("onResponse" , response);

                try {
                    JSONArray citesArray = new JSONArray(response);
                    for(int i = 0 ; i < citesArray.length() ; i++)
                    {
                        JSONObject cityObject = citesArray.getJSONObject(i);

                        String city_name = cityObject.getString("LocalizedName");
                        String city_id = cityObject.getString("Key");
                        String city_state = cityObject.getJSONObject("AdministrativeArea").getString("LocalizedName");
                        String city_country = cityObject.getJSONObject("Country").getString("LocalizedName");;

                        cityArrayList.add(new City(city_id , city_name , city_state , city_country));
                    }

                    SearchActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cityListAdapter = new CityListAdapter(SearchActivity.this , cityArrayList);
                            listView.setAdapter(cityListAdapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext() , e.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
