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
        mWeatherLiveData = mRepository.getWeatherData();


    }

    public LiveData<List<Weather>> getWeatherLiveData() {
        return mWeatherLiveData;
    }


    public void insert(List<Weather> weatherData) {
        mRepository.insert(weatherData);
    }

    public void update(List<Weather> weatherData) {
        mRepository.update(weatherData);
    }

    public void delete(Weather weather) {
        mRepository.delete(weather);
    }

    public void deleteWeatherData() {
        mRepository.deleteAllWeatherData();
    }



/*
    public void loadWeatherDataAsync(String longitude, String latitude) {

        //asynchronous call to download and parse data in the background
        //receives weatherData and posts it to Live Data

        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Pre async download----");
                    new DownloadController(mApplication.getApplicationContext(), longitude,latitude).postRequest(new DownloadController.VolleyCallback(){
                        @Override
                        public void onSuccess(List<Weather> weatherData){
                            weatherLiveData.postValue(weatherData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
*/
}

