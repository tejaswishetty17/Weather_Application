package com.example.weather_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    Context mContext;
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "WeatherData";
    public static final String TABLE = "weatherDetails";
    public static final String KEY_ID = "id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_UPDATE_TEXT = "updateText";
    public static final String KEY_WEATHER_DESCRIPTION = "weatherDescription";
    public static final String KEY_TEMP = "temperature";
    public static final String KEY_TEMP_MIN = "tempMin";
    public static final String KEY_TEMP_MAX = "tempMax";
    public static final String KEY_SUNRISE = "sunrise";
    public static final String KEY_SUNSET = "sunset";
    public static final String KEY_WIND_SPEED = "windSpeed";
    public static final String KEY_PRESSURE = "pressure";
    public static final String KEY_HUMIDITY = "humidity";


    private static final String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE + "" + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            KEY_ADDRESS + " TEXT NOT NULL,"+
            KEY_UPDATE_TEXT + " TEXT NOT NULL,"+
            KEY_WEATHER_DESCRIPTION + " TEXT NOT NULL,"+
            KEY_TEMP + " TEXT NOT NULL,"+
            KEY_TEMP_MIN + " TEXT NOT NULL,"+
            KEY_TEMP_MAX + " TEXT NOT NULL,"+
            KEY_SUNRISE + " TEXT NOT NULL,"+
            KEY_SUNSET + " TEXT NOT NULL,"+
            KEY_WIND_SPEED + " TEXT NOT NULL,"+
            KEY_PRESSURE + " TEXT NOT NULL,"+
            KEY_HUMIDITY + " TEXT NOT NULL"+
            ")";


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

  /*  public void addData(String address, String updateAtText, String weatherDescription, String temp,
                        String tempMin, String tempMax, String sunrise, String sunset, String windSpeed, String pressure, String humidity ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS, address);
        values.put(KEY_UPDATE_TEXT, updateAtText);
        values.put(KEY_WEATHER_DESCRIPTION, weatherDescription);
        values.put(KEY_TEMP, temp);
        values.put(KEY_TEMP_MIN, tempMin);
        values.put(KEY_TEMP_MAX, tempMax);
        values.put(KEY_SUNRISE, sunrise);
        values.put(KEY_SUNSET, sunset);
        values.put(KEY_WIND_SPEED, windSpeed);
        values.put(KEY_PRESSURE, pressure);
        values.put(KEY_HUMIDITY, humidity);
        db.insert(TABLE, null, values);
        db.close();
    }*/


  public void addData(WeatherDataModel weatherDataModel){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(KEY_ADDRESS, weatherDataModel.getAddress());
      values.put(KEY_UPDATE_TEXT, weatherDataModel.getUpdateAtText());
      values.put(KEY_WEATHER_DESCRIPTION, weatherDataModel.getWeatherDescription());
      values.put(KEY_TEMP, weatherDataModel.getTemp());
      values.put(KEY_TEMP_MIN, weatherDataModel.getTempMin());
      values.put(KEY_TEMP_MAX, weatherDataModel.getTempMax());
      values.put(KEY_SUNRISE, weatherDataModel.getSunrise());
      values.put(KEY_SUNSET, weatherDataModel.getSunset());
      values.put(KEY_WIND_SPEED, weatherDataModel.getWindSpeed());
      values.put(KEY_PRESSURE, weatherDataModel.getPressure());
      values.put(KEY_HUMIDITY, weatherDataModel.getHumidity());
      db.insert(TABLE, null, values);
      db.close();
  }

   /* public Cursor getWeatherData(){
        String selectQuery = "SELECT * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null, null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("KEY_ADDRESS")));
            cursor.moveToNext();
        }
        return cursor;
    }*/
   public List<WeatherDataModel> getWeatherData(){
       String selectQuery = "SELECT * FROM " + TABLE;
       /*String selectQuery = "SELECT TOP(5) FROM " + TABLE +" ORDER BY "+ KEY_ID + " DESC";*/
       SQLiteDatabase db = this.getReadableDatabase();

       Cursor cursor = db.rawQuery(selectQuery,null,null);
       List<WeatherDataModel> weatherModelArrayList = new ArrayList();
       if(cursor.moveToFirst()){
           do {
               WeatherDataModel wdm = new WeatherDataModel();
               wdm.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
               wdm.setUpdateAtText(cursor.getString(cursor.getColumnIndex(KEY_UPDATE_TEXT)));
               wdm.setWeatherDescription(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_DESCRIPTION)));
               wdm.setTemp(cursor.getString(cursor.getColumnIndex(KEY_TEMP)));
               wdm.setTempMin(cursor.getString(cursor.getColumnIndex(KEY_TEMP_MIN)));
               wdm.setTempMax(cursor.getString(cursor.getColumnIndex(KEY_TEMP_MAX)));
               wdm.setSunrise(cursor.getString(cursor.getColumnIndex(KEY_SUNRISE)));
               wdm.setSunset(cursor.getString(cursor.getColumnIndex(KEY_SUNSET)));
               wdm.setWindSpeed(cursor.getString(cursor.getColumnIndex(KEY_WIND_SPEED)));
               wdm.setPressure(cursor.getString(cursor.getColumnIndex(KEY_PRESSURE)));
               wdm.setHumidity(cursor.getString(cursor.getColumnIndex(KEY_HUMIDITY)));
               weatherModelArrayList.add(wdm);

           } while (cursor.moveToNext());

       }
       cursor.close();
       db.close();

       return weatherModelArrayList;
   }

    public void updateWeatherdata(WeatherDataModel weatherDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ADDRESS, weatherDataModel.getAddress());
        contentValues.put(KEY_UPDATE_TEXT, weatherDataModel.getUpdateAtText());
        contentValues.put(KEY_WEATHER_DESCRIPTION, weatherDataModel.getWeatherDescription());
        contentValues.put(KEY_TEMP, weatherDataModel.getTemp());
        contentValues.put(KEY_TEMP_MIN, weatherDataModel.getTempMin());
        contentValues.put(KEY_TEMP_MAX, weatherDataModel.getTempMax());
        contentValues.put(KEY_SUNRISE, weatherDataModel.getSunrise());
        contentValues.put(KEY_SUNSET, weatherDataModel.getSunset());
        contentValues.put(KEY_WIND_SPEED, weatherDataModel.getWindSpeed());
        contentValues.put(KEY_PRESSURE, weatherDataModel.getPressure());
        contentValues.put(KEY_HUMIDITY, weatherDataModel.getHumidity());

        db.update(TABLE, contentValues, KEY_ID + " = ?", new String[]{"" + weatherDataModel.getId()});

        db.close();
    }


    public void deleteweatherdata() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }

}
