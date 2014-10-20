package com.graduational.sensebox;


public class BuildString extends Builder implements DefinedValues {
	StringBuffer query;
	private String[] urlArray;
	
	
	
	public String[] getUrlArray() {
		return urlArray;
	}
	
	public void startStringBuilding(int elementClicked, String[] sensorsArray, String separator) {
		urlArray = new String[SENSORS_COUNT];
		for (int i = 0; i < sensorsArray.length; i++) {
			query = new StringBuffer("http://sensebox.noip.me/dynamicQueryExecutor.php?");

			urlArray[i] = buildString(elementClicked, sensorsArray[i], separator);
			System.out.println("Item clicked = " + elementClicked + ", " + sensorsArray[i] +
					", " + urlArray[i]);
		}
	}



	@Override
	public
	String buildString(int elementClicked, String sensor, String separator) {
		switch(elementClicked) {
			//If "Last 1 Days" clicked:
			case 0:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			//If "Last 2 Days" clicked:
			case 1:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			//If "Last Week" clicked:
			case 2:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			//If "Last Month" clicked:
			case 3:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			case 4:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			case 5:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			case 6:
				if (separator.equals("DESC")) {
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=DESC");
				break;
				} else if (separator.equals("ASC")) {
					query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=ASC");
					break;
				}
			case 7:
				if (separator.equals("DESC")) {
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=DESC");
				break;
				} else if (separator.equals("ASC")) {
					query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=ASC");
					break;
				}
			case 8:
				if (separator.equals("DESC")) {
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=DESC");
				break;
				} else if (separator.equals("ASC")) {
					query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=ASC");
					break;
				}
			case 9:
				if (separator.equals("DESC")) {
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=DESC");
				break;
				} else if (separator.equals("ASC")) {
					query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=ASC");
					break;
				}
			case 10:
				if (separator.equals("DESC")) {
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=DESC");
				break;
				} else if (separator.equals("ASC")) {
					query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked) + "&separator=ASC");
					break;
				}
			case 11:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(elementClicked));
				break;
			//Else:
			default:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(4));
				break;
		}
		System.out.println(query.toString());
		return query.toString();		
		}
	}


	

