package se.kth.fmheim.workoutweather.view;
    /*
   Items displayed in RecyclerView
    */

public class WeatherItem {
    private final int mWeatherSymbol;
    private final String mTextDate;
    private final String mTextTemperature;
    private final String mTextWorkout;

    public WeatherItem(int weatherSymbol, String textDate, String textTemperature, String textWorkout) {
        mWeatherSymbol = weatherSymbol;
        mTextDate = textDate;
        mTextTemperature = textTemperature;
        mTextWorkout = textWorkout;
    }

    public int getWeatherSymbol() {
        return mWeatherSymbol;
    }

    public String getTextDate() {
        return mTextDate;
    }

    public String getTextTemperature() {
        return mTextTemperature;
    }

    public String getTextWorkout() {
        return mTextWorkout;
    }
}
