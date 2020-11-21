package se.kth.fmheim.workoutweather.view;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.kth.fmheim.workoutweather.data.WeatherRepository;
import se.kth.fmheim.workoutweather.data.Weather;

public class WeatherViewModel extends AndroidViewModel {
    /*
    View Model that survives entire life cycle
    Using AndroidViewModel to pass Application Context!
    */
    private static final String LOG_TAG = WeatherViewModel.class.getSimpleName();
    private final WeatherRepository mRepository;

    private final LiveData<List<Weather>> mWeatherLiveData;


    public WeatherViewModel(Application application) {
        super(application);
        Log.d(LOG_TAG, "ViewModel Constructor");
        mRepository = new WeatherRepository(application);
        mWeatherLiveData = mRepository.getLiveWeatherData();


    }

    public LiveData<List<Weather>> getWeatherLiveData() {
        return mWeatherLiveData;
    }

}

