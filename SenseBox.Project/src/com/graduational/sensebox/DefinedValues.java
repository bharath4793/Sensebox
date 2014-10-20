package com.graduational.sensebox;

public interface DefinedValues {
     static final String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
 	 static final int DEFAULT_FLAG = 0;
 	 static final int SENSORS_COUNT = 7;
     static final String [] nav_drawer_array = {"Graphs", "Current Weather Report", "High/Lows", "Results Report"};
     static final String[] graphLabels = {"Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     static final String[] resultsReportLabels = {"Results Time", "Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     
     static final int last24hours = 0;
     static final int last48hours = 1;
     static final int lastWeek = 2;
     static final int lastMonth = 3;
     static final int lastThreeMonths = 4;
     
     static final int default_graphs_flag = 0;
     static final int default_CWR_flag = 5;
     static final int default_high_low_flag = 6;
     static final int default_results_report_flag = 0;
}
