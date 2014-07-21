package com.graduational.sensebox.GraphingClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Canvas;
import android.widget.LinearLayout;

import com.graduational.sensebox.JSON_resolver;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class GraphDrawer {
	
    private LineGraphView graphView;
    GraphViewSeries graphSeries;
    JSON_resolver resolver = null;
    ArrayList<Date> date;
    ArrayList<String> temperature;
    
    public GraphDrawer() {
    	
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

        GraphView.GraphViewData[] data = new GraphView.GraphViewData[date.size()];
//        for (int i = 0; i < date.size(); i++) {
//            data[i] = new GraphView.GraphViewData(5.1, Double.parseDouble(temperature.get(i)));
//        }

        long last_now = 0;
        for (int i = 0; i < data.length; i++) {
            long now = date.get(i).getTime();
            if (!(last_now < now)) {
            	System.out.println("ERROR AT TIME ORDERING DETECTED --> " + date.get(i).toString());
            	break;
            }
            //System.out.println(date.get(i).toString() + " --- " + now);
            data[i] = new GraphView.GraphViewData(now , Double.parseDouble(temperature.get(i)));
           // System.out.println(now + (i * 60 * 60 * 24 * 1000));
            last_now = now;
        }

        graphSeries = new GraphViewSeries(data);
        graphView = new LineGraphView(activity, title);
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

        
        //LinearLayout[] layouts = graphLayouts();

        layout.addView(graphView);
    }


}
