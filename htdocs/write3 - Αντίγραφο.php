<?php

echo "connection receiving";
$value0=$_GET['value0'];
$value1=$_GET['value1'];
$value2=$_GET['value2'];
$value3=$_GET['value3'];
$value4=$_GET['value4'];
$value5=$_GET['value5'];
$value6=$_GET['value6'];
$value7=$_GET['value7'];
$value8=$_GET['value8'];




$opendb=mysql_connect("localhost","root","sailor437") or die(mysql_error());
$db = mysql_select_db('arduino',$opendb);

if ($opendb){
  echo " database open.";
  if (!$db) {
	echo mysql_error();
	}
  $query = "INSERT INTO analoog0 VALUES(curdate(), curtime(), '$value0', '$value1', '$value2', '$value3', '$value4', '$value5', '$value6', '$value7', '$value8' );";
  /* Run the query */
  $result= mysql_query($query)or die(mysql_error());
  mysql_close($opendb);
  echo "values written = $value0";
  echo "	values written(Humidity) = $value1";
}


?>