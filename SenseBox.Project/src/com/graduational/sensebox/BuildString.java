package com.graduational.sensebox;


public class BuildString extends Builder {
	StringBuffer query = new StringBuffer("http://192.168.1.5/dynamicQueryExecutor.php?");
	
	@Override
	public
	String buildString(int elementClicked, String sensor) {
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
			case 98:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(6) + "&separator=DESC");
				break;
			case 99:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(6) + "&separator=ASC");
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


	

