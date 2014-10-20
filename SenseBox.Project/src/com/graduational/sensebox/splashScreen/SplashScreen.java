package com.graduational.sensebox.splashScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import com.graduational.sensebox.BuildString;
import com.graduational.sensebox.Builder;
import com.graduational.sensebox.DefinedValues;
import com.graduational.sensebox.R;
import com.graduational.sensebox.R.layout;
import com.graduational.sensebox.databaseClasses.DatabaseConnector;

public class SplashScreen extends Activity implements DefinedValues {
	private String[] defaultURLArray;
	public ArrayList<JSONObject> jsonArray = new ArrayList<>();
    DatabaseConnector databaseConnector;
    private Builder builder = new BuildString();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		builder.startStringBuilding(DEFAULT_FLAG, sensorsArray, "null");
		defaultURLArray = builder.getUrlArray();
		
        new PrefetchData(defaultURLArray, this).execute();
	}
}
