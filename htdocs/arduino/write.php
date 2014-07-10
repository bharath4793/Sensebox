<?php

echo "connection receivingg";
$value0=$_GET['value0'];


$opendb=mysql_connect("localhost","root","") or mysql_error("never come here");
mysql_select_db("arduino",$opendb);
if ($opendb){
  echo " database open.";
  $query = "INSERT INTO analoog0 VALUES(curdate(), curtime(), '$value0');";
  /* Run the query */
  $result= MYSQL_QUERY($query)or die( mysql_error());
  mysql_close($opendb);
  echo "values written = $value0";
}

?>