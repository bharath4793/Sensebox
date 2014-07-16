package com.graduational.sensebox;

public interface DefinedValues {
     static final String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
 	 static final int DEFAULT_FLAG = 0;
 	 static final int GRAPH_NUM = 7;
     static final String [] dummyArray = {"Last 2 Days Graph", "Last Week Graph", "Last Month Graph", "Last Three Months Graph",
    	 								  "Current Conditions Report", "Last 24 Hours Report", "Last 2 Days Report", "Last Week Report", "Last Three Monrths Report"};
     static final String[] graphLabels = {"Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
}
