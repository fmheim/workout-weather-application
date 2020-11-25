package se.kth.fmheim.workoutweather;
/*
Felix Heim
Assignment 1, Android
Mobile Sports Application and Datamining
Sports Technology
KTH
November 2020
Simple weather app with workout recommendation
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

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
    private AutoCompleteTextView editCityName;
    private TextView textViewApprovedTime;
    private TextView textViewCityName;
    private TextView textViewNoNet;
    private RecyclerView recyclerView;
    String[] defaultCities = {" Göteborg", " Luleå", " Lund", " Malmö", " Stockholm", " Uppsala"};
    //data
    private WeatherRepository weatherRepository;
    private String mCityName;
    //network
    private RequestQueue mRequestQueue;
    private static long lastDownload;
    private static boolean isConnected;
    private static final int DOWNLOAD_UPDATE_INTERVAL = 600000; // every 10min
    private static final int NET_CHECK_INTERVAL = 2000; //every 2 seconds
    private final Handler timerHandler = new Handler();
    private final Runnable timerRunnable = new Runnable() {
        //for checking network connection
        @Override
        public void run() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplication()
                    .getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            boolean isVisible = textViewNoNet.getVisibility() == View.VISIBLE;
            if (isConnected && isVisible) {
                textViewNoNet.setVisibility(View.INVISIBLE);
            } else if (!isConnected && !isVisible)
                textViewNoNet.setVisibility(View.VISIBLE);
            //Update if download is older than download update interval
            String oldCityName = textViewCityName.getText().toString().trim();
            Log.d(LOG_TAG, "No download since:  "
                    + (System.currentTimeMillis() - lastDownload) / 1000 + " seconds");
            if (!oldCityName.isEmpty()) {
                mCityName = oldCityName;
                if (isConnected &&
                        (System.currentTimeMillis() - lastDownload) > DOWNLOAD_UPDATE_INTERVAL) {
                    String oldApprovedTime = textViewApprovedTime.getText().toString();
                    weatherRepository.loadAndInsertData(mCityName);
                    lastDownload = System.currentTimeMillis();
                    if (!textViewApprovedTime.getText().toString().equals(oldApprovedTime)) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.weather_updated,
                                Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d(LOG_TAG, "Updated weather for " + mCityName);
                    }
                }
            }
            timerHandler.postDelayed(this, NET_CHECK_INTERVAL);
            // Log.d(LOG_TAG, "Timer: Is connected? " + isConnected);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //views
        setContentView(R.layout.activity_main);
        editCityName = (AutoCompleteTextView) findViewById(R.id.autoEditText_cityName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textViewApprovedTime = (TextView) findViewById(R.id.textView_approvedTime);
        textViewCityName = (TextView) findViewById(R.id.textView_cityName);
        textViewNoNet = (TextView) findViewById(R.id.textView_noNet);
        //Autocomplete
        ArrayAdapter autoCompleteAdapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                defaultCities);
        editCityName.setAdapter(autoCompleteAdapter);
        editCityName.setThreshold(0);
        editCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCityName.showDropDown();
            }
        });
        //data
        weatherRepository = new WeatherRepository(this.getApplication());
        mRequestQueue = weatherRepository.getRequestQueue();
        /*
        create a ViewModel the first time the system calls this activity's onCreate();
        re-created activities instances receive the same ViewModel instance created
        by the first activity.
        */
        WeatherViewModel weatherViewModel = new ViewModelProvider
                (this, WeatherViewModelFactory.getInstance(this.getApplication()))
                .get(WeatherViewModel.class);
        weatherViewModel.getWeatherLiveData().observe(this, new Observer<List<Weather>>() {
            @Override
            public void onChanged(List<Weather> weatherData) {
                Log.d(LOG_TAG, "LiveData changed");
                if (weatherData.size() != 0) {
                    fillRecycleView(weatherData);
                    printApprovedTime(weatherData.get(0));
                    printCityName(weatherData.get(0));
                    Log.d(LOG_TAG, "Size of weather-list: " + weatherData.size());
                    boolean onChangedFinished = true;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //network check and auto update timer
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        mRequestQueue.cancelAll(this);
    }

    public void onChangeLocation(View view) {
        if (isConnected) {
            mCityName = editCityName.getText().toString().trim();
            if (mCityName.isEmpty()) {
                Toast toast = Toast.makeText(this, R.string.empty_input,
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Log.d(LOG_TAG, "Pre loadAndInsertData....");
                String oldApprovedTime = textViewApprovedTime.getText().toString();
                String oldCityName = textViewCityName.getText().toString();
                weatherRepository.loadAndInsertData(mCityName);

                //if something changed toast download complete
                if (!textViewApprovedTime.getText().toString().equals(oldApprovedTime)
                        || !oldCityName.equals(textViewCityName.getText().toString())) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.download_complete,
                            Toast.LENGTH_SHORT);
                    toast.show();

               //if input same as city name and same approved time toast up to date
                } else if (textViewApprovedTime.getText().toString().equals(oldApprovedTime)
                        && (oldCityName.equals(mCityName))) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.up_to_date,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }//else download not possible from downloader
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
        For each element in list with weather data  at time (gotten from live data change)
        a weather item in the recycler view is filled
        */
        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();
        for (Weather weatherAtTime : weatherData) {
            int weatherSymbol = weatherAtTime.getSymbol();
            String date = weatherAtTime.getDate() + "\n" + weatherAtTime.getTime();
            String temperature = weatherAtTime.getTemperature() + "°C";
            String workoutRecommendation = weatherAtTime.getWorkoutRecommendation();
            weatherItemList.add(new WeatherItem(weatherSymbol, date, temperature, workoutRecommendation)); //fill Item and add to list
        }
        RecyclerView.Adapter<WeatherItemsAdapter.WeatherItemsViewHolder> adapter = new WeatherItemsAdapter(weatherItemList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d(LOG_TAG, "...filled recycler view");
    }

    private void printApprovedTime(Weather oneWeather) {
        String approvedTime = oneWeather.getApprovedTime();
        if (approvedTime.indexOf("2") == 0) {
            String date = approvedTime.substring(0, 10);
            String time = approvedTime.substring(11, 19);
            textViewApprovedTime.setText(getString(R.string.approvedTime, date, time));
        } else
            textViewApprovedTime.setText(approvedTime); //for init
        Log.d(LOG_TAG, "...printed approved date");
    }

    private void printCityName(Weather oneWeather) {
        String cityName = oneWeather.getCityName();
        textViewCityName.setText(cityName);
        Log.d(LOG_TAG, "...printed approved city name");
    }
}
