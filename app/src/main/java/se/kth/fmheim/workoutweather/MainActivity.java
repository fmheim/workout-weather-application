package se.kth.fmheim.workoutweather;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import se.kth.fmheim.workoutweather.networking.DownloadController;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText editLongitude;
    private EditText editLatitude;
    private DownloadController mDownloadController;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private RecyclerView mRecyclerView;
    private static Bundle mBundleRecyclerViewState;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mDownloadController = new DownloadController(this, this, "14.333", "60,383");
        editLongitude = (EditText) findViewById(R.id.editText_longitude);
        editLatitude = (EditText) findViewById(R.id.editText_latitude);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //mDownloadController.postRequest();
        //if (System.currentTimeMillis() - lastDownload > 3_600_000) { // 1 hour
        // downloadJokes(null);
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