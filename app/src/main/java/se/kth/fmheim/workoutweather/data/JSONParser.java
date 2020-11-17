package se.kth.fmheim.workoutweather.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.kth.fmheim.workoutweather.R;
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


    public List<Weather> parseToWeather(JSONObject root) throws JSONException {
        /*
            Parsing JASON-data into weather objects and returning array of all Weather data for next 10 days
        */
        String approvedTime = root.getString(APPROVED_TIME);
        String referenceTime = root.getString(REFERENCE_TIME);
        JSONArray timeSeries = root.getJSONArray(TIME_Series);
        Weather[] weatherForTenDaysArr = new Weather[timeSeries.length()]; //for each time entry new weather object
        Log.d(LOG_TAG, "parser init ");
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
                    Log.d(LOG_TAG, "Temp. assigned: " + Double.toString(values.getDouble(0)));
                }
                if (CLOUD_COVERAGE.equals(parameter.getString(NAME))) {
                    weatherForTenDaysArr[i].setClouds(getCloud(values.getInt(0)));
                    Log.d(LOG_TAG, "Cloud status assigned: " + Double.toString(values.getInt(0)));
                    weatherForTenDaysArr[i].setSymbol(getSymbol(values.getInt(0), weatherForTenDaysArr[i].getTime()));
                    weatherForTenDaysArr[i].setWorkoutRecommendation(getWorkoutRecommendation(values.getInt(0), weatherForTenDaysArr[i].getTemperature(),weatherForTenDaysArr[i].getTime()));
                }
            }
        }
        return new ArrayList<Weather>(Arrays.asList(weatherForTenDaysArr));
    }

    private String getDate(String validTime) {
        return validTime.substring(0, 10);
    }

    private String getTime(String validTime) {
        String time = validTime.substring(11, 13);
        if ("0".equals(time.substring(0, 1))) {
            time = time.substring(1);
        }
        return time;
    }

    private String getCloud(int cloudValue) {
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

    private int getSymbol(int cloudValue, String time) {

        boolean isDay = Integer.parseInt(time) > 5 && Integer.parseInt(time) < 18;
        if (isDay) {
            Log.d(LOG_TAG, "is day");
            switch (cloudValue) {
                case 1:
                    return R.drawable.day_1;
                case 2:
                    return R.drawable.day_2;
                case 3:
                    return R.drawable.day_3;
                case 4:
                    return R.drawable.day_4;
                case 5:
                    return R.drawable.day_5;
                case 6:
                    return R.drawable.day_6;
                case 7:
                    return R.drawable.day_7;
                case 8:
                    return R.drawable.day_8;
                case 9:
                    return R.drawable.day_9;
                case 10:
                    return R.drawable.day_10;
                case 11:
                    return R.drawable.day_11;
                case 12:
                    return R.drawable.day_12;
                case 13:
                    return R.drawable.day_13;
                case 14:
                    return R.drawable.day_14;
                case 15:
                    return R.drawable.day_15;
                case 16:
                    return R.drawable.day_16;
                case 17:
                    return R.drawable.day_17;
                case 18:
                    return R.drawable.day_18;
                case 19:
                    return R.drawable.day_19;
                case 20:
                    return R.drawable.day_20;
                case 21:
                    return R.drawable.day_21;
                case 22:
                    return R.drawable.day_22;
                case 23:
                    return R.drawable.day_23;
                case 24:
                    return R.drawable.day_24;
                case 25:
                    return R.drawable.day_25;
                case 26:
                    return R.drawable.day_26;
                case 27:
                    return R.drawable.day_17;
                default:
                    return R.drawable.day_1; //sun is always shining for default :)
            }
        } else {
            switch (cloudValue) {
                case 1:
                    return R.drawable.night_1;
                case 2:
                    return R.drawable.night_2;
                case 3:
                    return R.drawable.night_3;
                case 4:
                    return R.drawable.night_4;
                case 5:
                    return R.drawable.night_5;
                case 6:
                    return R.drawable.night_6;
                case 7:
                    return R.drawable.night_7;
                case 8:
                    return R.drawable.night_8;
                case 9:
                    return R.drawable.night_9;
                case 10:
                    return R.drawable.night_10;
                case 11:
                    return R.drawable.night_11;
                case 12:
                    return R.drawable.night_12;
                case 13:
                    return R.drawable.night_13;
                case 14:
                    return R.drawable.night_14;
                case 15:
                    return R.drawable.night_15;
                case 16:
                    return R.drawable.night_16;
                case 17:
                    return R.drawable.night_17;
                case 18:
                    return R.drawable.night_18;
                case 19:
                    return R.drawable.night_19;
                case 20:
                    return R.drawable.night_20;
                case 21:
                    return R.drawable.night_21;
                case 22:
                    return R.drawable.night_22;
                case 23:
                    return R.drawable.night_23;
                case 24:
                    return R.drawable.night_24;
                case 25:
                    return R.drawable.night_25;
                case 26:
                    return R.drawable.night_26;
                case 27:
                    return R.drawable.night_17;
                default:
                    return R.drawable.night_1; //sun is always shining for default :)
            }
        }
    }

    private String getWorkoutRecommendation(int cloudValue, double temperature, String time){
        String workoutRecommendation;
        if (cloudValue < 8 && temperature > 7 && Integer.parseInt(time) > 5 && Integer.parseInt(time) < 23)
            workoutRecommendation = "Perfect time for a run!";//extract to string resource!!
        else if (cloudValue < 8 && temperature < 8 && Integer.parseInt(time) > 5 && Integer.parseInt(time) < 23)
            workoutRecommendation = "Never to cold for a ride!";
        else if (cloudValue > 7 && Integer.parseInt(time) > 5 && Integer.parseInt(time) < 23)
           workoutRecommendation = "Good day for a pool swim!";
        else if (Integer.parseInt(time) < 6 || Integer.parseInt(time) > 22)
            workoutRecommendation = "Netflix or sleep! Work hard, rest harder!";
        else
            workoutRecommendation = "Bad weather is no excuse for not working out!";
        return workoutRecommendation;
    }

}


