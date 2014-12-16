        <?php
        $con=mysql_connect("localhost","root","") or die("Failed to connect with database!!!!");
        mysql_select_db("Arduino", $con); 

        $sth = mysql_query("SELECT * FROM analoog0");

        $data = array (
      'cols' => array( 
        array('id' => 'date', 'label' => 'Date ', 'type' => 'datetime'), 
        array('id' => 'temp1', 'label' => 'Temp 1', 'type' => 'number'), 
        array('id' => 'temp2', 'label' => 'Temp 2', 'type' => 'number')
    ),
    'rows' => array()
);

while ($res = mysql_fetch_assoc($sth))
    // array nesting is complex owing to to google charts api
    array_push($data['rows'], array('c' => array(
        array('v' => $res['date']), 
        array('v' => $res['temp1']), 
        array('v' => $res['temp2'])
    )));
}
?>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
            var bar_chart_data = new google.visualization.DataTable(<?php echo json_encode($data); ?>);
        var options = {
          title: 'Weather data'
        };
        var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chart.draw(bar_chart_data, options);
      }
    </script>
</head>
            <body>
                <div id="chart_div" style="width: 900px; height: 500px;"></div>
            </body>
        </html>