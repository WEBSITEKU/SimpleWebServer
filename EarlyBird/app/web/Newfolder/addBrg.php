<html>
<head></head>
<body>
    <h1>Input Data Barang</h1>
    <form action="insBrg.php" method="POST" enctype="multipart/form-data">
        ID = <input type="text" name="tid"> <br>
        Nama Barang = <input type="text" name="tnama"> <br>
        harga = <input type="text" name="thrg"> <br>
        Jml Stok = <input type="text" name="tjml"> <br>
        Keterangan = <input type="text" name="tket"> <br>
        Foto = <input type="file" name="foto"> <br>
        <input type="submit" value="Simpan">
    </form>
</body>
</html>