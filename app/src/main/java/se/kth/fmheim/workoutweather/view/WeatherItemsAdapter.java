package se.kth.fmheim.workoutweather.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.kth.fmheim.workoutweather.R;
import se.kth.fmheim.workoutweather.data.Weather;

public class WeatherItemsAdapter extends RecyclerView.Adapter<WeatherItemsAdapter.WeatherItemsViewHolder> {
    private ArrayList<WeatherItem> mWeatherItemList;
   private List<Weather> mWeatherData = new ArrayList<>();

    public static class WeatherItemsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mWeatherSymbol;
        public TextView mTextDate;
        public TextView mTextTemperature;
        public TextView mTextWorkout;


        public WeatherItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            /*
            get references to View-objects
            */
            mWeatherSymbol = itemView.findViewById(R.id.imageView_weatherSymbol);
            mTextDate = itemView.findViewById(R.id.textView_Date);
            mTextTemperature = itemView.findViewById(R.id.textView_temperature);
            mTextWorkout = itemView.findViewById(R.id.textView_workOut);
        }
    }

    /*
    constructor for WeatherItemsAdapter class
    */
    public WeatherItemsAdapter(ArrayList<WeatherItem> weatherItemsList) {
        /*
        When adapter is created List of WeatherItems is passed to member variable mWeatherItemList
         */
        mWeatherItemList = weatherItemsList;
    }
    public WeatherItemsAdapter() {
        /*
        When adapter empty constructor
         */

    }

    @NonNull
    @Override

    public WeatherItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        pass weather_item layout to adapter
        returning view holder
         */
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        return new WeatherItemsViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull WeatherItemsViewHolder holder, int position) {
        WeatherItem currentItem = mWeatherItemList.get(position); //Item of specific position
        holder.mWeatherSymbol.setImageResource(currentItem.getWeatherSymbol());
        holder.mTextDate.setText(currentItem.getTextDate());
        holder.mTextTemperature.setText(currentItem.getTextTemperature());
        holder.mTextWorkout.setText(currentItem.getTextWorkout());
    }
    public void setWeatherData(List<Weather> weatherData){
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mWeatherItemList.size(); //set number of items
    }
}
