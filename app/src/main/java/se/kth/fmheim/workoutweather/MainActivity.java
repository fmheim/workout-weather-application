package se.kth.fmheim.workoutweather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.kth.fmheim.workoutweather.model.Weather;
import se.kth.fmheim.workoutweather.networking.DownloadController;
import se.kth.fmheim.workoutweather.view.WeatherItem;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;
import se.kth.fmheim.workoutweather.view.WeatherViewModel;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText editLongitude;
    private EditText editLatitude;
    private DownloadController mDownloadController;
    private List<Weather> mWeatherData;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private WeatherViewModel mWeatherViewModel;
    private Activity mActivity;
    private TextView mTextViewApprovedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mDownloadController = new DownloadController(this, this, "14.333", "60,383");
        editLongitude = (EditText) findViewById(R.id.editText_longitude);
        editLatitude = (EditText) findViewById(R.id.editText_latitude);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTextViewApprovedTime = (TextView) findViewById(R.id.textView_appovedTime);
        mActivity = this;
        // create a ViewModel the first time the system calls this activity's onCreate();
        // re-created activities instances receive the same ViewModel instance created
        // by the first activity.


       mWeatherViewModel =
                new ViewModelProvider(this).get(WeatherViewModel.class);
        mWeatherViewModel.getWeatherData().observe(this, new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weatherData) {
               /*
                String longitude = editLongitude.getText().toString();
                String latitude = editLatitude.getText().toString();
                if (longitude.trim().isEmpty() || latitude.trim().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), R.string.wrong_input,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }else
                */
                Log.d(LOG_TAG, "changed----------------");
                fillRecycleView(weatherData);


            }
        });

    }




    public void onChangeLocation(View view) {
        String longitude = editLongitude.getText().toString();
        String latitude = editLatitude.getText().toString();
        if (longitude.trim().isEmpty() || latitude.trim().isEmpty()) {
            Toast toast = Toast.makeText(this, R.string.wrong_input,
                    Toast.LENGTH_SHORT);
            toast.show();
        } else
            Log.d(LOG_TAG, "pre async");
            mWeatherViewModel.loadWeatherDataAsync(this, longitude, latitude);
    }


    private void fillRecycleView(List<Weather> weatherData) {
        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();
        /*
        For each element in Array with weather data at time a weather item in the recycler view is filled
        */

        for (Weather weatherAtTime : weatherData) {
            int weatherSymbol = weatherAtTime.getSymbol();
            String date = weatherAtTime.getDate() + ", " + weatherAtTime.getTime() + " o'clock";
            String temperature = String.valueOf(weatherAtTime.getTemperature()) + " °C";
            String workoutRecommendation = weatherAtTime.getWorkoutRecommendation();
            weatherItemList.add(new WeatherItem(weatherSymbol, date, temperature, workoutRecommendation));
        }

        // mRecyclerView.setHasFixedSize(true);  //only if recycler view doesn't change size
        mAdapter = new WeatherItemsAdapter(weatherItemList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void printApprovedTime(Weather oneWeather) {
        String approvedTime = oneWeather.getApprovedTime();
        String date = approvedTime.substring(0, 10);
        String time = approvedTime.substring(11, 19);

        mTextViewApprovedTime.setText("Approved time: " + date + " " + time);
    }

}