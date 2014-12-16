package com.graduational.sensebox.resultsReportClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.graduational.sensebox.BuildString;
import com.graduational.sensebox.Builder;
import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.R;
import com.graduational.sensebox.SpinnerActivity;
import com.graduational.sensebox.R.id;
import com.graduational.sensebox.R.layout;
import com.graduational.sensebox.R.menu;
import com.graduational.sensebox.databaseClasses.GetDataFromDB;
import com.graduational.sensebox.navigationDrawer.DrawerItemClickListener;
import com.graduational.sensebox.navigationDrawer.NavDrawer;

public class ResultsActivity extends Activity implements DefinedValues {
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;  
    private String[] urlArray;
    private NavDrawer navigationDrawer;
    Activity activity = this;
    private int position;


	private SimpleDateFormat df;
	private DrawerItemClickListener clickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_activity);
		
        Intent intent = getIntent();
        urlArray = intent.getStringArrayExtra("urls");
		position = intent.getIntExtra("position", position);
        setTitle(intent.getStringExtra("title"));
        new SpinnerActivity(this);
        navigationDrawer = new NavDrawer(this);
        clickListener = navigationDrawer.getClickListener();
        drawerToggle = navigationDrawer.getDrawerToggle();
        new ResultsReportHandler(urlArray, this).execute();

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
        return true;
    }
    


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		Builder builder = new BuildString();
        Intent intent;
		switch (item.getItemId()) {
			case R.id.refresh:
				finish();
				builder.startStringBuilding(last24hours, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(this, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "24 Hours Report");
				startActivity(intent);
	            return true;
		}
		return true;
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
	    		super.onConfigurationChanged(newConfig);
	    		drawerToggle.onConfigurationChanged(newConfig);
	    	}
	    
	    /* -------------- */
	    

	    
}
