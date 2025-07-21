-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 21, 2025 at 01:35 AM
-- Server version: 8.4.3
-- PHP Version: 8.2.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `perpustakaan`
--

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id_buku` int NOT NULL,
  `kode_buku` varchar(256) NOT NULL,
  `nama_buku` varchar(265) NOT NULL,
  `nama_penulis` varchar(265) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tgl_terbit` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`id_buku`, `kode_buku`, `nama_buku`, `nama_penulis`, `tgl_terbit`) VALUES
(1, 'B25001', 'Menjelajahi codingan', 'Rifqi Pura', '1999-11-03'),
(2, 'B25002', 'Belajar HTML5', 'Qiqy Qwey', '2001-02-12'),
(3, 'B25003', 'Bebas dan Tenang', 'Izmi Nibilih', '2022-01-01'),
(4, 'B25004', 'Learning By Singing', 'Izmi Nibllih', '2020-05-02'),
(5, 'B25005', 'Kenapa Informatika?', 'Huzna Woqu', '1997-08-15');

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE `member` (
  `id_member` int NOT NULL,
  `nama_member` varchar(265) NOT NULL,
  `kode_member` varchar(256) NOT NULL,
  `alamat` varchar(265) NOT NULL,
  `status` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`id_member`, `nama_member`, `kode_member`, `alamat`, `status`) VALUES
(1, 'ismi nabilah', 'M25001', 'cimahi', 'mahasiswa'),
(2, 'revania zahrani', 'M25002', 'sariasih', 'mahasiswa'),
(3, 'anwaruddin', 'M25003', 'kab bandung barat', 'mahasiswa'),
(4, 'salma syarifah', 'M25004', 'bandung', 'mahasiswa'),
(5, 'rahma aulia', 'M25005', 'mahasiswa', 'cimahi');

-- --------------------------------------------------------

--
-- Table structure for table `pengunjung`
--

CREATE TABLE `pengunjung` (
  `id_pgj` int NOT NULL,
  `nama_pgj` varchar(265) NOT NULL,
  `alamat` varchar(265) NOT NULL,
  `status` varchar(256) NOT NULL,
  `ket_pgj` varchar(256) NOT NULL,
  `tgl_pgj` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pengunjung`
--

INSERT INTO `pengunjung` (`id_pgj`, `nama_pgj`, `alamat`, `status`, `ket_pgj`, `tgl_pgj`) VALUES
(1, 'ismi nabilah', 'cimahi', 'mahasiswa', 'belajar', '2025-07-04'),
(2, 'salma syarifah', 'bandung', 'mahasiswa', 'meminjam buku', '2025-07-05'),
(3, 'anwaruddin', 'kab bandung barat', 'mahasiswa', 'meminjam buku', '2025-07-05'),
(4, 'rahma aulia', 'cimahi', 'mahasiswa', 'belajar dan meminjam buku', '2025-07-05'),
(5, 'revania zahra', 'sariasih', 'mahasiswa', 'meminjam buku', '2025-07-05'),
(6, 'Zahra sabila', 'Bojongsoang', 'mahasiswa', 'belajar', '2025-07-07');

-- --------------------------------------------------------

--
-- Table structure for table `pinjam`
--

CREATE TABLE `pinjam` (
  `id_pinjam` int NOT NULL,
  `id_member` int NOT NULL,
  `id_buku` int NOT NULL,
  `nama_member` varchar(256) NOT NULL,
  `kode_member` varchar(256) NOT NULL,
  `nama_buku` varchar(256) NOT NULL,
  `status` enum('sudah dikembalikan','Belum dikembalikan') NOT NULL,
  `tgl_pinjam` date DEFAULT NULL,
  `tgl_kembali` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `pinjam`
--

INSERT INTO `pinjam` (`id_pinjam`, `id_member`, `id_buku`, `nama_member`, `kode_member`, `nama_buku`, `status`, `tgl_pinjam`, `tgl_kembali`) VALUES
(1, 1, 1, 'ismi nabilah', 'M25001', 'Menjelajahi codingan', 'Belum dikembalikan', '2025-07-05', NULL),
(2, 3, 5, 'anwaruddin', 'M25003', 'Kenapa Informatika?', 'Belum dikembalikan', '2025-07-05', NULL),
(3, 2, 3, 'revania zahrani', 'M25002', 'Bebas dan Tenang', 'Belum dikembalikan', '2025-07-05', NULL),
(4, 4, 4, 'salma syarifah', 'M25004', 'Learning By Singing', 'Belum dikembalikan', '2025-07-05', NULL),
(5, 1, 2, 'ismi nabilah', 'M25001', 'Belajar HTML5', 'sudah dikembalikan', '2025-07-05', '2025-07-05');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int NOT NULL,
  `nama_user` varchar(256) NOT NULL,
  `username` varchar(265) NOT NULL,
  `password` varchar(265) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `nama_user`, `username`, `password`) VALUES
(1, 'mina', 'admin01', '01001');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id_member`);

--
-- Indexes for table `pengunjung`
--
ALTER TABLE `pengunjung`
  ADD PRIMARY KEY (`id_pgj`);

--
-- Indexes for table `pinjam`
--
ALTER TABLE `pinjam`
  ADD PRIMARY KEY (`id_pinjam`),
  ADD KEY `id_member` (`id_member`,`id_buku`),
  ADD KEY `id_buku` (`id_buku`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `buku`
--
ALTER TABLE `buku`
  MODIFY `id_buku` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `member`
--
ALTER TABLE `member`
  MODIFY `id_member` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pengunjung`
--
ALTER TABLE `pengunjung`
  MODIFY `id_pgj` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pinjam`
--
ALTER TABLE `pinjam`
  MODIFY `id_pinjam` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pinjam`
--
ALTER TABLE `pinjam`
  ADD CONSTRAINT `pinjam_ibfk_1` FOREIGN KEY (`id_member`) REFERENCES `member` (`id_member`),
  ADD CONSTRAINT `pinjam_ibfk_2` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
