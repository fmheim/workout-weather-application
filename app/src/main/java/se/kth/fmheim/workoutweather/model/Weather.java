package se.kth.fmheim.workoutweather.model;

import android.util.Log;

public class Weather {
    private static final String LOG_TAG = Weather.class.getSimpleName();
    private String mTime;
    private String mDate;
    private double mTemperature;
    private String mClouds;
    private String mApprovedTime;
    private String mReferenceTime;

    //private String workoutRecommendation;
    //private String picture;

    public Weather() {
        mTime = "no time";
        mDate = "no date";
        mTemperature = 999;
        mClouds = "no data";
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


    public void logWeather() {
        Log.d(LOG_TAG,
                "\nTemperature: " + Double.toString(mTemperature)
                        + "\nClouds: " + mClouds
                        + "\nTime: " + mTime + " o'clock "
                        + "\nDate: " + mDate);
    }


/*
get day
...
 */

}
