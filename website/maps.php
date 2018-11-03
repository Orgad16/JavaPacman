<html>
<body>
<style>
body {
	text-align: center;
	font-family: Impact;
}
h1 {
	color: yellow;
	text-align: center;
	padding: 10px;
}
p {
	text-align: center;
	padding: 10px;
	color: white;
}
h1, p {
	text-align: center;
}
h3 {
	text-align: center;
	color: white;
}
</style>
	<?php
		$counter=1;
		$json = file_get_contents('./json_files/get_pictures.json');
		$obj = json_decode($json);
		$products = $obj->pictures;
		foreach ($products as $product) {
			echo "<p>";
			switch ($counter) {
				case 1:
					echo "<h1>First Map</h1>";
					break;
				case 2:
					echo "<h1>Second Map</h1>";
					break;
				case 3:
					echo "<h1>Third Map</h1>";
					break;
				case 4:
					echo "<h1>Fourh Map</h1>";
					break;
			}
			$counter+=1;
			echo "<p>This is the first map with the description <br> Next to it there is a video of the map";
			//echo "<p><img src='$product' style='width:100px;height:100px;padding:10px'></p>";
			echo "</p><br>";
			echo "<video align='middle' width='320' height='240' controls>";
			echo "<source src='./pacman_videos/cup.mp4' type='video/mp4'>";
			echo "</video>";
			echo "</p>";
			echo "<br>";
		}
	?>

</body>
</html>
