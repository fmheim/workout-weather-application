package se.kth.fmheim.workoutweather.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.kth.fmheim.workoutweather.model.Weather;

public class JSONParser {
    private static final String
            LOG_TAG = JSONParser.class.getSimpleName(),
            VALID_TIME = "validTime",
            PARAMETERS = "parameters",
            NAME = "name",
            VALUES = "values",
            TEMPERATURE = "t",
            CLOUD_COVERAGE = "Wsymb2",
            REFERENCE_TIME = "referenceTime",
            APPROVED_TIME = "approvedTime",
            TIME_Series = "timeSeries";


    public static Weather[] parseToWeather(JSONObject root) throws JSONException {
        /*
            Parsing JASON-data into weather objects and returning array of all Weather data for next 10 days
        */
        String approvedTime = root.getString(APPROVED_TIME);
        String referenceTime = root.getString(REFERENCE_TIME);
        JSONArray timeSeries = root.getJSONArray(TIME_Series);
        Weather[] weatherForTenDaysArr = new Weather[timeSeries.length()]; //for each time entry new weather object
        for (int i = 0; i < timeSeries.length(); i++) {
            /*
            looping through packages of weather data for provided times
            */
            weatherForTenDaysArr[i] = new Weather();
            JSONObject parametersAtTime = timeSeries.getJSONObject(i);
            String validTime = parametersAtTime.getString(VALID_TIME);

            weatherForTenDaysArr[i].setDate(getDate(validTime));
            weatherForTenDaysArr[i].setTime(getTime(validTime));
            weatherForTenDaysArr[i].setApprovedTime(approvedTime);
            weatherForTenDaysArr[i].setReferenceTime(referenceTime);

            JSONArray parameters = parametersAtTime.getJSONArray(PARAMETERS); //one set of parameters for a valid time

            for (int j = 0; j < parameters.length(); j++) {
            /*
            looping through parameters and parsing the important ones to weather object
            */
                JSONObject parameter = parameters.getJSONObject(j); //one single parameter in set of parameters
                JSONArray values = parameter.getJSONArray(VALUES);  //one set of Values for a valid time

                //Log.d(LOG_TAG, Integer.toString(i) + Integer.toString(j));
                if (TEMPERATURE.equals(parameter.getString(NAME))) {
                    weatherForTenDaysArr[i].setTemperature(values.getDouble(0));//
                    Log.d(LOG_TAG, "Temp. assigned: " + Double.toString(values.getDouble(0)) );
                }
                if (CLOUD_COVERAGE.equals(parameter.getString(NAME))) {
                    weatherForTenDaysArr[i].setClouds(getCloud(values.getInt(0)) );
                    Log.d(LOG_TAG, "Cloud status assigned: " + Double.toString(values.getInt(0)) );
                }
            }
        }
        return weatherForTenDaysArr;
    }

    private static String getDate(String validTime) {
        return validTime.substring(0, 10);
    }

    private static String getTime(String validTime) {
        String time = validTime.substring(11, 13);
        if ("0".equals(time.substring(0,1)) ) {
            time = time.substring(1);
        }
        return time;
    }

    private static String getCloud(int cloudValue) {
        /*
        decoding cloud coverage values
         */
        switch (cloudValue) {
            case 1:
                return "Clear sky";
            case 2:
                return "Nearly clear sky";
            case 3:
                return "Variable cloudiness";
            case 4:
                return "Halfclear sky";
            case 5:
                return "Cloudy sky";
            case 6:
                return "Overcast";
            case 7:
                return "Fog";
            case 8:
                return "Light rain showers";
            case 9:
                return "Moderate rain showers";
            case 10:
                return "Heavy rain showers";
            case 11:
                return "Thunderstorm";
            case 12:
                return "Light sleet showers";
            case 13:
                return "Moderate sleet showers";
            case 14:
                return "Heavy sleet showers";
            case 15:
                return "Light snow showers";
            case 16:
                return "Moderate snow showers";
            case 17:
                return "Heavy snow showers";
            case 18:
                return "Light rain";
            case 19:
                return "Moderate rain";
            case 20:
                return "Heavy rain";
            case 21:
                return "Thunder";
            case 22:
                return "Light sleet";
            case 23:
                return "Moderate sleet";
            case 24:
                return "Heavy sleet";
            case 25:
                return "Light snowfall";
            case 26:
                return "Moderate snowfall";
            case 27:
                return "Heavy snowfall";
            default:
                return "no info";
        }
    }
}

