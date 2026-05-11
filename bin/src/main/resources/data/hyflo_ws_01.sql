-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 04, 2026 at 02:50 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hyflo_ws_01`
--

-- --------------------------------------------------------

--
-- Table structure for table `r_t000201_t000203`
--

CREATE TABLE `r_t000201_t000203` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `r_t000201_t000203`
--

INSERT INTO `r_t000201_t000203` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18);

-- --------------------------------------------------------

--
-- Table structure for table `r_t000202_t000201`
--

CREATE TABLE `r_t000202_t000201` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `r_t000202_t000201`
--

INSERT INTO `r_t000202_t000201` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `r_t000202_t000203`
--

CREATE TABLE `r_t000202_t000203` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `r_t000202_t000203`
--

INSERT INTO `r_t000202_t000203` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(1, 11),
(3, 2),
(3, 5),
(3, 8);

-- --------------------------------------------------------

--
-- Table structure for table `r_t000203_t000204`
--

CREATE TABLE `r_t000203_t000204` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `r_t000203_t000204`
--

INSERT INTO `r_t000203_t000204` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 101),
(1, 102),
(1, 103),
(1, 104),
(1, 105),
(1, 106),
(1, 107),
(1, 108),
(1, 201),
(1, 202),
(1, 203),
(1, 204),
(1, 205),
(1, 206),
(1, 207),
(1, 208),
(1, 301),
(1, 302),
(1, 303),
(1, 304),
(1, 305),
(1, 306),
(1, 307),
(1, 308),
(1, 401),
(1, 402),
(1, 403),
(1, 404),
(1, 405),
(1, 406),
(1, 407),
(1, 408),
(1, 501),
(1, 502),
(1, 503),
(1, 504),
(1, 505),
(1, 506),
(1, 507),
(1, 508),
(1, 601),
(1, 602),
(1, 603),
(1, 604),
(1, 605),
(1, 606),
(1, 607),
(1, 608),
(1, 701),
(1, 702),
(1, 703),
(1, 704),
(1, 705),
(1, 706),
(1, 707),
(1, 708),
(2, 9),
(2, 10),
(2, 11),
(2, 12),
(2, 13),
(2, 14),
(2, 15),
(2, 16),
(2, 17),
(2, 18),
(2, 19),
(2, 20),
(3, 221),
(3, 222),
(3, 223),
(3, 224),
(3, 225),
(3, 226),
(3, 227),
(3, 228),
(3, 229),
(3, 230),
(3, 231),
(3, 232),
(3, 233),
(3, 234),
(3, 235),
(3, 236),
(3, 237),
(3, 238),
(3, 239),
(3, 240),
(3, 241),
(3, 242),
(3, 243),
(3, 244),
(3, 321),
(3, 322),
(3, 323),
(3, 324),
(3, 325),
(3, 326),
(3, 327),
(3, 328),
(3, 329),
(3, 330),
(3, 331),
(3, 332),
(3, 333),
(3, 334),
(3, 335),
(3, 336),
(3, 337),
(3, 338),
(3, 339),
(3, 340),
(3, 341),
(3, 342),
(3, 343),
(3, 344),
(3, 421),
(3, 422),
(3, 423),
(3, 424),
(3, 425),
(3, 426),
(3, 427),
(3, 428),
(3, 429),
(3, 430),
(3, 431),
(3, 432),
(3, 433),
(3, 434),
(3, 435),
(3, 436),
(3, 437),
(3, 438),
(3, 439),
(3, 440),
(3, 441),
(3, 442),
(3, 443),
(3, 444),
(3, 521),
(3, 522),
(3, 523),
(3, 524),
(3, 525),
(3, 526),
(3, 527),
(3, 528),
(3, 529),
(3, 530),
(3, 531),
(3, 532),
(3, 533),
(3, 534),
(3, 535),
(3, 536),
(3, 537),
(3, 538),
(3, 539),
(3, 540),
(3, 541),
(3, 542),
(3, 543),
(3, 544),
(3, 621),
(3, 622),
(3, 623),
(3, 624),
(3, 625),
(3, 626),
(3, 627),
(3, 628),
(3, 629),
(3, 630),
(3, 631),
(3, 632),
(3, 633),
(3, 634),
(3, 635),
(3, 636),
(3, 637),
(3, 638),
(3, 639),
(3, 640),
(3, 641),
(3, 642),
(3, 643),
(3, 644),
(4, 121),
(4, 122),
(4, 123),
(4, 124),
(4, 125),
(4, 126),
(4, 127),
(4, 128),
(4, 129),
(4, 130),
(4, 131),
(4, 132),
(4, 133),
(4, 134),
(4, 135),
(4, 136),
(4, 137),
(4, 138),
(4, 139),
(4, 140),
(4, 141),
(4, 142),
(4, 143),
(4, 144),
(4, 721),
(4, 722),
(4, 723),
(4, 724),
(4, 725),
(4, 726),
(4, 727),
(4, 728),
(4, 729),
(4, 730),
(4, 731),
(4, 732),
(4, 733),
(4, 734),
(4, 735),
(4, 736),
(4, 737),
(4, 738),
(4, 739),
(4, 740),
(4, 741),
(4, 742),
(4, 743),
(4, 744),
(5, 21),
(5, 22),
(5, 23),
(5, 24),
(5, 25),
(5, 26),
(5, 27),
(5, 28),
(5, 29),
(5, 30),
(5, 31),
(5, 32),
(5, 33),
(5, 34),
(5, 35),
(5, 36),
(5, 37),
(5, 38),
(5, 39),
(5, 40),
(5, 41),
(5, 42),
(5, 43),
(5, 44),
(6, 209),
(6, 210),
(6, 211),
(6, 212),
(6, 213),
(6, 214),
(6, 215),
(6, 216),
(6, 217),
(6, 218),
(6, 219),
(6, 220),
(6, 309),
(6, 310),
(6, 311),
(6, 312),
(6, 313),
(6, 314),
(6, 315),
(6, 316),
(6, 317),
(6, 318),
(6, 319),
(6, 320),
(6, 409),
(6, 410),
(6, 411),
(6, 412),
(6, 413),
(6, 414),
(6, 415),
(6, 416),
(6, 417),
(6, 418),
(6, 419),
(6, 420),
(6, 509),
(6, 510),
(6, 511),
(6, 512),
(6, 513),
(6, 514),
(6, 515),
(6, 516),
(6, 517),
(6, 518),
(6, 519),
(6, 520),
(6, 609),
(6, 610),
(6, 611),
(6, 612),
(6, 613),
(6, 614),
(6, 615),
(6, 616),
(6, 617),
(6, 618),
(6, 619),
(6, 620),
(7, 109),
(7, 110),
(7, 111),
(7, 112),
(7, 113),
(7, 114),
(7, 115),
(7, 116),
(7, 117),
(7, 118),
(7, 119),
(7, 120),
(7, 709),
(7, 710),
(7, 711),
(7, 712),
(7, 713),
(7, 714),
(7, 715),
(7, 716),
(7, 717),
(7, 718),
(7, 719),
(7, 720),
(8, 45),
(8, 46),
(8, 47),
(8, 48),
(8, 49),
(8, 50),
(8, 51),
(8, 52),
(8, 53),
(8, 54),
(8, 55),
(8, 56),
(8, 57),
(8, 58),
(8, 59),
(8, 345),
(8, 346),
(8, 347),
(8, 348),
(8, 349),
(8, 350),
(8, 351),
(8, 352),
(8, 353),
(8, 445),
(8, 446),
(8, 447),
(8, 448),
(8, 449),
(8, 450),
(8, 451),
(8, 452),
(8, 453),
(8, 545),
(8, 546),
(8, 547),
(8, 548),
(8, 549),
(8, 550),
(8, 551),
(8, 552),
(8, 553),
(8, 645),
(8, 646),
(8, 647),
(8, 648),
(8, 649),
(8, 650),
(8, 651),
(8, 652),
(8, 653),
(9, 245),
(9, 246),
(9, 247),
(9, 248),
(9, 249),
(9, 250),
(9, 251),
(9, 252),
(9, 253),
(9, 254),
(9, 255),
(9, 256),
(9, 257),
(9, 258),
(9, 259),
(9, 354),
(9, 355),
(9, 356),
(9, 357),
(9, 358),
(9, 359),
(9, 454),
(9, 455),
(9, 456),
(9, 457),
(9, 458),
(9, 459),
(10, 554),
(10, 555),
(10, 556),
(10, 557),
(10, 558),
(10, 559),
(10, 654),
(10, 655),
(10, 656),
(10, 657),
(10, 658),
(10, 659),
(11, 145),
(11, 146),
(11, 147),
(11, 148),
(11, 149),
(11, 150),
(11, 151),
(11, 152),
(11, 153),
(11, 154),
(11, 155),
(11, 156),
(11, 157),
(11, 158),
(11, 159),
(11, 745),
(11, 746),
(11, 747),
(11, 748),
(11, 749),
(11, 750),
(11, 751),
(11, 752),
(11, 753),
(11, 754),
(11, 755),
(11, 756),
(11, 757),
(11, 758),
(11, 759);

-- --------------------------------------------------------

--
-- Table structure for table `r_t020303_t020308`
--

CREATE TABLE `r_t020303_t020308` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020304_t020302`
--

CREATE TABLE `r_t020304_t020302` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020304_t020308`
--

CREATE TABLE `r_t020304_t020308` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020305_t020201`
--

CREATE TABLE `r_t020305_t020201` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020305_t020204`
--

CREATE TABLE `r_t020305_t020204` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020305_t020308`
--

CREATE TABLE `r_t020305_t020308` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020306_t020201`
--

CREATE TABLE `r_t020306_t020201` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020306_t020204`
--

CREATE TABLE `r_t020306_t020204` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020308_t010206`
--

CREATE TABLE `r_t020308_t010206` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `r_t020308_t020205`
--

CREATE TABLE `r_t020308_t020205` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_00_00_01`
--

CREATE TABLE `t_00_00_01` (
  `f_00` bigint(20) NOT NULL,
  `f_04` varchar(250) DEFAULT NULL,
  `f_01` varchar(100) NOT NULL,
  `f_03` varchar(20) NOT NULL,
  `f_02` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_00_01_01`
--

CREATE TABLE `t_00_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_04` varchar(20) DEFAULT NULL,
  `f_03` varchar(250) NOT NULL,
  `f_02` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_01_01`
--

INSERT INTO `t_00_01_01` (`f_00`, `f_01`, `f_04`, `f_03`, `f_02`) VALUES
(1, 'jpg', NULL, '2026/01/25/Abir_1ed97891.jpg', 177951),
(2, 'jpg', NULL, '2026/01/25/Abir_12cd2fca.jpg', 177951),
(3, 'jpg', NULL, '2026/02/02/PHOTO-2025-10-19-14-37-50_f2447ffc.jpg', 60748),
(4, 'jpg', NULL, '2026/02/02/PHOTO-2025-10-19-14-37-50_9c87fd58.jpg', 60748);

-- --------------------------------------------------------

--
-- Table structure for table `t_00_02_01`
--

CREATE TABLE `t_00_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_02_01`
--

INSERT INTO `t_00_02_01` (`f_00`, `f_02`, `f_01`) VALUES
(1, 'Group Members has all permissions on data belongs to system component', 'System Administrators'),
(2, 'Group Members has all permissions on data belongs to business component', 'Business Administrators'),
(3, 'Group Members has all permissions on data belongs to common component', 'Common Administrators'),
(4, 'Group Members has all permissions on data belongs to network component', 'Network Administrators'),
(5, 'Group Members has read permission on data belongs to system component', 'System Users'),
(6, 'Group Members has read permission on data belongs to business component', 'Business Users'),
(7, 'Group Members has read permission on data belongs to common component', 'Common Users'),
(8, 'Group Members has read permission on data belongs to network component', 'Network Users');

-- --------------------------------------------------------

--
-- Table structure for table `t_00_02_02`
--

CREATE TABLE `t_00_02_02` (
  `f_00` bigint(20) NOT NULL,
  `f_04` bit(1) NOT NULL,
  `f_05` bit(1) NOT NULL,
  `f_06` bit(1) NOT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_07` bit(1) NOT NULL,
  `f_03` varchar(120) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_08` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_02_02`
--

INSERT INTO `t_00_02_02` (`f_00`, `f_04`, `f_05`, `f_06`, `f_02`, `f_07`, `f_03`, `f_01`, `f_08`) VALUES
(1, b'1', b'1', b'1', 'superadmin@iaas.dz', b'1', '$2a$12$Qb3ip1Xu8b1RzshlRkDExO7RzvZ5ZxKD3O3k7zZ0365Li/Qkip6Om', 'superadmin', 1),
(2, b'1', b'1', b'1', 'admin@iaas.dz', b'1', '$2a$12$8zK8mN5pQ2rT7vW3xY4uZ0aB1cD2eF3gH4iJ5kL6mN7oP8qR9sT0', 'admin', NULL),
(3, b'1', b'1', b'1', 'business.manager@iaas.dz', b'1', '$2a$10$cvEC9IyZncaahnGwaVWVNunPrUqDn0a3sAhfUWGvLM4GjjqXxWv2a', 'business.manager', NULL),
(4, b'1', b'1', b'1', 'common.manager@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'common.manager', NULL),
(5, b'1', b'1', b'1', 'network.manager@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'network.manager', NULL),
(6, b'1', b'1', b'1', 'manager@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'manager', NULL),
(7, b'1', b'1', b'1', 'officer1@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'procurement.officer1', NULL),
(8, b'1', b'1', b'1', 'officer2@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'procurement.officer2', NULL),
(9, b'1', b'1', b'1', 'officer3@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'procurement.officer3', NULL),
(10, b'1', b'1', b'1', 'contract.officer@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'contract.officer', NULL),
(11, b'1', b'1', b'1', 'auditor@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'auditor', NULL),
(12, b'1', b'1', b'1', 'analyst@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'analyst', NULL),
(13, b'1', b'1', b'1', 'approver@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'approver', NULL),
(14, b'1', b'1', b'1', 'viewer@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'viewer', NULL),
(15, b'0', b'1', b'1', 'disabled@iaas.dz', b'0', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'disabled.user', NULL),
(16, b'1', b'0', b'1', 'locked@iaas.dz', b'0', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'locked.user', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `t_00_02_03`
--

CREATE TABLE `t_00_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_02_03`
--

INSERT INTO `t_00_02_03` (`f_00`, `f_02`, `f_01`) VALUES
(1, 'Manage system (Security, Settings, Audit)', 'SYSTEM_ADMIN'),
(2, 'Manage infrastructure (type, common and core infrastructure)', 'INFRASTRUCTURE_ADMIN'),
(3, 'Edit infrastructure (common and core infrastructure)', 'INFRASTRUCTURE_EDITOR'),
(4, 'View infrastructure data', 'INFRASTRUCTURE_CONSULTANT'),
(5, 'Manage organization (type, common and core organization and localization)', 'ORGANIZATION_ADMIN'),
(6, 'Edit organization (common and core organization and localization)', 'ORGANIZATION_EDITOR'),
(7, 'View organization data', 'ORGANIZATION_CONSULTANT'),
(8, 'Manage flow monitoring process', 'MONITORING_ADMIN'),
(9, 'Validate flow monitoring readings', 'MONITORING_VALIDATOR'),
(10, 'Record flow monitoring data', 'MONITORING_OPERATOR'),
(11, 'View flow monitoring data and statistics', 'MONITORING_CONSULTANT');

-- --------------------------------------------------------

--
-- Table structure for table `t_00_02_04`
--

CREATE TABLE `t_00_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_04` varchar(20) DEFAULT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(100) NOT NULL,
  `f_03` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_02_04`
--

INSERT INTO `t_00_02_04` (`f_00`, `f_04`, `f_02`, `f_01`, `f_03`) VALUES
(1, 'MANAGE', 'parameter management', 'PARAMETER:MANAGE', 'PARAMETER'),
(2, 'MANAGE', 'file management', 'FILE:MANAGE', 'FILE'),
(3, 'MANAGE', 'group management', 'GROUP:MANAGE', 'GROUP'),
(4, 'MANAGE', 'user management', 'USER:MANAGE', 'USER'),
(5, 'MANAGE', 'role management', 'ROLE:MANAGE', 'ROLE'),
(6, 'MANAGE', 'permission management', 'PERMISSION:MANAGE', 'PERMISSION'),
(7, 'MANAGE', 'authority management', 'AUTHORITY:MANAGE', 'AUTHORITY'),
(8, 'MANAGE', 'audit management', 'AUDIT:MANAGE', 'AUDIT'),
(9, 'MANAGE', 'structure_type management', 'STRUCTURE_TYPE:MANAGE', 'STRUCTURE_TYPE'),
(10, 'MANAGE', 'zone management', 'ZONE:MANAGE', 'ZONE'),
(11, 'MANAGE', 'country management', 'COUNTRY:MANAGE', 'COUNTRY'),
(12, 'MANAGE', 'state management', 'STATE:MANAGE', 'STATE'),
(13, 'MANAGE', 'district management', 'DISTRICT:MANAGE', 'DISTRICT'),
(14, 'MANAGE', 'locality management', 'LOCALITY:MANAGE', 'LOCALITY'),
(15, 'MANAGE', 'location management', 'LOCATION:MANAGE', 'LOCATION'),
(16, 'MANAGE', 'coordinate management', 'COORDINATE:MANAGE', 'COORDINATE'),
(17, 'MANAGE', 'structure management', 'STRUCTURE:MANAGE', 'STRUCTURE'),
(18, 'MANAGE', 'job management', 'JOB:MANAGE', 'JOB'),
(19, 'MANAGE', 'person management', 'PERSON:MANAGE', 'PERSON'),
(20, 'MANAGE', 'employee management', 'EMPLOYEE:MANAGE', 'EMPLOYEE'),
(21, 'MANAGE', 'company_type management', 'COMPANY_TYPE:MANAGE', 'COMPANY_TYPE'),
(22, 'MANAGE', 'partner_type management', 'PARTNER_TYPE:MANAGE', 'PARTNER_TYPE'),
(23, 'MANAGE', 'vendor_type management', 'VENDOR_TYPE:MANAGE', 'VENDOR_TYPE'),
(24, 'MANAGE', 'facility_type management', 'FACILITY_TYPE:MANAGE', 'FACILITY_TYPE'),
(25, 'MANAGE', 'station_type management', 'STATION_TYPE:MANAGE', 'STATION_TYPE'),
(26, 'MANAGE', 'terminal_type management', 'TERMINAL_TYPE:MANAGE', 'TERMINAL_TYPE'),
(27, 'MANAGE', 'processing_plant_type management', 'PROCESSING_PLANT_TYPE:MANAGE', 'PROCESSING_PLANT_TYPE'),
(28, 'MANAGE', 'production_field_type management', 'PRODUCTION_FIELD_TYPE:MANAGE', 'PRODUCTION_FIELD_TYPE'),
(29, 'MANAGE', 'equipment_type management', 'EQUIPMENT_TYPE:MANAGE', 'EQUIPMENT_TYPE'),
(30, 'MANAGE', 'product management', 'PRODUCT:MANAGE', 'PRODUCT'),
(31, 'MANAGE', 'operational_status management', 'OPERATIONAL_STATUS:MANAGE', 'OPERATIONAL_STATUS'),
(32, 'MANAGE', 'alloy management', 'ALLOY:MANAGE', 'ALLOY'),
(33, 'MANAGE', 'partner management', 'PARTNER:MANAGE', 'PARTNER'),
(34, 'MANAGE', 'vendor management', 'VENDOR:MANAGE', 'VENDOR'),
(35, 'MANAGE', 'infrastructure management', 'INFRASTRUCTURE:MANAGE', 'INFRASTRUCTURE'),
(36, 'MANAGE', 'facility management', 'FACILITY:MANAGE', 'FACILITY'),
(37, 'MANAGE', 'pipeline_system management', 'PIPELINE_SYSTEM:MANAGE', 'PIPELINE_SYSTEM'),
(38, 'MANAGE', 'pipeline management', 'PIPELINE:MANAGE', 'PIPELINE'),
(39, 'MANAGE', 'pipeline_segment management', 'PIPELINE_SEGMENT:MANAGE', 'PIPELINE_SEGMENT'),
(40, 'MANAGE', 'station management', 'STATION:MANAGE', 'STATION'),
(41, 'MANAGE', 'terminal management', 'TERMINAL:MANAGE', 'TERMINAL'),
(42, 'MANAGE', 'processing_plant management', 'PROCESSING_PLANT:MANAGE', 'PROCESSING_PLANT'),
(43, 'MANAGE', 'production_field management', 'PRODUCTION_FIELD:MANAGE', 'PRODUCTION_FIELD'),
(44, 'MANAGE', 'equipment management', 'EQUIPMENT:MANAGE', 'EQUIPMENT'),
(45, 'MANAGE', 'event_type management', 'EVENT_TYPE:MANAGE', 'EVENT_TYPE'),
(46, 'MANAGE', 'operation_type management', 'OPERATION_TYPE:MANAGE', 'OPERATION_TYPE'),
(47, 'MANAGE', 'event_status management', 'EVENT_STATUS:MANAGE', 'EVENT_STATUS'),
(48, 'MANAGE', 'alert_status management', 'ALERT_STATUS:MANAGE', 'ALERT_STATUS'),
(49, 'MANAGE', 'severity management', 'SEVERITY:MANAGE', 'SEVERITY'),
(50, 'MANAGE', 'data_source management', 'DATA_SOURCE:MANAGE', 'DATA_SOURCE'),
(51, 'MANAGE', 'quality_flag management', 'QUALITY_FLAG:MANAGE', 'QUALITY_FLAG'),
(52, 'MANAGE', 'validation_status management', 'VALIDATION_STATUS:MANAGE', 'VALIDATION_STATUS'),
(53, 'MANAGE', 'reading_slot management', 'READING_SLOT:MANAGE', 'READING_SLOT'),
(54, 'MANAGE', 'flow_operation management', 'FLOW_OPERATION:MANAGE', 'FLOW_OPERATION'),
(55, 'MANAGE', 'flow_forecast management', 'FLOW_FORECAST:MANAGE', 'FLOW_FORECAST'),
(56, 'MANAGE', 'flow_reading management', 'FLOW_READING:MANAGE', 'FLOW_READING'),
(57, 'MANAGE', 'flow_event management', 'FLOW_EVENT:MANAGE', 'FLOW_EVENT'),
(58, 'MANAGE', 'flow_alert management', 'FLOW_ALERT:MANAGE', 'FLOW_ALERT'),
(59, 'MANAGE', 'flow_threshold management', 'FLOW_THRESHOLD:MANAGE', 'FLOW_THRESHOLD'),
(101, 'EXECUTE', 'parameter execution', 'PARAMETER:EXECUTE', 'PARAMETER'),
(102, 'EXECUTE', 'file execution', 'FILE:EXECUTE', 'FILE'),
(103, 'EXECUTE', 'group execution', 'GROUP:EXECUTE', 'GROUP'),
(104, 'EXECUTE', 'user execution', 'USER:EXECUTE', 'USER'),
(105, 'EXECUTE', 'role execution', 'ROLE:EXECUTE', 'ROLE'),
(106, 'EXECUTE', 'permission execution', 'PERMISSION:EXECUTE', 'PERMISSION'),
(107, 'EXECUTE', 'authority execution', 'AUTHORITY:EXECUTE', 'AUTHORITY'),
(108, 'EXECUTE', 'audit execution', 'AUDIT:EXECUTE', 'AUDIT'),
(109, 'EXECUTE', 'structure_type execution', 'STRUCTURE_TYPE:EXECUTE', 'STRUCTURE_TYPE'),
(110, 'EXECUTE', 'zone execution', 'ZONE:EXECUTE', 'ZONE'),
(111, 'EXECUTE', 'country execution', 'COUNTRY:EXECUTE', 'COUNTRY'),
(112, 'EXECUTE', 'state execution', 'STATE:EXECUTE', 'STATE'),
(113, 'EXECUTE', 'district execution', 'DISTRICT:EXECUTE', 'DISTRICT'),
(114, 'EXECUTE', 'locality execution', 'LOCALITY:EXECUTE', 'LOCALITY'),
(115, 'EXECUTE', 'location execution', 'LOCATION:EXECUTE', 'LOCATION'),
(116, 'EXECUTE', 'coordinate execution', 'COORDINATE:EXECUTE', 'COORDINATE'),
(117, 'EXECUTE', 'structure execution', 'STRUCTURE:EXECUTE', 'STRUCTURE'),
(118, 'EXECUTE', 'job execution', 'JOB:EXECUTE', 'JOB'),
(119, 'EXECUTE', 'person execution', 'PERSON:EXECUTE', 'PERSON'),
(120, 'EXECUTE', 'employee execution', 'EMPLOYEE:EXECUTE', 'EMPLOYEE'),
(121, 'EXECUTE', 'company_type execution', 'COMPANY_TYPE:EXECUTE', 'COMPANY_TYPE'),
(122, 'EXECUTE', 'partner_type execution', 'PARTNER_TYPE:EXECUTE', 'PARTNER_TYPE'),
(123, 'EXECUTE', 'vendor_type execution', 'VENDOR_TYPE:EXECUTE', 'VENDOR_TYPE'),
(124, 'EXECUTE', 'facility_type execution', 'FACILITY_TYPE:EXECUTE', 'FACILITY_TYPE'),
(125, 'EXECUTE', 'station_type execution', 'STATION_TYPE:EXECUTE', 'STATION_TYPE'),
(126, 'EXECUTE', 'terminal_type execution', 'TERMINAL_TYPE:EXECUTE', 'TERMINAL_TYPE'),
(127, 'EXECUTE', 'processing_plant_type execution', 'PROCESSING_PLANT_TYPE:EXECUTE', 'PROCESSING_PLANT_TYPE'),
(128, 'EXECUTE', 'production_field_type execution', 'PRODUCTION_FIELD_TYPE:EXECUTE', 'PRODUCTION_FIELD_TYPE'),
(129, 'EXECUTE', 'equipment_type execution', 'EQUIPMENT_TYPE:EXECUTE', 'EQUIPMENT_TYPE'),
(130, 'EXECUTE', 'product execution', 'PRODUCT:EXECUTE', 'PRODUCT'),
(131, 'EXECUTE', 'operational_status execution', 'OPERATIONAL_STATUS:EXECUTE', 'OPERATIONAL_STATUS'),
(132, 'EXECUTE', 'alloy execution', 'ALLOY:EXECUTE', 'ALLOY'),
(133, 'EXECUTE', 'partner execution', 'PARTNER:EXECUTE', 'PARTNER'),
(134, 'EXECUTE', 'vendor execution', 'VENDOR:EXECUTE', 'VENDOR'),
(135, 'EXECUTE', 'infrastructure execution', 'INFRASTRUCTURE:EXECUTE', 'INFRASTRUCTURE'),
(136, 'EXECUTE', 'facility execution', 'FACILITY:EXECUTE', 'FACILITY'),
(137, 'EXECUTE', 'pipeline_system execution', 'PIPELINE_SYSTEM:EXECUTE', 'PIPELINE_SYSTEM'),
(138, 'EXECUTE', 'pipeline execution', 'PIPELINE:EXECUTE', 'PIPELINE'),
(139, 'EXECUTE', 'pipeline_segment execution', 'PIPELINE_SEGMENT:EXECUTE', 'PIPELINE_SEGMENT'),
(140, 'EXECUTE', 'station execution', 'STATION:EXECUTE', 'STATION'),
(141, 'EXECUTE', 'terminal execution', 'TERMINAL:EXECUTE', 'TERMINAL'),
(142, 'EXECUTE', 'processing_plant execution', 'PROCESSING_PLANT:EXECUTE', 'PROCESSING_PLANT'),
(143, 'EXECUTE', 'production_field execution', 'PRODUCTION_FIELD:EXECUTE', 'PRODUCTION_FIELD'),
(144, 'EXECUTE', 'equipment execution', 'EQUIPMENT:EXECUTE', 'EQUIPMENT'),
(145, 'EXECUTE', 'event_type execution', 'EVENT_TYPE:EXECUTE', 'EVENT_TYPE'),
(146, 'EXECUTE', 'operation_type execution', 'OPERATION_TYPE:EXECUTE', 'OPERATION_TYPE'),
(147, 'EXECUTE', 'event_status execution', 'EVENT_STATUS:EXECUTE', 'EVENT_STATUS'),
(148, 'EXECUTE', 'alert_status execution', 'ALERT_STATUS:EXECUTE', 'ALERT_STATUS'),
(149, 'EXECUTE', 'severity execution', 'SEVERITY:EXECUTE', 'SEVERITY'),
(150, 'EXECUTE', 'data_source execution', 'DATA_SOURCE:EXECUTE', 'DATA_SOURCE'),
(151, 'EXECUTE', 'quality_flag execution', 'QUALITY_FLAG:EXECUTE', 'QUALITY_FLAG'),
(152, 'EXECUTE', 'validation_status execution', 'VALIDATION_STATUS:EXECUTE', 'VALIDATION_STATUS'),
(153, 'EXECUTE', 'reading_slot execution', 'READING_SLOT:EXECUTE', 'READING_SLOT'),
(154, 'EXECUTE', 'flow_operation execution', 'FLOW_OPERATION:EXECUTE', 'FLOW_OPERATION'),
(155, 'EXECUTE', 'flow_forecast execution', 'FLOW_FORECAST:EXECUTE', 'FLOW_FORECAST'),
(156, 'EXECUTE', 'flow_reading execution', 'FLOW_READING:EXECUTE', 'FLOW_READING'),
(157, 'EXECUTE', 'flow_event execution', 'FLOW_EVENT:EXECUTE', 'FLOW_EVENT'),
(158, 'EXECUTE', 'flow_alert execution', 'FLOW_ALERT:EXECUTE', 'FLOW_ALERT'),
(159, 'EXECUTE', 'flow_threshold execution', 'FLOW_THRESHOLD:EXECUTE', 'FLOW_THRESHOLD'),
(201, 'APPROVE', 'parameter approval', 'PARAMETER:APPROVE', 'PARAMETER'),
(202, 'APPROVE', 'file approval', 'FILE:APPROVE', 'FILE'),
(203, 'APPROVE', 'group approval', 'GROUP:APPROVE', 'GROUP'),
(204, 'APPROVE', 'user approval', 'USER:APPROVE', 'USER'),
(205, 'APPROVE', 'role approval', 'ROLE:APPROVE', 'ROLE'),
(206, 'APPROVE', 'permission approval', 'PERMISSION:APPROVE', 'PERMISSION'),
(207, 'APPROVE', 'authority approval', 'AUTHORITY:APPROVE', 'AUTHORITY'),
(208, 'APPROVE', 'audit approval', 'AUDIT:APPROVE', 'AUDIT'),
(209, 'APPROVE', 'structure_type approval', 'STRUCTURE_TYPE:APPROVE', 'STRUCTURE_TYPE'),
(210, 'APPROVE', 'zone approval', 'ZONE:APPROVE', 'ZONE'),
(211, 'APPROVE', 'country approval', 'COUNTRY:APPROVE', 'COUNTRY'),
(212, 'APPROVE', 'state approval', 'STATE:APPROVE', 'STATE'),
(213, 'APPROVE', 'district approval', 'DISTRICT:APPROVE', 'DISTRICT'),
(214, 'APPROVE', 'locality approval', 'LOCALITY:APPROVE', 'LOCALITY'),
(215, 'APPROVE', 'location approval', 'LOCATION:APPROVE', 'LOCATION'),
(216, 'APPROVE', 'coordinate approval', 'COORDINATE:APPROVE', 'COORDINATE'),
(217, 'APPROVE', 'structure approval', 'STRUCTURE:APPROVE', 'STRUCTURE'),
(218, 'APPROVE', 'job approval', 'JOB:APPROVE', 'JOB'),
(219, 'APPROVE', 'person approval', 'PERSON:APPROVE', 'PERSON'),
(220, 'APPROVE', 'employee approval', 'EMPLOYEE:APPROVE', 'EMPLOYEE'),
(221, 'APPROVE', 'company_type approval', 'COMPANY_TYPE:APPROVE', 'COMPANY_TYPE'),
(222, 'APPROVE', 'partner_type approval', 'PARTNER_TYPE:APPROVE', 'PARTNER_TYPE'),
(223, 'APPROVE', 'vendor_type approval', 'VENDOR_TYPE:APPROVE', 'VENDOR_TYPE'),
(224, 'APPROVE', 'facility_type approval', 'FACILITY_TYPE:APPROVE', 'FACILITY_TYPE'),
(225, 'APPROVE', 'station_type approval', 'STATION_TYPE:APPROVE', 'STATION_TYPE'),
(226, 'APPROVE', 'terminal_type approval', 'TERMINAL_TYPE:APPROVE', 'TERMINAL_TYPE'),
(227, 'APPROVE', 'processing_plant_type approval', 'PROCESSING_PLANT_TYPE:APPROVE', 'PROCESSING_PLANT_TYPE'),
(228, 'APPROVE', 'production_field_type approval', 'PRODUCTION_FIELD_TYPE:APPROVE', 'PRODUCTION_FIELD_TYPE'),
(229, 'APPROVE', 'equipment_type approval', 'EQUIPMENT_TYPE:APPROVE', 'EQUIPMENT_TYPE'),
(230, 'APPROVE', 'product approval', 'PRODUCT:APPROVE', 'PRODUCT'),
(231, 'APPROVE', 'operational_status approval', 'OPERATIONAL_STATUS:APPROVE', 'OPERATIONAL_STATUS'),
(232, 'APPROVE', 'alloy approval', 'ALLOY:APPROVE', 'ALLOY'),
(233, 'APPROVE', 'partner approval', 'PARTNER:APPROVE', 'PARTNER'),
(234, 'APPROVE', 'vendor approval', 'VENDOR:APPROVE', 'VENDOR'),
(235, 'APPROVE', 'infrastructure approval', 'INFRASTRUCTURE:APPROVE', 'INFRASTRUCTURE'),
(236, 'APPROVE', 'facility approval', 'FACILITY:APPROVE', 'FACILITY'),
(237, 'APPROVE', 'pipeline_system approval', 'PIPELINE_SYSTEM:APPROVE', 'PIPELINE_SYSTEM'),
(238, 'APPROVE', 'pipeline approval', 'PIPELINE:APPROVE', 'PIPELINE'),
(239, 'APPROVE', 'pipeline_segment approval', 'PIPELINE_SEGMENT:APPROVE', 'PIPELINE_SEGMENT'),
(240, 'APPROVE', 'station approval', 'STATION:APPROVE', 'STATION'),
(241, 'APPROVE', 'terminal approval', 'TERMINAL:APPROVE', 'TERMINAL'),
(242, 'APPROVE', 'processing_plant approval', 'PROCESSING_PLANT:APPROVE', 'PROCESSING_PLANT'),
(243, 'APPROVE', 'production_field approval', 'PRODUCTION_FIELD:APPROVE', 'PRODUCTION_FIELD'),
(244, 'APPROVE', 'equipment approval', 'EQUIPMENT:APPROVE', 'EQUIPMENT'),
(245, 'APPROVE', 'event_type approval', 'EVENT_TYPE:APPROVE', 'EVENT_TYPE'),
(246, 'APPROVE', 'operation_type approval', 'OPERATION_TYPE:APPROVE', 'OPERATION_TYPE'),
(247, 'APPROVE', 'event_status approval', 'EVENT_STATUS:APPROVE', 'EVENT_STATUS'),
(248, 'APPROVE', 'alert_status approval', 'ALERT_STATUS:APPROVE', 'ALERT_STATUS'),
(249, 'APPROVE', 'severity approval', 'SEVERITY:APPROVE', 'SEVERITY'),
(250, 'APPROVE', 'data_source approval', 'DATA_SOURCE:APPROVE', 'DATA_SOURCE'),
(251, 'APPROVE', 'quality_flag approval', 'QUALITY_FLAG:APPROVE', 'QUALITY_FLAG'),
(252, 'APPROVE', 'validation_status approval', 'VALIDATION_STATUS:APPROVE', 'VALIDATION_STATUS'),
(253, 'APPROVE', 'reading_slot approval', 'READING_SLOT:APPROVE', 'READING_SLOT'),
(254, 'APPROVE', 'flow_operation approval', 'FLOW_OPERATION:APPROVE', 'FLOW_OPERATION'),
(255, 'APPROVE', 'flow_forecast approval', 'FLOW_FORECAST:APPROVE', 'FLOW_FORECAST'),
(256, 'APPROVE', 'flow_reading approval', 'FLOW_READING:APPROVE', 'FLOW_READING'),
(257, 'APPROVE', 'flow_event approval', 'FLOW_EVENT:APPROVE', 'FLOW_EVENT'),
(258, 'APPROVE', 'flow_alert approval', 'FLOW_ALERT:APPROVE', 'FLOW_ALERT'),
(259, 'APPROVE', 'flow_threshold approval', 'FLOW_THRESHOLD:APPROVE', 'FLOW_THRESHOLD'),
(301, 'DELETE', 'parameter delete', 'PARAMETER:DELETE', 'PARAMETER'),
(302, 'DELETE', 'file delete', 'FILE:DELETE', 'FILE'),
(303, 'DELETE', 'group delete', 'GROUP:DELETE', 'GROUP'),
(304, 'DELETE', 'user delete', 'USER:DELETE', 'USER'),
(305, 'DELETE', 'role delete', 'ROLE:DELETE', 'ROLE'),
(306, 'DELETE', 'permission delete', 'PERMISSION:DELETE', 'PERMISSION'),
(307, 'DELETE', 'authority delete', 'AUTHORITY:DELETE', 'AUTHORITY'),
(308, 'DELETE', 'audit delete', 'AUDIT:DELETE', 'AUDIT'),
(309, 'DELETE', 'structure_type delete', 'STRUCTURE_TYPE:DELETE', 'STRUCTURE_TYPE'),
(310, 'DELETE', 'zone delete', 'ZONE:DELETE', 'ZONE'),
(311, 'DELETE', 'country delete', 'COUNTRY:DELETE', 'COUNTRY'),
(312, 'DELETE', 'state delete', 'STATE:DELETE', 'STATE'),
(313, 'DELETE', 'district delete', 'DISTRICT:DELETE', 'DISTRICT'),
(314, 'DELETE', 'locality delete', 'LOCALITY:DELETE', 'LOCALITY'),
(315, 'DELETE', 'location delete', 'LOCATION:DELETE', 'LOCATION'),
(316, 'DELETE', 'coordinate delete', 'COORDINATE:DELETE', 'COORDINATE'),
(317, 'DELETE', 'structure delete', 'STRUCTURE:DELETE', 'STRUCTURE'),
(318, 'DELETE', 'job delete', 'JOB:DELETE', 'JOB'),
(319, 'DELETE', 'person delete', 'PERSON:DELETE', 'PERSON'),
(320, 'DELETE', 'employee delete', 'EMPLOYEE:DELETE', 'EMPLOYEE'),
(321, 'DELETE', 'company_type delete', 'COMPANY_TYPE:DELETE', 'COMPANY_TYPE'),
(322, 'DELETE', 'partner_type delete', 'PARTNER_TYPE:DELETE', 'PARTNER_TYPE'),
(323, 'DELETE', 'vendor_type delete', 'VENDOR_TYPE:DELETE', 'VENDOR_TYPE'),
(324, 'DELETE', 'facility_type delete', 'FACILITY_TYPE:DELETE', 'FACILITY_TYPE'),
(325, 'DELETE', 'station_type delete', 'STATION_TYPE:DELETE', 'STATION_TYPE'),
(326, 'DELETE', 'terminal_type delete', 'TERMINAL_TYPE:DELETE', 'TERMINAL_TYPE'),
(327, 'DELETE', 'processing_plant_type delete', 'PROCESSING_PLANT_TYPE:DELETE', 'PROCESSING_PLANT_TYPE'),
(328, 'DELETE', 'production_field_type delete', 'PRODUCTION_FIELD_TYPE:DELETE', 'PRODUCTION_FIELD_TYPE'),
(329, 'DELETE', 'equipment_type delete', 'EQUIPMENT_TYPE:DELETE', 'EQUIPMENT_TYPE'),
(330, 'DELETE', 'product delete', 'PRODUCT:DELETE', 'PRODUCT'),
(331, 'DELETE', 'operational_status delete', 'OPERATIONAL_STATUS:DELETE', 'OPERATIONAL_STATUS'),
(332, 'DELETE', 'alloy delete', 'ALLOY:DELETE', 'ALLOY'),
(333, 'DELETE', 'partner delete', 'PARTNER:DELETE', 'PARTNER'),
(334, 'DELETE', 'vendor delete', 'VENDOR:DELETE', 'VENDOR'),
(335, 'DELETE', 'infrastructure delete', 'INFRASTRUCTURE:DELETE', 'INFRASTRUCTURE'),
(336, 'DELETE', 'facility delete', 'FACILITY:DELETE', 'FACILITY'),
(337, 'DELETE', 'pipeline_system delete', 'PIPELINE_SYSTEM:DELETE', 'PIPELINE_SYSTEM'),
(338, 'DELETE', 'pipeline delete', 'PIPELINE:DELETE', 'PIPELINE'),
(339, 'DELETE', 'pipeline_segment delete', 'PIPELINE_SEGMENT:DELETE', 'PIPELINE_SEGMENT'),
(340, 'DELETE', 'station delete', 'STATION:DELETE', 'STATION'),
(341, 'DELETE', 'terminal delete', 'TERMINAL:DELETE', 'TERMINAL'),
(342, 'DELETE', 'processing_plant delete', 'PROCESSING_PLANT:DELETE', 'PROCESSING_PLANT'),
(343, 'DELETE', 'production_field delete', 'PRODUCTION_FIELD:DELETE', 'PRODUCTION_FIELD'),
(344, 'DELETE', 'equipment delete', 'EQUIPMENT:DELETE', 'EQUIPMENT'),
(345, 'DELETE', 'event_type delete', 'EVENT_TYPE:DELETE', 'EVENT_TYPE'),
(346, 'DELETE', 'operation_type delete', 'OPERATION_TYPE:DELETE', 'OPERATION_TYPE'),
(347, 'DELETE', 'event_status delete', 'EVENT_STATUS:DELETE', 'EVENT_STATUS'),
(348, 'DELETE', 'alert_status delete', 'ALERT_STATUS:DELETE', 'ALERT_STATUS'),
(349, 'DELETE', 'severity delete', 'SEVERITY:DELETE', 'SEVERITY'),
(350, 'DELETE', 'data_source delete', 'DATA_SOURCE:DELETE', 'DATA_SOURCE'),
(351, 'DELETE', 'quality_flag delete', 'QUALITY_FLAG:DELETE', 'QUALITY_FLAG'),
(352, 'DELETE', 'validation_status delete', 'VALIDATION_STATUS:DELETE', 'VALIDATION_STATUS'),
(353, 'DELETE', 'reading_slot delete', 'READING_SLOT:DELETE', 'READING_SLOT'),
(354, 'DELETE', 'flow_operation delete', 'FLOW_OPERATION:DELETE', 'FLOW_OPERATION'),
(355, 'DELETE', 'flow_forecast delete', 'FLOW_FORECAST:DELETE', 'FLOW_FORECAST'),
(356, 'DELETE', 'flow_reading delete', 'FLOW_READING:DELETE', 'FLOW_READING'),
(357, 'DELETE', 'flow_event delete', 'FLOW_EVENT:DELETE', 'FLOW_EVENT'),
(358, 'DELETE', 'flow_alert delete', 'FLOW_ALERT:DELETE', 'FLOW_ALERT'),
(359, 'DELETE', 'flow_threshold delete', 'FLOW_THRESHOLD:DELETE', 'FLOW_THRESHOLD'),
(401, 'UPDATE', 'parameter updating', 'PARAMETER:UPDATE', 'PARAMETER'),
(402, 'UPDATE', 'file updating', 'FILE:UPDATE', 'FILE'),
(403, 'UPDATE', 'group updating', 'GROUP:UPDATE', 'GROUP'),
(404, 'UPDATE', 'user updating', 'USER:UPDATE', 'USER'),
(405, 'UPDATE', 'role updating', 'ROLE:UPDATE', 'ROLE'),
(406, 'UPDATE', 'permission updating', 'PERMISSION:UPDATE', 'PERMISSION'),
(407, 'UPDATE', 'authority updating', 'AUTHORITY:UPDATE', 'AUTHORITY'),
(408, 'UPDATE', 'audit updating', 'AUDIT:UPDATE', 'AUDIT'),
(409, 'UPDATE', 'structure_type updating', 'STRUCTURE_TYPE:UPDATE', 'STRUCTURE_TYPE'),
(410, 'UPDATE', 'zone updating', 'ZONE:UPDATE', 'ZONE'),
(411, 'UPDATE', 'country updating', 'COUNTRY:UPDATE', 'COUNTRY'),
(412, 'UPDATE', 'state updating', 'STATE:UPDATE', 'STATE'),
(413, 'UPDATE', 'district updating', 'DISTRICT:UPDATE', 'DISTRICT'),
(414, 'UPDATE', 'locality updating', 'LOCALITY:UPDATE', 'LOCALITY'),
(415, 'UPDATE', 'location updating', 'LOCATION:UPDATE', 'LOCATION'),
(416, 'UPDATE', 'coordinate updating', 'COORDINATE:UPDATE', 'COORDINATE'),
(417, 'UPDATE', 'structure updating', 'STRUCTURE:UPDATE', 'STRUCTURE'),
(418, 'UPDATE', 'job updating', 'JOB:UPDATE', 'JOB'),
(419, 'UPDATE', 'person updating', 'PERSON:UPDATE', 'PERSON'),
(420, 'UPDATE', 'employee updating', 'EMPLOYEE:UPDATE', 'EMPLOYEE'),
(421, 'UPDATE', 'company_type updating', 'COMPANY_TYPE:UPDATE', 'COMPANY_TYPE'),
(422, 'UPDATE', 'partner_type updating', 'PARTNER_TYPE:UPDATE', 'PARTNER_TYPE'),
(423, 'UPDATE', 'vendor_type updating', 'VENDOR_TYPE:UPDATE', 'VENDOR_TYPE'),
(424, 'UPDATE', 'facility_type updating', 'FACILITY_TYPE:UPDATE', 'FACILITY_TYPE'),
(425, 'UPDATE', 'station_type updating', 'STATION_TYPE:UPDATE', 'STATION_TYPE'),
(426, 'UPDATE', 'terminal_type updating', 'TERMINAL_TYPE:UPDATE', 'TERMINAL_TYPE'),
(427, 'UPDATE', 'processing_plant_type updating', 'PROCESSING_PLANT_TYPE:UPDATE', 'PROCESSING_PLANT_TYPE'),
(428, 'UPDATE', 'production_field_type updating', 'PRODUCTION_FIELD_TYPE:UPDATE', 'PRODUCTION_FIELD_TYPE'),
(429, 'UPDATE', 'equipment_type updating', 'EQUIPMENT_TYPE:UPDATE', 'EQUIPMENT_TYPE'),
(430, 'UPDATE', 'product updating', 'PRODUCT:UPDATE', 'PRODUCT'),
(431, 'UPDATE', 'operational_status updating', 'OPERATIONAL_STATUS:UPDATE', 'OPERATIONAL_STATUS'),
(432, 'UPDATE', 'alloy updating', 'ALLOY:UPDATE', 'ALLOY'),
(433, 'UPDATE', 'partner updating', 'PARTNER:UPDATE', 'PARTNER'),
(434, 'UPDATE', 'vendor updating', 'VENDOR:UPDATE', 'VENDOR'),
(435, 'UPDATE', 'infrastructure updating', 'INFRASTRUCTURE:UPDATE', 'INFRASTRUCTURE'),
(436, 'UPDATE', 'facility updating', 'FACILITY:UPDATE', 'FACILITY'),
(437, 'UPDATE', 'pipeline_system updating', 'PIPELINE_SYSTEM:UPDATE', 'PIPELINE_SYSTEM'),
(438, 'UPDATE', 'pipeline updating', 'PIPELINE:UPDATE', 'PIPELINE'),
(439, 'UPDATE', 'pipeline_segment updating', 'PIPELINE_SEGMENT:UPDATE', 'PIPELINE_SEGMENT'),
(440, 'UPDATE', 'station updating', 'STATION:UPDATE', 'STATION'),
(441, 'UPDATE', 'terminal updating', 'TERMINAL:UPDATE', 'TERMINAL'),
(442, 'UPDATE', 'processing_plant updating', 'PROCESSING_PLANT:UPDATE', 'PROCESSING_PLANT'),
(443, 'UPDATE', 'production_field updating', 'PRODUCTION_FIELD:UPDATE', 'PRODUCTION_FIELD'),
(444, 'UPDATE', 'equipment updating', 'EQUIPMENT:UPDATE', 'EQUIPMENT'),
(445, 'UPDATE', 'event_type updating', 'EVENT_TYPE:UPDATE', 'EVENT_TYPE'),
(446, 'UPDATE', 'operation_type updating', 'OPERATION_TYPE:UPDATE', 'OPERATION_TYPE'),
(447, 'UPDATE', 'event_status updating', 'EVENT_STATUS:UPDATE', 'EVENT_STATUS'),
(448, 'UPDATE', 'alert_status updating', 'ALERT_STATUS:UPDATE', 'ALERT_STATUS'),
(449, 'UPDATE', 'severity updating', 'SEVERITY:UPDATE', 'SEVERITY'),
(450, 'UPDATE', 'data_source updating', 'DATA_SOURCE:UPDATE', 'DATA_SOURCE'),
(451, 'UPDATE', 'quality_flag updating', 'QUALITY_FLAG:UPDATE', 'QUALITY_FLAG'),
(452, 'UPDATE', 'validation_status updating', 'VALIDATION_STATUS:UPDATE', 'VALIDATION_STATUS'),
(453, 'UPDATE', 'reading_slot updating', 'READING_SLOT:UPDATE', 'READING_SLOT'),
(454, 'UPDATE', 'flow_operation updating', 'FLOW_OPERATION:UPDATE', 'FLOW_OPERATION'),
(455, 'UPDATE', 'flow_forecast updating', 'FLOW_FORECAST:UPDATE', 'FLOW_FORECAST'),
(456, 'UPDATE', 'flow_reading updating', 'FLOW_READING:UPDATE', 'FLOW_READING'),
(457, 'UPDATE', 'flow_event updating', 'FLOW_EVENT:UPDATE', 'FLOW_EVENT'),
(458, 'UPDATE', 'flow_alert updating', 'FLOW_ALERT:UPDATE', 'FLOW_ALERT'),
(459, 'UPDATE', 'flow_threshold updating', 'FLOW_THRESHOLD:UPDATE', 'FLOW_THRESHOLD'),
(501, 'CREATE', 'parameter creation', 'PARAMETER:CREATE', 'PARAMETER'),
(502, 'CREATE', 'file creation', 'FILE:CREATE', 'FILE'),
(503, 'CREATE', 'group creation', 'GROUP:CREATE', 'GROUP'),
(504, 'CREATE', 'user creation', 'USER:CREATE', 'USER'),
(505, 'CREATE', 'role creation', 'ROLE:CREATE', 'ROLE'),
(506, 'CREATE', 'permission creation', 'PERMISSION:CREATE', 'PERMISSION'),
(507, 'CREATE', 'authority creation', 'AUTHORITY:CREATE', 'AUTHORITY'),
(508, 'CREATE', 'audit creation', 'AUDIT:CREATE', 'AUDIT'),
(509, 'CREATE', 'structure_type creation', 'STRUCTURE_TYPE:CREATE', 'STRUCTURE_TYPE'),
(510, 'CREATE', 'zone creation', 'ZONE:CREATE', 'ZONE'),
(511, 'CREATE', 'country creation', 'COUNTRY:CREATE', 'COUNTRY'),
(512, 'CREATE', 'state creation', 'STATE:CREATE', 'STATE'),
(513, 'CREATE', 'district creation', 'DISTRICT:CREATE', 'DISTRICT'),
(514, 'CREATE', 'locality creation', 'LOCALITY:CREATE', 'LOCALITY'),
(515, 'CREATE', 'location creation', 'LOCATION:CREATE', 'LOCATION'),
(516, 'CREATE', 'coordinate creation', 'COORDINATE:CREATE', 'COORDINATE'),
(517, 'CREATE', 'structure creation', 'STRUCTURE:CREATE', 'STRUCTURE'),
(518, 'CREATE', 'job creation', 'JOB:CREATE', 'JOB'),
(519, 'CREATE', 'person creation', 'PERSON:CREATE', 'PERSON'),
(520, 'CREATE', 'employee creation', 'EMPLOYEE:CREATE', 'EMPLOYEE'),
(521, 'CREATE', 'company_type creation', 'COMPANY_TYPE:CREATE', 'COMPANY_TYPE'),
(522, 'CREATE', 'partner_type creation', 'PARTNER_TYPE:CREATE', 'PARTNER_TYPE'),
(523, 'CREATE', 'vendor_type creation', 'VENDOR_TYPE:CREATE', 'VENDOR_TYPE'),
(524, 'CREATE', 'facility_type creation', 'FACILITY_TYPE:CREATE', 'FACILITY_TYPE'),
(525, 'CREATE', 'station_type creation', 'STATION_TYPE:CREATE', 'STATION_TYPE'),
(526, 'CREATE', 'terminal_type creation', 'TERMINAL_TYPE:CREATE', 'TERMINAL_TYPE'),
(527, 'CREATE', 'processing_plant_type creation', 'PROCESSING_PLANT_TYPE:CREATE', 'PROCESSING_PLANT_TYPE'),
(528, 'CREATE', 'production_field_type creation', 'PRODUCTION_FIELD_TYPE:CREATE', 'PRODUCTION_FIELD_TYPE'),
(529, 'CREATE', 'equipment_type creation', 'EQUIPMENT_TYPE:CREATE', 'EQUIPMENT_TYPE'),
(530, 'CREATE', 'product creation', 'PRODUCT:CREATE', 'PRODUCT'),
(531, 'CREATE', 'operational_status creation', 'OPERATIONAL_STATUS:CREATE', 'OPERATIONAL_STATUS'),
(532, 'CREATE', 'alloy creation', 'ALLOY:CREATE', 'ALLOY'),
(533, 'CREATE', 'partner creation', 'PARTNER:CREATE', 'PARTNER'),
(534, 'CREATE', 'vendor creation', 'VENDOR:CREATE', 'VENDOR'),
(535, 'CREATE', 'infrastructure creation', 'INFRASTRUCTURE:CREATE', 'INFRASTRUCTURE'),
(536, 'CREATE', 'facility creation', 'FACILITY:CREATE', 'FACILITY'),
(537, 'CREATE', 'pipeline_system creation', 'PIPELINE_SYSTEM:CREATE', 'PIPELINE_SYSTEM'),
(538, 'CREATE', 'pipeline creation', 'PIPELINE:CREATE', 'PIPELINE'),
(539, 'CREATE', 'pipeline_segment creation', 'PIPELINE_SEGMENT:CREATE', 'PIPELINE_SEGMENT'),
(540, 'CREATE', 'station creation', 'STATION:CREATE', 'STATION'),
(541, 'CREATE', 'terminal creation', 'TERMINAL:CREATE', 'TERMINAL'),
(542, 'CREATE', 'processing_plant creation', 'PROCESSING_PLANT:CREATE', 'PROCESSING_PLANT'),
(543, 'CREATE', 'production_field creation', 'PRODUCTION_FIELD:CREATE', 'PRODUCTION_FIELD'),
(544, 'CREATE', 'equipment creation', 'EQUIPMENT:CREATE', 'EQUIPMENT'),
(545, 'CREATE', 'event_type creation', 'EVENT_TYPE:CREATE', 'EVENT_TYPE'),
(546, 'CREATE', 'operation_type creation', 'OPERATION_TYPE:CREATE', 'OPERATION_TYPE'),
(547, 'CREATE', 'event_status creation', 'EVENT_STATUS:CREATE', 'EVENT_STATUS'),
(548, 'CREATE', 'alert_status creation', 'ALERT_STATUS:CREATE', 'ALERT_STATUS'),
(549, 'CREATE', 'severity creation', 'SEVERITY:CREATE', 'SEVERITY'),
(550, 'CREATE', 'data_source creation', 'DATA_SOURCE:CREATE', 'DATA_SOURCE'),
(551, 'CREATE', 'quality_flag creation', 'QUALITY_FLAG:CREATE', 'QUALITY_FLAG'),
(552, 'CREATE', 'validation_status creation', 'VALIDATION_STATUS:CREATE', 'VALIDATION_STATUS'),
(553, 'CREATE', 'reading_slot creation', 'READING_SLOT:CREATE', 'READING_SLOT'),
(554, 'CREATE', 'flow_operation creation', 'FLOW_OPERATION:CREATE', 'FLOW_OPERATION'),
(555, 'CREATE', 'flow_forecast creation', 'FLOW_FORECAST:CREATE', 'FLOW_FORECAST'),
(556, 'CREATE', 'flow_reading creation', 'FLOW_READING:CREATE', 'FLOW_READING'),
(557, 'CREATE', 'flow_event creation', 'FLOW_EVENT:CREATE', 'FLOW_EVENT'),
(558, 'CREATE', 'flow_alert creation', 'FLOW_ALERT:CREATE', 'FLOW_ALERT'),
(559, 'CREATE', 'flow_threshold creation', 'FLOW_THRESHOLD:CREATE', 'FLOW_THRESHOLD'),
(601, 'WRITE', 'parameter editing', 'PARAMETER:WRITE', 'PARAMETER'),
(602, 'WRITE', 'file editing', 'FILE:WRITE', 'FILE'),
(603, 'WRITE', 'group editing', 'GROUP:WRITE', 'GROUP'),
(604, 'WRITE', 'user editing', 'USER:WRITE', 'USER'),
(605, 'WRITE', 'role editing', 'ROLE:WRITE', 'ROLE'),
(606, 'WRITE', 'permission editing', 'PERMISSION:WRITE', 'PERMISSION'),
(607, 'WRITE', 'authority editing', 'AUTHORITY:WRITE', 'AUTHORITY'),
(608, 'WRITE', 'audit editing', 'AUDIT:WRITE', 'AUDIT'),
(609, 'WRITE', 'structure_type editing', 'STRUCTURE_TYPE:WRITE', 'STRUCTURE_TYPE'),
(610, 'WRITE', 'zone editing', 'ZONE:WRITE', 'ZONE'),
(611, 'WRITE', 'country editing', 'COUNTRY:WRITE', 'COUNTRY'),
(612, 'WRITE', 'state editing', 'STATE:WRITE', 'STATE'),
(613, 'WRITE', 'district editing', 'DISTRICT:WRITE', 'DISTRICT'),
(614, 'WRITE', 'locality editing', 'LOCALITY:WRITE', 'LOCALITY'),
(615, 'WRITE', 'location editing', 'LOCATION:WRITE', 'LOCATION'),
(616, 'WRITE', 'coordinate editing', 'COORDINATE:WRITE', 'COORDINATE'),
(617, 'WRITE', 'structure editing', 'STRUCTURE:WRITE', 'STRUCTURE'),
(618, 'WRITE', 'job editing', 'JOB:WRITE', 'JOB'),
(619, 'WRITE', 'person editing', 'PERSON:WRITE', 'PERSON'),
(620, 'WRITE', 'employee editing', 'EMPLOYEE:WRITE', 'EMPLOYEE'),
(621, 'WRITE', 'company_type editing', 'COMPANY_TYPE:WRITE', 'COMPANY_TYPE'),
(622, 'WRITE', 'partner_type editing', 'PARTNER_TYPE:WRITE', 'PARTNER_TYPE'),
(623, 'WRITE', 'vendor_type editing', 'VENDOR_TYPE:WRITE', 'VENDOR_TYPE'),
(624, 'WRITE', 'facility_type editing', 'FACILITY_TYPE:WRITE', 'FACILITY_TYPE'),
(625, 'WRITE', 'station_type editing', 'STATION_TYPE:WRITE', 'STATION_TYPE'),
(626, 'WRITE', 'terminal_type editing', 'TERMINAL_TYPE:WRITE', 'TERMINAL_TYPE'),
(627, 'WRITE', 'processing_plant_type editing', 'PROCESSING_PLANT_TYPE:WRITE', 'PROCESSING_PLANT_TYPE'),
(628, 'WRITE', 'production_field_type editing', 'PRODUCTION_FIELD_TYPE:WRITE', 'PRODUCTION_FIELD_TYPE'),
(629, 'WRITE', 'equipment_type editing', 'EQUIPMENT_TYPE:WRITE', 'EQUIPMENT_TYPE'),
(630, 'WRITE', 'product editing', 'PRODUCT:WRITE', 'PRODUCT'),
(631, 'WRITE', 'operational_status editing', 'OPERATIONAL_STATUS:WRITE', 'OPERATIONAL_STATUS'),
(632, 'WRITE', 'alloy editing', 'ALLOY:WRITE', 'ALLOY'),
(633, 'WRITE', 'partner editing', 'PARTNER:WRITE', 'PARTNER'),
(634, 'WRITE', 'vendor editing', 'VENDOR:WRITE', 'VENDOR'),
(635, 'WRITE', 'infrastructure editing', 'INFRASTRUCTURE:WRITE', 'INFRASTRUCTURE'),
(636, 'WRITE', 'facility editing', 'FACILITY:WRITE', 'FACILITY'),
(637, 'WRITE', 'pipeline_system editing', 'PIPELINE_SYSTEM:WRITE', 'PIPELINE_SYSTEM'),
(638, 'WRITE', 'pipeline editing', 'PIPELINE:WRITE', 'PIPELINE'),
(639, 'WRITE', 'pipeline_segment editing', 'PIPELINE_SEGMENT:WRITE', 'PIPELINE_SEGMENT'),
(640, 'WRITE', 'station editing', 'STATION:WRITE', 'STATION'),
(641, 'WRITE', 'terminal editing', 'TERMINAL:WRITE', 'TERMINAL'),
(642, 'WRITE', 'processing_plant editing', 'PROCESSING_PLANT:WRITE', 'PROCESSING_PLANT'),
(643, 'WRITE', 'production_field editing', 'PRODUCTION_FIELD:WRITE', 'PRODUCTION_FIELD'),
(644, 'WRITE', 'equipment editing', 'EQUIPMENT:WRITE', 'EQUIPMENT'),
(645, 'WRITE', 'event_type editing', 'EVENT_TYPE:WRITE', 'EVENT_TYPE'),
(646, 'WRITE', 'operation_type editing', 'OPERATION_TYPE:WRITE', 'OPERATION_TYPE'),
(647, 'WRITE', 'event_status editing', 'EVENT_STATUS:WRITE', 'EVENT_STATUS'),
(648, 'WRITE', 'alert_status editing', 'ALERT_STATUS:WRITE', 'ALERT_STATUS'),
(649, 'WRITE', 'severity editing', 'SEVERITY:WRITE', 'SEVERITY'),
(650, 'WRITE', 'data_source editing', 'DATA_SOURCE:WRITE', 'DATA_SOURCE'),
(651, 'WRITE', 'quality_flag editing', 'QUALITY_FLAG:WRITE', 'QUALITY_FLAG'),
(652, 'WRITE', 'validation_status editing', 'VALIDATION_STATUS:WRITE', 'VALIDATION_STATUS'),
(653, 'WRITE', 'reading_slot editing', 'READING_SLOT:WRITE', 'READING_SLOT'),
(654, 'WRITE', 'flow_operation editing', 'FLOW_OPERATION:WRITE', 'FLOW_OPERATION'),
(655, 'WRITE', 'flow_forecast editing', 'FLOW_FORECAST:WRITE', 'FLOW_FORECAST'),
(656, 'WRITE', 'flow_reading editing', 'FLOW_READING:WRITE', 'FLOW_READING'),
(657, 'WRITE', 'flow_event editing', 'FLOW_EVENT:WRITE', 'FLOW_EVENT'),
(658, 'WRITE', 'flow_alert editing', 'FLOW_ALERT:WRITE', 'FLOW_ALERT'),
(659, 'WRITE', 'flow_threshold editing', 'FLOW_THRESHOLD:WRITE', 'FLOW_THRESHOLD'),
(701, 'READ', 'parameter viewing', 'PARAMETER:READ', 'PARAMETER'),
(702, 'READ', 'file viewing', 'FILE:READ', 'FILE'),
(703, 'READ', 'group viewing', 'GROUP:READ', 'GROUP'),
(704, 'READ', 'user viewing', 'USER:READ', 'USER'),
(705, 'READ', 'role viewing', 'ROLE:READ', 'ROLE'),
(706, 'READ', 'permission viewing', 'PERMISSION:READ', 'PERMISSION'),
(707, 'READ', 'authority viewing', 'AUTHORITY:READ', 'AUTHORITY'),
(708, 'READ', 'audit viewing', 'AUDIT:READ', 'AUDIT'),
(709, 'READ', 'structure_type viewing', 'STRUCTURE_TYPE:READ', 'STRUCTURE_TYPE'),
(710, 'READ', 'zone viewing', 'ZONE:READ', 'ZONE'),
(711, 'READ', 'country viewing', 'COUNTRY:READ', 'COUNTRY'),
(712, 'READ', 'state viewing', 'STATE:READ', 'STATE'),
(713, 'READ', 'district viewing', 'DISTRICT:READ', 'DISTRICT'),
(714, 'READ', 'locality viewing', 'LOCALITY:READ', 'LOCALITY'),
(715, 'READ', 'location viewing', 'LOCATION:READ', 'LOCATION'),
(716, 'READ', 'coordinate viewing', 'COORDINATE:READ', 'COORDINATE'),
(717, 'READ', 'structure viewing', 'STRUCTURE:READ', 'STRUCTURE'),
(718, 'READ', 'job viewing', 'JOB:READ', 'JOB'),
(719, 'READ', 'person viewing', 'PERSON:READ', 'PERSON'),
(720, 'READ', 'employee viewing', 'EMPLOYEE:READ', 'EMPLOYEE'),
(721, 'READ', 'company_type viewing', 'COMPANY_TYPE:READ', 'COMPANY_TYPE'),
(722, 'READ', 'partner_type viewing', 'PARTNER_TYPE:READ', 'PARTNER_TYPE'),
(723, 'READ', 'vendor_type viewing', 'VENDOR_TYPE:READ', 'VENDOR_TYPE'),
(724, 'READ', 'facility_type viewing', 'FACILITY_TYPE:READ', 'FACILITY_TYPE'),
(725, 'READ', 'station_type viewing', 'STATION_TYPE:READ', 'STATION_TYPE'),
(726, 'READ', 'terminal_type viewing', 'TERMINAL_TYPE:READ', 'TERMINAL_TYPE'),
(727, 'READ', 'processing_plant_type viewing', 'PROCESSING_PLANT_TYPE:READ', 'PROCESSING_PLANT_TYPE'),
(728, 'READ', 'production_field_type viewing', 'PRODUCTION_FIELD_TYPE:READ', 'PRODUCTION_FIELD_TYPE'),
(729, 'READ', 'equipment_type viewing', 'EQUIPMENT_TYPE:READ', 'EQUIPMENT_TYPE'),
(730, 'READ', 'product viewing', 'PRODUCT:READ', 'PRODUCT'),
(731, 'READ', 'operational_status viewing', 'OPERATIONAL_STATUS:READ', 'OPERATIONAL_STATUS'),
(732, 'READ', 'alloy viewing', 'ALLOY:READ', 'ALLOY'),
(733, 'READ', 'partner viewing', 'PARTNER:READ', 'PARTNER'),
(734, 'READ', 'vendor viewing', 'VENDOR:READ', 'VENDOR'),
(735, 'READ', 'infrastructure viewing', 'INFRASTRUCTURE:READ', 'INFRASTRUCTURE'),
(736, 'READ', 'facility viewing', 'FACILITY:READ', 'FACILITY'),
(737, 'READ', 'pipeline_system viewing', 'PIPELINE_SYSTEM:READ', 'PIPELINE_SYSTEM'),
(738, 'READ', 'pipeline viewing', 'PIPELINE:READ', 'PIPELINE'),
(739, 'READ', 'pipeline_segment viewing', 'PIPELINE_SEGMENT:READ', 'PIPELINE_SEGMENT'),
(740, 'READ', 'station viewing', 'STATION:READ', 'STATION'),
(741, 'READ', 'terminal viewing', 'TERMINAL:READ', 'TERMINAL'),
(742, 'READ', 'processing_plant viewing', 'PROCESSING_PLANT:READ', 'PROCESSING_PLANT'),
(743, 'READ', 'production_field viewing', 'PRODUCTION_FIELD:READ', 'PRODUCTION_FIELD'),
(744, 'READ', 'equipment viewing', 'EQUIPMENT:READ', 'EQUIPMENT'),
(745, 'READ', 'event_type viewing', 'EVENT_TYPE:READ', 'EVENT_TYPE'),
(746, 'READ', 'operation_type viewing', 'OPERATION_TYPE:READ', 'OPERATION_TYPE'),
(747, 'READ', 'event_status viewing', 'EVENT_STATUS:READ', 'EVENT_STATUS'),
(748, 'READ', 'alert_status viewing', 'ALERT_STATUS:READ', 'ALERT_STATUS'),
(749, 'READ', 'severity viewing', 'SEVERITY:READ', 'SEVERITY'),
(750, 'READ', 'data_source viewing', 'DATA_SOURCE:READ', 'DATA_SOURCE'),
(751, 'READ', 'quality_flag viewing', 'QUALITY_FLAG:READ', 'QUALITY_FLAG'),
(752, 'READ', 'validation_status viewing', 'VALIDATION_STATUS:READ', 'VALIDATION_STATUS'),
(753, 'READ', 'reading_slot viewing', 'READING_SLOT:READ', 'READING_SLOT'),
(754, 'READ', 'flow_operation viewing', 'FLOW_OPERATION:READ', 'FLOW_OPERATION'),
(755, 'READ', 'flow_forecast viewing', 'FLOW_FORECAST:READ', 'FLOW_FORECAST'),
(756, 'READ', 'flow_reading viewing', 'FLOW_READING:READ', 'FLOW_READING'),
(757, 'READ', 'flow_event viewing', 'FLOW_EVENT:READ', 'FLOW_EVENT'),
(758, 'READ', 'flow_alert viewing', 'FLOW_ALERT:READ', 'FLOW_ALERT'),
(759, 'READ', 'flow_threshold viewing', 'FLOW_THRESHOLD:READ', 'FLOW_THRESHOLD');

-- --------------------------------------------------------

--
-- Table structure for table `t_00_02_05`
--

CREATE TABLE `t_00_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_03` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_02_05`
--

INSERT INTO `t_00_02_05` (`f_00`, `f_02`, `f_01`, `f_03`) VALUES
(1, 'System Management', 'SYSTEM', 'Core system operations and configuration'),
(2, 'User Management', 'USER', 'User account and profile management'),
(3, 'Role Management', 'ROLE', 'Role definitions and assignments'),
(4, 'Permission Management', 'PERMISSION', 'Permission and access control'),
(5, 'Group Management', 'GROUP', 'User group organization'),
(6, 'Authority Management', 'AUTHORITY', 'Authority category management'),
(7, 'Audit Management', 'AUDIT', 'System audit logging and tracking'),
(8, 'Authentication', 'AUTH', 'Authentication and session management'),
(9, 'Configuration', 'CONFIG', 'System configuration settings'),
(10, 'System Utility', 'UTILITY', 'System utilities and tools'),
(11, 'Administration', 'ADMINISTRATION', 'Administrative operations'),
(12, 'Communication', 'COMMUNICATION', 'Messaging and notifications'),
(13, 'Document Management', 'DOCUMENT', 'Document handling and archiving'),
(14, 'Environment Data', 'ENVIRONMENT', 'Geographic and location data'),
(15, 'Network Management', 'NETWORK', 'Network infrastructure management'),
(16, 'IT Infrastructure', 'INFRASTRUCTURE', 'IT infrastructure and services'),
(17, 'Reporting & Analytics', 'REPORT', 'Reports, dashboards, and analytics'),
(18, 'Workflow Engine', 'WORKFLOW', 'Business process workflows'),
(19, 'System Integration', 'INTEGRATION', 'External system integrations');

-- --------------------------------------------------------

--
-- Table structure for table `t_00_03_01`
--

CREATE TABLE `t_00_03_01` (
  `f_00` bigint(20) NOT NULL,
  `f_03` varchar(20) NOT NULL,
  `f_18` varchar(50) DEFAULT NULL,
  `f_12` varchar(1000) DEFAULT NULL,
  `f_15` bigint(20) DEFAULT NULL,
  `f_02` bigint(20) NOT NULL,
  `f_01` varchar(100) NOT NULL,
  `f_14` text DEFAULT NULL,
  `f_06` varchar(45) DEFAULT NULL,
  `f_20` text DEFAULT NULL,
  `f_08` varchar(200) DEFAULT NULL,
  `f_17` varchar(50) DEFAULT NULL,
  `f_10` text DEFAULT NULL,
  `f_09` text DEFAULT NULL,
  `f_11` text DEFAULT NULL,
  `f_19` bigint(20) DEFAULT NULL,
  `f_16` varchar(100) DEFAULT NULL,
  `f_13` varchar(20) NOT NULL,
  `f_05` datetime(6) NOT NULL,
  `f_07` varchar(500) DEFAULT NULL,
  `f_04` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_00_04_01`
--

CREATE TABLE `t_00_04_01` (
  `f_00` bigint(20) NOT NULL,
  `f_02` datetime(6) NOT NULL,
  `f_01` varchar(255) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_04_01`
--

INSERT INTO `t_00_04_01` (`f_00`, `f_02`, `f_01`, `f_03`) VALUES
(25, '2026-02-05 12:28:09.000000', '30957290-8895-4fec-bd72-807f6e860885', 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_00_05_01`
--

CREATE TABLE `t_00_05_01` (
  `f_00` bigint(20) NOT NULL,
  `f_07` datetime(6) NOT NULL,
  `f_05` bit(1) NOT NULL,
  `f_02` varchar(1000) NOT NULL,
  `f_06` datetime(6) DEFAULT NULL,
  `f_03` varchar(50) DEFAULT NULL,
  `f_04` varchar(50) DEFAULT NULL,
  `f_01` varchar(200) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_08` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_00_05_02`
--

CREATE TABLE `t_00_05_02` (
  `f_00` bigint(20) NOT NULL,
  `f_09` bit(1) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_07` varchar(7) DEFAULT NULL,
  `f_05` varchar(500) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_06` varchar(50) DEFAULT NULL,
  `f_08` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_00_05_02`
--

INSERT INTO `t_00_05_02` (`f_00`, `f_09`, `f_01`, `f_07`, `f_05`, `f_02`, `f_03`, `f_04`, `f_06`, `f_08`) VALUES
(1, b'1', 'READING_SUBMITTED', '#2196F3', 'Triggered when a Reader user submits operational readings for validation', 'قراءة مقدمة', 'Reading Submitted', 'Lecture Soumise', 'fa-file-check', 3),
(2, b'1', 'READING_VALIDATED', '#4CAF50', 'Triggered when a Validator approves submitted readings', 'قراءة مصادق عليها', 'Reading Validated', 'Lecture Validée', 'fa-check-circle', 2),
(3, b'1', 'READING_REJECTED', '#F44336', 'Triggered when a Validator rejects submitted readings', 'قراءة مرفوضة', 'Reading Rejected', 'Lecture Rejetée', 'fa-times-circle', 3),
(4, b'1', 'SYSTEM_ALERT', '#FF9800', 'Critical system notifications requiring immediate attention', 'تنبيه نظام', 'System Alert', 'Alerte Système', 'fa-exclamation-triangle', 4),
(5, b'1', 'WARNING', '#FFC107', 'Warning messages about potential issues', 'تحذير', 'Warning', 'Avertissement', 'fa-exclamation-circle', 2),
(6, b'1', 'INFO', '#00BCD4', 'General informational messages', 'معلومات', 'Information', 'Information', 'fa-info-circle', 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_01_01`
--

CREATE TABLE `t_01_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_01_01`
--

INSERT INTO `t_01_01_01` (`f_00`, `f_01`, `f_02`, `f_03`) VALUES
(1, 'مديرية عامة', 'General Directorate', 'Direction Générale'),
(2, 'نشاط', 'Activity', 'Activité'),
(3, 'قسم', 'Division', 'Division'),
(4, 'مديرية', 'Directorate', 'Direction'),
(5, 'مديرية عمليات', 'Operations Directorate', 'Direction Opérationnelle'),
(6, 'مديرية جهوية', 'Regional Directorate', 'Direction Regionale'),
(7, 'مديرية فرعية', 'Sub-directorate', 'Sous Direction'),
(8, 'دائرة', 'Department', 'Département'),
(9, 'دائرة جهوية', 'Regional Department', 'Départment Régional'),
(10, 'مركز وطني', 'National Center', 'Centre National'),
(11, 'مصلحة', 'Service', 'Service'),
(12, 'مصلحة جهوية', 'Regional Service', 'Service Regional'),
(13, 'مركز', 'Center', 'Centre');

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_01`
--

CREATE TABLE `t_01_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_01`
--

INSERT INTO `t_01_02_01` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'S', 'الجنوب', 'South', 'Sud'),
(2, 'N', 'الشمال', 'North', 'Nord');

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_02`
--

CREATE TABLE `t_01_02_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(3) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_02`
--

INSERT INTO `t_01_02_02` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'AF', 'أفغانستان', 'Afghanistan', 'Afghanistan'),
(2, 'AX', 'جزر آلاند', 'Åland Islands', 'Îles Åland'),
(3, 'AL', 'ألبانيا', 'Albania', 'Albanie'),
(4, 'DZ', 'الجزائر', 'Algeria', 'Algérie'),
(5, 'AS', 'ساموا الأمريكية', 'American Samoa', 'Samoa Américaines'),
(6, 'AD', 'أندورا', 'Andorra', 'Andorre'),
(7, 'AO', 'أنغولا', 'Angola', 'Angola'),
(8, 'AI', 'أنغيلا', 'Anguilla', 'Anguilla'),
(9, 'AQ', 'القطب الجنوبي', 'Antarctica', 'Antarctique'),
(10, 'AG', 'أنتيغوا وباربودا', 'Antigua and Barbuda', 'Antigua-et-Barbuda'),
(11, 'AR', 'الأرجنتين', 'Argentina', 'Argentine'),
(12, 'AM', 'أرمينيا', 'Armenia', 'Arménie'),
(13, 'AW', 'أروبا', 'Aruba', 'Aruba'),
(14, 'AU', 'أستراليا', 'Australia', 'Australie'),
(15, 'AT', 'النمسا', 'Austria', 'Autriche'),
(16, 'AZ', 'أذربيجان', 'Azerbaijan', 'Azerbaïdjan'),
(17, 'BS', 'البهاما', 'Bahamas', 'Bahamas'),
(18, 'BH', 'البحرين', 'Bahrain', 'Bahreïn'),
(19, 'BD', 'بنغلاديش', 'Bangladesh', 'Bangladesh'),
(20, 'BB', 'باربادوس', 'Barbados', 'Barbade'),
(21, 'BY', 'بيلاروسيا', 'Belarus', 'Bélarus'),
(22, 'BE', 'بلجيكا', 'Belgium', 'Belgique'),
(23, 'BZ', 'بليز', 'Belize', 'Belize'),
(24, 'BJ', 'بنين', 'Benin', 'Bénin'),
(25, 'BM', 'برمودا', 'Bermuda', 'Bermudes'),
(26, 'BT', 'بوتان', 'Bhutan', 'Bhoutan'),
(27, 'BO', 'بوليفيا', 'Bolivia', 'Bolivie'),
(28, 'BQ', 'بونير وسينت أوستاتيوس وسابا', 'Bonaire, Sint Eustatius and Saba', 'Bonaire, Saint-Eustache et Saba'),
(29, 'BA', 'البوسنة والهرسك', 'Bosnia and Herzegovina', 'Bosnie-Herzégovine'),
(30, 'BW', 'بوتسوانا', 'Botswana', 'Botswana'),
(31, 'BV', 'جزيرة بوفيه', 'Bouvet Island', 'Île Bouvet'),
(32, 'BR', 'البرازيل', 'Brazil', 'Brésil'),
(33, 'IO', 'إقليم المحيط الهندي البريطاني', 'British Indian Ocean Territory', 'Territoire Britannique de l\'Océan Indien'),
(34, 'BN', 'بروناي', 'Brunei Darussalam', 'Brunéi Darussalam'),
(35, 'BG', 'بلغاريا', 'Bulgaria', 'Bulgarie'),
(36, 'BF', 'بوركينا فاسو', 'Burkina Faso', 'Burkina Faso'),
(37, 'BI', 'بوروندي', 'Burundi', 'Burundi'),
(38, 'CV', 'الرأس الأخضر', 'Cabo Verde', 'Cabo Verde'),
(39, 'KH', 'كمبوديا', 'Cambodia', 'Cambodge'),
(40, 'CM', 'الكاميرون', 'Cameroon', 'Cameroun'),
(41, 'CA', 'كندا', 'Canada', 'Canada'),
(42, 'KY', 'جزر كايمان', 'Cayman Islands', 'Îles Caïmans'),
(43, 'CF', 'جمهورية أفريقيا الوسطى', 'Central African Republic', 'République Centrafricaine'),
(44, 'TD', 'تشاد', 'Chad', 'Tchad'),
(45, 'CL', 'تشيلي', 'Chile', 'Chili'),
(46, 'CN', 'الصين', 'China', 'Chine'),
(47, 'CX', 'جزيرة كريسماس', 'Christmas Island', 'Île Christmas'),
(48, 'CC', 'جزر كوكوس', 'Cocos (Keeling) Islands', 'Îles Cocos'),
(49, 'CO', 'كولومبيا', 'Colombia', 'Colombie'),
(50, 'KM', 'جزر القمر', 'Comoros', 'Comores'),
(51, 'CD', 'جمهورية الكونغو الديمقراطية', 'Congo (Democratic Republic)', 'Congo (République Démocratique)'),
(52, 'CG', 'جمهورية الكونغو', 'Congo', 'Congo'),
(53, 'CK', 'جزر كوك', 'Cook Islands', 'Îles Cook'),
(54, 'CR', 'كوستاريكا', 'Costa Rica', 'Costa Rica'),
(55, 'CI', 'ساحل العاج', 'Côte d\'Ivoire', 'Côte d\'Ivoire'),
(56, 'HR', 'كرواتيا', 'Croatia', 'Croatie'),
(57, 'CU', 'كوبا', 'Cuba', 'Cuba'),
(58, 'CW', 'كوراساو', 'Curaçao', 'Curaçao'),
(59, 'CY', 'قبرص', 'Cyprus', 'Chypre'),
(60, 'CZ', 'التشيك', 'Czechia', 'République Tchèque'),
(61, 'DK', 'الدنمارك', 'Denmark', 'Danemark'),
(62, 'DJ', 'جيبوتي', 'Djibouti', 'Djibouti'),
(63, 'DM', 'دومينيكا', 'Dominica', 'Dominique'),
(64, 'DO', 'جمهورية الدومينيكان', 'Dominican Republic', 'République Dominicaine'),
(65, 'EC', 'الإكوادور', 'Ecuador', 'Équateur'),
(66, 'EG', 'مصر', 'Egypt', 'Égypte'),
(67, 'SV', 'السلفادور', 'El Salvador', 'El Salvador'),
(68, 'GQ', 'غينيا الاستوائية', 'Equatorial Guinea', 'Guinée Équatoriale'),
(69, 'ER', 'إريتريا', 'Eritrea', 'Érythrée'),
(70, 'EE', 'إستونيا', 'Estonia', 'Estonie'),
(71, 'SZ', 'إسواتيني', 'Eswatini', 'Eswatini'),
(72, 'ET', 'إثيوبيا', 'Ethiopia', 'Éthiopie'),
(73, 'FK', 'جزر فوكلاند', 'Falkland Islands', 'Îles Malouines'),
(74, 'FO', 'جزر فارو', 'Faroe Islands', 'Îles Féroé'),
(75, 'FJ', 'فيجي', 'Fiji', 'Fidji'),
(76, 'FI', 'فنلندا', 'Finland', 'Finlande'),
(77, 'FR', 'فرنسا', 'France', 'France'),
(78, 'GF', 'غويانا الفرنسية', 'French Guiana', 'Guyane Française'),
(79, 'PF', 'بولينيزيا الفرنسية', 'French Polynesia', 'Polynésie Française'),
(80, 'TF', 'الأقاليم الجنوبية الفرنسية', 'French Southern Territories', 'Terres Australes Françaises'),
(81, 'GA', 'الغابون', 'Gabon', 'Gabon'),
(82, 'GM', 'غامبيا', 'Gambia', 'Gambie'),
(83, 'GE', 'جورجيا', 'Georgia', 'Géorgie'),
(84, 'DE', 'ألمانيا', 'Germany', 'Allemagne'),
(85, 'GH', 'غانا', 'Ghana', 'Ghana'),
(86, 'GI', 'جبل طارق', 'Gibraltar', 'Gibraltar'),
(87, 'GR', 'اليونان', 'Greece', 'Grèce'),
(88, 'GL', 'جرينلاند', 'Greenland', 'Groenland'),
(89, 'GD', 'جرينادا', 'Grenada', 'Grenade'),
(90, 'GP', 'جوادلوب', 'Guadeloupe', 'Guadeloupe'),
(91, 'GU', 'غوام', 'Guam', 'Guam'),
(92, 'GT', 'غواتيمالا', 'Guatemala', 'Guatemala'),
(93, 'GG', 'غيرنزي', 'Guernsey', 'Guernesey'),
(94, 'GN', 'غينيا', 'Guinea', 'Guinée'),
(95, 'GW', 'غينيا بيساو', 'Guinea-Bissau', 'Guinée-Bissau'),
(96, 'GY', 'غويانا', 'Guyana', 'Guyana'),
(97, 'HT', 'هايتي', 'Haiti', 'Haïti'),
(98, 'HM', 'جزيرة هيرد وجزر ماكدونالد', 'Heard Island and McDonald Islands', 'Île Heard-et-Îles MacDonald'),
(99, 'VA', 'الفاتيكان', 'Holy See', 'Saint-Siège'),
(100, 'HN', 'هندوراس', 'Honduras', 'Honduras'),
(101, 'HK', 'هونغ كونغ', 'Hong Kong', 'Hong Kong'),
(102, 'HU', 'المجر', 'Hungary', 'Hongrie'),
(103, 'IS', 'آيسلندا', 'Iceland', 'Islande'),
(104, 'IN', 'الهند', 'India', 'Inde'),
(105, 'ID', 'إندونيسيا', 'Indonesia', 'Indonésie'),
(106, 'IR', 'إيران', 'Iran', 'Iran'),
(107, 'IQ', 'العراق', 'Iraq', 'Irak'),
(108, 'IE', 'أيرلندا', 'Ireland', 'Irlande'),
(109, 'IM', 'جزيرة مان', 'Isle of Man', 'Île de Man'),
(110, 'IL', 'إسرائيل', 'Israel', 'Israël'),
(111, 'IT', 'إيطاليا', 'Italy', 'Italie'),
(112, 'JM', 'جامايكا', 'Jamaica', 'Jamaïque'),
(113, 'JP', 'اليابان', 'Japan', 'Japon'),
(114, 'JE', 'جيرزي', 'Jersey', 'Jersey'),
(115, 'JO', 'الأردن', 'Jordan', 'Jordanie'),
(116, 'KZ', 'كازاخستان', 'Kazakhstan', 'Kazakhstan'),
(117, 'KE', 'كينيا', 'Kenya', 'Kenya'),
(118, 'KI', 'كيريباتي', 'Kiribati', 'Kiribati'),
(119, 'KP', 'كوريا الشمالية', 'Korea (North)', 'Corée du Nord'),
(120, 'KR', 'كوريا الجنوبية', 'Korea (South)', 'Corée du Sud'),
(121, 'KW', 'الكويت', 'Kuwait', 'Koweït'),
(122, 'KG', 'قيرغيزستان', 'Kyrgyzstan', 'Kirghizistan'),
(123, 'LA', 'لاوس', 'Laos', 'Laos'),
(124, 'LV', 'لاتفيا', 'Latvia', 'Lettonie'),
(125, 'LB', 'لبنان', 'Lebanon', 'Liban'),
(126, 'LS', 'ليسوتو', 'Lesotho', 'Lesotho'),
(127, 'LR', 'ليبيريا', 'Liberia', 'Libéria'),
(128, 'LY', 'ليبيا', 'Libya', 'Libye'),
(129, 'LI', 'ليختنشتاين', 'Liechtenstein', 'Liechtenstein'),
(130, 'LT', 'ليتوانيا', 'Lithuania', 'Lituanie'),
(131, 'LU', 'لوكسمبورغ', 'Luxembourg', 'Luxembourg'),
(132, 'MO', 'ماكاو', 'Macao', 'Macao'),
(133, 'MG', 'مدغشقر', 'Madagascar', 'Madagascar'),
(134, 'MW', 'مالاوي', 'Malawi', 'Malawi'),
(135, 'MY', 'ماليزيا', 'Malaysia', 'Malaisie'),
(136, 'MV', 'جزر المالديف', 'Maldives', 'Maldives'),
(137, 'ML', 'مالي', 'Mali', 'Mali'),
(138, 'MT', 'مالطا', 'Malta', 'Malte'),
(139, 'MH', 'جزر مارشال', 'Marshall Islands', 'Îles Marshall'),
(140, 'MQ', 'مارتينيك', 'Martinique', 'Martinique'),
(141, 'MR', 'موريتانيا', 'Mauritania', 'Mauritanie'),
(142, 'MU', 'موريشيوس', 'Mauritius', 'Maurice'),
(143, 'YT', 'مايوت', 'Mayotte', 'Mayotte'),
(144, 'MX', 'المكسيك', 'Mexico', 'Mexique'),
(145, 'FM', 'ميكرونيزيا', 'Micronesia', 'Micronésie'),
(146, 'MD', 'مولدوفا', 'Moldova', 'Moldavie'),
(147, 'MC', 'موناكو', 'Monaco', 'Monaco'),
(148, 'MN', 'منغوليا', 'Mongolia', 'Mongolie'),
(149, 'ME', 'الجبل الأسود', 'Montenegro', 'Monténégro'),
(150, 'MS', 'مونتسرات', 'Montserrat', 'Montserrat'),
(151, 'MA', 'المغرب', 'Morocco', 'Maroc'),
(152, 'MZ', 'موزمبيق', 'Mozambique', 'Mozambique'),
(153, 'MM', 'ميانمار', 'Myanmar', 'Myanmar'),
(154, 'NA', 'ناميبيا', 'Namibia', 'Namibie'),
(155, 'NR', 'ناورو', 'Nauru', 'Nauru'),
(156, 'NP', 'نيبال', 'Nepal', 'Népal'),
(157, 'NL', 'هولندا', 'Netherlands', 'Pays-Bas'),
(158, 'NC', 'كاليدونيا الجديدة', 'New Caledonia', 'Nouvelle-Calédonie'),
(159, 'NZ', 'نيوزيلندا', 'New Zealand', 'Nouvelle-Zélande'),
(160, 'NI', 'نيكاراغوا', 'Nicaragua', 'Nicaragua'),
(161, 'NE', 'النيجر', 'Niger', 'Niger'),
(162, 'NG', 'نيجيريا', 'Nigeria', 'Nigéria'),
(163, 'NU', 'نيوي', 'Niue', 'Niue'),
(164, 'NF', 'جزيرة نورفولك', 'Norfolk Island', 'Île Norfolk'),
(165, 'MK', 'مقدونيا الشمالية', 'North Macedonia', 'Macédoine du Nord'),
(166, 'MP', 'جزر ماريانا الشمالية', 'Northern Mariana Islands', 'Îles Mariannes du Nord'),
(167, 'NO', 'النرويج', 'Norway', 'Norvège'),
(168, 'OM', 'عمان', 'Oman', 'Oman'),
(169, 'PK', 'باكستان', 'Pakistan', 'Pakistan'),
(170, 'PW', 'بالاو', 'Palau', 'Palaos'),
(171, 'PS', 'فلسطين', 'Palestine', 'Palestine'),
(172, 'PA', 'بنما', 'Panama', 'Panama'),
(173, 'PG', 'بابوا غينيا الجديدة', 'Papua New Guinea', 'Papouasie-Nouvelle-Guinée'),
(174, 'PY', 'باراغواي', 'Paraguay', 'Paraguay'),
(175, 'PE', 'بيرو', 'Peru', 'Pérou'),
(176, 'PH', 'الفلبين', 'Philippines', 'Philippines'),
(177, 'PN', 'جزر بيتكيرن', 'Pitcairn', 'Pitcairn'),
(178, 'PL', 'بولندا', 'Poland', 'Pologne'),
(179, 'PT', 'البرتغال', 'Portugal', 'Portugal'),
(180, 'PR', 'بورتوريكو', 'Puerto Rico', 'Porto Rico'),
(181, 'QA', 'قطر', 'Qatar', 'Qatar'),
(182, 'RE', 'ريونيون', 'Réunion', 'Réunion'),
(183, 'RO', 'رومانيا', 'Romania', 'Roumanie'),
(184, 'RU', 'روسيا', 'Russia', 'Russie'),
(185, 'RW', 'رواندا', 'Rwanda', 'Rwanda'),
(186, 'BL', 'سان بارتيلمي', 'Saint Barthélemy', 'Saint-Barthélemy'),
(187, 'SH', 'سانت هيلينا وأسنسيون وتريستان دا كونا', 'Saint Helena', 'Sainte-Hélène'),
(188, 'KN', 'سانت كيتس ونيفيس', 'Saint Kitts and Nevis', 'Saint-Kitts-et-Nevis'),
(189, 'LC', 'سانت لوسيا', 'Saint Lucia', 'Sainte-Lucie'),
(190, 'MF', 'سانت مارتن', 'Saint Martin.', 'Saint-Martin.'),
(191, 'PM', 'سان بيير وميكلون', 'Saint Pierre and Miquelon', 'Saint-Pierre-et-Miquelon'),
(192, 'VC', 'سانت فينسنت والغرينادين', 'Saint Vincent and the Grenadines', 'Saint-Vincent-et-les-Grenadines'),
(193, 'WS', 'ساموا', 'Samoa', 'Samoa'),
(194, 'SM', 'سان مارينو', 'San Marino', 'Saint-Marin'),
(195, 'ST', 'ساو تومي وبرينسيب', 'Sao Tome and Principe', 'Sao Tomé-et-Principe'),
(196, 'SA', 'السعودية', 'Saudi Arabia', 'Arabie Saoudite'),
(197, 'SN', 'السنغال', 'Senegal', 'Sénégal'),
(198, 'RS', 'صربيا', 'Serbia', 'Serbie'),
(199, 'SC', 'سيشل', 'Seychelles', 'Seychelles'),
(200, 'SL', 'سيراليون', 'Sierra Leone', 'Sierra Leone'),
(201, 'SG', 'سنغافورة', 'Singapore', 'Singapour'),
(202, 'SX', 'سينت مارتن', 'Sint Maarten', 'Saint-Martin'),
(203, 'SK', 'سلوفاكيا', 'Slovakia', 'Slovaquie'),
(204, 'SI', 'سلوفينيا', 'Slovenia', 'Slovénie'),
(205, 'SB', 'جزر سليمان', 'Solomon Islands', 'Îles Salomon'),
(206, 'SO', 'الصومال', 'Somalia', 'Somalie'),
(207, 'ZA', 'جنوب أفريقيا', 'South Africa', 'Afrique du Sud'),
(208, 'GS', 'جورجيا الجنوبية وجزر ساندويتش الجنوبية', 'South Georgia and South Sandwich Islands', 'Géorgie du Sud-et-les Îles Sandwich du Sud'),
(209, 'SS', 'جنوب السودان', 'South Sudan', 'Soudan du Sud'),
(210, 'ES', 'إسبانيا', 'Spain', 'Espagne'),
(211, 'LK', 'سريلانكا', 'Sri Lanka', 'Sri Lanka'),
(212, 'SD', 'السودان', 'Sudan', 'Soudan'),
(213, 'SR', 'سورينام', 'Suriname', 'Suriname'),
(214, 'SJ', 'سفالبارد ويان ماين', 'Svalbard and Jan Mayen', 'Svalbard et Jan Mayen'),
(215, 'SE', 'السويد', 'Sweden', 'Suède'),
(216, 'CH', 'سويسرا', 'Switzerland', 'Suisse'),
(217, 'SY', 'سوريا', 'Syria', 'Syrie'),
(218, 'TW', 'تايوان', 'Taiwan', 'Taïwan'),
(219, 'TJ', 'طاجيكستان', 'Tajikistan', 'Tadjikistan'),
(220, 'TZ', 'تنزانيا', 'Tanzania', 'Tanzanie'),
(221, 'TH', 'تايلاند', 'Thailand', 'Thaïlande'),
(222, 'TL', 'تيمور الشرقية', 'Timor-Leste', 'Timor-Leste'),
(223, 'TG', 'توغو', 'Togo', 'Togo'),
(224, 'TK', 'توكيلاو', 'Tokelau', 'Tokelau'),
(225, 'TO', 'تونغا', 'Tonga', 'Tonga'),
(226, 'TT', 'ترينيداد وتوباغو', 'Trinidad and Tobago', 'Trinité-et-Tobago'),
(227, 'TN', 'تونس', 'Tunisia', 'Tunisie'),
(228, 'TR', 'تركيا', 'Türkiye', 'Turquie'),
(229, 'TM', 'تركمانستان', 'Turkmenistan', 'Turkménistan'),
(230, 'TC', 'جزر توركس وكايكوس', 'Turks and Caicos Islands', 'Îles Turques-et-Caïques'),
(231, 'TV', 'توفالو', 'Tuvalu', 'Tuvalu'),
(232, 'UG', 'أوغندا', 'Uganda', 'Ouganda'),
(233, 'UA', 'أوكرانيا', 'Ukraine', 'Ukraine'),
(234, 'AE', 'الإمارات العربية المتحدة', 'United Arab Emirates', 'Émirats Arabes Unis'),
(235, 'GB', 'المملكة المتحدة', 'United Kingdom', 'Royaume-Uni'),
(236, 'UM', 'جزر الولايات المتحدة البعيدة الصغيرة', 'United States Minor Outlying Islands', 'Îles Mineures Éloignées des États-Unis'),
(237, 'US', 'الولايات المتحدة', 'United States', 'États-Unis'),
(238, 'UY', 'أوروغواي', 'Uruguay', 'Uruguay'),
(239, 'UZ', 'أوزبكستان', 'Uzbekistan', 'Ouzbékistan'),
(240, 'VU', 'فانواتو', 'Vanuatu', 'Vanuatu'),
(241, 'VE', 'فنزويلا', 'Venezuela', 'Venezuela'),
(242, 'VN', 'فيتنام', 'Vietnam', 'Viêt Nam'),
(243, 'VG', 'جزر العذراء البريطانية', 'Virgin Islands (British)', 'Îles Vierges Britanniques'),
(244, 'VI', 'جزر العذراء الأمريكية', 'Virgin Islands (U.S.)', 'Îles Vierges des États-Unis'),
(245, 'WF', 'والس وفوتونا', 'Wallis and Futuna', 'Wallis-et-Futuna'),
(246, 'EH', 'الصحراء الغربية', 'Western Sahara', 'Sahara Occidental'),
(247, 'YE', 'اليمن', 'Yemen', 'Yémen'),
(248, 'ZM', 'زامبيا', 'Zambia', 'Zambie'),
(249, 'ZW', 'زيمبابوي', 'Zimbabwe', 'Zimbabwe');

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_03`
--

CREATE TABLE `t_01_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_03`
--

INSERT INTO `t_01_02_03` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, '01', 'أدرار', 'Adrar', 'Adrar'),
(2, '02', 'الشلف', 'Chlef', 'Chlef'),
(3, '03', 'الأغواط', 'Laghouat', 'Laghouat'),
(4, '04', 'أم البواقي', 'Oum El Bouaghi', 'Oum El Bouaghi'),
(5, '05', 'باتنة', 'Batna', 'Batna'),
(6, '06', 'بجاية', 'Bejaia', 'Béjaïa'),
(7, '07', 'بسكرة', 'Biskra', 'Biskra'),
(8, '08', 'بشار', 'Bechar', 'Béchar'),
(9, '09', 'البليدة', 'Blida', 'Blida'),
(10, '10', 'البويرة', 'Bouira', 'Bouïra'),
(11, '11', 'تمنراست', 'Tamanrasset', 'Tamanrasset'),
(12, '12', 'تبسة', 'Tebessa', 'Tébessa'),
(13, '13', 'تلمسان', 'Tlemcen', 'Tlemcen'),
(14, '14', 'تيارت', 'Tiaret', 'Tiaret'),
(15, '15', 'تيزي وزو', 'Tizi Ouzou', 'Tizi Ouzou'),
(16, '16', 'الجزائر', 'Alger', 'Alger'),
(17, '17', 'الجلفة', 'Djelfa', 'Djelfa'),
(18, '18', 'جيجل', 'Jijel', 'Jijel'),
(19, '19', 'سطيف', 'Setif', 'Sétif'),
(20, '20', 'سعيدة', 'Saida', 'Saïda'),
(21, '21', 'سكيكدة', 'Skikda', 'Skikda'),
(22, '22', 'سيدي بلعباس', 'Sidi Bel Abbes', 'Sidi Bel Abbès'),
(23, '23', 'عنابة', 'Annaba', 'Annaba'),
(24, '24', 'قالمة', 'Guelma', 'Guelma'),
(25, '25', 'قسنطينة', 'Constantine', 'Constantine'),
(26, '26', 'المدية', 'Medea', 'Médéa'),
(27, '27', 'مستغانم', 'Mostaganem', 'Mostaganem'),
(28, '28', 'المسيلة', 'M\'Sila', 'M\'Sila'),
(29, '29', 'معسكر', 'Mascara', 'Mascara'),
(30, '30', 'ورقلة', 'Ouargla', 'Ouargla'),
(31, '31', 'وهران', 'Oran', 'Oran'),
(32, '32', 'البيض', 'El Bayadh', 'El Bayadh'),
(33, '33', 'إليزي', 'Illizi', 'Illizi'),
(34, '34', 'برج بوعريريج', 'Bordj Bou Arreridj', 'Bordj Bou Arréridj'),
(35, '35', 'بومرداس', 'Boumerdes', 'Boumerdès'),
(36, '36', 'الطارف', 'El Tarf', 'El Tarf'),
(37, '37', 'تندوف', 'Tindouf', 'Tindouf'),
(38, '38', 'تيسمسيلت', 'Tissemsilt', 'Tissemsilt'),
(39, '39', 'الوادي', 'El Oued', 'El Oued'),
(40, '40', 'خنشلة', 'Khenchela', 'Khenchela'),
(41, '41', 'سوق أهراس', 'Souk Ahras', 'Souk Ahras'),
(42, '42', 'تيبازة', 'Tipaza', 'Tipaza'),
(43, '43', 'ميلة', 'Mila', 'Mila'),
(44, '44', 'عين الدفلى', 'Ain Defla', 'Aïn Defla'),
(45, '45', 'النعامة', 'Naâma', 'Naâma'),
(46, '46', 'عين تموشنت', 'Ain Temouchent', 'Aïn Témouchent'),
(47, '47', 'غرداية', 'Ghardaia', 'Ghardaïa'),
(48, '48', 'غليزان', 'Relizane', 'Relizane'),
(49, '49', 'تيميمون', 'Timimoun', 'Timimoun'),
(50, '50', 'برج باجي مختار', 'Bordj Baji Mokhtar', 'Bordj Baji Mokhtar'),
(51, '51', 'أولاد جلال', 'Ouled Djellal', 'Ouled Djellal'),
(52, '52', 'بني عباس', 'Beni Abbes', 'Béni Abbès'),
(53, '53', 'إن صالح', 'In Salah', 'In Salah'),
(54, '54', 'إن قزام', 'In Guezzam', 'In Guezzam'),
(55, '55', 'تقرت', 'Touggourt', 'Touggourt'),
(56, '56', 'جانت', 'Djanet', 'Djanet'),
(57, '57', 'المغير', 'El M\'Ghair', 'El M\'Ghair'),
(58, '58', 'المنيعة', 'El Menia', 'El Menia');

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_04`
--

CREATE TABLE `t_01_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_04`
--

INSERT INTO `t_01_02_04` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(1, '101', 'أدرار', 'Adrar', 'Adrar', 1),
(2, '104', 'رقان', 'Reggane', 'Reggane', 1),
(3, '108', 'تسابيت', 'Tsabit', 'Tsabit', 1),
(4, '111', 'زاوية كنتة', 'Zaouiat Kounta', 'Zaouiat Kounta', 1),
(5, '112', 'أولف', 'Aoulef', 'Aoulef', 1),
(6, '115', 'فنوغيل', 'Fenoughil', 'Fenoughil', 1),
(7, '201', 'الشلف', 'Chlef', 'Chlef', 2),
(8, '202', 'تنس', 'Tenes', 'Tenes', 2),
(9, '204', 'الكريمية', 'El Karimia', 'El Karimia', 2),
(10, '206', 'تاوقريت', 'Taougrit', 'Taougrit', 2),
(11, '207', 'بني حواء', 'Beni Haoua', 'Beni Haoua', 2),
(12, '210', 'أولاد فارس', 'Ouled Fares', 'Ouled Fares', 2),
(13, '212', 'بوقادير', 'Boukadir', 'Boukadir', 2),
(14, '220', 'الزبوجة', 'Zeboudja', 'Zeboudja', 2),
(15, '222', 'أبو الحسن', 'Abou El Hassane', 'Abou El Hassane', 2),
(16, '223', 'المرسى', 'El Marsa', 'El Marsa', 2),
(17, '229', 'وادي الفضة', 'Oued Fodda', 'Oued Fodda', 2),
(18, '230', 'أولاد بن عبد القادر', 'Ouled Ben Abdelkader', 'Ouled Ben Abdelkader', 2),
(19, '232', 'عين مران', 'Ain Merane', 'Ain Merane', 2),
(20, '301', 'الأغواط', 'Laghouat', 'Laghouat', 3),
(21, '302', 'قصر الحيران', 'Ksar El Hirane', 'Ksar El Hirane', 3),
(22, '304', 'سيدي مخلوف', 'Sidi Makhlouf', 'Sidi Makhlouf', 3),
(23, '306', 'حاسي الرمل', 'Hassi R\'mel', 'Hassi R\'mel', 3),
(24, '307', 'عين ماضي', 'Ain Madhi', 'Ain Madhi', 3),
(25, '310', 'قتلة سيدي سعيد', 'Gueltat Sidi Saad', 'Gueltat Sidi Saad', 3),
(26, '313', 'بريدة', 'Brida', 'Brida', 3),
(27, '314', 'الغيشة', 'El Ghicha', 'El Ghicha', 3),
(28, '319', 'أفلو', 'Aflou', 'Aflou', 3),
(29, '321', 'وادي مرة', 'Oued Morra', 'Oued Morra', 3),
(30, '401', 'أم البواقي', 'Oum El Bouaghi', 'Oum El Bouaghi', 4),
(31, '402', 'عين البيضاء', 'Ain Beida', 'Ain Beida', 4),
(32, '403', 'عين مليلة', 'Ain M\'lila', 'Ain M\'lila', 4),
(33, '406', 'سيقوس', 'Sigus', 'Sigus', 4),
(34, '408', 'عين ببوش', 'Ain Babouche', 'Ain Babouche', 4),
(35, '411', 'الضلعة', 'Dhalaa', 'Dhalaa', 4),
(36, '412', 'عين كرشة', 'Ain Kercha', 'Ain Kercha', 4),
(37, '416', 'فكيرينة', 'F\'kirina', 'F\'kirina', 4),
(38, '417', 'سوق نعمان', 'Souk Naamane', 'Souk Naamane', 4),
(39, '422', 'قصر الصباحي', 'Ksar Sbahi', 'Ksar Sbahi', 4),
(40, '424', 'مسكيانة', 'Meskiana', 'Meskiana', 4),
(41, '425', 'عين فكرون', 'Ain Fekroun', 'Ain Fekroun', 4),
(42, '501', 'باتنة', 'Batna', 'Batna', 5),
(43, '504', 'مروانة', 'Merouana', 'Merouana', 5),
(44, '505', 'سريانة', 'Seriana', 'Seriana', 5),
(45, '506', 'منعة', 'Menaa', 'Menaa', 5),
(46, '507', 'المعذر', 'El Madher', 'El Madher', 5),
(47, '508', 'تازولت', 'Tazoult', 'Tazoult', 5),
(48, '509', 'نقاوس', 'N\'gaous', 'N\'gaous', 5),
(49, '516', 'أريس', 'Arris', 'Arris', 5),
(50, '519', 'عين جاسر', 'Ain Djasser', 'Ain Djasser', 5),
(51, '529', 'سقانة', 'Seggana', 'Seggana', 5),
(52, '530', 'إشمول', 'Ichemoul', 'Ichemoul', 5),
(53, '535', 'بوزينة', 'Bouzina', 'Bouzina', 5),
(54, '536', 'الشمرة', 'Chemora', 'Chemora', 5),
(55, '542', 'بريكة', 'Barika', 'Barika', 5),
(56, '543', 'الجزار', 'Djezzar', 'Djezzar', 5),
(57, '544', 'تكوت', 'Tkout', 'Tkout', 5),
(58, '545', 'عين التوتة', 'Ain Touta', 'Ain Touta', 5),
(59, '547', 'ثنية العابد', 'Theniet El Abed', 'Theniet El Abed', 5),
(60, '550', 'تيمقاد', 'Timgad', 'Timgad', 5),
(61, '551', 'رأس العيون', 'Ras El Aioun', 'Ras El Aioun', 5),
(62, '553', 'أولاد سي سليمان', 'Ouled Si Slimane', 'Ouled Si Slimane', 5),
(63, '601', 'بجاية', 'Bejaia', 'Bejaia', 6),
(64, '602', 'أميزور', 'Amizour', 'Amizour', 6),
(65, '607', 'تيمزريت', 'Timezrit', 'Timezrit', 6),
(66, '608', 'سوق الإثنين', 'Souk El Tenine', 'Souk El Tenine', 6),
(67, '611', 'تيشي', 'Tichy', 'Tichy', 6),
(68, '617', 'إغيل علي', 'Ighil Ali', 'Ighil Ali', 6),
(69, '620', 'درقينة', 'Darguina', 'Darguina', 6),
(70, '622', 'أوقاس', 'Aokas', 'Aokas', 6),
(71, '624', 'أدكار', 'Adekar', 'Adekar', 6),
(72, '625', 'أقبو', 'Akbou', 'Akbou', 6),
(73, '626', 'صدوق', 'Seddouk', 'Seddouk', 6),
(74, '627', 'تازملت', 'Tazmalt', 'Tazmalt', 6),
(75, '629', 'شميني', 'Chemini', 'Chemini', 6),
(76, '634', 'برباشة', 'Barbacha', 'Barbacha', 6),
(77, '636', 'إفري أوزلاقن', 'Ifri Ouzellaguene', 'Ifri Ouzellaguene', 6),
(78, '639', 'سيدي عيش', 'Sidi Aich', 'Sidi Aich', 6),
(79, '640', 'القصر', 'El Kseur', 'El Kseur', 6),
(80, '644', 'خراطة', 'Kherrata', 'Kherrata', 6),
(81, '650', 'بني معوش', 'Beni Maouche', 'Beni Maouche', 6),
(82, '701', 'بسكرة', 'Biskra', 'Biskra', 7),
(83, '711', 'سيدي عقبة', 'Sidi Okba', 'Sidi Okba', 7),
(84, '712', 'مشونش', 'Mechouneche', 'Mechouneche', 7),
(85, '715', 'زريبة الوادي', 'Zeribet El Oued', 'Zeribet El Oued', 7),
(86, '717', 'القنطرة', 'El Kantara', 'El Kantara', 7),
(87, '719', 'الوطاية', 'El Outaya', 'El Outaya', 7),
(88, '720', 'جمورة', 'Djemorah', 'Djemorah', 7),
(89, '721', 'طولقة', 'Tolga', 'Tolga', 7),
(90, '724', 'أورلال', 'Ourlal', 'Ourlal', 7),
(91, '726', 'فوغالة', 'Foughala', 'Foughala', 7),
(92, '801', 'بشار', 'Bechar', 'Bechar', 8),
(93, '806', 'لحمر', 'Lahmar', 'Lahmar', 8),
(94, '810', 'القنادسة', 'Kenadsa', 'Kenadsa', 8),
(95, '812', 'تبلبالة', 'Tabelbala', 'Tabelbala', 8),
(96, '813', 'تاغيت', 'Taghit', 'Taghit', 8),
(97, '817', 'العبادلة', 'Abadla', 'Abadla', 8),
(98, '821', 'بني ونيف', 'Beni Ounif', 'Beni Ounif', 8),
(99, '901', 'البليدة', 'Blida', 'Blida', 9),
(100, '903', 'بوعينان', 'Bouinan', 'Bouinan', 9),
(101, '904', 'وادي العلايق', 'Oued El Alleug', 'Oued El Alleug', 9),
(102, '905', 'أولاد يعيش', 'Ouled Yaich', 'Ouled Yaich', 9),
(103, '907', 'العفرون', 'El Affroun', 'El Affroun', 9),
(104, '912', 'موزاية', 'Mouzaia', 'Mouzaia', 9),
(105, '914', 'مفتاح', 'Meftah', 'Meftah', 9),
(106, '916', 'بوفاريك', 'Boufarik', 'Boufarik', 9),
(107, '917', 'الأربعاء', 'Larbaa', 'Larbaa', 9),
(108, '922', 'بوقرة', 'Bougara', 'Bougara', 9),
(109, '1001', 'البويرة', 'Bouira', 'Bouira', 10),
(110, '1004', 'سوق الخميس', 'Souk El Khemis', 'Souk El Khemis', 10),
(111, '1005', 'القادرية', 'Kadiria', 'Kadiria', 10),
(112, '1012', 'الحيزر', 'Haizer', 'Haizer', 10),
(113, '1013', 'الأخضرية', 'Lakhdaria', 'Lakhdaria', 10),
(114, '1015', 'الهاشمية', 'El Hachimia', 'El Hachimia', 10),
(115, '1018', 'برج أوخريص', 'Bordj Okhriss', 'Bordj Okhriss', 10),
(116, '1033', 'بشلول', 'Bechloul', 'Bechloul', 10),
(117, '1035', 'عين بسام', 'Ain Bessem', 'Ain Bessem', 10),
(118, '1036', 'بئر غبالو', 'Bir Ghbalou', 'Bir Ghbalou', 10),
(119, '1037', 'مشد الله', 'M\'chedallah', 'M\'chedallah', 10),
(120, '1038', 'سور الغزلان', 'Sour El Ghozlane', 'Sour El Ghozlane', 10),
(121, '1101', 'تمنراست', 'Tamanrasset', 'Tamanrasset', 11),
(122, '1102', 'سيلت', 'Silet', 'Silet', 11),
(123, '1106', 'تاظروك', 'Tazrouk', 'Tazrouk', 11),
(124, '1201', 'تبسة', 'Tebessa', 'Tebessa', 12),
(125, '1202', 'بئر العاتر', 'Bir El Ater', 'Bir El Ater', 12),
(126, '1203', 'الشريعة', 'Cheria', 'Cheria', 12),
(127, '1205', 'العوينات', 'El Aouinet', 'El Aouinet', 12),
(128, '1209', 'نقرين', 'Negrine', 'Negrine', 12),
(129, '1210', 'بئر مقدم', 'Bir Mokadem', 'Bir Mokadem', 12),
(130, '1211', 'الكويف', 'El Kouif', 'El Kouif', 12),
(131, '1212', 'مرسط', 'Morsott', 'Morsott', 12),
(132, '1213', 'العقلة', 'El Ogla', 'El Ogla', 12),
(133, '1219', 'الونزة', 'Ouenza', 'Ouenza', 12),
(134, '1220', 'الماء الابيض', 'El Malabiod', 'El Malabiod', 12),
(135, '1221', 'أم علي', 'Oum Ali', 'Oum Ali', 12),
(136, '1301', 'تلمسان', 'Tlemcen', 'Tlemcen', 13),
(137, '1303', 'عين تالوت', 'Ain Tellout', 'Ain Tellout', 13),
(138, '1304', 'الرمشي', 'Remchi', 'Remchi', 13),
(139, '1306', 'صبرة', 'Sabra', 'Sabra', 13),
(140, '1307', 'الغزوات', 'Ghazaouet', 'Ghazaouet', 13),
(141, '1313', 'أولاد ميمون', 'Ouled Mimoun', 'Ouled Mimoun', 13),
(142, '1317', 'بني سنوس', 'Beni Snous', 'Beni Snous', 13),
(143, '1318', 'باب العسة', 'Bab El Assa', 'Bab El Assa', 13),
(144, '1320', 'فلاوسن', 'Fellaoucene', 'Fellaoucene', 13),
(145, '1324', 'بن سكران', 'Bensekrane', 'Bensekrane', 13),
(146, '1326', 'الحناية', 'Hennaya', 'Hennaya', 13),
(147, '1327', 'مغنية', 'Maghnia', 'Maghnia', 13),
(148, '1335', 'سبدو', 'Sebdou', 'Sebdou', 13),
(149, '1338', 'بني بوسعيد', 'Beni Boussaid', 'Beni Boussaid', 13),
(150, '1339', 'مرسى بن مهيدي', 'Marsa Ben Mehdi', 'Marsa Ben Mehdi', 13),
(151, '1340', 'ندرومة', 'Nedroma', 'Nedroma', 13),
(152, '1341', 'سيدي الجيلالي', 'Sidi Djillali', 'Sidi Djillali', 13),
(153, '1344', 'هنين', 'Honnaine', 'Honnaine', 13),
(154, '1350', 'شتوان', 'Chetouane', 'Chetouane', 13),
(155, '1351', 'منصورة', 'Mansourah', 'Mansourah', 13),
(156, '1401', 'تيارت', 'Tiaret', 'Tiaret', 14),
(157, '1402', 'مدروسة', 'Medroussa', 'Medroussa', 14),
(158, '1406', 'عين الذهب', 'Ain Deheb', 'Ain Deheb', 14),
(159, '1413', 'دحموني', 'Dahmouni', 'Dahmouni', 14),
(160, '1414', 'رحوية', 'Rahouia', 'Rahouia', 14),
(161, '1415', 'مهدية', 'Mahdia', 'Mahdia', 14),
(162, '1416', 'السوقر', 'Sougueur', 'Sougueur', 14),
(163, '1421', 'مغيلة', 'Meghila', 'Meghila', 14),
(164, '1427', 'فرندة', 'Frenda', 'Frenda', 14),
(165, '1428', 'عين كرمس', 'Ain Kermes', 'Ain Kermes', 14),
(166, '1429', 'قصر الشلالة', 'Ksar Chellala', 'Ksar Chellala', 14),
(167, '1433', 'وادي ليلي', 'Oued Lili', 'Oued Lili', 14),
(168, '1434', 'مشرع الصفا', 'Mechraa Sfa', 'Mechraa Sfa', 14),
(169, '1435', 'حمادية', 'Hamadia', 'Hamadia', 14),
(170, '1501', 'تيزي وزو', 'Tizi Ouzou', 'Tizi Ouzou', 15),
(171, '1502', 'عين الحمام', 'Ain El Hammam', 'Ain El Hammam', 15),
(172, '1509', 'ماكودة', 'Makouda', 'Makouda', 15),
(173, '1510', 'ذراع الميزان', 'Draa El Mizan', 'Draa El Mizan', 15),
(174, '1511', 'تيزي غنيف', 'Tizi-Ghenif', 'Tizi-Ghenif', 15),
(175, '1517', 'إفرحونان', 'Iferhounene', 'Iferhounene', 15),
(176, '1518', 'عزازقة', 'Azazga', 'Azazga', 15),
(177, '1521', 'الأربعاء ناث إيراثن', 'Larbaa Nath Iraten', 'Larbaa Nath Iraten', 15),
(178, '1522', 'تيزي راشد', 'Tizi Rached', 'Tizi Rached', 15),
(179, '1524', 'واقنون', 'Ouaguenoun', 'Ouaguenoun', 15),
(180, '1529', 'معاتقة', 'Maatkas', 'Maatkas', 15),
(181, '1532', 'بني دوالة', 'Beni Douala', 'Beni Douala', 15),
(182, '1534', 'بوزقن', 'Bouzeguene', 'Bouzeguene', 15),
(183, '1536', 'واضية', 'Ouadhias', 'Ouadhias', 15),
(184, '1537', 'أزفون', 'Azeffoun', 'Azeffoun', 15),
(185, '1538', 'تيقزيرت', 'Tigzirt', 'Tigzirt', 15),
(186, '1540', 'بوغني', 'Boghni', 'Boghni', 15),
(187, '1547', 'ذراع بن خدة', 'Draa Ben Khedda', 'Draa Ben Khedda', 15),
(188, '1548', 'واسيف', 'Ouacif', 'Ouacif', 15),
(189, '1550', 'مقلع', 'Mekla', 'Mekla', 15),
(190, '1552', 'بني يني', 'Benni Yenni', 'Benni Yenni', 15),
(191, '1602', 'سيدي امحمد', 'Sidi M\'hamed', 'Sidi M\'hamed', 16),
(192, '1605', 'باب الوادي', 'Bab El Oued', 'Bab El Oued', 16),
(193, '1609', 'بئر مراد رايس', 'Bir Mourad Rais', 'Bir Mourad Rais', 16),
(194, '1611', 'بوزريعة', 'Bouzareah', 'Bouzareah', 16),
(195, '1613', 'الحراش', 'El Harrach', 'El Harrach', 16),
(196, '1614', 'براقي', 'Baraki', 'Baraki', 16),
(197, '1617', 'حسين داي', 'Hussein Dey', 'Hussein Dey', 16),
(198, '1620', 'الدار البيضاء', 'Dar El Beida', 'Dar El Beida', 16),
(199, '1636', 'بئر توتة', 'Birtouta', 'Birtouta', 16),
(200, '1638', 'الرويبة', 'Rouiba', 'Rouiba', 16),
(201, '1644', 'زرالدة', 'Zeralda', 'Zeralda', 16),
(202, '1649', 'الدرارية', 'Draria', 'Draria', 16),
(203, '1652', 'الشراقة', 'Cheraga', 'Cheraga', 16),
(204, '1701', 'الجلفة', 'Djelfa', 'Djelfa', 17),
(205, '1704', 'حاسي بحبح', 'Hassi Bahbah', 'Hassi Bahbah', 17),
(206, '1707', 'فيض البطمة', 'Faidh El Botma', 'Faidh El Botma', 17),
(207, '1708', 'بيرين', 'Birine', 'Birine', 17),
(208, '1714', 'الادريسية', 'El Idrissia', 'El Idrissia', 17),
(209, '1717', 'مسعد', 'Messaad', 'Messaad', 17),
(210, '1719', 'سيدي لعجال', 'Sidi Laadjel', 'Sidi Laadjel', 17),
(211, '1720', 'حد الصحاري', 'Had Sahary', 'Had Sahary', 17),
(212, '1725', 'دار الشيوخ', 'Dar Chioukh', 'Dar Chioukh', 17),
(213, '1726', 'الشارف', 'Charef', 'Charef', 17),
(214, '1730', 'عين الإبل', 'Ain El Ibel', 'Ain El Ibel', 17),
(215, '1731', 'عين وسارة', 'Ain Oussera', 'Ain Oussera', 17),
(216, '1801', 'جيجل', 'Jijel', 'Jijel', 18),
(217, '1803', 'العوانة', 'El Aouana', 'El Aouana', 18),
(218, '1804', 'زيامة منصورية', 'Ziamah Mansouriah', 'Ziamah Mansouriah', 18),
(219, '1805', 'الطاهير', 'Taher', 'Taher', 18),
(220, '1807', 'الشقفة', 'Chekfa', 'Chekfa', 18),
(221, '1809', 'الميلية', 'El Milia', 'El Milia', 18),
(222, '1810', 'سيدي معروف', 'Sidi Marouf', 'Sidi Marouf', 18),
(223, '1811', 'السطارة', 'Settara', 'Settara', 18),
(224, '1812', 'العنصر', 'El Ancer', 'El Ancer', 18),
(225, '1817', 'جيملة', 'Djimla', 'Djimla', 18),
(226, '1824', 'تاكسنة', 'Texenna', 'Texenna', 18),
(227, '1901', 'سطيف', 'Setif', 'Setif', 19),
(228, '1902', 'عين الكبيرة', 'Ain El Kebira', 'Ain El Kebira', 19),
(229, '1903', 'بني عزيز', 'Beni Aziz', 'Beni Aziz', 19),
(230, '1908', 'بئر العرش', 'Bir El Arch', 'Bir El Arch', 19),
(231, '1916', 'بابور', 'Babor', 'Babor', 19),
(232, '1917', 'قجال', 'Guidjel', 'Guidjel', 19),
(233, '1920', 'العلمة', 'El Eulma', 'El Eulma', 19),
(234, '1921', 'جميلة', 'Djemila', 'Djemila', 19),
(235, '1922', 'بني ورتيلان', 'Beni Ourtilane', 'Beni Ourtilane', 19),
(236, '1926', 'عين أرنات', 'Ain Arnat', 'Ain Arnat', 19),
(237, '1927', 'عموشة', 'Amoucha', 'Amoucha', 19),
(238, '1928', 'عين ولمان', 'Ain Oulmene', 'Ain Oulmene', 19),
(239, '1930', 'بوعنداس', 'Bouandas', 'Bouandas', 19),
(240, '1932', 'حمام السخنة', 'Hammam Sokhna', 'Hammam Sokhna', 19),
(241, '1939', 'صالح باي', 'Salah Bey', 'Salah Bey', 19),
(242, '1940', 'عين أزال', 'Ain Azel', 'Ain Azel', 19),
(243, '1941', 'قنزات', 'Guenzet', 'Guenzet', 19),
(244, '1943', 'بوقاعة', 'Bougaa', 'Bougaa', 19),
(245, '1950', 'حمام قرقور', 'Hammam Guergour', 'Hammam Guergour', 19),
(246, '1955', 'ماوكلان', 'Maoklane', 'Maoklane', 19),
(247, '2001', 'سعيدة', 'Saida', 'Saida', 20),
(248, '2003', 'عين الحجر', 'Ain El Hadjar', 'Ain El Hadjar', 20),
(249, '2006', 'يوب', 'Youb', 'Youb', 20),
(250, '2009', 'سيدي بوبكر', 'Sidi Boubekeur', 'Sidi Boubekeur', 20),
(251, '2010', 'الحساسنة', 'El Hassasna', 'El Hassasna', 20),
(252, '2014', 'أولاد ابراهيم', 'Ouled Brahim', 'Ouled Brahim', 20),
(253, '2101', 'سكيكدة', 'Skikda', 'Skikda', 21),
(254, '2103', 'الحدائق', 'El Hadaiek', 'El Hadaiek', 21),
(255, '2104', 'عزابة', 'Azzaba', 'Azzaba', 21),
(256, '2108', 'بن عزوز', 'Ben Azzouz', 'Ben Azzouz', 21),
(257, '2110', 'القل', 'Collo', 'Collo', 21),
(258, '2113', 'أولاد عطية', 'Ouled Attia', 'Ouled Attia', 21),
(259, '2115', 'الزيتونة', 'Zitouna', 'Zitouna', 21),
(260, '2116', 'الحروش', 'El Harrouch', 'El Harrouch', 21),
(261, '2119', 'سيدي مزغيش', 'Sidi Mezghiche', 'Sidi Mezghiche', 21),
(262, '2123', 'رمضان جمال', 'Ramdane Djamel', 'Ramdane Djamel', 21),
(263, '2126', 'تمالوس', 'Tamalous', 'Tamalous', 21),
(264, '2127', 'عين قشرة', 'Ain Kechra', 'Ain Kechra', 21),
(265, '2128', 'أم الطوب', 'Oum Toub', 'Oum Toub', 21),
(266, '2201', 'سيدي بلعباس', 'Sidi Bel Abbes', 'Sidi Bel Abbes', 22),
(267, '2202', 'تسالة', 'Tessala', 'Tessala', 22),
(268, '2204', 'مصطفى بن ابراهيم', 'Mostefa  Ben Brahim', 'Mostefa  Ben Brahim', 22),
(269, '2205', 'تلاغ', 'Telagh', 'Telagh', 22),
(270, '2208', 'سيدي علي بوسيدي', 'Sidi Ali Boussidi', 'Sidi Ali Boussidi', 22),
(271, '2210', 'مرحوم', 'Marhoum', 'Marhoum', 22),
(272, '2214', 'سيدي لحسن', 'Sidi Lahcene', 'Sidi Lahcene', 22),
(273, '2217', 'تنيرة', 'Tenira', 'Tenira', 22),
(274, '2218', 'مولاي سليسن', 'Moulay Slissen', 'Moulay Slissen', 22),
(275, '2222', 'مرين', 'Merine', 'Merine', 22),
(276, '2223', 'راس الماء', 'Ras El Ma', 'Ras El Ma', 22),
(277, '2228', 'عين البرد', 'Ain El Berd', 'Ain El Berd', 22),
(278, '2229', 'سفيزف', 'Sfisef', 'Sfisef', 22),
(279, '2245', 'بن باديس', 'Ben Badis', 'Ben Badis', 22),
(280, '2246', 'سيدي علي بن يوب', 'Sidi Ali Ben Youb', 'Sidi Ali Ben Youb', 22),
(281, '2301', 'عنابة', 'Annaba', 'Annaba', 23),
(282, '2302', 'برحال', 'Berrahal', 'Berrahal', 23),
(283, '2303', 'الحجار', 'El Hadjar', 'El Hadjar', 23),
(284, '2305', 'البوني', 'El Bouni', 'El Bouni', 23),
(285, '2309', 'عين الباردة', 'Ain El Berda', 'Ain El Berda', 23),
(286, '2310', 'شطايبي', 'Chetaibi', 'Chetaibi', 23),
(287, '2401', 'قالمة', 'Guelma', 'Guelma', 24),
(288, '2404', 'وادي الزناتي', 'Oued Zenati', 'Oued Zenati', 24),
(289, '2413', 'عين مخلوف', 'Ain Makhlouf', 'Ain Makhlouf', 24),
(290, '2415', 'خزارة', 'Khezaras', 'Khezaras', 24),
(291, '2418', 'قلعة بوصبع', 'Guelaat Bousbaa', 'Guelaat Bousbaa', 24),
(292, '2419', 'حمام دباغ', 'Hammam Debagh', 'Hammam Debagh', 24),
(293, '2422', 'حمام النبايل', 'Hammam N\'bails', 'Hammam N\'bails', 24),
(294, '2425', 'بوشقوف', 'Bouchegouf', 'Bouchegouf', 24),
(295, '2426', 'هيليوبوليس', 'Heliopolis', 'Heliopolis', 24),
(296, '2427', 'عين حساينية', 'Ain Hessainia', 'Ain Hessainia', 24),
(297, '2501', 'قسنطينة', 'Constantine', 'Constantine', 25),
(298, '2502', 'حامة بوزيان', 'Hamma Bouziane', 'Hamma Bouziane', 25),
(299, '2504', 'زيغود يوسف', 'Zighoud Youcef', 'Zighoud Youcef', 25),
(300, '2506', 'الخروب', 'El Khroub', 'El Khroub', 25),
(301, '2507', 'عين عبيد', 'Ain Abid', 'Ain Abid', 25),
(302, '2512', 'ابن زياد', 'Ibn Ziad', 'Ibn Ziad', 25),
(303, '2601', 'المدية', 'Medea', 'Medea', 26),
(304, '2602', 'وزرة', 'Ouzera', 'Ouzera', 26),
(305, '2604', 'عين بوسيف', 'Ain Boucif', 'Ain Boucif', 26),
(306, '2607', 'العمارية', 'El Omaria', 'El Omaria', 26),
(307, '2609', 'القلب الكبير', 'Guelb El Kebir', 'Guelb El Kebir', 26),
(308, '2618', 'شلالة العذاورة', 'Chellalat El Adhaoura', 'Chellalat El Adhaoura', 26),
(309, '2626', 'سيدي نعمان', 'Sidi Naamane', 'Sidi Naamane', 26),
(310, '2632', 'عزيز', 'Aziz', 'Aziz', 26),
(311, '2633', 'السواقي', 'Souaghi', 'Souaghi', 26),
(312, '2635', 'قصر البخاري', 'Ksar El Boukhari', 'Ksar El Boukhari', 26),
(313, '2636', 'العزيزية', 'El Azizia', 'El Azizia', 26),
(314, '2638', 'الشهبونية', 'Chahbounia', 'Chahbounia', 26),
(315, '2643', 'عوامري', 'Ouamri', 'Ouamri', 26),
(316, '2644', 'سي المحجوب', 'Si Mahdjoub', 'Si Mahdjoub', 26),
(317, '2646', 'بني سليمان', 'Beni Slimane', 'Beni Slimane', 26),
(318, '2647', 'البرواقية', 'Berrouaghia', 'Berrouaghia', 26),
(319, '2648', 'سغوان', 'Seghouane', 'Seghouane', 26),
(320, '2652', 'تابلاط', 'Tablat', 'Tablat', 26),
(321, '2658', 'أولاد عنتر', 'Ouled Antar', 'Ouled Antar', 26),
(322, '2701', 'مستغانم', 'Mostaganem', 'Mostaganem', 27),
(323, '2705', 'عين نويسي', 'Ain Nouicy', 'Ain Nouicy', 27),
(324, '2706', 'حاسي ماماش', 'Hassi Mameche', 'Hassi Mameche', 27),
(325, '2707', 'عين تادلس', 'Ain Tedeles', 'Ain Tedeles', 27),
(326, '2711', 'خير الدين', 'Kheir Eddine', 'Kheir Eddine', 27),
(327, '2712', 'سيدي علي', 'Sidi Ali', 'Sidi Ali', 27),
(328, '2716', 'سيدي لخضر', 'Sidi Lakhdar', 'Sidi Lakhdar', 27),
(329, '2717', 'عشعاشة', 'Achaacha', 'Achaacha', 27),
(330, '2719', 'بوقيراط', 'Bouguirat', 'Bouguirat', 27),
(331, '2722', 'ماسرة', 'Mesra', 'Mesra', 27),
(332, '2801', 'المسيلة', 'M\'sila', 'M\'sila', 28),
(333, '2803', 'حمام الضلعة', 'Hammam Dalaa', 'Hammam Dalaa', 28),
(334, '2804', 'أولاد دراج', 'Ouled Derradj', 'Ouled Derradj', 28),
(335, '2807', 'خبانة', 'Khoubana', 'Khoubana', 28),
(336, '2809', 'شلال', 'Chellal', 'Chellal', 28),
(337, '2811', 'مقرة', 'Magra', 'Magra', 28),
(338, '2816', 'سيدي عيسى', 'Sidi Aissa', 'Sidi Aissa', 28),
(339, '2817', 'عين الحجل', 'Ain El Hadjel', 'Ain El Hadjel', 28),
(340, '2820', 'بوسعادة', 'Bousaada', 'Bousaada', 28),
(341, '2821', 'أولاد سيدي ابراهيم', 'Ouled Sidi Brahim', 'Ouled Sidi Brahim', 28),
(342, '2822', 'سيدي عامر', 'Sidi Ameur', 'Sidi Ameur', 28),
(343, '2824', 'بن سرور', 'Ben Srour', 'Ben Srour', 28),
(344, '2841', 'عين الملح', 'Ain El Melh', 'Ain El Melh', 28),
(345, '2842', 'امجدل', 'Medjedel', 'Medjedel', 28),
(346, '2847', 'جبل مساعد', 'Djebel Messaad', 'Djebel Messaad', 28),
(347, '2901', 'معسكر', 'Mascara', 'Mascara', 29),
(348, '2902', 'بوحنيفية', 'Bouhanifia', 'Bouhanifia', 29),
(349, '2903', 'تيزي', 'Tizi', 'Tizi', 29),
(350, '2906', 'تيغنيف', 'Tighennif', 'Tighennif', 29),
(351, '2907', 'الحشم', 'Hachem', 'Hachem', 29),
(352, '2910', 'وادي الأبطال', 'Oued El Abtal', 'Oued El Abtal', 29),
(353, '2912', 'غريس', 'Ghriss', 'Ghriss', 29),
(354, '2917', 'البرج', 'El Bordj', 'El Bordj', 29),
(355, '2918', 'عين فكان', 'Ain Fekan', 'Ain Fekan', 29),
(356, '2922', 'وادي التاغية', 'Oued Taria', 'Oued Taria', 29),
(357, '2923', 'عوف', 'Aouf', 'Aouf', 29),
(358, '2924', 'عين فارس', 'Ain Fares', 'Ain Fares', 29),
(359, '2926', 'سيق', 'Sig', 'Sig', 29),
(360, '2927', 'عقاز', 'Oggaz', 'Oggaz', 29),
(361, '2930', 'زهانة', 'Zahana', 'Zahana', 29),
(362, '2931', 'المحمدية', 'Mohammadia', 'Mohammadia', 29),
(363, '3001', 'ورقلة', 'Ouargla', 'Ouargla', 30),
(364, '3003', 'انقوسة', 'N\'goussa', 'N\'goussa', 30),
(365, '3004', 'حاسي مسعود', 'Hassi Messaoud', 'Hassi Messaoud', 30),
(366, '3011', 'سيدي خويلد', 'Sidi Khouiled', 'Sidi Khouiled', 30),
(367, '3021', 'البرمة', 'El Borma', 'El Borma', 30),
(368, '3101', 'وهران', 'Oran', 'Oran', 31),
(369, '3102', 'قديل', 'Gdyel', 'Gdyel', 31),
(370, '3103', 'بئر الجير', 'Bir El Djir', 'Bir El Djir', 31),
(371, '3105', 'السانية', 'Es Senia', 'Es Senia', 31),
(372, '3106', 'أرزيو', 'Arzew', 'Arzew', 31),
(373, '3107', 'بطيوة', 'Bethioua', 'Bethioua', 31),
(374, '3109', 'عين الترك', 'Ain Turk', 'Ain Turk', 31),
(375, '3111', 'وادي تليلات', 'Oued Tlelat', 'Oued Tlelat', 31),
(376, '3124', 'بوتليليس', 'Boutlelis', 'Boutlelis', 31),
(377, '3201', 'البيض', 'El Bayadh', 'El Bayadh', 32),
(378, '3202', 'رقاصة', 'Rogassa', 'Rogassa', 32),
(379, '3203', 'بريزينة', 'Brezina', 'Brezina', 32),
(380, '3204', 'بوعلام', 'Boualem', 'Boualem', 32),
(381, '3205', 'الأبيض سيدي الشيخ', 'Labiodh Sidi Cheikh', 'Labiodh Sidi Cheikh', 32),
(382, '3206', 'بوقطب', 'Bougtoub', 'Bougtoub', 32),
(383, '3207', 'بوسمغون', 'Boussemghoun', 'Boussemghoun', 32),
(384, '3208', 'شلالة', 'Chellala', 'Chellala', 32),
(385, '3301', 'إيليزي', 'Illizi', 'Illizi', 33),
(386, '3306', 'إن أمناس', 'In Amenas', 'In Amenas', 33),
(387, '3401', 'برج بوعريريج', 'Bordj Bou Arreridj', 'Bordj Bou Arreridj', 34),
(388, '3402', 'رأس الوادي', 'Ras El Oued', 'Ras El Oued', 34),
(389, '3403', 'برج زمورة', 'Bordj Zemmoura', 'Bordj Zemmoura', 34),
(390, '3404', 'المنصورة', 'Mansourah', 'Mansourah', 34),
(391, '3408', 'عين تاغروت', 'Ain Taghrout', 'Ain Taghrout', 34),
(392, '3409', 'برج الغدير', 'Bordj Ghedir', 'Bordj Ghedir', 34),
(393, '3411', 'الحمادية', 'El Hamadia', 'El Hamadia', 34),
(394, '3413', 'مجانة', 'Medjana', 'Medjana', 34),
(395, '3415', 'جعافرة', 'Djaafra', 'Djaafra', 34),
(396, '3431', 'بئر قاصد علي', 'Bir Kasdali', 'Bir Kasdali', 34),
(397, '3501', 'بومرداس', 'Boumerdes', 'Boumerdes', 35),
(398, '3502', 'بودواو', 'Boudouaou', 'Boudouaou', 35),
(399, '3504', 'برج منايل', 'Bordj Menaiel', 'Bordj Menaiel', 35),
(400, '3505', 'بغلية', 'Baghlia', 'Baghlia', 35),
(401, '3507', 'الناصرية', 'Naciria', 'Naciria', 35),
(402, '3509', 'يسر', 'Isser', 'Isser', 35),
(403, '3514', 'الثنية', 'Thenia', 'Thenia', 35),
(404, '3523', 'دلس', 'Dellys', 'Dellys', 35),
(405, '3531', 'خميس الخشنة', 'Khemis El Khechna', 'Khemis El Khechna', 35),
(406, '3601', 'الطارف', 'El Tarf', 'El Tarf', 36),
(407, '3602', 'بوحجار', 'Bouhadjar', 'Bouhadjar', 36),
(408, '3603', 'بن مهيدي', 'Ben M\'hidi', 'Ben M\'hidi', 36),
(409, '3605', 'القالة', 'El Kala', 'El Kala', 36),
(410, '3608', 'بوثلجة', 'Bouteldja', 'Bouteldja', 36),
(411, '3613', 'الذرعان', 'Drean', 'Drean', 36),
(412, '3616', 'البسباس', 'Besbes', 'Besbes', 36),
(413, '3701', 'تندوف', 'Tindouf', 'Tindouf', 37),
(414, '3801', 'تيسمسيلت', 'Tissemsilt', 'Tissemsilt', 38),
(415, '3802', 'برج بونعامة', 'Bordj Bounaama', 'Bordj Bounaama', 38),
(416, '3803', 'ثنية الاحد', 'Theniet El Had', 'Theniet El Had', 38),
(417, '3804', 'الأزهرية', 'Lazharia', 'Lazharia', 38),
(418, '3806', 'لرجام', 'Lardjem', 'Lardjem', 38),
(419, '3809', 'برج الأمير عبد القادر', 'Bordj Emir Abdelkader', 'Bordj Emir Abdelkader', 38),
(420, '3811', 'خميستي', 'Khemisti', 'Khemisti', 38),
(421, '3813', 'عماري', 'Ammari', 'Ammari', 38),
(422, '3901', 'الوادي', 'El Oued', 'El Oued', 39),
(423, '3902', 'الرباح', 'Robbah', 'Robbah', 39),
(424, '3904', 'البياضة', 'Bayadha', 'Bayadha', 39),
(425, '3906', 'قمار', 'Guemar', 'Guemar', 39),
(426, '3908', 'الرقيبة', 'Reguiba', 'Reguiba', 39),
(427, '3911', 'الدبيلة', 'Debila', 'Debila', 39),
(428, '3913', 'حاسي خليفة', 'Hassi Khalifa', 'Hassi Khalifa', 39),
(429, '3914', 'الطالب العربي', 'Taleb Larbi', 'Taleb Larbi', 39),
(430, '3918', 'المقرن', 'Magrane', 'Magrane', 39),
(431, '3926', 'اميه وانسة', 'Mih Ouensa', 'Mih Ouensa', 39),
(432, '4001', 'خنشلة', 'Khenchela', 'Khenchela', 40),
(433, '4003', 'قايس', 'Kais', 'Kais', 40),
(434, '4005', 'الحامة', 'El Hamma', 'El Hamma', 40),
(435, '4006', 'عين الطويلة', 'Ain Touila', 'Ain Touila', 40),
(436, '4008', 'بوحمامة', 'Bouhmama', 'Bouhmama', 40),
(437, '4011', 'ششار', 'Chechar', 'Chechar', 40),
(438, '4013', 'بابار', 'Babar', 'Babar', 40),
(439, '4016', 'أولاد رشاش', 'Ouled Rechache', 'Ouled Rechache', 40),
(440, '4101', 'سوق أهراس', 'Souk Ahras', 'Souk Ahras', 41),
(441, '4102', 'سدراتة', 'Sedrata', 'Sedrata', 41),
(442, '4104', 'المشروحة', 'Mechroha', 'Mechroha', 41),
(443, '4105', 'أولاد إدريس', 'Ouled Driss', 'Ouled Driss', 41),
(444, '4108', 'تاورة', 'Taoura', 'Taoura', 41),
(445, '4110', 'الحدادة', 'Haddada', 'Haddada', 41),
(446, '4112', 'المراهنة', 'Merahna', 'Merahna', 41),
(447, '4114', 'بئر بوحوش', 'Bir Bouhouche', 'Bir Bouhouche', 41),
(448, '4115', 'مداوروش', 'M\'daourouche', 'M\'daourouche', 41),
(449, '4116', 'أم العظايم', 'Oum El Adhaim', 'Oum El Adhaim', 41),
(450, '4201', 'تيبازة', 'Tipaza', 'Tipaza', 42),
(451, '4208', 'حجوط', 'Hadjout', 'Hadjout', 42),
(452, '4209', 'سيدي أعمر', 'Sidi Amar', 'Sidi Amar', 42),
(453, '4210', 'قوراية', 'Gouraya', 'Gouraya', 42),
(454, '4214', 'شرشال', 'Cherchell', 'Cherchell', 42),
(455, '4215', 'الداموس', 'Damous', 'Damous', 42),
(456, '4217', 'فوكة', 'Fouka', 'Fouka', 42),
(457, '4218', 'بواسماعيل', 'Bou Ismail', 'Bou Ismail', 42),
(458, '4219', 'أحمر العين', 'Ahmar El Ain', 'Ahmar El Ain', 42),
(459, '4224', 'القليعة', 'Kolea', 'Kolea', 42),
(460, '4301', 'ميلة', 'Mila', 'Mila', 43),
(461, '4302', 'فرجيوة', 'Ferdjioua', 'Ferdjioua', 43),
(462, '4303', 'شلغوم العيد', 'Chelghoum Laid', 'Chelghoum Laid', 43),
(463, '4306', 'التلاغمة', 'Teleghma', 'Teleghma', 43),
(464, '4308', 'تاجنانت', 'Tadjenanet', 'Tadjenanet', 43),
(465, '4310', 'وادي النجاء', 'Oued Endja', 'Oued Endja', 43),
(466, '4314', 'بوحاتم', 'Bouhatem', 'Bouhatem', 43),
(467, '4315', 'الرواشد', 'Rouached', 'Rouached', 43),
(468, '4317', 'القرارم قوقة', 'Grarem Gouga', 'Grarem Gouga', 43),
(469, '4318', 'سيدي مروان', 'Sidi Merouane', 'Sidi Merouane', 43),
(470, '4319', 'تسدان حدادة', 'Tassadane Haddada', 'Tassadane Haddada', 43),
(471, '4323', 'ترعي باينان', 'Terrai Bainen', 'Terrai Bainen', 43),
(472, '4330', 'عين البيضاء أحريش', 'Ain Beida Harriche', 'Ain Beida Harriche', 43),
(473, '4401', 'عين الدفلى', 'Ain Defla', 'Ain Defla', 44),
(474, '4402', 'مليانة', 'Miliana', 'Miliana', 44),
(475, '4403', 'بومدفع', 'Boumedfaa', 'Boumedfaa', 44),
(476, '4404', 'خميس', 'Khemis', 'Khemis', 44),
(477, '4405', 'حمام ريغة', 'Hammam Righa', 'Hammam Righa', 44),
(478, '4407', 'جليدة', 'Djelida', 'Djelida', 44),
(479, '4408', 'العامرة', 'El Amra', 'El Amra', 44),
(480, '4410', 'العطاف', 'El Attaf', 'El Attaf', 44),
(481, '4411', 'العبادية', 'El Abadia', 'El Abadia', 44),
(482, '4412', 'جندل', 'Djendel', 'Djendel', 44),
(483, '4414', 'عين الاشياخ', 'Ain Lechiakh', 'Ain Lechiakh', 44),
(484, '4416', 'الروينة', 'Rouina', 'Rouina', 44),
(485, '4422', 'برج الأمير خالد', 'Bordj El Emir Khaled', 'Bordj El Emir Khaled', 44),
(486, '4431', 'بطحية', 'Bathia', 'Bathia', 44),
(487, '4501', 'النعامة', 'Naama', 'Naama', 45),
(488, '4502', 'المشرية', 'Mecheria', 'Mecheria', 45),
(489, '4503', 'عين الصفراء', 'Ain Sefra', 'Ain Sefra', 45),
(490, '4505', 'سفيسيفة', 'Sfissifa', 'Sfissifa', 45),
(491, '4506', 'مغرار', 'Moghrar', 'Moghrar', 45),
(492, '4507', 'عسلة', 'Asla', 'Asla', 45),
(493, '4510', 'مكمن بن عمار', 'Mekmen Ben Amar', 'Mekmen Ben Amar', 45),
(494, '4601', 'عين تموشنت', 'Ain Temouchent', 'Ain Temouchent', 46),
(495, '4604', 'حمام بوحجر', 'Hammam Bou Hadjar', 'Hammam Bou Hadjar', 46),
(496, '4609', 'عين الأربعاء', 'Ain Larbaa', 'Ain Larbaa', 46),
(497, '4614', 'المالح', 'El Maleh', 'El Maleh', 46),
(498, '4619', 'العامرية', 'El Amria', 'El Amria', 46),
(499, '4622', 'عين الكيحل', 'Ain Kihel', 'Ain Kihel', 46),
(500, '4623', 'بني صاف', 'Beni Saf', 'Beni Saf', 46),
(501, '4625', 'ولهاصة الغرابة', 'Oulhassa Gheraba', 'Oulhassa Gheraba', 46),
(502, '4701', 'غرداية', 'Ghardaia', 'Ghardaia', 47),
(503, '4703', 'ضاية بن ضحوة', 'Dhayet Ben Dhahoua', 'Dhayet Ben Dhahoua', 47),
(504, '4704', 'بريان', 'Berriane', 'Berriane', 47),
(505, '4705', 'متليلي', 'Metlili', 'Metlili', 47),
(506, '4706', 'القرارة', 'El Guerrara', 'El Guerrara', 47),
(507, '4708', 'زلفانة', 'Zelfana', 'Zelfana', 47),
(508, '4710', 'بونورة', 'Bounoura', 'Bounoura', 47),
(509, '4801', 'غليزان', 'Relizane', 'Relizane', 48),
(510, '4802', 'وادي رهيو', 'Oued Rhiou', 'Oued Rhiou', 48),
(511, '4807', 'الحمادنة', 'El H\'madna', 'El H\'madna', 48),
(512, '4808', 'سيدي أمحمد بن علي', 'Sidi M\'hamed Ben Ali', 'Sidi M\'hamed Ben Ali', 48),
(513, '4811', 'عمي موسى', 'Ammi Moussa', 'Ammi Moussa', 48),
(514, '4812', 'زمورة', 'Zemmoura', 'Zemmoura', 48),
(515, '4814', 'جديوية', 'Djidiouia', 'Djidiouia', 48),
(516, '4817', 'المطمر', 'El Matmar', 'El Matmar', 48),
(517, '4819', 'عين طارق', 'Ain Tarek', 'Ain Tarek', 48),
(518, '4822', 'مازونة', 'Mazouna', 'Mazouna', 48),
(519, '4825', 'يلل', 'Yellel', 'Yellel', 48),
(520, '4827', 'الرمكة', 'Ramka', 'Ramka', 48),
(521, '4828', 'منداس', 'Mendes', 'Mendes', 48),
(522, '103', 'شروين', 'Charouine', 'Charouine', 49),
(523, '109', 'تيميمون', 'Timimoun', 'Timimoun', 49),
(524, '116', 'تنركوك', 'Tinerkouk', 'Tinerkouk', 49),
(525, '123', 'أوقروت', 'Aougrout', 'Aougrout', 49),
(526, '125', 'برج باجي مختار', 'Bordj Badji Mokhtar', 'Bordj Badji Mokhtar', 50),
(527, '705', 'أولاد جلال', 'Ouled Djellal', 'Ouled Djellal', 51),
(528, '708', 'سيدي  خالد', 'Sidi Khaled', 'Sidi Khaled', 51),
(529, '803', 'أولاد خضير', 'Ouled Khodeir', 'Ouled Khodeir', 52),
(530, '807', 'بني عباس', 'Beni Abbes', 'Beni Abbes', 52),
(531, '811', 'إقلي', 'Igli', 'Igli', 52),
(532, '814', 'الواتة', 'El Ouata', 'El Ouata', 52),
(533, '818', 'كرزاز', 'Kerzaz', 'Kerzaz', 52),
(534, '1103', 'إينغر', 'In Ghar', 'In Ghar', 53),
(535, '1108', 'عين صالح', 'In Salah', 'In Salah', 53),
(536, '1104', 'عين قزام', 'In Guezzam', 'In Guezzam', 54),
(537, '1107', 'تين زواتين', 'Tin Zouatine', 'Tin Zouatine', 54),
(538, '3013', 'تقرت', 'Touggourt', 'Touggourt', 55),
(539, '3014', 'الحجيرة', 'El-Hadjira', 'El-Hadjira', 55),
(540, '3015', 'الطيبات', 'Taibet', 'Taibet', 55),
(541, '3016', 'تماسين', 'Temacine', 'Temacine', 55),
(542, '3019', 'المقارين', 'Megarine', 'Megarine', 55),
(543, '3302', 'جانت', 'Djanet', 'Djanet', 56),
(544, '3927', 'المغير', 'El Meghaier', 'El Meghaier', 57),
(545, '3928', 'جامعة', 'Djamaa', 'Djamaa', 57),
(546, '4702', 'المنيعة', 'El Menia', 'El Menia', 58),
(547, '4713', 'المنصورة', 'Mansourah', 'Mansourah', 58);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_05`
--

CREATE TABLE `t_01_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_05`
--

INSERT INTO `t_01_02_05` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(1, '1', 'أدرار', 'Adrar', 'Adrar', 1),
(2, '6', 'بودة', 'Bouda', 'Bouda', 1),
(3, '13', 'أولاد أحمد تيمي', 'Ouled Ahmed Timmi', 'Ouled Ahmed Timmi', 1),
(4, '16', 'رقان', 'Reggane', 'Reggane', 2),
(5, '17', 'سالي', 'Sali', 'Sali', 2),
(6, '18', 'السبع', 'Sebaa', 'Sebaa', 3),
(7, '27', 'تسابيت', 'Tsabit', 'Tsabit', 3),
(8, '10', 'إن زغمير', 'In Zghmir', 'In Zghmir', 4),
(9, '28', 'زاوية كنتة', 'Zaouiet Kounta', 'Zaouiet Kounta', 4),
(10, '2', 'اقبلي', 'Akabli', 'Akabli', 5),
(11, '4', 'أولف', 'Aoulef', 'Aoulef', 5),
(12, '22', 'تيمقتن', 'Timekten', 'Timekten', 5),
(13, '26', 'تيت', 'Tit', 'Tit', 5),
(14, '9', 'فنوغيل', 'Fenoughil', 'Fenoughil', 6),
(15, '20', 'تامنطيط', 'Tamantit', 'Tamantit', 6),
(16, '21', 'تامست', 'Tamest', 'Tamest', 6),
(17, '39', 'الشلف', 'Chlef', 'Chlef', 7),
(18, '54', 'أم الدروع', 'Oum Drou', 'Oum Drou', 7),
(19, '55', 'سنجاس', 'Sendjas', 'Sendjas', 7),
(20, '56', 'سيدي عبد الرحمن', 'Sidi Abderrahmane', 'Sidi Abderrahmane', 8),
(21, '57', 'سيدي عكاشة', 'Sidi Akkacha', 'Sidi Akkacha', 8),
(22, '62', 'تنس', 'Tenes', 'Tenes', 8),
(23, '32', 'بني بوعتاب', 'Beni  Bouattab', 'Beni  Bouattab', 9),
(24, '42', 'الكريمية', 'El Karimia', 'El Karimia', 9),
(25, '44', 'حرشون', 'Harchoun', 'Harchoun', 9),
(26, '40', 'الظهرة', 'Dahra', 'Dahra', 10),
(27, '61', 'تاوقريت', 'Taougrit', 'Taougrit', 10),
(28, '33', 'بني حواء', 'Beni Haoua', 'Beni Haoua', 11),
(29, '37', 'بريرة', 'Breira', 'Breira', 11),
(30, '49', 'وادي قوسين', 'Oued Goussine', 'Oued Goussine', 11),
(31, '38', 'الشطية', 'Chettia', 'Chettia', 12),
(32, '46', 'الأبيض مجاجة', 'Labiod Medjadja', 'Labiod Medjadja', 12),
(33, '53', 'أولاد فارس', 'Ouled Fares', 'Ouled Fares', 12),
(34, '35', 'بوقادير', 'Boukadir', 'Boukadir', 13),
(35, '50', 'وادي سلي', 'Oued Sly', 'Oued Sly', 13),
(36, '58', 'الصبحة', 'Sobha', 'Sobha', 13),
(37, '31', 'بنايرية', 'Benairia', 'Benairia', 14),
(38, '36', 'بوزغاية', 'Bouzeghaia', 'Bouzeghaia', 14),
(39, '63', 'الزبوجة', 'Zeboudja', 'Zeboudja', 14),
(40, '29', 'أبو الحسن', 'Abou El Hassane', 'Abou El Hassane', 15),
(41, '59', 'تاجنة', 'Tadjena', 'Tadjena', 15),
(42, '60', 'تلعصة', 'Talassa', 'Talassa', 15),
(43, '43', 'المرسى', 'El Marsa', 'El Marsa', 16),
(44, '47', 'مصدق', 'Moussadek', 'Moussadek', 16),
(45, '34', 'بني راشد', 'Beni Rached', 'Beni Rached', 17),
(46, '48', 'وادي الفضة', 'Oued Fodda', 'Oued Fodda', 17),
(47, '51', 'أولاد عباس', 'Ouled Abbes', 'Ouled Abbes', 17),
(48, '41', 'الحجاج', 'El Hadjadj', 'El Hadjadj', 18),
(49, '52', 'أولاد بن عبد القادر', 'Ouled Ben Abdelkader', 'Ouled Ben Abdelkader', 18),
(50, '30', 'عين مران', 'Ain Merane', 'Ain Merane', 19),
(51, '45', 'الهرانفة', 'Herenfa', 'Herenfa', 19),
(52, '79', 'الأغواط', 'Laghouat', 'Laghouat', 20),
(53, '68', 'بن ناصر بن شهرة', 'Benacer Benchohra', 'Benacer Benchohra', 21),
(54, '78', 'قصر الحيران', 'Ksar El Hirane', 'Ksar El Hirane', 21),
(55, '70', 'العسافية', 'El Assafia', 'El Assafia', 22),
(56, '84', 'سيدي مخلوف', 'Sidi Makhlouf', 'Sidi Makhlouf', 22),
(57, '75', 'حاسي الدلاعة', 'Hassi Delaa', 'Hassi Delaa', 23),
(58, '76', 'حاسي الرمل', 'Hassi R\'mel', 'Hassi R\'mel', 23),
(59, '65', 'عين ماضي', 'Ain Madhi', 'Ain Madhi', 24),
(60, '72', 'الحويطة', 'El Haouaita', 'El Haouaita', 24),
(61, '77', 'الخنق', 'Kheneg', 'Kheneg', 24),
(62, '85', 'تاجموت', 'Tadjemout', 'Tadjemout', 24),
(63, '86', 'تاجرونة', 'Tadjrouna', 'Tadjrouna', 24),
(64, '66', 'عين سيدي علي', 'Ain Sidi Ali', 'Ain Sidi Ali', 25),
(65, '67', 'البيضاء', 'El Beidha', 'El Beidha', 25),
(66, '73', 'قلتة سيدي سعد', 'Gueltat Sidi Saad', 'Gueltat Sidi Saad', 25),
(67, '69', 'بريدة', 'Brida', 'Brida', 26),
(68, '74', 'الحاج مشري', 'Hadj Mechri', 'Hadj Mechri', 26),
(69, '87', 'تاويالة', 'Taouiala', 'Taouiala', 26),
(70, '71', 'الغيشة', 'El Ghicha', 'El Ghicha', 27),
(71, '64', 'أفلو', 'Aflou', 'Aflou', 28),
(72, '82', 'سبقاق', 'Sebgag', 'Sebgag', 28),
(73, '83', 'سيدي بوزيد', 'Sidi Bouzid', 'Sidi Bouzid', 28),
(74, '80', 'وادي مرة', 'Oued Morra', 'Oued Morra', 29),
(75, '81', 'وادي مزي', 'Oued M\'zi', 'Oued M\'zi', 29),
(76, '94', 'عين الزيتون', 'Ain Zitoun', 'Ain Zitoun', 30),
(77, '112', 'أم البواقي', 'Oum El Bouaghi', 'Oum El Bouaghi', 30),
(78, '89', 'عين البيضاء', 'Ain Beida', 'Ain Beida', 31),
(79, '96', 'بريش', 'Berriche', 'Berriche', 31),
(80, '116', 'الزرق', 'Zorg', 'Zorg', 31),
(81, '93', 'عين مليلة', 'Ain M\'lila', 'Ain M\'lila', 32),
(82, '109', 'أولاد قاسم', 'Ouled Gacem', 'Ouled Gacem', 32),
(83, '110', 'أولاد حملة', 'Ouled Hamla', 'Ouled Hamla', 32),
(84, '99', 'العامرية', 'El Amiria', 'El Amiria', 33),
(85, '114', 'سيقوس', 'Sigus', 'Sigus', 33),
(86, '88', 'عين ببوش', 'Ain Babouche', 'Ain Babouche', 34),
(87, '90', 'عين الديس', 'Ain Diss', 'Ain Diss', 34),
(88, '98', 'الضلعة', 'Dhalaa', 'Dhalaa', 35),
(89, '101', 'الجازية', 'El Djazia', 'El Djazia', 35),
(90, '92', 'عين كرشة', 'Ain Kercha', 'Ain Kercha', 36),
(91, '103', 'الحرملية', 'El Harmilia', 'El Harmilia', 36),
(92, '105', 'هنشير تومغني', 'Hanchir Toumghani', 'Hanchir Toumghani', 36),
(93, '104', 'فكيرينة', 'Fkirina', 'Fkirina', 37),
(94, '108', 'وادي نيني', 'Oued Nini', 'Oued Nini', 37),
(95, '97', 'بئر الشهداء', 'Bir Chouhada', 'Bir Chouhada', 38),
(96, '111', 'أولاد زواي', 'Ouled Zouai', 'Ouled Zouai', 38),
(97, '115', 'سوق نعمان', 'Souk Naamane', 'Souk Naamane', 38),
(98, '106', 'قصر الصباحي', 'Ksar Sbahi', 'Ksar Sbahi', 39),
(99, '95', 'بحير الشرقي', 'Behir Chergui', 'Behir Chergui', 40),
(100, '100', 'البلالة', 'El Belala', 'El Belala', 40),
(101, '107', 'مسكيانة', 'Meskiana', 'Meskiana', 40),
(102, '113', 'الرحية', 'Rahia', 'Rahia', 40),
(103, '91', 'عين فكرون', 'Ain Fekroun', 'Ain Fekroun', 41),
(104, '102', 'الفجوج بوغرارة سعودي', 'El Fedjoudj Boughrara Sa', 'El Fedjoudj Boughrara Sa', 41),
(105, '123', 'باتنة', 'Batna', 'Batna', 42),
(106, '136', 'فسديس', 'Fesdis', 'Fesdis', 42),
(107, '154', 'وادي الشعبة', 'Oued Chaaba', 'Oued Chaaba', 42),
(108, '141', 'حيدوسة', 'Hidoussa', 'Hidoussa', 43),
(109, '145', 'قصر بلزمة', 'Ksar Bellezma', 'Ksar Bellezma', 43),
(110, '152', 'مروانة', 'Merouana', 'Merouana', 43),
(111, '155', 'وادي الماء', 'Oued El Ma', 'Oued El Ma', 43),
(112, '147', 'لازرو', 'Lazrou', 'Lazrou', 44),
(113, '167', 'سريانة', 'Seriana', 'Seriana', 44),
(114, '177', 'زانة البيضاء', 'Zanet El Beida', 'Zanet El Beida', 44),
(115, '151', 'منعة', 'Menaa', 'Menaa', 45),
(116, '174', 'تغرغار', 'Tigharghar', 'Tigharghar', 45),
(117, '119', 'عين ياقوت', 'Ain Yagout', 'Ain Yagout', 46),
(118, '128', 'بومية', 'Boumia', 'Boumia', 46),
(119, '132', 'جرمة', 'Djerma', 'Djerma', 46),
(120, '135', 'المعذر', 'El Madher', 'El Madher', 46),
(121, '162', 'عيون العصافير', 'Ouyoun El Assafir', 'Ouyoun El Assafir', 47),
(122, '171', 'تازولت', 'Tazoult', 'Tazoult', 47),
(123, '127', 'بومقر', 'Boumagueur', 'Boumagueur', 48),
(124, '153', 'نقاوس', 'N Gaous', 'N Gaous', 48),
(125, '165', 'سفيان', 'Sefiane', 'Sefiane', 48),
(126, '120', 'أريس', 'Arris', 'Arris', 49),
(127, '173', 'تيغانمين', 'Tighanimine', 'Tighanimine', 49),
(128, '117', 'عين جاسر', 'Ain Djasser', 'Ain Djasser', 50),
(129, '134', 'الحاسي', 'El Hassi', 'El Hassi', 50),
(130, '166', 'سقانة', 'Seggana', 'Seggana', 51),
(131, '175', 'تيلاطو', 'Tilatou', 'Tilatou', 51),
(132, '137', 'فم الطوب', 'Foum Toub', 'Foum Toub', 52),
(133, '142', 'إشمول', 'Ichemoul', 'Ichemoul', 52),
(134, '143', 'إينوغيسن', 'Inoughissen', 'Inoughissen', 52),
(135, '129', 'بوزينة', 'Bouzina', 'Bouzina', 53),
(136, '146', 'لارباع', 'Larbaa', 'Larbaa', 53),
(137, '126', 'بولهيلات', 'Boulhilat', 'Boulhilat', 54),
(138, '130', 'الشمرة', 'Chemora', 'Chemora', 54),
(139, '122', 'بريكة', 'Barika', 'Barika', 55),
(140, '125', 'بيطام', 'Bitam', 'Bitam', 55),
(141, '149', 'إمدوكل', 'M Doukal', 'M Doukal', 55),
(142, '121', 'عزيل عبد القادر', 'Azil Abedelkader', 'Azil Abedelkader', 56),
(143, '133', 'الجزار', 'Djezzar', 'Djezzar', 56),
(144, '157', 'أولاد عمار', 'Ouled Ammar', 'Ouled Ammar', 56),
(145, '138', 'غسيرة', 'Ghassira', 'Ghassira', 57),
(146, '144', 'كيمل', 'Kimmel', 'Kimmel', 57),
(147, '168', 'تكوت', 'T Kout', 'T Kout', 57),
(148, '118', 'عين التوتة', 'Ain Touta', 'Ain Touta', 58),
(149, '124', 'بني فضالة الحقانية', 'Beni Foudhala El Hakania', 'Beni Foudhala El Hakania', 58),
(150, '150', 'معافة', 'Maafa', 'Maafa', 58),
(151, '158', 'أولاد عوف', 'Ouled Aouf', 'Ouled Aouf', 58),
(152, '131', 'شير', 'Chir', 'Chir', 59),
(153, '156', 'وادي الطاقة', 'Oued Taga', 'Oued Taga', 59),
(154, '172', 'ثنية العابد', 'Teniet El Abed', 'Teniet El Abed', 59),
(155, '159', 'أولاد فاضل', 'Ouled Fadel', 'Ouled Fadel', 60),
(156, '176', 'تيمقاد', 'Timgad', 'Timgad', 60),
(157, '139', 'القصبات', 'Gosbat', 'Gosbat', 61),
(158, '140', 'القيقبة', 'Guigba', 'Guigba', 61),
(159, '160', 'أولاد سلام', 'Ouled Sellem', 'Ouled Sellem', 61),
(160, '163', 'الرحبات', 'Rahbat', 'Rahbat', 61),
(161, '164', 'رأس العيون', 'Ras El Aioun', 'Ras El Aioun', 61),
(162, '169', 'تالخمت', 'Talkhamt', 'Talkhamt', 61),
(163, '148', 'لمسان', 'Lemcene', 'Lemcene', 62),
(164, '161', 'أولاد سي سليمان', 'Ouled Si Slimane', 'Ouled Si Slimane', 62),
(165, '170', 'تاكسلانت', 'Taxlent', 'Taxlent', 62),
(166, '187', 'بجاية', 'Bejaia', 'Bejaia', 63),
(167, '210', 'وادي غير', 'Oued Ghir', 'Oued Ghir', 63),
(168, '184', 'أميزور', 'Amizour', 'Amizour', 64),
(169, '188', 'بني جليل', 'Beni Djellil', 'Beni Djellil', 64),
(170, '202', 'فرعون', 'Feraoun', 'Feraoun', 64),
(171, '214', 'سمعون', 'Smaoun', 'Smaoun', 64),
(172, '226', 'تيمزريت', 'Timezrit', 'Timezrit', 65),
(173, '209', 'مالبو', 'Melbou', 'Melbou', 66),
(174, '215', 'سوق لإثنين', 'Souk El Tenine', 'Souk El Tenine', 66),
(175, '219', 'تامريجت', 'Tamridjet', 'Tamridjet', 66),
(176, '194', 'بوخليفة', 'Boukhelifa', 'Boukhelifa', 67),
(177, '217', 'تالة حمزة', 'Tala Hamza', 'Tala Hamza', 67),
(178, '224', 'تيشي', 'Tichy', 'Tichy', 67),
(179, '179', 'أيت رزين', 'Ait R\'zine', 'Ait R\'zine', 68),
(180, '204', 'إغيل علي', 'Ighil-Ali', 'Ighil-Ali', 68),
(181, '180', 'أيت إسماعيل', 'Ait-Smail', 'Ait-Smail', 69),
(182, '197', 'درقينة', 'Darguina', 'Darguina', 69),
(183, '221', 'تاسكريوت', 'Taskriout', 'Taskriout', 69),
(184, '185', 'أوقاس', 'Aokas', 'Aokas', 70),
(185, '228', 'تيزي نبربر', 'Tizi-N\'berber', 'Tizi-N\'berber', 70),
(186, '178', 'أدكار', 'Adekar', 'Adekar', 71),
(187, '189', 'بني كسيلة', 'Beni K\'sila', 'Beni K\'sila', 71),
(188, '220', 'تاوريرت إغيل', 'Taourit Ighil', 'Taourit Ighil', 71),
(189, '181', 'أقبو', 'Akbou', 'Akbou', 72),
(190, '195', 'شلاطة', 'Chellata', 'Chellata', 72),
(191, '205', 'اغرم', 'Ighram', 'Ighram', 72),
(192, '218', 'تامقرة', 'Tamokra', 'Tamokra', 72),
(193, '183', 'أمالو', 'Amalou', 'Amalou', 73),
(194, '193', 'بوحمزة', 'Bouhamza', 'Bouhamza', 73),
(195, '208', 'مسيسنة', 'M\'cisna', 'M\'cisna', 73),
(196, '211', 'صدوق', 'Seddouk', 'Seddouk', 73),
(197, '190', 'بني مليكش', 'Beni-Mallikeche', 'Beni-Mallikeche', 74),
(198, '192', 'بو جليل', 'Boudjellil', 'Boudjellil', 74),
(199, '222', 'تازمالت', 'Tazmalt', 'Tazmalt', 74),
(200, '182', 'أكفادو', 'Akfadou', 'Akfadou', 75),
(201, '196', 'شميني', 'Chemini', 'Chemini', 75),
(202, '216', 'سوق اوفلا', 'Souk Oufella', 'Souk Oufella', 75),
(203, '223', 'طيبان', 'Tibane', 'Tibane', 75),
(204, '186', 'برباشة', 'Barbacha', 'Barbacha', 76),
(205, '206', 'كنديرة', 'Kendira', 'Kendira', 76),
(206, '203', 'أوزلاقن', 'Ouzellaguen', 'Ouzellaguen', 77),
(207, '199', 'الفلاي', 'Leflaye', 'Leflaye', 78),
(208, '212', 'سيدي عياد', 'Sidi Ayad', 'Sidi Ayad', 78),
(209, '213', 'سيدي عيش', 'Sidi-Aich', 'Sidi-Aich', 78),
(210, '225', 'تيفرة', 'Tifra', 'Tifra', 78),
(211, '227', 'تينبدار', 'Tinebdar', 'Tinebdar', 78),
(212, '200', 'القصر', 'El Kseur', 'El Kseur', 79),
(213, '201', 'فناية الماثن', 'Fenaia Il Maten', 'Fenaia Il Maten', 79),
(214, '229', 'توجة', 'Toudja', 'Toudja', 79),
(215, '198', 'ذراع القايد', 'Dra El Caid', 'Dra El Caid', 80),
(216, '207', 'خراطة', 'Kherrata', 'Kherrata', 80),
(217, '191', 'بني معوش', 'Benimaouche', 'Benimaouche', 81),
(218, '233', 'بسكرة', 'Biskra', 'Biskra', 82),
(219, '243', 'الحاجب', 'El Hadjab', 'El Hadjab', 82),
(220, '230', 'عين الناقة', 'Ain Naga', 'Ain Naga', 83),
(221, '238', 'شتمة', 'Chetma', 'Chetma', 83),
(222, '244', 'الحوش', 'El Haouch', 'El Haouch', 83),
(223, '260', 'سيدي عقبة', 'Sidi Okba', 'Sidi Okba', 83),
(224, '251', 'مشونش', 'M\'chouneche', 'M\'chouneche', 84),
(225, '241', 'الفيض', 'El Feidh', 'El Feidh', 85),
(226, '248', 'خنقة سيدي ناجي', 'Khenguet Sidi Nadji', 'Khenguet Sidi Nadji', 85),
(227, '253', 'المزيرعة', 'Meziraa', 'Meziraa', 85),
(228, '262', 'زريبة الوادي', 'Zeribet El Oued', 'Zeribet El Oued', 85),
(229, '231', 'عين زعطوط', 'Ain Zaatout', 'Ain Zaatout', 86),
(230, '245', 'القنطرة', 'El Kantara', 'El Kantara', 86),
(231, '246', 'الوطاية', 'El Outaya', 'El Outaya', 87),
(232, '236', 'برانيس', 'Branis', 'Branis', 88),
(233, '239', 'جمورة', 'Djemorah', 'Djemorah', 88),
(234, '234', 'برج بن عزوز', 'Bordj Ben Azzouz', 'Bordj Ben Azzouz', 89),
(235, '235', 'بوشقرون', 'Bouchakroun', 'Bouchakroun', 89),
(236, '249', 'ليشانة', 'Lichana', 'Lichana', 89),
(237, '261', 'طولقة', 'Tolga', 'Tolga', 89),
(238, '250', 'ليوة', 'Lioua', 'Lioua', 90),
(239, '252', 'مخادمة', 'Mekhadma', 'Mekhadma', 90),
(240, '254', 'مليلي', 'M\'lili', 'M\'lili', 90),
(241, '256', 'أوماش', 'Oumache', 'Oumache', 90),
(242, '257', 'أورلال', 'Ourlal', 'Ourlal', 90),
(243, '242', 'الغروس', 'El Ghrous', 'El Ghrous', 91),
(244, '247', 'فوغالة', 'Foughala', 'Foughala', 91),
(245, '264', 'بشار', 'Bechar', 'Bechar', 92),
(246, '268', 'بوكايس', 'Boukais', 'Boukais', 93),
(247, '275', 'لحمر', 'Lahmar', 'Lahmar', 93),
(248, '278', 'موغل', 'Mogheul', 'Mogheul', 93),
(249, '272', 'القنادسة', 'Kenadsa', 'Kenadsa', 94),
(250, '277', 'المريجة', 'Meridja', 'Meridja', 94),
(251, '280', 'تبلبالة', 'Tabelbala', 'Tabelbala', 95),
(252, '281', 'تاغيت', 'Taghit', 'Taghit', 96),
(253, '263', 'العبادلة', 'Abadla', 'Abadla', 97),
(254, '270', 'عرق فراج', 'Erg-Ferradj', 'Erg-Ferradj', 97),
(255, '276', 'مشرع هواري بومدين', 'Machraa-Houari-Boumediene', 'Machraa-Houari-Boumediene', 97),
(256, '267', 'بني ونيف', 'Beni-Ounif', 'Beni-Ounif', 98),
(257, '288', 'البليدة', 'Blida', 'Blida', 99),
(258, '289', 'بوعرفة', 'Bouarfa', 'Bouarfa', 99),
(259, '292', 'بوعينان', 'Bouinan', 'Bouinan', 100),
(260, '293', 'الشبلي', 'Chebli', 'Chebli', 100),
(261, '286', 'بني تامو', 'Beni-Tamou', 'Beni-Tamou', 101),
(262, '287', 'بن خليل', 'Benkhelil', 'Benkhelil', 101),
(263, '304', 'وادي العلايق', 'Oued El Alleug', 'Oued El Alleug', 101),
(264, '285', 'بني مراد', 'Beni Mered', 'Beni Mered', 102),
(265, '295', 'الشريعة', 'Chrea', 'Chrea', 102),
(266, '306', 'أولاد يعيش', 'Ouled Yaich', 'Ouled Yaich', 102),
(267, '297', 'العفرون', 'El-Affroun', 'El-Affroun', 103),
(268, '303', 'وادي جر', 'Oued  Djer', 'Oued  Djer', 103),
(269, '284', 'عين الرمانة', 'Ain Romana', 'Ain Romana', 104),
(270, '294', 'الشفة', 'Chiffa', 'Chiffa', 104),
(271, '302', 'موزاية', 'Mouzaia', 'Mouzaia', 104),
(272, '296', 'جبابرة', 'Djebabra', 'Djebabra', 105),
(273, '301', 'مفتاح', 'Meftah', 'Meftah', 105),
(274, '290', 'بوفاريك', 'Boufarik', 'Boufarik', 106),
(275, '298', 'قرواو', 'Guerrouaou', 'Guerrouaou', 106),
(276, '308', 'الصومعة', 'Soumaa', 'Soumaa', 106),
(277, '300', 'الأربعاء', 'Larbaa', 'Larbaa', 107),
(278, '307', 'صوحان', 'Souhane', 'Souhane', 107),
(279, '291', 'بوقرة', 'Bougara', 'Bougara', 108),
(280, '299', 'حمام ملوان', 'Hammam Elouane', 'Hammam Elouane', 108),
(281, '305', 'اولاد سلامة', 'Ouled Slama', 'Ouled Slama', 108),
(282, '313', 'عين الترك', 'Ain Turk', 'Ain Turk', 109),
(283, '315', 'أيت لعزيز', 'Ait Laaziz', 'Ait Laaziz', 109),
(284, '321', 'البويرة', 'Bouira', 'Bouira', 109),
(285, '332', 'المقراني', 'El-Mokrani', 'El-Mokrani', 110),
(286, '348', 'سوق الخميس', 'Souk El Khemis', 'Souk El Khemis', 110),
(287, '316', 'أعمر', 'Aomar', 'Aomar', 111),
(288, '326', 'جباحية', 'Djebahia', 'Djebahia', 111),
(289, '337', 'قادرية', 'Kadiria', 'Kadiria', 111),
(290, '335', 'حيزر', 'Haizer', 'Haizer', 112),
(291, '350', 'تاغزوت', 'Taghzout', 'Taghzout', 112),
(292, '320', 'بودربالة', 'Bouderbala', 'Bouderbala', 113),
(293, '322', 'بوكرم', 'Boukram', 'Boukram', 113),
(294, '333', 'قرومة', 'Guerrouma', 'Guerrouma', 113),
(295, '338', 'الأخضرية', 'Lakhdaria', 'Lakhdaria', 113),
(296, '340', 'معلة', 'Maala', 'Maala', 113),
(297, '353', 'زبربر', 'Z\'barbar (El Isseri )', 'Z\'barbar (El Isseri )', 113),
(298, '329', 'الهاشمية', 'El Hachimia', 'El Hachimia', 114),
(299, '343', 'وادي البردي', 'Oued El Berdi', 'Oued El Berdi', 114),
(300, '319', 'برج أوخريص', 'Bordj Okhriss', 'Bordj Okhriss', 115),
(301, '334', 'الحجرة الزرقاء', 'Hadjera Zerga', 'Hadjera Zerga', 115),
(302, '342', 'مزدور', 'Mezdour', 'Mezdour', 115),
(303, '351', 'تاقديت', 'Taguedite', 'Taguedite', 115),
(304, '310', 'أهل القصر', 'Ahl El Ksar', 'Ahl El Ksar', 116),
(305, '317', 'بشلول', 'Bechloul', 'Bechloul', 116),
(306, '327', 'العجيبة', 'El Adjiba', 'El Adjiba', 116),
(307, '328', 'الأسنام', 'El Asnam', 'El Asnam', 116),
(308, '344', 'أولاد راشد', 'Ouled Rached', 'Ouled Rached', 116),
(309, '311', 'عين الحجر', 'Ain El Hadjar', 'Ain El Hadjar', 117),
(310, '312', 'عين العلوي', 'Ain Laloui', 'Ain Laloui', 117),
(311, '314', 'عين بسام', 'Ain-Bessem', 'Ain-Bessem', 117),
(312, '318', 'بئر غبالو', 'Bir Ghbalou', 'Bir Ghbalou', 118),
(313, '330', 'الخبوزية', 'El Khabouzia', 'El Khabouzia', 118),
(314, '345', 'روراوة', 'Raouraoua', 'Raouraoua', 118),
(315, '309', 'أغبالو', 'Aghbalou', 'Aghbalou', 119),
(316, '323', 'شرفة', 'Chorfa', 'Chorfa', 119),
(317, '336', 'حنيف', 'Hanif', 'Hanif', 119),
(318, '339', 'أمشدالة', 'M Chedallah', 'M Chedallah', 119),
(319, '347', 'سحاريج', 'Saharidj', 'Saharidj', 119),
(320, '352', 'آث  منصور', 'Ath Mansour', 'Ath Mansour', 119),
(321, '324', 'الدشمية', 'Dechmia', 'Dechmia', 120),
(322, '325', 'ديرة', 'Dirah', 'Dirah', 120),
(323, '331', 'الحاكمية', 'El-Hakimia', 'El-Hakimia', 120),
(324, '341', 'المعمورة', 'Maamora', 'Maamora', 120),
(325, '346', 'ريدان', 'Ridane', 'Ridane', 120),
(326, '349', 'سور الغزلان', 'Sour El Ghozlane', 'Sour El Ghozlane', 120),
(327, '355', 'عين امقل', 'Ain Amguel', 'Ain Amguel', 121),
(328, '361', 'تمنراست', 'Tamanrasset', 'Tamanrasset', 121),
(329, '354', 'ابلسة', 'Abelsa', 'Abelsa', 122),
(330, '359', 'أدلس', 'Idles', 'Idles', 123),
(331, '362', 'تاظروك', 'Tazrouk', 'Tazrouk', 123),
(332, '390', 'تبسة', 'Tebessa', 'Tebessa', 124),
(333, '369', 'بئر العاتر', 'Bir-El-Ater', 'Bir-El-Ater', 125),
(334, '378', 'العقلة المالحة', 'El Ogla El Malha', 'El Ogla El Malha', 125),
(335, '372', 'الشريعة', 'Cheria', 'Cheria', 126),
(336, '391', 'ثليجان', 'Telidjen', 'Telidjen', 126),
(337, '370', 'بوخضرة', 'Boukhadra', 'Boukhadra', 127),
(338, '379', 'العوينات', 'El-Aouinet', 'El-Aouinet', 127),
(339, '381', 'فركان', 'Ferkane', 'Ferkane', 128),
(340, '385', 'نقرين', 'Negrine', 'Negrine', 128),
(341, '368', 'بئر مقدم', 'Bir Mokkadem', 'Bir Mokkadem', 129),
(342, '382', 'قريقر', 'Guorriguer', 'Guorriguer', 129),
(343, '383', 'الحمامات', 'Hammamet', 'Hammamet', 129),
(344, '366', 'بكارية', 'Bekkaria', 'Bekkaria', 130),
(345, '371', 'بولحاف الدير', 'Boulhaf Dyr', 'Boulhaf Dyr', 130),
(346, '373', 'الكويف', 'El Kouif', 'El Kouif', 130),
(347, '367', 'بئر الذهب', 'Bir Dheheb', 'Bir Dheheb', 131),
(348, '384', 'مرسط', 'Morsott', 'Morsott', 131),
(349, '365', 'بجن', 'Bedjene', 'Bedjene', 132),
(350, '376', 'المزرعة', 'El Mezeraa', 'El Mezeraa', 132),
(351, '377', 'العقلة', 'El Ogla', 'El Ogla', 132),
(352, '389', 'سطح قنطيس', 'Stah Guentis', 'Stah Guentis', 132),
(353, '364', 'عين الزرقاء', 'Ain Zerga', 'Ain Zerga', 133),
(354, '375', 'المريج', 'El Meridj', 'El Meridj', 133),
(355, '386', 'الونزة', 'Ouenza', 'Ouenza', 133),
(356, '374', 'الماء الابيض', 'El Malabiod', 'El Malabiod', 134),
(357, '380', 'الحويجبات', 'El-Houidjbet', 'El-Houidjbet', 134),
(358, '387', 'أم علي', 'Oum Ali', 'Oum Ali', 135),
(359, '388', 'صفصاف الوسرى', 'Saf Saf El Ouesra', 'Saf Saf El Ouesra', 135),
(360, '443', 'تلمسان', 'Tlemcen', 'Tlemcen', 136),
(361, '396', 'عين النحالة', 'Ain Nehala', 'Ain Nehala', 137),
(362, '397', 'عين تالوت', 'Ain Tellout', 'Ain Tellout', 137),
(363, '398', 'عين يوسف', 'Ain Youcef', 'Ain Youcef', 138),
(364, '405', 'بني وارسوس', 'Beni Ouarsous', 'Beni Ouarsous', 138),
(365, '416', 'الفحول', 'El Fehoul', 'El Fehoul', 138),
(366, '431', 'الرمشي', 'Remchi', 'Remchi', 138),
(367, '433', 'سبعة شيوخ', 'Sebbaa Chioukh', 'Sebbaa Chioukh', 138),
(368, '409', 'بوحلو', 'Bouhlou', 'Bouhlou', 139),
(369, '432', 'صبرة', 'Sabra', 'Sabra', 139),
(370, '412', 'دار يغمراسن', 'Dar Yaghmoracen', 'Dar Yaghmoracen', 140),
(371, '419', 'الغزوات', 'Ghazaouet', 'Ghazaouet', 140),
(372, '438', 'السواحلية', 'Souahlia', 'Souahlia', 140),
(373, '442', 'تيانت', 'Tianet', 'Tianet', 140),
(374, '406', 'بني صميل', 'Beni Smiel', 'Beni Smiel', 141),
(375, '428', 'وادي الخضر', 'Oued Lakhdar', 'Oued Lakhdar', 141),
(376, '429', 'أولاد ميمون', 'Ouled Mimoun', 'Ouled Mimoun', 141),
(377, '401', 'بني بهدل', 'Beni Bahdel', 'Beni Bahdel', 142),
(378, '407', 'بني سنوس', 'Beni Snous', 'Beni Snous', 142),
(379, '415', 'العزايل', 'Azail', 'Azail', 142),
(380, '400', 'باب العسة', 'Bab El Assa', 'Bab El Assa', 143),
(381, '439', 'السواني', 'Souani', 'Souani', 143),
(382, '440', 'سوق الثلاثاء', 'Souk Tleta', 'Souk Tleta', 143),
(383, '392', 'عين فتاح', 'Ain Fetah', 'Ain Fetah', 144),
(384, '395', 'عين الكبيرة', 'Ain Kebira', 'Ain Kebira', 144),
(385, '418', 'فلاوسن', 'Fellaoucene', 'Fellaoucene', 144),
(386, '408', 'بن سكران', 'Bensekrane', 'Bensekrane', 145),
(387, '435', 'سيدي العبدلي', 'Sidi Abdelli', 'Sidi Abdelli', 145),
(388, '421', 'الحناية', 'Hennaya', 'Hennaya', 146),
(389, '430', 'أولاد رياح', 'Ouled Riyah', 'Ouled Riyah', 146),
(390, '444', 'زناتة', 'Zenata', 'Zenata', 146),
(391, '420', 'حمام بوغرارة', 'Hammam Boughrara', 'Hammam Boughrara', 147),
(392, '423', 'مغنية', 'Maghnia', 'Maghnia', 147),
(393, '414', 'العريشة', 'El Aricha', 'El Aricha', 148),
(394, '417', 'القور', 'El Gor', 'El Gor', 148),
(395, '434', 'سبدو', 'Sebdou', 'Sebdou', 148),
(396, '402', 'بني بوسعيد', 'Beni Boussaid', 'Beni Boussaid', 149),
(397, '437', 'سيدي مجاهد', 'Sidi Medjahed', 'Sidi Medjahed', 149),
(398, '425', 'مرسى بن مهيدي', 'Marsa Ben M\'hidi', 'Marsa Ben M\'hidi', 150),
(399, '426', 'مسيردة الفواقة', 'M\'sirda Fouaga', 'M\'sirda Fouaga', 150),
(400, '413', 'جبالة', 'Djebala', 'Djebala', 151),
(401, '427', 'ندرومة', 'Nedroma', 'Nedroma', 151),
(402, '410', 'البويهي', 'Bouihi', 'Bouihi', 152),
(403, '436', 'سيدي الجيلالي', 'Sidi Djillali', 'Sidi Djillali', 152),
(404, '403', 'بني خلاد', 'Beni Khellad', 'Beni Khellad', 153),
(405, '422', 'هنين', 'Honnaine', 'Honnaine', 153),
(406, '393', 'عين فزة', 'Ain Fezza', 'Ain Fezza', 154),
(407, '399', 'عمير', 'Amieur', 'Amieur', 154),
(408, '411', 'شتوان', 'Chetouane', 'Chetouane', 154),
(409, '394', 'عين غرابة', 'Ain Ghoraba', 'Ain Ghoraba', 155),
(410, '404', 'بني مستر', 'Beni Mester', 'Beni Mester', 155),
(411, '424', 'منصورة', 'Mansourah', 'Mansourah', 155),
(412, '441', 'تيرني بني هديل', 'Terny Beni Hediel', 'Terny Beni Hediel', 155),
(413, '483', 'تيارت', 'Tiaret', 'Tiaret', 156),
(414, '464', 'مدروسة', 'Medroussa', 'Medroussa', 157),
(415, '466', 'ملاكو', 'Mellakou', 'Mellakou', 157),
(416, '478', 'سيدي بختي', 'Sidi Bakhti', 'Sidi Bakhti', 157),
(417, '446', 'عين الذهب', 'Ain Deheb', 'Ain Deheb', 158),
(418, '451', 'شحيمة', 'Chehaima', 'Chehaima', 158),
(419, '468', 'النعيمة', 'Naima', 'Naima', 158),
(420, '445', 'عين بوشقيف', 'Ain Bouchekif', 'Ain Bouchekif', 159),
(421, '452', 'دحموني', 'Dahmouni', 'Dahmouni', 159),
(422, '457', 'قرطوفة', 'Guertoufa', 'Guertoufa', 160),
(423, '470', 'الرحوية', 'Rahouia', 'Rahouia', 160),
(424, '447', 'عين دزاريت', 'Ain Dzarit', 'Ain Dzarit', 161),
(425, '461', 'مهدية', 'Mahdia', 'Mahdia', 161),
(426, '467', 'الناظورة', 'Nadorah', 'Nadorah', 161),
(427, '472', 'السبعين', 'Sebaine', 'Sebaine', 161),
(428, '455', 'الفايجة', 'Faidja', 'Faidja', 162),
(429, '475', 'سي عبد الغني', 'Si Abdelghani', 'Si Abdelghani', 162),
(430, '480', 'السوقر', 'Sougueur', 'Sougueur', 162),
(431, '485', 'توسنينة', 'Tousnina', 'Tousnina', 162),
(432, '465', 'مغيلة', 'Meghila', 'Meghila', 163),
(433, '473', 'السبت', 'Sebt', 'Sebt', 163),
(434, '479', 'سيدي حسني', 'Sidi Hosni', 'Sidi Hosni', 163),
(435, '448', 'عين الحديد', 'Ain El Hadid', 'Ain El Hadid', 164),
(436, '456', 'فرندة', 'Frenda', 'Frenda', 164),
(437, '482', 'تخمرت', 'Takhemaret', 'Takhemaret', 164),
(438, '449', 'عين كرمس', 'Ain Kermes', 'Ain Kermes', 165),
(439, '453', 'جبيلات الرصفاء', 'Djebilet Rosfa', 'Djebilet Rosfa', 165),
(440, '460', 'مادنة', 'Madna', 'Madna', 165),
(441, '463', 'مدريسة', 'Medrissa', 'Medrissa', 165),
(442, '476', 'سيدي عبد الرحمن', 'Sidi Abderrahmane', 'Sidi Abderrahmane', 165),
(443, '459', 'قصر الشلالة', 'Ksar Chellala', 'Ksar Chellala', 166),
(444, '474', 'سرغين', 'Serghine', 'Serghine', 166),
(445, '486', 'زمالة  الأمير عبد القادر', 'Zmalet El Emir Abdelkade', 'Zmalet El Emir Abdelkade', 166),
(446, '469', 'وادي ليلي', 'Oued Lilli', 'Oued Lilli', 167),
(447, '477', 'سيدي علي ملال', 'Sidi Ali Mellal', 'Sidi Ali Mellal', 167),
(448, '484', 'تيدة', 'Tidda', 'Tidda', 167),
(449, '454', 'جيلالي بن عمار', 'Djillali Ben Amar', 'Djillali Ben Amar', 168),
(450, '462', 'مشرع الصفا', 'Mechraa Safa', 'Mechraa Safa', 168),
(451, '481', 'تاقدمت', 'Tagdempt', 'Tagdempt', 168),
(452, '450', 'بوقرة', 'Bougara', 'Bougara', 169),
(453, '458', 'حمادية', 'Hamadia', 'Hamadia', 169),
(454, '471', 'الرشايقة', 'Rechaiga', 'Rechaiga', 169),
(455, '549', 'تيزي وزو', 'Tizi-Ouzou', 'Tizi-Ouzou', 170),
(456, '487', 'أبي يوسف', 'Abi-Youcef', 'Abi-Youcef', 171),
(457, '490', 'عين الحمام', 'Ain-El-Hammam', 'Ain-El-Hammam', 171),
(458, '502', 'أيت يحيى', 'Ait-Yahia', 'Ait-Yahia', 171),
(459, '503', 'اقبيل', 'Akbil', 'Akbil', 171),
(460, '514', 'بوجيمة', 'Boudjima', 'Boudjima', 172),
(461, '532', 'ماكودة', 'Makouda', 'Makouda', 172),
(462, '491', 'عين الزاوية', 'Ain-Zaouia', 'Ain-Zaouia', 173),
(463, '496', 'أيت يحي موسى', 'Ait Yahia Moussa', 'Ait Yahia Moussa', 173),
(464, '518', 'ذراع الميزان', 'Draa-El-Mizan', 'Draa-El-Mizan', 173),
(465, '520', 'فريقات', 'Frikat', 'Frikat', 173),
(466, '536', 'مكيرة', 'M\'kira', 'M\'kira', 174),
(467, '548', 'تيزي غنيف', 'Tizi-Gheniff', 'Tizi-Gheniff', 174),
(468, '523', 'إفــرحــونان', 'Iferhounene', 'Iferhounene', 175),
(469, '526', 'إيلـيــلتـن', 'Illilten', 'Illilten', 175),
(470, '528', 'إمســوحال', 'Imsouhal', 'Imsouhal', 175),
(471, '506', 'عزازقة', 'Azazga', 'Azazga', 176),
(472, '519', 'فريحة', 'Freha', 'Freha', 176),
(473, '524', 'إيفيغاء', 'Ifigha', 'Ifigha', 176),
(474, '551', 'إعــكورن', 'Yakourene', 'Yakourene', 176),
(475, '553', 'زكري', 'Zekri', 'Zekri', 176),
(476, '492', 'أيت عقـواشة', 'Ait Aggouacha', 'Ait Aggouacha', 177),
(477, '529', 'إيرجـــن', 'Irdjen', 'Irdjen', 177),
(478, '530', 'الأربعــاء ناث إيراثن', 'Larbaa Nath Irathen', 'Larbaa Nath Irathen', 177),
(479, '500', 'أيت  أومالو', 'Ait-Oumalou', 'Ait-Oumalou', 178),
(480, '550', 'تيزي راشد', 'Tizi-Rached', 'Tizi-Rached', 178),
(481, '497', 'أيت عيسى ميمون', 'Ait-Aissa-Mimoun', 'Ait-Aissa-Mimoun', 179),
(482, '539', 'واقنون', 'Ouaguenoun', 'Ouaguenoun', 179),
(483, '545', 'تيمـيزار', 'Timizart', 'Timizart', 179),
(484, '531', 'معـــاتقة', 'Maatkas', 'Maatkas', 180),
(485, '542', 'سوق الإثنين', 'Souk-El-Tenine', 'Souk-El-Tenine', 180),
(486, '499', 'أيت محمود', 'Ait-Mahmoud', 'Ait-Mahmoud', 181),
(487, '508', 'بنــــي زمنزار', 'Beni Zmenzer', 'Beni Zmenzer', 181),
(488, '509', 'بني عيسي', 'Beni-Aissi', 'Beni-Aissi', 181),
(489, '510', 'بني دوالة', 'Beni-Douala', 'Beni-Douala', 181),
(490, '512', 'بني زيكــي', 'Beni-Zikki', 'Beni-Zikki', 182),
(491, '516', 'بوزقــن', 'Bouzeguene', 'Bouzeguene', 182),
(492, '522', 'إيجــار', 'Idjeur', 'Idjeur', 182),
(493, '527', 'إيلولة أومـــالو', 'Illoula Oumalou', 'Illoula Oumalou', 182),
(494, '489', 'أقني قغران', 'Agouni-Gueghrane', 'Agouni-Gueghrane', 183),
(495, '493', 'أيت بــوادو', 'Ait Bouaddou', 'Ait Bouaddou', 183),
(496, '538', 'واضية', 'Ouadhias', 'Ouadhias', 183),
(497, '547', 'تيزي نثلاثة', 'Tizi N\'tleta', 'Tizi N\'tleta', 183),
(498, '488', 'أغريب', 'Aghribs', 'Aghribs', 184),
(499, '498', 'أيت شافع', 'Ait-Chafaa', 'Ait-Chafaa', 184),
(500, '504', 'أقرو', 'Akerrou', 'Akerrou', 184),
(501, '507', 'أزفون', 'Azeffoun', 'Azeffoun', 184),
(502, '525', 'إفليـــسن', 'Iflissen', 'Iflissen', 185),
(503, '535', 'ميزرانـــة', 'Mizrana', 'Mizrana', 185),
(504, '544', 'تيقـزيرت', 'Tigzirt', 'Tigzirt', 185),
(505, '505', 'أسي يوسف', 'Assi-Youcef', 'Assi-Youcef', 186),
(506, '513', 'بوغني', 'Boghni', 'Boghni', 186),
(507, '515', 'بونوح', 'Bounouh', 'Bounouh', 186),
(508, '533', 'مشطراس', 'Mechtras', 'Mechtras', 186),
(509, '517', 'ذراع بن خدة', 'Draa-Ben-Khedda', 'Draa-Ben-Khedda', 187),
(510, '540', 'سيدي نعمان', 'Sidi Namane', 'Sidi Namane', 187),
(511, '543', 'تادمايت', 'Tadmait', 'Tadmait', 187),
(512, '546', 'تيرمتين', 'Tirmitine', 'Tirmitine', 187),
(513, '494', 'أيت بومهدي', 'Ait Boumahdi', 'Ait Boumahdi', 188),
(514, '501', 'أيت تودرت', 'Ait-Toudert', 'Ait-Toudert', 188),
(515, '537', 'واسيف', 'Ouacif', 'Ouacif', 188),
(516, '495', 'أيت خليلي', 'Ait Khellili', 'Ait Khellili', 189),
(517, '534', 'مقــلع', 'Mekla', 'Mekla', 189),
(518, '541', 'صوامـــع', 'Souama', 'Souama', 189),
(519, '511', 'بني يني', 'Beni-Yenni', 'Beni-Yenni', 190),
(520, '521', 'إبودرارن', 'Iboudrarene', 'Iboudrarene', 190),
(521, '552', 'يطــافن', 'Yatafene', 'Yatafene', 190),
(522, '556', 'الجزائر الوسطى', 'Alger Centre', 'Alger Centre', 191),
(523, '582', 'المدنية', 'El Madania', 'El Madania', 191),
(524, '585', 'المرادية', 'El Mouradia', 'El Mouradia', 191),
(525, '605', 'سيدي امحمد', 'Sidi M\'hamed', 'Sidi M\'hamed', 191),
(526, '557', 'باب الوادي', 'Bab El Oued', 'Bab El Oued', 192),
(527, '567', 'بولوغين بن زيري', 'Bologhine Ibnou Ziri', 'Bologhine Ibnou Ziri', 192),
(528, '572', 'القصبة', 'Casbah', 'Casbah', 192),
(529, '596', 'وادي قريش', 'Oued Koriche', 'Oued Koriche', 192),
(530, '601', 'الرايس حميدو', 'Rais Hamidou', 'Rais Hamidou', 192),
(531, '564', 'بئر مراد رايس', 'Bir Mourad Rais', 'Bir Mourad Rais', 193),
(532, '565', 'بئر خادم', 'Birkhadem', 'Birkhadem', 193),
(533, '576', 'جسر قسنطينة', 'Djasr Kasentina', 'Djasr Kasentina', 193),
(534, '589', 'حيدرة', 'Hydra', 'Hydra', 193),
(535, '604', 'السحاولة', 'Sehaoula', 'Sehaoula', 193),
(536, '562', 'ابن عكنون', 'Ben Aknoun', 'Ben Aknoun', 194),
(537, '563', 'بني مسوس', 'Beni Messous', 'Beni Messous', 194),
(538, '571', 'بوزريعة', 'Bouzareah', 'Bouzareah', 194),
(539, '580', 'الابيار', 'El Biar', 'El Biar', 194),
(540, '560', 'باش جراح', 'Bachedjerah', 'Bachedjerah', 195),
(541, '570', 'بوروبة', 'Bourouba', 'Bourouba', 195),
(542, '581', 'الحراش', 'El Harrach', 'El Harrach', 195),
(543, '597', 'وادي السمار', 'Oued Smar', 'Oued Smar', 195),
(544, '561', 'براقي', 'Baraki', 'Baraki', 196),
(545, '592', 'الكاليتوس', 'Les Eucalyptus', 'Les Eucalyptus', 196),
(546, '606', 'سيدي موسى', 'Sidi Moussa', 'Sidi Moussa', 196),
(547, '583', 'المغارية', 'El Magharia', 'El Magharia', 197),
(548, '588', 'حسين داي', 'Hussein Dey', 'Hussein Dey', 197),
(549, '591', 'القبة', 'Kouba', 'Kouba', 197),
(550, '594', 'محمد بلوزداد', 'Mohamed Belouzdad', 'Mohamed Belouzdad', 197),
(551, '555', 'عين طاية', 'Ain Taya', 'Ain Taya', 198),
(552, '558', 'باب الزوار', 'Bab Ezzouar', 'Bab Ezzouar', 198),
(553, '568', 'برج البحري', 'Bordj El Bahri', 'Bordj El Bahri', 198),
(554, '569', 'برج الكيفان', 'Bordj El Kiffan', 'Bordj El Kiffan', 198),
(555, '574', 'الدار البيضاء', 'Dar El Beida', 'Dar El Beida', 198),
(556, '584', 'المرسى', 'El Marsa', 'El Marsa', 198),
(557, '595', 'المحمدية', 'Mohammadia', 'Mohammadia', 198),
(558, '566', 'بئر توتة', 'Bir Touta', 'Bir Touta', 199),
(559, '598', 'اولاد شبل', 'Ouled Chebel', 'Ouled Chebel', 199),
(560, '609', 'تسالة المرجة', 'Tessala El Merdja', 'Tessala El Merdja', 199),
(561, '587', 'هراوة', 'Herraoua', 'Herraoua', 200),
(562, '602', 'رغاية', 'Reghaia', 'Reghaia', 200),
(563, '603', 'الرويبة', 'Rouiba', 'Rouiba', 200),
(564, '593', 'المعالمة', 'Maalma', 'Maalma', 201),
(565, '600', 'الرحمانية', 'Rahmania', 'Rahmania', 201),
(566, '607', 'سويدانية', 'Souidania', 'Souidania', 201),
(567, '608', 'سطاوالي', 'Staoueli', 'Staoueli', 201),
(568, '610', 'زرالدة', 'Zeralda', 'Zeralda', 201),
(569, '559', 'بابا حسن', 'Baba Hassen', 'Baba Hassen', 202),
(570, '577', 'الدويرة', 'Douira', 'Douira', 202),
(571, '578', 'الدرارية', 'Draria', 'Draria', 202),
(572, '579', 'العاشور', 'El Achour', 'El Achour', 202),
(573, '590', 'الخرايسية', 'Khraissia', 'Khraissia', 202),
(574, '554', 'عين بنيان', 'Ain Benian', 'Ain Benian', 203),
(575, '573', 'الشراقة', 'Cheraga', 'Cheraga', 203),
(576, '575', 'دالي ابراهيم', 'Dely Ibrahim', 'Dely Ibrahim', 203),
(577, '586', 'الحمامات', 'Hammamet', 'Hammamet', 203),
(578, '599', 'اولاد فايت', 'Ouled Fayet', 'Ouled Fayet', 203),
(579, '624', 'الجلفة', 'Djelfa', 'Djelfa', 204),
(580, '614', 'عين معبد', 'Ain Maabed', 'Ain Maabed', 205),
(581, '633', 'حاسي بحبح', 'Hassi Bahbah', 'Hassi Bahbah', 205),
(582, '634', 'حاسي العش', 'Hassi El Euch', 'Hassi El Euch', 205),
(583, '645', 'زعفران', 'Zaafrane', 'Zaafrane', 205),
(584, '616', 'عمورة', 'Amourah', 'Amourah', 206),
(585, '629', 'فيض البطمة', 'Faidh El Botma', 'Faidh El Botma', 206),
(586, '639', 'أم العظام', 'Oum Laadham', 'Oum Laadham', 206),
(587, '617', 'بنهار', 'Benhar', 'Benhar', 207),
(588, '619', 'بيرين', 'Birine', 'Birine', 207),
(589, '611', 'عين الشهداء', 'Ain Chouhada', 'Ain Chouhada', 208),
(590, '625', 'دويس', 'Douis', 'Douis', 208),
(591, '627', 'الادريسية', 'El Idrissia', 'El Idrissia', 208),
(592, '623', 'دلدول', 'Deldoul', 'Deldoul', 209),
(593, '631', 'قطارة', 'Guettara', 'Guettara', 209),
(594, '636', 'مسعد', 'Messaad', 'Messaad', 209),
(595, '640', 'سد الرحال', 'Sed Rahal', 'Sed Rahal', 209),
(596, '641', 'سلمانة', 'Selmana', 'Selmana', 209),
(597, '628', 'الخميس', 'El Khemis', 'El Khemis', 210),
(598, '635', 'حاسي فدول', 'Hassi Fedoul', 'Hassi Fedoul', 210),
(599, '643', 'سيدي لعجال', 'Sidi Laadjel', 'Sidi Laadjel', 210),
(600, '613', 'عين فقه', 'Ain Fekka', 'Ain Fekka', 211),
(601, '620', 'بويرة الأحداب', 'Bouira Lahdab', 'Bouira Lahdab', 211),
(602, '632', 'حد الصحاري', 'Had Sahary', 'Had Sahary', 211),
(603, '622', 'دار الشيوخ', 'Dar Chioukh', 'Dar Chioukh', 212),
(604, '637', 'مليليحة', 'M\'liliha', 'M\'liliha', 212),
(605, '642', 'سيدي بايزيد', 'Sidi Baizid', 'Sidi Baizid', 212),
(606, '618', 'بن يعقوب', 'Benyagoub', 'Benyagoub', 213),
(607, '621', 'الشارف', 'Charef', 'Charef', 213),
(608, '626', 'القديد', 'El Guedid', 'El Guedid', 213),
(609, '612', 'عين الإبل', 'Ain El Ibel', 'Ain El Ibel', 214),
(610, '638', 'مجبارة', 'Moudjebara', 'Moudjebara', 214),
(611, '644', 'تعظميت', 'Taadmit', 'Taadmit', 214),
(612, '646', 'زكار', 'Zaccar', 'Zaccar', 214),
(613, '615', 'عين وسارة', 'Ain Oussera', 'Ain Oussera', 215),
(614, '630', 'قرنيني', 'Guernini', 'Guernini', 215),
(615, '662', 'جيجل', 'Jijel', 'Jijel', 216),
(616, '656', 'العوانة', 'El Aouana', 'El Aouana', 217),
(617, '668', 'سلمى بن زيادة', 'Selma Benziada', 'Selma Benziada', 217),
(618, '660', 'أراقن سويسي', 'Erraguene Souissi', 'Erraguene Souissi', 218),
(619, '674', 'زيامة منصورية', 'Ziama Mansouriah', 'Ziama Mansouriah', 218),
(620, '650', 'بوسيف أولاد عسكر', 'Boussif Ouled Askeur', 'Boussif Ouled Askeur', 219),
(621, '651', 'الشحنة', 'Chahna', 'Chahna', 219),
(622, '659', 'الامير عبد القادر', 'Emir Abdelkader', 'Emir Abdelkader', 219),
(623, '665', 'وجانة', 'Oudjana', 'Oudjana', 219),
(624, '672', 'الطاهير', 'Taher', 'Taher', 219),
(625, '647', 'برج الطهر', 'Bordj T\'har', 'Bordj T\'har', 220),
(626, '652', 'الشقفة', 'Chekfa', 'Chekfa', 220),
(627, '657', 'القنار نشفي', 'El Kennar Nouchfi', 'El Kennar Nouchfi', 220),
(628, '670', 'سيدي عبد العزيز', 'Sidi Abdelaziz', 'Sidi Abdelaziz', 220),
(629, '658', 'الميلية', 'El Milia', 'El Milia', 221),
(630, '667', 'أولاد يحيى خدروش', 'Ouled Yahia Khadrouch', 'Ouled Yahia Khadrouch', 221),
(631, '666', 'أولاد رابح', 'Ouled Rabah', 'Ouled Rabah', 222),
(632, '671', 'سيدي معروف', 'Sidi Marouf', 'Sidi Marouf', 222),
(633, '661', 'غبالة', 'Ghebala', 'Ghebala', 223),
(634, '669', 'السطارة', 'Settara', 'Settara', 223),
(635, '649', 'بوراوي بلهادف', 'Bouraoui Belhadef', 'Bouraoui Belhadef', 224),
(636, '653', 'الجمعة بني حبيبي', 'Djemaa Beni Habibi', 'Djemaa Beni Habibi', 224),
(637, '655', 'العنصر', 'El Ancer', 'El Ancer', 224),
(638, '664', 'خيري واد عجول', 'Khiri Oued Adjoul', 'Khiri Oued Adjoul', 224),
(639, '648', 'بودريعة بني  ياجيس', 'Boudria Beniyadjis', 'Boudria Beniyadjis', 225),
(640, '654', 'جيملة', 'Djimla', 'Djimla', 225),
(641, '663', 'قاوس', 'Kaous', 'Kaous', 226),
(642, '673', 'تاكسنة', 'Texenna', 'Texenna', 226),
(643, '729', 'سطيف', 'Setif', 'Setif', 227),
(644, '678', 'عين الكبيرة', 'Ain El Kebira', 'Ain El Kebira', 228),
(645, '703', 'الدهامشة', 'Dehamcha', 'Dehamcha', 228),
(646, '722', 'أولاد عدوان', 'Ouled Addouane', 'Ouled Addouane', 228),
(647, '683', 'عين السبت', 'Ain-Sebt', 'Ain-Sebt', 229),
(648, '695', 'بني عزيز', 'Beni-Aziz', 'Beni-Aziz', 229),
(649, '718', 'معاوية', 'Maaouia', 'Maaouia', 229),
(650, '690', 'بلاعة', 'Bellaa', 'Bellaa', 230),
(651, '698', 'بئر العرش', 'Bir-El-Arch', 'Bir-El-Arch', 230),
(652, '708', 'الولجة', 'El-Ouldja', 'El-Ouldja', 230),
(653, '730', 'تاشودة', 'Tachouda', 'Tachouda', 230),
(654, '687', 'بابور', 'Babor', 'Babor', 231),
(655, '728', 'سرج الغول', 'Serdj-El-Ghoul', 'Serdj-El-Ghoul', 231),
(656, '712', 'قجال', 'Guidjel', 'Guidjel', 232),
(657, '723', 'أولاد صابر', 'Ouled Sabor', 'Ouled Sabor', 232),
(658, '688', 'بازر سكرة', 'Bazer-Sakra', 'Bazer-Sakra', 233),
(659, '706', 'العلمة', 'El Eulma', 'El Eulma', 233),
(660, '710', 'قلتة زرقاء', 'Guelta Zerka', 'Guelta Zerka', 233),
(661, '692', 'بني فودة', 'Beni Fouda', 'Beni Fouda', 234),
(662, '704', 'جميلة', 'Djemila', 'Djemila', 234),
(663, '681', 'عين لقراج', 'Ain-Legradj', 'Ain-Legradj', 235),
(664, '691', 'بني شبانة', 'Beni Chebana', 'Beni Chebana', 235),
(665, '693', 'بني ورتيلان', 'Beni Ourtilane', 'Beni Ourtilane', 235),
(666, '696', 'بني موحلي', 'Beni-Mouhli', 'Beni-Mouhli', 235),
(667, '675', 'عين عباسة', 'Ain Abessa', 'Ain Abessa', 236),
(668, '676', 'عين أرنات', 'Ain Arnat', 'Ain Arnat', 236),
(669, '707', 'أوريسيا', 'El Ouricia', 'El Ouricia', 236),
(670, '720', 'مزلوق', 'Mezloug', 'Mezloug', 236),
(671, '686', 'عموشة', 'Amoucha', 'Amoucha', 237),
(672, '721', 'واد البارد', 'Oued El Bared', 'Oued El Bared', 237),
(673, '734', 'تيزي نبشار', 'Tizi N\'bechar', 'Tizi N\'bechar', 237),
(674, '680', 'عين ولمان', 'Ain Oulmene', 'Ain Oulmene', 238),
(675, '709', 'قلال', 'Guellal', 'Guellal', 238),
(676, '717', 'قصر الابطال', 'Kasr El Abtal', 'Kasr El Abtal', 238),
(677, '724', 'أولاد سي أحمد', 'Ouled Si Ahmed', 'Ouled Si Ahmed', 238),
(678, '684', 'أيت نوال مزادة', 'Ait Naoual Mezada', 'Ait Naoual Mezada', 239),
(679, '685', 'ايت تيزي', 'Ait-Tizi', 'Ait-Tizi', 239),
(680, '699', 'بوعنداس', 'Bouandas', 'Bouandas', 239),
(681, '701', 'بوسلام', 'Bousselam', 'Bousselam', 239),
(682, '713', 'حمام السخنة', 'Hamam Soukhna', 'Hamam Soukhna', 240),
(683, '732', 'الطاية', 'Taya', 'Taya', 240),
(684, '733', 'التلة', 'Tella', 'Tella', 240),
(685, '702', 'بوطالب', 'Boutaleb', 'Boutaleb', 241),
(686, '714', 'الحامة', 'Hamma', 'Hamma', 241),
(687, '725', 'أولاد تبان', 'Ouled Tebben', 'Ouled Tebben', 241),
(688, '726', 'الرصفة', 'Rosfa', 'Rosfa', 241),
(689, '727', 'صالح باي', 'Salah Bey', 'Salah Bey', 241),
(690, '677', 'عين أزال', 'Ain Azel', 'Ain Azel', 242),
(691, '679', 'عين الحجر', 'Ain Lahdjar', 'Ain Lahdjar', 242),
(692, '689', 'بيضاء برج', 'Beidha Bordj', 'Beidha Bordj', 242),
(693, '697', 'بئر حدادة', 'Bir Haddada', 'Bir Haddada', 242),
(694, '711', 'قنزات', 'Guenzet', 'Guenzet', 243),
(695, '716', 'حربيل', 'Harbil', 'Harbil', 243),
(696, '682', 'عين الروى', 'Ain-Roua', 'Ain-Roua', 244),
(697, '694', 'بني وسين', 'Beni Oussine', 'Beni Oussine', 244),
(698, '700', 'بوقاعة', 'Bougaa', 'Bougaa', 244),
(699, '705', 'ذراع قبيلة', 'Draa-Kebila', 'Draa-Kebila', 245),
(700, '715', 'حمام قرقور', 'Hammam Guergour', 'Hammam Guergour', 245),
(701, '719', 'ماوكلان', 'Maouaklane', 'Maouaklane', 246),
(702, '731', 'تالة إيفاسن', 'Tala-Ifacene', 'Tala-Ifacene', 246),
(703, '745', 'سعيدة', 'Saida', 'Saida', 247),
(704, '735', 'عين الحجر', 'Ain El Hadjar', 'Ain El Hadjar', 248),
(705, '742', 'مولاي العربي', 'Moulay Larbi', 'Moulay Larbi', 248),
(706, '746', 'سيدي احمد', 'Sidi Ahmed', 'Sidi Ahmed', 248),
(707, '738', 'دوي ثابت', 'Doui Thabet', 'Doui Thabet', 249),
(708, '750', 'يوب', 'Youb', 'Youb', 249),
(709, '740', 'هونت', 'Hounet', 'Hounet', 250),
(710, '744', 'أولاد خالد', 'Ouled Khaled', 'Ouled Khaled', 250),
(711, '747', 'سيدي عمر', 'Sidi Amar', 'Sidi Amar', 250),
(712, '748', 'سيدي بوبكر', 'Sidi Boubekeur', 'Sidi Boubekeur', 250),
(713, '736', 'عين السخونة', 'Ain Sekhouna', 'Ain Sekhouna', 251),
(714, '739', 'الحساسنة', 'El Hassasna', 'El Hassasna', 251),
(715, '741', 'المعمورة', 'Maamora', 'Maamora', 251),
(716, '737', 'عين السلطان', 'Ain Soltane', 'Ain Soltane', 252),
(717, '743', 'أولاد إبراهيم', 'Ouled Brahim', 'Ouled Brahim', 252),
(718, '749', 'تيرسين', 'Tircine', 'Tircine', 252),
(719, '772', 'فلفلة', 'Filfila', 'Filfila', 253),
(720, '773', 'حمادي كرومة', 'Hammadi Krouma', 'Hammadi Krouma', 253),
(721, '785', 'سكيكدة', 'Skikda', 'Skikda', 253),
(722, '754', 'عين زويت', 'Ain Zouit', 'Ain Zouit', 254),
(723, '762', 'بوشطاطة', 'Bouchetata', 'Bouchetata', 254),
(724, '768', 'الحدائق', 'El Hadaiek', 'El Hadaiek', 254),
(725, '752', 'عين شرشار', 'Ain Charchar', 'Ain Charchar', 255),
(726, '755', 'عزابة', 'Azzaba', 'Azzaba', 255),
(727, '765', 'جندل سعدي محمد', 'Djendel Saadi Mohamed', 'Djendel Saadi Mohamed', 255),
(728, '767', 'الغدير', 'El Ghedir', 'El Ghedir', 255),
(729, '771', 'السبت', 'Es Sebt', 'Es Sebt', 255),
(730, '756', 'بكوش لخضر', 'Bekkouche Lakhdar', 'Bekkouche Lakhdar', 256),
(731, '757', 'بن عزوز', 'Ben Azzouz', 'Ben Azzouz', 256),
(732, '769', 'المرسى', 'El Marsa', 'El Marsa', 256),
(733, '760', 'بني زيد', 'Beni Zid', 'Beni Zid', 257),
(734, '763', 'الشرايع', 'Cheraia', 'Cheraia', 257),
(735, '764', 'القل', 'Collo', 'Collo', 257),
(736, '776', 'خناق مايو', 'Khenag Maoune', 'Khenag Maoune', 258),
(737, '777', 'وادي الزهور', 'Oued Zhour', 'Oued Zhour', 258),
(738, '779', 'أولاد عطية', 'Ouled Attia', 'Ouled Attia', 258),
(739, '774', 'قنواع', 'Kanoua', 'Kanoua', 259),
(740, '788', 'الزيتونة', 'Zitouna', 'Zitouna', 259),
(741, '766', 'الحروش', 'El Arrouch', 'El Arrouch', 260),
(742, '770', 'مجاز الدشيش', 'Emjez Edchich', 'Emjez Edchich', 260),
(743, '780', 'أولاد حبابة', 'Ouled Habbaba', 'Ouled Habbaba', 260),
(744, '783', 'صالح بو الشعور', 'Salah Bouchaour', 'Salah Bouchaour', 260),
(745, '787', 'زردازة', 'Zerdezas', 'Zerdezas', 260),
(746, '751', 'عين بوزيان', 'Ain Bouziane', 'Ain Bouziane', 261),
(747, '759', 'بني ولبان', 'Beni Oulbane', 'Beni Oulbane', 261),
(748, '784', 'سيدي مزغيش', 'Sidi Mezghiche', 'Sidi Mezghiche', 261),
(749, '758', 'بني بشير', 'Beni Bechir', 'Beni Bechir', 262),
(750, '782', 'رمضان جمال', 'Ramdane Djamel', 'Ramdane Djamel', 262),
(751, '761', 'بين الويدان', 'Bin El Ouiden', 'Bin El Ouiden', 263),
(752, '775', 'الكركرة', 'Kerkara', 'Kerkara', 263),
(753, '786', 'تمالوس', 'Tamalous', 'Tamalous', 263),
(754, '753', 'عين قشرة', 'Ain Kechra', 'Ain Kechra', 264),
(755, '778', 'الولجة بولبلوط', 'Ouldja Boulbalout', 'Ouldja Boulbalout', 264),
(756, '781', 'أم الطوب', 'Oum Toub', 'Oum Toub', 265),
(757, '824', 'سيدي بلعباس', 'Sidi Bel-Abbes', 'Sidi Bel-Abbes', 266),
(758, '791', 'عين الثريد', 'Ain Thrid', 'Ain Thrid', 267),
(759, '820', 'السهالة الثورة', 'Sehala Thaoura', 'Sehala Thaoura', 267),
(760, '838', 'تسالة', 'Tessala', 'Tessala', 267),
(761, '796', 'بلعربي', 'Belarbi', 'Belarbi', 268),
(762, '813', 'مصطفى بن ابراهيم', 'Mostefa  Ben Brahim', 'Mostefa  Ben Brahim', 268),
(763, '839', 'تلموني', 'Tilmouni', 'Tilmouni', 268),
(764, '840', 'زروالة', 'Zerouala', 'Zerouala', 268),
(765, '803', 'الضاية', 'Dhaya', 'Dhaya', 269),
(766, '812', 'مزاورو', 'Mezaourou', 'Mezaourou', 269),
(767, '835', 'تغاليمت', 'Teghalimet', 'Teghalimet', 269),
(768, '836', 'تلاغ', 'Telagh', 'Telagh', 269),
(769, '790', 'عين قادة', 'Ain Kada', 'Ain Kada', 270),
(770, '807', 'لمطار', 'Lamtar', 'Lamtar', 270),
(771, '823', 'سيدي علي بوسيدي', 'Sidi Ali Boussidi', 'Sidi Ali Boussidi', 270),
(772, '827', 'سيدي دحو الزاير', 'Sidi Dahou Zairs', 'Sidi Dahou Zairs', 270),
(773, '799', 'بئر الحمام', 'Bir El Hammam', 'Bir El Hammam', 271),
(774, '809', 'مرحوم', 'Marhoum', 'Marhoum', 271),
(775, '826', 'سيدي شعيب', 'Sidi Chaib', 'Sidi Chaib', 271),
(776, '794', 'العمارنة', 'Amarnas', 'Amarnas', 272),
(777, '829', 'سيدي خالد', 'Sidi Khaled', 'Sidi Khaled', 272),
(778, '830', 'سيدي لحسن', 'Sidi Lahcene', 'Sidi Lahcene', 272),
(779, '831', 'سيدي يعقوب', 'Sidi Yacoub', 'Sidi Yacoub', 272),
(780, '798', 'بن عشيبة شلية', 'Benachiba Chelia', 'Benachiba Chelia', 273),
(781, '805', 'حاسي دحو', 'Hassi Dahou', 'Hassi Dahou', 273),
(782, '816', 'وادي سفيون', 'Oued Sefioun', 'Oued Sefioun', 273),
(783, '837', 'تنيرة', 'Tenira', 'Tenira', 273),
(784, '792', 'عين تندمين', 'Ain Tindamine', 'Ain Tindamine', 274),
(785, '804', 'الحصيبة', 'El Hacaiba', 'El Hacaiba', 274),
(786, '814', 'مولاي سليسن', 'Moulay Slissen', 'Moulay Slissen', 274),
(787, '811', 'مرين', 'Merine', 'Merine', 275),
(788, '817', 'وادي تاوريرة', 'Oued Taourira', 'Oued Taourira', 275),
(789, '833', 'تاودموت', 'Taoudmout', 'Taoudmout', 275),
(790, '834', 'تفسور', 'Tefessour', 'Tefessour', 275),
(791, '815', 'وادي السبع', 'Oued Sebaa', 'Oued Sebaa', 276),
(792, '818', 'راس الماء', 'Ras El Ma', 'Ras El Ma', 276),
(793, '819', 'رجم دموش', 'Redjem Demouche', 'Redjem Demouche', 276),
(794, '789', 'عين البرد', 'Ain El Berd', 'Ain El Berd', 277),
(795, '808', 'مكدرة', 'Makedra', 'Makedra', 277),
(796, '825', 'سيدي ابراهيم', 'Sidi Brahim', 'Sidi Brahim', 277),
(797, '828', 'سيدي حمادوش', 'Sidi Hamadouche', 'Sidi Hamadouche', 277),
(798, '793', 'عين أدن', 'Ain- Adden', 'Ain- Adden', 278),
(799, '800', 'بوجبهة البرج', 'Boudjebaa El Bordj', 'Boudjebaa El Bordj', 278),
(800, '810', 'مسيد', 'M\'cid', 'M\'cid', 278),
(801, '821', 'سفيزف', 'Sfisef', 'Sfisef', 278),
(802, '795', 'بضرابين المقراني', 'Bedrabine El Mokrani', 'Bedrabine El Mokrani', 279),
(803, '797', 'بن باديس', 'Ben Badis', 'Ben Badis', 279),
(804, '802', 'شيطوان البلايلة', 'Chetouane Belaila', 'Chetouane Belaila', 279),
(805, '806', 'حاسي زهانة', 'Hassi Zahana', 'Hassi Zahana', 279),
(806, '801', 'بوخنفيس', 'Boukhanefis', 'Boukhanefis', 280),
(807, '822', 'سيدي علي بن يوب', 'Sidi Ali Benyoub', 'Sidi Ali Benyoub', 280),
(808, '832', 'طابية', 'Tabia', 'Tabia', 280),
(809, '842', 'عنابة', 'Annaba', 'Annaba', 281),
(810, '850', 'سرايدي', 'Seraidi', 'Seraidi', 281),
(811, '843', 'برحال', 'Berrahal', 'Berrahal', 282),
(812, '849', 'واد العنب', 'Oued El Aneb', 'Oued El Aneb', 282),
(813, '852', 'التريعات', 'Treat', 'Treat', 282),
(814, '848', 'الحجار', 'El Hadjar', 'El Hadjar', 283),
(815, '851', 'سيدي عمار', 'Sidi Amar', 'Sidi Amar', 283),
(816, '846', 'البوني', 'El Bouni', 'El Bouni', 284),
(817, '841', 'عين الباردة', 'Ain El Berda', 'Ain El Berda', 285),
(818, '845', 'الشرفة', 'Cheurfa', 'Cheurfa', 285),
(819, '847', 'العلمة', 'El Eulma', 'El Eulma', 285),
(820, '844', 'شطايبي', 'Chetaibi', 'Chetaibi', 286),
(821, '859', 'بن جراح', 'Bendjarah', 'Bendjarah', 287),
(822, '871', 'قالمة', 'Guelma', 'Guelma', 287),
(823, '856', 'عين رقادة', 'Ain Regada', 'Ain Regada', 288),
(824, '861', 'برج صباط', 'Bordj Sabath', 'Bordj Sabath', 288),
(825, '882', 'وادي الزناتي', 'Oued Zenati', 'Oued Zenati', 288),
(826, '854', 'عين العربي', 'Ain Larbi', 'Ain Larbi', 289),
(827, '855', 'عين مخلوف', 'Ain Makhlouf', 'Ain Makhlouf', 289),
(828, '886', 'تاملوكة', 'Tamlouka', 'Tamlouka', 289),
(829, '857', 'عين صندل', 'Ain Sandel', 'Ain Sandel', 290),
(830, '862', 'بوحشانة', 'Bou Hachana', 'Bou Hachana', 290),
(831, '876', 'لخزارة', 'Khezaras', 'Khezaras', 290),
(832, '858', 'بلخير', 'Belkheir', 'Belkheir', 291),
(833, '860', 'بني مزلين', 'Beni Mezline', 'Beni Mezline', 291),
(834, '866', 'بومهرة أحمد', 'Boumahra Ahmed', 'Boumahra Ahmed', 291),
(835, '868', 'جبالة الخميسي', 'Djeballah Khemissi', 'Djeballah Khemissi', 291),
(836, '870', 'قلعة بوصبع', 'Guelaat Bou Sbaa', 'Guelaat Bou Sbaa', 291),
(837, '875', 'نشماية', 'Nechmaya', 'Nechmaya', 291),
(838, '863', 'بوحمدان', 'Bou Hamdane', 'Bou Hamdane', 292),
(839, '872', 'حمام دباغ', 'Hammam Debagh', 'Hammam Debagh', 292),
(840, '884', 'الركنية', 'Roknia', 'Roknia', 292),
(841, '867', 'الدهوارة', 'Dahouara', 'Dahouara', 293),
(842, '873', 'حمام النبايل', 'Hammam N\'bail', 'Hammam N\'bail', 293),
(843, '880', 'وادي الشحم', 'Oued Cheham', 'Oued Cheham', 293),
(844, '853', 'عين بن بيضاء', 'Ain Ben Beida', 'Ain Ben Beida', 294),
(845, '865', 'بوشقوف', 'Bouchegouf', 'Bouchegouf', 294),
(846, '878', 'مجاز الصفاء', 'Medjez Sfa', 'Medjez Sfa', 294),
(847, '881', 'وادي فراغة', 'Oued Ferragha', 'Oued Ferragha', 294),
(848, '864', 'بوعاتي محمود', 'Bouati Mahmoud', 'Bouati Mahmoud', 295),
(849, '869', 'الفجوج', 'El Fedjoudj', 'El Fedjoudj', 295),
(850, '874', 'هيليوبوليس', 'Heliopolis', 'Heliopolis', 295),
(851, '877', 'مجاز عمار', 'Medjez Amar', 'Medjez Amar', 296),
(852, '879', 'هواري بومدين', 'Houari Boumedienne', 'Houari Boumedienne', 296),
(853, '883', 'رأس العقبة', 'Ras El Agba', 'Ras El Agba', 296),
(854, '885', 'سلاوة عنونة', 'Sellaoua Announa', 'Sellaoua Announa', 296),
(855, '891', 'قسنطينة', 'Constantine', 'Constantine', 297),
(856, '892', 'ديدوش مراد', 'Didouche Mourad', 'Didouche Mourad', 298),
(857, '894', 'حامة بوزيان', 'Hamma Bouziane', 'Hamma Bouziane', 298),
(858, '890', 'بني حميدان', 'Beni Hamidane', 'Beni Hamidane', 299),
(859, '898', 'زيغود يوسف', 'Zighoud Youcef', 'Zighoud Youcef', 299),
(860, '888', 'عين السمارة', 'Ain Smara', 'Ain Smara', 300),
(861, '893', 'الخروب', 'El Khroub', 'El Khroub', 300),
(862, '897', 'أولاد رحمون', 'Ouled Rahmoun', 'Ouled Rahmoun', 300),
(863, '887', 'عين عبيد', 'Ain Abid', 'Ain Abid', 301),
(864, '889', 'أبن باديس الهرية', 'Ben Badis', 'Ben Badis', 301),
(865, '895', 'ابن زياد', 'Ibn Ziad', 'Ibn Ziad', 302),
(866, '896', 'بوجريو مسعود', 'Messaoud Boudjeriou', 'Messaoud Boudjeriou', 302),
(867, '919', 'ذراع السمار', 'Draa Esmar', 'Draa Esmar', 303),
(868, '931', 'المدية', 'Medea', 'Medea', 303),
(869, '959', 'تمسقيدة', 'Tamesguida', 'Tamesguida', 303),
(870, '904', 'بن شكاو', 'Ben Chicao', 'Ben Chicao', 304),
(871, '922', 'الحمدانية', 'El Hamdania', 'El Hamdania', 304),
(872, '945', 'وزرة', 'Ouzera', 'Ouzera', 304),
(873, '960', 'تيزي مهدي', 'Tizi Mahdi', 'Tizi Mahdi', 304),
(874, '899', 'عين بوسيف', 'Ain Boucif', 'Ain Boucif', 305),
(875, '925', 'العوينات', 'El Ouinet', 'El Ouinet', 305),
(876, '927', 'الكاف الاخضر', 'Kef Lakhdar', 'Kef Lakhdar', 305),
(877, '942', 'أولاد امعرف', 'Ouled Emaaraf', 'Ouled Emaaraf', 305),
(878, '951', 'سيدي دامد', 'Sidi Demed', 'Sidi Demed', 305),
(879, '903', 'بعطة', 'Baata', 'Baata', 306),
(880, '924', 'العمارية', 'El Omaria', 'El Omaria', 306),
(881, '940', 'أولاد إبراهيم', 'Ouled Brahim', 'Ouled Brahim', 306),
(882, '907', 'بئر بن عابد', 'Bir Ben Laabed', 'Bir Ben Laabed', 307),
(883, '921', 'القلب الكبير', 'El Guelbelkebir', 'El Guelbelkebir', 307),
(884, '948', 'سدراية', 'Sedraya', 'Sedraya', 307),
(885, '900', 'عين اقصير', 'Ain Ouksir', 'Ain Ouksir', 308),
(886, '915', 'شلالة العذاورة', 'Chelalet El Adhaoura', 'Chelalet El Adhaoura', 308),
(887, '916', 'شنيقل', 'Cheniguel', 'Cheniguel', 308),
(888, '958', 'تفراوت', 'Tafraout', 'Tafraout', 308),
(889, '911', 'بوشراحيل', 'Bouchrahil', 'Bouchrahil', 309),
(890, '928', 'خمس جوامع', 'Khams Djouamaa', 'Khams Djouamaa', 309),
(891, '952', 'سيدي نعمان', 'Sidi Naamane', 'Sidi Naamane', 309),
(892, '902', 'عزيز', 'Aziz', 'Aziz', 310),
(893, '917', 'دراق', 'Derrag', 'Derrag', 310),
(894, '944', 'أم الجليل', 'Oum El Djellil', 'Oum El Djellil', 310),
(895, '918', 'جواب', 'Djouab', 'Djouab', 311);
INSERT INTO `t_01_02_05` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(896, '954', 'سيدي زهار', 'Sidi Zahar', 'Sidi Zahar', 311),
(897, '955', 'سيدي زيان', 'Sidi Ziane', 'Sidi Ziane', 311),
(898, '956', 'السواقي', 'Souagui', 'Souagui', 311),
(899, '929', 'قصر البخاري', 'Ksar El Boukhari', 'Ksar El Boukhari', 312),
(900, '934', 'مفاتحة', 'M\'fatha', 'M\'fatha', 312),
(901, '947', 'السانق', 'Saneg', 'Saneg', 312),
(902, '920', 'العزيزية', 'El Azizia', 'El Azizia', 313),
(903, '930', 'مغراوة', 'Maghraoua', 'Maghraoua', 313),
(904, '935', 'ميهوب', 'Mihoub', 'Mihoub', 313),
(905, '909', 'بوعيش', 'Bouaiche', 'Bouaiche', 314),
(906, '912', 'بوغزول', 'Boughzoul', 'Boughzoul', 314),
(907, '914', 'الشهبونية', 'Chabounia', 'Chabounia', 314),
(908, '926', 'حناشة', 'Hannacha', 'Hannacha', 315),
(909, '936', 'عوامري', 'Ouamri', 'Ouamri', 315),
(910, '937', 'وادي حربيل', 'Oued Harbil', 'Oued Harbil', 315),
(911, '910', 'بوعيشون', 'Bouaichoune', 'Bouaichoune', 316),
(912, '939', 'أولاد بوعشرة', 'Ouled Bouachra', 'Ouled Bouachra', 316),
(913, '950', 'سي المحجوب', 'Si Mahdjoub', 'Si Mahdjoub', 316),
(914, '905', 'بني سليمان', 'Beni Slimane', 'Beni Slimane', 317),
(915, '913', 'بوسكن', 'Bouskene', 'Bouskene', 317),
(916, '953', 'سيدي الربيع', 'Sidi Rabie', 'Sidi Rabie', 317),
(917, '906', 'البرواقية', 'Berrouaghia', 'Berrouaghia', 318),
(918, '941', 'أولاد دايد', 'Ouled Deid', 'Ouled Deid', 318),
(919, '946', 'الربعية', 'Rebaia', 'Rebaia', 318),
(920, '932', 'مجبر', 'Medjebar', 'Medjebar', 319),
(921, '949', 'سغوان', 'Seghouane', 'Seghouane', 319),
(922, '961', 'ثلاث دوائر', 'Tletat Ed Douair', 'Tletat Ed Douair', 319),
(923, '962', 'الزبيرية', 'Zoubiria', 'Zoubiria', 319),
(924, '901', 'العيساوية', 'Aissaouia', 'Aissaouia', 320),
(925, '923', 'الحوضان', 'El Haoudane', 'El Haoudane', 320),
(926, '933', 'مزغنة', 'Mezerana', 'Mezerana', 320),
(927, '957', 'تابلاط', 'Tablat', 'Tablat', 320),
(928, '908', 'بوغار', 'Boghar', 'Boghar', 321),
(929, '938', 'أولاد عنتر', 'Ouled Antar', 'Ouled Antar', 321),
(930, '943', 'أولاد هلال', 'Ouled Hellal', 'Ouled Hellal', 321),
(931, '979', 'مستغانم', 'Mostaganem', 'Mostaganem', 322),
(932, '965', 'عين نويسي', 'Ain-Nouissy', 'Ain-Nouissy', 323),
(933, '970', 'فرناقة', 'Fornaka', 'Fornaka', 323),
(934, '973', 'الحسيان (بني ياحي', 'Hassiane', 'Hassiane', 323),
(935, '972', 'حاسي ماماش', 'Hassi Mameche', 'Hassi Mameche', 324),
(936, '977', 'مزغران', 'Mazagran', 'Mazagran', 324),
(937, '992', 'ستيدية', 'Stidia', 'Stidia', 324),
(938, '967', 'عين تادلس', 'Ain-Tedles', 'Ain-Tedles', 325),
(939, '981', 'وادي الخير', 'Oued El Kheir', 'Oued El Kheir', 325),
(940, '987', 'سيدي بلعطار', 'Sidi Belaattar', 'Sidi Belaattar', 325),
(941, '991', 'سور', 'Sour', 'Sour', 325),
(942, '964', 'عين بودينار', 'Ain-Boudinar', 'Ain-Boudinar', 326),
(943, '975', 'خير الدين', 'Kheir-Eddine', 'Kheir-Eddine', 326),
(944, '985', 'صيادة', 'Sayada', 'Sayada', 326),
(945, '983', 'أولاد مع الله', 'Ouled-Maalah', 'Ouled-Maalah', 327),
(946, '986', 'سيدي علي', 'Sidi Ali', 'Sidi Ali', 327),
(947, '993', 'تزقايت', 'Tazgait', 'Tazgait', 327),
(948, '968', 'بن عبد المالك رمضان', 'Benabdelmalek Ramdane', 'Benabdelmalek Ramdane', 328),
(949, '971', 'حجاج', 'Hadjadj', 'Hadjadj', 328),
(950, '988', 'سيدي لخضر', 'Sidi-Lakhdar', 'Sidi-Lakhdar', 328),
(951, '963', 'عشعاشة', 'Achaacha', 'Achaacha', 329),
(952, '974', 'خضرة', 'Khadra', 'Khadra', 329),
(953, '980', 'نكمارية', 'Nekmaria', 'Nekmaria', 329),
(954, '982', 'أولاد بوغالم', 'Ouled Boughalem', 'Ouled Boughalem', 329),
(955, '969', 'بوقيراط', 'Bouguirat', 'Bouguirat', 330),
(956, '984', 'صفصاف', 'Safsaf', 'Safsaf', 330),
(957, '989', 'سيرات', 'Sirat', 'Sirat', 330),
(958, '990', 'السوافلية', 'Souaflia', 'Souaflia', 330),
(959, '966', 'عين سيدي الشريف', 'Ain-Sidi Cherif', 'Ain-Sidi Cherif', 331),
(960, '976', 'منصورة', 'Mansourah', 'Mansourah', 331),
(961, '978', 'ماسرة', 'Mesra', 'Mesra', 331),
(962, '994', 'الطواهرية', 'Touahria', 'Touahria', 331),
(963, '1023', 'المسيلة', 'M\'sila', 'M\'sila', 332),
(964, '1013', 'حمام الضلعة', 'Hammam Dalaa', 'Hammam Dalaa', 333),
(965, '1025', 'ونوغة', 'Ouanougha', 'Ouanougha', 333),
(966, '1029', 'أولاد منصور', 'Ouled Mansour', 'Ouled Mansour', 333),
(967, '1040', 'تارمونت', 'Tarmount', 'Tarmount', 333),
(968, '1016', 'المعاضيد', 'Maadid', 'Maadid', 334),
(969, '1024', 'المطارفة', 'M\'tarfa', 'M\'tarfa', 334),
(970, '1026', 'أولاد عدي لقبالة', 'Ouled Addi Guebala', 'Ouled Addi Guebala', 334),
(971, '1027', 'أولاد دراج', 'Ouled Derradj', 'Ouled Derradj', 334),
(972, '1038', 'السوامع', 'Souamaa', 'Souamaa', 334),
(973, '1012', 'الحوامد', 'El Houamed', 'El Houamed', 335),
(974, '1015', 'خبانة', 'Khoubana', 'Khoubana', 335),
(975, '1019', 'مسيف', 'M\'cif', 'M\'cif', 335),
(976, '1008', 'شلال', 'Chellal', 'Chellal', 336),
(977, '1014', 'خطوطي سد الجير', 'Khettouti Sed-El-Jir', 'Khettouti Sed-El-Jir', 336),
(978, '1017', 'معاريف', 'Maarif', 'Maarif', 336),
(979, '1028', 'أولاد ماضي', 'Ouled Madhi', 'Ouled Madhi', 336),
(980, '998', 'عين الخضراء', 'Ain Khadra', 'Ain Khadra', 337),
(981, '1000', 'بلعايبة', 'Belaiba', 'Belaiba', 337),
(982, '1004', 'برهوم', 'Berhoum', 'Berhoum', 337),
(983, '1009', 'دهاهنة', 'Dehahna', 'Dehahna', 337),
(984, '1018', 'مقرة', 'Magra', 'Magra', 337),
(985, '1002', 'بني يلمان', 'Beni Ilmane', 'Beni Ilmane', 338),
(986, '1007', 'بوطي السايح', 'Bouti Sayeh', 'Bouti Sayeh', 338),
(987, '1033', 'سيدي عيسى', 'Sidi Aissa', 'Sidi Aissa', 338),
(988, '995', 'عين الحجل', 'Ain El Hadjel', 'Ain El Hadjel', 339),
(989, '1035', 'سيدي هجرس', 'Sidi Hadjeres', 'Sidi Hadjeres', 339),
(990, '1006', 'بوسعادة', 'Bou Saada', 'Bou Saada', 340),
(991, '1011', 'الهامل', 'El Hamel', 'El Hamel', 340),
(992, '1032', 'ولتام', 'Oulteme', 'Oulteme', 340),
(993, '1003', 'بن زوه', 'Benzouh', 'Benzouh', 341),
(994, '1030', 'أولاد سيدي ابراهيم', 'Ouled Sidi Brahim', 'Ouled Sidi Brahim', 341),
(995, '1034', 'سيدي عامر', 'Sidi Ameur', 'Sidi Ameur', 342),
(996, '1039', 'تامسة', 'Tamsa', 'Tamsa', 342),
(997, '1001', 'بن سرور', 'Ben Srour', 'Ben Srour', 343),
(998, '1022', 'محمد بوضياف', 'Mohamed Boudiaf', 'Mohamed Boudiaf', 343),
(999, '1031', 'أولاد سليمان', 'Ouled Slimane', 'Ouled Slimane', 343),
(1000, '1041', 'زرزور', 'Zarzour', 'Zarzour', 343),
(1001, '996', 'عين الملح', 'Ain El Melh', 'Ain El Melh', 344),
(1002, '997', 'عين فارس', 'Ain Fares', 'Ain Fares', 344),
(1003, '999', 'عين الريش', 'Ain Rich', 'Ain Rich', 344),
(1004, '1005', 'بئر فضة', 'Bir Foda', 'Bir Foda', 344),
(1005, '1036', 'سيدي امحمد', 'Sidi M\'hamed', 'Sidi M\'hamed', 344),
(1006, '1020', 'امجدل', 'Medjedel', 'Medjedel', 345),
(1007, '1021', 'مناعة', 'Menaa', 'Menaa', 345),
(1008, '1010', 'جبل مساعد', 'Djebel Messaad', 'Djebel Messaad', 346),
(1009, '1037', 'سليم', 'Slim', 'Slim', 346),
(1010, '1069', 'معسكر', 'Mascara', 'Mascara', 347),
(1011, '1050', 'بوحنيفية', 'Bouhanifia', 'Bouhanifia', 348),
(1012, '1055', 'القطنة', 'El Gueitena', 'El Gueitena', 348),
(1013, '1065', 'حسين', 'Hacine', 'Hacine', 348),
(1014, '1057', 'القرط', 'El Keurt', 'El Keurt', 349),
(1015, '1061', 'فروحة', 'Froha', 'Froha', 349),
(1016, '1086', 'تيزي', 'Tizi', 'Tizi', 349),
(1017, '1079', 'السهايلية', 'Sehailia', 'Sehailia', 350),
(1018, '1083', 'سيدي قادة', 'Sidi Kada', 'Sidi Kada', 350),
(1019, '1085', 'تيغنيف', 'Tighennif', 'Tighennif', 350),
(1020, '1056', 'الحشم', 'El Hachem', 'El Hachem', 351),
(1021, '1073', 'نسمط', 'Nesmot', 'Nesmot', 351),
(1022, '1088', 'زلامطة', 'Zelamta', 'Zelamta', 351),
(1023, '1044', 'عين فراح', 'Ain Ferah', 'Ain Ferah', 352),
(1024, '1075', 'وادي الأبطال', 'Oued El Abtal', 'Oued El Abtal', 352),
(1025, '1080', 'سيدي عبد الجبار', 'Sidi Abdeldjebar', 'Sidi Abdeldjebar', 352),
(1026, '1063', 'غريس', 'Ghriss', 'Ghriss', 353),
(1027, '1067', 'ماقضة', 'Makhda', 'Makhda', 353),
(1028, '1068', 'ماوسة', 'Maoussa', 'Maoussa', 353),
(1029, '1070', 'المطمور', 'Matemore', 'Matemore', 353),
(1030, '1082', 'سيدي بوسعيد', 'Sidi Boussaid', 'Sidi Boussaid', 353),
(1031, '1052', 'البرج', 'El Bordj', 'El Bordj', 354),
(1032, '1059', 'المنور', 'El Menaouer', 'El Menaouer', 354),
(1033, '1066', 'خلوية', 'Khalouia', 'Khalouia', 354),
(1034, '1043', 'عين فكان', 'Ain Fekan', 'Ain Fekan', 355),
(1035, '1045', 'عين أفرص', 'Ain Frass', 'Ain Frass', 355),
(1036, '1064', 'قرجوم', 'Guerdjoum', 'Guerdjoum', 356),
(1037, '1076', 'وادي التاغية', 'Oued Taria', 'Oued Taria', 356),
(1038, '1047', 'عوف', 'Aouf', 'Aouf', 357),
(1039, '1048', 'بنيان', 'Benian', 'Benian', 357),
(1040, '1062', 'غروس', 'Gharrous', 'Gharrous', 357),
(1041, '1042', 'عين فارس', 'Ain Fares', 'Ain Fares', 358),
(1042, '1058', 'المأمونية', 'El Mamounia', 'El Mamounia', 358),
(1043, '1049', 'بوهني', 'Bou Henni', 'Bou Henni', 359),
(1044, '1051', 'الشرفاء', 'Chorfa', 'Chorfa', 359),
(1045, '1084', 'سيق', 'Sig', 'Sig', 359),
(1046, '1046', 'العلايمية', 'Alaimia', 'Alaimia', 360),
(1047, '1074', 'عقاز', 'Oggaz', 'Oggaz', 360),
(1048, '1077', 'رأس عين عميروش', 'Ras El Ain Amirouche', 'Ras El Ain Amirouche', 360),
(1049, '1053', 'القعدة', 'El Gaada', 'El Gaada', 361),
(1050, '1087', 'زهانة', 'Zahana', 'Zahana', 361),
(1051, '1054', 'الغمري', 'El Ghomri', 'El Ghomri', 362),
(1052, '1060', 'فراقيق', 'Ferraguig', 'Ferraguig', 362),
(1053, '1071', 'مقطع الدوز', 'Mocta-Douz', 'Mocta-Douz', 362),
(1054, '1072', 'المحمدية', 'Mohammadia', 'Mohammadia', 362),
(1055, '1078', 'سجرارة', 'Sedjerara', 'Sedjerara', 362),
(1056, '1081', 'سيدي عبد المومن', 'Sidi Abdelmoumene', 'Sidi Abdelmoumene', 362),
(1057, '1101', 'ورقلة', 'Ouargla', 'Ouargla', 363),
(1058, '1102', 'الرويسات', 'Rouissat', 'Rouissat', 363),
(1059, '1100', 'انقوسة', 'N\'goussa', 'N\'goussa', 364),
(1060, '1096', 'حاسي مسعود', 'Hassi Messaoud', 'Hassi Messaoud', 365),
(1061, '1089', 'عين البيضاء', 'Ain Beida', 'Ain Beida', 366),
(1062, '1095', 'حاسي بن عبد الله', 'Hassi Ben Abdellah', 'Hassi Ben Abdellah', 366),
(1063, '1103', 'سيدي خويلد', 'Sidi Khouiled', 'Sidi Khouiled', 366),
(1064, '1093', 'البرمة', 'El Borma', 'El Borma', 367),
(1065, '1131', 'وهران', 'Oran', 'Oran', 368),
(1066, '1114', 'بن فريحة', 'Ben Freha', 'Ben Freha', 369),
(1067, '1124', 'قديل', 'Gdyel', 'Gdyel', 369),
(1068, '1127', 'حاسي مفسوخ', 'Hassi Mefsoukh', 'Hassi Mefsoukh', 369),
(1069, '1116', 'بئر الجير', 'Bir El Djir', 'Bir El Djir', 370),
(1070, '1125', 'حاسي بن عقبة', 'Hassi Ben Okba', 'Hassi Ben Okba', 370),
(1071, '1126', 'حاسي بونيف', 'Hassi Bounif', 'Hassi Bounif', 370),
(1072, '1122', 'الكرمة', 'El Kerma', 'El Kerma', 371),
(1073, '1123', 'السانية', 'Es Senia', 'Es Senia', 371),
(1074, '1134', 'سيدي الشحمي', 'Sidi Chami', 'Sidi Chami', 371),
(1075, '1113', 'أرزيو', 'Arzew', 'Arzew', 372),
(1076, '1133', 'سيدي بن يبقى', 'Sidi Ben Yebka', 'Sidi Ben Yebka', 372),
(1077, '1110', 'عين البية', 'Ain Biya', 'Ain Biya', 373),
(1078, '1115', 'بطيوة', 'Bethioua', 'Bethioua', 373),
(1079, '1128', 'مرسى الحجاج', 'Marsat El Hadjadj', 'Marsat El Hadjadj', 373),
(1080, '1112', 'عين الترك', 'Ain Turk', 'Ain Turk', 374),
(1081, '1118', 'بوسفر', 'Bousfer', 'Bousfer', 374),
(1082, '1120', 'العنصر', 'El Ancor', 'El Ancor', 374),
(1083, '1129', 'المرسى الكبير', 'Mers El Kebir', 'Mers El Kebir', 374),
(1084, '1117', 'بوفاتيس', 'Boufatis', 'Boufatis', 375),
(1085, '1121', 'البراية', 'El Braya', 'El Braya', 375),
(1086, '1132', 'وادي تليلات', 'Oued Tlelat', 'Oued Tlelat', 375),
(1087, '1135', 'طفراوي', 'Tafraoui', 'Tafraoui', 375),
(1088, '1111', 'عين الكرمة', 'Ain Kerma', 'Ain Kerma', 376),
(1089, '1119', 'بوتليليس', 'Boutlelis', 'Boutlelis', 376),
(1090, '1130', 'مسرغين', 'Messerghin', 'Messerghin', 376),
(1091, '1144', 'البيض', 'El Bayadh', 'El Bayadh', 377),
(1092, '1142', 'الشقيق', 'Cheguig', 'Cheguig', 378),
(1093, '1150', 'الكاف الأحمر', 'Kef El Ahmar', 'Kef El Ahmar', 378),
(1094, '1152', 'رقاصة', 'Rogassa', 'Rogassa', 378),
(1095, '1141', 'بريزينة', 'Brezina', 'Brezina', 379),
(1096, '1149', 'الغاسول', 'Ghassoul', 'Ghassoul', 379),
(1097, '1151', 'كراكدة', 'Krakda', 'Krakda', 379),
(1098, '1138', 'بوعلام', 'Boualem', 'Boualem', 380),
(1099, '1153', 'سيدي عامر', 'Sidi Ameur', 'Sidi Ameur', 380),
(1100, '1154', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 380),
(1101, '1155', 'سيدي طيفور', 'Sidi Tiffour', 'Sidi Tiffour', 380),
(1102, '1156', 'ستيتن', 'Stitten', 'Stitten', 380),
(1103, '1136', 'عين العراك', 'Ain El Orak', 'Ain El Orak', 381),
(1104, '1137', 'اربوات', 'Arbaouat', 'Arbaouat', 381),
(1105, '1145', 'الأبيض سيدي الشيخ', 'Labiodh Sidi Cheikh', 'Labiodh Sidi Cheikh', 381),
(1106, '1146', 'البنود', 'El Bnoud', 'El Bnoud', 381),
(1107, '1139', 'بوقطب', 'Bougtoub', 'Bougtoub', 382),
(1108, '1147', 'الخيثر', 'El Kheiter', 'El Kheiter', 382),
(1109, '1157', 'توسمولين', 'Tousmouline', 'Tousmouline', 382),
(1110, '1140', 'بوسمغون', 'Boussemghoun', 'Boussemghoun', 383),
(1111, '1143', 'شلالة', 'Chellala', 'Chellala', 384),
(1112, '1148', 'المحرة', 'El Mehara', 'El Mehara', 384),
(1113, '1162', 'إيليزي', 'Illizi', 'Illizi', 385),
(1114, '1159', 'برج عمر إدريس', 'Bordj Omar Driss', 'Bordj Omar Driss', 386),
(1115, '1160', 'دبداب', 'Debdeb', 'Debdeb', 386),
(1116, '1163', 'إن أمناس', 'In Amenas', 'In Amenas', 386),
(1117, '1166', 'برج بوعريرج', 'B. B. Arreridj', 'B. B. Arreridj', 387),
(1118, '1165', 'عين تسرة', 'Ain Tesra', 'Ain Tesra', 388),
(1119, '1187', 'أولاد أبراهم', 'Ouled Brahem', 'Ouled Brahem', 388),
(1120, '1191', 'رأس الوادي', 'Ras El Oued', 'Ras El Oued', 388),
(1121, '1171', 'برج زمورة', 'Bordj Zemmoura', 'Bordj Zemmoura', 389),
(1122, '1188', 'أولاد دحمان', 'Ouled Dahmane', 'Ouled Dahmane', 389),
(1123, '1195', 'تسامرت', 'Tassamert', 'Tassamert', 389),
(1124, '1168', 'بن داود', 'Ben Daoud', 'Ben Daoud', 390),
(1125, '1179', 'المهير', 'El M\'hir', 'El M\'hir', 390),
(1126, '1181', 'حرازة', 'Haraza', 'Haraza', 390),
(1127, '1185', 'المنصورة', 'Mansoura', 'Mansoura', 390),
(1128, '1189', 'أولاد سيدي ابراهيم', 'Ouled Sidi-Brahim', 'Ouled Sidi-Brahim', 390),
(1129, '1164', 'عين تاغروت', 'Ain Taghrout', 'Ain Taghrout', 391),
(1130, '1197', 'تيكستار', 'Tixter', 'Tixter', 391),
(1131, '1167', 'بليمور', 'Belimour', 'Belimour', 392),
(1132, '1170', 'برج الغدير', 'Bordj Ghedir', 'Bordj Ghedir', 392),
(1133, '1176', 'العناصر', 'El Annasseur', 'El Annasseur', 392),
(1134, '1180', 'غيلاسة', 'Ghailasa', 'Ghailasa', 392),
(1135, '1194', 'تقلعيت', 'Taglait', 'Taglait', 392),
(1136, '1174', 'العش', 'El Euch', 'El Euch', 393),
(1137, '1177', 'الحمادية', 'Elhammadia', 'Elhammadia', 393),
(1138, '1184', 'القصور', 'Ksour', 'Ksour', 393),
(1139, '1190', 'الرابطة', 'Rabta', 'Rabta', 393),
(1140, '1175', 'الياشير', 'El Achir', 'El Achir', 394),
(1141, '1182', 'حسناوة', 'Hasnaoua', 'Hasnaoua', 394),
(1142, '1186', 'مجانة', 'Medjana', 'Medjana', 394),
(1143, '1196', 'ثنية النصر', 'Teniet En Nasr', 'Teniet En Nasr', 394),
(1144, '1172', 'القلة', 'Colla', 'Colla', 395),
(1145, '1173', 'جعافرة', 'Djaafra', 'Djaafra', 395),
(1146, '1178', 'الماين', 'El Main', 'El Main', 395),
(1147, '1193', 'تفرق', 'Tefreg', 'Tefreg', 395),
(1148, '1169', 'بئر قاصد علي', 'Bir Kasdali', 'Bir Kasdali', 396),
(1149, '1183', 'خليل', 'Khelil', 'Khelil', 396),
(1150, '1192', 'سيدي أمبارك', 'Sidi-Embarek', 'Sidi-Embarek', 396),
(1151, '1206', 'بومرداس', 'Boumerdes', 'Boumerdes', 397),
(1152, '1209', 'قورصو', 'Corso', 'Corso', 397),
(1153, '1227', 'تيجلابين', 'Tidjelabine', 'Tidjelabine', 397),
(1154, '1204', 'بودواو', 'Boudouaou', 'Boudouaou', 398),
(1155, '1205', 'بودواو البحري', 'Boudouaou El Bahri', 'Boudouaou El Bahri', 398),
(1156, '1207', 'بوزقزة قدارة', 'Bouzegza Keddara', 'Bouzegza Keddara', 398),
(1157, '1212', 'الخروبة', 'El Kharrouba', 'El Kharrouba', 398),
(1158, '1220', 'أولاد هداج', 'Ouled Hedadj', 'Ouled Hedadj', 398),
(1159, '1203', 'برج منايل', 'Bordj Menaiel', 'Bordj Menaiel', 399),
(1160, '1211', 'جنات', 'Djinet', 'Djinet', 399),
(1161, '1217', 'لقاطة', 'Leghata', 'Leghata', 399),
(1162, '1229', 'زموري', 'Zemmouri', 'Zemmouri', 399),
(1163, '1200', 'بغلية', 'Baghlia', 'Baghlia', 400),
(1164, '1223', 'سيدي داود', 'Sidi Daoud', 'Sidi Daoud', 400),
(1165, '1225', 'تاورقة', 'Taourga', 'Taourga', 400),
(1166, '1218', 'الناصرية', 'Naciria', 'Naciria', 401),
(1167, '1219', 'أولاد عيسى', 'Ouled Aissa', 'Ouled Aissa', 401),
(1168, '1208', 'شعبة العامر', 'Chabet El Ameur', 'Chabet El Ameur', 402),
(1169, '1214', 'يسر', 'Isser', 'Isser', 402),
(1170, '1222', 'سي مصطفى', 'Si Mustapha', 'Si Mustapha', 402),
(1171, '1228', 'تيمزريت', 'Timezrit', 'Timezrit', 402),
(1172, '1199', 'عمال', 'Ammal', 'Ammal', 403),
(1173, '1202', 'بني عمران', 'Beni Amrane', 'Beni Amrane', 403),
(1174, '1224', 'سوق الحد', 'Souk El Had', 'Souk El Had', 403),
(1175, '1226', 'الثنية', 'Thenia', 'Thenia', 403),
(1176, '1198', 'أعفير', 'Afir', 'Afir', 404),
(1177, '1201', 'بن شود', 'Ben Choud', 'Ben Choud', 404),
(1178, '1210', 'دلس', 'Dellys', 'Dellys', 404),
(1179, '1213', 'حمادي', 'Hammedi', 'Hammedi', 405),
(1180, '1215', 'خميس الخشنة', 'Khemis El Khechna', 'Khemis El Khechna', 405),
(1181, '1216', 'الاربعطاش', 'Larbatache', 'Larbatache', 405),
(1182, '1221', 'أولاد موسى', 'Ouled Moussa', 'Ouled Moussa', 405),
(1183, '1230', 'عين العسل', 'Ain El Assel', 'Ain El Assel', 406),
(1184, '1236', 'بوقوس', 'Bougous', 'Bougous', 406),
(1185, '1246', 'الطارف', 'El Tarf', 'El Tarf', 406),
(1186, '1253', 'الزيتونة', 'Zitouna', 'Zitouna', 406),
(1187, '1231', 'عين الكرمة', 'Ain Kerma', 'Ain Kerma', 407),
(1188, '1237', 'بوحجار', 'Bouhadjar', 'Bouhadjar', 407),
(1189, '1247', 'حمام بني صالح', 'Hammam Beni Salah', 'Hammam Beni Salah', 407),
(1190, '1249', 'وادي الزيتون', 'Oued Zitoun', 'Oued Zitoun', 407),
(1191, '1233', 'بن مهيدي', 'Ben M Hidi', 'Ben M Hidi', 408),
(1192, '1234', 'بريحان', 'Berrihane', 'Berrihane', 408),
(1193, '1243', 'الشط', 'Echatt', 'Echatt', 408),
(1194, '1244', 'العيون', 'El Aioun', 'El Aioun', 409),
(1195, '1245', 'القالة', 'El Kala', 'El Kala', 409),
(1196, '1250', 'رمل السوق', 'Raml Souk', 'Raml Souk', 409),
(1197, '1251', 'السوارخ', 'Souarekh', 'Souarekh', 409),
(1198, '1238', 'بوثلجة', 'Bouteldja', 'Bouteldja', 410),
(1199, '1240', 'الشافية', 'Chefia', 'Chefia', 410),
(1200, '1248', 'بحيرة الطيور', 'Lac Des Oiseaux', 'Lac Des Oiseaux', 410),
(1201, '1239', 'شبيطة مختار', 'Chebaita Mokhtar', 'Chebaita Mokhtar', 411),
(1202, '1241', 'شحاني', 'Chihani', 'Chihani', 411),
(1203, '1242', 'الذرعـان', 'Drean', 'Drean', 411),
(1204, '1232', 'عصفور', 'Asfour', 'Asfour', 412),
(1205, '1235', 'البسباس', 'Besbes', 'Besbes', 412),
(1206, '1252', 'زريزر', 'Zerizer', 'Zerizer', 412),
(1207, '1254', 'أم العسل', 'Oum El Assel', 'Oum El Assel', 413),
(1208, '1255', 'تندوف', 'Tindouf', 'Tindouf', 413),
(1209, '1269', 'أولاد بسام', 'Ouled Bessam', 'Ouled Bessam', 414),
(1210, '1276', 'تيسمسيلت', 'Tissemsilt', 'Tissemsilt', 414),
(1211, '1257', 'بني شعيب', 'Beni Chaib', 'Beni Chaib', 415),
(1212, '1258', 'بني لحسن', 'Beni Lahcene', 'Beni Lahcene', 415),
(1213, '1259', 'برج بونعامة', 'Bordj Bounaama', 'Bordj Bounaama', 415),
(1214, '1273', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 415),
(1215, '1271', 'سيدي بوتوشنت', 'Sidi Boutouchent', 'Sidi Boutouchent', 416),
(1216, '1275', 'ثنية الاحد', 'Theniet El Had', 'Theniet El Had', 416),
(1217, '1261', 'بوقائد', 'Boucaid', 'Boucaid', 417),
(1218, '1263', 'الأربعاء', 'Larbaa', 'Larbaa', 417),
(1219, '1266', 'الأزهرية', 'Lazharia', 'Lazharia', 417),
(1220, '1264', 'لرجام', 'Lardjem', 'Lardjem', 418),
(1221, '1268', 'الملعب', 'Melaab', 'Melaab', 418),
(1222, '1272', 'سيدي العنتري', 'Sidi Lantri', 'Sidi Lantri', 418),
(1223, '1274', 'تملاحت', 'Tamellahet', 'Tamellahet', 418),
(1224, '1260', 'برج الأمير عبد القادر', 'Bordj El Emir Abdelkader', 'Bordj El Emir Abdelkader', 419),
(1225, '1277', 'اليوسفية', 'Youssoufia', 'Youssoufia', 419),
(1226, '1262', 'خميستي', 'Khemisti', 'Khemisti', 420),
(1227, '1265', 'العيون', 'Layoune', 'Layoune', 420),
(1228, '1256', 'عماري', 'Ammari', 'Ammari', 421),
(1229, '1267', 'المعاصم', 'Maacem', 'Maacem', 421),
(1230, '1270', 'سيدي عابد', 'Sidi Abed', 'Sidi Abed', 421),
(1231, '1285', 'الوادي', 'El-Oued', 'El-Oued', 422),
(1232, '1290', 'كوينين', 'Kouinine', 'Kouinine', 422),
(1233, '1283', 'العقلة', 'El Ogla', 'El Ogla', 423),
(1234, '1294', 'النخلة', 'Nakhla', 'Nakhla', 423),
(1235, '1299', 'الرباح', 'Robbah', 'Robbah', 423),
(1236, '1278', 'البياضة', 'Bayadha', 'Bayadha', 424),
(1237, '1286', 'قمار', 'Guemar', 'Guemar', 425),
(1238, '1297', 'ورماس', 'Ourmes', 'Ourmes', 425),
(1239, '1304', 'تغزوت', 'Taghzout', 'Taghzout', 425),
(1240, '1287', 'الحمراية', 'Hamraia', 'Hamraia', 426),
(1241, '1298', 'الرقيبة', 'Reguiba', 'Reguiba', 426),
(1242, '1280', 'الدبيلة', 'Debila', 'Debila', 427),
(1243, '1288', 'حساني عبد الكريم', 'Hassani Abdelkrim', 'Hassani Abdelkrim', 427),
(1244, '1289', 'حاسي خليفة', 'Hassi Khalifa', 'Hassi Khalifa', 428),
(1245, '1307', 'الطريفاوي', 'Trifaoui', 'Trifaoui', 428),
(1246, '1279', 'بن  قشة', 'Ben Guecha', 'Ben Guecha', 429),
(1247, '1282', 'دوار الماء', 'Douar El Maa', 'Douar El Maa', 429),
(1248, '1305', 'الطالب العربي', 'Taleb Larbi', 'Taleb Larbi', 429),
(1249, '1291', 'المقرن', 'Magrane', 'Magrane', 430),
(1250, '1301', 'سيدي عون', 'Sidi Aoun', 'Sidi Aoun', 430),
(1251, '1292', 'اميه وانسة', 'Mih Ouansa', 'Mih Ouansa', 431),
(1252, '1295', 'وادي العلندة', 'Oued El Alenda', 'Oued El Alenda', 431),
(1253, '1320', 'خنشلة', 'Khenchela', 'Khenchela', 432),
(1254, '1319', 'قايس', 'Kais', 'Kais', 433),
(1255, '1325', 'الرميلة', 'Remila', 'Remila', 433),
(1256, '1327', 'تاوزيانت', 'Taouzianat', 'Taouzianat', 433),
(1257, '1310', 'بغاي', 'Baghai', 'Baghai', 434),
(1258, '1315', 'الحامة', 'El Hamma', 'El Hamma', 434),
(1259, '1318', 'انسيغة', 'Ensigha', 'Ensigha', 434),
(1260, '1326', 'طامزة', 'Tamza', 'Tamza', 434),
(1261, '1308', 'عين الطويلة', 'Ain Touila', 'Ain Touila', 435),
(1262, '1323', 'متوسة', 'M\'toussa', 'M\'toussa', 435),
(1263, '1311', 'بوحمامة', 'Bouhmama', 'Bouhmama', 436),
(1264, '1313', 'شلية', 'Chelia', 'Chelia', 436),
(1265, '1322', 'مصارة', 'M\'sara', 'M\'sara', 436),
(1266, '1328', 'يابوس', 'Yabous', 'Yabous', 436),
(1267, '1312', 'ششار', 'Chechar', 'Chechar', 437),
(1268, '1314', 'جلال', 'Djellal', 'Djellal', 437),
(1269, '1317', 'الولجة', 'El Oueldja', 'El Oueldja', 437),
(1270, '1321', 'خيران', 'Khirane', 'Khirane', 437),
(1271, '1309', 'بابار', 'Babar', 'Babar', 438),
(1272, '1316', 'المحمل', 'El Mahmal', 'El Mahmal', 439),
(1273, '1324', 'أولاد رشاش', 'Ouled Rechache', 'Ouled Rechache', 439),
(1274, '1349', 'سوق أهراس', 'Souk Ahras', 'Souk Ahras', 440),
(1275, '1329', 'عين سلطان', 'Ain Soltane', 'Ain Soltane', 441),
(1276, '1336', 'خميسة', 'Khemissa', 'Khemissa', 441),
(1277, '1347', 'سدراتة', 'Sedrata', 'Sedrata', 441),
(1278, '1334', 'الحنانشة', 'Hanencha', 'Hanencha', 442),
(1279, '1337', 'المشروحة', 'Machroha', 'Machroha', 442),
(1280, '1330', 'عين الزانة', 'Ain Zana', 'Ain Zana', 443),
(1281, '1341', 'أولاد إدريس', 'Ouled Driss', 'Ouled Driss', 443),
(1282, '1332', 'الدريعة', 'Drea', 'Drea', 444),
(1283, '1350', 'تاورة', 'Taoura', 'Taoura', 444),
(1284, '1353', 'الزعرورية', 'Zaarouria', 'Zaarouria', 444),
(1285, '1333', 'الحدادة', 'Haddada', 'Haddada', 445),
(1286, '1335', 'الخضارة', 'Khedara', 'Khedara', 445),
(1287, '1342', 'أولاد مومن', 'Ouled Moumen', 'Ouled Moumen', 445),
(1288, '1339', 'المراهنة', 'Merahna', 'Merahna', 446),
(1289, '1344', 'ويلان', 'Ouillen', 'Ouillen', 446),
(1290, '1348', 'سيدي فرج', 'Sidi Fredj', 'Sidi Fredj', 446),
(1291, '1331', 'بئر بوحوش', 'Bir Bouhouche', 'Bir Bouhouche', 447),
(1292, '1346', 'سافل الويدان', 'Safel El Ouiden', 'Safel El Ouiden', 447),
(1293, '1354', 'الزوابي', 'Zouabi', 'Zouabi', 447),
(1294, '1338', 'مداوروش', 'M\'daourouche', 'M\'daourouche', 448),
(1295, '1345', 'الراقوبة', 'Ragouba', 'Ragouba', 448),
(1296, '1352', 'تيفاش', 'Tiffech', 'Tiffech', 448),
(1297, '1340', 'وادي الكبريت', 'Oued Kebrit', 'Oued Kebrit', 449),
(1298, '1343', 'أم العظايم', 'Oum El Adhaim', 'Oum El Adhaim', 449),
(1299, '1351', 'ترقالت', 'Terraguelt', 'Terraguelt', 449),
(1300, '1382', 'تيبازة', 'Tipaza', 'Tipaza', 450),
(1301, '1369', 'حجوط', 'Hadjout', 'Hadjout', 451),
(1302, '1375', 'مراد', 'Merad', 'Merad', 451),
(1303, '1374', 'مناصر', 'Menaceur', 'Menaceur', 452),
(1304, '1377', 'الناظور', 'Nador', 'Nador', 452),
(1305, '1381', 'سيدي عامر', 'Sidi-Amar', 'Sidi-Amar', 452),
(1306, '1355', 'أغبال', 'Aghbal', 'Aghbal', 453),
(1307, '1368', 'قوراية', 'Gouraya', 'Gouraya', 453),
(1308, '1376', 'مسلمون', 'Messelmoun', 'Messelmoun', 453),
(1309, '1364', 'شرشال', 'Cherchell', 'Cherchell', 454),
(1310, '1370', 'حجرة النص', 'Hadjret Ennous', 'Hadjret Ennous', 454),
(1311, '1378', 'سيدي غيلاس', 'Sidi Ghiles', 'Sidi Ghiles', 454),
(1312, '1380', 'سيدي سميان', 'Sidi Semiane', 'Sidi Semiane', 454),
(1313, '1359', 'بني ميلك', 'Beni Mileuk', 'Beni Mileuk', 455),
(1314, '1365', 'الداموس', 'Damous', 'Damous', 455),
(1315, '1373', 'الأرهاط', 'Larhat', 'Larhat', 455),
(1316, '1366', 'دواودة', 'Douaouda', 'Douaouda', 456),
(1317, '1367', 'فوكة', 'Fouka', 'Fouka', 456),
(1318, '1357', 'عين تاقورايت', 'Ain Tagourait', 'Ain Tagourait', 457),
(1319, '1360', 'بوهارون', 'Bou Haroun', 'Bou Haroun', 457),
(1320, '1361', 'بواسماعيل', 'Bou Ismail', 'Bou Ismail', 457),
(1321, '1371', 'خميستي', 'Khemisti', 'Khemisti', 457),
(1322, '1356', 'أحمر العين', 'Ahmer El Ain', 'Ahmer El Ain', 458),
(1323, '1362', 'بورقيقة', 'Bourkika', 'Bourkika', 458),
(1324, '1379', 'سيدي راشد', 'Sidi Rached', 'Sidi Rached', 458),
(1325, '1358', 'الحطاطبة', 'Attatba', 'Attatba', 459),
(1326, '1363', 'الشعيبة', 'Chaiba', 'Chaiba', 459),
(1327, '1372', 'القليعة', 'Kolea', 'Kolea', 459),
(1328, '1386', 'عين التين', 'Ain Tine', 'Ain Tine', 460),
(1329, '1398', 'ميلة', 'Mila', 'Mila', 460),
(1330, '1405', 'سيدي خليفة', 'Sidi Khelifa', 'Sidi Khelifa', 460),
(1331, '1395', 'فرجيوة', 'Ferdjioua', 'Ferdjioua', 461),
(1332, '1413', 'يحي بني قشة', 'Yahia Beniguecha', 'Yahia Beniguecha', 461),
(1333, '1385', 'عين الملوك', 'Ain Mellouk', 'Ain Mellouk', 462),
(1334, '1390', 'شلغوم العيد', 'Chelghoum Laid', 'Chelghoum Laid', 462),
(1335, '1400', 'وادي العثمانية', 'Oued Athmenia', 'Oued Athmenia', 462),
(1336, '1394', 'مشيرة', 'El Mechira', 'El Mechira', 463),
(1337, '1402', 'وادي سقان', 'Oued Seguen', 'Oued Seguen', 463),
(1338, '1409', 'التلاغمة', 'Teleghma', 'Teleghma', 463),
(1339, '1388', 'بن يحي عبد الرحمن', 'Benyahia Abderrahmane', 'Benyahia Abderrahmane', 464),
(1340, '1403', 'أولاد اخلوف', 'Ouled Khalouf', 'Ouled Khalouf', 464),
(1341, '1407', 'تاجنانت', 'Tadjenanet', 'Tadjenanet', 464),
(1342, '1383', 'أحمد راشدي', 'Ahmed Rachedi', 'Ahmed Rachedi', 465),
(1343, '1401', 'وادي النجاء', 'Oued Endja', 'Oued Endja', 465),
(1344, '1414', 'زغاية', 'Zeghaia', 'Zeghaia', 465),
(1345, '1389', 'بوحاتم', 'Bouhatem', 'Bouhatem', 466),
(1346, '1392', 'دراحي بوصلاح', 'Derrahi Bousselah', 'Derrahi Bousselah', 466),
(1347, '1404', 'الرواشد', 'Rouached', 'Rouached', 467),
(1348, '1412', 'تيبرقنت', 'Tiberguent', 'Tiberguent', 467),
(1349, '1396', 'القرارم قوقة', 'Grarem Gouga', 'Grarem Gouga', 468),
(1350, '1397', 'حمالة', 'Hamala', 'Hamala', 468),
(1351, '1391', 'الشيقارة', 'Chigara', 'Chigara', 469),
(1352, '1406', 'سيدي مروان', 'Sidi Merouane', 'Sidi Merouane', 469),
(1353, '1399', 'مينار زارزة', 'Minar Zarza', 'Minar Zarza', 470),
(1354, '1408', 'تسدان حدادة', 'Tassadane Haddada', 'Tassadane Haddada', 470),
(1355, '1387', 'اعميرة اراس', 'Amira Arres', 'Amira Arres', 471),
(1356, '1410', 'ترعي باينان', 'Terrai Bainen', 'Terrai Bainen', 471),
(1357, '1411', 'تسالة لمطاعي', 'Tassala Lematai', 'Tassala Lematai', 471),
(1358, '1384', ' عين البيضاء أحريش', 'Ain Beida Harriche', 'Ain Beida Harriche', 472),
(1359, '1393', 'العياضي برباس', 'El Ayadi Barbes', 'El Ayadi Barbes', 472),
(1360, '1417', 'عين الدفلى', 'Ain-Defla', 'Ain-Defla', 473),
(1361, '1425', 'بن علال', 'Ben Allal', 'Ben Allal', 474),
(1362, '1442', 'مليانة', 'Miliana', 'Miliana', 474),
(1363, '1428', 'بومدفع', 'Boumedfaa', 'Boumedfaa', 475),
(1364, '1439', 'الحسينية', 'Hoceinia', 'Hoceinia', 475),
(1365, '1440', 'خميس مليانة', 'Khemis-Miliana', 'Khemis-Miliana', 476),
(1366, '1446', 'سيدي الأخضر', 'Sidi-Lakhdar', 'Sidi-Lakhdar', 476),
(1367, '1415', 'عين البنيان', 'Ain-Benian', 'Ain-Benian', 477),
(1368, '1420', 'عين التركي', 'Ain-Torki', 'Ain-Torki', 477),
(1369, '1437', 'حمام ريغة', 'Hammam-Righa', 'Hammam-Righa', 477),
(1370, '1429', 'بوراشد', 'Bourached', 'Bourached', 478),
(1371, '1430', 'جليدة', 'Djelida', 'Djelida', 478),
(1372, '1431', 'جمعة أولاد الشيخ', 'Djemaa Ouled Cheikh', 'Djemaa Ouled Cheikh', 478),
(1373, '1421', 'عريب', 'Arib', 'Arib', 479),
(1374, '1434', 'العامرة', 'El-Amra', 'El-Amra', 479),
(1375, '1441', 'المخاطرية', 'Mekhatria', 'Mekhatria', 479),
(1376, '1435', 'العطاف', 'El-Attaf', 'El-Attaf', 480),
(1377, '1449', 'تبركانين', 'Tiberkanine', 'Tiberkanine', 480),
(1378, '1416', 'عين بويحيى', 'Ain-Bouyahia', 'Ain-Bouyahia', 481),
(1379, '1433', 'العبادية', 'El-Abadia', 'El-Abadia', 481),
(1380, '1447', 'تاشتة زقاغة', 'Tacheta Zegagha', 'Tacheta Zegagha', 481),
(1381, '1422', 'بربوش', 'Birbouche', 'Birbouche', 482),
(1382, '1432', 'جندل', 'Djendel', 'Djendel', 482),
(1383, '1443', 'وادي الشرفاء', 'Oued Chorfa', 'Oued Chorfa', 482),
(1384, '1418', 'عين الاشياخ', 'Ain-Lechiakh', 'Ain-Lechiakh', 483),
(1385, '1419', 'عين السلطان', 'Ain-Soltane', 'Ain-Soltane', 483),
(1386, '1444', 'واد الجمعة', 'Oued Djemaa', 'Oued Djemaa', 483),
(1387, '1436', 'الماين', 'El-Maine', 'El-Maine', 484),
(1388, '1445', 'الروينة', 'Rouina', 'Rouina', 484),
(1389, '1450', 'زدين', 'Zeddine', 'Zeddine', 484),
(1390, '1426', 'بئر ولد خليفة', 'Bir-Ould-Khelifa', 'Bir-Ould-Khelifa', 485),
(1391, '1427', 'برج الأمير خالد', 'Bordj-Emir-Khaled', 'Bordj-Emir-Khaled', 485),
(1392, '1448', 'طارق بن زياد', 'Tarik-Ibn-Ziad', 'Tarik-Ibn-Ziad', 485),
(1393, '1423', 'بطحية', 'Bathia', 'Bathia', 486),
(1394, '1424', 'بلعاص', 'Belaas', 'Belaas', 486),
(1395, '1438', 'الحسانية', 'Hassania', 'Hassania', 486),
(1396, '1460', 'النعامة', 'Naama', 'Naama', 487),
(1397, '1451', 'عين بن خليل', 'Ain Ben Khelil', 'Ain Ben Khelil', 488),
(1398, '1455', 'البيوض', 'El Biodh', 'El Biodh', 488),
(1399, '1458', 'المشرية', 'Mecheria', 'Mecheria', 488),
(1400, '1452', 'عين الصفراء', 'Ain Sefra', 'Ain Sefra', 489),
(1401, '1462', 'تيوت', 'Tiout', 'Tiout', 489),
(1402, '1461', 'سفيسيفة', 'Sfissifa', 'Sfissifa', 490),
(1403, '1454', 'جنين بورزق', 'Djenienne Bourezg', 'Djenienne Bourezg', 491),
(1404, '1459', 'مغرار', 'Moghrar', 'Moghrar', 491),
(1405, '1453', 'عسلة', 'Asla', 'Asla', 492),
(1406, '1456', 'القصدير', 'Kasdir', 'Kasdir', 493),
(1407, '1457', 'مكمن بن عمار', 'Makmen Ben Amar', 'Makmen Ben Amar', 493),
(1408, '1466', 'عين تموشنت', 'Ain Temouchent', 'Ain Temouchent', 494),
(1409, '1485', 'سيدي بن عدة', 'Sidi Ben Adda', 'Sidi Ben Adda', 494),
(1410, '1472', 'شنتوف', 'Chentouf', 'Chentouf', 495),
(1411, '1477', 'حمام بوحجر', 'Hammam Bou Hadjar', 'Hammam Bou Hadjar', 495),
(1412, '1478', 'الحساسنة', 'Hassasna', 'Hassasna', 495),
(1413, '1480', 'وادي برقش', 'Oued Berkeche', 'Oued Berkeche', 495),
(1414, '1464', 'عين الأربعاء', 'Ain El Arbaa', 'Ain El Arbaa', 496),
(1415, '1481', 'وادي الصباح', 'Oued Sebbah', 'Oued Sebbah', 496),
(1416, '1486', 'سيدي بومدين', 'Sidi Boumediene', 'Sidi Boumediene', 496),
(1417, '1489', 'تامزورة', 'Tamzoura', 'Tamzoura', 496),
(1418, '1471', 'شعبة اللحم', 'Chaabat El Ham', 'Chaabat El Ham', 497),
(1419, '1474', 'المالح', 'El Maleh', 'El Maleh', 497),
(1420, '1483', 'أولاد الكيحل', 'Ouled Kihal', 'Ouled Kihal', 497),
(1421, '1490', 'تارقة', 'Terga', 'Terga', 497),
(1422, '1470', 'بوزجار', 'Bouzedjar', 'Bouzedjar', 498),
(1423, '1473', 'العامرية', 'El Amria', 'El Amria', 498),
(1424, '1475', 'المساعيد', 'El Messaid', 'El Messaid', 498),
(1425, '1479', 'حاسي الغلة', 'Hassi El Ghella', 'Hassi El Ghella', 498),
(1426, '1482', 'أولاد بوجمعة', 'Ouled Boudjemaa', 'Ouled Boudjemaa', 498),
(1427, '1463', 'أغلال', 'Aghlal', 'Aghlal', 499),
(1428, '1465', 'عين الكيحل', 'Ain Kihal', 'Ain Kihal', 499),
(1429, '1467', 'عين الطلبة', 'Ain Tolba', 'Ain Tolba', 499),
(1430, '1468', 'عقب الليل', 'Aoubellil', 'Aoubellil', 499),
(1431, '1469', 'بني صاف', 'Beni Saf', 'Beni Saf', 500),
(1432, '1476', 'الأمير عبد القادر', 'Emir Abdelkader', 'Emir Abdelkader', 500),
(1433, '1488', 'سيدي صافي', 'Sidi Safi', 'Sidi Safi', 500),
(1434, '1484', 'ولهاصة الغرابة', 'Oulhaca El Gheraba', 'Oulhaca El Gheraba', 501),
(1435, '1487', 'سيدي ورياش', 'Sidi Ouriache', 'Sidi Ouriache', 501),
(1436, '1496', 'غرداية', 'Ghardaia', 'Ghardaia', 502),
(1437, '1493', 'ضاية بن ضحوة', 'Dhayet Bendhahoua', 'Dhayet Bendhahoua', 503),
(1438, '1491', 'بريان', 'Berriane', 'Berriane', 504),
(1439, '1501', 'متليلي', 'Metlili', 'Metlili', 505),
(1440, '1502', 'سبسب', 'Sebseb', 'Sebseb', 505),
(1441, '1497', 'القرارة', 'El Guerrara', 'El Guerrara', 506),
(1442, '1503', 'زلفانة', 'Zelfana', 'Zelfana', 507),
(1443, '1492', 'بونورة', 'Bounoura', 'Bounoura', 508),
(1444, '1494', 'العطف', 'El Atteuf', 'El Atteuf', 508),
(1445, '1500', 'المنصورة', 'Mansoura', 'Mansoura', 547),
(1446, '1508', 'بن داود', 'Bendaoud', 'Bendaoud', 509),
(1447, '1533', 'غليزان', 'Relizane', 'Relizane', 509),
(1448, '1521', 'لحلاف', 'Lahlef', 'Lahlef', 510),
(1449, '1525', 'مرجة سيدي عابد', 'Merdja Sidi Abed', 'Merdja Sidi Abed', 510),
(1450, '1526', 'واريزان', 'Ouarizane', 'Ouarizane', 510),
(1451, '1529', 'وادي رهيو', 'Oued-Rhiou', 'Oued-Rhiou', 510),
(1452, '1514', 'الحمادنة', 'El H\'madna', 'El H\'madna', 511),
(1453, '1527', 'وادي الجمعة', 'Oued El Djemaa', 'Oued El Djemaa', 511),
(1454, '1510', 'بني زنطيس', 'Beni Zentis', 'Beni Zentis', 512),
(1455, '1523', 'مديونة', 'Mediouna', 'Mediouna', 512),
(1456, '1536', 'سيدي أمحمد بن علي', 'Sidi M\'hamed Benali', 'Sidi M\'hamed Benali', 512),
(1457, '1506', 'عمي موسى', 'Ammi Moussa', 'Ammi Moussa', 513),
(1458, '1513', 'الحاسي', 'El Hassi', 'El Hassi', 513),
(1459, '1515', 'الولجة', 'El Ouldja', 'El Ouldja', 513),
(1460, '1530', 'أولاد يعيش', 'Ouled Aiche', 'Ouled Aiche', 513),
(1461, '1509', 'بني درقن', 'Beni Dergoun', 'Beni Dergoun', 514),
(1462, '1511', 'دار بن عبد الله', 'Dar Ben Abdelah', 'Dar Ben Abdelah', 514),
(1463, '1541', 'زمورة', 'Zemmoura', 'Zemmoura', 514),
(1464, '1512', 'جديوية', 'Djidiouia', 'Djidiouia', 515),
(1465, '1519', 'حمري', 'Hamri', 'Hamri', 515),
(1466, '1531', 'أولاد سيدي الميهوب', 'Ouled Sidi Mihoub', 'Ouled Sidi Mihoub', 515),
(1467, '1507', 'بلعسل بوزقزة', 'Belaassel Bouzagza', 'Belaassel Bouzagza', 516),
(1468, '1517', 'المطمر', 'El-Matmar', 'El-Matmar', 516),
(1469, '1534', 'سيدي  خطاب', 'Sidi Khettab', 'Sidi Khettab', 516),
(1470, '1537', 'سيدي امحمد بن عودة', 'Sidi M\'hamed Benaouda', 'Sidi M\'hamed Benaouda', 516),
(1471, '1505', 'عين طارق', 'Ain-Tarek', 'Ain-Tarek', 517),
(1472, '1518', 'حد الشكالة', 'Had Echkalla', 'Had Echkalla', 517),
(1473, '1516', 'القطار', 'El-Guettar', 'El-Guettar', 518),
(1474, '1522', 'مازونة', 'Mazouna', 'Mazouna', 518),
(1475, '1504', 'عين الرحمة', 'Ain Rahma', 'Ain Rahma', 519),
(1476, '1520', 'القلعة', 'Kalaa', 'Kalaa', 519),
(1477, '1538', 'سيدي سعادة', 'Sidi Saada', 'Sidi Saada', 519),
(1478, '1540', 'يلل', 'Yellel', 'Yellel', 519),
(1479, '1532', 'الرمكة', 'Ramka', 'Ramka', 520),
(1480, '1539', 'سوق الحد', 'Souk El Had', 'Souk El Had', 520),
(1481, '1524', 'منداس', 'Mendes', 'Mendes', 521),
(1482, '1528', 'وادي السلام', 'Oued Essalem', 'Oued Essalem', 521),
(1483, '1535', 'سيدي لزرق', 'Sidi Lazreg', 'Sidi Lazreg', 521),
(1484, '7', 'شروين', 'Charouine', 'Charouine', 522),
(1485, '14', 'أولاد عيسى', 'Ouled Aissa', 'Ouled Aissa', 522),
(1486, '19', 'طالمين', 'Talmine', 'Talmine', 522),
(1487, '15', 'أولاد السعيد', 'Ouled Said', 'Ouled Said', 523),
(1488, '24', 'تيميمون', 'Timimoun', 'Timimoun', 523),
(1489, '11', 'قصر قدور', 'Ksar Kaddour', 'Ksar Kaddour', 524),
(1490, '25', 'تنركوك', 'Tinerkouk', 'Tinerkouk', 524),
(1491, '3', 'أوقروت', 'Aougrout', 'Aougrout', 525),
(1492, '8', 'دلدول', 'Deldoul', 'Deldoul', 525),
(1493, '12', 'المطارفة', 'Metarfa', 'Metarfa', 525),
(1494, '5', 'برج باجي مختار', 'Bordj Badji Mokhtar', 'Bordj Badji Mokhtar', 526),
(1495, '23', 'تيمياوين', 'Timiaouine', 'Timiaouine', 526),
(1496, '237', 'الشعيبة', 'Chaiba', 'Chaiba', 527),
(1497, '240', 'الدوسن', 'Doucen', 'Doucen', 527),
(1498, '255', 'أولاد جلال', 'Ouled Djellal', 'Ouled Djellal', 527),
(1499, '232', 'بسباس', 'Besbes', 'Besbes', 528),
(1500, '258', 'رأس الميعاد', 'Ras El Miad', 'Ras El Miad', 528),
(1501, '259', 'سيدي  خالد', 'Sidi Khaled', 'Sidi Khaled', 528),
(1502, '274', 'القصابي', 'Ksabi', 'Ksabi', 529),
(1503, '279', 'أولاد خضير', 'Ouled-Khodeir', 'Ouled-Khodeir', 529),
(1504, '265', 'بني عباس', 'Beni-Abbes', 'Beni-Abbes', 530),
(1505, '282', 'تامترت', 'Tamtert', 'Tamtert', 530),
(1506, '271', 'إقلي', 'Igli', 'Igli', 531),
(1507, '269', 'الواتة', 'El Ouata', 'El Ouata', 532),
(1508, '266', 'بن يخلف', 'Beni-Ikhlef', 'Beni-Ikhlef', 533),
(1509, '273', 'كرزاز', 'Kerzaz', 'Kerzaz', 533),
(1510, '283', 'تيمودي', 'Timoudi', 'Timoudi', 533),
(1511, '360', 'إينغر', 'Inghar', 'Inghar', 534),
(1512, '357', 'عين صالح', 'Ain Salah', 'Ain Salah', 535),
(1513, '358', 'فقارة الزوى', 'Foggaret Ezzoua', 'Foggaret Ezzoua', 535),
(1514, '356', 'عين قزام', 'Ain Guezzam', 'Ain Guezzam', 536),
(1515, '363', 'تين زواتين', 'Tin Zouatine', 'Tin Zouatine', 537),
(1516, '1099', 'النزلة', 'Nezla', 'Nezla', 538),
(1517, '1106', 'تبسبست', 'Tebesbest', 'Tebesbest', 538),
(1518, '1108', 'تقرت', 'Touggourt', 'Touggourt', 538),
(1519, '1109', 'الزاوية العابدية', 'Zaouia El Abidia', 'Zaouia El Abidia', 538),
(1520, '1092', 'العالية', 'El Alia', 'El Alia', 539),
(1521, '1094', 'الحجيرة', 'El-Hadjira', 'El-Hadjira', 539),
(1522, '1090', 'بن ناصر', 'Benaceur', 'Benaceur', 540),
(1523, '1098', 'المنقر', 'M\'naguer', 'M\'naguer', 540),
(1524, '1105', 'الطيبات', 'Taibet', 'Taibet', 540),
(1525, '1091', 'بلدة اعمر', 'Blidet Amor', 'Blidet Amor', 541),
(1526, '1107', 'تماسين', 'Temacine', 'Temacine', 541),
(1527, '1097', 'المقارين', 'Megarine', 'Megarine', 542),
(1528, '1104', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 542),
(1529, '1158', 'برج الحواس', 'Bordj El Haouass', 'Bordj El Haouass', 543),
(1530, '1161', 'جانت', 'Djanet', 'Djanet', 543),
(1531, '1284', 'المغير', 'El-M\'ghaier', 'El-M\'ghaier', 544),
(1532, '1296', 'أم الطيور', 'Oum Touyour', 'Oum Touyour', 544),
(1533, '1302', 'سيدي خليل', 'Sidi Khelil', 'Sidi Khelil', 544),
(1534, '1303', 'سطيل', 'Still', 'Still', 544),
(1535, '1281', 'جامعة', 'Djamaa', 'Djamaa', 545),
(1536, '1293', 'المرارة', 'M\'rara', 'M\'rara', 545),
(1537, '1300', 'سيدي عمران', 'Sidi Amrane', 'Sidi Amrane', 545),
(1538, '1306', 'تندلة', 'Tenedla', 'Tenedla', 545),
(1539, '1495', 'المنيعة', 'El Meniaa', 'El Meniaa', 546),
(1540, '1499', 'حاسي القارة', 'Hassi Gara', 'Hassi Gara', 546),
(1541, '1498', 'حاسي الفحل', 'Hassi Fehal', 'Hassi Fehal', 547);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_06`
--

CREATE TABLE `t_01_02_06` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL,
  `f_06` double DEFAULT NULL,
  `f_04` double NOT NULL,
  `f_05` double NOT NULL,
  `f_07` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_02_06`
--

INSERT INTO `t_01_02_06` (`f_00`, `f_01`, `f_02`, `f_03`, `f_06`, `f_04`, `f_05`, `f_07`) VALUES
(1, NULL, NULL, 'Ain El Bia 1', 2, 35.807, -0.343, 1075),
(2, NULL, NULL, 'Ain El Bia 2', 5, 35.835, -0.238, 1075),
(3, NULL, NULL, 'Ain Naga', -2, 34.686, 6.094, 218),
(4, NULL, NULL, 'Ain Tsila', 350, 27.2, 10.5, 1113),
(5, NULL, NULL, 'Al Aricha', 650, 33.82, 1.9, 393),
(6, NULL, NULL, 'Al Atteuf', 495, 32.478, 3.748, 1444),
(7, NULL, NULL, 'Alger', 50, 36.45, 3.2, 544),
(8, NULL, NULL, 'Alrar', 60, 35.4, 0.45, 1116),
(9, NULL, NULL, 'Arzew', 5, 35.835, -0.238, 1075),
(10, NULL, NULL, 'Arzew', 2, 35.862, -0.345, 1065),
(11, NULL, NULL, 'Béjaïa', 52, 36.751, 5.059, 166),
(12, NULL, NULL, 'Beni Mensour', 450, 36.336, 4.356, 198),
(13, NULL, NULL, 'Beni Mensour', 750, 33.45, 2.45, 198),
(14, NULL, NULL, 'Beni Saf', 10, 35.301, -1.382, 1431),
(15, NULL, NULL, 'Berkine', 380, 26.418, 9.08, 1057),
(16, NULL, NULL, 'Bethioua', 2, 35.805, -0.241, 1065),
(17, NULL, NULL, 'Bir Rebaa North', 185, 31.85, 5.973, 1062),
(18, NULL, NULL, 'Bord Menaiel', 100, 36.38, 3.8, 1159),
(19, NULL, NULL, 'Bourmadia', 81, 35.732, 0.613, 1447),
(20, NULL, NULL, 'Chaiba', 389, 34.5, 5.25, 1498),
(21, NULL, NULL, 'Deb Deb', 350, 29.968, 9.424, 1115),
(22, NULL, NULL, 'Djamaa1', 280, 33.933, 5.93, 1531),
(23, NULL, NULL, 'Djamaa2', 850, 33.52, 2.85, 1535),
(24, NULL, NULL, 'Djebila', 973, 35.372, 1.325, 413),
(25, NULL, NULL, 'El Borma', 246, 31.006, 8.134, 1064),
(26, NULL, NULL, 'El Merk', 380, 26.418, 9.08, 1057),
(27, NULL, NULL, 'Faïdja', 185, 35.193, 1.706, 428),
(28, NULL, NULL, 'Ferme Omar Driss', 495, 35.034, 5.595, 231),
(29, NULL, NULL, 'Flifla', 5, 36.877, 6.906, 721),
(30, NULL, NULL, 'Gassi Touil', 267, 30.271, 6.416, 1060),
(31, NULL, NULL, 'Haoud El Hamra', 156, 31.941, 6.071, 1060),
(32, NULL, NULL, 'Hassi Berkine', 185, 30.559, 5.453, 1060),
(33, NULL, NULL, 'Hassi Delaa', 716, 32.935, 3.269, 58),
(34, NULL, NULL, 'Hassi Messaoud', 85, 31.97, 6.265, 1060),
(35, NULL, NULL, 'Hassi R\'mel', 777, 32.936, 3.269, 58),
(36, NULL, NULL, 'Hassi tedjirane', 180, 31, 7.85, 1064),
(37, NULL, NULL, 'In Amenas', 420, 28.041, 9.543, 1116),
(38, NULL, NULL, 'In Salah', 400, 28.62, 8.62, 1512),
(39, NULL, NULL, 'Isser', 100, 36.38, 3.8, 1169),
(40, NULL, NULL, 'Kenanda', 2, 35.737, 0.556, 1075),
(41, NULL, NULL, 'Krechba', 600, 28.642, 2.825, 1512),
(42, NULL, NULL, 'Labiodh Sidi Cheikh', 966, 32.91, 0.545, 1091),
(43, NULL, NULL, 'Mechtatine', 926, 36.123, 6.789, 1329),
(44, NULL, NULL, 'Medarreg1', 983, 35.425, 1.425, 421),
(45, NULL, NULL, 'Medarreg2', 1080, 35.184, 1.496, 430),
(46, NULL, NULL, 'Medarreg3', 971, 35.103, 1.706, 428),
(47, NULL, NULL, 'Mederba', 380, 27, 8.5, 1116),
(48, NULL, NULL, 'Medjedel', 710, 35.15, 3.683, 1006),
(49, NULL, NULL, 'Mers El Hadjadj', 2, 35.862, -0.345, 1065),
(50, NULL, NULL, 'Mesdar', 172, 31.192, 6.794, 1064),
(51, NULL, NULL, 'Milok1', 885, 33.481, 2.523, 61),
(52, NULL, NULL, 'Milok2', 885, 33.803, 2.875, 52),
(53, NULL, NULL, 'Mohgoun plateau1', 10, 35.83, -0.324, 1065),
(54, NULL, NULL, 'Mohgoun plateau2', 5, 35.863, -0.331, 1065),
(55, NULL, NULL, 'Moudjebara', 1100, 34.507, 3.47, 579),
(56, NULL, NULL, 'M\'seka1', 856, 33.789, 2.583, 52),
(57, NULL, NULL, 'M\'seka2', 875, 33.809, 2.59, 52),
(58, NULL, NULL, 'M\'seka3', 835, 33.769, 2.576, 52),
(59, NULL, NULL, 'M\'Sila', 600, 35.719, 4.523, 963),
(60, NULL, NULL, 'Nador', 1018, 35.288, 1.452, 413),
(61, NULL, NULL, 'Nezla', 182, 33.083, 6.051, 1057),
(62, NULL, NULL, 'N\'Gaous', 580, 35.557, 5.593, 124),
(63, NULL, NULL, 'Ohanet', 600, 28.68, 8.873, 1113),
(64, NULL, NULL, 'Ouargla1', 256, 31.951, 5.331, 1057),
(65, NULL, NULL, 'Ouargla2', 260, 31.933, 5.327, 1057),
(66, NULL, NULL, 'Oued Saf Saf', 85, 35.72, 1.5, 359),
(67, NULL, NULL, 'Ouled Djellal', 397, 34.417, 5.067, 1498),
(68, NULL, NULL, 'Ourhoud', 360, 31.7, 5.85, 1057),
(69, NULL, NULL, 'Reggane', 200, 26.8, -2.8, 4),
(70, NULL, NULL, 'Rhoude El Baguel', 300, 32.15, 2.15, 1064),
(71, NULL, NULL, 'Rhourde Nouss', 252, 29.74, 6.702, 1114),
(72, NULL, NULL, 'Sebaa', 280, 28.21, -0.156, 1),
(73, NULL, NULL, 'Sefiane', 575, 35.235, 6.123, 105),
(74, NULL, NULL, 'Selga/El Outaya', 185, 35.034, 5.595, 231),
(75, NULL, NULL, 'Sidi Arcine', 80, 36.754, 3.059, 544),
(76, NULL, NULL, 'Skikda', 18, 36.885, 6.924, 721),
(77, NULL, NULL, 'Timzhert 1', 890, 33.625, 2.739, 52),
(78, NULL, NULL, 'Timzhert 2', 912, 33.641, 2.741, 52),
(79, NULL, NULL, 'Timzhert 3', 875, 33.608, 2.736, 52),
(80, NULL, NULL, 'Tin-Fouyé 1', 470, 28.146, 6.826, 1113),
(81, NULL, NULL, 'Tin-Fouyé 2', 400, 27.5, 10.2, 1113),
(82, NULL, NULL, 'Tolga', 495, 34.833, 6.071, 237),
(83, NULL, NULL, 'Zaaroura', 1032, 35.345, 1.319, 413),
(84, NULL, NULL, 'Zina', 166, 31.788, 6.052, 1057),
(85, NULL, NULL, 'Zmelat El Arbi', 920, 33.78, 3.56, 595);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_02_07`
--

CREATE TABLE `t_01_02_07` (
  `f_00` bigint(20) NOT NULL,
  `f_04` double DEFAULT NULL,
  `f_02` double NOT NULL,
  `f_03` double NOT NULL,
  `f_01` int(11) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_01_03_01`
--

CREATE TABLE `t_01_03_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_06` bigint(20) DEFAULT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_03_01`
--

INSERT INTO `t_01_03_01` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_06`, `f_05`) VALUES
(1, 'DG', 'المديرية العامة سوناطراك', 'General Directorate - SONATRACH', 'Direction Générale SONATRACH', NULL, 1),
(2, 'EP', 'الاستكشاف والإنتاج', 'Exploration & Production', 'Exploration et Production', 1, 2),
(3, 'TRC', 'النقل عبر الأنابيب', 'Transport by Pipelines', 'Transport par Canalisations', 1, 2),
(4, 'LQS', 'التسييل والفصل', 'Liquefaction & Separation', 'Liquéfaction et Séparation', 1, 2),
(5, 'RPC', 'التكرير والبتروكيماويات', 'Refining & Petrochemicals', 'Raffinage et Pétrochimie', 1, 2),
(6, 'COM', 'التسويق', 'Commercialization', 'Commercialisation', 1, 2),
(7, 'EXP', 'قسم الاستغلال', 'Operations Division', 'Division Exploitation', 3, 3),
(8, 'MNT', 'قسم الصيانة', 'Maintenance Division', 'Division Maintenance', 3, 3),
(9, 'ESR', 'قسم الدراسات ومتابعة الإنجازات', 'Studies and Works Monitoring Division', 'Division Étude et Suivi des Réalisations', 3, 3),
(10, 'GFG', 'مديرية تسيير تدفق الغاز', 'Gas Flow Management Directorate', 'Direction Gestion Flux Gaz', 7, 4),
(11, 'RTO', 'المديرية الجهوية للنقل غرب', 'Western Regional Transport Directorate', 'Direction Regionale de Transport Ouest', 7, 6),
(12, 'HEH', 'المديرية الجهوية للنقل حوض الحمرة', 'Haoud El Hamra Regional Transport Directorate', 'Direction Regionale de Transport Haoud El Hamra', 7, 6),
(13, 'RTE', 'المديرية الجهوية للنقل الشرق', 'Eastern Regional Transport Directorate', 'Direction Regionale de Transport Est', 7, 6),
(14, 'RTI', 'المديرية الجهوية للنقل إليزي', 'Illizi Regional Transport Directorate', 'Direction Regionale de Transport Illizi', 7, 6),
(15, 'RTC', 'المديرية الجهوية للنقل الوسط', 'Central Regional Transport Directorate', 'Direction Regionale de Transport Centre', 7, 6),
(16, 'HRM', 'المديرية الجهوية للنقل حاسي الرمل', 'Hassi R’Mel Regional Transport Directorate', 'Direction Regionale de Transport Hassi R’Mel', 7, 6),
(17, 'GEM', 'مديرية عمليات خط أنابيب الغاز إنريكو ماتّي', 'Enrico Mattei Gas Pipeline Operational Directorate', 'Direction Opérationnelle du Gazoducs Enrico Mattei', 7, 5),
(18, 'GPDF', 'مديرية عمليات خط أنابيب الغاز بيدرو دوران فاريل', 'Pedro Duran Farell Gas Pipeline Operational Directorate', 'Direction Opérationnelle du Gazoduc Pedro Duran Farell', 7, 5),
(19, 'DGFG', 'إدارة تسيير التدفق', 'Flow Management Department', 'Département Getion Flux', 10, 8),
(20, 'DOG', 'إدارة عمليات خط أنابيب الغاز', 'Gas Pipeline Operations Department', 'Département Opérations Gasoduc', 10, 8);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_03_02`
--

CREATE TABLE `t_01_03_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_03_02`
--

INSERT INTO `t_01_03_02` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(1, 'CDGFG', 'Department Chef of Gas Flow Management', 'Department Chef of Gas Flow Management', 'Chef Départment Gestion Flux Gas', 19),
(2, 'CNDGC', 'Center Chef', 'Center Chef', 'Chef du centre', 16),
(3, 'CDHL', 'Center Chef', 'Center Chef', 'Chef de Centre', 12);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_03_03`
--

CREATE TABLE `t_01_03_03` (
  `f_00` bigint(20) NOT NULL,
  `f_08` varchar(200) DEFAULT NULL,
  `f_09` varchar(200) DEFAULT NULL,
  `f_05` datetime(6) DEFAULT NULL,
  `f_06` varchar(200) DEFAULT NULL,
  `f_07` varchar(200) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL,
  `f_11` bigint(20) DEFAULT NULL,
  `f_10` bigint(20) DEFAULT NULL,
  `f_12` bigint(20) DEFAULT NULL,
  `f_13` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_03_03`
--

INSERT INTO `t_01_03_03` (`f_00`, `f_08`, `f_09`, `f_05`, `f_06`, `f_07`, `f_02`, `f_04`, `f_01`, `f_03`, `f_11`, `f_10`, `f_12`, `f_13`) VALUES
(1, 'bbbbbb', 'bbbbbb', '1986-06-26 00:00:00.000000', 'حسين داي', 'Hussein Dey', 'عبير', 'Abir', 'مجراب', 'MEDJERAB', 1327, 548, 4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `t_01_03_04`
--

CREATE TABLE `t_01_03_04` (
  `f_14` varchar(50) DEFAULT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_15` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_01_03_04`
--

INSERT INTO `t_01_03_04` (`f_14`, `f_00`, `f_15`) VALUES
('SONT6000', 1, 3);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_01`
--

CREATE TABLE `t_02_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_01`
--

INSERT INTO `t_02_01_01` (`f_00`, `f_01`, `f_02`, `f_03`) VALUES
(1, 'مشغل عام وطني', 'National Public Operator', 'Opérateur Public National'),
(2, 'مشغل خاص وطني', 'National Private Operator', 'Opérateur Privé National'),
(3, 'مشغل دولي', 'International Operator', 'Opérateur International'),
(4, 'مشروع مشترك', 'Joint Venture', 'Coentreprise'),
(5, 'شركة نقل', 'Transport Company', 'Société de Transport'),
(6, 'شركة تكرير', 'Refining Company', 'Société de Raffinage'),
(7, 'شركة توزيع', 'Distribution Company', 'Société de Distribution'),
(8, 'شركة هندسية', 'Engineering Company', 'Société d\'Ingénierie'),
(9, 'مالك البنية التحتية', 'Infrastructure Owner', 'Propriétaire d\'Infrastructure'),
(10, 'مقاول EPC', 'EPC Contractor', 'Entrepreneur EPC'),
(11, 'مقاول بناء', 'Construction Contractor', 'Entrepreneur de Construction'),
(12, 'مستشار هندسي', 'Engineering Consultant', 'Consultant en Ingénierie'),
(13, 'إدارة مشاريع', 'Project Management', 'Gestion de Projet'),
(14, 'مصنع معدات', 'Equipment Manufacturer', 'Fabricant d\'Équipements'),
(15, 'مصنع أنابيب', 'Pipe Manufacturer', 'Fabricant de Tubes'),
(16, 'مورد أنظمة أتمتة', 'Automation Vendor', 'Fournisseur d\'Automatisation'),
(17, 'مورد أجهزة قياس', 'Instrumentation Vendor', 'Fournisseur d\'Instrumentation'),
(18, 'شركة فحص', 'Inspection Company', 'Société d\'Inspection'),
(19, 'هيئة اعتماد', 'Certification Body', 'Organisme de Certification'),
(20, 'مزود خدمة صيانة', 'Maintenance Service', 'Service de Maintenance'),
(21, 'مزود خدمات لوجستية', 'Logistics Provider', 'Prestataire Logistique'),
(22, 'مقاول محلي', 'Local Subcontractor', 'Sous-Traitant Local');

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_02`
--

CREATE TABLE `t_02_01_02` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_02`
--

INSERT INTO `t_02_01_02` (`f_00`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7),
(8),
(9);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_03`
--

CREATE TABLE `t_02_01_03` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_03`
--

INSERT INTO `t_02_01_03` (`f_00`) VALUES
(10),
(11),
(12),
(13),
(14),
(15),
(16),
(17),
(18),
(19),
(20),
(21),
(22);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_04`
--

CREATE TABLE `t_02_01_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_04`
--

INSERT INTO `t_02_01_04` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'ONET', 'نهائي تصدير بري', 'Onshore Export Terminal', 'Terminal d\'Exportation Terrestre'),
(2, 'OFET', 'نهائي تصدير بحري', 'Offshore Export Terminal', 'Terminal d\'Exportation Maritime'),
(3, 'DT', 'نهائي توزيع', 'Distribution Terminal', 'Terminal de Distribution'),
(4, 'TT', 'نهائي تحويل', 'Transfer Terminal', 'Terminal de Transfert'),
(5, 'PT', 'نهائي أنابيب', 'Pipeline Terminal', 'Terminal de Pipeline'),
(6, 'ST', 'نهائي تخزين', 'Storage Terminal', 'Terminal de Stockage'),
(7, 'PS', 'محطة ضخ', 'Pumping Station', 'Station de Pompage'),
(8, 'CS', 'محطة ضغط', 'Compression Station', 'Station de Compression'),
(9, 'RS', 'محطة تنظيم', 'Regulator Station', 'Station de Régulation'),
(10, 'CLS', 'محطة تجميع', 'Collect Station', 'Station de Collecte'),
(11, 'MS', 'محطة قياس', 'Metering Station', 'Station de Comptage'),
(12, 'DS', 'محطة تسليم', 'Delivery Station', 'Station de Livraison'),
(13, 'IS', 'محطة حقن', 'Injection Station', 'Station d\'Injection'),
(14, 'SS', 'محطة كاشطات', 'Scraper Station', 'Station de Raclage'),
(15, 'COS', 'محطة قطع', 'Cut-Off Station', 'Poste de Coupure'),
(16, 'SZS', 'محطة تقسيم', 'Sectionalizing Station', 'Poste de Sectionnement'),
(17, 'PGS', 'محطة تطهير', 'Purge Station', 'Poste de Purge'),
(18, 'OF', 'حقل نفط', 'Oil Field', 'Champ Pétrolier'),
(19, 'GF', 'حقل غاز', 'Gas Field', 'Champ Gazier'),
(20, 'CF', 'حقل مكثفات', 'Condensate Field', 'Champ de Condensats'),
(21, 'LNGC', 'مجمع الغاز الطبيعي المسال', 'LNG Complex', 'Complexe GNL'),
(22, 'LPGP', 'مصنع الغاز البترولي المسال', 'LPG Plant', 'Usine GPL'),
(23, 'RAF', 'مصفاة', 'Refinery', 'Raffinerie'),
(24, 'CNDP', 'مصنع المكثفات', 'Condensate Plant', 'Usine de Condensats'),
(25, 'PC', 'مجمع بتروكيماوي', 'Petrochemical Complex', 'Complexe Pétrochimique'),
(26, 'CPF', 'منشأة المعالجة المركزية', 'Central Processing Facility', 'Unité de Traitement Central'),
(27, 'EPF', 'منشأة الإنتاج المبكر', 'Early Production Facility', 'Installation de Production Précoce'),
(28, 'PM', 'مشعب الإنتاج', 'Production Manifold', 'Collecteur de Production'),
(29, 'GPC', 'مجمع معالجة الغاز', 'Gas Processing Complex', 'Complexe de Traitement du Gaz'),
(30, 'GSC', 'فصل وضغط الغاز', 'Gas Separation & Compression', 'Séparation et Compression du Gaz'),
(31, 'GEH', 'مركز تصدير الغاز', 'Gas Export Hub', 'Centre d\'Export du Gaz'),
(32, 'CRP', 'محطة استعادة المكثفات', 'Condensate Recovery Plant', 'Usine de Récupération des Condensats'),
(33, 'LCS', 'فصل الغاز المسال والمكثفات', 'LPG & Condensate Separation', 'Séparation GPL et Condensats'),
(34, 'LSC', 'مجمع فصل الغاز المسال', 'LPG Separation Complex', 'Complexe de Séparation du GPL'),
(35, 'LSU', 'وحدة فصل الغاز المسال', 'LPG Separation Unit', 'Unité de Séparation du GPL'),
(36, 'ES', 'فصل الإيثان', 'Ethane Separation', 'Séparation de l\'Éthane');

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_05`
--

CREATE TABLE `t_02_01_05` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_05`
--

INSERT INTO `t_02_01_05` (`f_00`) VALUES
(7),
(8),
(9),
(10),
(11),
(12),
(13),
(14),
(15),
(16),
(17);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_06`
--

CREATE TABLE `t_02_01_06` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_06`
--

INSERT INTO `t_02_01_06` (`f_00`) VALUES
(1),
(2),
(3),
(4),
(5),
(6);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_07`
--

CREATE TABLE `t_02_01_07` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_07`
--

INSERT INTO `t_02_01_07` (`f_00`) VALUES
(18),
(19),
(20);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_08`
--

CREATE TABLE `t_02_01_08` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_01_08`
--

INSERT INTO `t_02_01_08` (`f_00`) VALUES
(21),
(22),
(23),
(24),
(25),
(26),
(27),
(28),
(29),
(30),
(31),
(32),
(33),
(34),
(35),
(36);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_01_09`
--

CREATE TABLE `t_02_01_09` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_02_02_01`
--

CREATE TABLE `t_02_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_05` double NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_07` double NOT NULL,
  `f_09` bit(1) NOT NULL,
  `f_08` double NOT NULL,
  `f_06` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_02_01`
--

INSERT INTO `t_02_02_01` (`f_00`, `f_01`, `f_05`, `f_02`, `f_03`, `f_04`, `f_07`, `f_09`, `f_08`, `f_06`) VALUES
(1, 'GN', 0.7, 'الغاز الطبيعي', 'Natural Gas', 'Gaz naturel', -188, b'1', 0.01, 0.01),
(2, 'PB', 0.85, 'النفط الخام', 'Crude Oil', 'Pétrole brut', -30, b'1', 1.5, 10.5),
(3, 'COND', 0.75, 'المكثفات', 'Condensate', 'Condensat', -40, b'1', 0.05, 0.8),
(4, 'GPL', 0.55, 'غاز البترول المسال', 'Liquefied Petroleum Gas', 'Gaz de Pétrole Liquéfié (GPL)', -104, b'1', 0.02, 0.15);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_02_02`
--

CREATE TABLE `t_02_02_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_05` varchar(200) DEFAULT NULL,
  `f_06` varchar(200) DEFAULT NULL,
  `f_07` varchar(200) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_02_02`
--

INSERT INTO `t_02_02_02` (`f_00`, `f_01`, `f_05`, `f_06`, `f_07`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'INSERVICE', 'العمليات العادية', 'Normal operations', 'Opérations normales', 'قيد الخدمة', 'In Service', 'En Service'),
(2, 'STOPPED', 'ظروف السوق، المرونة التشغيلية', 'Market conditions, operational flexibility', 'Conditions du marché, flexibilité opérationnelle', 'متوقف مؤقتا', 'Stopped', 'À l\'Arrêt'),
(3, 'SHUTDOWN', 'صيانة مخططة أو حادث غير متوقع', 'Planned maintenance or unexpected incident', 'Maintenance planifiée ou incident imprévu', 'متوقف', 'Shutdown', 'Arrêté'),
(4, 'MAINTENANCE', 'إصلاحات مجدولة، فحوصات', 'Scheduled repairs, inspections', 'Réparations programmées, inspections', 'قيد الصيانة', 'Under Maintenance', 'En Maintenance'),
(5, 'TESTING', 'ما قبل التشغيل، فحوصات ما بعد الإصلاح', 'Pre-commissioning, post-repair verifications', 'Pré-mise en service, vérifications post-réparation', 'قيد الاختبار', 'Under Testing', 'En Test'),
(6, 'REDUCED', 'عطل في المعدات، قيود تشغيلية', 'Equipment failure, operational constraints', 'Panne d\'équipement, contraintes opérationnelles', 'طاقة مخفضة', 'Reduced Capacity', 'Capacité Réduite'),
(7, 'EMERGENCY', 'تسرب، انفجار، حادث أمني', 'Leak, rupture, security incident', 'Fuite, rupture, incident de sécurité', 'حالة طوارئ / حادث', 'Emergency / Incident', 'Urgence / Incident'),
(8, 'DECOMMISSIONED', 'نهاية العمر التشغيلي', 'End of operational life', 'Fin de vie opérationnelle', 'خارج الخدمة', 'Decommissioned', 'Mis Hors Service');

-- --------------------------------------------------------

--
-- Table structure for table `t_02_02_03`
--

CREATE TABLE `t_02_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_05` varchar(200) DEFAULT NULL,
  `f_06` varchar(200) DEFAULT NULL,
  `f_07` varchar(200) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_02_03`
--

INSERT INTO `t_02_02_03` (`f_00`, `f_01`, `f_05`, `f_06`, `f_07`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'API-5L-B', 'فولاذ كربوني منخفض المقاومة للأنابيب العامة، حد أدنى للخضوع 35 كيلو باوند/بوصة²', 'Low-strength carbon steel for general purpose pipelines, minimum yield 35 ksi', 'Acier au carbone de résistance faible pour pipelines usage général, limite élastique min 35 ksi', 'فولاذ API 5L درجة B', 'API 5L Grade B', 'Acier API 5L Grade B'),
(2, 'API-5L-X42', 'فولاذ متوسط المقاومة للضغوط المعتدلة، حد أدنى للخضوع 42 كيلو باوند/بوصة²', 'Medium-strength steel for moderate pressure applications, minimum yield 42 ksi', 'Acier de résistance moyenne pour applications à pression modérée, limite élastique min 42 ksi', 'فولاذ API 5L X42', 'API 5L Grade X42', 'Acier API 5L Grade X42'),
(3, 'API-5L-X52', 'فولاذ عالي المقاومة للأنابيب القياسية، حد أدنى للخضوع 52 كيلو باوند/بوصة²', 'High-strength steel for standard pipelines, minimum yield 52 ksi, widely used', 'Acier haute résistance pour pipelines standard, limite élastique min 52 ksi, très utilisé', 'فولاذ API 5L X52', 'API 5L Grade X52', 'Acier API 5L Grade X52'),
(4, 'API-5L-X60', 'فولاذ عالي المقاومة للنقل لمسافات طويلة، حد أدنى للخضوع 60 كيلو باوند/بوصة²', 'High-strength steel for long-distance transmission, minimum yield 60 ksi', 'Acier haute résistance pour transport longue distance, limite élastique min 60 ksi', 'فولاذ API 5L X60', 'API 5L Grade X60', 'Acier API 5L Grade X60'),
(5, 'API-5L-X65', 'فولاذ عالي المقاومة للأنظمة عالية الضغط، حد أدنى للخضوع 65 كيلو باوند/بوصة²', 'High-strength steel for high-pressure systems, minimum yield 65 ksi, excellent toughness', 'Acier haute résistance pour systèmes haute pression, limite élastique min 65 ksi, excellente ténacité', 'فولاذ API 5L X65', 'API 5L Grade X65', 'Acier API 5L Grade X65'),
(6, 'API-5L-X70', 'فولاذ فائق المقاومة للأنابيب الحديثة، حد أدنى للخضوع 70 كيلو باوند/بوصة²', 'Very high-strength steel for modern high-capacity pipelines, minimum yield 70 ksi', 'Acier très haute résistance pour pipelines modernes haute capacité, limite élastique min 70 ksi', 'فولاذ API 5L X70', 'API 5L Grade X70', 'Acier API 5L Grade X70'),
(7, 'API-5L-X80', 'فولاذ فائق المقاومة للظروف القصوى، حد أدنى للخضوع 80 كيلو باوند/بوصة²', 'Ultra high-strength steel for extreme conditions and ultra-high pressure, minimum yield 80 ksi', 'Acier ultra haute résistance pour conditions extrêmes et ultra haute pression, limite élastique min 80 ksi', 'فولاذ API 5L X80', 'API 5L Grade X80', 'Acier API 5L Grade X80'),
(8, 'SS-316L', 'فولاذ مقاوم للتآكل للغاز الحمضي، مقاوم ممتاز للكبريتيد والكلوريد', 'Corrosion-resistant steel for sour gas service, excellent resistance to H2S and chlorides', 'Acier résistant à la corrosion pour gaz acides, excellente résistance H2S et chlorures', 'فولاذ مقاوم للصدأ 316L', 'Stainless Steel 316L', 'Acier Inoxydable 316L'),
(9, 'DSS-2205', 'فولاذ عالي المقاومة ومقاوم للتآكل، مثالي للبيئات القاسية والتآكل بالنقر', 'High-strength corrosion-resistant steel, ideal for harsh environments and pitting resistance', 'Acier haute résistance et résistant à la corrosion, idéal pour environnements sévères et piqûres', 'فولاذ مزدوج 2205', 'Duplex Stainless Steel 2205', 'Acier Inoxydable Duplex 2205'),
(10, 'FBE', 'طلاء حماية خارجي أحادي الطبقة ضد التآكل، التصاق ممتاز ومقاومة كيميائية عالية', 'Single-layer external corrosion protection coating, excellent adhesion and chemical resistance', 'Revêtement externe monocouche protection corrosion, excellente adhérence et résistance chimique', 'إيبوكسي مدمج بالانصهار', 'Fusion Bonded Epoxy', 'Époxy Thermodurcissable'),
(11, '3LPE', 'نظام طلاء خارجي متعدد الطبقات (FBE + لاصق + PE)، حماية طويلة الأمد للأنابيب المدفونة', 'Multi-layer external coating (FBE + adhesive + PE), excellent long-term protection for buried pipelines', 'Système revêtement externe multicouche (FBE + adhésif + PE), excellente protection long terme pipelines enterrés', 'بولي إيثيلين ثلاثي الطبقات', '3-Layer Polyethylene', 'Polyéthylène 3 Couches'),
(12, '3LPP', 'نظام طلاء خارجي لدرجات الحرارة العالية (FBE + لاصق + PP)، مقاومة ميكانيكية وحرارية ممتازة', 'High-temperature external coating system (FBE + adhesive + PP), excellent mechanical and thermal resistance', 'Système revêtement externe haute température (FBE + adhésif + PP), excellente résistance mécanique et thermique', 'بولي بروبيلين ثلاثي الطبقات', '3-Layer Polypropylene', 'Polypropylène 3 Couches'),
(13, 'PU-EXT', 'طلاء خارجي مرن، مقاومة ممتازة للصدمات والتآكل الميكانيكي، مثالي للأنابيب المعرضة للحركة', 'Flexible external coating, excellent impact and abrasion resistance, ideal for pipelines subject to movement', 'Revêtement externe flexible, excellente résistance impacts et abrasion, idéal pipelines soumis à mouvement', 'طلاء بولي يوريثان خارجي', 'Polyurethane External Coating', 'Revêtement Polyuréthane Externe'),
(14, 'CTE', 'طلاء خارجي تقليدي، حماية ممتازة ضد التآكل التربة، تطبيق ساخن متعدد الطبقات', 'Traditional external coating, excellent protection against soil corrosion, hot-applied multi-layer system', 'Revêtement externe traditionnel, excellente protection contre corrosion sol, système multicouche appliqué à chaud', 'مينا قطران الفحم', 'Coal Tar Enamel', 'Email au Goudron de Houille'),
(15, 'TAPE-WRAP', 'نظام حماية خارجي بالشريط اللاصق، حماية ميكانيكية وختم ممتاز، سهل التطبيق والإصلاح', 'External tape wrap protection system, mechanical protection and excellent sealing, easy application and repair', 'Système protection externe par enrubannage, protection mécanique et excellent scellement, application et réparation faciles', 'نظام لف الشريط اللاصق', 'Tape Wrap System', 'Système Enrubannage'),
(16, 'CONCRETE', 'طلاء خارجي خرساني، حماية ميكانيكية وطفو سلبي للأنابيب البحرية والمغمورة', 'External concrete coating, mechanical protection and negative buoyancy for subsea and submerged pipelines', 'Revêtement externe béton, protection mécanique et flottabilité négative pour pipelines sous-marins et immergés', 'طلاء خرساني للوزن', 'Concrete Weight Coating', 'Revêtement Béton Lestage'),
(17, 'EPOXY-INT', 'بطانة داخلية للحماية من التآكل وتحسين كفاءة التدفق، سطح أملس يقلل الاحتكاك والترسبات', 'Internal lining for corrosion protection and flow efficiency, smooth surface reduces friction and deposits', 'Revêtement interne protection corrosion et efficacité écoulement, surface lisse réduit friction et dépôts', 'بطانة إيبوكسي داخلية', 'Internal Epoxy Lining', 'Revêtement Époxy Interne'),
(18, 'PU-INT', 'بطانة داخلية مرنة، مقاومة كيميائية ممتازة ومقاومة للتآكل، مناسبة للسوائل العدوانية', 'Flexible internal lining, excellent chemical and abrasion resistance, suitable for aggressive fluids', 'Revêtement interne flexible, excellente résistance chimique et abrasion, adapté fluides agressifs', 'بطانة بولي يوريثان داخلية', 'Polyurethane Internal Lining', 'Revêtement Polyuréthane Interne'),
(19, 'PHENOLIC-INT', 'بطانة داخلية لدرجات الحرارة العالية، مقاومة حرارية وكيميائية ممتازة حتى 200 درجة مئوية', 'High-temperature internal lining, excellent thermal and chemical resistance up to 200°C', 'Revêtement interne haute température, excellente résistance thermique et chimique jusqu\'à 200°C', 'بطانة فينولية داخلية', 'Phenolic Internal Lining', 'Revêtement Phénolique Interne'),
(20, 'CEMENT-INT', 'بطانة داخلية إسمنتية تقليدية، حماية من التآكل ومقاومة كيميائية جيدة، شائعة في أنابيب المياه', 'Traditional cement internal lining, corrosion protection and good chemical resistance, common in water pipelines', 'Revêtement interne ciment traditionnel, protection corrosion et bonne résistance chimique, courant canalisations eau', 'بطانة ملاط إسمنتي داخلية', 'Cement Mortar Lining', 'Revêtement Mortier Ciment Interne'),
(21, 'HDPE-INT', 'بطانة داخلية من البولي إيثيلين عالي الكثافة، سطح أملس جداً ومقاومة كيميائية ممتازة', 'High-density polyethylene internal lining, very smooth surface and excellent chemical resistance', 'Revêtement interne polyéthylène haute densité, surface très lisse et excellente résistance chimique', 'بطانة HDPE داخلية', 'HDPE Internal Lining', 'Revêtement PEHD Interne'),
(22, 'PRIMER-AC', 'طبقة أساسية تحضيرية مضادة للتآكل، تحسين الالتصاق وحماية أولية ضد التآكل', 'Preparatory base layer for corrosion protection, improves adhesion and provides initial corrosion protection', 'Couche base préparatoire protection corrosion, améliore adhérence et fournit protection initiale corrosion', 'طبقة أساس مضادة للتآكل', 'Anti-Corrosion Primer', 'Primaire Anticorrosion'),
(23, 'WAX-COAT', 'طلاء شمعي مؤقت للحماية أثناء النقل والتخزين، سهل الإزالة قبل التركيب', 'Temporary wax coating for protection during transport and storage, easily removable before installation', 'Revêtement cire temporaire protection durant transport et stockage, facilement amovible avant installation', 'طلاء شمعي', 'Wax Coating', 'Revêtement Cire');

-- --------------------------------------------------------

--
-- Table structure for table `t_02_02_04`
--

CREATE TABLE `t_02_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(20) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_02_04`
--

INSERT INTO `t_02_02_04` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`) VALUES
(1, 'Société Nationale pour la Recherche, la Production, le Transport, la Transformation et la Commercial', 'SONATRACH', 4, 1),
(2, 'Société Nationale de l\'Électricité et du Gaz', 'SONELGAZ', 4, 1),
(3, 'Société Nationale de Transport Maritime des Hydrocarbures', 'SNTM-HYPROC', 4, 1),
(4, 'Groupement TRC Reghaïa', 'GTP', 4, 2),
(5, 'Tassili Airlines', 'TASSILI', 4, 2),
(6, 'Ente Nazionale Idrocarburi', 'ENI', 111, 3),
(7, 'TotalEnergies SE', 'TOTALENERGIES', 77, 3),
(8, 'British Petroleum plc', 'BP', 235, 3),
(9, 'Equinor ASA', 'EQUINOR', 167, 3),
(10, 'Repsol S.A.', 'REPSOL', 210, 3),
(11, 'In Salah Gas Joint Venture', 'ISG-JV', 4, 4),
(12, 'In Amenas Joint Venture', 'IAM-JV', 4, 4),
(13, 'Tin Fouye Tabankort Condensate Company', 'TFCC', 4, 4),
(14, 'Société Nationale de Transport Maritime', 'SNTM', 4, 5),
(15, 'Groupement Transport Par Canalisation', 'GTP-CANAL', 4, 5),
(16, 'Raffinerie d\'Alger', 'RA1K', 4, 6),
(17, 'Raffinerie d\'Arzew', 'RA1Z', 4, 6),
(18, 'Raffinerie de Skikda', 'RA1S', 4, 6),
(19, 'Raffinerie d\'Hassi Messaoud', 'RHMS', 4, 6),
(20, 'Entreprise Nationale de Raffinage et de Distribution des Produits Pétroliers', 'NAFTAL', 4, 7),
(21, 'Algerian Distribution Company', 'ALDIC', 4, 7),
(22, 'Entreprise Nationale des Grands Travaux Pétroliers', 'ENGTP', 4, 8),
(23, 'Société Algérienne des Foires et Exportations', 'SAFIR', 4, 8),
(24, 'Technip Energies N.V.', 'TECHNIP', 77, 8),
(25, 'Saipem S.p.A.', 'SAIPEM', 111, 8),
(26, 'Algerian Energy Company', 'ALGERCO', 4, 9),
(27, 'Algerian National Pipeline Infrastructure', 'ALNAFT', 4, 9),
(28, 'China Petroleum & Chemical Corporation', 'Sinopec', 46, 4),
(29, 'Occidental Petroleum Corporation', 'Occidental', 237, 4);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_02_05`
--

CREATE TABLE `t_02_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(20) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_02_05`
--

INSERT INTO `t_02_02_05` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`) VALUES
(1, 'Saipem S.p.A.', 'SAIPEM-EPC', 111, 10),
(2, 'Technip Energies Algeria', 'TECHNIP-EPC', 77, 10),
(3, 'China National Petroleum Corporation', 'CNPC-EPC', 46, 10),
(4, 'Cosider Travaux Publics', 'COSIDER-TP', 4, 11),
(5, 'Entreprise de Travaux Routiers Hydrauliques et Bâtiments', 'ETRHB', 4, 11),
(6, 'ENGTP Construction Division', 'ENGTP-CONSTR', 4, 11),
(7, 'Wood Group Algeria', 'WOOD', 235, 12),
(8, 'Worley Algeria', 'WORLEY', 14, 12),
(9, 'Penspen Limited', 'PENSPEN', 235, 12),
(10, 'Amec Foster Wheeler Algeria', 'AFW-PMC', 235, 13),
(11, 'KBR Inc.', 'KBR', 237, 13),
(12, 'GE Oil & Gas Algeria', 'GE-OG', 237, 14),
(13, 'Siemens Energy Algeria', 'SIEMENS-E', 84, 14),
(14, 'Sulzer Pumps Services Algeria', 'SULZER', 216, 14),
(15, 'Nuovo Pignone Algeria', 'NUOVO-PIG', 111, 14),
(16, 'Alfapipe Spa', 'ALFAPIPE', 4, 15),
(17, 'Europipe GmbH', 'EUROPIPE', 84, 15),
(18, 'Vallourec Algérie', 'VALLOUREC', 77, 15),
(19, 'Schneider Electric Algérie', 'SCHNEIDER', 77, 16),
(20, 'Siemens Automation Algeria', 'SIEMENS-AUTO', 84, 16),
(21, 'Honeywell Process Solutions Algeria', 'HONEYWELL', 237, 16),
(22, 'Emerson Process Management Algeria', 'EMERSON', 237, 17),
(23, 'Endress+Hauser Algeria', 'E+H', 216, 17),
(24, 'ABB Measurement & Analytics Algeria', 'ABB-MEAS', 216, 17),
(25, 'Entreprise Nationale de Forage', 'ENAFOR', 4, 18),
(26, 'SGS Algeria', 'SGS', 216, 18),
(27, 'Applus+ Algeria', 'APPLUS', 210, 18),
(28, 'Bureau Veritas Algeria', 'BV', 77, 19),
(29, 'TÜV Rheinland Algeria', 'TUV', 84, 19),
(30, 'DNV Algeria', 'DNV', 167, 19),
(31, 'Entreprise Nationale des Travaux aux Puits', 'ENTP', 4, 20),
(32, 'Wood Group PSN Algeria', 'WOOD-PSN', 235, 20),
(33, 'Société de Transport de Marchandises Express', 'TRANSMEX', 4, 21),
(34, 'Gulf Agency Company Algeria', 'GAC', 234, 21),
(35, 'SDV Logistique Internationale Algérie', 'SDV', 77, 21),
(36, 'Kanaghaz Engineering', 'KANAGHAZ', 4, 22),
(37, 'General Emballage Algérie', 'GEA', 4, 22),
(38, 'Groupe Industriel des Ciments d\'Algérie', 'GICA', 4, 22),
(39, 'Société Nationale pour la Recherche, la Production, le Transport, la Transformation et la Commercial', 'SONATRACH', 4, 22);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_01`
--

CREATE TABLE `t_02_03_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_04` date DEFAULT NULL,
  `f_05` date DEFAULT NULL,
  `f_03` date DEFAULT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_06` bigint(20) NOT NULL,
  `f_07` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_01`
--

INSERT INTO `t_02_03_01` (`f_00`, `f_01`, `f_04`, `f_05`, `f_03`, `f_02`, `f_06`, `f_07`) VALUES
(1, 'ONET-DD', '2007-11-20', NULL, '2005-03-15', 'Terminal d\'Exportation Terrestre Deb Deb', 1, 10),
(2, 'ONET-OSS', '2010-09-15', NULL, '2008-06-10', 'Terminal d\'Exportation Terrestre Oued Saf Saf', 1, 10),
(3, 'ONET-BS', '2008-12-10', NULL, '2006-04-20', 'Terminal d\'Exportation Terrestre Beni Saf', 1, 10),
(4, 'DZAZW', '1981-06-20', NULL, '1978-01-15', 'Terminal d\'Exportation Maritime Arzew', 1, 10),
(5, 'DZSKI', '1975-09-25', NULL, '1972-03-10', 'Terminal d\'Exportation Maritime Skikda', 1, 10),
(6, 'DZBJA', '2001-03-18', NULL, '1998-05-12', 'Terminal d\'Exportation Maritime Bejaia', 1, 10),
(7, 'CDHL', '2005-10-15', NULL, '2003-02-08', 'Terminal de Distribution CDHL', 1, 10),
(8, 'CNDG', '2006-11-30', NULL, '2004-07-20', 'Terminal de Distribution CNDG', 1, 10),
(9, 'DT-MED', '2009-08-22', NULL, '2007-09-15', 'Terminal de Distribution Medjedel', 1, 10),
(10, 'DT-MSL', '2010-07-12', NULL, '2008-04-18', 'Terminal de Distribution M\'Sila', 1, 10),
(11, 'DT-DJM', '2011-06-25', NULL, '2009-11-05', 'Terminal de Distribution SP1 Bis Djamaa', 1, 10),
(12, 'TT-ARH', '2012-09-08', NULL, '2010-03-12', 'Terminal de Transfert Al Aricha', 1, 10),
(13, 'TT-BNM', '2013-11-15', NULL, '2011-05-20', 'Terminal de Transfert Beni Mensour', 1, 10),
(14, 'TT-HMD', '2004-12-20', NULL, '2002-08-10', 'Terminal de Transfert Hassi Messaoud', 1, 10),
(15, 'TT-MSD', '2007-10-30', NULL, '2005-06-15', 'Terminal de Transfert Mesdar', 1, 10),
(16, 'TT-OHT', '2008-08-18', NULL, '2006-09-22', 'Terminal de Transfert Ohanet', 1, 10),
(17, 'TT-137', '2009-07-20', NULL, '2007-12-05', 'Terminal de Transfert PC1/PK137', 1, 10),
(18, 'TT-RDN', '2010-05-25', NULL, '2008-03-18', 'Terminal de Transfert Rhoude Nouss', 1, 10),
(19, 'PT-ALG', '1997-09-15', NULL, '1995-01-10', 'Terminal de Pipeline Alger', 1, 10),
(20, 'PT-ALR', '2000-11-30', NULL, '1998-04-22', 'Terminal de Pipeline Alrar', 1, 10),
(21, 'PR-ARZ', '1983-02-28', NULL, '1980-06-15', 'Terminal de Pipeline Arzew', 1, 10),
(22, 'PT-BJA', '1999-12-10', NULL, '1997-08-20', 'Terminal de Pipeline Bejaia', 1, 10),
(23, 'PT-BRN', '2003-07-25', NULL, '2001-03-15', 'Terminal de Pipeline Bir Rebaa North', 1, 10),
(24, 'PT-BMN', '1998-08-22', NULL, '1996-11-08', 'Terminal de Pipeline Bord Menaiel', 1, 10),
(25, 'PT-BRM', '2002-10-18', NULL, '2000-05-12', 'Terminal de Pipeline El Borma', 1, 10),
(26, 'PT-GST', '2005-06-20', NULL, '2003-09-05', 'Terminal de Pipeline Gassi Touil', 1, 10),
(27, 'PT-HBK', '2006-04-30', NULL, '2004-02-18', 'Terminal de Pipeline Hassi Berkine', 1, 10),
(28, 'PT-HTJ', '2007-09-15', NULL, '2005-07-22', 'Terminal de Pipeline Hassi tedjirane', 1, 10),
(29, 'PT-NMS', '2004-08-25', NULL, '2002-10-08', 'Terminal de Pipeline In Amenas', 1, 10),
(30, 'PT-NSL', '2008-11-20', NULL, '2006-12-15', 'Terminal de Pipeline In Salah', 1, 10),
(31, 'PT-ISR', '1999-05-10', NULL, '1997-03-20', 'Terminal de Pipeline Isser', 1, 10),
(32, 'PT-RGN', '2012-10-30', NULL, '2010-06-12', 'Terminal de Pipeline Reggane', 1, 10),
(33, 'PT-RBL', '2003-12-22', NULL, '2001-09-18', 'Terminal de Pipeline Rhoude El Baguel', 1, 10),
(34, 'PT-SKD', '1978-01-15', NULL, '1975-04-10', 'Terminal de Pipeline Skikda', 1, 10),
(35, 'SP1-OZ1', '2001-09-20', NULL, '2000-01-15', 'Station de Pompage SP1-OZ1', 1, 10),
(36, 'SP1-OZ2', '2001-09-20', NULL, '2000-01-15', 'Station de Pompage SP1-OZ2', 1, 10),
(37, 'SP2-OZ1', '2002-11-15', NULL, '2001-03-10', 'Station de Pompage SP2-OZ1', 1, 10),
(38, 'SP2-OZ2', '2002-11-15', NULL, '2001-03-10', 'Station de Pompage SP2-OZ2', 1, 10),
(39, 'SP3-OZ1', '2003-12-30', NULL, '2002-05-20', 'Station de Pompage SP2-OZ1', 1, 10),
(40, 'SP3-OZ2', '2003-12-30', NULL, '2002-05-20', 'Station de Pompage SP2-OZ2', 1, 10),
(41, 'SP4-OZ1', '2005-03-22', NULL, '2003-08-15', 'Station de Pompage SP2-OZ1', 1, 10),
(42, 'SP4-OZ2', '2005-03-22', NULL, '2003-08-15', 'Station de Pompage SP2-OZ2', 1, 10),
(43, 'SP5-OZ1', '2006-06-18', NULL, '2004-10-05', 'Station de Pompage SP2-OZ1', 1, 10),
(44, 'SP5-OZ2', '2006-06-18', NULL, '2004-10-05', 'Station de Pompage SP2-OZ2', 1, 10),
(45, 'SP6-OZ1', '2007-08-25', NULL, '2005-12-12', 'Station de Pompage SP2-OZ1', 1, 10),
(46, 'SP6-OZ2', '2007-08-25', NULL, '2005-12-12', 'Station de Pompage SP2-OZ2', 1, 10),
(47, 'PRS-OZ1', '2007-11-10', NULL, '2006-03-20', 'Station de Pompage PRS-OZ1', 1, 10),
(48, 'PRS-OZ2', '2007-11-10', NULL, '2006-03-20', 'Station de Régulation PRS-OZ2', 1, 10),
(49, 'SP1-OK1', '2003-02-20', NULL, '2001-06-15', 'Station de Pompage SP1-OK1', 1, 10),
(50, 'SP1-OK1-NEW', '2012-01-15', NULL, '2010-04-10', 'Station de Pompage SP1-OK1-NEW', 1, 10),
(51, 'SP2-OK1', '2004-05-30', NULL, '2002-08-22', 'Station de Pompage SP2-OK1', 1, 10),
(52, 'SP3-OK1', '2005-07-18', NULL, '2003-11-05', 'Station de Pompage SP3-OK1', 1, 10),
(53, 'SP3-OK1-BIS', '2010-10-28', NULL, '2009-02-15', 'Station de Pompage SP3-OK1-BIS', 1, 10),
(54, 'SP4-OK1', '2006-01-22', NULL, '2004-05-18', 'Station de Pompage SP4-OK1', 1, 10),
(55, 'SP1-OB1', '2003-11-25', NULL, '2002-03-12', 'Station de Pompage SP1-OB1', 1, 10),
(56, 'SP1-BIS-OB1', '2010-04-15', NULL, '2008-07-20', 'Station de Pompage SP1-BIS-OB1', 1, 10),
(57, 'SP2-OB1', '2005-06-18', NULL, '2003-09-10', 'Station de Pompage SP2-OB1', 1, 10),
(58, 'SP3-OB1', '2006-08-22', NULL, '2004-12-05', 'Station de Pompage SP3-OB1', 1, 10),
(59, 'SP1-BIS-OG1', '2009-01-30', NULL, '2007-04-18', 'Station de Pompage SP1-BIS-OG1', 1, 10),
(60, 'SP1-OT1', '2006-10-15', NULL, '2005-02-22', 'Station de Pompage SP1-OT1', 1, 10),
(61, 'SP2-OT1', '2006-10-15', NULL, '2005-02-22', 'Station de Pompage SP2-OT1', 1, 10),
(62, 'SP1-OH1', '2008-01-20', NULL, '2006-05-15', 'Station de Pompage SP1-OH1', 1, 10),
(63, 'SP2-OH1', '2008-01-20', NULL, '2006-05-15', 'Station de Pompage SP2-OH1', 1, 10),
(64, 'SP3-OH1', '2008-01-20', NULL, '2006-05-15', 'Station de Pompage SP3-OH1', 1, 10),
(65, 'SP1-OD1', '2009-04-25', NULL, '2007-08-10', 'Station de Pompage SP1-OD1', 1, 10),
(66, 'SP1-OH2', '2010-07-30', NULL, '2008-11-15', 'Station de Pompage SP1-OH2', 1, 10),
(67, 'SP1-OH3/OH4', '2011-01-15', NULL, '2009-03-20', 'Station de Pompage SP1-OH3/OH4', 1, 10),
(68, 'SP2-OH3/OH4', '2011-01-15', NULL, '2009-03-20', 'Station de Pompage SP2-OH3/OH4', 1, 10),
(69, 'SP4-NZ1', '2012-03-22', NULL, '2010-06-08', 'Station de Pompage SP4-NZ1', 1, 10),
(70, 'SP5-NZ1', '2012-03-22', NULL, '2010-06-08', 'Station de Pompage SP5-NZ1', 1, 10),
(71, 'SP6-NZ1', '2012-03-22', NULL, '2010-06-08', 'Station de Pompage SP6-NZ1', 1, 10),
(72, 'PRS-NZ1', '2012-03-22', NULL, '2010-06-08', 'Station de Régulation PRS-NZ1', 1, 10),
(73, 'SP1-NK1', '2013-06-18', NULL, '2011-09-12', 'Station de Pompage SP1-NK1', 1, 10),
(74, 'SP3-NK1', '2013-06-18', NULL, '2011-09-12', 'Station de Pompage SP3-NK1', 1, 10),
(75, 'SP4-LNZ1', '2013-11-20', NULL, '2012-02-15', 'Station de Pompage SP4-LNZ1', 1, 10),
(76, 'SP5-LNZ1', '2013-11-20', NULL, '2012-02-15', 'Station de Pompage SP5-LNZ1', 1, 10),
(77, 'SP6-LNZ1', '2013-11-20', NULL, '2012-02-15', 'Station de Pompage SP6-LNZ1', 1, 10),
(78, 'SP0-LZ1', '2015-01-25', NULL, '2013-04-10', 'Station de Pompage SP0-LZ1', 1, 10),
(79, 'SP1-LZ1', '2015-01-25', NULL, '2013-04-10', 'Station de Pompage SP1-LZ1', 1, 10),
(80, 'SP1-LZ2', '2015-01-25', NULL, '2013-04-10', 'Station de Pompage SP1-LZ2', 1, 10),
(81, 'PRS-LZ1/LZ2', '2015-01-25', NULL, '2013-04-10', 'Station de Régulation PRS-LZ1/LZ2', 1, 10),
(82, 'SPRN-LR1', '2016-04-30', NULL, '2014-07-18', 'Station de Pompage SPRN-LR1', 1, 10),
(83, 'SP0-LR1', '2016-04-30', NULL, '2014-07-18', 'Station de Pompage SP1-LR1', 1, 10),
(84, 'SP1-LR1', '2016-04-30', NULL, '2014-07-18', 'Station de Pompage SP1-LR1', 1, 10),
(85, 'SP2-LR1', '2016-04-30', NULL, '2014-07-18', 'Station de Pompage SP2-LR1', 1, 10),
(86, 'SP1-LNZ1', '2017-08-15', NULL, '2015-10-22', 'Station de Pompage SP1-LNZ1', 1, 10),
(87, 'SP3-LNZ1', '2017-08-15', NULL, '2015-10-22', 'Station de Pompage SP3-LNZ1', 1, 10),
(88, 'SC1-GZ0', '2002-01-15', NULL, '2000-02-10', 'Station de Compression SC1-GZ0', 1, 10),
(89, 'SC2-GZ0', '2002-01-15', NULL, '2000-02-10', 'Station de Compression SC2-GZ0', 1, 10),
(90, 'SC3-GZ0', '2002-01-15', NULL, '2000-02-10', 'Station de Compression SC3-GZ0', 1, 10),
(91, 'SC4-GZ0', '2002-01-15', NULL, '2000-02-10', 'Station de Compression SC4-GZ0', 1, 10),
(92, 'SC1-GZ1', '2005-04-20', NULL, '2003-05-15', 'Station de Compression SC1-GZ1', 1, 10),
(93, 'SC1-GZ2', '2005-04-20', NULL, '2003-05-15', 'Station de Compression SC1-GZ2', 1, 10),
(94, 'SC1-GZ3', '2005-04-20', NULL, '2003-05-15', 'Station de Compression SC1-GZ3', 1, 10),
(95, 'SC2-GZ1', '2006-07-25', NULL, '2004-08-20', 'Station de Compression SC2-GZ1', 1, 10),
(96, 'SC2-GZ2', '2006-07-25', NULL, '2004-08-20', 'Station de Compression SC2-GZ2', 1, 10),
(97, 'SC2-GZ3', '2006-07-25', NULL, '2004-08-20', 'Station de Compression SC2-GZ3', 1, 10),
(98, 'SC3-GZ1', '2007-10-15', NULL, '2005-11-10', 'Station de Compression SC3-GZ1', 1, 10),
(99, 'SC3-GZ2', '2007-10-15', NULL, '2005-11-10', 'Station de Compression SC3-GZ2', 1, 10),
(100, 'SC3-GZ3', '2007-10-15', NULL, '2005-11-10', 'Station de Compression SC3-GZ3', 1, 10),
(101, 'SC4-GZ1', '2008-02-20', NULL, '2006-03-15', 'Station de Compression SC4-GZ1', 1, 10),
(102, 'SC4-GZ2', '2008-02-20', NULL, '2006-03-15', 'Station de Compression SC4-GZ2', 1, 10),
(103, 'SC4-GZ3', '2008-02-20', NULL, '2006-03-15', 'Station de Compression SC4-GZ3', 1, 10),
(104, 'SC5-GZ1', '2009-05-25', NULL, '2007-06-20', 'Station de Compression SC5-GZ1', 1, 10),
(105, 'SC5-GZ2', '2009-05-25', NULL, '2007-06-20', 'Station de Compression SC5-GZ2', 1, 10),
(106, 'SC5-GZ3', '2009-05-25', NULL, '2007-06-20', 'Station de Compression SC5-GZ3', 1, 10),
(107, 'SC3-GPDF', '2010-08-18', NULL, '2008-09-12', 'Station de Compression SC3-GPDF', 1, 10),
(108, 'SC1-GK1', '2011-11-10', NULL, '2009-12-05', 'Station de Compression SC1-GK1', 1, 10),
(109, 'SC2-GK1', '2011-11-10', NULL, '2009-12-05', 'Station de Compression SC2-GK1', 1, 10),
(110, 'SC3-GK1', '2011-11-10', NULL, '2009-12-05', 'Station de Compression SC3-GK1', 1, 10),
(111, 'SC4-GK1', '2011-11-10', NULL, '2009-12-05', 'Station de Compression SC4-GK1', 1, 10),
(112, 'SC5-GK1', '2011-11-10', NULL, '2009-12-05', 'Station de Compression SC5-GK1', 1, 10),
(113, 'S01-GO2', '2012-03-20', NULL, '2010-04-15', 'Station de Compression S01-GO2', 1, 10),
(114, 'S01-GG1', '2013-06-25', NULL, '2011-07-20', 'Station de Compression S01-GG1', 1, 10),
(115, 'S01-GR5', '2014-09-15', NULL, '2012-10-10', 'Station de Compression S01-GR5', 1, 10),
(116, 'S01-GR7', '2015-01-22', NULL, '2013-02-18', 'Station de Compression S01-GR7', 1, 10),
(117, 'SC-TFT', '2016-04-28', NULL, '2014-05-22', 'Station de Compression SC-TFT', 1, 10),
(118, 'SC1-GR2', '2017-07-18', NULL, '2015-08-12', 'Station de Compression SC1-GR2', 1, 10),
(119, 'SC2-GR2', '2017-07-18', NULL, '2015-08-12', 'Station de Compression SC2-GR2', 1, 10),
(120, 'SC3-GR2', '2017-07-18', NULL, '2015-08-12', 'Station de Compression SC3-GR2', 1, 10),
(121, 'SC4-GR2', '2017-07-18', NULL, '2015-08-12', 'Station de Compression SC4-GR2', 1, 10),
(122, 'GL1Z', '1964-10-02', NULL, '1962-09-14', 'Complexe GNL GL1Z', 1, 4),
(123, 'GL2Z', '1981-09-20', NULL, '1978-03-15', 'Complexe GNL GL2Z', 1, 4),
(124, 'GL3Z', '2013-12-10', NULL, '2010-05-20', 'Complexe GNL GL3Z', 1, 4),
(125, 'GL1K', '1972-07-15', NULL, '1967-01-01', 'Complexe GNL GL1K', 1, 4),
(126, 'GP1Z', '1974-07-30', NULL, '1970-02-18', 'Usine GPL GP1Z', 1, 4),
(127, 'GP2Z', '1980-10-15', NULL, '1976-04-22', 'Usine GPL GP2Z', 1, 4),
(128, 'RA1G', '1964-03-20', NULL, '1961-06-10', 'Raffinerie RA1G', 1, 5),
(129, 'RA1Z', '1975-05-25', NULL, '1971-09-15', 'Raffinerie RA1Z', 1, 5),
(130, 'RHM2', '1998-08-30', NULL, '1995-11-20', 'Raffinerie RHM2', 1, 5),
(131, 'RA1K', '1980-12-18', NULL, '1977-03-12', 'Raffinerie RA1K', 1, 5),
(132, 'RA2K', '1980-12-18', NULL, '1977-03-12', 'Raffinerie RA2K', 1, 5),
(133, 'RA1D', '1973-02-15', NULL, '1968-05-08', 'Raffinerie RA1D', 1, 5),
(134, 'CP1Z', '1982-11-22', NULL, '1979-07-18', 'Usine de Condensats CP1Z', 1, 4),
(135, 'CP2K', '1984-06-30', NULL, '1980-10-25', 'Usine de Condensats CP2K', 1, 4),
(136, 'SORFERT', '1975-09-20', NULL, '1970-01-15', 'Complexe Pétrochimique SORFERT', 1, 5),
(137, 'AOA', '2008-11-15', NULL, '2003-04-10', 'Complexe Pétrochimique AOA', 1, 5),
(138, 'CPF-OHD', '2001-12-28', NULL, '1998-06-20', 'Usine d\'Ourhoud', 1, 2),
(139, 'CPF-MNS', '2003-08-30', NULL, '2000-09-15', 'Unité de Traitement Central d\'In Amenas', 1, 2),
(140, 'CPF-BBK', '2004-10-25', NULL, '2001-11-20', 'Unité de Traitement Central de Berkine (El Merk)', 1, 2),
(141, 'GPC-HRM', '1982-07-15', NULL, '1978-03-10', 'Station Centrale de Hassi R\'Mel', 1, 2),
(142, 'GPC-OHT', '2003-11-20', NULL, '2000-05-12', 'Unité de Traitement Central d\'Ohanet - Train de Gaz', 1, 2),
(143, 'GPC-TFT', '2007-06-25', NULL, '2004-08-18', 'Unité de Traitement Central de Tin-Fouyé-Tabenkort', 1, 2),
(144, 'GPC-TSL', '2025-08-20', NULL, '2022-03-15', 'Complexe Gazier d\'Ain Tsila', 1, 2),
(145, 'GPC-RGN', '2014-04-30', NULL, '2010-10-22', 'Traitement du Gaz du Bassin de Reggane', 1, 2),
(146, 'CRP-OHT', '2004-03-20', NULL, '2001-07-15', 'Unité de Condensats d\'Ohanet', 1, 2),
(147, 'CRP-TFT', '2008-05-28', NULL, '2005-09-20', 'Unité de Condensats de Tin-Fouyé', 1, 2),
(148, 'CRP-TSL', '2025-10-15', NULL, '2023-01-10', 'Usine de récupération des Condensats d\'Ain Tsila', 1, 2),
(149, 'CRP-MRK', '2005-07-12', NULL, '2002-11-05', 'Unité de Condensats d\'El Merk', 1, 2),
(150, 'CRP-MNS', '2004-02-25', NULL, '2001-06-18', 'Usine de récupération des Condensats d\'In Amenas', 1, 2),
(151, 'LPG1-HRM', '1985-12-15', NULL, '1982-04-20', 'Unités GPL/Condensats de Hassi R\'Mel', 1, 2),
(152, 'LPG2-HRM', '1993-06-20', NULL, '1990-08-10', 'Complexe de récupération Centrale GPL de Hassi R\'Mel', 1, 2),
(153, 'LPGE-HRM', '1998-09-22', NULL, '1995-11-15', 'Complexe de récupération d\'Éthane de Hassi R\'Mel', 1, 2),
(154, 'LPG-TFT', '2009-01-30', NULL, '2006-03-25', 'Complexe de récupération GPL de Tin-Fouyé', 1, 2),
(155, 'LPG-TSL', '2025-12-18', NULL, '2023-06-12', 'Complexe de récupération GPL d\'Ain Tsila', 1, 2),
(156, 'OZ1', '1967-10-30', NULL, '1965-02-15', 'Pipeline OZ1', 1, 10),
(157, 'OZ2', '2003-08-18', NULL, '2003-08-18', 'Pipeline OZ2', 1, 10),
(158, 'OK1', '1972-01-01', NULL, '1972-01-01', 'Pipeline OK1', 1, 10),
(159, 'OB1', '1959-12-05', NULL, '1957-09-01', 'Pipeline OB1', 1, 10),
(160, 'ROB1-01', '1999-11-15', NULL, '1996-06-20', 'Pipeline ROB1-01', 1, 10),
(161, 'ROB1-02', '2003-08-22', NULL, '2000-03-10', 'Pipeline ROB1-02', 1, 10),
(162, 'OG1', '2005-07-04', NULL, '2005-07-04', 'Pipeline OG1', 1, 10),
(163, 'OT1', '1960-01-15', NULL, '1958-06-01', 'Pipeline OT1', 1, 10),
(164, 'OH1', '1982-01-01', NULL, '1982-01-01', 'Pipeline OH1', 1, 10),
(165, 'ROH1', '2008-06-25', NULL, '2005-08-18', 'Pipeline ROH1', 1, 10),
(166, 'OH2', '1972-04-10', NULL, '1972-04-10', 'Pipeline OH2', 1, 10),
(167, 'OH3', '2001-05-15', NULL, '1997-02-15', 'Pipeline OH3', 1, 10),
(168, 'OH4', '2006-01-01', NULL, '2006-01-01', 'Pipeline OH4', 1, 10),
(169, 'OD1', '1977-03-23', NULL, '1977-03-23', 'Pipeline OD1', 1, 10),
(170, 'ROD1', '2008-09-30', NULL, '2005-04-15', 'Pipeline ROD1', 1, 10),
(171, 'OD2', '2009-04-18', NULL, '2006-09-10', 'Pipeline OD2', 1, 10),
(172, 'OD3', '2010-08-20', NULL, '2007-10-25', 'Pipeline OD3', 1, 10),
(173, 'NZ1', '1978-11-01', NULL, '1978-11-01', 'Pipeline NZ1', 1, 10),
(174, 'NK1', '2009-08-04', NULL, '2009-08-04', 'Pipeline NK1', 1, 10),
(175, 'NH2', '1960-01-01', NULL, '1960-01-01', 'Pipeline NH2', 1, 10),
(176, 'LNZ1', '2005-10-22', NULL, '2003-01-18', 'Pipeline LNZ1', 1, 10),
(177, 'LZ1', '1984-02-08', NULL, '1984-02-08', 'Pipeline LZ1', 1, 10),
(178, 'LZ2', '2010-11-01', NULL, '2010-11-01', 'Pipeline LZ2', 1, 10),
(179, 'LR1', '1996-01-01', NULL, '1996-01-01', 'Pipeline LR1', 1, 10),
(180, 'DLR1', '2004-10-04', NULL, '2004-10-04', 'Pipeline DLR1', 1, 10),
(181, 'ELR1', '2014-01-01', NULL, '2014-01-01', 'Pipeline ELR1', 1, 10),
(182, 'GZ0', '1961-04-01', NULL, '1960-03-01', 'Pipeline GZ0', 1, 10),
(183, 'GZ0 Bis', '2022-09-01', NULL, '2022-09-01', 'Pipeline GZ0 Bis', 1, 10),
(184, 'GZ1', '1976-01-01', NULL, '1976-01-01', 'Pipeline GZ1', 1, 10),
(185, 'GZ2', '1982-01-01', NULL, '1982-01-01', 'Pipeline GZ2', 1, 10),
(186, 'RGZ2', '2005-02-22', NULL, '2002-03-18', 'Pipeline RGZ2', 1, 10),
(187, 'GZ3', '1989-01-01', NULL, '1989-01-01', 'Pipeline GZ3', 1, 10),
(188, 'RGZ3-01', '2018-01-01', NULL, '2018-01-01', 'Pipeline RGZ3-01', 1, 10),
(189, 'RGZ3-02', '2006-08-20', NULL, '2003-05-12', 'Pipeline RGZ3-02', 1, 10),
(190, 'GZ4-01', '2010-04-16', NULL, '2010-04-16', 'Pipeline GZ4-01', 1, 10),
(191, 'GZ4-02', '2006-10-28', NULL, '2004-01-20', 'Pipeline GZ4-02', 1, 10),
(192, 'GZ4-03', '2007-11-30', NULL, '2005-02-15', 'Pipeline GZ4-03', 1, 10),
(193, 'GK1', '1978-01-01', NULL, '1971-01-01', 'Pipeline GK1', 1, 10),
(194, 'GK2', '2001-01-01', NULL, '1998-01-01', 'Pipeline GK2', 1, 10),
(195, 'GK3-01', '2012-01-01', NULL, '2012-01-01', 'Pipeline GK3-01', 1, 10),
(196, 'GK3-02', '2005-10-15', NULL, '2003-03-20', 'Pipeline GK3-02', 1, 10),
(197, 'GG1', '1981-01-01', NULL, '1981-01-01', 'Pipeline GG1', 1, 10),
(198, 'RGG1', '2019-01-01', NULL, '2019-01-01', 'Pipeline RGG1', 1, 10),
(199, 'GM1', '1996-11-20', NULL, '1993-09-10', 'Pipeline GM1', 1, 10),
(200, 'GR1', '1986-01-01', NULL, '1986-01-01', 'Pipeline GR1', 1, 10),
(201, 'GR2', '1999-01-01', NULL, '1999-01-01', 'Pipeline GR2', 1, 10),
(202, 'GR3', '2007-07-22', NULL, '2004-03-18', 'Pipeline GR3', 1, 10),
(203, 'GR4', '2014-01-01', NULL, '2014-01-01', 'Pipeline GR4', 1, 10),
(204, 'GR5', '2017-06-01', NULL, '2017-06-01', 'Pipeline GR5', 1, 10),
(205, 'GR6', '2017-01-01', NULL, '2017-01-01', 'Pipeline GR6', 1, 10),
(206, 'GR7', '2019-12-01', NULL, '2019-12-01', 'Pipeline GR7', 1, 10),
(207, 'GPDF', '1996-01-01', NULL, '1996-01-01', 'Pipeline GPDF', 1, 10),
(208, 'EGPDF', '2021-01-01', NULL, '2021-01-01', 'Pipeline EGPDF', 1, 10),
(209, 'GO1', '1982-01-01', NULL, '1982-01-01', 'Pipeline GO1', 1, 10),
(210, 'GO2', '1987-01-01', NULL, '1987-01-01', 'Pipeline GO2', 1, 10),
(211, 'GO3', '2010-01-01', NULL, '2008-01-01', 'Pipeline GO3', 1, 10);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_02`
--

CREATE TABLE `t_02_03_02` (
  `f_00` bigint(20) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_08` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_02`
--

INSERT INTO `t_02_03_02` (`f_00`, `f_09`, `f_08`) VALUES
(1, 21, 1),
(2, 67, 1),
(3, 14, 1),
(4, 2, 1),
(5, 76, 1),
(6, 11, 1),
(7, 34, 1),
(8, 35, 1),
(9, 48, 1),
(10, 59, 1),
(11, 23, 1),
(12, 5, 1),
(13, 13, 1),
(14, 34, 1),
(15, 50, 1),
(16, 63, 1),
(17, 85, 1),
(18, 71, 1),
(19, 7, 1),
(20, 8, 1),
(21, 9, 1),
(22, 11, 1),
(23, 17, 1),
(24, 18, 1),
(25, 25, 1),
(26, 30, 1),
(27, 32, 1),
(28, 36, 1),
(29, 37, 1),
(30, 38, 1),
(31, 39, 1),
(32, 69, 1),
(33, 71, 1),
(34, 76, 1),
(35, 31, 1),
(36, 31, 1),
(37, 64, 1),
(38, 65, 1),
(39, 6, 1),
(40, 6, 1),
(41, 35, 1),
(42, 35, 1),
(43, 51, 1),
(44, 51, 1),
(45, 24, 1),
(46, 24, 1),
(47, 40, 1),
(48, 40, 1),
(49, 31, 1),
(50, 22, 1),
(51, 82, 1),
(52, 83, 1),
(53, 62, 1),
(54, 31, 1),
(55, 22, 1),
(56, 28, 1),
(57, 59, 1),
(58, 12, 1),
(59, 37, 1),
(60, 21, 1),
(61, 37, 1),
(62, 47, 1),
(63, 30, 1),
(64, 32, 1),
(65, 64, 1),
(66, 25, 1),
(67, 50, 1),
(68, 61, 1),
(69, 35, 1),
(70, 51, 1),
(71, 44, 1),
(72, 40, 1),
(73, 31, 1),
(74, 75, 1),
(75, 35, 1),
(76, 52, 1),
(77, 27, 1),
(78, 31, 1),
(79, 52, 1),
(80, 35, 1),
(81, 45, 1),
(82, 71, 1),
(83, 31, 1),
(84, 31, 1),
(85, 65, 1),
(86, 31, 1),
(87, 6, 1),
(88, 52, 1),
(89, 46, 1),
(90, 84, 1),
(91, 19, 1),
(92, 78, 1),
(93, 79, 1),
(94, 80, 1),
(95, 56, 1),
(96, 57, 1),
(97, 58, 1),
(98, 46, 1),
(99, 46, 1),
(100, 46, 1),
(101, 60, 1),
(102, 60, 1),
(103, 60, 1),
(104, 40, 1),
(105, 40, 1),
(106, 40, 1),
(107, 42, 1),
(108, 33, 1),
(109, 68, 1),
(110, 20, 1),
(111, 74, 1),
(112, 43, 1),
(113, 3, 1),
(114, 55, 1),
(115, 35, 1),
(116, 41, 1),
(117, 81, 1),
(118, 72, 1),
(119, 85, 1),
(120, 66, 1),
(121, 35, 1),
(122, 16, 1),
(123, 16, 1),
(124, 16, 1),
(125, 29, 1),
(126, 49, 1),
(127, 49, 1),
(128, 76, 1),
(129, 53, 1),
(130, 34, 1),
(131, 76, 1),
(132, 76, 1),
(133, 73, 1),
(134, 54, 1),
(135, 77, 1),
(136, 1, 1),
(137, 10, 1),
(138, 69, 1),
(139, 37, 1),
(140, 15, 1),
(141, 35, 1),
(142, 63, 1),
(143, 81, 1),
(144, 4, 1),
(145, 70, 1),
(146, 63, 1),
(147, 81, 1),
(148, 4, 1),
(149, 26, 1),
(150, 37, 1),
(151, 35, 1),
(152, 35, 1),
(153, 35, 1),
(154, 82, 1),
(155, 4, 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_03`
--

CREATE TABLE `t_02_03_03` (
  `f_00` bigint(20) NOT NULL,
  `f_11` bigint(20) DEFAULT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_03`
--

INSERT INTO `t_02_03_03` (`f_00`, `f_11`, `f_10`) VALUES
(35, 1, 7),
(36, 1, 7),
(37, 1, 7),
(38, 1, 7),
(39, 1, 7),
(40, 1, 7),
(41, 1, 7),
(42, 1, 7),
(43, 1, 7),
(44, 1, 7),
(45, 1, 7),
(46, 1, 7),
(47, 1, 7),
(48, 1, 9),
(49, 2, 7),
(50, 2, 7),
(51, 2, 7),
(52, 2, 7),
(53, 2, 7),
(54, 2, 7),
(55, 3, 7),
(56, 3, 7),
(57, 3, 7),
(58, 3, 7),
(59, 3, 7),
(60, 4, 7),
(61, 4, 7),
(62, 5, 7),
(63, 5, 7),
(64, 5, 7),
(65, 5, 7),
(66, 6, 7),
(67, 7, 7),
(68, 7, 7),
(69, 8, 7),
(70, 8, 7),
(71, 8, 7),
(72, 8, 9),
(73, 9, 7),
(74, 9, 7),
(75, 11, 7),
(76, 11, 7),
(77, 11, 7),
(78, 11, 7),
(79, 11, 7),
(80, 11, 7),
(81, 11, 9),
(82, 12, 7),
(83, 12, 7),
(84, 12, 7),
(85, 12, 7),
(86, 12, 7),
(87, 12, 7),
(88, 13, 8),
(89, 13, 8),
(90, 13, 8),
(91, 13, 8),
(92, 14, 8),
(93, 14, 8),
(94, 14, 8),
(95, 14, 8),
(96, 14, 8),
(97, 14, 8),
(98, 14, 8),
(99, 14, 8),
(100, 14, 8),
(101, 14, 8),
(102, 14, 8),
(103, 14, 8),
(104, 14, 8),
(105, 14, 8),
(106, 14, 8),
(107, 16, 8),
(108, 17, 8),
(109, 17, 8),
(110, 17, 8),
(111, 17, 8),
(112, 17, 8),
(113, 19, 8),
(114, 20, 8),
(115, 22, 8),
(116, 22, 8),
(117, 21, 8),
(118, 21, 8),
(119, 21, 8),
(120, 21, 8),
(121, 21, 8);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_04`
--

CREATE TABLE `t_02_03_04` (
  `f_00` bigint(20) NOT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_04`
--

INSERT INTO `t_02_03_04` (`f_00`, `f_10`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 2),
(5, 2),
(6, 2),
(7, 3),
(8, 3),
(9, 3),
(10, 3),
(11, 3),
(12, 4),
(13, 4),
(14, 4),
(15, 4),
(16, 4),
(17, 4),
(18, 4),
(19, 5),
(20, 5),
(21, 5),
(22, 5),
(23, 5),
(24, 5),
(25, 5),
(26, 5),
(27, 5),
(28, 5),
(29, 5),
(30, 5),
(31, 5),
(32, 5),
(33, 5),
(34, 5);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_05`
--

CREATE TABLE `t_02_03_05` (
  `f_10` double DEFAULT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_05`
--

INSERT INTO `t_02_03_05` (`f_10`, `f_00`, `f_11`) VALUES
(1, 122, 21),
(1, 123, 21),
(1, 124, 21),
(1, 125, 21),
(1, 126, 22),
(1, 127, 22),
(1, 128, 23),
(1, 129, 23),
(1, 130, 23),
(1, 131, 23),
(1, 132, 23),
(1, 133, 23),
(1, 134, 24),
(1, 135, 24),
(1, 136, 25),
(1, 137, 25),
(1, 138, 26),
(1, 139, 26),
(1, 140, 26),
(1, 141, 29),
(1, 142, 29),
(1, 143, 29),
(1, 144, 29),
(1, 145, 29),
(1, 146, 32),
(1, 147, 32),
(1, 148, 32),
(1, 149, 32),
(1, 150, 32),
(1, 151, 34),
(1, 152, 34),
(1, 153, 34),
(1, 154, 34),
(1, 155, 34);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_06`
--

CREATE TABLE `t_02_03_06` (
  `f_10` double DEFAULT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_12` bigint(20) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_07`
--

CREATE TABLE `t_02_03_07` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_07`
--

INSERT INTO `t_02_03_07` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`, `f_05`) VALUES
(1, 'OZ1/OZ2', 'STC OZ1/OZ2', 1, 2, 11),
(2, 'OK1', 'STC OK1', 1, 2, 10),
(3, 'OB1/OG1', 'STC OB1/OG1', 1, 2, 10),
(4, 'OT1', 'STC OT1', 1, 2, 10),
(5, 'OH1', 'STC OH1', 1, 2, 10),
(6, 'OD1/OD3/OH2', 'STC OD1/OD3/OH2', 1, 2, 10),
(7, 'OH3/OH4', 'STC OH3/OH4', 1, 2, 10),
(8, 'NZ1', 'STC NZ1', 1, 3, 10),
(9, 'NK1', 'STC NK1', 1, 3, 10),
(10, 'NH2', 'STC NH2', 1, 3, 10),
(11, 'LZ1/LZ2', 'STC LZ1/LZ2', 1, 4, 10),
(12, 'LR1/DLR1/ELR1', 'STC LR1/DLR1/ELR1', 1, 4, 10),
(13, 'GZ0', 'STC GZ0', 1, 1, 10),
(14, 'GZ1/GZ2/GZ3', 'STC GZ1/GZ2/GZ3', 1, 1, 10),
(15, 'GZ4', 'STC GZ4', 1, 1, 10),
(16, 'GPDF', 'STC GPDF', 1, 1, 10),
(17, 'GK1/GK2', 'STC GK1/GK2', 1, 1, 10),
(18, 'GK3', 'STC GK3', 1, 1, 10),
(19, 'GO1/GO2/GO3', 'STC GO1/GO2/GO3', 1, 1, 10),
(20, 'GG1', 'STC GG1', 1, 1, 10),
(21, 'GR1/GR2/GR4/GR6', 'STC GR1/GR2/GR4/GR6', 1, 1, 10),
(22, 'GR5/GR7', 'STC GR5/GR7', 1, 1, 10);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_08`
--

CREATE TABLE `t_02_03_08` (
  `f_16` double NOT NULL,
  `f_12` double NOT NULL,
  `f_14` double NOT NULL,
  `f_09` double NOT NULL,
  `f_08` varchar(255) NOT NULL,
  `f_11` varchar(255) NOT NULL,
  `f_10` varchar(255) NOT NULL,
  `f_17` double NOT NULL,
  `f_13` double NOT NULL,
  `f_15` double NOT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_23` bigint(20) NOT NULL,
  `f_22` bigint(20) NOT NULL,
  `f_24` bigint(20) NOT NULL,
  `f_18` bigint(20) DEFAULT NULL,
  `f_19` bigint(20) DEFAULT NULL,
  `f_20` bigint(20) DEFAULT NULL,
  `f_21` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_02_03_08`
--

INSERT INTO `t_02_03_08` (`f_16`, `f_12`, `f_14`, `f_09`, `f_08`, `f_11`, `f_10`, `f_17`, `f_13`, `f_15`, `f_00`, `f_23`, `f_22`, `f_24`, `f_18`, `f_19`, `f_20`, `f_21`) VALUES
(21, 63, 0, 801.478, '28', '0.0457', '6.35 / 7.14 / 7.92 / 8.74 / 9.52 /  11.91 / 12.70', 11, 54, 0, 156, 21, 7, 12, 3, 14, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_09`
--

CREATE TABLE `t_02_03_09` (
  `f_08` double NOT NULL,
  `f_13` double NOT NULL,
  `f_09` double NOT NULL,
  `f_11` double NOT NULL,
  `f_12` double NOT NULL,
  `f_10` double NOT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_14` bigint(20) DEFAULT NULL,
  `f_15` bigint(20) DEFAULT NULL,
  `f_16` bigint(20) DEFAULT NULL,
  `f_17` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_02_03_10`
--

CREATE TABLE `t_02_03_10` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_07` date DEFAULT NULL,
  `f_09` date DEFAULT NULL,
  `f_06` date DEFAULT NULL,
  `f_08` date DEFAULT NULL,
  `f_05` date DEFAULT NULL,
  `f_03` varchar(50) DEFAULT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_04` varchar(100) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL,
  `f_12` bigint(20) NOT NULL,
  `f_13` bigint(20) NOT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_01_01`
--

CREATE TABLE `t_03_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_01_01`
--

INSERT INTO `t_03_01_01` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`) VALUES
(1, 'PRODUCTION', 'إنتاج', 'Production', 'Production'),
(2, 'TRANSPORT', 'نقل', 'Transport', 'Transport'),
(3, 'EXPORT', 'تصدير', 'Exportation', 'Export'),
(4, 'IMPORT', 'استيراد', 'Importation', 'Import'),
(5, 'STORAGE_IN', 'تخزين - دخول', 'Stockage - Entrée', 'Storage In'),
(6, 'STORAGE_OUT', 'تخزين - خروج', 'Stockage - Sortie', 'Storage Out'),
(7, 'CONSUMPTION', 'استهلاك', 'Consommation', 'Consumption'),
(8, 'INJECTION', 'حقن', 'Injection', 'Injection'),
(9, 'FLARING', 'حرق', 'Torchage', 'Flaring'),
(10, 'TRANSFER', 'نقل داخلي', 'Transfert', 'Transfer');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_01_02`
--

CREATE TABLE `t_03_01_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_01_02`
--

INSERT INTO `t_03_01_02` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`) VALUES
(1, 'EMERGENCY_SHUTDOWN', 'إيقاف طارئ', 'Emergency Shutdown', 'Arrêt d\'Urgence'),
(2, 'PLANNED_MAINT', 'صيانة مخططة', 'Planned Maintenance', 'Maintenance Planifiée'),
(3, 'CORRECTIVE_MAINT', 'صيانة تصحيحية', 'Corrective Maintenance', 'Maintenance Corrective'),
(4, 'EQUIPMENT_FAILURE', 'عطل في المعدات', 'Equipment Failure', 'Panne d\'Équipement'),
(5, 'SAFETY_INCIDENT', 'حادث أمان', 'Safety Incident', 'Incident de Sécurité'),
(6, 'INSPECTION', 'تفتيش', 'Inspection', 'Inspection'),
(7, 'STARTUP', 'بدء التشغيل', 'Startup', 'Démarrage'),
(8, 'NORMAL_SHUTDOWN', 'إيقاف عادي', 'Normal Shutdown', 'Arrêt Normal'),
(9, 'PROCESS_UPSET', 'اضطراب العملية', 'Process Upset', 'Perturbation du Processus'),
(10, 'LEAK_DETECTION', 'كشف التسرب', 'Leak Detection', 'Détection de Fuite');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_01`
--

CREATE TABLE `t_03_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_01`
--

INSERT INTO `t_03_02_01` (`f_00`, `f_01`, `f_02`, `f_03`) VALUES
(1, 'مجدول', 'Scheduled', 'Planifié'),
(2, 'جاري', 'Ongoing', 'En Cours'),
(3, 'مكتمل', 'Completed', 'Terminé'),
(4, 'ملغى', 'Cancelled', 'Annulé'),
(5, 'مؤجل', 'Postponed', 'Reporté'),
(6, 'قيد التحقيق', 'Under Investigation', 'En Investigation');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_02`
--

CREATE TABLE `t_03_02_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_02`
--

INSERT INTO `t_03_02_02` (`f_00`, `f_01`, `f_02`, `f_03`) VALUES
(1, 'مفتوح', 'Open', 'Ouvert'),
(2, 'تم الإقرار', 'Acknowledged', 'Reconnu'),
(3, 'قيد التنفيذ', 'In Progress', 'En Cours'),
(4, 'تم الحل', 'Resolved', 'Résolu'),
(5, 'مغلق', 'Closed', 'Fermé'),
(6, 'تم التصعيد', 'Escalated', 'Escaladé'),
(7, 'إنذار كاذب', 'False Alarm', 'Fausse Alerte');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_03`
--

CREATE TABLE `t_03_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_03`
--

INSERT INTO `t_03_02_03` (`f_00`, `f_01`, `f_02`, `f_03`) VALUES
(1, 'معلومات', 'Info', 'Information'),
(2, 'منخفض', 'Low', 'Faible'),
(3, 'متوسط', 'Medium', 'Moyen'),
(4, 'عالي', 'High', 'Élevé'),
(5, 'حرج', 'Critical', 'Critique'),
(6, 'طارئ', 'Emergency', 'Urgence');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_04`
--

CREATE TABLE `t_03_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_05` varchar(255) DEFAULT NULL,
  `f_06` varchar(255) DEFAULT NULL,
  `f_07` varchar(255) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_04`
--

INSERT INTO `t_03_02_04` (`f_00`, `f_01`, `f_05`, `f_06`, `f_07`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'SCADA', 'قراءات تلقائية من نظام المراقبة والتحكم', 'Automated readings from Supervisory Control and Data Acquisition system', 'Lectures automatisées du système de contrôle et d\'acquisition de données', 'نظام SCADA', 'SCADA System', 'Système SCADA'),
(2, 'MANUAL', 'قراءات يدوية من قبل المشغلين في الميدان', 'Manual readings taken by field operators', 'Lectures manuelles effectuées par les opérateurs sur le terrain', 'قراءة يدوية', 'Manual Reading', 'Lecture Manuelle'),
(3, 'CALCULATED', 'قيم محسوبة من بيانات أخرى', 'Values computed from other data sources', 'Valeurs calculées à partir d\'autres sources de données', 'قيمة محسوبة', 'Calculated Value', 'Valeur Calculée'),
(4, 'EXTERNAL', 'بيانات من أنظمة خارجية أو شركاء', 'Data from external systems or partners', 'Données provenant de systèmes externes ou de partenaires', 'مصدر خارجي', 'External Source', 'Source Externe'),
(5, 'FLOWMETER', 'قراءات مباشرة من أجهزة قياس التدفق', 'Direct readings from flow measurement devices', 'Lectures directes des dispositifs de mesure de débit', 'عداد التدفق', 'Flow Meter', 'Débitmètre'),
(6, 'ESTIMATED', 'قيم تقديرية بناء على بيانات تاريخية', 'Estimated values based on historical data', 'Valeurs estimées basées sur des données historiques', 'تقديري', 'Estimated', 'Estimé');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_05`
--

CREATE TABLE `t_03_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_05` varchar(255) DEFAULT NULL,
  `f_06` varchar(255) DEFAULT NULL,
  `f_07` varchar(255) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL,
  `f_08` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_05`
--

INSERT INTO `t_03_02_05` (`f_00`, `f_01`, `f_05`, `f_06`, `f_07`, `f_02`, `f_03`, `f_04`, `f_08`) VALUES
(1, 'GOOD', 'البيانات ذات جودة عالية وموثوقة', 'Data is of high quality and reliable', 'Les données sont de haute qualité et fiables', 'جيد', 'Good Quality', 'Bonne Qualité', 'LOW'),
(2, 'SENSOR_ERROR', 'الحساس يظهر قراءات غير صحيحة', 'Sensor showing incorrect readings', 'Le capteur affiche des lectures incorrectes', 'خطأ في الحساس', 'Sensor Error', 'Erreur de Capteur', 'HIGH'),
(3, 'CALIBRATION_NEEDED', 'الجهاز يحتاج إلى معايرة', 'Device requires calibration', 'L\'appareil nécessite un étalonnage', 'يحتاج معايرة', 'Calibration Needed', 'Étalonnage Requis', 'MEDIUM'),
(4, 'OUT_OF_RANGE', 'القراءة خارج النطاق المتوقع', 'Reading outside expected range', 'Lecture en dehors de la plage attendue', 'خارج النطاق', 'Out of Range', 'Hors Plage', 'HIGH'),
(5, 'SUSPICIOUS', 'القراءة تبدو غير طبيعية', 'Reading appears abnormal', 'La lecture semble anormale', 'قراءة مشبوهة', 'Suspicious Reading', 'Lecture Suspecte', 'MEDIUM'),
(6, 'COMM_FAILURE', 'فقدان الاتصال مع الجهاز', 'Loss of communication with device', 'Perte de communication avec l\'appareil', 'فشل الاتصال', 'Communication Failure', 'Échec de Communication', 'CRITICAL'),
(7, 'DRIFT', 'انحراف في قيم القياس بمرور الوقت', 'Measurement values drifting over time', 'Valeurs de mesure dérivant dans le temps', 'انحراف مكتشف', 'Drift Detected', 'Dérive Détectée', 'MEDIUM');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_06`
--

CREATE TABLE `t_03_02_06` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_05` varchar(255) DEFAULT NULL,
  `f_06` varchar(255) DEFAULT NULL,
  `f_07` varchar(255) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_06`
--

INSERT INTO `t_03_02_06` (`f_00`, `f_01`, `f_05`, `f_06`, `f_07`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'NOT_RECORDED', 'لم يتم تسجيل أي قراءة لهذا الأنبوب والفترة الزمنية', 'No reading has been recorded for this pipeline and time slot', 'Aucune lecture n\'a été enregistrée pour ce pipeline et ce créneau horaire', 'غير مسجل', 'Not Recorded', 'Non Enregistré'),
(2, 'DRAFT', 'قراءة قيد الإدخال ولم يتم تقديمها بعد للمصادقة', 'Reading in progress, not yet submitted for validation', 'Lecture en cours de saisie, non encore soumise pour validation', 'مسودة', 'Draft', 'Brouillon'),
(3, 'SUBMITTED', 'قراءة مقدمة من قبل المشغل وتنتظر المصادقة من المشرف', 'Reading submitted by operator and awaiting supervisor validation', 'Lecture soumise par l\'opérateur et en attente de validation du superviseur', 'مقدم للمصادقة', 'Submitted', 'Soumis pour Validation'),
(4, 'APPROVED', 'قراءة تم التحقق منها والمصادقة عليها من قبل المشرف', 'Reading verified and approved by supervisor', 'Lecture vérifiée et approuvée par le superviseur', 'مصادق عليه', 'Approved', 'Approuvé'),
(5, 'REJECTED', 'قراءة مرفوضة من قبل المشرف وتتطلب إعادة الإدخال', 'Reading rejected by supervisor and requires re-entry', 'Lecture rejetée par le superviseur et nécessitant une nouvelle saisie', 'مرفوض', 'Rejected', 'Rejeté');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_02_07`
--

CREATE TABLE `t_03_02_07` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_04` varchar(100) DEFAULT NULL,
  `f_05` varchar(100) NOT NULL,
  `f_06` varchar(100) DEFAULT NULL,
  `f_07` int(11) DEFAULT NULL,
  `f_03` time NOT NULL,
  `f_02` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_02_07`
--

INSERT INTO `t_03_02_07` (`f_00`, `f_01`, `f_04`, `f_05`, `f_06`, `f_07`, `f_03`, `f_02`) VALUES
(1, 'SLOT_01', 'الصباح المبكر', 'Morning Early', 'Matinée tôt', NULL, '10:00:00', '08:00:00'),
(2, 'SLOT_02', 'الصباح المتأخر', 'Late Morning', 'Fin de matinée', NULL, '12:00:00', '10:00:00'),
(3, 'SLOT_03', 'الظهيرة', 'Midday', 'Midi', NULL, '14:00:00', '12:00:00'),
(4, 'SLOT_04', 'بعد الظهر', 'Early Afternoon', 'Début d\'après-midi', NULL, '16:00:00', '14:00:00'),
(5, 'SLOT_05', 'بعد الظهر المتأخر', 'Late Afternoon', 'Fin d\'après-midi', NULL, '18:00:00', '16:00:00'),
(6, 'SLOT_06', 'المساء', 'Evening', 'Soirée', NULL, '20:00:00', '18:00:00'),
(7, 'SLOT_07', 'المساء المتأخر', 'Late Evening', 'Fin de soirée', NULL, '22:00:00', '20:00:00'),
(8, 'SLOT_08', 'الليل', 'Night', 'Nuit', NULL, '00:00:00', '22:00:00'),
(9, 'SLOT_09', 'منتصف الليل', 'Midnight', 'Minuit', NULL, '02:00:00', '00:00:00'),
(10, 'SLOT_10', 'الليل المتأخر', 'Late Night', 'Fin de nuit', NULL, '04:00:00', '02:00:00'),
(11, 'SLOT_11', 'الفجر المبكر', 'Early Dawn', 'Aube', NULL, '06:00:00', '04:00:00'),
(12, 'SLOT_12', 'الفجر', 'Dawn', 'Petit matin', NULL, '08:00:00', '06:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_01`
--

CREATE TABLE `t_03_03_01` (
  `f_00` bigint(20) NOT NULL,
  `f_05` varchar(500) DEFAULT NULL,
  `f_01` date NOT NULL,
  `f_03` datetime(6) DEFAULT NULL,
  `f_04` datetime(6) DEFAULT NULL,
  `f_02` decimal(15,2) NOT NULL,
  `f_06` bigint(20) NOT NULL,
  `f_07` bigint(20) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_08` bigint(20) NOT NULL,
  `f_10` bigint(20) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_02`
--

CREATE TABLE `t_03_03_02` (
  `f_00` bigint(20) NOT NULL,
  `f_05` decimal(7,4) DEFAULT NULL,
  `f_04` decimal(15,2) DEFAULT NULL,
  `f_03` decimal(15,2) DEFAULT NULL,
  `f_06` varchar(500) DEFAULT NULL,
  `f_01` date NOT NULL,
  `f_02` decimal(15,2) NOT NULL,
  `f_07` bigint(20) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_08` bigint(20) NOT NULL,
  `f_10` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_03`
--

CREATE TABLE `t_03_03_03` (
  `f_00` bigint(20) NOT NULL,
  `f_05` decimal(38,2) DEFAULT NULL,
  `f_04` decimal(38,2) DEFAULT NULL,
  `f_08` varchar(500) DEFAULT NULL,
  `f_02` decimal(38,2) DEFAULT NULL,
  `f_01` date NOT NULL,
  `f_06` datetime(6) NOT NULL,
  `f_03` decimal(38,2) DEFAULT NULL,
  `f_07` datetime(6) DEFAULT NULL,
  `f_12` bigint(20) NOT NULL,
  `f_13` bigint(20) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_10` bigint(20) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_04`
--

CREATE TABLE `t_03_03_04` (
  `f_00` bigint(20) NOT NULL,
  `f_13` varchar(2000) DEFAULT NULL,
  `f_03` varchar(2000) DEFAULT NULL,
  `f_11` datetime(6) DEFAULT NULL,
  `f_01` datetime(6) NOT NULL,
  `f_14` bit(1) NOT NULL,
  `f_10` datetime(6) DEFAULT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_06` bigint(20) NOT NULL,
  `f_09` bigint(20) DEFAULT NULL,
  `f_08` bigint(20) DEFAULT NULL,
  `f_07` bigint(20) NOT NULL,
  `f_04` bigint(20) DEFAULT NULL,
  `f_12` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_05`
--

CREATE TABLE `t_03_03_05` (
  `f_00` bigint(20) NOT NULL,
  `f_05` datetime(6) DEFAULT NULL,
  `f_02` decimal(38,2) NOT NULL,
  `f_01` datetime(6) NOT NULL,
  `f_04` varchar(1000) DEFAULT NULL,
  `f_08` bit(1) NOT NULL,
  `f_09` datetime(6) DEFAULT NULL,
  `f_07` varchar(1000) DEFAULT NULL,
  `f_06` datetime(6) DEFAULT NULL,
  `f_03` decimal(38,2) DEFAULT NULL,
  `f_14` bigint(20) DEFAULT NULL,
  `f_12` bigint(20) DEFAULT NULL,
  `f_10` bigint(20) DEFAULT NULL,
  `f_13` bigint(20) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `t_03_03_06`
--

CREATE TABLE `t_03_03_06` (
  `f_00` bigint(20) NOT NULL,
  `f_10` bit(1) NOT NULL,
  `f_09` decimal(5,2) NOT NULL,
  `f_08` decimal(12,2) NOT NULL,
  `f_07` decimal(12,2) NOT NULL,
  `f_06` decimal(12,2) NOT NULL,
  `f_05` decimal(12,2) NOT NULL,
  `f_02` decimal(12,2) NOT NULL,
  `f_01` decimal(12,2) NOT NULL,
  `f_04` decimal(12,2) NOT NULL,
  `f_03` decimal(12,2) NOT NULL,
  `f_11` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `t_03_03_06`
--

INSERT INTO `t_03_03_06` (`f_00`, `f_10`, `f_09`, `f_08`, `f_07`, `f_06`, `f_05`, `f_02`, `f_01`, `f_04`, `f_03`, `f_11`) VALUES
(1, b'1', 5.00, 130000.00, 20000.00, 1500.00, 1254.00, 70.00, 50.00, 120.00, 65.00, 156);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `r_t000201_t000203`
--
ALTER TABLE `r_t000201_t000203`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000201_T000203_FK_02` (`f_02`);

--
-- Indexes for table `r_t000202_t000201`
--
ALTER TABLE `r_t000202_t000201`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000202_T000201_FK_02` (`f_02`);

--
-- Indexes for table `r_t000202_t000203`
--
ALTER TABLE `r_t000202_t000203`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000202_T000203_FK_02` (`f_02`);

--
-- Indexes for table `r_t000203_t000204`
--
ALTER TABLE `r_t000203_t000204`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000203_T000204_FK_02` (`f_02`);

--
-- Indexes for table `r_t020303_t020308`
--
ALTER TABLE `r_t020303_t020308`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020303_T020308_FK_02` (`f_02`);

--
-- Indexes for table `r_t020304_t020302`
--
ALTER TABLE `r_t020304_t020302`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020304_T020302_FK_02` (`f_02`);

--
-- Indexes for table `r_t020304_t020308`
--
ALTER TABLE `r_t020304_t020308`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020304_T020308_FK_02` (`f_02`);

--
-- Indexes for table `r_t020305_t020201`
--
ALTER TABLE `r_t020305_t020201`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020201_FK_02` (`f_02`);

--
-- Indexes for table `r_t020305_t020204`
--
ALTER TABLE `r_t020305_t020204`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020204_FK_02` (`f_02`);

--
-- Indexes for table `r_t020305_t020308`
--
ALTER TABLE `r_t020305_t020308`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020308_FK_02` (`f_02`);

--
-- Indexes for table `r_t020306_t020201`
--
ALTER TABLE `r_t020306_t020201`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020306_T020201_FK_02` (`f_02`);

--
-- Indexes for table `r_t020306_t020204`
--
ALTER TABLE `r_t020306_t020204`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020306_T020204_FK_02` (`f_02`);

--
-- Indexes for table `r_t020308_t010206`
--
ALTER TABLE `r_t020308_t010206`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020308_T010206_FK_02` (`f_02`);

--
-- Indexes for table `r_t020308_t020205`
--
ALTER TABLE `r_t020308_t020205`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020308_T020205_FK_02` (`f_02`);

--
-- Indexes for table `t_00_00_01`
--
ALTER TABLE `t_00_00_01`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_00_01_01`
--
ALTER TABLE `t_00_01_01`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_00_02_01`
--
ALTER TABLE `t_00_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_01_UK_01` (`f_01`);

--
-- Indexes for table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_00_02_02_UK_02` (`f_02`),
  ADD KEY `T_00_02_02_FK_01` (`f_08`);

--
-- Indexes for table `t_00_02_03`
--
ALTER TABLE `t_00_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_03_UK_01` (`f_01`);

--
-- Indexes for table `t_00_02_04`
--
ALTER TABLE `t_00_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_04_UK_01` (`f_01`);

--
-- Indexes for table `t_00_02_05`
--
ALTER TABLE `t_00_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_05_UK_01` (`f_01`);

--
-- Indexes for table `t_00_03_01`
--
ALTER TABLE `t_00_03_01`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_04_01_UK_01` (`f_03`);

--
-- Indexes for table `t_00_05_01`
--
ALTER TABLE `t_00_05_01`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `IDX_T000501_F04` (`f_04`),
  ADD KEY `IDX_T000501_F07` (`f_07`),
  ADD KEY `IDX_T000501_F09` (`f_09`),
  ADD KEY `T_00_05_01_FK_01` (`f_08`);

--
-- Indexes for table `t_00_05_02`
--
ALTER TABLE `t_00_05_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_05_02_UK_01` (`f_01`);

--
-- Indexes for table `t_01_01_01`
--
ALTER TABLE `t_01_01_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_01_01_UK_01` (`f_03`);

--
-- Indexes for table `t_01_02_01`
--
ALTER TABLE `t_01_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_02_01_UK_02` (`f_04`);

--
-- Indexes for table `t_01_02_02`
--
ALTER TABLE `t_01_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_02_UK_01` (`f_04`);

--
-- Indexes for table `t_01_02_03`
--
ALTER TABLE `t_01_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_03_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_02_03_UK_02` (`f_04`);

--
-- Indexes for table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_04_UK_01` (`f_01`),
  ADD KEY `T_01_02_04_FK_01` (`f_05`);

--
-- Indexes for table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_05_UK_01` (`f_01`),
  ADD KEY `T_01_02_05_FK_01` (`f_05`);

--
-- Indexes for table `t_01_02_06`
--
ALTER TABLE `t_01_02_06`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_02_06_FK_01` (`f_07`);

--
-- Indexes for table `t_01_02_07`
--
ALTER TABLE `t_01_02_07`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_02_07_FK_01` (`f_05`);

--
-- Indexes for table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_03_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_03_01_UK_02` (`f_04`),
  ADD KEY `T_01_03_01_FK_02` (`f_06`),
  ADD KEY `T_01_03_01_FK_01` (`f_05`);

--
-- Indexes for table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_03_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_03_02_UK_02` (`f_04`),
  ADD KEY `T_01_03_02_FK_01` (`f_05`);

--
-- Indexes for table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_03_03_FK_02` (`f_11`),
  ADD KEY `T_01_03_03_FK_01` (`f_10`),
  ADD KEY `T_01_03_03_FK_03` (`f_12`),
  ADD KEY `T_01_03_03_FK_04` (`f_13`);

--
-- Indexes for table `t_01_03_04`
--
ALTER TABLE `t_01_03_04`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_03_04_FK_01` (`f_15`);

--
-- Indexes for table `t_02_01_01`
--
ALTER TABLE `t_02_01_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_01_UK_01` (`f_03`);

--
-- Indexes for table `t_02_01_02`
--
ALTER TABLE `t_02_01_02`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_03`
--
ALTER TABLE `t_02_01_03`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_04`
--
ALTER TABLE `t_02_01_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_04_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_01_04_UK_02` (`f_04`);

--
-- Indexes for table `t_02_01_05`
--
ALTER TABLE `t_02_01_05`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_06`
--
ALTER TABLE `t_02_01_06`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_07`
--
ALTER TABLE `t_02_01_07`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_08`
--
ALTER TABLE `t_02_01_08`
  ADD PRIMARY KEY (`f_00`);

--
-- Indexes for table `t_02_01_09`
--
ALTER TABLE `t_02_01_09`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_09_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_01_09_UK_02` (`f_04`);

--
-- Indexes for table `t_02_02_01`
--
ALTER TABLE `t_02_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_02_01_UK_02` (`f_04`);

--
-- Indexes for table `t_02_02_02`
--
ALTER TABLE `t_02_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_02_02_UK_02` (`f_04`);

--
-- Indexes for table `t_02_02_03`
--
ALTER TABLE `t_02_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_03_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_02_03_UK_02` (`f_04`);

--
-- Indexes for table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_04_UK_01` (`f_02`),
  ADD KEY `T_02_02_04_FK_02` (`f_04`),
  ADD KEY `T_02_02_04_FK_01` (`f_03`);

--
-- Indexes for table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_05_UK_01` (`f_02`),
  ADD KEY `T_02_02_05_FK_02` (`f_04`),
  ADD KEY `T_02_02_05_FK_01` (`f_03`);

--
-- Indexes for table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_01_UK_01` (`f_01`),
  ADD KEY `T_02_03_01_FK_01` (`f_06`),
  ADD KEY `T_02_03_01_FK_02` (`f_07`);

--
-- Indexes for table `t_02_03_02`
--
ALTER TABLE `t_02_03_02`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_02_FK_02` (`f_09`),
  ADD KEY `T_02_03_02_FK_01` (`f_08`);

--
-- Indexes for table `t_02_03_03`
--
ALTER TABLE `t_02_03_03`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_03_FK_02` (`f_11`),
  ADD KEY `T_02_03_03_FK_01` (`f_10`);

--
-- Indexes for table `t_02_03_04`
--
ALTER TABLE `t_02_03_04`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_04_FK_01` (`f_10`);

--
-- Indexes for table `t_02_03_05`
--
ALTER TABLE `t_02_03_05`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_05_FK_01` (`f_11`);

--
-- Indexes for table `t_02_03_06`
--
ALTER TABLE `t_02_03_06`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_06_FK_02` (`f_12`),
  ADD KEY `T_02_03_06_FK_01` (`f_11`);

--
-- Indexes for table `t_02_03_07`
--
ALTER TABLE `t_02_03_07`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_07_UK_01` (`f_01`),
  ADD KEY `T_02_03_07_FK_02` (`f_04`),
  ADD KEY `T_02_03_07_FK_01` (`f_03`),
  ADD KEY `T_02_03_07_FK_03` (`f_05`);

--
-- Indexes for table `t_02_03_08`
--
ALTER TABLE `t_02_03_08`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_08_FK_06` (`f_23`),
  ADD KEY `T_02_03_08_FK_05` (`f_22`),
  ADD KEY `T_02_03_01_FK_07` (`f_24`),
  ADD KEY `T_02_03_08_FK_01` (`f_18`),
  ADD KEY `T_02_03_08_FK_02` (`f_19`),
  ADD KEY `T_02_03_08_FK_03` (`f_20`),
  ADD KEY `T_02_03_08_FK_04` (`f_21`);

--
-- Indexes for table `t_02_03_09`
--
ALTER TABLE `t_02_03_09`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_09_FK_01` (`f_14`),
  ADD KEY `T_02_03_09_FK_02` (`f_15`),
  ADD KEY `T_02_03_09_FK_03` (`f_16`),
  ADD KEY `T_02_03_09_FK_04` (`f_17`);

--
-- Indexes for table `t_02_03_10`
--
ALTER TABLE `t_02_03_10`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_10_UK_01` (`f_02`),
  ADD KEY `T_02_03_10_FK_02` (`f_11`),
  ADD KEY `T_02_03_10_FK_03` (`f_12`),
  ADD KEY `T_02_03_10_FK_04` (`f_13`),
  ADD KEY `T_02_03_10_FK_01` (`f_10`);

--
-- Indexes for table `t_03_01_01`
--
ALTER TABLE `t_03_01_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_01_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_03_01_01_UK_02` (`f_03`);

--
-- Indexes for table `t_03_01_02`
--
ALTER TABLE `t_03_01_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_01_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_03_01_02_UK_02` (`f_03`);

--
-- Indexes for table `t_03_02_01`
--
ALTER TABLE `t_03_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_01_UK_01` (`f_03`);

--
-- Indexes for table `t_03_02_02`
--
ALTER TABLE `t_03_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_02_UK_01` (`f_03`);

--
-- Indexes for table `t_03_02_03`
--
ALTER TABLE `t_03_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_03_UK_01` (`f_03`);

--
-- Indexes for table `t_03_02_04`
--
ALTER TABLE `t_03_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_04_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_03_02_04_UK_02` (`f_04`);

--
-- Indexes for table `t_03_02_05`
--
ALTER TABLE `t_03_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_05_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_03_02_05_UK_02` (`f_04`);

--
-- Indexes for table `t_03_02_06`
--
ALTER TABLE `t_03_02_06`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_06_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_03_02_06_UK_02` (`f_04`);

--
-- Indexes for table `t_03_02_07`
--
ALTER TABLE `t_03_02_07`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_02_07_UK_01` (`f_01`,`f_02`);

--
-- Indexes for table `t_03_03_01`
--
ALTER TABLE `t_03_03_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_03_01_UK_01` (`f_01`,`f_06`,`f_08`,`f_09`),
  ADD KEY `T_03_03_01_IX_01` (`f_01`),
  ADD KEY `T_03_03_01_IX_02` (`f_06`),
  ADD KEY `T_03_03_01_IX_03` (`f_08`),
  ADD KEY `T_03_03_01_FK_02` (`f_07`),
  ADD KEY `T_03_03_01_FK_04` (`f_09`),
  ADD KEY `T_03_03_01_FK_05` (`f_10`),
  ADD KEY `T_03_03_01_FK_06` (`f_11`);

--
-- Indexes for table `t_03_03_02`
--
ALTER TABLE `t_03_03_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_03_02_UK_01` (`f_01`,`f_07`,`f_08`,`f_09`),
  ADD KEY `T_03_03_02_IX_01` (`f_01`),
  ADD KEY `T_03_03_02_IX_02` (`f_07`),
  ADD KEY `T_03_03_02_FK_03` (`f_09`),
  ADD KEY `T_03_03_02_FK_02` (`f_08`),
  ADD KEY `T_03_03_02_FK_04` (`f_10`);

--
-- Indexes for table `t_03_03_03`
--
ALTER TABLE `t_03_03_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_03_03_UK_01` (`f_01`,`f_12`,`f_13`),
  ADD KEY `T_03_03_03_IX_01` (`f_01`),
  ADD KEY `T_03_03_03_IX_02` (`f_01`,`f_13`),
  ADD KEY `T_03_03_03_FK_04` (`f_12`),
  ADD KEY `T_03_03_03_FK_05` (`f_13`),
  ADD KEY `T_03_03_03_FK_01` (`f_09`),
  ADD KEY `T_03_03_03_FK_02` (`f_10`),
  ADD KEY `T_03_03_03_FK_03` (`f_11`);

--
-- Indexes for table `t_03_03_04`
--
ALTER TABLE `t_03_03_04`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_03_03_04_IX_01` (`f_01`),
  ADD KEY `T_03_03_04_IX_02` (`f_06`),
  ADD KEY `T_03_03_04_IX_03` (`f_01`,`f_02`,`f_07`),
  ADD KEY `T_03_03_04_FK_06` (`f_09`),
  ADD KEY `T_03_03_04_FK_05` (`f_08`),
  ADD KEY `T_03_03_04_FK_04` (`f_07`),
  ADD KEY `T_03_03_04_FK_01` (`f_04`),
  ADD KEY `T_03_03_04_FK_07` (`f_12`);

--
-- Indexes for table `t_03_03_05`
--
ALTER TABLE `t_03_03_05`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_03_03_05_IX_01` (`f_01`),
  ADD KEY `T_03_03_05_IX_02` (`f_13`),
  ADD KEY `T_03_03_05_IX_03` (`f_11`,`f_13`),
  ADD KEY `T_03_03_05_FK_05` (`f_14`),
  ADD KEY `T_03_03_05_FK_02` (`f_12`),
  ADD KEY `T_03_03_05_FK_04` (`f_10`);

--
-- Indexes for table `t_03_03_06`
--
ALTER TABLE `t_03_03_06`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_03_03_06_UK_01` (`f_09`,`f_10`),
  ADD KEY `T_03_03_06_FK_01` (`f_11`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `t_00_00_01`
--
ALTER TABLE `t_00_00_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_00_01_01`
--
ALTER TABLE `t_00_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `t_00_02_01`
--
ALTER TABLE `t_00_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `t_00_02_03`
--
ALTER TABLE `t_00_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `t_00_02_04`
--
ALTER TABLE `t_00_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=760;

--
-- AUTO_INCREMENT for table `t_00_02_05`
--
ALTER TABLE `t_00_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `t_00_03_01`
--
ALTER TABLE `t_00_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `t_00_05_01`
--
ALTER TABLE `t_00_05_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_00_05_02`
--
ALTER TABLE `t_00_05_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `t_01_01_01`
--
ALTER TABLE `t_01_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `t_01_02_01`
--
ALTER TABLE `t_01_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `t_01_02_02`
--
ALTER TABLE `t_01_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=250;

--
-- AUTO_INCREMENT for table `t_01_02_03`
--
ALTER TABLE `t_01_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=548;

--
-- AUTO_INCREMENT for table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1542;

--
-- AUTO_INCREMENT for table `t_01_02_06`
--
ALTER TABLE `t_01_02_06`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

--
-- AUTO_INCREMENT for table `t_01_02_07`
--
ALTER TABLE `t_01_02_07`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `t_02_01_01`
--
ALTER TABLE `t_02_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `t_02_01_04`
--
ALTER TABLE `t_02_01_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `t_02_01_09`
--
ALTER TABLE `t_02_01_09`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_02_02_01`
--
ALTER TABLE `t_02_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `t_02_02_02`
--
ALTER TABLE `t_02_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `t_02_02_03`
--
ALTER TABLE `t_02_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT for table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=212;

--
-- AUTO_INCREMENT for table `t_02_03_07`
--
ALTER TABLE `t_02_03_07`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `t_02_03_10`
--
ALTER TABLE `t_02_03_10`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_01_01`
--
ALTER TABLE `t_03_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `t_03_01_02`
--
ALTER TABLE `t_03_01_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `t_03_02_01`
--
ALTER TABLE `t_03_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `t_03_02_02`
--
ALTER TABLE `t_03_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `t_03_02_03`
--
ALTER TABLE `t_03_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `t_03_02_04`
--
ALTER TABLE `t_03_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `t_03_02_05`
--
ALTER TABLE `t_03_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `t_03_02_06`
--
ALTER TABLE `t_03_02_06`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `t_03_02_07`
--
ALTER TABLE `t_03_02_07`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `t_03_03_01`
--
ALTER TABLE `t_03_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_03_02`
--
ALTER TABLE `t_03_03_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_03_03`
--
ALTER TABLE `t_03_03_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_03_04`
--
ALTER TABLE `t_03_03_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_03_05`
--
ALTER TABLE `t_03_03_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `t_03_03_06`
--
ALTER TABLE `t_03_03_06`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `r_t000201_t000203`
--
ALTER TABLE `r_t000201_t000203`
  ADD CONSTRAINT `R_T000201_T000203_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_01` (`f_00`),
  ADD CONSTRAINT `R_T000201_T000203_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_03` (`f_00`);

--
-- Constraints for table `r_t000202_t000201`
--
ALTER TABLE `r_t000202_t000201`
  ADD CONSTRAINT `R_T000202_T000201_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_02` (`f_00`),
  ADD CONSTRAINT `R_T000202_T000201_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_01` (`f_00`);

--
-- Constraints for table `r_t000202_t000203`
--
ALTER TABLE `r_t000202_t000203`
  ADD CONSTRAINT `R_T000202_T000203_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_02` (`f_00`),
  ADD CONSTRAINT `R_T000202_T000203_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_03` (`f_00`);

--
-- Constraints for table `r_t000203_t000204`
--
ALTER TABLE `r_t000203_t000204`
  ADD CONSTRAINT `R_T000203_T000204_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_03` (`f_00`),
  ADD CONSTRAINT `R_T000203_T000204_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_04` (`f_00`);

--
-- Constraints for table `r_t020303_t020308`
--
ALTER TABLE `r_t020303_t020308`
  ADD CONSTRAINT `R_T020303_T020308_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_03` (`f_00`),
  ADD CONSTRAINT `R_T020303_T020308_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_08` (`f_00`);

--
-- Constraints for table `r_t020304_t020302`
--
ALTER TABLE `r_t020304_t020302`
  ADD CONSTRAINT `R_T020304_T020302_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_04` (`f_00`),
  ADD CONSTRAINT `R_T020304_T020302_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_02` (`f_00`);

--
-- Constraints for table `r_t020304_t020308`
--
ALTER TABLE `r_t020304_t020308`
  ADD CONSTRAINT `R_T020304_T020308_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_04` (`f_00`),
  ADD CONSTRAINT `R_T020304_T020308_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_08` (`f_00`);

--
-- Constraints for table `r_t020305_t020201`
--
ALTER TABLE `r_t020305_t020201`
  ADD CONSTRAINT `R_T020305_T020201_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020201_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_01` (`f_00`);

--
-- Constraints for table `r_t020305_t020204`
--
ALTER TABLE `r_t020305_t020204`
  ADD CONSTRAINT `R_T020305_T020204_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020204_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_04` (`f_00`);

--
-- Constraints for table `r_t020305_t020308`
--
ALTER TABLE `r_t020305_t020308`
  ADD CONSTRAINT `R_T020305_T020308_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020308_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_08` (`f_00`);

--
-- Constraints for table `r_t020306_t020201`
--
ALTER TABLE `r_t020306_t020201`
  ADD CONSTRAINT `R_T020306_T020201_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_06` (`f_00`),
  ADD CONSTRAINT `R_T020306_T020201_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_01` (`f_00`);

--
-- Constraints for table `r_t020306_t020204`
--
ALTER TABLE `r_t020306_t020204`
  ADD CONSTRAINT `R_T020306_T020204_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_06` (`f_00`),
  ADD CONSTRAINT `R_T020306_T020204_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_04` (`f_00`);

--
-- Constraints for table `r_t020308_t010206`
--
ALTER TABLE `r_t020308_t010206`
  ADD CONSTRAINT `R_T020308_T010206_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_08` (`f_00`),
  ADD CONSTRAINT `R_T020308_T010206_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_01_02_06` (`f_00`);

--
-- Constraints for table `r_t020308_t020205`
--
ALTER TABLE `r_t020308_t020205`
  ADD CONSTRAINT `R_T020308_T020205_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_08` (`f_00`),
  ADD CONSTRAINT `R_T020308_T020205_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_05` (`f_00`);

--
-- Constraints for table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  ADD CONSTRAINT `T_00_02_02_FK_01` FOREIGN KEY (`f_08`) REFERENCES `t_01_03_04` (`f_00`);

--
-- Constraints for table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  ADD CONSTRAINT `T_00_04_01_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_00_02_02` (`f_00`);

--
-- Constraints for table `t_00_05_01`
--
ALTER TABLE `t_00_05_01`
  ADD CONSTRAINT `T_00_05_01_FK_01` FOREIGN KEY (`f_08`) REFERENCES `t_00_05_02` (`f_00`),
  ADD CONSTRAINT `T_00_05_01_FK_02` FOREIGN KEY (`f_09`) REFERENCES `t_00_02_02` (`f_00`);

--
-- Constraints for table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  ADD CONSTRAINT `T_01_02_04_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_02_03` (`f_00`);

--
-- Constraints for table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  ADD CONSTRAINT `T_01_02_05_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_02_04` (`f_00`);

--
-- Constraints for table `t_01_02_06`
--
ALTER TABLE `t_01_02_06`
  ADD CONSTRAINT `T_01_02_06_FK_01` FOREIGN KEY (`f_07`) REFERENCES `t_01_02_05` (`f_00`);

--
-- Constraints for table `t_01_02_07`
--
ALTER TABLE `t_01_02_07`
  ADD CONSTRAINT `T_01_02_07_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_02_03_01` (`f_00`);

--
-- Constraints for table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  ADD CONSTRAINT `T_01_03_01_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_01_01` (`f_00`),
  ADD CONSTRAINT `T_01_03_01_FK_02` FOREIGN KEY (`f_06`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Constraints for table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  ADD CONSTRAINT `T_01_03_02_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Constraints for table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  ADD CONSTRAINT `T_01_03_03_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_01_02_05` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_01_02_05` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_03` FOREIGN KEY (`f_12`) REFERENCES `t_01_02_02` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_04` FOREIGN KEY (`f_13`) REFERENCES `t_00_01_01` (`f_00`);

--
-- Constraints for table `t_01_03_04`
--
ALTER TABLE `t_01_03_04`
  ADD CONSTRAINT `T_01_03_04_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_01_03_03` (`f_00`),
  ADD CONSTRAINT `T_01_03_04_FK_01` FOREIGN KEY (`f_15`) REFERENCES `t_01_03_02` (`f_00`);

--
-- Constraints for table `t_02_01_02`
--
ALTER TABLE `t_02_01_02`
  ADD CONSTRAINT `T_02_01_02_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_01` (`f_00`);

--
-- Constraints for table `t_02_01_03`
--
ALTER TABLE `t_02_01_03`
  ADD CONSTRAINT `T_02_01_03_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_01` (`f_00`);

--
-- Constraints for table `t_02_01_05`
--
ALTER TABLE `t_02_01_05`
  ADD CONSTRAINT `T_02_01_05_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Constraints for table `t_02_01_06`
--
ALTER TABLE `t_02_01_06`
  ADD CONSTRAINT `T_02_01_06_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Constraints for table `t_02_01_07`
--
ALTER TABLE `t_02_01_07`
  ADD CONSTRAINT `T_02_01_07_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Constraints for table `t_02_01_08`
--
ALTER TABLE `t_02_01_08`
  ADD CONSTRAINT `T_02_01_08_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Constraints for table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  ADD CONSTRAINT `T_02_02_04_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_01_02` (`f_00`),
  ADD CONSTRAINT `T_02_02_04_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_01_02_02` (`f_00`);

--
-- Constraints for table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  ADD CONSTRAINT `T_02_02_05_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_01_03` (`f_00`),
  ADD CONSTRAINT `T_02_02_05_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_01_02_02` (`f_00`);

--
-- Constraints for table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  ADD CONSTRAINT `T_02_03_01_FK_01` FOREIGN KEY (`f_06`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_01_FK_02` FOREIGN KEY (`f_07`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Constraints for table `t_02_03_02`
--
ALTER TABLE `t_02_03_02`
  ADD CONSTRAINT `T_02_03_02_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_02_FK_01` FOREIGN KEY (`f_08`) REFERENCES `t_02_02_05` (`f_00`),
  ADD CONSTRAINT `T_02_03_02_FK_02` FOREIGN KEY (`f_09`) REFERENCES `t_01_02_06` (`f_00`);

--
-- Constraints for table `t_02_03_03`
--
ALTER TABLE `t_02_03_03`
  ADD CONSTRAINT `T_02_03_03_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_03_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_01_05` (`f_00`),
  ADD CONSTRAINT `T_02_03_03_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Constraints for table `t_02_03_04`
--
ALTER TABLE `t_02_03_04`
  ADD CONSTRAINT `T_02_03_04_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_04_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_01_06` (`f_00`);

--
-- Constraints for table `t_02_03_05`
--
ALTER TABLE `t_02_03_05`
  ADD CONSTRAINT `T_02_03_05_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_05_FK_01` FOREIGN KEY (`f_11`) REFERENCES `t_02_01_08` (`f_00`);

--
-- Constraints for table `t_02_03_06`
--
ALTER TABLE `t_02_03_06`
  ADD CONSTRAINT `T_02_03_06_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_06_FK_01` FOREIGN KEY (`f_11`) REFERENCES `t_02_01_07` (`f_00`),
  ADD CONSTRAINT `T_02_03_06_FK_02` FOREIGN KEY (`f_12`) REFERENCES `t_02_03_05` (`f_00`);

--
-- Constraints for table `t_02_03_07`
--
ALTER TABLE `t_02_03_07`
  ADD CONSTRAINT `T_02_03_07_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_02_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_03` FOREIGN KEY (`f_05`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Constraints for table `t_02_03_08`
--
ALTER TABLE `t_02_03_08`
  ADD CONSTRAINT `T_02_03_01_FK_07` FOREIGN KEY (`f_24`) REFERENCES `t_01_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_01` FOREIGN KEY (`f_18`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_02` FOREIGN KEY (`f_19`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_03` FOREIGN KEY (`f_20`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_04` FOREIGN KEY (`f_21`) REFERENCES `t_02_03_07` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_05` FOREIGN KEY (`f_22`) REFERENCES `t_02_03_04` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_06` FOREIGN KEY (`f_23`) REFERENCES `t_02_03_04` (`f_00`);

--
-- Constraints for table `t_02_03_09`
--
ALTER TABLE `t_02_03_09`
  ADD CONSTRAINT `T_02_03_09_FK_00` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_01` FOREIGN KEY (`f_14`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_02` FOREIGN KEY (`f_15`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_03` FOREIGN KEY (`f_16`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_04` FOREIGN KEY (`f_17`) REFERENCES `t_02_03_08` (`f_00`);

--
-- Constraints for table `t_02_03_10`
--
ALTER TABLE `t_02_03_10`
  ADD CONSTRAINT `T_02_03_10_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_10_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_02_01_09` (`f_00`),
  ADD CONSTRAINT `T_02_03_10_FK_03` FOREIGN KEY (`f_12`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_10_FK_04` FOREIGN KEY (`f_13`) REFERENCES `t_02_02_05` (`f_00`);

--
-- Constraints for table `t_03_03_01`
--
ALTER TABLE `t_03_03_01`
  ADD CONSTRAINT `T_03_03_01_FK_01` FOREIGN KEY (`f_06`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_01_FK_02` FOREIGN KEY (`f_07`) REFERENCES `t_02_02_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_01_FK_03` FOREIGN KEY (`f_08`) REFERENCES `t_03_01_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_01_FK_04` FOREIGN KEY (`f_09`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_01_FK_05` FOREIGN KEY (`f_10`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_01_FK_06` FOREIGN KEY (`f_11`) REFERENCES `t_03_02_06` (`f_00`);

--
-- Constraints for table `t_03_03_02`
--
ALTER TABLE `t_03_03_02`
  ADD CONSTRAINT `T_03_03_02_FK_01` FOREIGN KEY (`f_07`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_02_FK_02` FOREIGN KEY (`f_08`) REFERENCES `t_02_02_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_02_FK_03` FOREIGN KEY (`f_09`) REFERENCES `t_03_01_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_02_FK_04` FOREIGN KEY (`f_10`) REFERENCES `t_01_03_04` (`f_00`);

--
-- Constraints for table `t_03_03_03`
--
ALTER TABLE `t_03_03_03`
  ADD CONSTRAINT `T_03_03_03_FK_01` FOREIGN KEY (`f_09`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_03_FK_02` FOREIGN KEY (`f_10`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_03_FK_03` FOREIGN KEY (`f_11`) REFERENCES `t_03_02_06` (`f_00`),
  ADD CONSTRAINT `T_03_03_03_FK_04` FOREIGN KEY (`f_12`) REFERENCES `t_02_03_08` (`f_00`),
  ADD CONSTRAINT `T_03_03_03_FK_05` FOREIGN KEY (`f_13`) REFERENCES `t_03_02_07` (`f_00`);

--
-- Constraints for table `t_03_03_04`
--
ALTER TABLE `t_03_03_04`
  ADD CONSTRAINT `T_03_03_04_FK_01` FOREIGN KEY (`f_04`) REFERENCES `t_03_02_03` (`f_00`),
  ADD CONSTRAINT `T_03_03_04_FK_03` FOREIGN KEY (`f_06`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_03_03_04_FK_04` FOREIGN KEY (`f_07`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_04_FK_05` FOREIGN KEY (`f_08`) REFERENCES `t_03_03_03` (`f_00`),
  ADD CONSTRAINT `T_03_03_04_FK_06` FOREIGN KEY (`f_09`) REFERENCES `t_03_03_05` (`f_00`),
  ADD CONSTRAINT `T_03_03_04_FK_07` FOREIGN KEY (`f_12`) REFERENCES `t_03_02_01` (`f_00`);

--
-- Constraints for table `t_03_03_05`
--
ALTER TABLE `t_03_03_05`
  ADD CONSTRAINT `T_03_03_05_FK_01` FOREIGN KEY (`f_11`) REFERENCES `t_03_03_06` (`f_00`),
  ADD CONSTRAINT `T_03_03_05_FK_02` FOREIGN KEY (`f_12`) REFERENCES `t_03_03_03` (`f_00`),
  ADD CONSTRAINT `T_03_03_05_FK_03` FOREIGN KEY (`f_13`) REFERENCES `t_03_02_02` (`f_00`),
  ADD CONSTRAINT `T_03_03_05_FK_04` FOREIGN KEY (`f_10`) REFERENCES `t_01_03_04` (`f_00`),
  ADD CONSTRAINT `T_03_03_05_FK_05` FOREIGN KEY (`f_14`) REFERENCES `t_01_03_04` (`f_00`);

--
-- Constraints for table `t_03_03_06`
--
ALTER TABLE `t_03_03_06`
  ADD CONSTRAINT `T_03_03_06_FK_01` FOREIGN KEY (`f_11`) REFERENCES `t_02_03_08` (`f_00`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
