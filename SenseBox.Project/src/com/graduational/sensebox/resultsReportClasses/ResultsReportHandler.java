package com.graduational.sensebox.resultsReportClasses;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.R;
import com.graduational.sensebox.databaseClasses.DatabaseConnector;
import com.graduational.sensebox.jsonParsing.JSON_resolver;
import com.graduational.sensebox.jsonParsing.JsonToStringConverter;

public class ResultsReportHandler extends AsyncTask<Void, Void, Void> implements DefinedValues{
    private String[] selectedItemStringArray = new String[GRAPH_NUM];
    private JsonToStringConverter converter = new JsonToStringConverter();
	private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private JSON_resolver resolver = null;
    private ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
    private ArrayList<Date> date;
    private TextView textView;
    private String[] urlArray;
	private Activity activity;
	private ProgressDialog mProgressDialog;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private ArrayList<ArrayList<String>> sensorsData = new ArrayList<ArrayList<String>>();
	private SimpleDateFormat dateFormater;
	
	public ResultsReportHandler(String[] urlArray, Activity activity) {
		this.urlArray = urlArray;
		this.activity = activity;
        
		tableLayout = (TableLayout) activity.findViewById(R.id.maintable);
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
        jsonArray = new JSONObject[GRAPH_NUM];
        for(int i = 0; i < urlArray.length; i++) {
        	try {
                selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
                selectedItemStringArray = converter.converter(selectedItemObjectArray);
    			resolver = new JSON_resolver();
				JSONObject obj = new JSONObject(selectedItemStringArray[i]);
				jsonArray[i] = obj;
				resolver.setjObject(obj);
				resolver.resolve();
				if (i == 0) {
					date = resolver.getDate();
				}
				sensorsData.add(resolver.getTemp());
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}
        }
        return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {	
		tableRow = new TableRow(activity);
    	for(int i = 0; i < resultsReportLabels.length; i++) {
    		addHeader(resultsReportLabels[i], i);
    	}
		// Add the TableRow to the TableLayout
		tableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		addData(sensorsData);    	
		mProgressDialog.dismiss();
	}
	
	void addHeader(String title, int i) {
		/** Create a TableRow dynamically **/
		LinearLayout.LayoutParams params;
		LinearLayout Ll;
		/** Creating a TextView to add to the row **/
		textView = new TextView(activity);
		textView.setText(title);
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setPadding(2, 2, 2, 2);
		textView.setBackgroundColor(Color.RED);
		Ll = new LinearLayout(activity);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(2, 2, 2, 2);
		Ll.addView(textView, params);
		tableRow.addView((View) Ll); // Adding textView to tablerow.
	}

	
	public void addData(ArrayList<ArrayList<String>> sensorsData) {
		dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < sensorsData.get(0).size(); i++) {
				/** Create a TableRow dynamically **/
				tableRow = new TableRow(activity);
				LinearLayout.LayoutParams params;
				LinearLayout Ll;
				TextView dateView = new TextView(activity);
				dateView.setText(dateFormater.format(date.get(i)));
				dateView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				dateView.setPadding(2, 2, 2, 2);
				dateView.setBackgroundColor(Color.GRAY);
				Ll = new LinearLayout(activity);
				params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(2, 2, 2, 2);
				Ll.addView(dateView, params);
				tableRow.addView((View) Ll); // Adding tv to tablerow.

				for (int j = 0; j < 7; j++) {
						ArrayList<String> temp = sensorsData.get(j);
						TextView dataView = new TextView(activity);
						dataView.setText(temp.get(i));
						dataView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
						dataView.setPadding(2, 2, 2, 2);
						dataView.setBackgroundColor(Color.GRAY);
						Ll = new LinearLayout(activity);
						params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						params.setMargins(2, 2, 2, 2);
						Ll.addView(dataView, params);
						tableRow.addView((View) Ll); // Adding tv to tablerow.
				}
				tableLayout.addView(tableRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
		}
	}
	

