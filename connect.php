<?php
  define ( 'DB_HOST', 'localhost' );
	define ( 'DB_USER', 'id11793517_taxilik2020' );
	define ( 'DB_PASSWORD', 'Taxilik2021++++' );
	define ( 'DB_NAME', 'id11793517_takilik' );
	$conn=new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}
?>