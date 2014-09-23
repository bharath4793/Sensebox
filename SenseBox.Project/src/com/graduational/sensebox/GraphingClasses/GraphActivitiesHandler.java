package com.graduational.sensebox.graphingClasses;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.databaseClasses.DatabaseConnector;
import com.graduational.sensebox.jsonParsing.JSON_resolver;
import com.graduational.sensebox.jsonParsing.JsonToStringConverter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.LinearLayout;

public class GraphActivitiesHandler extends AsyncTask<Void, Void, Void> implements DefinedValues{
    private String[] selectedItemStringArray = new String[SENSORS_COUNT];
    private JsonToStringConverter converter = new JsonToStringConverter();
	private JSONObject[] jsonArray = new JSONObject[SENSORS_COUNT];
    private LinearLayout[] layouts;
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
	private String[] urlArray;
	private Activity activity;
	private ProgressDialog mProgressDialog;
	
	public GraphActivitiesHandler(String[] urlArray, Activity activity) {
		this.urlArray = urlArray;
		this.activity = activity;
		
		mProgressDialog = new ProgressDialog(activity);
	}
	
	
	    @Override
	    protected void onPreExecute() {
	        mProgressDialog.setMessage("Fetching Weather Data from Database...");
	        mProgressDialog.setIndeterminate(true);
	        mProgressDialog.setCancelable(false);
	        mProgressDialog.show();
	    }

	@Override
	protected Void doInBackground(Void... params) {
        for (int i = 0; i < SENSORS_COUNT; i++) {
            selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
            selectedItemStringArray = converter.converter(selectedItemObjectArray);
        }
    	
        
        jsonArray = new JSONObject[SENSORS_COUNT];
        for(int i = 0; i < urlArray.length; i++) {
        	try {
				JSONObject obj = new JSONObject(selectedItemStringArray[i]);
				jsonArray[i] = obj;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {	
		super.onPostExecute(result);
        layouts = graphLayouts();
        for(int i = 0; i < SENSORS_COUNT; i++) {
        	try {
        		new GraphDrawer(layouts[i], graphLabels[i], activity, jsonArray[i], new JSON_resolver()).execute();
				
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		 mProgressDialog.dismiss();
	}

	private LinearLayout[] graphLayouts() {
    	LinearLayout[] graphSpots = new LinearLayout[SENSORS_COUNT];
    	Resources resources = activity.getResources();
    	for (int i = 0; i < SENSORS_COUNT; i++) {
    		String idName = "graph" + i;
    		graphSpots[i] = (LinearLayout) activity.findViewById(resources.getIdentifier(idName, "id", activity.getPackageName()));
    	}
    	return graphSpots;
    }
	
}