package com.graduational.sensebox.databaseClasses;

import java.util.List;

import org.json.JSONObject;

import android.os.Message;

public class DatabaseConnector {
	private static final int GRAPH_NUM = 7;
	List<Message> titles;
	private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];

	public DatabaseConnector() {

	}
	
	public JSONObject getData(String url) {
		GetDataFromDB db = new GetDataFromDB();
		return db.makeHttpRequest(url, "GET", null);
	}
	public JSONObject[] getJsonArray() {
		return jsonArray;
	}
}
