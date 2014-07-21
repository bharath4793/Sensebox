<?php
require ("conf.php");

$sensor = $_GET['sensor'];
$flag = $_GET['flag'];
$separator = $_GET['separator']; //ASC for min or DESC for max at sensor's order by
$iflag = (int) $flag;

$query = "select Date,Time,$sensor from analoog0";
$sql_query = resolveFlag($flag, $sensor, $separator);


$sql = mysqli_query($mysqli, $sql_query);
if (!$sql) {
  die("Error running $sql: " . mysql_error());
}



$json = jsonization($sensor, $sql);

function jsonization($sensor, $sql) {
	$results = array(
		'cols' => array (
			array('label' => 'Date', 'type' => 'datetime'),
			array('label' => 'Temperature', 'type' => 'number')
		),
		'rows' => array()
	);


	while($row = mysqli_fetch_assoc($sql)) {
		// date assumes "yyyy-MM-dd" format
		$dateArr = explode('-', $row['Date']);
		$year = (int) $dateArr[0];
		$month = (int) $dateArr[1] - 1; // subtract 1 to make month compatible with javascript months
		$day = (int) $dateArr[2];

		// time assumes "hh:mm:ss" format
		$timeArr = explode(':', $row['Time']);
		$hour = (int) $timeArr[0];
		$minute = (int) $timeArr[1];
		$second = (int) $timeArr[2];

		$results['rows'][] = array('c' => array(
			array('Date' => "Date($year, $month, $day, $hour, $minute, $second)"),
			array('Value' => $row[$sensor])
		));
	}
	$json = json_encode($results, JSON_NUMERIC_CHECK);
	return $json;
}



function resolveFlag($flag, $sensor, $separator) {
	$specialQuery;
	switch($flag) {
		//Last 24 hours.
		case 0:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 24 HOUR)";
			break;
		//Last 48 hours.
		case 1:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 2 DAY)";
			break;
		//Last Week.
		case 2:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 1 WEEK)";
			break;
		//Last Month.
		case 3:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
			break;
		case 4:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 3 MONTH)";
			break;
		case 5:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE Date = CURDATE() ORDER BY Time DESC LIMIT 1";
			break;
		case 6:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 24 HOUR) ORDER BY $sensor $separator, Time DESC limit 1";		
			break;
		//Else Last 24 hours.
		default:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
	}
	return $specialQuery;
}





echo $json;

?>