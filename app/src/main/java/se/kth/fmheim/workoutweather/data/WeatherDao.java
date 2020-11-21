package se.kth.fmheim.workoutweather.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDao {

    /*
    Data access object
     */

    @Insert
    void insert(List<Weather> weatherData);



    @Update
    void update(List<Weather> weatherData);

    @Delete
    void delete(Weather weather);

    @Query("DELETE FROM weather_table")
    void deleteAllWeatherData();

    @Query("SELECT * FROM weather_table")
    LiveData<List<Weather>> getLiveWeatherData();

    @Query("SELECT * FROM weather_table")
    List<Weather> getWeatherData();
}
