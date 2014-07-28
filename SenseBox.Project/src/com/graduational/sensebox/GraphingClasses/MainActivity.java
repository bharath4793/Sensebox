package com.graduational.sensebox.GraphingClasses;

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

import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.JSON_resolver;
import com.graduational.sensebox.R;
import com.graduational.sensebox.SplashScreen;
import com.graduational.sensebox.navigationDrawer.NavDrawer;


public class MainActivity extends Activity implements DefinedValues {
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;
    private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
    private LinearLayout[] layouts;
    private GraphDrawer graphDrawer;
    public Activity act = this;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);
        Intent intent = getIntent();
       // PrefetchData pd = (PrefetchData) intent.getParcelableExtra("jsonStrings");
      //  System.out.println(pd.getJsonStrings()[1]);
        String[] tempArray = intent.getStringArrayExtra("jsonStrings");
		//System.out.println("[DEBUG_6.1] " + tempArray[0]);
        jsonArray = new JSONObject[GRAPH_NUM];
        for(int i = 0; i < tempArray.length; i++) {
        	try {
				JSONObject obj = new JSONObject(tempArray[i]);
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
				graphDrawer.makeGraphs(layouts[i], graphLabels[i], this, jsonArray[i], new JSON_resolver());
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        NavDrawer navigationDrawer = new NavDrawer(this);
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
    


//    public void getGraphActivity(View view) {
//        Intent intent = new Intent(this, Graphs.class);
//        startActivity(intent);
//    }



    }


