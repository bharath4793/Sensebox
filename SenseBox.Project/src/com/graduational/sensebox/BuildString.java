package com.graduational.sensebox;


public class BuildString extends Builder {
	StringBuffer query = new StringBuffer("http://sensebox.noip.me/dynamicQueryExecutor.php?");
	
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
			//Else:
			default:
				query.append("sensor=" + sensor + "&flag=" + String.valueOf(4));
				break;
		}
		System.out.println(query.toString());
		return query.toString();		
		}
	}


	

