package se.kth.fmheim.workoutweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

import se.kth.fmheim.workoutweather.networking.JSONVolleyDownloader;
import se.kth.fmheim.workoutweather.view.WeatherItem;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONVolleyDownloader parser = new JSONVolleyDownloader(this, "14.333", "60.383");
        Log.d(LOG_TAG, "start");
        parser.fetchData();
        Log.d(LOG_TAG, "end");

        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();
        weatherItemList.add(new WeatherItem(R.drawable.day_1, "Date", "15 °C", "Good day for a run"));
        weatherItemList.add(new WeatherItem(R.drawable.day_2, "Date", "17°C", "Good day for a swim"));
        weatherItemList.add(new WeatherItem(R.drawable.day_2, "Date", "13°C", "Good day for a ride"));

        mRecyclerView = findViewById(R.id.recyclerView);
        // mRecyclerView.setHasFixedSize(true);  //only if recycler view doesn't change size
        mAdapter = new WeatherItemsAdapter(weatherItemList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }



    public


}