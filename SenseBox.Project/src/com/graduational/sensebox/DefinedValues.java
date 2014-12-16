package com.graduational.sensebox;

public interface DefinedValues {
     public static final String[] sensorsArray = 
    	{"Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"};
     public static final int DEFAULT_FLAG = 0;
     public static final int SENSORS_COUNT = 7;
     public static final String [] nav_drawer_array = {"Graphs", "Current Weather Report", "High/Lows", "Results Report"};
     public static final String[] graphLabels = {"Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     public static final String[] resultsReportLabels = {"Results Time", "Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"};
     
     public static final int last24hours = 0;
     public static final int last48hours = 1;
     public static final int lastWeek = 2;
     public static final int lastMonth = 3;
     public static final int lastThreeMonths = 4;
     
     public static final int default_graphs_flag = 0;
     public static final int default_CWR_flag = 5;
     public static final int default_high_low_flag = 6;
     public static final int default_results_report_flag = 0;
}
