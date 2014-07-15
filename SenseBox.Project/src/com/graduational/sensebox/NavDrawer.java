package com.graduational.sensebox;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NavDrawer implements DefinedValues {
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ListView drawerList;
    
	public NavDrawer(final Activity activity) {

	        mTitle = mDrawerTitle = activity.getTitle();	
	        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
	        drawerList = (ListView) activity.findViewById(R.id.left_drawer);
	        drawerList.setAdapter(new ArrayAdapter<>(activity,  R.layout.drawer_list_item, dummyArray));
	        drawerList.setOnItemClickListener(new DrawerItemClickListener(activity));
	        
	        
	        // set a custom shadow that overlays the main content when the drawer opens
	        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        // ActionBarDrawerToggle ties together the the proper interactions
	        // between the sliding drawer and the action bar app icon
	        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
	        	@Override
	        	public void onDrawerClosed(View drawerView) {
	        		activity.getActionBar().setTitle(mTitle);
	        		activity.invalidateOptionsMenu();
	        	}
	        	
	            /** Called when a drawer has settled in a completely open state. */
	        	@Override
	        	public void onDrawerOpened(View drawerView) {
	        		activity.getActionBar().setTitle(mDrawerTitle);
	        		activity.invalidateOptionsMenu();
	        	}
	        };
	        drawerLayout.setDrawerListener(drawerToggle);
	        // enable ActionBar app icon to behave as action to toggle nav drawer
	        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
	        activity.getActionBar().setHomeButtonEnabled(true);
	}
		
    public ActionBarDrawerToggle getDrawerToggle() {
		return drawerToggle;
	}

	public void setDrawerToggle(ActionBarDrawerToggle drawerToggle) {
		this.drawerToggle = drawerToggle;
	}

}
