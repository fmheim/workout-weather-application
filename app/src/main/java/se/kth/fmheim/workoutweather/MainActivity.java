package se.kth.fmheim.workoutweather;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import se.kth.fmheim.workoutweather.data.WeatherEntity;
import se.kth.fmheim.workoutweather.networking.DownloadController;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;
import se.kth.fmheim.workoutweather.view.WeatherViewModel;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText editLongitude;
    private EditText editLatitude;
    private DownloadController mDownloadController;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Bundle mBundleRecyclerViewState;
    private WeatherViewModel mWeatherViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mDownloadController = new DownloadController(this, this, "14.333", "60,383");
        editLongitude = (EditText) findViewById(R.id.editText_longitude);
        editLatitude = (EditText) findViewById(R.id.editText_latitude);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        mWeatherViewModel = new ViewModelProvider (this).get(WeatherViewModel.class);
 //       mAdapter = new WeatherItemsAdapter();
  //      mLayoutManager = new LinearLayoutManager(this);
  //      mRecyclerView.setLayoutManager(mLayoutManager);
    //    mRecyclerView.setAdapter(mAdapter);

/*
//no time for Implementation of view model and database
        mWeatherViewModel.getAllWeathers().observe(this, new Observer<List<WeatherEntity>>() {
            @Override
            public void onChanged(List<WeatherEntity> mWeatherEntities) {
                Toast toast = Toast.makeText(this, R.string.changed, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

 */


    }

    @Override
    protected void onStart() {
        super.onStart();
        //mDownloadController.postRequest();
        //if (System.currentTimeMillis() - lastDownload > 3_600_000) {
        //
        //}
    }

    public void onChangeLocation(View view) {
        String longitude = editLongitude.getText().toString();
        String latitude = editLatitude.getText().toString();
        if (longitude == null || latitude == null) {
            Toast toast = Toast.makeText(this, R.string.wrong_input,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
       DownloadController dc = new DownloadController(this, this, longitude , latitude);
        dc.postRequest();
        Log.d(LOG_TAG,dc.getUrl());
    }




}