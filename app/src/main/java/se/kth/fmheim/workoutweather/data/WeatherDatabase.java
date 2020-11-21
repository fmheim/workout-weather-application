package se.kth.fmheim.workoutweather.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    private static final String LOG_TAG = WeatherDatabase.class.getSimpleName();

    public abstract WeatherDao weatherDao();

    private static volatile WeatherDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);  //to run in background

    static WeatherDatabase getDatabase(final Context context) {
    /*
    singleton for only one Database
     */
        if (INSTANCE == null) {
            synchronized (WeatherDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherDatabase.class, "weather_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase database) {
            super.onCreate(database);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                WeatherDao dao = INSTANCE.weatherDao();
                dao.deleteAllWeatherData();

                List<Weather> startWeatherData = new ArrayList<>();
                Weather weather = new Weather();
                startWeatherData.add(weather);
                startWeatherData.add(weather);
                startWeatherData.add(weather);

                dao.insert(startWeatherData);
                Log.d(LOG_TAG, "New Database initialized.");
                Log.d(LOG_TAG, "Approved Time database init: " + weather.getApprovedTime());

            });
        }
    };


}
