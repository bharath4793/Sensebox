package com.graduational.sensebox;

public interface DefinedValues {
     static final String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
 	 static final int DEFAULT_FLAG = 0;
 	 static final int SENSORS_COUNT = 7;
     static final String [] dummyArray = {"Last 24 Hours Graphs","Last 2 Days Graphs", "Last Week Graphs", "Last Month Graphs", "Last Three Months Graphs",
    	 								  "Current Conditions Report", "Last 24 Hours Highs/Lows", "Last 2 Days Highs/Lows", "Last Week Highs/Lows", "Last Month Highs/Lows", 
    	 								  "Last Three Months Highs/Lows", "Results Report"};
     static final String[] graphLabels = {"Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     static final String[] resultsReportLabels = {"Results Time", "Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     static final int last24hours = 0;
     static final int last48hours = 1;
     static final int lastWeek = 3;
     static final int lastMonth = 4;
     static final int lastThreeMonths = 5;
}
