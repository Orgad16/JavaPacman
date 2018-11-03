<html>
<body>
<style>
html, body {
    height: 100%;
}

html {
    display: table;
    margin: auto;
}
body {
	vertical-align: middle;
	font-family: Impact;
	display: table-cell;
}
table {
    border-collapse: collapse;
		background-color: #000000;
		width:600px;
		height:20px;
		text-align:center;
}

th {
	color: orange;
	padding: 10px;
	font-size: 30;
	text-align:center;
}
td {
	font-size: 20;
	text-align:center;
}



/* red: #ff0000 pink: #FFC0CB sky-blue:#87ceeb orange:#ffa500 yellow:#FFFF00 */

</style>
<h1 style="color:white;text-align:center;">THE 10 BEST PLAYERS</h1>
<!-- <div> -->
	<?php
		$json = file_get_contents('./json_files/get_achivements.json');
		$obj = json_decode($json);
		$achivements = $obj->achivements;
		echo "<table><tbody><tr><th>RANK</th><th>SCORE</th><th>PLAYER</th></tr>";
		$counter=1;
		foreach ($achivements as $achivement) {
			switch ($counter):
				case 6:
				case 1:
					if ($counter == 1) {
						echo "<tr><td style='color:#ff0000;'>1ST</td>";
					}
					else {
						echo "<tr><td style='color:#ff0000;'>6TH</td>";
					}
					echo "<td style='color:#ff0000;'>";
					echo "$achivement->score";
					echo "</td><td style='color:#ff0000;'>";
					echo "$achivement->name";
					echo "</td></tr>";
					break;
				case 7:
				case 2:
					if ($counter == 2) {
						echo "<tr><td style='color:#FFC0CB;'>2ND</td>";
					}
					else {
						echo "<tr><td style='color:#FFC0CB;'>7TH</td>";
					}
					echo "<td style='color:#FFC0CB;'>";
					echo "$achivement->score";
					echo "</td><td style='color:#FFC0CB;'>";
					echo "$achivement->name";
					echo "</td></tr>";
					break;
				case 8:
				case 3:
					if ($counter == 3) {
						echo "<tr><td style='color:#87ceeb;'>3RD</td>";
					}
					else {
						echo "<tr><td style='color:#87ceeb;'>8TH</td>";
					}
					echo "<td style='color:#87ceeb;'>";
					echo "$achivement->score";
					echo "</td><td style='color:#87ceeb;'>";
					echo "$achivement->name";
					echo "</td></tr>";
					break;
				case 9:
				case 4:
					if ($counter == 4) {
						echo "<tr><td style='color:#ffa500;'>4TH</td>";
					}
					else {
						echo "<tr><td style='color:#ffa500;'>9TH</td>";
					}
					echo "<td style='color:#ffa500;'>";
					echo "$achivement->score";
					echo "</td><td style='color:#ffa500;'>";
					echo "$achivement->name";
					echo "</td></tr>";
					break;
				case 10:
				case 5:
					if ($counter == 5) {
						echo "<tr><td style='color:#FFFF00;'>5TH</td>";
					}
					else {
						echo "<tr><td style='color:#FFFF00;'>10TH</td>";
					}
					echo "<td style='color:#FFFF00;'>";
					echo "$achivement->score";
					echo "</td><td style='color:#FFFF00;'>";
					echo "$achivement->name";
					echo "</td></tr>";
					break;
			endswitch;


			$counter+=1;
			echo "<td>";
			echo "$achivement->name";
			echo "</td><td>";
			echo "$achivement->score";
			echo "</td></tr>";
		}
		echo "</tbody></table>";
	?>
<!-- <div> -->



</body>
</html>
