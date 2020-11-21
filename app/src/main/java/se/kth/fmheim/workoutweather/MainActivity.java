package se.kth.fmheim.workoutweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import se.kth.fmheim.workoutweather.data.Weather;
import se.kth.fmheim.workoutweather.data.WeatherRepository;
import se.kth.fmheim.workoutweather.view.WeatherItem;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;
import se.kth.fmheim.workoutweather.view.WeatherViewModel;
import se.kth.fmheim.workoutweather.view.WeatherViewModelFactory;

public class MainActivity extends AppCompatActivity {
    //logging
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //views
    private EditText editLongitude;
    private EditText editLatitude;
    private TextView texViewApprovedTime;
    private TextView textViewCoordinates;
    private TextView textViewNoNet;
    private RecyclerView recyclerView;
    //data
    private WeatherRepository weatherRepository;
    private WeatherViewModel weatherViewModel;
    private String longitude;
    private String latitude;
    //network
    private boolean isConnected;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetwork;
    private long lastDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //views
        setContentView(R.layout.activity_main);
        editLongitude = (EditText) findViewById(R.id.editText_longitude);
        editLatitude = (EditText) findViewById(R.id.editText_latitude);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        texViewApprovedTime = (TextView) findViewById(R.id.textView_approvedTime);
        textViewCoordinates = (TextView) findViewById(R.id.textView_coordinates);
        textViewNoNet = (TextView) findViewById(R.id.textView_noNet);
        //data
        weatherRepository = new WeatherRepository(this.getApplication());
        longitude = "0";
        latitude = "0";
        lastDownload = 0;
        /*
        create a ViewModel the first time the system calls this activity's onCreate();
        re-created activities instances receive the same ViewModel instance created
        by the first activity.
        */
        weatherViewModel = new ViewModelProvider
                (this, WeatherViewModelFactory.getInstance(this.getApplication()))
                .get(WeatherViewModel.class);
        weatherViewModel.getWeatherLiveData().observe(this, new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weatherData) {
                Log.d(LOG_TAG, "LiveData changed");
                if (weatherData.size() != 0) {
                    fillRecycleView(weatherData);
                    printApprovedTime(weatherData.get(0));
                    printCoordinates(weatherData.get(0));
                    Log.d(LOG_TAG, "Size of List: " + String.valueOf(weatherData.size()));
                }
            }
        });
        //network
        connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected && (System.currentTimeMillis() - lastDownload) > 6) {
            lastDownload = 0;
            weatherRepository.loadWeatherDataAsync(this, longitude, latitude);
            Log.d(LOG_TAG, "On Start, " + longitude + ", " + latitude);
        }
    }

    public void onChangeLocation(View view) {
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (textViewNoNet.getVisibility() == View.VISIBLE)
                textViewNoNet.setVisibility(View.INVISIBLE);
            longitude = editLongitude.getText().toString();
            latitude = editLatitude.getText().toString();
            if (longitude.trim().isEmpty() || latitude.trim().isEmpty()) {
                Toast toast = Toast.makeText(this, R.string.wrong_input,
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Log.d(LOG_TAG, "Pre loadWeatherDataAsync....");
                weatherRepository.loadWeatherDataAsync(this, longitude, latitude);
                lastDownload = System.currentTimeMillis();
            }
        } else {
            Log.d(LOG_TAG, "No network connection");
            Toast toast = Toast.makeText(this, R.string.no_network_connection,
                    Toast.LENGTH_SHORT);
            toast.show();
            textViewNoNet.setVisibility(View.VISIBLE);
        }

    }


    private void fillRecycleView(List<Weather> weatherData) {
        /*
        For each element in List with weather data at time
        a weather item in the recycler view is filled
        */
        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();
        for (Weather weatherAtTime : weatherData) {
            int weatherSymbol = weatherAtTime.getSymbol();
            String date = weatherAtTime.getDate() + ", " + weatherAtTime.getTime() + " o'clock";
            String temperature = weatherAtTime.getTemperature() + " °C";
            String workoutRecommendation = weatherAtTime.getWorkoutRecommendation();
            weatherItemList.add(new WeatherItem(weatherSymbol, date, temperature, workoutRecommendation));
        }
        RecyclerView.Adapter adapter = new WeatherItemsAdapter(weatherItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(LOG_TAG, "...filled recycler view");

    }

    private void printApprovedTime(Weather oneWeather) {
        String approvedTime = oneWeather.getApprovedTime();
        if (approvedTime.indexOf("2") == 0) {
            String date = approvedTime.substring(0, 10);
            String time = approvedTime.substring(11, 19);
            texViewApprovedTime.setText(getString(R.string.approvedTime, date, time));
        } else
            texViewApprovedTime.setText(approvedTime); //for init
        Log.d(LOG_TAG, "...printed approved date recycler view");
    }

    private void printCoordinates(Weather oneWeather) {
        String coordinates = oneWeather.getCoordinates();
        textViewCoordinates.setText(coordinates);
    }
}
