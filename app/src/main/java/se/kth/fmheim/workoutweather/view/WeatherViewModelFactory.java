package se.kth.fmheim.workoutweather.view;
    /*
    class needed to make a view model
    */

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private static WeatherViewModelFactory instance;

    private WeatherViewModelFactory(Application application) {
        mApplication = application;
    }

    public static WeatherViewModelFactory getInstance(Application application) {

        if (instance == null) {
            synchronized (WeatherViewModelFactory.class) {
                if (instance == null) {
                    return new WeatherViewModelFactory(application);
                }
            }
        }
        return instance;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeatherViewModel.class))
            return (T) new WeatherViewModel(mApplication);
        return null;
    }
}
