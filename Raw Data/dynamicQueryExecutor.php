<?php
require ("conf.php");

$sensor = $_GET['sensor'];

$query = "select Date,Time,$sensor from analoog0";

//echo $query;

$sql = mysqli_query($mysqli, $query);


  
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




echo $json;

?>