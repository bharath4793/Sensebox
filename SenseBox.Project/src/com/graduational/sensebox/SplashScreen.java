package com.graduational.sensebox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import com.graduational.sensebox.DatabaseConnector;

public class SplashScreen extends Activity implements DefinedValues {
	private String[] defaultURLArray;
	public ArrayList<JSONObject> jsonArray = new ArrayList<>();
    private String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
    DatabaseConnector databaseConnector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
        defaultURLArray = makeURL(DEFAULT_FLAG, sensorsArray);
        //System.out.println("DEFAULT URL ARRAY 4 "  + defaultURLArray[3]);
		
        new PrefetchData(defaultURLArray, this).execute();
	}
    
    private String[] makeURL(int elementClicked, String[] sensorsArray) {
    	String[] urlArray = new String[GRAPH_NUM];
    	for(int i = 0; i < sensorsArray.length; i++) {
        	Builder builder = new BuildString();
    		urlArray[i] = builder.buildString(elementClicked, sensorsArray[i]);
    	}
    	//urlArray holds all the urls for all the sensors.
    	return urlArray;
    }





}
