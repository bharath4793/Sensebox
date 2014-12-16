package com.graduational.sensebox;

import java.util.ArrayList;
import java.util.List;

import com.graduational.sensebox.graphingClasses.GraphActivity;
import com.graduational.sensebox.graphingClasses.MainActivity;
import com.graduational.sensebox.minMaxReportClasses.ReportsAtivity;
import com.graduational.sensebox.resultsReportClasses.ResultsActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SpinnerActivity implements OnItemSelectedListener, DefinedValues {
	private Activity activity;
	String[] urlArray;
	String[] minUrlArray;
	String[] maxUrlArray;
	Intent intent;
	Builder builder = new BuildString();
	List<String> list = new ArrayList<String>();
	private int highsLowsDifference = 6;
	
	public SpinnerActivity(final Activity activity) {
		this.activity = activity;
		Spinner spinner = (Spinner) activity.findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item) {

		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {

		        View v = super.getView(position, convertView, parent);
		        System.out.println(position + ", " + getCount());
		        if (position == getCount()) {
		            ((TextView)v.findViewById(android.R.id.text1)).setText("");
		            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
		        }

		        return v;
		    }       

		    @Override
		    public int getCount() {
		        return super.getCount()-1; // you dont display last item. It is used as hint.
		    }

		};

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("Daily");
		adapter.add("Two Days");
		adapter.add("Weekly");
		adapter.add("Monthly");
		adapter.add("Three Months");
		adapter.add("Select Time Range");


		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getCount()); //display hint
		spinner.setOnItemSelectedListener(this);
	}
	


	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case last24hours:
			if (activity instanceof GraphActivity || activity instanceof MainActivity) {
				activity.finish();
				builder.startStringBuilding(position, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, GraphActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "24 Hours Graphs");
				intent.putExtra("position", position);
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ReportsAtivity) {
				activity.finish();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "ASC");
				minUrlArray = builder.getUrlArray();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "DESC");
				maxUrlArray = builder.getUrlArray();
				intent = new Intent(activity, ReportsAtivity.class);
				intent.putExtra("minUrlArray", minUrlArray);
				intent.putExtra("maxUrlArray", maxUrlArray);
				intent.putExtra("position", position);
				intent.putExtra("title", "Daily High/Lows");
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ResultsActivity) {
				activity.finish();
				builder.startStringBuilding(last24hours, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Daily Report");
				activity.startActivity(intent);
				break;
			}
			
		case last48hours:
			if (activity instanceof GraphActivity || activity instanceof MainActivity) {
				activity.finish();
				builder.startStringBuilding(position, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, GraphActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Two Days Graphs");
				intent.putExtra("position", position);
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ReportsAtivity) {
				activity.finish();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "ASC");
				minUrlArray = builder.getUrlArray();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "DESC");
				maxUrlArray = builder.getUrlArray();
				intent = new Intent(activity, ReportsAtivity.class);
				intent.putExtra("minUrlArray", minUrlArray);
				intent.putExtra("maxUrlArray", maxUrlArray);
				intent.putExtra("position", position);
				intent.putExtra("title", "Two Days High/Lows");
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ResultsActivity) {
				activity.finish();
				builder.startStringBuilding(last48hours, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Two Days Report");
				activity.startActivity(intent);
				break;
			}	
		case lastWeek:
			if (activity instanceof GraphActivity || activity instanceof MainActivity) {
				activity.finish();
				builder.startStringBuilding(position, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, GraphActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Weekly Graphs");
				intent.putExtra("position", position);
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ReportsAtivity) {
				activity.finish();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "ASC");
				minUrlArray = builder.getUrlArray();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "DESC");
				maxUrlArray = builder.getUrlArray();
				intent = new Intent(activity, ReportsAtivity.class);
				intent.putExtra("minUrlArray", minUrlArray);
				intent.putExtra("maxUrlArray", maxUrlArray);
				intent.putExtra("position", position);
				intent.putExtra("title", "Weekly High/Lows");
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ResultsActivity) {
				activity.finish();
				builder.startStringBuilding(lastWeek, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Weekly Report");
				activity.startActivity(intent);
				break;
			}
		case lastMonth:
			if (activity instanceof GraphActivity || activity instanceof MainActivity) {
				activity.finish();
				builder.startStringBuilding(position, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, GraphActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Monthly Graphs");
				intent.putExtra("position", position);
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ReportsAtivity) {
				activity.finish();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "ASC");
				minUrlArray = builder.getUrlArray();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "DESC");
				maxUrlArray = builder.getUrlArray();
				intent = new Intent(activity, ReportsAtivity.class);
				intent.putExtra("minUrlArray", minUrlArray);
				intent.putExtra("maxUrlArray", maxUrlArray);
				intent.putExtra("position", position);
				intent.putExtra("title", "Monthly High/Lows");
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ResultsActivity) {
				activity.finish();
				builder.startStringBuilding(lastMonth, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Monthly Report");
				activity.startActivity(intent);
				break;
			}
		case lastThreeMonths:
			if (activity instanceof GraphActivity || activity instanceof MainActivity) {
				activity.finish();
				builder.startStringBuilding(position, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, GraphActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Three Months Graphs");
				intent.putExtra("position", position);
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ReportsAtivity) {
				activity.finish();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "ASC");
				minUrlArray = builder.getUrlArray();
				builder.startStringBuilding(position + highsLowsDifference, sensorsArray, "DESC");
				maxUrlArray = builder.getUrlArray();
				intent = new Intent(activity, ReportsAtivity.class);
				intent.putExtra("minUrlArray", minUrlArray);
				intent.putExtra("maxUrlArray", maxUrlArray);
				intent.putExtra("position", position);
				intent.putExtra("title", "Three Months High/Lows");
				activity.startActivity(intent);
				break;
			} else if (activity instanceof ResultsActivity) {
				activity.finish();
				builder.startStringBuilding(lastThreeMonths, sensorsArray, null);
				urlArray = builder.getUrlArray();
				intent = new Intent(activity, ResultsActivity.class);
				intent.putExtra("urls", urlArray);
				intent.putExtra("title", "Three Months Report");
				activity.startActivity(intent);
				break;
			}
		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
