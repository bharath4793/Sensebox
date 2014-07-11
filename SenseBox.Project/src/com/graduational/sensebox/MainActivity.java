package com.graduational.sensebox;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MainActivity extends Activity {
   // Activity activity = this;
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> temperature;
    Activity activity;
    GraphView graphView;
    GraphViewSeries graphSeries;
    private JSONObject jObject;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);

        AsyncTask<String, Void, Void> db = null;
        try {
            db = new db_conn(this);
            db.execute("http://192.168.1.5/dynamicQueryExecutor.php?sensor=BMP_temp").get(); //jObject gets a value
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        try {
            resolver = new JSON_resolver();
            resolver.setjObject(jObject);
            resolver.resolve();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //   textView.setText(data);
        if (resolver != null) {
            date = resolver.getDate();
            temperature = resolver.getTemp();
        }

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[date.size()];
        for (int i = 0; i < date.size(); i++) {
            data[i] = new GraphView.GraphViewData(5.1, Double.parseDouble(temperature.get(i)));
        }


        for (int i = 0; i < data.length; i++) {
            long now = date.get(i).getTime();
            System.out.println(now);
            data[i] = new GraphView.GraphViewData(now , Double.parseDouble(temperature.get(i)));
           // System.out.println(now + (i * 60 * 60 * 24 * 1000));
        }

        graphSeries = new GraphViewSeries(data);
        graphView = new LineGraphView(this, "Demo");
        ((LineGraphView) graphView).setDrawBackground(true);

        graphView.addSeries(graphSeries);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d k:m:s ");
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Date d = new Date((long) value);
                    return dateFormat.format(d);
                }
                return null; // let graphview generate Y-axis label for us
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.graph2);
        layout.addView(graphView);
        


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
			//selectItem(position);	
		}
    }

//    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
    

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


    class db_conn extends AsyncTask<String, Void, Void> {
        Activity activity2;
        ProgressDialog dialog;
        List<Message> titles;
        private Context context;
        public db_conn(Activity activity) {
            this.activity2 = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        /** progress dialog to show user that the backup is processing. */


        @Override
        protected Void doInBackground(String... params) {
            GetDataFromDB db = new GetDataFromDB();
            String url = params[0];
            System.out.println(url);
            jObject = db.makeHttpRequest(url, "GET", null);
         //   Graphs graph = new Graphs(resolver, activity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myHandler.sendEmptyMessage(0);
        }

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        // calling to this function from other pleaces
                        // The notice call method of doing things
                        break;
                    default:
                        break;
                }
            }
        };
    }

    }


