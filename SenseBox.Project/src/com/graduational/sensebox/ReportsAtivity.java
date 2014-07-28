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

import com.graduational.sensebox.DatabaseClasses.DatabaseConnector;
import com.graduational.sensebox.GraphingClasses.GraphActivity;
import com.graduational.sensebox.navigationDrawer.NavDrawer;

public class ReportsAtivity extends Activity implements DefinedValues{

    private NavDrawer navigationDrawer;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private String minUrlArray[];
    private String maxUrlArray[];
    private int position;
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
		position = intent.getIntExtra("position", position);
		System.out.println("min url array " + minUrlArray[0]);
		System.out.println("maxn url array " + maxUrlArray[0]);
		
	    
	    new ReportsActivitiesHandler(minUrlArray, maxUrlArray, this).execute();
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
		lv.performItemClick(lv.getAdapter().getView(position, null, null), position, lv.getAdapter().getItemId(position));		
	}
	}

