package se.kth.fmheim.workoutweather.view;

public class WeatherItem {
    private int mWeatherSymbol;
    private String mTextDate;
    private String mTextTemperature;
    private String mTextWorkout;

    public WeatherItem(int weatherSymbol, String textDate, String textTemperature, String textWorkout) {
        mWeatherSymbol = weatherSymbol;
        mTextDate = textDate;
        mTextTemperature = textTemperature;
        mTextWorkout = textWorkout;


    }


    public int getWeatherSymbol() {
        return mWeatherSymbol;
    }

    public void setWeatherSymbol(int mWeatherSymbol) {
        this.mWeatherSymbol = mWeatherSymbol;
    }

    public String getTextDate() {
        return mTextDate;
    }

    public void setTextDate(String mTextDate) {
        this.mTextDate = mTextDate;
    }

    public String getTextTemperature() {
        return mTextTemperature;
    }

    public void setTextTemperature(String mTextTemperature) {
        this.mTextTemperature = mTextTemperature;
    }

    public String getTextWorkout() {
        return mTextWorkout;
    }

    public void setTextWorkout(String mTextWorkout) {
        this.mTextWorkout = mTextWorkout;
    }


}
