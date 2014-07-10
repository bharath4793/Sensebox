package com.graduational.sensebox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

public class Drawer extends Activity{
	private DrawerLayout drawerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
	}
}
