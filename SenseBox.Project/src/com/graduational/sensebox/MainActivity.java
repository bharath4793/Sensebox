package com.graduational.sensebox;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.jjoe64.graphview.*;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends Activity {
	private static final int DEFAULT_FLAG = 4;
	private static final int GRAPH_NUM = 7;
   // Activity activity = this;
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> temperature;
    Activity activity;
    GraphView graphView;
    GraphViewSeries graphSeries;
 //   private JSONObject jObject;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView drawerList;
    private String defaultRequest = "http://192.168.1.5/dynamicQueryExecutor.php?sensor=BMP_temp&flag=4";
    private String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
    private JSONObject[] jsonArray = new JSONObject[GRAPH_NUM];
    private LinearLayout[] layouts;
    private GraphDrawer graphDrawer;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);
        Intent intent = getIntent();

        String[] tempArray = intent.getStringArrayExtra("jsonStrings");
		System.out.println("[DEBUG_6.1] " + tempArray[0]);
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
//        try {
//			resolver = new JSON_resolver();
//		} catch (JSONException | ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        graphDrawer = new GraphDrawer();

//        new Thread(new Runnable() {
//            public void run() {
//
//            }
//        }, "httpRequestThread").start();

        String[] defaultURLArray = makeURL(DEFAULT_FLAG, sensorsArray);
        
        
        layouts = graphLayouts();
        for(int i = 0; i < GRAPH_NUM; i++) {
        	try {
				graphDrawer.makeGraphs(layouts[i], "DEMO", this, jsonArray[i], new JSON_resolver());
			} catch (JSONException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }



        String [] dummyArray = {"Last 2 Days", "Last Week", "Last Month", "Last Three Months"};
        mTitle = mDrawerTitle = getTitle();	
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,  R.layout.drawer_list_item, dummyArray));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        
        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		getActionBar().setTitle(mTitle);
        		invalidateOptionsMenu();
        	}
        	
            /** Called when a drawer has settled in a completely open state. */
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		getActionBar().setTitle(mDrawerTitle);
        		invalidateOptionsMenu();
        	}
        };
        drawerLayout.setDrawerListener(drawerToggle);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }
    
    private LinearLayout[] graphLayouts() {
    	LinearLayout[] graphSpots = new LinearLayout[GRAPH_NUM];
    	Resources resources = getResources();
    	for (int i = 0; i < GRAPH_NUM; i++) {
    		String idName = "graph" + i;
    		graphSpots[i] = (LinearLayout) findViewById(resources.getIdentifier(idName, "id", getPackageName()));
    		System.out.println("[DEBUG_2] " + graphSpots[i].getId());
    	}
    	return graphSpots;
    }

    
    private String[] makeURL(int elementClicked, String[] sensorsArray) {
    	String[] urlArray = new String[GRAPH_NUM];
    	for(int i = 0; i < sensorsArray.length; i++) {
        	Builder builder = new BuildString();
    		urlArray[i] = builder.buildString(elementClicked, sensorsArray[i]);
    	}
    	//urlArray holds all the urls for all the sensors.
    	return urlArray;
    }

    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			System.out.println("--> " + id + ", " + position);
			selectItem(position);	
		}
    }

    private void selectItem(final int position) {	

        new Thread(new Runnable() {
        	
        	DatabaseConnector databaseConnector = new DatabaseConnector();;
            ArrayList<JSONObject> selectedItemObjectArray = new ArrayList<>();
            GraphDrawer drawer = new GraphDrawer();
            
            public void run() {
                String[] urlArray = makeURL(position, sensorsArray);
                for (int i = 0; i < GRAPH_NUM; i++) {
                    selectedItemObjectArray.add(databaseConnector.getData(urlArray[i]));
                }

                
                
                layouts = graphLayouts();
                for(int i = 0; i < GRAPH_NUM; i++) {
                	// makeGraphs(layouts[i], jsonArray[i]);
                }
            }
        }, "selectItemThread").start();
    	

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
    


//    public void getGraphActivity(View view) {
//        Intent intent = new Intent(this, Graphs.class);
//        startActivity(intent);
//    }



    }


