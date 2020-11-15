package se.kth.fmheim.workoutweather.model;

import android.util.Log;

import se.kth.fmheim.workoutweather.R;

public class Weather {
    private static final String LOG_TAG = Weather.class.getSimpleName();
    private String mTime;
    private String mDate;
    private double mTemperature;
    private String mClouds;
    private String mApprovedTime;
    private String mReferenceTime;
    private int mSymbol;
    private String mWorkoutRecommendation;


    public Weather() {
        mTime = "no time";
        mDate = "no date";
        mTemperature = 999;
        mClouds = "no data";
        mSymbol = R.drawable.day_1; // the sun is always shining :)
        Log.d(LOG_TAG, "init");

    }

    /*
    set weather data and times
     */
    public void setReferenceTime(String referenceTime) {
        mReferenceTime = referenceTime;
    }

    public void setApprovedTime(String approvedTime) {
        mApprovedTime = approvedTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public void setDate(String date) {
        Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~Set Date~~~~~~~~~~~~~~~~~~~~~~~ " + date);
        mDate = date;
    }

    public void setTemperature(double temperature) {
        //Log.d(LOG_TAG, "~~~~~~~~~~~~~~~~~Set Temperature~~~~~~~~~~~~~~~~~~~~~~~ " + temperature);
        mTemperature = temperature;
    }

    public void setClouds(String clouds) {
        mClouds = clouds;
    }

    public void setSymbol(int symbol) {
        mSymbol = symbol;
    }

    public void setWorkoutRecommendation(String mWorkoutRecommendation) {
        this.mWorkoutRecommendation = mWorkoutRecommendation;
    }

    /*
    get weather data
     */
    public String getTime() {
        return mTime;
    }


    public String getDate() {
        return mDate;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getClouds() {
        return mClouds;
    }

    public int getSymbol() {
        return mSymbol;
    }

    public String getWorkoutRecommendation() {
        return mWorkoutRecommendation;
    }

    public String getApprovedTime() {
        return mApprovedTime;
    }

    public void logWeather() {
        Log.d(LOG_TAG,
                "\nTemperature: " + Double.toString(mTemperature)
                        + "\nClouds: " + mClouds
                        + "\nTime: " + mTime
                        + "\nDate: " + mDate);
    }








/*
get day
...
 */

}
