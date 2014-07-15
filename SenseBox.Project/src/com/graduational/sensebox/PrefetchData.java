package com.graduational.sensebox;

import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


public class PrefetchData extends AsyncTask<Void, Void, Void>  implements Parcelable {
	
	private static final int GRAPH_NUM = 7;

	public ArrayList<JSONObject> jsonArray = new ArrayList<>();
//	public JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
	public static String[] jsonStrings;

    DatabaseConnector databaseConnector;
	private String [] urls;
	private SplashScreen splashScreen;
	
	PrefetchData(String[] urls, SplashScreen splashScreen) {
		this.urls = urls;
		this.splashScreen = splashScreen;
	}
	
	public String[] getJsonStrings() {
		return jsonStrings;
	}

	public PrefetchData(Parcel source) {
		source.readStringArray(jsonStrings);
	}

	@Override
	protected Void doInBackground(Void... params) {
	    for (int i = 0; i < GRAPH_NUM; i++) {
            makeHttpRequest(urls[i], i);
            //System.out.println("[DEBUG 4] " + urls[i] + ", " + i);
        }
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		jsonStrings = converter(jsonArray);
		Intent intent = new Intent(splashScreen, MainActivity.class);
		intent.putExtra("jsonStrings", jsonStrings);
	//	Bundle mBundle = new Bundle();
		//mBundle.putP
	//	System.out.println("[DEBUG_6] " + jsonStrings[4]);
		//Keep splash screen on for three more seconds.
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		splashScreen.startActivity(intent);		
	}
	

    public String[] converter(ArrayList<JSONObject> jsonArray) {
    	jsonStrings = new String[GRAPH_NUM];
    	for(int i = 0; i < jsonArray.size(); i++) {
    		jsonStrings[i] = jsonArray.get(i).toString(); 
    	}
    //	System.out.println("[DEBUG 3] " + jsonStrings[2]);
    	return jsonStrings;
    }
	
    
    private void makeHttpRequest(String httpRequest, int num) {
      //  System.out.println("[DEBUG_1] " + num);
        databaseConnector = new DatabaseConnector();
        
        jsonArray.add(databaseConnector.getData(httpRequest));
    }

	@Override
	public int describeContents() {
		return hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(jsonStrings);
		
	}
	
	public static final Parcelable.Creator<PrefetchData> CREATOR = new Creator<PrefetchData>() {
		
		@Override
		public PrefetchData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new PrefetchData[size];
		}
		
		@Override
		public PrefetchData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new PrefetchData(source);
		}
	};
};


