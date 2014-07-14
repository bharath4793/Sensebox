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

public class SplashScreen extends Activity {
	private static final int GRAPH_NUM = 7;
	private static final int DEFAULT_FLAG = 4;
	private String[] defaultURLArray;
	public ArrayList<JSONObject> jsonArray = new ArrayList<>();
//	public JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
	private String[] jsonStrings;
    private String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
    DatabaseConnector databaseConnector;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
        defaultURLArray = makeURL(DEFAULT_FLAG, sensorsArray);
        System.out.println("DEFAULT URL ARRAY 4 "  + defaultURLArray[3]);
		
        new prefetchData(defaultURLArray).execute();
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

    
    private class prefetchData extends AsyncTask<Void, Void, Void> {
    	private String [] urls;
    	
    	public prefetchData(String[] urls) {
			this.urls = urls;
		}

		@Override
		protected Void doInBackground(Void... params) {
		    for (int i = 0; i < GRAPH_NUM; i++) {
	            makeHttpRequest(urls[i], i);
	            System.out.println("[DEBUG 4] " + urls[i] + ", " + i);
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			jsonStrings = converter(jsonArray);
			Intent intent = new Intent(SplashScreen.this, MainActivity.class);
			intent.putExtra("jsonStrings", jsonStrings);
			System.out.println("[DEBUG_6] " + jsonStrings[4]);
			//Keep splash screen on for three more seconds.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startActivity(intent);		
		}
		

        public String[] converter(ArrayList<JSONObject> jsonArray) {
        	jsonStrings = new String[GRAPH_NUM];
        	for(int i = 0; i < jsonArray.size(); i++) {
        		jsonStrings[i] = jsonArray.get(i).toString(); 
        	}
        	System.out.println("[DEBUG 3] " + jsonStrings[2]);
        	return jsonStrings;
        }
		
	    
	    private void makeHttpRequest(String httpRequest, int num) {
	        System.out.println("[DEBUG_1] " + num);
	        databaseConnector = new DatabaseConnector();
	        
	        jsonArray.add(databaseConnector.getData(httpRequest));
	    }


		};
    public void setJsonStrings(String[] strings) {
    	jsonStrings = strings;
    }


}
