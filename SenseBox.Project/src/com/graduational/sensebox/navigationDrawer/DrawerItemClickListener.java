package com.graduational.sensebox.navigationDrawer;

import com.graduational.sensebox.BuildString;
import com.graduational.sensebox.Builder;
import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.CurrentConditionsActivity;
import com.graduational.sensebox.graphingClasses.GraphActivity;
import com.graduational.sensebox.minMaxReportClasses.ReportsAtivity;
import com.graduational.sensebox.resultsReportClasses.ResultsActivity;
import com.graduational.sensebox.splashScreen.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/* The click listner for ListView in the navigation drawer */
public class DrawerItemClickListener implements ListView.OnItemClickListener,
		DefinedValues {
	private Activity activity;

	public DrawerItemClickListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		selectItem(position, activity);
	}

	private void selectItem(final int position, final Activity activity) {
		new Thread(new Runnable() {
			String[] urlArray;
			String[] minUrlArray;
			String[] maxUrlArray;
			Intent intent;
			Builder builder = new BuildString();

			public void run() {
				switch (position) {
				case 0:
					builder.startStringBuilding(default_graphs_flag, sensorsArray, null);
					urlArray = builder.getUrlArray();
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "24 Hours Graphs");
					intent.putExtra("position", default_graphs_flag);
					activity.startActivity(intent);
					break;
				case 1:
					builder.startStringBuilding(default_CWR_flag, sensorsArray, null);
					urlArray = builder.getUrlArray();
					intent = new Intent(activity, CurrentConditionsActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("position", default_CWR_flag);
					activity.startActivity(intent);
					break;
				case 2:
					builder.startStringBuilding(default_high_low_flag, sensorsArray, "ASC");
					minUrlArray = builder.getUrlArray();
					builder.startStringBuilding(default_high_low_flag, sensorsArray, "DESC");
					maxUrlArray = builder.getUrlArray();
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", default_high_low_flag);
					intent.putExtra("title", "Daily High/Lows");
					activity.startActivity(intent);
					break;
				case 3:
					builder.startStringBuilding(default_results_report_flag, sensorsArray, null);
					urlArray = builder.getUrlArray();
					intent = new Intent(activity, ResultsActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "24 Hours Report");
					intent.putExtra("position", default_results_report_flag);
					activity.startActivity(intent);
					break;
				}

			}
		}, "selectItemThread").start();

	}

}
