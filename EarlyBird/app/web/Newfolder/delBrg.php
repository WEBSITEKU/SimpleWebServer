<?php
    include "konek.php";
    $id = $_GET['id'];

    $sql = "SELECT foto FROM brg WHERE id='$id'";
    $hasil = $conn -> query($sql);
    while ($row = $hasil -> fetch_assoc()) {
        $foto = $row["foto"];
    }
    if ($foto != "") {
        unlink("img/" . $foto);
    }
    $sql = "DELETE FROM brg WHERE id='$id'";
    if ($conn -> query($sql) === TRUE) {
        header("location:index.php");
    }
    $conn -> close();
    echo "New records failed";
?>