package com.graduational.sensebox.GraphingClasses;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.R;
import com.graduational.sensebox.SplashScreen;
import com.graduational.sensebox.R.id;
import com.graduational.sensebox.R.layout;
import com.graduational.sensebox.R.menu;
import com.graduational.sensebox.navigationDrawer.NavDrawer;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
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

public class GraphActivity extends Activity implements DefinedValues{

	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;  
    private String[] urlArray;
    final CountDownLatch latch = new CountDownLatch(1); //wait for thread to complete 
    private NavDrawer navigationDrawer;
    Activity activity = this;
    private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        
        Intent intent = getIntent();
        urlArray = intent.getStringArrayExtra("urls");
		position = intent.getIntExtra("position", position);
        setTitle(intent.getStringExtra("title"));
		//System.out.println("[DEBUG_6.1] " + urlArray[0]);
        navigationDrawer = new NavDrawer(this);
        drawerToggle = navigationDrawer.getDrawerToggle();
        new GraphActivitiesHandler(urlArray, this).execute();
    
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

