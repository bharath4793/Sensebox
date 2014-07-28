<?php

$gust=$_GET['gust'];

$opendb=mysql_connect("localhost","root","sailor437") or die(mysql_error());
$db = mysql_select_db('arduino',$opendb);


if ($opendb){
  echo " database open.\n";
  if (!$db) {
	echo mysql_error();
  }
  $query = "INSERT INTO gust VALUES(curdate(), curtime(), '$gust');";
  /* Run the query */
  $result= mysql_query($query)or die(mysql_error());
  mysql_close($opendb);
  echo "Gust = $gust";
}
?>