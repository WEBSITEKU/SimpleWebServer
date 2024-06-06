<?php
session_start();
if (isset($_SESSION["username"]))
    $user = $_SESSION["username"];
else
    header("location:index.php");
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">

    <title>Hello, world!</title>
</head>
<body>
    <nav class="nav nav-pills flex-sm-row bg-primary">
        <a href="dashboard.php" class="flex-sm-fill text-sm-start nav-link text-light">Dashboard</a>
        <span class="nav-link text-warning disabled" aria-current="page">List Produk</span>
        <a href="#" class="flex-sm-fill text-sm-start nav-link text-light disabled">User: <?= $user; ?> </a>
        <a href="logout.php" class="flex-sm-fill text-sm-start nav-link text-light">Logout</a>
    </nav>

    <div class="container">
    <?php
        include "koneksi.php";
        $sql = "SELECT * FROM barang ORDER BY id";
        $hasil = $conn->query($sql);
        echo "<a href='addBrg.php' class='btn btn-success text-white'>Tambah Data</a>";

        if ($hasil->num_rows > 0) {
            echo "<table class='table table-striped'>
                    <thead>
                        <tr>
                            <th scope='col'>ID</th>
                            <th scope='col'>Nama</th>
                            <th scope='col'>Harga</th>
                            <th scope='col'>Stok</th>
                            <th scope='col'>Keterangan</th>
                            <th scope='col'>Foto</th>
                            <th scope='col'>Edit</th>
                            <th scope='col'>Hapus</th>
                        </tr>
                    </thead>
                    <tbody>";
            while ($row = $hasil->fetch_assoc()) {
                $teks = "<tr>";
                $teks .= "<td>" . $row["id"] . "</td>";
                $teks .= "<td>" . $row["nama"] . "</td>";
                $teks .= "<td>" . $row["hrg"] . "</td>";
                $teks .= "<td>" . $row["jml"] . "</td>";
                $teks .= "<td>" . $row["keterangan"] . "</td>";
                $teks .= "<td><img src='img/". $row["foto"] ."' style='width:100px; height:100px;'></img></td>";
                $teks .= "<td><a href='editBrg.php?id=" . $row["id"] . "'>Edit</a></td>";
                $teks .= "<td><a href='delBrg.php?id=" . $row["id"] ."'>Hapus</a></td>";
                $teks .= "</tr>";
                echo $teks;
            }
            echo "</tbody>
                </table>";
        } else {
            echo "jml rec: 0 ";
        }
        $conn->close();
    ?>

    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>