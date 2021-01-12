# TaxiLik
taxi
playlist for videos gps : https://www.youtube.com/watch?v=eiexkzCI8m8&list=PLcS69DU4tB0Z72nXmp9eEl7GdG0qahEpW&index=1

//service profile
connect.php
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
profile:
<?php
	require_once "connect.php";
$id="noVal";
if(isset($_GET['id'])){
    $id=$_GET['id'];
}
$sql = "SELECT * FROM `users` WHERE id = '$id' ";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
  // output data of each row
  while($row = $result->fetch_assoc()) {
    echo " { 'id': '" . $row["client_id"]."' ,'nom' : '" . $row["firstname"]. "' ,'prenom': '" . $row["lastname"]. "','email': '" . $row["email"]. "','phone': '" . $row["phone"]. "' } <br>";
  }
} else {
  echo "0 results ".$id;
}
$conn->close();
?>
