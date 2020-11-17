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

    @Insert
    void insert(WeatherEntity weather);

    @Update
    void update(WeatherEntity weather);

    @Delete
    void delete(WeatherEntity weather);

    @Query("DELETE FROM weather_table")
    void deleteAllWeathers();

    @Query("SELECT * FROM weather_table")
    LiveData<List<WeatherEntity>> getAllWeathers();



}
