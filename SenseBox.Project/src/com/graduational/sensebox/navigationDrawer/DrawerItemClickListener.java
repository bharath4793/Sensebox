package com.graduational.sensebox.navigationDrawer;

import com.graduational.sensebox.BuildString;
import com.graduational.sensebox.Builder;
import com.graduational.sensebox.DailyReport;
import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.ReportsActivity;
import com.graduational.sensebox.SplashScreen;
import com.graduational.sensebox.GraphingClasses.GraphActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/* The click listner for ListView in the navigation drawer */
public class DrawerItemClickListener implements ListView.OnItemClickListener, DefinedValues {
	private Activity activity;
	public DrawerItemClickListener(Activity activity) {

		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		System.out.println("--> " + id + ", " + position);
		selectItem(position + 1, activity);	
	}
	
    private void selectItem(final int position, final Activity activity) {	
		System.out.println("--> " +  position + ", " + activity.getTitle());
        new Thread(new Runnable() { 
        	String[] urlArray;
        	Intent intent;
            public void run() {
            	switch (position) {
            	case 0:
                    intent = new Intent(activity, SplashScreen.class);
                    activity.startActivity(intent);
                    break;
            	case 1:
                    urlArray = makeURL(position, sensorsArray);
                    System.out.println("CLICKED--> " + position);
                    intent = new Intent(activity, GraphActivity.class);
                    intent.putExtra("urls", urlArray);
                    intent.putExtra("title", "Two Days Graphs");
                    activity.startActivity(intent);
                    break;
            	case 2:
                    urlArray = makeURL(position, sensorsArray);
                    System.out.println("CLICKED--> " + position);
                    intent = new Intent(activity, GraphActivity.class);
                    intent.putExtra("urls", urlArray);
                    intent.putExtra("title", "Weekly Graphs");
                    activity.startActivity(intent);
                    break;
            	case 3:
                    urlArray = makeURL(position, sensorsArray);
                    System.out.println("CLICKED--> " + position);
                    intent = new Intent(activity, GraphActivity.class);
                    intent.putExtra("urls", urlArray);
                    intent.putExtra("title", "Monthly Graphs");
                    activity.startActivity(intent);
                    break;
            	case 4:
                    urlArray = makeURL(position, sensorsArray);
                    System.out.println("CLICKED--> " + position);
                    intent = new Intent(activity, GraphActivity.class);
                    intent.putExtra("urls", urlArray);
                    intent.putExtra("title", "Three Months Graphs");
                    activity.startActivity(intent);
                    break;
            	case 5: 
            		urlArray = makeURL(position, sensorsArray);
                    intent = new Intent(activity, ReportsActivity.class);
                    System.out.println("CLICKED--> " + position);
                    intent.putExtra("urls", urlArray);
            		activity.startActivity(intent);
            		break;
            	case 6: 
            		String[] minUrlArray = makeURL(99, sensorsArray);
            		String[] maxUrlArray = makeURL(98, sensorsArray);
                    System.out.println("CLICKED--> " + position);
            		intent = new Intent(activity, DailyReport.class);
            		intent.putExtra("minUrlArray", minUrlArray);
            		intent.putExtra("maxUrlArray", maxUrlArray);
            		activity.startActivity(intent);
            		break;

            		


            	}

            }
        }, "selectItemThread").start();
    	

    }
//	private void restartApp() {
//		System.out.println(mainActivity.getTitle());
//		mainActivity.finish();
//		activity.finish();
//		Intent intent = new Intent(mainActivity, SplashScreen.class);
//		mainActivity.startActivity(intent);
//		
//	}

    
    
    private String[] makeURL(int elementClicked, String[] sensorsArray) {
    	String[] urlArray = new String[GRAPH_NUM];
    	for(int i = 0; i < sensorsArray.length; i++) {
        	Builder builder = new BuildString();
        	
    		urlArray[i] = builder.buildString(elementClicked, sensorsArray[i]);
    	}
    	//urlArray holds all the urls for all the sensors.
    	return urlArray;
    }


}
