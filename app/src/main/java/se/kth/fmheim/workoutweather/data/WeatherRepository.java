package se.kth.fmheim.workoutweather.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.networking.Downloader;

public class WeatherRepository {
    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();
    /*
        A Repository class provides a clean API for data access to the rest of the application.
        ViewModel has to call only these methods to interact with Database
        executed NOT on main thread
         */
    private final WeatherDao mWeatherDao;
    private final MutableLiveData<List<Weather>> mWeatherLiveDataFromWeb = new MutableLiveData<>();
    private final Downloader mDownloader;

    public WeatherRepository(Application application) {
        WeatherDatabase database = WeatherDatabase.getDatabase(application);
        mWeatherDao = database.weatherDao();
        mDownloader = new Downloader(application.getApplicationContext());
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Weather>> getWeatherData() {
        Log.d(LOG_TAG, "Getting LiveData from Repository");
        return mWeatherDao.getLiveWeatherData();
    }

    public void insert(List<Weather> weatherData) {
        WeatherDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherDao.insert(weatherData);
        });
    }

    public void update(List<Weather> weatherData) {
        WeatherDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherDao.update(weatherData);
        });
    }

    public RequestQueue getQueue() {
        return mDownloader.getQueue();
    }


    public void delete(Weather weather) {
        WeatherDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherDao.delete(weather);
        });
    }

    public void deleteAllWeatherData() {
        WeatherDatabase.databaseWriteExecutor.execute(() -> {
            mWeatherDao.deleteAllWeatherData();
        });
    }

    public void loadWeatherDataAsync(Context ctx, String longitude, String latitude) {
        // asynchronous call to download and parse data in the background
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Pre async download....");
                    mDownloader.setUrl(longitude, latitude);
                    mDownloader.postRequest(new Downloader.VolleyCallback() {
                        @Override
                        public void onSuccess(List<Weather> weatherData) {
                            deleteAllWeatherData();
                            insert(weatherData);
                            Toast toast = Toast.makeText(ctx, R.string.data_updated,
                                    Toast.LENGTH_SHORT);
                            toast.show();
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