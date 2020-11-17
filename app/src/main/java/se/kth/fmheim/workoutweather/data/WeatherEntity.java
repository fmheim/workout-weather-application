package se.kth.fmheim.workoutweather.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_table")
public class WeatherEntity {
    /*
    One entity that is saved in room database
     */

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mTime;
    private String mDate;
    private double mTemperature;
    private String mClouds;
    private String mApprovedTime;
    private String mReferenceTime;
    private int mSymbol;
    private String mWorkoutRecommendation;

    public WeatherEntity(String mTime,
                         String mDate,
                         double mTemperature,
                         String mClouds,
                         String mApprovedTime,
                         String mReferenceTime,
                         int mSymbol,
                         String mWorkoutRecommendation) {
        this.mTime = mTime;
        this.mDate = mDate;
        this.mTemperature = mTemperature;
        this.mClouds = mClouds;
        this.mApprovedTime = mApprovedTime;
        this.mReferenceTime = mReferenceTime;
        this.mSymbol = mSymbol;
        this.mWorkoutRecommendation = mWorkoutRecommendation;
    }

    public int getId() {
        return id;
    }

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

    public String getApprovedTime() {
        return mApprovedTime;
    }

    public String getReferenceTime() {
        return mReferenceTime;
    }

    public int getSymbol() {
        return mSymbol;
    }

    public String getWorkoutRecommendation() {
        return mWorkoutRecommendation;
    }

    public void setId(int id) {
        this.id = id;
    }
}


