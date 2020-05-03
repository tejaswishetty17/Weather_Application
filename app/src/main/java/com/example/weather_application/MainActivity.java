package com.example.weather_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String CITY = "Mumbai";
   /* String CITY = "524901,703448,2643743";*/
    String APIKEY = "9594b9b3f25899e7f519811429005c1c";


    TextView addressTxt,updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    List<WeatherDataModel> weatherDataModelArrayList = new ArrayList<WeatherDataModel>();
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        dataBase = new DataBase(this);
        new weatherTask().execute();
        /*new weatherTask().execute();*/
    }

    class weatherTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            /*response = HttpRequest.excuteGet("http://api.openweathermap.org/data/2.5/group?id=" + CITY + "&units=metric&appid=" + APIKEY);*/
            response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + APIKEY);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
           try {
               JSONObject jsonObject = new JSONObject(s);
               JSONObject main = jsonObject.getJSONObject("main");
               JSONObject sys = jsonObject.getJSONObject("sys");
               JSONObject wind = jsonObject.getJSONObject("wind");
               JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

               Long updateAt = jsonObject.getLong("dt");
               String updateAtText = "Updated at:"+ new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updateAt * 1000));
               String temp = main.getString("temp") + "\u2103";
               String tempMin = "Min Temp: " + main.getString("temp_min") + "\u2103";
               String tempMax = "Max Temp: " + main.getString("temp_max") + "\u2103";
               String pressure = main.getString("pressure");
               String humidity = main.getString("humidity");

               Long sunrise = sys.getLong("sunrise");
               Long sunset = sys.getLong("sunset");
               String windSpeed = wind.getString("speed");
               String weatherDescription = weather.getString("description");

               String address = jsonObject.getString("name") + ", " + sys.getString("country");

               String sunriseData = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000));
               String sunsetData = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000));

               try {
                   WeatherDataModel weatherDataModel = new WeatherDataModel();
                   weatherDataModel.setAddress(address);
                   weatherDataModel.setUpdateAtText(updateAtText);
                   weatherDataModel.setWeatherDescription(weatherDescription);
                   weatherDataModel.setTemp(temp);
                   weatherDataModel.setTempMin(tempMin);
                   weatherDataModel.setTempMax(tempMax);
                   weatherDataModel.setSunrise(sunriseData);
                   weatherDataModel.setSunset(sunsetData);
                   weatherDataModel.setWindSpeed(windSpeed);
                   weatherDataModel.setPressure(pressure);
                   weatherDataModel.setHumidity(humidity);
                   dataBase.addData(weatherDataModel);
                   Toast.makeText(MainActivity.this, "Data Inserted successfully", Toast.LENGTH_SHORT).show();
               }
               catch (Error e){
                   Log.d("Error", "Eroor is" + e);
               }
               weatherDataModelArrayList = dataBase.getWeatherData();

               for(int i = 0; i< weatherDataModelArrayList.size(); i++) {
                   Log.d("TAG", "data are" + weatherDataModelArrayList.get(i).getAddress());
               }
               addressTxt.setText(address);
               updated_atTxt.setText(updateAtText);
               statusTxt.setText(weatherDescription.toUpperCase());
               tempTxt.setText(temp);
               temp_minTxt.setText(tempMin);
               temp_maxTxt.setText(tempMax);
               sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
               sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
               windTxt.setText(windSpeed);
               pressureTxt.setText(pressure);
               humidityTxt.setText(humidity);

               findViewById(R.id.loader).setVisibility(View.GONE);
               findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
           }catch (JSONException e){
               findViewById(R.id.loader).setVisibility(View.GONE);
               findViewById(R.id.errorText).setVisibility(View.VISIBLE);
           }
        }
    }
}
