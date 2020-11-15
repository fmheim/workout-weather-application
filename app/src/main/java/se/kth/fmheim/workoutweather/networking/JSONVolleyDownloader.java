package se.kth.fmheim.workoutweather.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import se.kth.fmheim.workoutweather.data.JSONParser;
import se.kth.fmheim.workoutweather.model.Weather;

public class JSONVolleyDownloader {

    private static final String LOG_TAG = JSONVolleyDownloader.class.getSimpleName();
    private RequestQueue mQueue;
    private String url;
    private Context ctx;

    /*
    Two constructor :
     */
    public JSONVolleyDownloader(Context ctx, String longitude, String latitude) {
        this.ctx = ctx;
        url = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/"
                + longitude + "/lat/ " + latitude + "/"; //for development
        /*
      url = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/"
                + longitude + "/lat/ "+ latitude + "/data.json; //for development
        */
        mQueue = Volley.newRequestQueue(ctx);
    }


    public String getUrl() {
        return url;
    }

    public void fetchData() {
    /*
    Method to download data from url feed by using volley
     */
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, root -> {
            try {
                Weather[] tenDayWeather = JSONParser.parseToWeather(root);
                for (int i = 0; i < tenDayWeather.length; i++) {
                    tenDayWeather[i].logWeather();
                    Log.d(LOG_TAG, Integer.toString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        mQueue.add(request);
    }

}
