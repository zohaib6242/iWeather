package com.superior.iweather.models;

public class Forcast {

    private int minTemp;
    private int maxTemp;
    private String dateTemp;
    private String dayTempDescription;
    private String nightTempDescription;
    private int dayIconCode;
    private int nightIconCode;

    public Forcast(int minTemp, int maxTemp, String dateTemp, String dayTempDescription, String nightTempDescription, int dayIconCode, int nightIconCode) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.dateTemp = dateTemp;
        this.dayTempDescription = dayTempDescription;
        this.nightTempDescription = nightTempDescription;
        this.dayIconCode = dayIconCode;
        this.nightIconCode = nightIconCode;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public String getDateTemp() {
        return dateTemp;
    }

    public String getDayTempDescription() {
        return dayTempDescription;
    }

    public String getNightTempDescription() {
        return nightTempDescription;
    }

    public int getDayIconCode() {
        return dayIconCode;
    }

    public int getNightIconCode() {
        return nightIconCode;
    }
}
