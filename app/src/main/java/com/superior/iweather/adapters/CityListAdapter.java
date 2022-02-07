package com.superior.iweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superior.iweather.models.City;
import com.superior.iweather.R;

import java.util.ArrayList;

public class CityListAdapter extends BaseAdapter {

    private Activity _activity;
    private LayoutInflater layoutInflater;
    private ArrayList<City> cities;

    public CityListAdapter(Activity _activity, ArrayList<City> cities) {
        this._activity = _activity;
        this.cities = cities;
        this.layoutInflater = (LayoutInflater)   _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.row_city_list, null);

        City city = getItem(i);
        TextView cityDetail = view.findViewById(R.id.tv_city_detail);
        cityDetail.setText(city.getName() + ", " + city.getState() + ", "+ city.getCountry());

        return view;
    }
}
