package com.graduational.sensebox;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

public class ReportsActivity extends Activity implements DefinedValues {
    private String[] urlArray;
    private ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
    private String[] selectedItemStringArray = new String[GRAPH_NUM];
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private JsonToStringConverter converter = new JsonToStringConverter();
    final CountDownLatch latch = new CountDownLatch(1); //wait for thread to complete 
	private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
	private TextView[] textViews;
	private TextView dateTimeTextView;
    
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> sensorValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_activity);
		
        Intent intent = getIntent();
        urlArray = intent.getStringArrayExtra("urls");
        
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		        for (int i = 0; i < GRAPH_NUM; i++) {
		            selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
		            selectedItemStringArray = converter.converter(selectedItemObjectArray);
		        }
				latch.countDown();
			}
		}).start();
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        
        jsonArray = new JSONObject[GRAPH_NUM];
        for(int i = 0; i < urlArray.length; i++) {
        	try {
				JSONObject obj = new JSONObject(selectedItemStringArray[i]);
				jsonArray[i] = obj;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        try {
			resolver = new JSON_resolver();
		} catch (JSONException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        for(int i = 0; i < GRAPH_NUM; i++) {   		
            try {
                resolver.setjObject(jsonArray[i]);
                resolver.resolve();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        
        //   textView.setText(data);
        if (resolver != null) {
            date = resolver.getDate();
            sensorValue = resolver.getTemp();
        }
		

        
        textViews = new TextView[GRAPH_NUM];
        Resources resources = getResources();
        for(int i = 0; i < GRAPH_NUM; i++) {
        	int k = i + 1;
        	String idName = "textView" + k;
        	textViews[i] = (TextView) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
        }
        
        dateTimeTextView = (TextView) findViewById(R.id.textView8);
        
        dateTimeTextView.setText("Current Weather in Knossos:\n" + date.get(0));
        
        for(int i = 0; i < GRAPH_NUM; i++) {
        	textViews[i].append(sensorValue.get(i));
        }
	}
}
