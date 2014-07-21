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
import android.widget.TextView;

import com.graduational.sensebox.DatabaseClasses.DatabaseConnector;
import com.graduational.sensebox.navigationDrawer.NavDrawer;

public class DailyReport extends Activity implements DefinedValues{
	String minUrlArray[];
	String maxUrlArray[];
    private NavDrawer navigationDrawer;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private JsonToStringConverter converter = new JsonToStringConverter();
    final CountDownLatch latch = new CountDownLatch(1); //wait for thread to complete 
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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.min_max_activity);
	    
		navigationDrawer = new NavDrawer(this);
	    drawerToggle = navigationDrawer.getDrawerToggle();
	    Intent intent = getIntent();
	    minUrlArray = intent.getStringArrayExtra("minUrlArray");
	    maxUrlArray = intent.getStringArrayExtra("maxUrlArray");
	    System.out.println("min url array " + minUrlArray[0]);
	    System.out.println("maxn url array " + maxUrlArray[0]);
	    
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		        for (int i = 0; i < GRAPH_NUM; i++) {
		            minItemObjectArray.add(databaseConnector.getData(minUrlArray[i]));
		            minItemStringArray = converter.converter(minItemObjectArray);
		            
		            maxItemObjectArray.add(databaseConnector.getData(maxUrlArray[i]));
		            maxItemStringArray = converter.converter(maxItemObjectArray);
		        }
			    System.out.println("min Item string array " + minItemStringArray[1]);
			    System.out.println("man Item string array " + maxItemStringArray[1]);
				latch.countDown();
			}
		}).start();
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	       
		maxJsonArray = new JSONObject[GRAPH_NUM];
		minJsonArray = new JSONObject[GRAPH_NUM];
        for(int i = 0; i < maxItemStringArray.length; i++) {
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        for(int i = 0; i < GRAPH_NUM; i++) {   		
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

        
        //   textView.setText(data);
        if (maxResolver != null && minResolver != null) {
            maxDate = maxResolver.getDate();
            maxSensorValue = maxResolver.getTemp();
            
            minDate = minResolver.getDate();
            minSensorValue = minResolver.getTemp();
        }
        
        textViews = new TextView[GRAPH_NUM];
        Resources resources = getResources();
        for(int i = 0; i < GRAPH_NUM; i++) {
        	int k = i + 1;
        	String idName = "textView" + k;
        	textViews[i] = (TextView) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
        }
        
        //dateTimeTextView = (TextView) findViewById(R.id.textView8);
        
        //dateTimeTextView.setText("Current Weather in Knossos:\n" + date.get(0));
        
        for(int i = 0; i < GRAPH_NUM; i++) {
        	textViews[i].append("\nMax: " + maxSensorValue.get(i) + "\n"
        						+ "Min: " + minSensorValue.get(i));
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
		finish();
		Intent intent = new Intent(this, SplashScreen.class);
		startActivity(intent);
		
	}
	}

