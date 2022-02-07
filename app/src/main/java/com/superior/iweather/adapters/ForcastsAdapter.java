package com.superior.iweather.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superior.iweather.R;
import com.superior.iweather.models.Forcast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ForcastsAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Forcast> data;
    private Activity _activity;

    public ForcastsAdapter(ArrayList<Forcast> data, Activity _activity) {
        this.data = data;
        this._activity = _activity;
        this.inflater = (LayoutInflater)   _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Forcast getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_forcast, null);

        TextView tvTempValue = view.findViewById(R.id.tv_temp_value);
        TextView tvDate = view.findViewById(R.id.tv_temp_date);
        TextView tvDay= view.findViewById(R.id.tv_temp_day);
        TextView tvDayDescription = view.findViewById(R.id.tv_temp_day_desc);
        TextView tvNightDescription = view.findViewById(R.id.tv_temp_night_desc);

        ImageView ivDayIcon = view.findViewById(R.id.iv_temp_day_icon);
        ImageView ivNightIcon = view.findViewById(R.id.iv_temp_night_icon);

        Forcast forcast = getItem(i);

        tvTempValue.setText( fahrenheitToCelsius(forcast.getMinTemp()) + "°C - " + fahrenheitToCelsius(forcast.getMaxTemp()) + "°C" );

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = inputFormat.parse(forcast.getDateTemp());
            String formattedDate = outputFormat.format(date);
            tvDate.setText(formattedDate);

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdf.format(date);
            tvDay.setText(dayOfTheWeek);

        }catch (Exception ex)
        {
            Log.e("Exception" , ex.getMessage());
        }

        tvDayDescription.setText(forcast.getDayTempDescription());
        tvNightDescription.setText(forcast.getNightTempDescription());

        Glide.with( _activity ).load(getCompleteUrl(forcast.getDayIconCode())).into(ivDayIcon);

        Glide.with( _activity ).load(getCompleteUrl(forcast.getNightIconCode())).into(ivNightIcon);

        return view;
    }

    private double fahrenheitToCelsius(int temperature)
    {
        return ((temperature - 32)*5)/9;
    }

    private String getCompleteUrl(int iconID)
    {
        String iconUrl = "https://developer.accuweather.com/sites/default/files/";

        if(iconID < 10)
        {
            iconUrl = iconUrl + "0" + iconID + "-s.png";
        }
        else
        {
            iconUrl = iconUrl + iconID + "-s.png";
        }

        return iconUrl;
    }
}
