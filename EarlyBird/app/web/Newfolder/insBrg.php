<?php
    include "konek.php";
    include "uploadFoto.php";
    $id = $_POST["tid"];
    $nama = $_POST["tnama"];
    $hrg = $_POST["thrg"];
    $ket = $_POST["tket"];
    $jml = $_POST["tjml"];

    if (upload_foto($_FILES["foto"])) {
        $foto = $_FILES["foto"]["name"];
        $sql = "INSERT INTO brg (id,nama,hrg,jml,keterangan,foto) VALUES ('$id', '$nama', $hrg, $jml, '$ket', '$foto')";
        if ($conn -> query($sql) === TRUE) {
            $conn -> close();
            header("location:index.php");
        }
        else {
            $conn -> close();
            echo "New records failed";
        }
    } else {
        echo "Sorry, there was an error uploading your file.";
    }
?>