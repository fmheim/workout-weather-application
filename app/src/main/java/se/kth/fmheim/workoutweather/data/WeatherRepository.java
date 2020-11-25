package se.kth.fmheim.workoutweather.data;
    /*
    Class to provides data access to the rest of the application.
    ViewModel has to call only these methods to interact with Database
    executed NOT on main thread
    */

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.android.volley.RequestQueue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import se.kth.fmheim.workoutweather.networking.Downloader;

public class WeatherRepository {
    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();

    private final WeatherDao mWeatherDao;
    private final Downloader mDownloader;


    public WeatherRepository(Application application) {
        WeatherDatabase database = WeatherDatabase.getDatabase(application);
        mWeatherDao = database.weatherDao();
        mDownloader = new Downloader(application.getApplicationContext());
    }

    public RequestQueue getRequestQueue() {
        return mDownloader.getQueue();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Weather>> getLiveWeatherData() {
        Log.d(LOG_TAG, "Getting LiveData from Repository");
        return mWeatherDao.getLiveWeatherData();
    }

    public void insert(List<Weather> weatherData) {
        WeatherDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherDao.insert(weatherData);
        });
        Log.d(LOG_TAG, "Inserted new data in database.");
    }

    public void deleteAllWeatherData() {
        WeatherDatabase.databaseWriteExecutor.execute(mWeatherDao::deleteAllWeatherData);
        Log.d(LOG_TAG, "Deleted old data in database.");

    }

    public void loadAndInsertData(String cityName) {
        // asynchronous call to download and parse data in the background
        // and update database
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Pre async download....");
                    mDownloader.setCityUrl(cityName);
                    mDownloader.postRequest(new Downloader.VolleyCallback() {
                        @Override
                        public void onSuccess(List<Weather> weatherData) {
                            deleteAllWeatherData();
                            try {
                                TimeUnit.MILLISECONDS.sleep(5); //without this delay live data updates the deleted database but doesn't notice the insertion...
                            } catch (InterruptedException mE) {
                                mE.printStackTrace();
                            }
                            insert(weatherData);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}