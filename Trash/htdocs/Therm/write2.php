<?php

echo "connection receiving";
$value0=$_GET['value0'];




$opendb=mysql_connect("localhost","root","") or die(mysql_error());
$db = mysql_select_db('thermistor',$opendb);

if ($opendb){
  echo " database open.";
  if (!$db) {
	echo mysql_error();
	}
  $query = "INSERT INTO thermistor.temp VALUES('$value0', curdate(), curtime());";
  /* Run the query */
  $result= mysql_query($query)or die(mysql_error());
  mysql_close($opendb);
  echo "values written = $value0";
}


?>