/*
Felix Heim
Mobile Application
Assignment 1

Unfortunately not all features implemented due to lack of time!
 */


package se.kth.fmheim.workoutweather.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.data.JSONParser;
import se.kth.fmheim.workoutweather.data.WeatherEntity;
import se.kth.fmheim.workoutweather.model.Weather;
import se.kth.fmheim.workoutweather.view.WeatherItem;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;
import se.kth.fmheim.workoutweather.view.WeatherViewModel;

public class DownloadController {

    private static final String LOG_TAG = DownloadController.class.getSimpleName();
    private final Context mCtx;
    //private final TextView mTextViewApprovedTime;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private   List<Weather> mWeatherData;
    //private final Activity mActivity;
    private RequestQueue mQueue;
    private String mUrl;



    public DownloadController(Context ctx, String longitude, String latitude) {
        /*
        Constructor :
        - needs context and location data as input
        - sets url, request queue
        */
        mCtx = ctx;
        //mActivity = activity;
        mQueue = VolleySingleton.getInstance(mCtx).getRequestQueue();
       //mRecyclerView = mActivity.findViewById(R.id.recyclerView);
        //mTextViewApprovedTime = mActivity.findViewById(R.id.textView_appovedTime);

        mUrl = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/"
                + longitude + "/lat/" + latitude + "/"; //for development


        //mUrl = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/"
        // + longitude + "/lat/"+ latitude + "/data.json";
    }

    public interface VolleyCallback{
        void onSuccess(List<Weather> weatherData);
    }

    public void postRequest( final VolleyCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                mUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject root) {
                        Log.d(LOG_TAG, "Response from volley request gotten.");
                        try {
                            mWeatherData = new JSONParser().parseToWeather(root);
                            //fillRecycleView(mWeatherData);
                            //printApprovedTime(mWeatherData.get(1));
                            callback.onSuccess(mWeatherData);
                        } catch (JSONException mE) {
                            mE.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError mVolleyError) {
                        mVolleyError.printStackTrace();
                    }
                });
        mQueue.add(request);

    }


    public String getUrl() {
        return mUrl;
    }

/*
    private void fillRecycleView(List<Weather> weatherForTenDays) {
        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();

        //For each element in Array with weather data at time a weather item in the recycler view is filled


        for (Weather weatherAtTime : weatherForTenDays) {
            int weatherSymbol = weatherAtTime.getSymbol();
            String date = weatherAtTime.getDate() + ", " + weatherAtTime.getTime() + " o'clock";
            String temperature = String.valueOf(weatherAtTime.getTemperature()) + " °C";
            String workoutRecommendation = weatherAtTime.getWorkoutRecommendation();
            weatherItemList.add(new WeatherItem(weatherSymbol, date, temperature, workoutRecommendation));
        }

        // mRecyclerView.setHasFixedSize(true);  //only if recycler view doesn't change size
        mAdapter = new WeatherItemsAdapter(weatherItemList);
        mLayoutManager = new LinearLayoutManager(mCtx);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void printApprovedTime(Weather oneWeather) {
        String approvedTime = oneWeather.getApprovedTime();
        String date = approvedTime.substring(0, 10);
        String time = approvedTime.substring(11, 19);

        mTextViewApprovedTime.setText("Approved time: " + date + " " + time);
    }

*/
}
