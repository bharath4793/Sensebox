package com.graduational.sensebox.graphingClasses;

import java.text.ParseException;

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
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.R;
import com.graduational.sensebox.SpinnerActivity;
import com.graduational.sensebox.jsonParsing.JSON_resolver;
import com.graduational.sensebox.navigationDrawer.NavDrawer;
import com.graduational.sensebox.splashScreen.SplashScreen;


public class MainActivity extends Activity implements DefinedValues {
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private JSONObject[] jsonArray = new JSONObject[SENSORS_COUNT];
    private LinearLayout[] layouts;
    public Activity act = this;
    private SpinnerActivity spinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        Intent intent = getIntent();
        String[] tempArray = intent.getStringArrayExtra("jsonStrings");
        spinner = new SpinnerActivity(this);
        jsonArray = new JSONObject[SENSORS_COUNT];
        for(int i = 0; i < tempArray.length; i++) {
        	try {
				JSONObject obj = new JSONObject(tempArray[i]);
				jsonArray[i] = obj;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        layouts = graphLayouts();
        for(int i = 0; i < SENSORS_COUNT; i++) {
        	try {
        		new GraphDrawer(layouts[i], graphLabels[i], this, jsonArray[i], new JSON_resolver()).execute();
			} catch (JSONException | ParseException e) {
				e.printStackTrace();
			}
        }
        NavDrawer navigationDrawer = new NavDrawer(this);
        drawerToggle = navigationDrawer.getDrawerToggle();
    }
   
    private LinearLayout[] graphLayouts() {
    	LinearLayout[] graphSpots = new LinearLayout[SENSORS_COUNT];
    	Resources resources = getResources();
    	for (int i = 0; i < SENSORS_COUNT; i++) {
    		String idName = "graph" + i;
    		graphSpots[i] = (LinearLayout) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
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
    		super.onPostCreate(savedInstanceState);
    		drawerToggle.syncState();
    	}
    
    @Override
    	public void onConfigurationChanged(Configuration newConfig) {
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


