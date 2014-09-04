package com.graduational.sensebox.graphingClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.graduational.sensebox.jsonParsing.JSON_resolver;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

public class GraphDrawer extends AsyncTask<Void, Void, Void>{
	
    private LineGraphView graphView;
    GraphViewSeries graphSeries;
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> temperature;
    
    LinearLayout layout;
    String title;
    Activity activity;
    JSONObject jObject;
    
    
    public GraphDrawer(LinearLayout layout, String title,
    		Activity activity, JSONObject jObject, JSON_resolver resolver) {
    	this.layout = layout;
    	this.title = title;
    	this.activity = activity;
    	this.jObject = jObject;
    	this.resolver = resolver;
	}
    
    @Override
    protected Void doInBackground(Void... params) {
    	makeGraphs(layout, title, activity, jObject, resolver);
    	return null;
    }
    
    @Override
    protected void onPostExecute(Void result) {
    	drawGraphs();
    	super.onPostExecute(result);
    }
	
    public void makeGraphs(LinearLayout layout, String title,
    		Activity activity, JSONObject jObject, JSON_resolver resolver) {
    	String g1 = "graph1";
        try {
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
    }
    
    public void drawGraphs() {
        GraphView.GraphViewData[] data = new GraphView.GraphViewData[date.size()];
//      for (int i = 0; i < date.size(); i++) {
//          data[i] = new GraphView.GraphViewData(5.1, Double.parseDouble(temperature.get(i)));
//      }
      double max = 0, min = 0;
      long last_now = 0;
      Date lastDate = null;
      for (int i = 0; i < data.length; i++) {
          long now = date.get(i).getTime();
          if (!(last_now < now)) {
        	if (date.get(i) == null || lastDate == null) {
        		System.out.println("ASDF " + date.get(i).toString());
              	break;
        	} else {
        		System.out.println("ERROR AT TIME ORDERING DETECTED --> " + date.get(i).toString() + ", last date" + lastDate.toString());
              	break;
        	}

          }
          //System.out.println(date.get(i).toString() + " --- " + now);
          data[i] = new GraphView.GraphViewData(now , Double.parseDouble(temperature.get(i)));
         // System.out.println(now + (i * 60 * 60 * 24 * 1000));
          last_now = now;
      }

      graphSeries = new GraphViewSeries("", new GraphViewSeriesStyle(Color.RED, 4), data);
      graphView = new LineGraphView(activity, title);
      ((LineGraphView) graphView).setDrawBackground(true);
      
      graphView.addSeries(graphSeries);
 //     graphView.setManualYAxisBounds(max + 5, min - 5);
      graphView.setDataPointsRadius(2);
      graphView.setDrawBackground(false);
      graphView.setScalable(true);
      graphView.getGraphViewStyle().setGridColor(Color.BLUE);
			
      
      
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

      
      //LinearLayout[] layouts = graphLayouts();

      layout.addView(graphView);
    }


}
