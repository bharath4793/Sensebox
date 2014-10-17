<?php
$mysqli =mysqli_connect('localhost', 'root', 'sailor437', 'arduino');
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

$query = "SELECT * FROM analoog0 ORDER BY row_num DESC LIMIT 1;";

$sql = mysqli_query($mysqli, $query);
if (!$sql) {
  die("Error running $sql: " . mysql_error());
}


$gust_query = "select * from gust where Date > DATE_SUB(NOW(), INTERVAL 24 HOUR) ORDER BY gust DESC Limit 1";

mysqli_select_db($mysqli,"gust");

$gust_sql= mysqli_query($mysqli, $gust_query);
if (!$gust_sql) {
die("Error running $gust_sql: " . mysql_error());
}


$row = mysqli_fetch_array($sql, MYSQL_BOTH);
echo "<h3>" . $row[1] . "&nbsp;&nbsp;" . $row[2] . "</h3>";
echo "<p>" . "Temperature (DHT22): " . $row[3] . " &deg;C" . "</p>";
echo "<p>" . "Humidity: " . $row[4] . " %" . "</p>";
echo "<p>" . "Temperature (BMP): " . $row[5] . " &deg;C" . "</p>";
echo "<p>" . "Pressure: " . $row[6] . " hPa" . "</p>";
echo "<p>" . "Altitude: " . $row[7] . " Meters" . "</p>";
echo "<p>" . "Wind Speed: " . $row[8] . " Knots" . "</p>";
echo "<p>" . "Direction: " . $row[9] . " Degrees" . "</p>";
echo "<p>" . "Rain: " . $row[10] . " mm/h" . "</p>";
$gust_row = mysqli_fetch_array($gust_sql, MYSQL_BOTH);
echo "<p>" . "Max Gust: " . $gust_row[2] . " knots at " . $gust_row[0] . "  " . $gust_row[1] ."</p>";
echo "</table>";

mysql_close($conn);
?>