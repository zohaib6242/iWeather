package com.superior.iweather.net;

import com.superior.iweather.helper.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {

    private static WebService _instance;

    public static WebService getInstance()
    {
        if(_instance == null)
        {
            _instance = new WebService();
        }
        return _instance;
    }

    public void getCitiesList(String keywords , DataFetchListener listener)
    {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete?apikey=NxHYdTsu2Cv8xiyVrc862lZ9aDfd7XdX&q=laho
        String url = Constants.BaseURL + "locations/v1/cities/autocomplete?apikey=" + Constants.API_ACCESS_KEY + "&q=" + keywords;
        this.doGetRequest(url , listener);
    }

    public void getWeatherForcast(String locationKey , DataFetchListener listener)
    {
        //http://dataservice.accuweather.com/forecasts/v1/daily/5day/260622?apikey=NxHYdTsu2Cv8xiyVrc862lZ9aDfd7XdX
        String url = Constants.BaseURL + "forecasts/v1/daily/5day/" + locationKey + "?apikey=" + Constants.API_ACCESS_KEY;
        this.doGetRequest(url , listener);
    }

    private void doGetRequest(String url , final DataFetchListener listener)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                listener.onResponse(response.body().string());
            }
        });
    }
}
