package se.kth.fmheim.workoutweather.networking;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.data.JSONParser;
import se.kth.fmheim.workoutweather.model.Weather;
import se.kth.fmheim.workoutweather.view.WeatherItem;
import se.kth.fmheim.workoutweather.view.WeatherItemsAdapter;

public class DownloadController {

    private static final String LOG_TAG = DownloadController.class.getSimpleName();
    private final Context mCtx;
    private final TextView mTextViewApprovedTime;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final Activity mActivity;
    private RequestQueue mQueue;
    private String mUrl;
    private Weather[] mWeatherArr;




    public DownloadController(Activity activity, Context ctx, String longitude, String latitude) {
        /*
        Constructor :
        - needs context and location data as input
        - sets url, request queue
        - gets json root object by starting a volley request
        */
        mCtx = ctx;
        mActivity = activity;
        mQueue = Volley.newRequestQueue(ctx);
        mRecyclerView = mActivity.findViewById(R.id.recyclerView);
        mTextViewApprovedTime = mActivity.findViewById(R.id.textView_appovedTime);
        mUrl = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/"
                + longitude + "/lat/" + latitude + "/"; //for development


        //mUrl = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/"
               // + longitude + "/lat/"+ latitude + "/data.json";
    }




    public void postRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                mUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject root) {
                        Log.d(LOG_TAG, "Response from volley request gotten.");
                        try {
                            mWeatherArr= JSONParser.parseToWeather(root);
                            fillRecycleView(mWeatherArr);
                            fillTextView(mWeatherArr[0]);
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


    private void fillRecycleView(Weather[] weatherForTenDays) {
        ArrayList<WeatherItem> weatherItemList = new ArrayList<>();
        /*
        For each element in Array with weather data at time a weather item in the recycler view is filled
        */

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

    private void fillTextView(Weather oneWeather){
     String approvedTime = oneWeather.getApprovedTime();
     String date = approvedTime.substring(0,10);
     String time = approvedTime.substring(11,19);

        mTextViewApprovedTime.setText("Approved time: " + date + " " + time) ;
    }

}
