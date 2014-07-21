package com.graduational.sensebox.GraphingClasses;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.JSON_resolver;
import com.graduational.sensebox.JsonToStringConverter;
import com.graduational.sensebox.DatabaseClasses.DatabaseConnector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.LinearLayout;

public class GraphActivitiesHandler extends AsyncTask<Void, Void, Void> implements DefinedValues{
    private String[] selectedItemStringArray = new String[GRAPH_NUM];
    private JsonToStringConverter converter = new JsonToStringConverter();
	private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
	private GraphDrawer graphDrawer;
    private LinearLayout[] layouts;
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
	String[] urlArray;
	private Activity activity;
	private ProgressDialog mProgressDialog;
	
	public GraphActivitiesHandler(String[] urlArray, Activity activity) {
		this.urlArray = urlArray;
		this.activity = activity;
		
		mProgressDialog = new ProgressDialog(activity);
	}
	
	
	    @Override
	    protected void onPreExecute() {


	        mProgressDialog.setMessage("Please wait....");
	        mProgressDialog.setIndeterminate(true);
	        mProgressDialog.setCancelable(false);
	        mProgressDialog.show();

	    }

	@Override
	protected Void doInBackground(Void... params) {
        for (int i = 0; i < GRAPH_NUM; i++) {
            selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
            selectedItemStringArray = converter.converter(selectedItemObjectArray);
        }
    	
        
        jsonArray = new JSONObject[GRAPH_NUM];
        for(int i = 0; i < urlArray.length; i++) {
        	try {
				JSONObject obj = new JSONObject(selectedItemStringArray[i]);
				jsonArray[i] = obj;
				System.out.println("[DEBUG_5] ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {	
		super.onPostExecute(result);

        graphDrawer = new GraphDrawer();
        layouts = graphLayouts();
        for(int i = 0; i < GRAPH_NUM; i++) {
        	try {
				graphDrawer.makeGraphs(layouts[i], graphLabels[i], activity, jsonArray[i], new JSON_resolver());
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		 mProgressDialog.dismiss();
	}
	
	


	private LinearLayout[] graphLayouts() {
    	LinearLayout[] graphSpots = new LinearLayout[GRAPH_NUM];
    	Resources resources = activity.getResources();
    	for (int i = 0; i < GRAPH_NUM; i++) {
    		String idName = "graph" + i;
    		graphSpots[i] = (LinearLayout) activity.findViewById(resources.getIdentifier(idName, "id", activity.getPackageName()));
    		//System.out.println("[DEBUG_2] " + graphSpots[i].getId());
    	}
    	return graphSpots;
    }
	
}