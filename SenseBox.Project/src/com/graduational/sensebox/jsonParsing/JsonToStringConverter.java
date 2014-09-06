package com.graduational.sensebox.jsonParsing;

import java.util.ArrayList;

import org.json.JSONObject;

import com.graduational.sensebox.DefinedValues;

public class JsonToStringConverter implements DefinedValues {

	private String[] jsonStrings;

	public JsonToStringConverter() {
		// TODO Auto-generated constructor stub
	}
	
    public String[] converter(ArrayList<JSONObject> jsonArray) {
    	jsonStrings = new String[SENSORS_COUNT];
    	for(int i = 0; i < jsonArray.size(); i++) {
    		jsonStrings[i] = jsonArray.get(i).toString(); 
    	}
    	return jsonStrings;
    }

}
