package com.graduational.sensebox;

import android.app.Activity;
import android.content.Intent;
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
		System.out.println("--> " +  position);
        new Thread(new Runnable() { 
        	String[] urlArray;
        	Intent intent;
            public void run() {
            	switch (position) {
            	case 1:
                    urlArray = makeURL(position, sensorsArray);

                    intent = new Intent(activity, TwoDaysActivity.class);
                    intent.putExtra("urls", urlArray);
                    activity.startActivity(intent);
                    break;
            	case 5: 
            		String[] urlArray = makeURL(position, sensorsArray);
            		intent = new Intent(activity, ReportsActivity.class);
            		intent.putExtra("urls", urlArray);
            		activity.startActivity(intent);
            		break;
            	}

            }
        }, "selectItemThread").start();
    	

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
}
