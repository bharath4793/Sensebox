package com.graduational.sensebox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Manos on 9/7/2014.
 */
public class JSON_resolver {

    ArrayList<String> temperature = new ArrayList<String>();
    ArrayList<Date> date = new ArrayList<Date>();
    private JSONObject jObject;
    public JSON_resolver() throws JSONException, ParseException {

    }

    public void resolve () throws JSONException, ParseException {
        JSONArray rowsArray = jObject.getJSONArray("rows");

        for (int i = 0; i < rowsArray.length(); i++) {
            JSONObject rowsElements = rowsArray.getJSONObject(i);
            JSONArray valuesArray = rowsElements.getJSONArray("c");
            JSONObject temperatureObject = valuesArray.getJSONObject(1); //Change this to get Date or Temp
            temperature.add(temperatureObject.getString("Value"));

            JSONObject dateObject = valuesArray.getJSONObject(0);
            String dateString = (String) dateObject.get("Date");
            dateString = dateString.substring(dateString.indexOf("(")+1,dateString.indexOf(")"));

            Date dateParser = new SimpleDateFormat("yyyy, MM, dd, HH, mm, ss").parse(dateString);
            date.add(dateParser);
        }
//        System.out.println("ArrayList -> " + temperature.get(0));
//        System.out.println("Date AL --> " + date.get(0));

    }

    public ArrayList<String> getTemp() {
        return temperature;
    }

    public ArrayList<Date> getDate() {
        return date;
    }

    public void setjObject(JSONObject jObject) {
        this.jObject = jObject;
    }
}
