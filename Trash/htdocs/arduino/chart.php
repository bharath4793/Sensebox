<?php

$opendb=mysql_connect("localhost","root","") or mysql_error("never come here");
mysql_select_db("arduino",$opendb);
if ($opendb){
  $query = "SELECT *  FROM analoog0 ORDER BY Date DESC, Time DESC LIMIT 0,16";
  /* Run the query */
  $result= MYSQL_QUERY($query)or die( "Unable to query netabel");
  while ( $row = mysql_fetch_assoc( $result ) )  {     
          $Temperature[] = $row['Temperature'];                //put table row in array
          
          $Time[] = $row['Time'];
          $Date[] = $row['Date'];
  }
}
$data= implode( ',', $Temperature )  ;

$Date= implode( '|', $Date) ;
$Time= implode ( '|', $Time) ;
$imgSRC = 'http://chart.apis.google.com/chart';
$imgSRC=$imgSRC . '?chxt=x,y,x';
$imgSRC=$imgSRC . '&chxl=0:|' . $Time . '|';
$imgSRC=$imgSRC . '1:|256|512|768|1023|';
//$imgSRC=$imgSRC . '1:|0|256|512|768|1023|';
$imgSRC=$imgSRC . '2:|' . $Date . '';
$imgSRC=$imgSRC . '&chs=1000x300';
$imgSRC=$imgSRC . '&cht=lc';
$imgSRC=$imgSRC . '&chf=a,s,000000A0|';
//$imgSRC=$imgSRC . 'c,ls,90,EFEFEF,0.25,BBBBBB,0.25';
$imgSRC=$imgSRC . '&chds=0,600';
$imgSRC=$imgSRC . '&chd=t:' . $data . '';
$imgSRC=$imgSRC . '&chco=ff0000,3072F3'; 
$imgSRC=$imgSRC . '&chls=2|2,6,3';
$imgSRC=$imgSRC . '&chds=256,1025';                  //values min/max y
$imgSRC=$imgSRC . '&chtt=waarden';
$imgSRC=$imgSRC . '&chdl=Value0|Value1&chdlp=bv';

echo '<img src="' . $imgSRC . '" />';
?>