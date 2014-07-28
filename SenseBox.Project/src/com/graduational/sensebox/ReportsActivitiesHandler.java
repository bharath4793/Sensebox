package com.graduational.sensebox;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduational.sensebox.DatabaseClasses.DatabaseConnector;
import com.graduational.sensebox.GraphingClasses.GraphDrawer;
import com.graduational.sensebox.navigationDrawer.NavDrawer;

public class ReportsActivitiesHandler extends AsyncTask<Void, Void, Void>
		implements DefinedValues {
	String minUrlArray[];
	String maxUrlArray[];
	private DatabaseConnector databaseConnector = new DatabaseConnector();
	private JsonToStringConverter converter = new JsonToStringConverter();
	final CountDownLatch latch = new CountDownLatch(1); // wait for thread to
														// complete
	private JSONObject[] maxJsonArray;
	private JSONObject[] minJsonArray;

	private ArrayList<JSONObject> minItemObjectArray = new ArrayList<>();
	private String[] minItemStringArray = new String[GRAPH_NUM];
	private ArrayList<JSONObject> maxItemObjectArray = new ArrayList<>();
	private String[] maxItemStringArray = new String[GRAPH_NUM];
	private JSON_resolver minResolver;
	private JSON_resolver maxResolver;
	ArrayList<Date> maxDate;
	ArrayList<String> maxSensorValue;
	ArrayList<Date> minDate;
	ArrayList<String> minSensorValue;
	private TextView[] textViews;
	private TextView dateTimeTextView;

	ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
	String[] urlArray;
	private Activity activity;
	private ProgressDialog mProgressDialog;

	public ReportsActivitiesHandler(String[] minUrlArray, String[] maxUrlArray, Activity activity) {
		this.minUrlArray = minUrlArray;
		this.maxUrlArray = maxUrlArray;
		this.activity = activity;
		mProgressDialog = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
        mProgressDialog.setMessage("Fetching Weather Data from Database...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		executeUrls(minUrlArray, maxUrlArray);
		jsonProccessing(maxItemStringArray, minItemStringArray);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		virtualizeData(maxResolver, minResolver);
		mProgressDialog.dismiss();
	}
	
	private void virtualizeData(JSON_resolver maxResolver, JSON_resolver minResolver) {	
		// textView.setText(data);
		if (maxResolver != null && minResolver != null) {
			maxDate = maxResolver.getDate();
			maxSensorValue = maxResolver.getTemp();

			minDate = minResolver.getDate();
			minSensorValue = minResolver.getTemp();
		}

		textViews = new TextView[GRAPH_NUM];
		Resources resources = activity.getResources();
		for (int i = 0; i < GRAPH_NUM; i++) {
			int k = i + 1;
			String idName = "textView" + k;
			textViews[i] = (TextView) activity.findViewById(resources
					.getIdentifier(idName, "id", activity.getPackageName()));
		}

		// dateTimeTextView = (TextView) findViewById(R.id.textView8);

		// dateTimeTextView.setText("Current Weather in Knossos:\n" +
		// date.get(0));

		for (int i = 0; i < GRAPH_NUM; i++) {
			textViews[i].append("\nMax: " + maxSensorValue.get(i) + "\n"
					+ "Min: " + minSensorValue.get(i));
		}
	}
	
	private void jsonProccessing(String[] maxItemStringArray, String[] minItemStringArray) {
		maxJsonArray = new JSONObject[GRAPH_NUM];
		minJsonArray = new JSONObject[GRAPH_NUM];
		for (int i = 0; i < maxItemStringArray.length; i++) {
			try {
				JSONObject maxObject = new JSONObject(maxItemStringArray[i]);
				maxJsonArray[i] = maxObject;

				JSONObject minObject = new JSONObject(minItemStringArray[i]);
				minJsonArray[i] = minObject;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		try {
			maxResolver = new JSON_resolver();
			minResolver = new JSON_resolver();
		} catch (JSONException | ParseException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < GRAPH_NUM; i++) {
			try {
				maxResolver.setjObject(maxJsonArray[i]);
				maxResolver.resolve();

				minResolver.setjObject(minJsonArray[i]);
				minResolver.resolve();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}
	
	private void executeUrls(String[] minUrlArray, String[] maxUrlArray) {

		for (int i = 0; i < GRAPH_NUM; i++) {
			minItemObjectArray.add(databaseConnector.getData(minUrlArray[i]));
			minItemStringArray = converter.converter(minItemObjectArray);

			maxItemObjectArray.add(databaseConnector.getData(maxUrlArray[i]));
			maxItemStringArray = converter.converter(maxItemObjectArray);
		}
		System.out.println("min Item string array " + minItemStringArray[1]);
		System.out.println("man Item string array " + maxItemStringArray[1]);

	}
}