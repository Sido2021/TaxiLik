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
