/*
Felix Heim
Mobile Application
Assignment 1

Unfortunately not all features implemented due to lack of time!
 */


package se.kth.fmheim.workoutweather.networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.data.JSONParser;
import se.kth.fmheim.workoutweather.data.Weather;

public class Downloader {

    private static final String LOG_TAG = Downloader.class.getSimpleName();
    private List<Weather> mWeatherData;



    private final RequestQueue mQueue;
    private String mUrl;
    private Context mCtx;


    public Downloader(Context ctx) {
        /*
        Constructor :
        - needs context
        - sets request queue
        */
        mQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        mCtx = ctx;
    }

    public RequestQueue getQueue() {
        return mQueue;
    }

    public void setUrl(String longitude, String latitude){
        //mUrl = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/"
              //  + longitude + "/lat/" + latitude + "/"; //for development
         mUrl = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/"
         + longitude + "/lat/" + latitude + "/data.json";
    }


    public interface VolleyCallback {
        // to return List of Weather when it was received from responseListener
        void onSuccess(List<Weather> weatherData);
    }

    public void postRequest(final VolleyCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                mUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject root) {
                        Log.d(LOG_TAG, "Response from volley request gotten from\n " +
                                mUrl);
                        try {
                            mWeatherData = new JSONParser().parseToWeather(root);
                            callback.onSuccess(mWeatherData); //to return List with weatherData
                        } catch (JSONException mE) {
                            mE.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError mVolleyError) {
                        Toast toast = Toast.makeText(mCtx, R.string.out_off_bounds,
                                Toast.LENGTH_SHORT);
                        toast.show();
                        mVolleyError.printStackTrace();
                    }
                });
        mQueue.add(request);
    }
}
