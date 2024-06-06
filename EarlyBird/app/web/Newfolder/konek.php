<?php
$host="localhost";
$user="root";
$pass="";
$db="barang";

$conn = new mysqli($host,$user,$pass,$db);
if(!$conn){
    die("koneksi ke server gagal");
}else{
}
?>