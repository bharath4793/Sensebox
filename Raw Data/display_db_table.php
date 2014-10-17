<?php
$mysqli =mysqli_connect('localhost', 'root', 'sailor437', 'arduino');
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: ".mysqli_connect_error();
}

$query = "select * from analoog0 WHERE analoog0.Date > DATE_SUB(CURDATE(), INTERVAL 24 HOUR)";

$sql = mysqli_query($mysqli, $query);
if (!$sql) {
  die("Error running $sql: " . mysql_error());
}
echo "<table border=1>
<tr>
<th>Date</th>
<th>Time</th>
<th>Temperature (DHT22)</th>
<th>Humidity</th>
<th>Temperature (BMP)</th>
<th>Pressure</th>
<th>Altitude</th>
<th>Wind Speed</th>
<th>Direction</th>
<th>Rain</th>
</tr>";

while($row = mysqli_fetch_array($sql, MYSQL_BOTH)) {
echo "<tr>";
echo "<td>" . $row[1] . "</td>";
echo "<td>" . $row[2] . "</td>";
echo "<td>" . $row[3] . "</td>";
echo "<td>" . $row[4] . "</td>";
echo "<td>" . $row[5] . "</td>";
echo "<td>" . $row[6] . "</td>";
echo "<td>" . $row[7] . "</td>";
echo "<td>" . $row[8] . "</td>";
echo "<td>" . $row[9] . "</td>";
echo "<td>" . $row[10] . "</td>";
echo "</tr>";
}
echo "</table>";

mysql_close($conn);
?>