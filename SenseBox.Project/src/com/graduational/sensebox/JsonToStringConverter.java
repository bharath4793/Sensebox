package com.graduational.sensebox;

import java.util.ArrayList;

import org.json.JSONObject;

public class JsonToStringConverter implements DefinedValues {

	private String[] jsonStrings;

	public JsonToStringConverter() {
		// TODO Auto-generated constructor stub
	}
	
    public String[] converter(ArrayList<JSONObject> jsonArray) {
    	jsonStrings = new String[GRAPH_NUM];
    	for(int i = 0; i < jsonArray.size(); i++) {
    		jsonStrings[i] = jsonArray.get(i).toString(); 
    	}
    	//System.out.println("[DEBUG 3] " + jsonStrings[2]);
    	return jsonStrings;
    }

}
