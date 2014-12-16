<?php

echo "connection receiving";
$value0=$_GET['value0'];
$value1=$_GET['value1'];



$opendb=mysql_connect("localhost","root","") or die(mysql_error());
$db = mysql_select_db('arduino',$opendb);

if ($opendb){
  echo " database open.";
  if (!$db) {
	echo mysql_error();
	}
  $query = "INSERT INTO analoog0 (Date, Time, Temperature, Humidity) VALUES(curdate(), curtime(), '$value0', '$value1');";
  /* Run the query */
  $result= mysql_query($query)or die(mysql_error());
  mysql_close($opendb);
  echo "values written = $value0";
  echo "	values written(Humidity) = $value1";
}


?>