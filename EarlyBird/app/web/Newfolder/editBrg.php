<?php
    include "konek.php";
    $id = $_GET["id"];
    $sql = "SELECT * FROM brg WHERE id='$id'";
    $hasil = $conn -> query($sql);
    while ($row = $hasil -> fetch_assoc()) {
        $nama = $row["nama"];
        $hrg = $row["hrg"];
        $jml = $row["jml"];
        $keterangan = $row["keterangan"];
        $foto = $row["foto"];
    }
?>
<html>
<head></head>
<body>
    <h1>Edit Data Barang</h1>
    <form action="updBrg.php" method="POST" enctype="multipart/form-data">
        ID = <input type="text" name="tid" value="<?= $id; ?>" readonly> <br>
        Nama Barang = <input type="text" name="tnama" value="<?= $nama; ?>"> <br>
        harga = <input type="text" name="thrg" value="<?= $hrg; ?>"> <br>
        Jml Stok = <input type="text" name="tjml" value="<?= $jml; ?>"> <br>
        Keterangan = <input type="text" name="tket" value="<?= $keterangan; ?>"> <br>
        Foto = <input type="file" name="foto"> <br>
        <input type="hidden" name="foto_lama" value="<?= $foto; ?>">
        <img src="img/<?php echo $foto; ?>" width="150px" height="120px"> <br>
        <input type="checkbox" name="ubah_foto" value="true" checked>Ceklis jika ingin mengubah foto <br>

        <input type="submit" value="Update">
    </form>
</body>
</html>