<?php

echo "connection receiving";
$value0=$_GET['value0'];





$opendb=mysql_connect("localhost","root","sailor437") or die(mysql_error());
$db = mysql_select_db('temps',$opendb);

if ($opendb){
  echo " database open.";
  if (!$db) {
	echo mysql_error();
	}
  $query = "INSERT INTO arduinoTest VALUES('$value0');";
  /* Run the query */
  $result= mysql_query($query)or die(mysql_error());
  mysql_close($opendb);
   echo "values written = $value0";

}


?>