package se.kth.fmheim.workoutweather.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import se.kth.fmheim.workoutweather.model.Weather;

public class WeatherRepository {
    //ViewModel has to call only these methods to interact with Database
    private WeatherDao mWeatherDao;
    private LiveData<List<WeatherEntity>> allWeathers;

    public WeatherRepository(Application application){
       WeatherDatabase database = WeatherDatabase.getInstance(application);
       mWeatherDao = database.weatherDao();
       allWeathers = mWeatherDao.getAllWeathers();
    }

    public void insert (WeatherEntity weather){
        new InsertWeatherAsyncTask(mWeatherDao).execute(weather);
    }

    public void update (WeatherEntity weather){
        new UpdateWeatherAsyncTask(mWeatherDao).execute(weather);
    }

    public void delete (WeatherEntity weather){
        new DeleteWeatherAsyncTask(mWeatherDao).execute(weather);
    }

    public void deleteAllWeathers(){
        new DeleteAllWeathersAsyncTask(mWeatherDao).execute();
    }

    public LiveData<List<WeatherEntity>> getAllWeathers(){
        return allWeathers;
    }


    //DO IN BACKGROUND
    private static class InsertWeatherAsyncTask extends AsyncTask<WeatherEntity, Void, Void> {

    private WeatherDao weatherDao;
    private InsertWeatherAsyncTask(WeatherDao weatherDao){
        this.weatherDao = weatherDao;
    }
        @Override
        protected Void doInBackground(WeatherEntity... weathers) {
        weatherDao.insert(weathers[0]);
            return null;
        }
    }


    private static class UpdateWeatherAsyncTask extends AsyncTask<WeatherEntity, Void, Void> {

        private WeatherDao weatherDao;
        private UpdateWeatherAsyncTask(WeatherDao weatherDao){
            this.weatherDao = weatherDao;
        }
        @Override
        protected Void doInBackground(WeatherEntity... weathers) {
            weatherDao.update(weathers[0]);
            return null;
        }
    }

    private static class DeleteWeatherAsyncTask extends AsyncTask<WeatherEntity, Void, Void> {

        private WeatherDao weatherDao;
        private DeleteWeatherAsyncTask(WeatherDao weatherDao){
            this.weatherDao = weatherDao;
        }
        @Override
        protected Void doInBackground(WeatherEntity... weathers) {
            weatherDao.delete(weathers[0]);
            return null;
        }
    }

    private static class DeleteAllWeathersAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeatherDao weatherDao;
        private DeleteAllWeathersAsyncTask(WeatherDao weatherDao){
            this.weatherDao = weatherDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            weatherDao.deleteAllWeathers();
            return null;
        }
    }


}
