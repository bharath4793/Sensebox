   <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript">

    
    // Load the Visualization API and the piechart package.
    google.load('visualization', '1', {'packages':['corechart']});
	var sensorsArray = ["Humidity", "BMP_temp", "BMP_pressure", "Gust", "Direction", "Rain", "Speed"];
	var titles = ["Humidity", "Temperature", "Pressure", "Gust", "Direction", "Rain", "Speed"];
	var query = 'http://192.168.1.5/dynamicQueryExecutor[web].php?';
	var urlArray = [];
	
   	function queryBuilder() {
		for (i = 0; i < sensorsArray.length; i++) {
			urlArray[i] = query + "sensor=" + sensorsArray[i] + "&flag=0" + "&title=" + titles[i];
		}
	}
	
	
    // Set a callback to run when the Google Visualization API is loaded.
	//The following are the steps to make it work:
		//Place google.load and google.setOnLoadCallback in a separate script tag.
		//Use a function expression.
    google.setOnLoadCallback(callDrawer);
	
	function callDrawer() {
		queryBuilder();
		for (i = 0; i < sensorsArray.length; i++) {
			drawGraph(urlArray[i], titles[i]);
		}
	}
	function log(msg) {
		setTimeout(function() {
        throw new Error(msg);
		}, 0);
	}	

      

function drawGraph(path, title) {

var url = path;
	$.ajax({
		url: path,
		dataType: 'json',
        success: function (jsonData) {
            // Create our data table out of JSON data loaded from server.
            var data = new google.visualization.DataTable(jsonData);
			
			var range = data.getColumnRange(1);
			var options = {
				title: title,
				width: 1200,
				height: 600,
				vAxis: {
				minValue: range.min - 2,
				maxValue: range.max + 4
				}
			};
            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.LineChart(document.getElementById(title));
            chart.draw(data, options);
        }
    });
}

    </script>