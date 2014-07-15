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
		selectItem(position, activity);	
	}
	
    private void selectItem(final int position, final Activity activity) {	

        new Thread(new Runnable() {        
            public void run() {
            	switch (position) {
            	case 0:
                    String[] urlArray = makeURL(position, sensorsArray);

                    Intent intent = new Intent(activity, TwoDaysActivity.class);
                    intent.putExtra("urls", urlArray);
                    activity.startActivity(intent);
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
