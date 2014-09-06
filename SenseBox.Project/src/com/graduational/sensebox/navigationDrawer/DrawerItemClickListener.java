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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("--> " + id + ", " + position);
		selectItem(position, activity);
	}

	private void selectItem(final int position, final Activity activity) {
		System.out.println("--> " + position + ", " + activity.getTitle());
		new Thread(new Runnable() {
			String[] urlArray;
			String[] minUrlArray;
			String[] maxUrlArray;
			Intent intent;

			public void run() {
				switch (position) {
				case 0:
					urlArray = makeURL(position, sensorsArray, null);
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "24 Hours Graphs");
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 1:
					urlArray = makeURL(position, sensorsArray, null);
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "Two Days Graphs");
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 2:
					urlArray = makeURL(position, sensorsArray, null);
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "Weekly Graphs");
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 3:
					urlArray = makeURL(position, sensorsArray, null);
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "Monthly Graphs");
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 4:
					urlArray = makeURL(position, sensorsArray, null);
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, GraphActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "Three Months Graphs");
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 5:
					urlArray = makeURL(position, sensorsArray, null);
					intent = new Intent(activity, CurrentConditionsActivity.class);
					System.out.println("CLICKED--> " + position);
					intent.putExtra("urls", urlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 6:
					minUrlArray = makeURL(position, sensorsArray, "ASC");
					maxUrlArray = makeURL(position, sensorsArray, "DESC");
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 7:
					minUrlArray = makeURL(position, sensorsArray, "ASC");
					maxUrlArray = makeURL(position, sensorsArray, "DESC");
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 8:
					minUrlArray = makeURL(position, sensorsArray, "ASC");
					maxUrlArray = makeURL(position, sensorsArray, "DESC");
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 9:
					minUrlArray = makeURL(position, sensorsArray, "ASC");
					maxUrlArray = makeURL(position, sensorsArray, "DESC");
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 10:
					minUrlArray = makeURL(position, sensorsArray, "ASC");
					maxUrlArray = makeURL(position, sensorsArray, "DESC");
					System.out.println("CLICKED--> " + position);
					intent = new Intent(activity, ReportsAtivity.class);
					intent.putExtra("minUrlArray", minUrlArray);
					intent.putExtra("maxUrlArray", maxUrlArray);
					intent.putExtra("position", position);
					activity.startActivity(intent);
					break;
				case 11:
					urlArray = makeURL(0, sensorsArray, null);
					intent = new Intent(activity, ResultsActivity.class);
					intent.putExtra("urls", urlArray);
					intent.putExtra("title", "24 Hours Report");
					intent.putExtra("position", last24hours);
					activity.startActivity(intent);
					break;
				}

			}
		}, "selectItemThread").start();

	}


	public String[] makeURL(int elementClicked, String[] sensorsArray, String separator) {
		String[] urlArray = new String[SENSORS_COUNT];
		for (int i = 0; i < sensorsArray.length; i++) {
			Builder builder = new BuildString();

			urlArray[i] = builder.buildString(elementClicked, sensorsArray[i],
					separator);
		}
		// urlArray holds all the urls for all the sensors.
		return urlArray;
	}

}
