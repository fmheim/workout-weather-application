package se.kth.fmheim.workoutweather.data;
/*
Class to create weather objects
 - weather data for certain time
Annotations for database
 */

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import se.kth.fmheim.workoutweather.R;

@Entity(tableName = "weather_table")
public class Weather {
    private static final String LOG_TAG = Weather.class.getSimpleName();

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Time")
    private String mTime;
    @ColumnInfo(name = "Date")
    private String mDate;
    @ColumnInfo(name = "Temperature")
    private double mTemperature;
    @ColumnInfo(name = "Cloud coverage")
    private String mClouds;
    @ColumnInfo(name = "Approved Time")
    private String mApprovedTime;
    @ColumnInfo(name = "Reference Time")
    private String mReferenceTime;
    @ColumnInfo(name = "Weather Symbol")
    private int mSymbol;
    @ColumnInfo(name = "Workout Recommendation")
    private String mWorkoutRecommendation;
    @ColumnInfo(name = "Coordinates")
    private String mCoordinates;
    @ColumnInfo(name = "City")
    private String mCityName;


    public Weather() {
        mTime = "A sunny time";
        mDate = "A sunny year";
        mTemperature = 27;
        mClouds = "no data";
        mSymbol = R.drawable.day_1; // the sun is always shining :)
        mApprovedTime = "Enter Location to get real weather data!";
        mWorkoutRecommendation = "Here you'll find a recommendation for your next work out";
        mReferenceTime = "No reference time yet";
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
        Log.d(LOG_TAG, "New date set: " + date);
        mDate = date;
    }

    public void setTemperature(double temperature) {
        Log.d(LOG_TAG, "New temperature set: " + temperature);
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

    public void setCoordinates(String coordinates) {
        mCoordinates = coordinates;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }
    public void setId(int id) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public String getCoordinates() {
        return mCoordinates;
    }

    public String getCityName() {
        return mCityName;
    }
    public String getLongitude(){
        return mCoordinates.substring(0, 9);
    }
    public String getLatitude(){
        return mCoordinates.substring(11);
    }



    public String getReferenceTime() {
        return mReferenceTime;
    }

    public void logWeather() {
        Log.d(LOG_TAG,
                "\nTemperature: " + mTemperature
                        + "\nClouds: " + mClouds
                        + "\nTime: " + mTime
                        + "\nDate: " + mDate);
    }




}
