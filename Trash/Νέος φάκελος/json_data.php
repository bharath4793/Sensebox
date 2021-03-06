<?php
require ("conf.php");

$sensor = $_GET['sensor'];
$flag = $_GET['flag'];
$label = $_GET['label'];


$specialQuery = resolveFlag($flag, $sensor);

  
$results = array(
    'cols' => array (
        array('label' => 'Date', 'type' => 'datetime'),
        array('label' => $label, 'type' => 'number')
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
        array('v' => "Date($year, $month, $day, $hour, $minute, $second)"),
        array('v' => $row[$label])
    ));
}

function resolveFlag($flag, $sensor) {
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
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE Date = CURDATE() ORDER BY Time DESC LIMIT 1";
			break;
		//Else Last 24 hours.
		default:
			$specialQuery = "SELECT Date, Time, $sensor FROM analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
	}
	return $specialQuery;
}

$json = json_encode($results, JSON_NUMERIC_CHECK);
echo $json;


?>