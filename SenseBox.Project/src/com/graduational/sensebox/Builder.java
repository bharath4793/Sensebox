package com.graduational.sensebox;

public abstract class Builder {
	public abstract void startStringBuilding(int elementClicked, String[] sensorsArray, String separator);
	public abstract String buildString(int elementClicked, String sensor, String separator);
	public abstract String[] getUrlArray();
}
