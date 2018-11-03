<html>
<body>
<style>
body {
	text-align: center;
}
h1 ,p{
	font-family: Impact;
	color: white;
}
h1 {
	font-size: 30;
	color: yellow;
}
p {
	font-size: 20;
}
</style>
<h1>ABOUT</h1>
<p>
	<?php
		$json = file_get_contents('./json_files/get_about.json');
		$obj = json_decode($json);
		$abouts = $obj->abouts;
		foreach ($abouts as $about) {
			echo "$about";
		}
	?>
</p>


</body>
</html>
