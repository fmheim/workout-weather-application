package se.kth.fmheim.workoutweather.networking;
/*
Class to download json data from the internet
using volley
first downloading city information
then - on response - using the parsed coordinates to download weather data
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.data.JSONParser;
import se.kth.fmheim.workoutweather.data.Weather;

public class Downloader {

    private static final String LOG_TAG = Downloader.class.getSimpleName();
    private List<Weather> mWeatherData;
    private String[] mCoordinates = new String[2];


    private final RequestQueue mQueue;
    private String mWeatherUrl;
    private String mCityUrl;
    private final Context mCtx;
    private String mLongitude;
    private String mLatitude;
    private JSONParser parser;


    public Downloader(Context ctx) {
        /*
        Constructor :
        - needs context
        - sets request queue
        */
        mQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        mCtx = ctx;
        parser = new JSONParser();
    }

    public RequestQueue getQueue() {
        return mQueue;
    }

    public void setCityUrl(String cityName) {
        mCityUrl = "https://www.smhi.se/wpt-a/backend_solr/autocomplete/search/" + cityName;
        //mCityUrl = "https://maceo.sth.kth.se/wpt-a/backend_solr/autocomplete/search/" + cityName;
    }

    private void setWeatherUrl(String longitude, String latitude) {
        //mWeatherUrl = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/"
        // + longitude + "/lat/" + latitude + "/"; //for development
        mWeatherUrl = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/"
                + longitude + "/lat/" + latitude + "/data.json";
    }

    public interface VolleyCallback {
        // to return List of Weather when it was received from responseListener
        void onSuccess(List<Weather> weatherData);
    }

    public void postRequest(final VolleyCallback callback) {
        JsonArrayRequest cityRequest = new JsonArrayRequest(Request.Method.GET,
                mCityUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray root) {
                        /*
                        On response of city information --> download weather data!
                         */
                        Log.d(LOG_TAG, "City-Response from volley request gotten from\n " +
                                mCityUrl);
                        try {
                            mCoordinates = parser.parseToCoordinates(root);
                            setWeatherUrl(mCoordinates[0], mCoordinates[1]);
                            JsonObjectRequest weatherRequest = new JsonObjectRequest(Request.Method.GET,
                                    mWeatherUrl,
                                    null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject root) {
                                            Log.d(LOG_TAG, "Weather-Response from volley request gotten from\n " +
                                                    mWeatherUrl + " " + mCoordinates[0] + " " + mCoordinates[1]);
                                            try {
                                                mWeatherData = parser.parseToWeather(root);
                                                callback.onSuccess(mWeatherData); //to return List with weatherData
                                            } catch (JSONException | ParseException mE) {
                                                Toast toast = Toast.makeText(mCtx, R.string.download_not_possible,
                                                        Toast.LENGTH_LONG);
                                                toast.show();
                                                mE.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError mVolleyError) {
                                            Toast toast = Toast.makeText(mCtx, R.string.download_not_possible,
                                                    Toast.LENGTH_LONG);
                                            toast.show();
                                            mVolleyError.printStackTrace();
                                        }
                                    });
                            weatherRequest.setTag(mCtx);
                            mQueue.add(weatherRequest);
                        } catch (JSONException mE) {
                            Toast toast = Toast.makeText(mCtx, R.string.download_not_possible,
                                    Toast.LENGTH_LONG);
                            toast.show();
                            mE.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError mVolleyError) {
                        Toast toast = Toast.makeText(mCtx, R.string.download_not_possible,
                                Toast.LENGTH_LONG);
                        toast.show();
                        mVolleyError.printStackTrace();
                    }
                });
        cityRequest.setTag(mCtx);
        mQueue.add(cityRequest);
    }


}
