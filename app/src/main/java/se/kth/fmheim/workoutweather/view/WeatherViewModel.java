package se.kth.fmheim.workoutweather.view;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import se.kth.fmheim.workoutweather.model.Weather;
import se.kth.fmheim.workoutweather.networking.DownloadController;

public class WeatherViewModel  extends ViewModel {

    private static final String LOG_TAG = WeatherViewModel.class.getSimpleName() ;
    /*
        View Model that survives entire life cycle
         */
    private MutableLiveData<List<Weather>> weatherLiveData;

    public WeatherViewModel() {
        weatherLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Weather>>getWeatherData(){
        return weatherLiveData;
    }

    public void loadWeatherDataAsync(Context ctx, String longitude, String latitude) {
        // asynchronous call to download and parse data in the background
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Pre async download----");
                    new DownloadController(ctx, longitude,latitude).postRequest(new DownloadController.VolleyCallback(){
                        @Override
                        public void onSuccess(List<Weather> weatherData){
                            weatherLiveData.postValue(weatherData);
                        }
                    });
                } catch (Exception e) {
                    // do nothing or clear LiveData;
                    // to signal errors from background tasks we need custom data class
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

