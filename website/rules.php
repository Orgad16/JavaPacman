<html>

<body>
<style>
body {
	font-family: Impact;
}
h1, li {
	color: white;
}
li {
	text-align: left;
	font-size: 20
}
h1 {
	text-align: center;
	color: yellow;
}
p{
	text-align: left;
	color: white;
}
h3 {
	text-align: left;
	color: white;
	font-size: 30;
}
</style>
<h1>
	RULES OF PACMAN
</h1>
<ul>
	<?php
		$json = file_get_contents('./json_files/get_rules.json');
		$obj = json_decode($json);
		$rules = $obj->rules;
		foreach ($rules as $rule) {
			echo "<div>";
			echo "<h3>$rule->desc</h3>";
			echo "<p>rule description<p>";
			echo "</div>";
			echo "<br><br>";
		}
	?>
</ul>
</body>
</html>
