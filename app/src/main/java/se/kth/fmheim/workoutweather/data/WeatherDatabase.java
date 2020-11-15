package se.kth.fmheim.workoutweather.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WeatherEntity.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    private static WeatherDatabase instance;

    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDatabase getInstance(Context context) {
    /*
    singleton for only one Database
     */
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WeatherDatabase.class, "weather_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsyncTAsk(instance).execute();
        }
    };
    private static class PopulateDbAsyncTAsk extends AsyncTask<Void, Void, Void> {
        //populate data base when created
        private WeatherDao weatherDao;
        private PopulateDbAsyncTAsk(WeatherDatabase db){
            weatherDao = db.weatherDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            weatherDao.insert(new WeatherEntity("00",
                    "00",
                    0,
                    "00",
                    "00",
                    "00",
                    0,
                    "01"));
            weatherDao.insert(new WeatherEntity("01",
                    "01",
                    1,
                    "01",
                    "1",
                    "01",
                    1,
                    "01"));
            weatherDao.insert(new WeatherEntity("01",
                    "02",
                    2,
                    "02",
                    "02",
                    "02",
                    2,
                    "02"));
            return null;
        }
    }
}
