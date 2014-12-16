package com.graduational.sensebox.splashScreen;

import java.util.ArrayList;

import org.json.JSONObject;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.databaseClasses.DatabaseConnector;
import com.graduational.sensebox.graphingClasses.MainActivity;

import android.content.Intent;
import android.os.AsyncTask;


public class PrefetchData extends AsyncTask<Void, Void, Void> implements DefinedValues  {

	private static final int GRAPH_NUM = 7;

	public ArrayList<JSONObject> jsonArray = new ArrayList<>();
	public static String[] jsonStrings;

	private DatabaseConnector databaseConnector;
	private String[] urls;
	private SplashScreen splashScreen;

	PrefetchData(String[] urls, SplashScreen splashScreen) {
		this.urls = urls;
		this.splashScreen = splashScreen;
	}

	public String[] getJsonStrings() {
		return jsonStrings;
	}


	@Override
	protected Void doInBackground(Void... params) {
		for (int i = 0; i < GRAPH_NUM; i++) {
			makeHttpRequest(urls[i], i);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		jsonStrings = converter(jsonArray);
		Intent intent = new Intent(splashScreen, MainActivity.class);
		intent.putExtra("jsonStrings", jsonStrings);
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		splashScreen.startActivity(intent);
	}

	public String[] converter(ArrayList<JSONObject> jsonArray) {
		jsonStrings = new String[GRAPH_NUM];
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonStrings[i] = jsonArray.get(i).toString();
		}

		return jsonStrings;
	}

	private void makeHttpRequest(String httpRequest, int num) {
		databaseConnector = new DatabaseConnector();
		jsonArray.add(databaseConnector.getData(httpRequest));
	}
};
