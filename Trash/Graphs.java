package project.graduational.manos.sensebox;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.jjoe64.graphview.*;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Manos on 9/7/2014.
 */
public class Graphs extends Activity {
    ArrayList<Date> date;
    ArrayList<String> temperature;
    Activity activity;
    GraphView graphView;
    GraphViewSeries graphSeries;



    public Graphs() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_layout);



        GraphView.GraphViewData[] data = new GraphView.GraphViewData[date.size()];
        for (int i = 0; i < date.size(); i++) {
            data[i] = new GraphView.GraphViewData(5.1, Double.parseDouble(temperature.get(i)));
        }

        long now = new Date().getTime();
        for (int i = 0; i < data.length; i++) {
            data[i] = new GraphView.GraphViewData(now + (i * 60 * 60 * 24 * 1000), Double.parseDouble(temperature.get(i)));
        }

        graphSeries = new GraphViewSeries(data);
        graphView = new LineGraphView(this, "Demo");
        ((LineGraphView) graphView).setDrawBackground(true);

        graphView.addSeries(graphSeries);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.graph2);
        layout.addView(graphView);
    }

    public Graphs(JSON_resolver resolver, Activity activity) {
        this.activity = activity;


    }


    private void dateParser() {

    }

}

