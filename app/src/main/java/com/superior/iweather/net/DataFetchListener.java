package com.superior.iweather.net;

public interface DataFetchListener {

    public void onFailure(String error);
    public void onResponse(String response);
}
