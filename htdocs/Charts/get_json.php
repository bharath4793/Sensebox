<?php
/* $server = the IP address or network name of the server
 * $userName = the user to log into the Timebase with
 * $password = the Timebase account password
 * $TimebaseName = the name of the Timebase to pull Time from
 * table structure - colum1 is Date: has text/description - column2 is Time has the value
 */
$con = mysql_connect('localhost', 'root', '') or die('Error connecting to server');
 
mysql_select_db('Arduino', $con); 

// write your SQL query here (you may use parameters from $_GET or $_POST if you need them)
$query = mysql_query('SELECT * FROM analoog0');

$table = array();
$table['cols'] = array(
	/* define your TimeTable columns here
	 * each column gets its own array
	 * syntax of the arrays is:
	 * label => column label
	 * type => Time type of column (string, number, date, datetime, boolean)
	 */
	// I assumed your first column is a "string" type
	// and your second column is a "number" type
	// but you can change them if they are not
    array('label' => 'Date', 'type' => 'date'),
	array('label' => 'Time', 'type' => 'time'),
	array('label' => 'Temperature', 'type' => 'number')
);

$rows = array();
while($r = mysql_fetch_assoc($query)) {
    $temp = array();
	// each column needs to have Time inserted via the $temp array
	$temp[] = array('v' => $r['Date']);
	$temp[] = array('v' => (int) $r['Time']);
	$temp[] = array('v' => $r['Temperature']); // typeDatet all numbers to the appropriate type (int or float) as needed - otherwise they are input as strings
	
	// insert the temp array into $rows
    $rows[] = array('c' => $temp);
}

// populate the table with rows of Time
$table['rows'] = $rows;

// encode the table as JSON
$jsonTable = json_encode($table);

// set up header; first two prevent IE from caching queries
header('Cache-Control: no-cache, must-revalidate');
header('Expires: Mon, 26 Jul 1997 05:00:00 GMT');
header('Content-type: application/json');

// return the JSON Time
echo $jsonTable;
?>