package com.graduational.sensebox;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.graduational.sensebox.databaseClasses.DatabaseConnector;
import com.graduational.sensebox.jsonParsing.JSON_resolver;
import com.graduational.sensebox.jsonParsing.JsonToStringConverter;
import com.graduational.sensebox.navigationDrawer.NavDrawer;

public class CurrentConditionsActivity extends Activity implements DefinedValues {
    private String[] urlArray;
    private ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
    private String[] selectedItemStringArray = new String[SENSORS_COUNT];
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private JsonToStringConverter converter = new JsonToStringConverter();
    final CountDownLatch latch = new CountDownLatch(1); //wait for thread to complete 
	private JSONObject[] jsonArray = new JSONObject[SENSORS_COUNT];
	private TextView[] textViews;
	private TextView dateTimeTextView;
    private CharSequence mTitle;
    private NavDrawer navigationDrawer;
	
	private ActionBarDrawerToggle drawerToggle;
    private int position;
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> sensorValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_activity);
        
		navigationDrawer = new NavDrawer(this);
        drawerToggle = navigationDrawer.getDrawerToggle();
        Intent intent = getIntent();
		position = intent.getIntExtra("position", position);
        urlArray = intent.getStringArrayExtra("urls");
        
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		        for (int i = 0; i < SENSORS_COUNT; i++) {
		            selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
		            selectedItemStringArray = converter.converter(selectedItemObjectArray);
		        }
				latch.countDown();
			}
		}).start();
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
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
        try {
			resolver = new JSON_resolver();
		} catch (JSONException | ParseException e1) {
			e1.printStackTrace();
		}
        for(int i = 0; i < SENSORS_COUNT; i++) {   		
            try {
                resolver.setjObject(jsonArray[i]);
                resolver.resolve();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

      
        if (resolver != null) {
            date = resolver.getDate();
            sensorValue = resolver.getData();
        }
		

        
        textViews = new TextView[SENSORS_COUNT];
        Resources resources = getResources();
        for(int i = 0; i < SENSORS_COUNT; i++) {
        	int k = i + 1;
        	String idName = "textView" + k;
        	textViews[i] = (TextView) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
        }
        dateTimeTextView = (TextView) findViewById(R.id.textView8);      
        dateTimeTextView.setText("Current Weather in Knossos:\n" + date.get(0));
        
        for(int i = 0; i < SENSORS_COUNT; i++) {
        	textViews[i].append(sensorValue.get(i));
        }
	}
	   
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
  
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    	protected void onPostCreate(Bundle savedInstanceState) {
    		// TODO Auto-generated method stub
    		super.onPostCreate(savedInstanceState);
    		drawerToggle.syncState();
    	}
    
    @Override
    	public void onConfigurationChanged(Configuration newConfig) {
    		// TODO Auto-generated method stub
    		super.onConfigurationChanged(newConfig);
    		drawerToggle.onConfigurationChanged(newConfig);
    	}
    
    /* -------------- */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.refresh) {
            restartApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	private void restartApp() {
		ListView lv = navigationDrawer.getDrawerList();
		lv.performItemClick(lv.getAdapter().getView(default_CWR_flag, null, null), default_CWR_flag, lv.getAdapter().getItemId(default_CWR_flag));		
	}
}
