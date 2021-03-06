<?php


  $mysqli =mysqli_connect('localhost', 'root', '', 'Arduino');
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
}


  $sql = mysqli_query($mysqli, 'SELECT Date, Time, Temperature, Humidity FROM analoog0 where analoog0.Date >= SYSDATE() - INTERVAL 1 DAY');
  
  if (!$sql) {
  die("Error running $sql: " . mysql_error());
  }
  

  
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
        array('v' => "Date($year, $month, $day, $hour, $minute, $second)"),
        array('v' => $row['Temperature'])
    ));
}
$json = json_encode($results, JSON_NUMERIC_CHECK);
echo $json;

?>

