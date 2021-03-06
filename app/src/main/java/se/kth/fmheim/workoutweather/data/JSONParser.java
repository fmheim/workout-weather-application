package se.kth.fmheim.workoutweather.data;
/*
Class to parse data fetched from download
    - setting coordinates from city information download
    - setting weather data from weather download
 */

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.kth.fmheim.workoutweather.R;

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
            TIME_Series = "timeSeries",
            GEOMETRY = "geometry",
            COORDINATES = "coordinates",
            LONGITUDE = "lon",
            LATITUDE = "lat",
            PLACE = "place";
    private String mCityName;


    public String[] parseToCoordinates(JSONArray root) throws JSONException {
        String[] coordinates = new String[2];
        JSONObject cityInfo = root.getJSONObject(0);
        mCityName = cityInfo.getString(PLACE);
        Double longitude = cityInfo.getDouble(LONGITUDE);
        Double latitude = cityInfo.getDouble(LATITUDE);
        coordinates[0] = String.valueOf(longitude);
        coordinates[1] = String.valueOf(latitude);

        if (coordinates[0].length() > 6)
            coordinates[0] = coordinates[0].substring(0, 6);
        if (coordinates[1].length() > 6)
            coordinates[1] = coordinates[1].substring(0, 6);

        Log.d(LOG_TAG, coordinates[0] + " trimmed??");
        return coordinates;
    }

    public List<Weather> parseToWeather(JSONObject root) throws JSONException, ParseException {
        /*
            Parsing JASON-data into weather objects and returning array of all Weather data for next 10 days
        */
        String approvedTime = root.getString(APPROVED_TIME);
        String referenceTime = root.getString(REFERENCE_TIME);
        JSONArray timeSeries = root.getJSONArray(TIME_Series);
        JSONObject geometry = root.getJSONObject(GEOMETRY);
        JSONArray coordinatesArr = geometry.getJSONArray(COORDINATES);

        List<Weather> weatherData = new ArrayList<>(); //for each time entry new weather object
        Log.d(LOG_TAG, "parser initialized ");
        for (int i = 0; i < timeSeries.length(); i++) {
            /*
            looping through packages of weather data for provided times
            */
            JSONObject parametersAtTime = timeSeries.getJSONObject(i);
            String validTime = parametersAtTime.getString(VALID_TIME);
            String coordinates = getCoordinates(coordinatesArr.getJSONArray(0).getDouble(0),
                    coordinatesArr.getJSONArray(0).getDouble(1));
            JSONArray parameters = parametersAtTime.getJSONArray(PARAMETERS); //one set of parameters for a valid time
            Weather weatherAtTime = new Weather();
            weatherData.add(weatherAtTime);
            weatherAtTime.setDate(getDate(validTime));
            weatherAtTime.setTime(getTime(validTime));
            weatherAtTime.setApprovedTime(approvedTime);
            weatherAtTime.setReferenceTime(referenceTime);
            weatherAtTime.setCoordinates(coordinates);
            weatherAtTime.setCityName(mCityName);
            for (int j = 0; j < parameters.length(); j++) {
            /*
            looping through parameters and parsing the important ones to weather object
            */
                JSONObject parameter = parameters.getJSONObject(j); //one single parameter in set of parameters
                JSONArray values = parameter.getJSONArray(VALUES);  //one set of Values for a valid time

                //Log.d(LOG_TAG, Integer.toString(i) + Integer.toString(j));
                if (TEMPERATURE.equals(parameter.getString(NAME))) {
                    weatherAtTime.setTemperature(values.getDouble(0));//
                    Log.d(LOG_TAG, "Temp. assigned: " + Double.toString(values.getDouble(0)));
                }
                if (CLOUD_COVERAGE.equals(parameter.getString(NAME))) {
                    weatherAtTime.setClouds(getCloud(values.getInt(0)));
                    Log.d(LOG_TAG, "Cloud status assigned: " + Double.toString(values.getInt(0)));
                    weatherAtTime.setSymbol(getSymbol(values.getInt(0), weatherAtTime.getTime()));
                    weatherAtTime.setWorkoutRecommendation(getWorkoutRecommendation(values.getInt(0), weatherAtTime.getTemperature(), weatherAtTime.getTime()));
                }
            }

        }
        return weatherData;
    }

    private String getDate(String validTime) throws ParseException {
        String oldFormat = "yyyy-MM-dd";
        String newFormat = "EEEE\nMMM d";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdFormat = new SimpleDateFormat(oldFormat);
        Date validDate = sdFormat.parse(validTime.substring(0, 10));
        sdFormat.applyPattern(newFormat);
        return sdFormat.format(validDate);
    }

    private String getTime(String validTime) {
        return validTime.substring(11, 16);
    }

    private String getCoordinates(Double longitude, Double latitude) {
        return longitude + ", " + latitude;
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
        boolean isDay = Integer.parseInt(time.substring(0, 2)) > 5
                && Integer.parseInt(time.substring(0, 2)) < 18;
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
                    return R.drawable.night_27;
                default:
                    return R.drawable.night_1; //sun is always shining for default :)
            }
        }
    }

    private String getWorkoutRecommendation(int cloudValue, double temperature, String fullTime) {
        String workoutRecommendation;
        int time = Integer.parseInt(fullTime.substring(0, 2));
        if (cloudValue == 1 && time > 5 && time < 19)
            workoutRecommendation = "Sun's out, guns out!";
        else if (cloudValue < 8 && temperature > 7 && time > 5 && time < 21)
            workoutRecommendation = "Good time for a ride!";//extract to string resource!!
        else if (cloudValue < 8 && temperature < 8 && time > 5 && time < 21)
            workoutRecommendation = "Never too cold for a run!";
        else if (cloudValue > 7 && cloudValue < 11 && time > 5 && time < 21)
            workoutRecommendation = "Good time for a pool swim!";
        else if (cloudValue > 10 && time > 5 && time < 21)
            workoutRecommendation = "Good time for a home workout!";
        else if (time < 6 || time > 20)
            workoutRecommendation = "Work hard, rest harder!";
        else
            workoutRecommendation = "Always a good idea to work out";
        return workoutRecommendation;
    }
}


