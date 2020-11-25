package se.kth.fmheim.workoutweather.view;
    /*
    View Model that survives entire life cycle
    Using AndroidViewModel to pass Application Context!
    */
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.kth.fmheim.workoutweather.data.WeatherRepository;
import se.kth.fmheim.workoutweather.data.Weather;

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG_TAG = WeatherViewModel.class.getSimpleName();
    private final LiveData<List<Weather>> mWeatherLiveData;

    public WeatherViewModel(Application application) {
        super(application);
        Log.d(LOG_TAG, "ViewModel Constructor");
        WeatherRepository repository = new WeatherRepository(application);
        mWeatherLiveData = repository.getLiveWeatherData();
    }

    public LiveData<List<Weather>> getWeatherLiveData() {
        return mWeatherLiveData;
    }
}

