package se.kth.fmheim.workoutweather.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import se.kth.fmheim.workoutweather.data.WeatherEntity;
import se.kth.fmheim.workoutweather.data.WeatherRepository;

public class WeatherViewModel  extends AndroidViewModel {

    /*
    View Model that survives entire life cycle
     */
    private WeatherRepository repository;
    private LiveData<List<WeatherEntity>> allWeathers;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        repository = new WeatherRepository(application);
        allWeathers = repository.getAllNotes();
    }

    public void insert(WeatherEntity weather){
        repository.insert(weather);
    }

    public void update(WeatherEntity weather){
        repository.insert(weather);
    }

    public void delete(WeatherEntity weather){
        repository.insert(weather);
    }

    public void deleteAllWeathers(){
        repository.deleteAllWeathers();
    }

    public LiveData<List<WeatherEntity>> getAllWeathers() {
        return allWeathers;
    }

}
