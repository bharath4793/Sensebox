package com.graduational.sensebox;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TwoDaysActivity extends Activity {
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView drawerList;
    
    private static final int GRAPH_NUM = 7;
	private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
	private GraphDrawer graphDrawer;
    private LinearLayout[] layouts;
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
    private String[] selectedItemStringArray = new String[GRAPH_NUM];
    private JsonToStringConverter converter = new JsonToStringConverter();
    private String[] urlArray;
    final CountDownLatch latch = new CountDownLatch(1); //wait for thread to complete 
    private NavDrawer navigationDrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_days_activity);
        
        Intent intent = getIntent();
        urlArray = intent.getStringArrayExtra("urls");
		System.out.println("[DEBUG_6.1] " + urlArray[0]);
		
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
				System.out.println("[DEBUG_5] ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        
        graphDrawer = new GraphDrawer();
        
        
        layouts = graphLayouts();
        for(int i = 0; i < GRAPH_NUM; i++) {
        	try {
				graphDrawer.makeGraphs(layouts[i], "DEMO", this, jsonArray[i], new JSON_resolver());
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        navigationDrawer = new NavDrawer(this);
        drawerToggle = navigationDrawer.getDrawerToggle();
        
	}


	private LinearLayout[] graphLayouts() {
    	LinearLayout[] graphSpots = new LinearLayout[GRAPH_NUM];
    	Resources resources = getResources();
    	for (int i = 0; i < GRAPH_NUM; i++) {
    		String idName = "graph" + i;
    		graphSpots[i] = (LinearLayout) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
    		//System.out.println("[DEBUG_2] " + graphSpots[i].getId());
    	}
    	return graphSpots;
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
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        // The action bar home/up action should open or close the drawer.
	        // ActionBarDrawerToggle will take care of this.
	        if (drawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
    

}

