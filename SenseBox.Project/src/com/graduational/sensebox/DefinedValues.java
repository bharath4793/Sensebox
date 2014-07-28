package com.graduational.sensebox;

public interface DefinedValues {
     static final String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
 	 static final int DEFAULT_FLAG = 0;
 	 static final int GRAPH_NUM = 7;
     static final String [] dummyArray = {"Last 24 Hours Graphs","Last 2 Days Graphs", "Last Week Graphs", "Last Month Graphs", "Last Three Months Graphs",
    	 								  "Current Conditions Report", "Last 24 Hours Highs/Lows", "Last 2 Days Highs/Lows", "Last Week Highs/Lows", "Last Month Highs/Lows", 
    	 								  "Last Three Months Highs/Lows"};
     static final String[] graphLabels = {"Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
}
