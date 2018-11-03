<html>
	<title>Pacman Site</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<body>
<style>
* {
    box-sizing: border-box;
}

body {
    font-family: Arial, Helvetica, sans-serif;
}

/* Style the header */
header {
    background-color: #000000;
    padding: 30px;
    text-align: center;
    font-size: 35px;
    color: yellow;
		border-bottom: 1px solid #333333;
}

/* Container for flexboxes */
section {
    display: -webkit-flex;
    display: flex;
}

/* Style the navigation menu */
nav {
    -webkit-flex: 1;
    -ms-flex: 1;
    flex: 0.5;
    background: #000001;
    padding: 20px;
}

/* Style the list inside the menu */
nav ul {
    list-style-type: none;
		text-align: center;
    padding: 0;
}

/* Style the content */
article {
    -webkit-flex: 3;
    -ms-flex: 3;
    flex: 2;
    background-color: #000000;
    padding: 30px;
		color: white;
		border-left: 1px solid #333333;
}

/* Style the footer */
footer {
    background-color: #000000;
    padding: 10px;
    text-align: center;
    color: white;
		border-top: 1px solid #333333;
}

li {
	color: white;
	font-family: Impact;
	font-size: 30;
	padding-top: 45px;
}
</style>

<header>
<!-- <?php
	$json = file_get_contents('http://get_header');
	$obj = json_decode($json);

	$products = $obj->products;
	foreach ($products as $product) {
		echo "<h2>$product</h2>";
	}
?> -->
<img src='/pacman_pictures/logo.png'>
</header>
<section>
	<nav>
		<ul>
			<?php
				$json = file_get_contents('./json_files/get_menu_text.json');
				$obj = json_decode($json);
				$products = $obj->text;
				foreach ($products as $product) {
					switch ($product){
						case "ABOUT":
							echo "<li><a href='/about.php' target='first_frame'>$product</a></li>";
							break;
						case "MAPS":
							echo "<li><a href='/maps.php' target='first_frame'>$product</a></li>";
							break;
						case "RULES":
							echo "<li><a href='/rules.php' target='first_frame'>$product</a></li>";
							break;
						case "ACHIVEMENTS":
							echo "<li><a href='/achivements.php' target='first_frame'>$product</a></li>";
							break;
					}
				}
			?>
		</ul>
	</nav>
	<article >
		<iframe style="width:1060px;height:380px;border:none;background-color:black;" src="" name="first_frame"></iframe>
	</article>
</section>

<footer>
	<h1>copy</h1>
</footer>

</body>
</html>
