<!DOCTYPE html>
<html>
<head>
    <title>YouTube Recent Upload Thing</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
</head>
<body>
<div id="static_video"></div>
    <script type="text/javascript">
   <!--   
        function showVideo(response) {
            if(response.data) {
				if(response.data.items) {
					var items = response.data.items;
					if(items.length>0) {
						var item = items[0];
						var videoid = "http://www.youtube.com/embed/"+item.id;
						console.log("Latest ID: '"+videoid+"'");
						var video = "<iframe width='640' height='480' src='"+videoid+"' frameborder='0' allowfullscreen></iframe>"; 
						$('#static_video').html(video);
					}
				}
			}
        }
//-->
    </script>
    <script type="text/javascript" src="https://gdata.youtube.com/feeds/api/users/XNi-7_0FXTSTdNFKO7RN1w/uploads?max-results=1&amp;orderby=published&amp;v=2&amp;alt=jsonc&amp;callback=showVideo"></script>
</body>
</html>
