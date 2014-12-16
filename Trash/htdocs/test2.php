
<?php

$db_connection = pg_connect("host=localhost dbname=postgres user=tele password=tiny");

$q = pg_query($db_connection, "SELECT id, humid, humtemp FROM table1 ORDER BY id ASC");




while ($get_total_rows = pg_fetch_assoc($q)) {

    $list[] = $get_total_rows;

    //echo "\n";
    //echo $get_total_rows["humid"];
    //echo $get_total_rows["humtemp"];
    //echo $get_total_rows["id"]; 

}



echo(json_encode($list, JSON_PRETTY_PRINT));

//print $json_string;



?>

