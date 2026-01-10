-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 10 jan. 2026 à 22:11
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `hyflo_v01`
--

-- --------------------------------------------------------

--
-- Structure de la table `r_t000201_t000203`
--

CREATE TABLE `r_t000201_t000203` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `r_t000201_t000203`
--

INSERT INTO `r_t000201_t000203` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(3, 9),
(3, 10),
(3, 11),
(3, 12),
(4, 13),
(4, 14),
(4, 15),
(5, 16),
(5, 17),
(6, 18),
(6, 19),
(6, 20),
(6, 21),
(6, 22),
(6, 23),
(7, 24),
(7, 25),
(7, 26),
(7, 27),
(8, 28),
(8, 29),
(8, 30);

-- --------------------------------------------------------

--
-- Structure de la table `r_t000202_t000201`
--

CREATE TABLE `r_t000202_t000201` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `r_t000202_t000201`
--

INSERT INTO `r_t000202_t000201` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8);

-- --------------------------------------------------------

--
-- Structure de la table `r_t000202_t000203`
--

CREATE TABLE `r_t000202_t000203` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `r_t000202_t000203`
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
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(1, 20),
(1, 21),
(1, 22),
(1, 23),
(1, 24),
(1, 25),
(1, 26),
(1, 27),
(1, 28),
(1, 29),
(1, 30);

-- --------------------------------------------------------

--
-- Structure de la table `r_t000203_t000204`
--

CREATE TABLE `r_t000203_t000204` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `r_t000203_t000204`
--

INSERT INTO `r_t000203_t000204` (`f_01`, `f_02`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 6),
(2, 7),
(3, 8),
(3, 9),
(3, 10),
(3, 11),
(4, 12),
(4, 13),
(4, 14),
(4, 15),
(4, 16),
(4, 17),
(4, 18),
(4, 19),
(4, 20),
(5, 21),
(5, 22),
(5, 23),
(5, 24),
(5, 25),
(5, 26),
(5, 27),
(6, 28),
(6, 29),
(6, 30),
(6, 31),
(6, 32),
(7, 33),
(7, 34),
(7, 35),
(7, 36),
(7, 37),
(8, 38),
(8, 39),
(8, 40),
(8, 41),
(9, 42),
(9, 43),
(9, 44),
(9, 45),
(9, 46),
(9, 47),
(9, 48),
(10, 49),
(10, 50),
(10, 51),
(11, 52),
(11, 53),
(12, 54),
(12, 55),
(12, 56),
(12, 57),
(12, 58),
(12, 59),
(12, 60),
(12, 61),
(12, 62),
(12, 63),
(13, 64),
(13, 65),
(13, 66),
(13, 67),
(13, 68),
(13, 69),
(13, 70),
(13, 71),
(14, 72),
(14, 73),
(14, 74),
(14, 75),
(14, 76),
(14, 77),
(14, 78),
(14, 79),
(15, 80),
(15, 81),
(15, 82),
(15, 83),
(15, 84),
(15, 85),
(15, 86),
(15, 87),
(15, 88),
(16, 89),
(16, 90),
(16, 91),
(16, 92),
(16, 93),
(17, 94),
(18, 95),
(18, 96),
(18, 97),
(18, 98),
(18, 99),
(19, 100),
(19, 101),
(19, 102),
(19, 103),
(19, 104),
(19, 105),
(19, 106),
(19, 107),
(19, 108),
(20, 109),
(20, 110),
(20, 111),
(20, 112),
(20, 113),
(20, 114),
(20, 115),
(21, 116),
(21, 117),
(21, 118),
(21, 119),
(21, 120),
(22, 121),
(22, 122),
(22, 123),
(22, 124),
(22, 125),
(23, 126),
(23, 127),
(23, 128),
(23, 129),
(24, 130),
(24, 131),
(24, 132),
(24, 133),
(24, 134),
(24, 135),
(24, 136),
(25, 137),
(25, 138),
(25, 139),
(26, 140),
(26, 141),
(27, 142),
(27, 143),
(27, 144),
(27, 145),
(27, 146),
(27, 147),
(27, 148),
(27, 149),
(27, 150),
(27, 151),
(28, 152),
(28, 153),
(28, 154),
(28, 155),
(28, 156),
(28, 157),
(28, 158),
(28, 159),
(29, 160),
(29, 161),
(29, 162),
(29, 163),
(29, 164),
(29, 165),
(29, 166),
(29, 167),
(30, 168),
(30, 169),
(30, 170),
(30, 171),
(30, 172),
(30, 173),
(30, 174),
(30, 175),
(30, 176);

-- --------------------------------------------------------

--
-- Structure de la table `r_t020303_t020307`
--

CREATE TABLE `r_t020303_t020307` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `r_t020304_t020307`
--

CREATE TABLE `r_t020304_t020307` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `r_t020305_t020201`
--

CREATE TABLE `r_t020305_t020201` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `r_t020305_t020204`
--

CREATE TABLE `r_t020305_t020204` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `r_t020305_t020307`
--

CREATE TABLE `r_t020305_t020307` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `r_t020307_t010205`
--

CREATE TABLE `r_t020307_t010205` (
  `f_01` bigint(20) NOT NULL,
  `f_02` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_00_01_01`
--

CREATE TABLE `t_00_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_04` varchar(20) DEFAULT NULL,
  `f_03` varchar(250) NOT NULL,
  `f_02` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_00_02_01`
--

CREATE TABLE `t_00_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_00_02_01`
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
-- Structure de la table `t_00_02_02`
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
-- Déchargement des données de la table `t_00_02_02`
--

INSERT INTO `t_00_02_02` (`f_00`, `f_04`, `f_05`, `f_06`, `f_02`, `f_07`, `f_03`, `f_01`, `f_08`) VALUES
(1, b'1', b'1', b'1', 'superadmin@iaas.dz', b'1', '$2a$12$Qb3ip1Xu8b1RzshlRkDExO7RzvZ5ZxKD3O3k7zZ0365Li/Qkip6Om', 'superadmin', NULL),
(2, b'1', b'1', b'1', 'admin@iaas.dz', b'1', '$2a$12$8zK8mN5pQ2rT7vW3xY4uZ0aB1cD2eF3gH4iJ5kL6mN7oP8qR9sT0', 'admin', NULL),
(3, b'1', b'1', b'1', 'business.manager@iaas.dz', b'1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'business.manager', NULL),
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
-- Structure de la table `t_00_02_03`
--

CREATE TABLE `t_00_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_00_02_03`
--

INSERT INTO `t_00_02_03` (`f_00`, `f_02`, `f_01`) VALUES
(1, 'Manage system security data', 'SYSTEM-SECURITY_ADMIN_ROLE'),
(2, 'Manage system audit data', 'SYSTEM-AUDIT_ADMIN_ROLE'),
(3, 'Manage business core data', 'BUSINESS-CORE_ADMIN_ROLE'),
(4, 'Manage business plan data', 'BUSINESS-PLAN_ADMIN_ROLE'),
(5, 'Manage business provider data', 'BUSINESS-PROVIDER_ADMIN_ROLE'),
(6, 'Manage business consultation data', 'BUSINESS-CONSULTATION_ADMIN_ROLE'),
(7, 'Manage business contract data', 'BUSINESS-CONTRACT_ADMIN_ROLE'),
(8, 'Manage business amendment data', 'BUSINESS-AMENDMENT_ADMIN_ROLE'),
(9, 'Manage common Environment data', 'COMMON-ENVIRONMENT_ADMIN_ROLE'),
(10, 'Manage common communication data', 'COMMON-COMMUNICATION_ADMIN_ROLE'),
(11, 'Manage common document data', 'COMMON-DOCUMENT_ADMIN_ROLE'),
(12, 'Manage common administration data', 'COMMON-ADMINISTRATION_ADMIN_ROLE'),
(13, 'Manage network type data', 'NETWORK-TYPE_ADMIN_ROLE'),
(14, 'Manage network common data', 'NETWORK-COMMON_ADMIN_ROLE'),
(15, 'Manage network core data', 'NETWORK-CORE_ADMIN_ROLE'),
(16, 'View system security data', 'SYSTEM-SECURITY_READ_ROLE'),
(17, 'View system audit data', 'SYSTEM-AUDIT_READ_ROLE'),
(18, 'View business core data', 'BUSINESS-CORE_READ_ROLE'),
(19, 'View business plan data', 'BUSINESS-PLAN_READ_ROLE'),
(20, 'View business provider data', 'BUSINESS-PROVIDER_READ_ROLE'),
(21, 'View business consultation data', 'BUSINESS-CONSULTATION_READ_ROLE'),
(22, 'View business contract data', 'BUSINESS-CONTRACT_READ_ROLE'),
(23, 'View business amendment data', 'BUSINESS-AMENDMENT_READ_ROLE'),
(24, 'View common environment data', 'COMMON-ENVIRONMENT_READ_ROLE'),
(25, 'View common communication data', 'COMMON-COMMUNICATION_READ_ROLE'),
(26, 'View common document data', 'COMMON-DOCUMENT_READ_ROLE'),
(27, 'View common administration data', 'COMMON-ADMINISTRATION_READ_ROLE'),
(28, 'View network type data', 'NETWORK-TYPE_READ_ROLE'),
(29, 'View network common data', 'NETWORK-COMMON_READ_ROLE'),
(30, 'View network core data', 'NETWORK-CORE_READ_ROLE');

-- --------------------------------------------------------

--
-- Structure de la table `t_00_02_04`
--

CREATE TABLE `t_00_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_04` varchar(20) DEFAULT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(100) NOT NULL,
  `f_03` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_00_02_04`
--

INSERT INTO `t_00_02_04` (`f_00`, `f_04`, `f_02`, `f_01`, `f_03`) VALUES
(1, 'ADMIN', 'GROUP ADMINISTRATION', 'GROUP:ADMIN', 'GROUP'),
(2, 'ADMIN', 'USER ADMINISTRATION', 'USER:ADMIN', 'USER'),
(3, 'ADMIN', 'ROLE ADMINISTRATION', 'ROLE:ADMIN', 'ROLE'),
(4, 'ADMIN', 'PERMISSION ADMINISTRATION', 'PERMISSION:ADMIN', 'PERMISSION'),
(5, 'ADMIN', 'AUTHORITY ADMINISTRATION', 'AUTHORITY:ADMIN', 'AUTHORITY'),
(6, 'ADMIN', 'AUDIT ADMINISTRATION', 'AUDIT:ADMIN', 'AUDIT'),
(7, 'ADMIN', 'CURRENCY ADMINISTRATION', 'CURRENCY:ADMIN', 'CURRENCY'),
(8, 'ADMIN', 'APPROVAL_STATUS ADMINISTRATION', 'APPROVAL_STATUS:ADMIN', 'APPROVAL_STATUS'),
(9, 'ADMIN', 'PROCUREMENT_DIRECTOR ADMINISTRATION', 'PROCUREMENT_DIRECTOR:ADMIN', 'PROCUREMENT_DIRECTOR'),
(10, 'ADMIN', 'PROCUREMENT_NATURE ADMINISTRATION', 'PROCUREMENT_NATURE:ADMIN', 'PROCUREMENT_NATURE'),
(11, 'ADMIN', 'PROCUREMENT_STATUS ADMINISTRATION', 'PROCUREMENT_STATUS:ADMIN', 'PROCUREMENT_STATUS'),
(12, 'ADMIN', 'BUDGET_TYPE ADMINISTRATION', 'BUDGET_TYPE:ADMIN', 'BUDGET_TYPE'),
(13, 'ADMIN', 'ITEM_STATUS ADMINISTRATION', 'ITEM_STATUS:ADMIN', 'ITEM_STATUS'),
(14, 'ADMIN', 'FINANCIAL_OPERATION ADMINISTRATION', 'FINANCIAL_OPERATION:ADMIN', 'FINANCIAL_OPERATION'),
(15, 'ADMIN', 'DOMAIN ADMINISTRATION', 'DOMAIN:ADMIN', 'DOMAIN'),
(16, 'ADMIN', 'RUBRIC ADMINISTRATION', 'RUBRIC:ADMIN', 'RUBRIC'),
(17, 'ADMIN', 'ITEM ADMINISTRATION', 'ITEM:ADMIN', 'ITEM'),
(18, 'ADMIN', 'BUDGET_MODIFICATION ADMINISTRATION', 'BUDGET_MODIFICATION:ADMIN', 'BUDGET_MODIFICATION'),
(19, 'ADMIN', 'PLANNED_ITEM ADMINISTRATION', 'PLANNED_ITEM:ADMIN', 'PLANNED_ITEM'),
(20, 'ADMIN', 'ITEM_DISTRIBUTION ADMINISTRATION', 'ITEM_DISTRIBUTION:ADMIN', 'ITEM_DISTRIBUTION'),
(21, 'ADMIN', 'ECONOMIC_DOMAIN ADMINISTRATION', 'ECONOMIC_DOMAIN:ADMIN', 'ECONOMIC_DOMAIN'),
(22, 'ADMIN', 'ECONOMIC_NATURE ADMINISTRATION', 'ECONOMIC_NATURE:ADMIN', 'ECONOMIC_NATURE'),
(23, 'ADMIN', 'EXCLUSION_TYPE ADMINISTRATION', 'EXCLUSION_TYPE:ADMIN', 'EXCLUSION_TYPE'),
(24, 'ADMIN', 'PROVIDER ADMINISTRATION', 'PROVIDER:ADMIN', 'PROVIDER'),
(25, 'ADMIN', 'PROVIDER_REPRESENTATOR ADMINISTRATION', 'PROVIDER_REPRESENTATOR:ADMIN', 'PROVIDER_REPRESENTATOR'),
(26, 'ADMIN', 'CLEARANCE ADMINISTRATION', 'CLEARANCE:ADMIN', 'CLEARANCE'),
(27, 'ADMIN', 'PROVIDER_EXCLUSION ADMINISTRATION', 'PROVIDER_EXCLUSION:ADMIN', 'PROVIDER_EXCLUSION'),
(28, 'ADMIN', 'AWARD_METHOD ADMINISTRATION', 'AWARD_METHOD:ADMIN', 'AWARD_METHOD'),
(29, 'ADMIN', 'CONSULTATION_PHASE ADMINISTRATION', 'CONSULTATION_PHASE:ADMIN', 'CONSULTATION_PHASE'),
(30, 'ADMIN', 'CONSULTATION_STEP ADMINISTRATION', 'CONSULTATION_STEP:ADMIN', 'CONSULTATION_STEP'),
(31, 'ADMIN', 'CONSULTATION ADMINISTRATION', 'CONSULTATION:ADMIN', 'CONSULTATION'),
(32, 'ADMIN', 'SUBMISSION ADMINISTRATION', 'SUBMISSION:ADMIN', 'SUBMISSION'),
(33, 'ADMIN', 'CONTRACT_TYPE ADMINISTRATION', 'CONTRACT_TYPE:ADMIN', 'CONTRACT_TYPE'),
(34, 'ADMIN', 'CONTRACT_PHASE ADMINISTRATION', 'CONTRACT_PHASE:ADMIN', 'CONTRACT_PHASE'),
(35, 'ADMIN', 'CONTRACT_STEP ADMINISTRATION', 'CONTRACT_STEP:ADMIN', 'CONTRACT_STEP'),
(36, 'ADMIN', 'CONTRACT ADMINISTRATION', 'CONTRACT:ADMIN', 'CONTRACT'),
(37, 'ADMIN', 'CONTRACT_ITEM ADMINISTRATION', 'CONTRACT_ITEM:ADMIN', 'CONTRACT_ITEM'),
(38, 'ADMIN', 'AMENDMENT_TYPE ADMINISTRATION', 'AMENDMENT_TYPE:ADMIN', 'AMENDMENT_TYPE'),
(39, 'ADMIN', 'AMENDMENT_PHASE ADMINISTRATION', 'AMENDMENT_PHASE:ADMIN', 'AMENDMENT_PHASE'),
(40, 'ADMIN', 'AMENDMENT_STEP ADMINISTRATION', 'AMENDMENT_STEP:ADMIN', 'AMENDMENT_STEP'),
(41, 'ADMIN', 'AMENDMENT ADMINISTRATION', 'AMENDMENT:ADMIN', 'AMENDMENT'),
(42, 'ADMIN', 'BLOC ADMINISTRATION', 'BLOC:ADMIN', 'BLOC'),
(43, 'ADMIN', 'FLOOR ADMINISTRATION', 'FLOOR:ADMIN', 'FLOOR'),
(44, 'ADMIN', 'ROOM ADMINISTRATION', 'ROOM:ADMIN', 'ROOM'),
(45, 'ADMIN', 'SHELF_FLOOR ADMINISTRATION', 'SHELF_FLOOR:ADMIN', 'SHELF_FLOOR'),
(46, 'ADMIN', 'SHELF ADMINISTRATION', 'SHELF:ADMIN', 'SHELF'),
(47, 'ADMIN', 'ARCHIVE_BOX ADMINISTRATION', 'ARCHIVE_BOX:ADMIN', 'ARCHIVE_BOX'),
(48, 'ADMIN', 'FOLDER ADMINISTRATION', 'FOLDER:ADMIN', 'FOLDER'),
(49, 'ADMIN', 'MAIL_NATURE ADMINISTRATION', 'MAIL_NATURE:ADMIN', 'MAIL_NATURE'),
(50, 'ADMIN', 'MAIL_TYPE ADMINISTRATION', 'MAIL_TYPE:ADMIN', 'MAIL_TYPE'),
(51, 'ADMIN', 'MAIL ADMINISTRATION', 'MAIL:ADMIN', 'MAIL'),
(52, 'ADMIN', 'DOCUMENT_TYPE ADMINISTRATION', 'DOCUMENT_TYPE:ADMIN', 'DOCUMENT_TYPE'),
(53, 'ADMIN', 'DOCUMENT ADMINISTRATION', 'DOCUMENT:ADMIN', 'DOCUMENT'),
(54, 'ADMIN', 'COUNTRY ADMINISTRATION', 'COUNTRY:ADMIN', 'COUNTRY'),
(55, 'ADMIN', 'STATE ADMINISTRATION', 'STATE:ADMIN', 'STATE'),
(56, 'ADMIN', 'LOCALITY ADMINISTRATION', 'LOCALITY:ADMIN', 'LOCALITY'),
(57, 'ADMIN', 'MILITARY_CATEGORY ADMINISTRATION', 'MILITARY_CATEGORY:ADMIN', 'MILITARY_CATEGORY'),
(58, 'ADMIN', 'MILITARY_RANK ADMINISTRATION', 'MILITARY_RANK:ADMIN', 'MILITARY_RANK'),
(59, 'ADMIN', 'STRUCTURE_TYPE ADMINISTRATION', 'STRUCTURE_TYPE:ADMIN', 'STRUCTURE_TYPE'),
(60, 'ADMIN', 'STRUCTURE ADMINISTRATION', 'STRUCTURE:ADMIN', 'STRUCTURE'),
(61, 'ADMIN', 'JOB ADMINISTRATION', 'JOB:ADMIN', 'JOB'),
(62, 'ADMIN', 'PERSON ADMINISTRATION', 'PERSON:ADMIN', 'PERSON'),
(63, 'ADMIN', 'EMPLOYEE ADMINISTRATION', 'EMPLOYEE:ADMIN', 'EMPLOYEE'),
(64, 'ADMIN', 'COMPANY_TYPE ADMINISTRATION', 'COMPANY_TYPE:ADMIN', 'COMPANY_TYPE'),
(65, 'ADMIN', 'PARTNER_TYPE ADMINISTRATION', 'PARTNER_TYPE:ADMIN', 'PARTNER_TYPE'),
(66, 'ADMIN', 'VENDOR_TYPE ADMINISTRATION', 'VENDOR_TYPE:ADMIN', 'VENDOR_TYPE'),
(67, 'ADMIN', 'FACILITY_TYPE ADMINISTRATION', 'FACILITY_TYPE:ADMIN', 'FACILITY_TYPE'),
(68, 'ADMIN', 'STATION_TYPE ADMINISTRATION', 'STATION_TYPE:ADMIN', 'STATION_TYPE'),
(69, 'ADMIN', 'TERMINAL_TYPE ADMINISTRATION', 'TERMINAL_TYPE:ADMIN', 'TERMINAL_TYPE'),
(70, 'ADMIN', 'HYDROCARBON_FIELD_TYPE ADMINISTRATION', 'HYDROCARBON_FIELD_TYPE:ADMIN', 'HYDROCARBON_FIELD_TYPE'),
(71, 'ADMIN', 'EQUIPMENT_TYPE ADMINISTRATION', 'EQUIPMENT_TYPE:ADMIN', 'EQUIPMENT_TYPE'),
(72, 'ADMIN', 'ZONE ADMINISTRATION', 'ZONE:ADMIN', 'ZONE'),
(73, 'ADMIN', 'REGION ADMINISTRATION', 'REGION:ADMIN', 'REGION'),
(74, 'ADMIN', 'LOCATION ADMINISTRATION', 'LOCATION:ADMIN', 'LOCATION'),
(75, 'ADMIN', 'PRODUCT ADMINISTRATION', 'PRODUCT:ADMIN', 'PRODUCT'),
(76, 'ADMIN', 'OPERATIONAL_STATUS ADMINISTRATION', 'OPERATIONAL_STATUS:ADMIN', 'OPERATIONAL_STATUS'),
(77, 'ADMIN', 'ALLOY ADMINISTRATION', 'ALLOY:ADMIN', 'ALLOY'),
(78, 'ADMIN', 'PARTNER ADMINISTRATION', 'PARTNER:ADMIN', 'PARTNER'),
(79, 'ADMIN', 'VENDOR ADMINISTRATION', 'VENDOR:ADMIN', 'VENDOR'),
(80, 'ADMIN', 'INFRASTRUCTURE ADMINISTRATION', 'INFRASTRUCTURE:ADMIN', 'INFRASTRUCTURE'),
(81, 'ADMIN', 'FACILITY ADMINISTRATION', 'FACILITY:ADMIN', 'FACILITY'),
(82, 'ADMIN', 'PIPELINE_SYSTEM ADMINISTRATION', 'PIPELINE_SYSTEM:ADMIN', 'PIPELINE_SYSTEM'),
(83, 'ADMIN', 'PIPELINE ADMINISTRATION', 'PIPELINE:ADMIN', 'PIPELINE'),
(84, 'ADMIN', 'PIPELINE_SEGMENT ADMINISTRATION', 'PIPELINE_SEGMENT:ADMIN', 'PIPELINE_SEGMENT'),
(85, 'ADMIN', 'STATION ADMINISTRATION', 'STATION:ADMIN', 'STATION'),
(86, 'ADMIN', 'TERMINAL ADMINISTRATION', 'TERMINAL:ADMIN', 'TERMINAL'),
(87, 'ADMIN', 'HYDROCARBON_FIELD ADMINISTRATION', 'HYDROCARBON_FIELD:ADMIN', 'HYDROCARBON_FIELD'),
(88, 'ADMIN', 'EQUIPMENT ADMINISTRATION', 'EQUIPMENT:ADMIN', 'EQUIPMENT'),
(89, 'READ', 'GROUP VIEWING', 'GROUP:READ', 'GROUP'),
(90, 'READ', 'USER VIEWING', 'USER:READ', 'USER'),
(91, 'READ', 'ROLE VIEWING', 'ROLE:READ', 'ROLE'),
(92, 'READ', 'PERMISSION VIEWING', 'PERMISSION:READ', 'PERMISSION'),
(93, 'READ', 'AUTHORITY VIEWING', 'AUTHORITY:READ', 'AUTHORITY'),
(94, 'READ', 'AUDIT VIEWING', 'AUDIT:READ', 'AUDIT'),
(95, 'READ', 'CURRENCY VIEWING', 'CURRENCY:READ', 'CURRENCY'),
(96, 'READ', 'APPROVAL_STATUS VIEWING', 'APPROVAL_STATUS:READ', 'APPROVAL_STATUS'),
(97, 'READ', 'PROCUREMENT_DIRECTOR VIEWING', 'PROCUREMENT_DIRECTOR:READ', 'PROCUREMENT_DIRECTOR'),
(98, 'READ', 'PROCUREMENT_NATURE VIEWING', 'PROCUREMENT_NATURE:READ', 'PROCUREMENT_NATURE'),
(99, 'READ', 'PROCUREMENT_STATUS VIEWING', 'PROCUREMENT_STATUS:READ', 'PROCUREMENT_STATUS'),
(100, 'READ', 'BUDGET_TYPE VIEWING', 'BUDGET_TYPE:READ', 'BUDGET_TYPE'),
(101, 'READ', 'ITEM_STATUS VIEWING', 'ITEM_STATUS:READ', 'ITEM_STATUS'),
(102, 'READ', 'FINANCIAL_OPERATION VIEWING', 'FINANCIAL_OPERATION:READ', 'FINANCIAL_OPERATION'),
(103, 'READ', 'DOMAIN VIEWING', 'DOMAIN:READ', 'DOMAIN'),
(104, 'READ', 'RUBRIC VIEWING', 'RUBRIC:READ', 'RUBRIC'),
(105, 'READ', 'ITEM VIEWING', 'ITEM:READ', 'ITEM'),
(106, 'READ', 'BUDGET_MODIFICATION VIEWING', 'BUDGET_MODIFICATION:READ', 'BUDGET_MODIFICATION'),
(107, 'READ', 'PLANNED_ITEM VIEWING', 'PLANNED_ITEM:READ', 'PLANNED_ITEM'),
(108, 'READ', 'ITEM_DISTRIBUTION VIEWING', 'ITEM_DISTRIBUTION:READ', 'ITEM_DISTRIBUTION'),
(109, 'READ', 'ECONOMIC_DOMAIN VIEWING', 'ECONOMIC_DOMAIN:READ', 'ECONOMIC_DOMAIN'),
(110, 'READ', 'ECONOMIC_NATURE VIEWING', 'ECONOMIC_NATURE:READ', 'ECONOMIC_NATURE'),
(111, 'READ', 'EXCLUSION_TYPE VIEWING', 'EXCLUSION_TYPE:READ', 'EXCLUSION_TYPE'),
(112, 'READ', 'PROVIDER VIEWING', 'PROVIDER:READ', 'PROVIDER'),
(113, 'READ', 'PROVIDER_REPRESENTATOR VIEWING', 'PROVIDER_REPRESENTATOR:READ', 'PROVIDER_REPRESENTATOR'),
(114, 'READ', 'CLEARANCE VIEWING', 'CLEARANCE:READ', 'CLEARANCE'),
(115, 'READ', 'PROVIDER_EXCLUSION VIEWING', 'PROVIDER_EXCLUSION:READ', 'PROVIDER_EXCLUSION'),
(116, 'READ', 'AWARD_METHOD VIEWING', 'AWARD_METHOD:READ', 'AWARD_METHOD'),
(117, 'READ', 'CONSULTATION_PHASE VIEWING', 'CONSULTATION_PHASE:READ', 'CONSULTATION_PHASE'),
(118, 'READ', 'CONSULTATION_STEP VIEWING', 'CONSULTATION_STEP:READ', 'CONSULTATION_STEP'),
(119, 'READ', 'CONSULTATION VIEWING', 'CONSULTATION:READ', 'CONSULTATION'),
(120, 'READ', 'SUBMISSION VIEWING', 'SUBMISSION:READ', 'SUBMISSION'),
(121, 'READ', 'CONTRACT_TYPE VIEWING', 'CONTRACT_TYPE:READ', 'CONTRACT_TYPE'),
(122, 'READ', 'CONTRACT_PHASE VIEWING', 'CONTRACT_PHASE:READ', 'CONTRACT_PHASE'),
(123, 'READ', 'CONTRACT_STEP VIEWING', 'CONTRACT_STEP:READ', 'CONTRACT_STEP'),
(124, 'READ', 'CONTRACT VIEWING', 'CONTRACT:READ', 'CONTRACT'),
(125, 'READ', 'CONTRACT_ITEM VIEWING', 'CONTRACT_ITEM:READ', 'CONTRACT_ITEM'),
(126, 'READ', 'AMENDMENT_TYPE VIEWING', 'AMENDMENT_TYPE:READ', 'AMENDMENT_TYPE'),
(127, 'READ', 'AMENDMENT_PHASE VIEWING', 'AMENDMENT_PHASE:READ', 'AMENDMENT_PHASE'),
(128, 'READ', 'AMENDMENT_STEP VIEWING', 'AMENDMENT_STEP:READ', 'AMENDMENT_STEP'),
(129, 'READ', 'AMENDMENT VIEWING', 'AMENDMENT:READ', 'AMENDMENT'),
(130, 'READ', 'BLOC VIEWING', 'BLOC:READ', 'BLOC'),
(131, 'READ', 'FLOOR VIEWING', 'FLOOR:READ', 'FLOOR'),
(132, 'READ', 'ROOM VIEWING', 'ROOM:READ', 'ROOM'),
(133, 'READ', 'SHELF_FLOOR VIEWING', 'SHELF_FLOOR:READ', 'SHELF_FLOOR'),
(134, 'READ', 'SHELF VIEWING', 'SHELF:READ', 'SHELF'),
(135, 'READ', 'ARCHIVE_BOX VIEWING', 'ARCHIVE_BOX:READ', 'ARCHIVE_BOX'),
(136, 'READ', 'FOLDER VIEWING', 'FOLDER:READ', 'FOLDER'),
(137, 'READ', 'MAIL_NATURE VIEWING', 'MAIL_NATURE:READ', 'MAIL_NATURE'),
(138, 'READ', 'MAIL_TYPE VIEWING', 'MAIL_TYPE:READ', 'MAIL_TYPE'),
(139, 'READ', 'MAIL VIEWING', 'MAIL:READ', 'MAIL'),
(140, 'READ', 'DOCUMENT_TYPE VIEWING', 'DOCUMENT_TYPE:READ', 'DOCUMENT_TYPE'),
(141, 'READ', 'DOCUMENT VIEWING', 'DOCUMENT:READ', 'DOCUMENT'),
(142, 'READ', 'COUNTRY VIEWING', 'COUNTRY:READ', 'COUNTRY'),
(143, 'READ', 'STATE VIEWING', 'STATE:READ', 'STATE'),
(144, 'READ', 'LOCALITY VIEWING', 'LOCALITY:READ', 'LOCALITY'),
(145, 'READ', 'MILITARY_CATEGORY VIEWING', 'MILITARY_CATEGORY:READ', 'MILITARY_CATEGORY'),
(146, 'READ', 'MILITARY_RANK VIEWING', 'MILITARY_RANK:READ', 'MILITARY_RANK'),
(147, 'READ', 'STRUCTURE_TYPE VIEWING', 'STRUCTURE_TYPE:READ', 'STRUCTURE_TYPE'),
(148, 'READ', 'STRUCTURE VIEWING', 'STRUCTURE:READ', 'STRUCTURE'),
(149, 'READ', 'JOB VIEWING', 'JOB:READ', 'JOB'),
(150, 'READ', 'PERSON VIEWING', 'PERSON:READ', 'PERSON'),
(151, 'READ', 'EMPLOYEE VIEWING', 'EMPLOYEE:READ', 'EMPLOYEE'),
(152, 'READ', 'COMPANY_TYPE VIEWING', 'COMPANY_TYPE:READ', 'COMPANY_TYPE'),
(153, 'READ', 'PARTNER_TYPE VIEWING', 'PARTNER_TYPE:READ', 'PARTNER_TYPE'),
(154, 'READ', 'VENDOR_TYPE VIEWING', 'VENDOR_TYPE:READ', 'VENDOR_TYPE'),
(155, 'READ', 'FACILITY_TYPE VIEWING', 'FACILITY_TYPE:READ', 'FACILITY_TYPE'),
(156, 'READ', 'HYDROCARBON_FIELD_TYPE VIEWING', 'HYDROCARBON_FIELD_TYPE:READ', 'HYDROCARBON_FIELD_TYPE'),
(157, 'READ', 'STATION_TYPE VIEWING', 'STATION_TYPE:READ', 'STATION_TYPE'),
(158, 'READ', 'TERMINAL_TYPE VIEWING', 'TERMINAL_TYPE:READ', 'TERMINAL_TYPE'),
(159, 'READ', 'EQUIPMENT_TYPE VIEWING', 'EQUIPMENT_TYPE:READ', 'EQUIPMENT_TYPE'),
(160, 'READ', 'ZONE VIEWING', 'ZONE:READ', 'ZONE'),
(161, 'READ', 'REGION VIEWING', 'REGION:READ', 'REGION'),
(162, 'READ', 'LOCATION VIEWING', 'LOCATION:READ', 'LOCATION'),
(163, 'READ', 'PRODUCT VIEWING', 'PRODUCT:READ', 'PRODUCT'),
(164, 'READ', 'OPERATIONAL_STATUS VIEWING', 'OPERATIONAL_STATUS:READ', 'OPERATIONAL_STATUS'),
(165, 'READ', 'ALLOY VIEWING', 'ALLOY:READ', 'ALLOY'),
(166, 'READ', 'PARTNER VIEWING', 'PARTNER:READ', 'PARTNER'),
(167, 'READ', 'VENDOR VIEWING', 'VENDOR:READ', 'VENDOR'),
(168, 'READ', 'INFRASTRUCTURE VIEWING', 'INFRASTRUCTURE:READ', 'INFRASTRUCTURE'),
(169, 'READ', 'FACILITY VIEWING', 'FACILITY:READ', 'FACILITY'),
(170, 'READ', 'STATION VIEWING', 'STATION:READ', 'STATION'),
(171, 'READ', 'TERMINAL VIEWING', 'TERMINAL:READ', 'TERMINAL'),
(172, 'READ', 'HYDROCARBON_FIELD VIEWING', 'HYDROCARBON_FIELD:READ', 'HYDROCARBON_FIELD'),
(173, 'READ', 'PIPELINE_SYSTEM VIEWING', 'PIPELINE_SYSTEM:READ', 'PIPELINE_SYSTEM'),
(174, 'READ', 'PIPELINE VIEWING', 'PIPELINE:READ', 'PIPELINE'),
(175, 'READ', 'PIPELINE_SEGMENT VIEWING', 'PIPELINE_SEGMENT:READ', 'PIPELINE_SEGMENT'),
(176, 'READ', 'EQUIPMENT VIEWING', 'EQUIPMENT:READ', 'EQUIPMENT');

-- --------------------------------------------------------

--
-- Structure de la table `t_00_02_05`
--

CREATE TABLE `t_00_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(200) DEFAULT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_03` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_00_02_05`
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
(11, 'Procurement Core', 'PROCUREMENT', 'Core procurement operations'),
(12, 'Approval Process', 'APPROVAL', 'Approval status and workflows'),
(13, 'Currency Management', 'CURRENCY', 'Currency and exchange rates'),
(14, 'Contract Management', 'CONTRACT', 'Contract lifecycle management'),
(15, 'Contract Amendment', 'AMENDMENT', 'Contract amendment processing'),
(16, 'Consultation Process', 'CONSULTATION', 'Consultation and tender evaluation'),
(17, 'Procurement Planning', 'PLAN', 'Annual procurement planning'),
(18, 'Provider Management', 'PROVIDER', 'Provider and vendor management'),
(19, 'Administration', 'ADMINISTRATION', 'Administrative operations'),
(20, 'Communication', 'COMMUNICATION', 'Messaging and notifications'),
(21, 'Document Management', 'DOCUMENT', 'Document handling and archiving'),
(22, 'Environment Data', 'ENVIRONMENT', 'Geographic and location data'),
(23, 'Network Management', 'NETWORK', 'Network infrastructure management'),
(24, 'IT Infrastructure', 'INFRASTRUCTURE', 'IT infrastructure and services'),
(25, 'Reporting & Analytics', 'REPORT', 'Reports, dashboards, and analytics'),
(26, 'Workflow Engine', 'WORKFLOW', 'Business process workflows'),
(27, 'System Integration', 'INTEGRATION', 'External system integrations');

-- --------------------------------------------------------

--
-- Structure de la table `t_00_03_01`
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
-- Structure de la table `t_00_04_01`
--

CREATE TABLE `t_00_04_01` (
  `f_00` bigint(20) NOT NULL,
  `f_02` datetime(6) NOT NULL,
  `f_01` varchar(255) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_00_04_01`
--

INSERT INTO `t_00_04_01` (`f_00`, `f_02`, `f_01`, `f_03`) VALUES
(3, '2026-01-11 11:28:13.000000', '0a23d5b2-a33b-4ec0-8457-db2b023d804c', 1);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_01_01`
--

CREATE TABLE `t_01_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_01_01`
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
-- Structure de la table `t_01_02_01`
--

CREATE TABLE `t_01_02_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_02_01`
--

INSERT INTO `t_01_02_01` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'SUD', 'الجنوب', 'South', 'Sud'),
(2, 'NORD', 'الشمال', 'North', 'Nord');

-- --------------------------------------------------------

--
-- Structure de la table `t_01_02_02`
--

CREATE TABLE `t_01_02_02` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(3) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_02_02`
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
-- Structure de la table `t_01_02_03`
--

CREATE TABLE `t_01_02_03` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(10) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_02_03`
--

INSERT INTO `t_01_02_03` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, '1', 'أدرار', NULL, 'Adrar'),
(2, '2', 'الشلف', NULL, 'Chlef'),
(3, '3', 'الأغواط', NULL, 'Laghouat'),
(4, '4', 'أم البواقي', NULL, 'Oum El Bouaghi'),
(5, '5', 'باتنة', NULL, 'Batna'),
(6, '6', 'بجاية', NULL, 'Béjaïa'),
(7, '7', 'بسكرة', NULL, 'Biskra'),
(8, '8', 'بشار', NULL, 'Béchar'),
(9, '9', 'البليدة', NULL, 'Blida'),
(10, '10', 'البويرة', NULL, 'Bouïra'),
(11, '11', 'تمنراست', NULL, 'Tamanrasset'),
(12, '12', 'تبسة', NULL, 'Tébessa'),
(13, '13', 'تلمسان', NULL, 'Tlemcen'),
(14, '14', 'تيارت', NULL, 'Tiaret'),
(15, '15', 'تيزي وزو', NULL, 'Tizi Ouzou'),
(16, '16', 'الجزائر', NULL, 'Alger'),
(17, '17', 'الجلفة', NULL, 'Djelfa'),
(18, '18', 'جيجل', NULL, 'Jijel'),
(19, '19', 'سطيف', NULL, 'Sétif'),
(20, '20', 'سعيدة', NULL, 'Saïda'),
(21, '21', 'سكيكدة', NULL, 'Skikda'),
(22, '22', 'سيدي بلعباس', NULL, 'Sidi Bel Abbès'),
(23, '23', 'عنابة', NULL, 'Annaba'),
(24, '24', 'قالمة', NULL, 'Guelma'),
(25, '25', 'قسنطينة', NULL, 'Constantine'),
(26, '26', 'المدية', NULL, 'Médéa'),
(27, '27', 'مستغانم', NULL, 'Mostaganem'),
(28, '28', 'المسيلة', NULL, 'M\'Sila'),
(29, '29', 'معسكر', NULL, 'Mascara'),
(30, '30', 'ورقلة', NULL, 'Ouargla'),
(31, '31', 'وهران', NULL, 'Oran'),
(32, '32', 'البيض', NULL, 'El Bayadh'),
(33, '33', 'إليزي', NULL, 'Illizi'),
(34, '34', 'برج بوعريريج', NULL, 'Bordj Bou Arréridj'),
(35, '35', 'بومرداس', NULL, 'Boumerdès'),
(36, '36', 'الطارف', NULL, 'El Tarf'),
(37, '37', 'تندوف', NULL, 'Tindouf'),
(38, '38', 'تيسمسيلت', NULL, 'Tissemsilt'),
(39, '39', 'الوادي', NULL, 'El Oued'),
(40, '40', 'خنشلة', NULL, 'Khenchela'),
(41, '41', 'سوق أهراس', NULL, 'Souk Ahras'),
(42, '42', 'تيبازة', NULL, 'Tipaza'),
(43, '43', 'ميلة', NULL, 'Mila'),
(44, '44', 'عين الدفلى', NULL, 'Aïn Defla'),
(45, '45', 'النعامة', NULL, 'Naâma'),
(46, '46', 'عين تموشنت', NULL, 'Aïn Témouchent'),
(47, '47', 'غرداية', NULL, 'Ghardaïa'),
(48, '48', 'غليزان', NULL, 'Relizane'),
(49, '49', 'تيميمون', NULL, 'Timimoun'),
(50, '50', 'برج باجي مختار', NULL, 'Bordj Baji Mokhtar'),
(51, '51', 'أولاد جلال', NULL, 'Ouled Djellal'),
(52, '52', 'بني عباس', NULL, 'Béni Abbès'),
(53, '53', 'إن صالح', NULL, 'In Salah'),
(54, '54', 'إن قزام', NULL, 'In Guezzam'),
(55, '55', 'تقرت', NULL, 'Touggourt'),
(56, '56', 'جانت', NULL, 'Djanet'),
(57, '57', 'المغير', NULL, 'El M\'Ghair'),
(58, '58', 'المنيعة', NULL, 'El Menia');

-- --------------------------------------------------------

--
-- Structure de la table `t_01_02_04`
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
-- Déchargement des données de la table `t_01_02_04`
--

INSERT INTO `t_01_02_04` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(1, '0001', 'أدرار', 'Adrar', 'Adrar', 1),
(2, '0002', 'اقبلي', 'Akabli', 'Akabli', 1),
(3, '0003', 'أوقروت', 'Aougrout', 'Aougrout', 49),
(4, '0004', 'أولف', 'Aoulef', 'Aoulef', 1),
(5, '0005', 'برج باجي مختار', 'Bordj Badji Mokhtar', 'Bordj Badji Mokhtar', 50),
(6, '0006', 'بودة', 'Bouda', 'Bouda', 1),
(7, '0007', 'شروين', 'Charouine', 'Charouine', 49),
(8, '0008', 'دلدول', 'Deldoul', 'Deldoul', 49),
(9, '0009', 'فنوغيل', 'Fenoughil', 'Fenoughil', 1),
(10, '0010', 'إن زغمير', 'In Zghmir', 'In Zghmir', 1),
(11, '0011', 'قصر قدور', 'Ksar Kaddour', 'Ksar Kaddour', 49),
(12, '0012', 'المطارفة', 'Metarfa', 'Metarfa', 49),
(13, '0013', 'أولاد أحمد تيمي', 'Ouled Ahmed Timmi', 'Ouled Ahmed Timmi', 1),
(14, '0014', 'أولاد عيسى', 'Ouled Aissa', 'Ouled Aissa', 49),
(15, '0015', 'أولاد السعيد', 'Ouled Said', 'Ouled Said', 49),
(16, '0016', 'رقان', 'Reggane', 'Reggane', 1),
(17, '0017', 'سالي', 'Sali', 'Sali', 1),
(18, '0018', 'السبع', 'Sebaa', 'Sebaa', 1),
(19, '0019', 'طالمين', 'Talmine', 'Talmine', 49),
(20, '0020', 'تامنطيط', 'Tamantit', 'Tamantit', 1),
(21, '0021', 'تامست', 'Tamest', 'Tamest', 1),
(22, '0022', 'تيمقتن', 'Timekten', 'Timekten', 1),
(23, '0023', 'تيمياوين', 'Timiaouine', 'Timiaouine', 50),
(24, '0024', 'تيميمون', 'Timimoun', 'Timimoun', 49),
(25, '0025', 'تنركوك', 'Tinerkouk', 'Tinerkouk', 49),
(26, '0026', 'تيت', 'Tit', 'Tit', 1),
(27, '0027', 'تسابيت', 'Tsabit', 'Tsabit', 1),
(28, '0028', 'زاوية كنتة', 'Zaouiet Kounta', 'Zaouiet Kounta', 1),
(29, '0029', 'أبو الحسن', 'Abou El Hassane', 'Abou El Hassane', 2),
(30, '0030', 'عين مران', 'Ain Merane', 'Ain Merane', 2),
(31, '0031', 'بنايرية', 'Benairia', 'Benairia', 2),
(32, '0032', 'بني بوعتاب', 'Beni  Bouattab', 'Beni  Bouattab', 2),
(33, '0033', 'بني حواء', 'Beni Haoua', 'Beni Haoua', 2),
(34, '0034', 'بني راشد', 'Beni Rached', 'Beni Rached', 2),
(35, '0035', 'بوقادير', 'Boukadir', 'Boukadir', 2),
(36, '0036', 'بوزغاية', 'Bouzeghaia', 'Bouzeghaia', 2),
(37, '0037', 'بريرة', 'Breira', 'Breira', 2),
(38, '0038', 'الشطية', 'Chettia', 'Chettia', 2),
(39, '0039', 'الشلف', 'Chlef', 'Chlef', 2),
(40, '0040', 'الظهرة', 'Dahra', 'Dahra', 2),
(41, '0041', 'الحجاج', 'El Hadjadj', 'El Hadjadj', 2),
(42, '0042', 'الكريمية', 'El Karimia', 'El Karimia', 2),
(43, '0043', 'المرسى', 'El Marsa', 'El Marsa', 2),
(44, '0044', 'حرشون', 'Harchoun', 'Harchoun', 2),
(45, '0045', 'الهرانفة', 'Herenfa', 'Herenfa', 2),
(46, '0046', 'الأبيض مجاجة', 'Labiod Medjadja', 'Labiod Medjadja', 2),
(47, '0047', 'مصدق', 'Moussadek', 'Moussadek', 2),
(48, '0048', 'وادي الفضة', 'Oued Fodda', 'Oued Fodda', 2),
(49, '0049', 'وادي قوسين', 'Oued Goussine', 'Oued Goussine', 2),
(50, '0050', 'وادي سلي', 'Oued Sly', 'Oued Sly', 2),
(51, '0051', 'أولاد عباس', 'Ouled Abbes', 'Ouled Abbes', 2),
(52, '0052', 'أولاد بن عبد القادر', 'Ouled Ben Abdelkader', 'Ouled Ben Abdelkader', 2),
(53, '0053', 'أولاد فارس', 'Ouled Fares', 'Ouled Fares', 2),
(54, '0054', 'أم الدروع', 'Oum Drou', 'Oum Drou', 2),
(55, '0055', 'سنجاس', 'Sendjas', 'Sendjas', 2),
(56, '0056', 'سيدي عبد الرحمن', 'Sidi Abderrahmane', 'Sidi Abderrahmane', 2),
(57, '0057', 'سيدي عكاشة', 'Sidi Akkacha', 'Sidi Akkacha', 2),
(58, '0058', 'الصبحة', 'Sobha', 'Sobha', 2),
(59, '0059', 'تاجنة', 'Tadjena', 'Tadjena', 2),
(60, '0060', 'تلعصة', 'Talassa', 'Talassa', 2),
(61, '0061', 'تاوقريت', 'Taougrit', 'Taougrit', 2),
(62, '0062', 'تنس', 'Tenes', 'Tenes', 2),
(63, '0063', 'الزبوجة', 'Zeboudja', 'Zeboudja', 2),
(64, '0064', 'أفلو', 'Aflou', 'Aflou', 3),
(65, '0065', 'عين ماضي', 'Ain Madhi', 'Ain Madhi', 3),
(66, '0066', 'عين سيدي علي', 'Ain Sidi Ali', 'Ain Sidi Ali', 3),
(67, '0067', 'البيضاء', 'El Beidha', 'El Beidha', 3),
(68, '0068', 'بن ناصر بن شهرة', 'Benacer Benchohra', 'Benacer Benchohra', 3),
(69, '0069', 'بريدة', 'Brida', 'Brida', 3),
(70, '0070', 'العسافية', 'El Assafia', 'El Assafia', 3),
(71, '0071', 'الغيشة', 'El Ghicha', 'El Ghicha', 3),
(72, '0072', 'الحويطة', 'El Haouaita', 'El Haouaita', 3),
(73, '0073', 'قلتة سيدي سعد', 'Gueltat Sidi Saad', 'Gueltat Sidi Saad', 3),
(74, '0074', 'الحاج مشري', 'Hadj Mechri', 'Hadj Mechri', 3),
(75, '0075', 'حاسي الدلاعة', 'Hassi Delaa', 'Hassi Delaa', 3),
(76, '0076', 'حاسي الرمل', 'Hassi R\'mel', 'Hassi R\'mel', 3),
(77, '0077', 'الخنق', 'Kheneg', 'Kheneg', 3),
(78, '0078', 'قصر الحيران', 'Ksar El Hirane', 'Ksar El Hirane', 3),
(79, '0079', 'الأغواط', 'Laghouat', 'Laghouat', 3),
(80, '0080', 'وادي مرة', 'Oued Morra', 'Oued Morra', 3),
(81, '0081', 'وادي مزي', 'Oued M\'zi', 'Oued M\'zi', 3),
(82, '0082', 'سبقاق', 'Sebgag', 'Sebgag', 3),
(83, '0083', 'سيدي بوزيد', 'Sidi Bouzid', 'Sidi Bouzid', 3),
(84, '0084', 'سيدي مخلوف', 'Sidi Makhlouf', 'Sidi Makhlouf', 3),
(85, '0085', 'تاجموت', 'Tadjemout', 'Tadjemout', 3),
(86, '0086', 'تاجرونة', 'Tadjrouna', 'Tadjrouna', 3),
(87, '0087', 'تاويالة', 'Taouiala', 'Taouiala', 3),
(88, '0088', 'عين ببوش', 'Ain Babouche', 'Ain Babouche', 4),
(89, '0089', 'عين البيضاء', 'Ain Beida', 'Ain Beida', 4),
(90, '0090', 'عين الديس', 'Ain Diss', 'Ain Diss', 4),
(91, '0091', 'عين فكرون', 'Ain Fekroun', 'Ain Fekroun', 4),
(92, '0092', 'عين كرشة', 'Ain Kercha', 'Ain Kercha', 4),
(93, '0093', 'عين مليلة', 'Ain M\'lila', 'Ain M\'lila', 4),
(94, '0094', 'عين الزيتون', 'Ain Zitoun', 'Ain Zitoun', 4),
(95, '0095', 'بحير الشرقي', 'Behir Chergui', 'Behir Chergui', 4),
(96, '0096', 'بريش', 'Berriche', 'Berriche', 4),
(97, '0097', 'بئر الشهداء', 'Bir Chouhada', 'Bir Chouhada', 4),
(98, '0098', 'الضلعة', 'Dhalaa', 'Dhalaa', 4),
(99, '0099', 'العامرية', 'El Amiria', 'El Amiria', 4),
(100, '0100', 'البلالة', 'El Belala', 'El Belala', 4),
(101, '0101', 'الجازية', 'El Djazia', 'El Djazia', 4),
(102, '0102', 'الفجوج بوغرارة سعودي', 'El Fedjoudj Boughrara Sa', 'El Fedjoudj Boughrara Sa', 4),
(103, '0103', 'الحرملية', 'El Harmilia', 'El Harmilia', 4),
(104, '0104', 'فكيرينة', 'Fkirina', 'Fkirina', 4),
(105, '0105', 'هنشير تومغني', 'Hanchir Toumghani', 'Hanchir Toumghani', 4),
(106, '0106', 'قصر الصباحي', 'Ksar Sbahi', 'Ksar Sbahi', 4),
(107, '0107', 'مسكيانة', 'Meskiana', 'Meskiana', 4),
(108, '0108', 'وادي نيني', 'Oued Nini', 'Oued Nini', 4),
(109, '0109', 'أولاد قاسم', 'Ouled Gacem', 'Ouled Gacem', 4),
(110, '0110', 'أولاد حملة', 'Ouled Hamla', 'Ouled Hamla', 4),
(111, '0111', 'أولاد زواي', 'Ouled Zouai', 'Ouled Zouai', 4),
(112, '0112', 'أم البواقي', 'Oum El Bouaghi', 'Oum El Bouaghi', 4),
(113, '0113', 'الرحية', 'Rahia', 'Rahia', 4),
(114, '0114', 'سيقوس', 'Sigus', 'Sigus', 4),
(115, '0115', 'سوق نعمان', 'Souk Naamane', 'Souk Naamane', 4),
(116, '0116', 'الزرق', 'Zorg', 'Zorg', 4),
(117, '0117', 'عين جاسر', 'Ain Djasser', 'Ain Djasser', 5),
(118, '0118', 'عين التوتة', 'Ain Touta', 'Ain Touta', 5),
(119, '0119', 'عين ياقوت', 'Ain Yagout', 'Ain Yagout', 5),
(120, '0120', 'أريس', 'Arris', 'Arris', 5),
(121, '0121', 'عزيل عبد القادر', 'Azil Abedelkader', 'Azil Abedelkader', 5),
(122, '0122', 'بريكة', 'Barika', 'Barika', 5),
(123, '0123', 'باتنة', 'Batna', 'Batna', 5),
(124, '0124', 'بني فضالة الحقانية', 'Beni Foudhala El Hakania', 'Beni Foudhala El Hakania', 5),
(125, '0125', 'بيطام', 'Bitam', 'Bitam', 5),
(126, '0126', 'بولهيلات', 'Boulhilat', 'Boulhilat', 5),
(127, '0127', 'بومقر', 'Boumagueur', 'Boumagueur', 5),
(128, '0128', 'بومية', 'Boumia', 'Boumia', 5),
(129, '0129', 'بوزينة', 'Bouzina', 'Bouzina', 5),
(130, '0130', 'الشمرة', 'Chemora', 'Chemora', 5),
(131, '0131', 'شير', 'Chir', 'Chir', 5),
(132, '0132', 'جرمة', 'Djerma', 'Djerma', 5),
(133, '0133', 'الجزار', 'Djezzar', 'Djezzar', 5),
(134, '0134', 'الحاسي', 'El Hassi', 'El Hassi', 5),
(135, '0135', 'المعذر', 'El Madher', 'El Madher', 5),
(136, '0136', 'فسديس', 'Fesdis', 'Fesdis', 5),
(137, '0137', 'فم الطوب', 'Foum Toub', 'Foum Toub', 5),
(138, '0138', 'غسيرة', 'Ghassira', 'Ghassira', 5),
(139, '0139', 'القصبات', 'Gosbat', 'Gosbat', 5),
(140, '0140', 'القيقبة', 'Guigba', 'Guigba', 5),
(141, '0141', 'حيدوسة', 'Hidoussa', 'Hidoussa', 5),
(142, '0142', 'إشمول', 'Ichemoul', 'Ichemoul', 5),
(143, '0143', 'إينوغيسن', 'Inoughissen', 'Inoughissen', 5),
(144, '0144', 'كيمل', 'Kimmel', 'Kimmel', 5),
(145, '0145', 'قصر بلزمة', 'Ksar Bellezma', 'Ksar Bellezma', 5),
(146, '0146', 'لارباع', 'Larbaa', 'Larbaa', 5),
(147, '0147', 'لازرو', 'Lazrou', 'Lazrou', 5),
(148, '0148', 'لمسان', 'Lemcene', 'Lemcene', 5),
(149, '0149', 'إمدوكل', 'M Doukal', 'M Doukal', 5),
(150, '0150', 'معافة', 'Maafa', 'Maafa', 5),
(151, '0151', 'منعة', 'Menaa', 'Menaa', 5),
(152, '0152', 'مروانة', 'Merouana', 'Merouana', 5),
(153, '0153', 'نقاوس', 'N Gaous', 'N Gaous', 5),
(154, '0154', 'وادي الشعبة', 'Oued Chaaba', 'Oued Chaaba', 5),
(155, '0155', 'وادي الماء', 'Oued El Ma', 'Oued El Ma', 5),
(156, '0156', 'وادي الطاقة', 'Oued Taga', 'Oued Taga', 5),
(157, '0157', 'أولاد عمار', 'Ouled Ammar', 'Ouled Ammar', 5),
(158, '0158', 'أولاد عوف', 'Ouled Aouf', 'Ouled Aouf', 5),
(159, '0159', 'أولاد فاضل', 'Ouled Fadel', 'Ouled Fadel', 5),
(160, '0160', 'أولاد سلام', 'Ouled Sellem', 'Ouled Sellem', 5),
(161, '0161', 'أولاد سي سليمان', 'Ouled Si Slimane', 'Ouled Si Slimane', 5),
(162, '0162', 'عيون العصافير', 'Ouyoun El Assafir', 'Ouyoun El Assafir', 5),
(163, '0163', 'الرحبات', 'Rahbat', 'Rahbat', 5),
(164, '0164', 'رأس العيون', 'Ras El Aioun', 'Ras El Aioun', 5),
(165, '0165', 'سفيان', 'Sefiane', 'Sefiane', 5),
(166, '0166', 'سقانة', 'Seggana', 'Seggana', 5),
(167, '0167', 'سريانة', 'Seriana', 'Seriana', 5),
(168, '0168', 'تكوت', 'T Kout', 'T Kout', 5),
(169, '0169', 'تالخمت', 'Talkhamt', 'Talkhamt', 5),
(170, '0170', 'تاكسلانت', 'Taxlent', 'Taxlent', 5),
(171, '0171', 'تازولت', 'Tazoult', 'Tazoult', 5),
(172, '0172', 'ثنية العابد', 'Teniet El Abed', 'Teniet El Abed', 5),
(173, '0173', 'تيغانمين', 'Tighanimine', 'Tighanimine', 5),
(174, '0174', 'تغرغار', 'Tigharghar', 'Tigharghar', 5),
(175, '0175', 'تيلاطو', 'Tilatou', 'Tilatou', 5),
(176, '0176', 'تيمقاد', 'Timgad', 'Timgad', 5),
(177, '0177', 'زانة البيضاء', 'Zanet El Beida', 'Zanet El Beida', 5),
(178, '0178', 'أدكار', 'Adekar', 'Adekar', 6),
(179, '0179', 'أيت رزين', 'Ait R\'zine', 'Ait R\'zine', 6),
(180, '0180', 'أيت إسماعيل', 'Ait-Smail', 'Ait-Smail', 6),
(181, '0181', 'أقبو', 'Akbou', 'Akbou', 6),
(182, '0182', 'أكفادو', 'Akfadou', 'Akfadou', 6),
(183, '0183', 'أمالو', 'Amalou', 'Amalou', 6),
(184, '0184', 'أميزور', 'Amizour', 'Amizour', 6),
(185, '0185', 'أوقاس', 'Aokas', 'Aokas', 6),
(186, '0186', 'برباشة', 'Barbacha', 'Barbacha', 6),
(187, '0187', 'بجاية', 'Bejaia', 'Bejaia', 6),
(188, '0188', 'بني جليل', 'Beni Djellil', 'Beni Djellil', 6),
(189, '0189', 'بني كسيلة', 'Beni K\'sila', 'Beni K\'sila', 6),
(190, '0190', 'بني مليكش', 'Beni-Mallikeche', 'Beni-Mallikeche', 6),
(191, '0191', 'بني معوش', 'Benimaouche', 'Benimaouche', 6),
(192, '0192', 'بو جليل', 'Boudjellil', 'Boudjellil', 6),
(193, '0193', 'بوحمزة', 'Bouhamza', 'Bouhamza', 6),
(194, '0194', 'بوخليفة', 'Boukhelifa', 'Boukhelifa', 6),
(195, '0195', 'شلاطة', 'Chellata', 'Chellata', 6),
(196, '0196', 'شميني', 'Chemini', 'Chemini', 6),
(197, '0197', 'درقينة', 'Darguina', 'Darguina', 6),
(198, '0198', 'ذراع القايد', 'Dra El Caid', 'Dra El Caid', 6),
(199, '0199', 'الفلاي', 'Leflaye', 'Leflaye', 6),
(200, '0200', 'القصر', 'El Kseur', 'El Kseur', 6),
(201, '0201', 'فناية الماثن', 'Fenaia Il Maten', 'Fenaia Il Maten', 6),
(202, '0202', 'فرعون', 'Feraoun', 'Feraoun', 6),
(203, '0203', 'أوزلاقن', 'Ouzellaguen', 'Ouzellaguen', 6),
(204, '0204', 'إغيل علي', 'Ighil-Ali', 'Ighil-Ali', 6),
(205, '0205', 'اغرم', 'Ighram', 'Ighram', 6),
(206, '0206', 'كنديرة', 'Kendira', 'Kendira', 6),
(207, '0207', 'خراطة', 'Kherrata', 'Kherrata', 6),
(208, '0208', 'مسيسنة', 'M\'cisna', 'M\'cisna', 6),
(209, '0209', 'مالبو', 'Melbou', 'Melbou', 6),
(210, '0210', 'وادي غير', 'Oued Ghir', 'Oued Ghir', 6),
(211, '0211', 'صدوق', 'Seddouk', 'Seddouk', 6),
(212, '0212', 'سيدي عياد', 'Sidi Ayad', 'Sidi Ayad', 6),
(213, '0213', 'سيدي عيش', 'Sidi-Aich', 'Sidi-Aich', 6),
(214, '0214', 'سمعون', 'Smaoun', 'Smaoun', 6),
(215, '0215', 'سوق لإثنين', 'Souk El Tenine', 'Souk El Tenine', 6),
(216, '0216', 'سوق اوفلا', 'Souk Oufella', 'Souk Oufella', 6),
(217, '0217', 'تالة حمزة', 'Tala Hamza', 'Tala Hamza', 6),
(218, '0218', 'تامقرة', 'Tamokra', 'Tamokra', 6),
(219, '0219', 'تامريجت', 'Tamridjet', 'Tamridjet', 6),
(220, '0220', 'تاوريرت إغيل', 'Taourit Ighil', 'Taourit Ighil', 6),
(221, '0221', 'تاسكريوت', 'Taskriout', 'Taskriout', 6),
(222, '0222', 'تازمالت', 'Tazmalt', 'Tazmalt', 6),
(223, '0223', 'طيبان', 'Tibane', 'Tibane', 6),
(224, '0224', 'تيشي', 'Tichy', 'Tichy', 6),
(225, '0225', 'تيفرة', 'Tifra', 'Tifra', 6),
(226, '0226', 'تيمزريت', 'Timezrit', 'Timezrit', 6),
(227, '0227', 'تينبدار', 'Tinebdar', 'Tinebdar', 6),
(228, '0228', 'تيزي نبربر', 'Tizi-N\'berber', 'Tizi-N\'berber', 6),
(229, '0229', 'توجة', 'Toudja', 'Toudja', 6),
(230, '0230', 'عين الناقة', 'Ain Naga', 'Ain Naga', 7),
(231, '0231', 'عين زعطوط', 'Ain Zaatout', 'Ain Zaatout', 7),
(232, '0232', 'بسباس', 'Besbes', 'Besbes', 51),
(233, '0233', 'بسكرة', 'Biskra', 'Biskra', 7),
(234, '0234', 'برج بن عزوز', 'Bordj Ben Azzouz', 'Bordj Ben Azzouz', 7),
(235, '0235', 'بوشقرون', 'Bouchakroun', 'Bouchakroun', 7),
(236, '0236', 'برانيس', 'Branis', 'Branis', 7),
(237, '0237', 'الشعيبة', 'Chaiba', 'Chaiba', 51),
(238, '0238', 'شتمة', 'Chetma', 'Chetma', 7),
(239, '0239', 'جمورة', 'Djemorah', 'Djemorah', 7),
(240, '0240', 'الدوسن', 'Doucen', 'Doucen', 51),
(241, '0241', 'الفيض', 'El Feidh', 'El Feidh', 7),
(242, '0242', 'الغروس', 'El Ghrous', 'El Ghrous', 7),
(243, '0243', 'الحاجب', 'El Hadjab', 'El Hadjab', 7),
(244, '0244', 'الحوش', 'El Haouch', 'El Haouch', 7),
(245, '0245', 'القنطرة', 'El Kantara', 'El Kantara', 7),
(246, '0246', 'الوطاية', 'El Outaya', 'El Outaya', 7),
(247, '0247', 'فوغالة', 'Foughala', 'Foughala', 7),
(248, '0248', 'خنقة سيدي ناجي', 'Khenguet Sidi Nadji', 'Khenguet Sidi Nadji', 7),
(249, '0249', 'ليشانة', 'Lichana', 'Lichana', 7),
(250, '0250', 'ليوة', 'Lioua', 'Lioua', 7),
(251, '0251', 'مشونش', 'M\'chouneche', 'M\'chouneche', 7),
(252, '0252', 'مخادمة', 'Mekhadma', 'Mekhadma', 7),
(253, '0253', 'المزيرعة', 'Meziraa', 'Meziraa', 7),
(254, '0254', 'مليلي', 'M\'lili', 'M\'lili', 7),
(255, '0255', 'أولاد جلال', 'Ouled Djellal', 'Ouled Djellal', 51),
(256, '0256', 'أوماش', 'Oumache', 'Oumache', 7),
(257, '0257', 'أورلال', 'Ourlal', 'Ourlal', 7),
(258, '0258', 'رأس الميعاد', 'Ras El Miad', 'Ras El Miad', 51),
(259, '0259', 'سيدي  خالد', 'Sidi Khaled', 'Sidi Khaled', 51),
(260, '0260', 'سيدي عقبة', 'Sidi Okba', 'Sidi Okba', 7),
(261, '0261', 'طولقة', 'Tolga', 'Tolga', 7),
(262, '0262', 'زريبة الوادي', 'Zeribet El Oued', 'Zeribet El Oued', 7),
(263, '0263', 'العبادلة', 'Abadla', 'Abadla', 8),
(264, '0264', 'بشار', 'Bechar', 'Bechar', 8),
(265, '0265', 'بني عباس', 'Beni-Abbes', 'Beni-Abbes', 52),
(266, '0266', 'بن يخلف', 'Beni-Ikhlef', 'Beni-Ikhlef', 52),
(267, '0267', 'بني ونيف', 'Beni-Ounif', 'Beni-Ounif', 8),
(268, '0268', 'بوكايس', 'Boukais', 'Boukais', 8),
(269, '0269', 'الواتة', 'El Ouata', 'El Ouata', 52),
(270, '0270', 'عرق فراج', 'Erg-Ferradj', 'Erg-Ferradj', 8),
(271, '0271', 'إقلي', 'Igli', 'Igli', 52),
(272, '0272', 'القنادسة', 'Kenadsa', 'Kenadsa', 8),
(273, '0273', 'كرزاز', 'Kerzaz', 'Kerzaz', 52),
(274, '0274', 'القصابي', 'Ksabi', 'Ksabi', 52),
(275, '0275', 'لحمر', 'Lahmar', 'Lahmar', 8),
(276, '0276', 'مشرع هواري بومدين', 'Machraa-Houari-Boumediene', 'Machraa-Houari-Boumediene', 8),
(277, '0277', 'المريجة', 'Meridja', 'Meridja', 8),
(278, '0278', 'موغل', 'Mogheul', 'Mogheul', 8),
(279, '0279', 'أولاد خضير', 'Ouled-Khodeir', 'Ouled-Khodeir', 52),
(280, '0280', 'تبلبالة', 'Tabelbala', 'Tabelbala', 8),
(281, '0281', 'تاغيت', 'Taghit', 'Taghit', 8),
(282, '0282', 'تامترت', 'Tamtert', 'Tamtert', 52),
(283, '0283', 'تيمودي', 'Timoudi', 'Timoudi', 52),
(284, '0284', 'عين الرمانة', 'Ain Romana', 'Ain Romana', 9),
(285, '0285', 'بني مراد', 'Beni Mered', 'Beni Mered', 9),
(286, '0286', 'بني تامو', 'Beni-Tamou', 'Beni-Tamou', 9),
(287, '0287', 'بن خليل', 'Benkhelil', 'Benkhelil', 9),
(288, '0288', 'البليدة', 'Blida', 'Blida', 9),
(289, '0289', 'بوعرفة', 'Bouarfa', 'Bouarfa', 9),
(290, '0290', 'بوفاريك', 'Boufarik', 'Boufarik', 9),
(291, '0291', 'بوقرة', 'Bougara', 'Bougara', 9),
(292, '0292', 'بوعينان', 'Bouinan', 'Bouinan', 9),
(293, '0293', 'الشبلي', 'Chebli', 'Chebli', 9),
(294, '0294', 'الشفة', 'Chiffa', 'Chiffa', 9),
(295, '0295', 'الشريعة', 'Chrea', 'Chrea', 9),
(296, '0296', 'جبابرة', 'Djebabra', 'Djebabra', 9),
(297, '0297', 'العفرون', 'El-Affroun', 'El-Affroun', 9),
(298, '0298', 'قرواو', 'Guerrouaou', 'Guerrouaou', 9),
(299, '0299', 'حمام ملوان', 'Hammam Elouane', 'Hammam Elouane', 9),
(300, '0300', 'الأربعاء', 'Larbaa', 'Larbaa', 9),
(301, '0301', 'مفتاح', 'Meftah', 'Meftah', 9),
(302, '0302', 'موزاية', 'Mouzaia', 'Mouzaia', 9),
(303, '0303', 'وادي جر', 'Oued  Djer', 'Oued  Djer', 9),
(304, '0304', 'وادي العلايق', 'Oued El Alleug', 'Oued El Alleug', 9),
(305, '0305', 'اولاد سلامة', 'Ouled Slama', 'Ouled Slama', 9),
(306, '0306', 'أولاد يعيش', 'Ouled Yaich', 'Ouled Yaich', 9),
(307, '0307', 'صوحان', 'Souhane', 'Souhane', 9),
(308, '0308', 'الصومعة', 'Soumaa', 'Soumaa', 9),
(309, '0309', 'أغبالو', 'Aghbalou', 'Aghbalou', 10),
(310, '0310', 'أهل القصر', 'Ahl El Ksar', 'Ahl El Ksar', 10),
(311, '0311', 'عين الحجر', 'Ain El Hadjar', 'Ain El Hadjar', 10),
(312, '0312', 'عين العلوي', 'Ain Laloui', 'Ain Laloui', 10),
(313, '0313', 'عين الترك', 'Ain Turk', 'Ain Turk', 10),
(314, '0314', 'عين بسام', 'Ain-Bessem', 'Ain-Bessem', 10),
(315, '0315', 'أيت لعزيز', 'Ait Laaziz', 'Ait Laaziz', 10),
(316, '0316', 'أعمر', 'Aomar', 'Aomar', 10),
(317, '0317', 'بشلول', 'Bechloul', 'Bechloul', 10),
(318, '0318', 'بئر غبالو', 'Bir Ghbalou', 'Bir Ghbalou', 10),
(319, '0319', 'برج أوخريص', 'Bordj Okhriss', 'Bordj Okhriss', 10),
(320, '0320', 'بودربالة', 'Bouderbala', 'Bouderbala', 10),
(321, '0321', 'البويرة', 'Bouira', 'Bouira', 10),
(322, '0322', 'بوكرم', 'Boukram', 'Boukram', 10),
(323, '0323', 'شرفة', 'Chorfa', 'Chorfa', 10),
(324, '0324', 'الدشمية', 'Dechmia', 'Dechmia', 10),
(325, '0325', 'ديرة', 'Dirah', 'Dirah', 10),
(326, '0326', 'جباحية', 'Djebahia', 'Djebahia', 10),
(327, '0327', 'العجيبة', 'El Adjiba', 'El Adjiba', 10),
(328, '0328', 'الأسنام', 'El Asnam', 'El Asnam', 10),
(329, '0329', 'الهاشمية', 'El Hachimia', 'El Hachimia', 10),
(330, '0330', 'الخبوزية', 'El Khabouzia', 'El Khabouzia', 10),
(331, '0331', 'الحاكمية', 'El-Hakimia', 'El-Hakimia', 10),
(332, '0332', 'المقراني', 'El-Mokrani', 'El-Mokrani', 10),
(333, '0333', 'قرومة', 'Guerrouma', 'Guerrouma', 10),
(334, '0334', 'الحجرة الزرقاء', 'Hadjera Zerga', 'Hadjera Zerga', 10),
(335, '0335', 'حيزر', 'Haizer', 'Haizer', 10),
(336, '0336', 'حنيف', 'Hanif', 'Hanif', 10),
(337, '0337', 'قادرية', 'Kadiria', 'Kadiria', 10),
(338, '0338', 'الأخضرية', 'Lakhdaria', 'Lakhdaria', 10),
(339, '0339', 'أمشدالة', 'M Chedallah', 'M Chedallah', 10),
(340, '0340', 'معلة', 'Maala', 'Maala', 10),
(341, '0341', 'المعمورة', 'Maamora', 'Maamora', 10),
(342, '0342', 'مزدور', 'Mezdour', 'Mezdour', 10),
(343, '0343', 'وادي البردي', 'Oued El Berdi', 'Oued El Berdi', 10),
(344, '0344', 'أولاد راشد', 'Ouled Rached', 'Ouled Rached', 10),
(345, '0345', 'روراوة', 'Raouraoua', 'Raouraoua', 10),
(346, '0346', 'ريدان', 'Ridane', 'Ridane', 10),
(347, '0347', 'سحاريج', 'Saharidj', 'Saharidj', 10),
(348, '0348', 'سوق الخميس', 'Souk El Khemis', 'Souk El Khemis', 10),
(349, '0349', 'سور الغزلان', 'Sour El Ghozlane', 'Sour El Ghozlane', 10),
(350, '0350', 'تاغزوت', 'Taghzout', 'Taghzout', 10),
(351, '0351', 'تاقديت', 'Taguedite', 'Taguedite', 10),
(352, '0352', 'آث  منصور', 'Ath Mansour', 'Ath Mansour', 10),
(353, '0353', 'زبربر', 'Z\'barbar (El Isseri )', 'Z\'barbar (El Isseri )', 10),
(354, '0354', 'ابلسة', 'Abelsa', 'Abelsa', 11),
(355, '0355', 'عين امقل', 'Ain Amguel', 'Ain Amguel', 11),
(356, '0356', 'عين قزام', 'Ain Guezzam', 'Ain Guezzam', 54),
(357, '0357', 'عين صالح', 'Ain Salah', 'Ain Salah', 53),
(358, '0358', 'فقارة الزوى', 'Foggaret Ezzoua', 'Foggaret Ezzoua', 53),
(359, '0359', 'أدلس', 'Idles', 'Idles', 11),
(360, '0360', 'إينغر', 'Inghar', 'Inghar', 53),
(361, '0361', 'تمنراست', 'Tamanrasset', 'Tamanrasset', 11),
(362, '0362', 'تاظروك', 'Tazrouk', 'Tazrouk', 11),
(363, '0363', 'تين زواتين', 'Tin Zouatine', 'Tin Zouatine', 54),
(364, '0364', 'عين الزرقاء', 'Ain Zerga', 'Ain Zerga', 12),
(365, '0365', 'بجن', 'Bedjene', 'Bedjene', 12),
(366, '0366', 'بكارية', 'Bekkaria', 'Bekkaria', 12),
(367, '0367', 'بئر الذهب', 'Bir Dheheb', 'Bir Dheheb', 12),
(368, '0368', 'بئر مقدم', 'Bir Mokkadem', 'Bir Mokkadem', 12),
(369, '0369', 'بئر العاتر', 'Bir-El-Ater', 'Bir-El-Ater', 12),
(370, '0370', 'بوخضرة', 'Boukhadra', 'Boukhadra', 12),
(371, '0371', 'بولحاف الدير', 'Boulhaf Dyr', 'Boulhaf Dyr', 12),
(372, '0372', 'الشريعة', 'Cheria', 'Cheria', 12),
(373, '0373', 'الكويف', 'El Kouif', 'El Kouif', 12),
(374, '0374', 'الماء الابيض', 'El Malabiod', 'El Malabiod', 12),
(375, '0375', 'المريج', 'El Meridj', 'El Meridj', 12),
(376, '0376', 'المزرعة', 'El Mezeraa', 'El Mezeraa', 12),
(377, '0377', 'العقلة', 'El Ogla', 'El Ogla', 12),
(378, '0378', 'العقلة المالحة', 'El Ogla El Malha', 'El Ogla El Malha', 12),
(379, '0379', 'العوينات', 'El-Aouinet', 'El-Aouinet', 12),
(380, '0380', 'الحويجبات', 'El-Houidjbet', 'El-Houidjbet', 12),
(381, '0381', 'فركان', 'Ferkane', 'Ferkane', 12),
(382, '0382', 'قريقر', 'Guorriguer', 'Guorriguer', 12),
(383, '0383', 'الحمامات', 'Hammamet', 'Hammamet', 12),
(384, '0384', 'مرسط', 'Morsott', 'Morsott', 12),
(385, '0385', 'نقرين', 'Negrine', 'Negrine', 12),
(386, '0386', 'الونزة', 'Ouenza', 'Ouenza', 12),
(387, '0387', 'أم علي', 'Oum Ali', 'Oum Ali', 12),
(388, '0388', 'صفصاف الوسرى', 'Saf Saf El Ouesra', 'Saf Saf El Ouesra', 12),
(389, '0389', 'سطح قنطيس', 'Stah Guentis', 'Stah Guentis', 12),
(390, '0390', 'تبسة', 'Tebessa', 'Tebessa', 12),
(391, '0391', 'ثليجان', 'Telidjen', 'Telidjen', 12),
(392, '0392', 'عين فتاح', 'Ain Fetah', 'Ain Fetah', 13),
(393, '0393', 'عين فزة', 'Ain Fezza', 'Ain Fezza', 13),
(394, '0394', 'عين غرابة', 'Ain Ghoraba', 'Ain Ghoraba', 13),
(395, '0395', 'عين الكبيرة', 'Ain Kebira', 'Ain Kebira', 13),
(396, '0396', 'عين النحالة', 'Ain Nehala', 'Ain Nehala', 13),
(397, '0397', 'عين تالوت', 'Ain Tellout', 'Ain Tellout', 13),
(398, '0398', 'عين يوسف', 'Ain Youcef', 'Ain Youcef', 13),
(399, '0399', 'عمير', 'Amieur', 'Amieur', 13),
(400, '0400', 'باب العسة', 'Bab El Assa', 'Bab El Assa', 13),
(401, '0401', 'بني بهدل', 'Beni Bahdel', 'Beni Bahdel', 13),
(402, '0402', 'بني بوسعيد', 'Beni Boussaid', 'Beni Boussaid', 13),
(403, '0403', 'بني خلاد', 'Beni Khellad', 'Beni Khellad', 13),
(404, '0404', 'بني مستر', 'Beni Mester', 'Beni Mester', 13),
(405, '0405', 'بني وارسوس', 'Beni Ouarsous', 'Beni Ouarsous', 13),
(406, '0406', 'بني صميل', 'Beni Smiel', 'Beni Smiel', 13),
(407, '0407', 'بني سنوس', 'Beni Snous', 'Beni Snous', 13),
(408, '0408', 'بن سكران', 'Bensekrane', 'Bensekrane', 13),
(409, '0409', 'بوحلو', 'Bouhlou', 'Bouhlou', 13),
(410, '0410', 'البويهي', 'Bouihi', 'Bouihi', 13),
(411, '0411', 'شتوان', 'Chetouane', 'Chetouane', 13),
(412, '0412', 'دار يغمراسن', 'Dar Yaghmoracen', 'Dar Yaghmoracen', 13),
(413, '0413', 'جبالة', 'Djebala', 'Djebala', 13),
(414, '0414', 'العريشة', 'El Aricha', 'El Aricha', 13),
(415, '0415', 'العزايل', 'Azail', 'Azail', 13),
(416, '0416', 'الفحول', 'El Fehoul', 'El Fehoul', 13),
(417, '0417', 'القور', 'El Gor', 'El Gor', 13),
(418, '0418', 'فلاوسن', 'Fellaoucene', 'Fellaoucene', 13),
(419, '0419', 'الغزوات', 'Ghazaouet', 'Ghazaouet', 13),
(420, '0420', 'حمام بوغرارة', 'Hammam Boughrara', 'Hammam Boughrara', 13),
(421, '0421', 'الحناية', 'Hennaya', 'Hennaya', 13),
(422, '0422', 'هنين', 'Honnaine', 'Honnaine', 13),
(423, '0423', 'مغنية', 'Maghnia', 'Maghnia', 13),
(424, '0424', 'منصورة', 'Mansourah', 'Mansourah', 13),
(425, '0425', 'مرسى بن مهيدي', 'Marsa Ben M\'hidi', 'Marsa Ben M\'hidi', 13),
(426, '0426', 'مسيردة الفواقة', 'M\'sirda Fouaga', 'M\'sirda Fouaga', 13),
(427, '0427', 'ندرومة', 'Nedroma', 'Nedroma', 13),
(428, '0428', 'وادي الخضر', 'Oued Lakhdar', 'Oued Lakhdar', 13),
(429, '0429', 'أولاد ميمون', 'Ouled Mimoun', 'Ouled Mimoun', 13),
(430, '0430', 'أولاد رياح', 'Ouled Riyah', 'Ouled Riyah', 13),
(431, '0431', 'الرمشي', 'Remchi', 'Remchi', 13),
(432, '0432', 'صبرة', 'Sabra', 'Sabra', 13),
(433, '0433', 'سبعة شيوخ', 'Sebbaa Chioukh', 'Sebbaa Chioukh', 13),
(434, '0434', 'سبدو', 'Sebdou', 'Sebdou', 13),
(435, '0435', 'سيدي العبدلي', 'Sidi Abdelli', 'Sidi Abdelli', 13),
(436, '0436', 'سيدي الجيلالي', 'Sidi Djillali', 'Sidi Djillali', 13),
(437, '0437', 'سيدي مجاهد', 'Sidi Medjahed', 'Sidi Medjahed', 13),
(438, '0438', 'السواحلية', 'Souahlia', 'Souahlia', 13),
(439, '0439', 'السواني', 'Souani', 'Souani', 13),
(440, '0440', 'سوق الثلاثاء', 'Souk Tleta', 'Souk Tleta', 13),
(441, '0441', 'تيرني بني هديل', 'Terny Beni Hediel', 'Terny Beni Hediel', 13),
(442, '0442', 'تيانت', 'Tianet', 'Tianet', 13),
(443, '0443', 'تلمسان', 'Tlemcen', 'Tlemcen', 13),
(444, '0444', 'زناتة', 'Zenata', 'Zenata', 13),
(445, '0445', 'عين بوشقيف', 'Ain Bouchekif', 'Ain Bouchekif', 14),
(446, '0446', 'عين الذهب', 'Ain Deheb', 'Ain Deheb', 14),
(447, '0447', 'عين دزاريت', 'Ain Dzarit', 'Ain Dzarit', 14),
(448, '0448', 'عين الحديد', 'Ain El Hadid', 'Ain El Hadid', 14),
(449, '0449', 'عين كرمس', 'Ain Kermes', 'Ain Kermes', 14),
(450, '0450', 'بوقرة', 'Bougara', 'Bougara', 14),
(451, '0451', 'شحيمة', 'Chehaima', 'Chehaima', 14),
(452, '0452', 'دحموني', 'Dahmouni', 'Dahmouni', 14),
(453, '0453', 'جبيلات الرصفاء', 'Djebilet Rosfa', 'Djebilet Rosfa', 14),
(454, '0454', 'جيلالي بن عمار', 'Djillali Ben Amar', 'Djillali Ben Amar', 14),
(455, '0455', 'الفايجة', 'Faidja', 'Faidja', 14),
(456, '0456', 'فرندة', 'Frenda', 'Frenda', 14),
(457, '0457', 'قرطوفة', 'Guertoufa', 'Guertoufa', 14),
(458, '0458', 'حمادية', 'Hamadia', 'Hamadia', 14),
(459, '0459', 'قصر الشلالة', 'Ksar Chellala', 'Ksar Chellala', 14),
(460, '0460', 'مادنة', 'Madna', 'Madna', 14),
(461, '0461', 'مهدية', 'Mahdia', 'Mahdia', 14),
(462, '0462', 'مشرع الصفا', 'Mechraa Safa', 'Mechraa Safa', 14),
(463, '0463', 'مدريسة', 'Medrissa', 'Medrissa', 14),
(464, '0464', 'مدروسة', 'Medroussa', 'Medroussa', 14),
(465, '0465', 'مغيلة', 'Meghila', 'Meghila', 14),
(466, '0466', 'ملاكو', 'Mellakou', 'Mellakou', 14),
(467, '0467', 'الناظورة', 'Nadorah', 'Nadorah', 14),
(468, '0468', 'النعيمة', 'Naima', 'Naima', 14),
(469, '0469', 'وادي ليلي', 'Oued Lilli', 'Oued Lilli', 14),
(470, '0470', 'الرحوية', 'Rahouia', 'Rahouia', 14),
(471, '0471', 'الرشايقة', 'Rechaiga', 'Rechaiga', 14),
(472, '0472', 'السبعين', 'Sebaine', 'Sebaine', 14),
(473, '0473', 'السبت', 'Sebt', 'Sebt', 14),
(474, '0474', 'سرغين', 'Serghine', 'Serghine', 14),
(475, '0475', 'سي عبد الغني', 'Si Abdelghani', 'Si Abdelghani', 14),
(476, '0476', 'سيدي عبد الرحمن', 'Sidi Abderrahmane', 'Sidi Abderrahmane', 14),
(477, '0477', 'سيدي علي ملال', 'Sidi Ali Mellal', 'Sidi Ali Mellal', 14),
(478, '0478', 'سيدي بختي', 'Sidi Bakhti', 'Sidi Bakhti', 14),
(479, '0479', 'سيدي حسني', 'Sidi Hosni', 'Sidi Hosni', 14),
(480, '0480', 'السوقر', 'Sougueur', 'Sougueur', 14),
(481, '0481', 'تاقدمت', 'Tagdempt', 'Tagdempt', 14),
(482, '0482', 'تخمرت', 'Takhemaret', 'Takhemaret', 14),
(483, '0483', 'تيارت', 'Tiaret', 'Tiaret', 14),
(484, '0484', 'تيدة', 'Tidda', 'Tidda', 14),
(485, '0485', 'توسنينة', 'Tousnina', 'Tousnina', 14),
(486, '0486', 'زمالة  الأمير عبد القادر', 'Zmalet El Emir Abdelkade', 'Zmalet El Emir Abdelkade', 14),
(487, '0487', 'أبي يوسف', 'Abi-Youcef', 'Abi-Youcef', 15),
(488, '0488', 'أغريب', 'Aghribs', 'Aghribs', 15),
(489, '0489', 'أقني قغران', 'Agouni-Gueghrane', 'Agouni-Gueghrane', 15),
(490, '0490', 'عين الحمام', 'Ain-El-Hammam', 'Ain-El-Hammam', 15),
(491, '0491', 'عين الزاوية', 'Ain-Zaouia', 'Ain-Zaouia', 15),
(492, '0492', 'أيت عقـواشة', 'Ait Aggouacha', 'Ait Aggouacha', 15),
(493, '0493', 'أيت بــوادو', 'Ait Bouaddou', 'Ait Bouaddou', 15),
(494, '0494', 'أيت بومهدي', 'Ait Boumahdi', 'Ait Boumahdi', 15),
(495, '0495', 'أيت خليلي', 'Ait Khellili', 'Ait Khellili', 15),
(496, '0496', 'أيت يحي موسى', 'Ait Yahia Moussa', 'Ait Yahia Moussa', 15),
(497, '0497', 'أيت عيسى ميمون', 'Ait-Aissa-Mimoun', 'Ait-Aissa-Mimoun', 15),
(498, '0498', 'أيت شافع', 'Ait-Chafaa', 'Ait-Chafaa', 15),
(499, '0499', 'أيت محمود', 'Ait-Mahmoud', 'Ait-Mahmoud', 15),
(500, '0500', 'أيت  أومالو', 'Ait-Oumalou', 'Ait-Oumalou', 15),
(501, '0501', 'أيت تودرت', 'Ait-Toudert', 'Ait-Toudert', 15),
(502, '0502', 'أيت يحيى', 'Ait-Yahia', 'Ait-Yahia', 15),
(503, '0503', 'اقبيل', 'Akbil', 'Akbil', 15),
(504, '0504', 'أقرو', 'Akerrou', 'Akerrou', 15),
(505, '0505', 'أسي يوسف', 'Assi-Youcef', 'Assi-Youcef', 15),
(506, '0506', 'عزازقة', 'Azazga', 'Azazga', 15),
(507, '0507', 'أزفون', 'Azeffoun', 'Azeffoun', 15),
(508, '0508', 'بنــــي زمنزار', 'Beni Zmenzer', 'Beni Zmenzer', 15),
(509, '0509', 'بني عيسي', 'Beni-Aissi', 'Beni-Aissi', 15),
(510, '0510', 'بني دوالة', 'Beni-Douala', 'Beni-Douala', 15),
(511, '0511', 'بني يني', 'Beni-Yenni', 'Beni-Yenni', 15),
(512, '0512', 'بني زيكــي', 'Beni-Zikki', 'Beni-Zikki', 15),
(513, '0513', 'بوغني', 'Boghni', 'Boghni', 15),
(514, '0514', 'بوجيمة', 'Boudjima', 'Boudjima', 15),
(515, '0515', 'بونوح', 'Bounouh', 'Bounouh', 15),
(516, '0516', 'بوزقــن', 'Bouzeguene', 'Bouzeguene', 15),
(517, '0517', 'ذراع بن خدة', 'Draa-Ben-Khedda', 'Draa-Ben-Khedda', 15),
(518, '0518', 'ذراع الميزان', 'Draa-El-Mizan', 'Draa-El-Mizan', 15),
(519, '0519', 'فريحة', 'Freha', 'Freha', 15),
(520, '0520', 'فريقات', 'Frikat', 'Frikat', 15),
(521, '0521', 'إبودرارن', 'Iboudrarene', 'Iboudrarene', 15),
(522, '0522', 'إيجــار', 'Idjeur', 'Idjeur', 15),
(523, '0523', 'إفــرحــونان', 'Iferhounene', 'Iferhounene', 15),
(524, '0524', 'إيفيغاء', 'Ifigha', 'Ifigha', 15),
(525, '0525', 'إفليـــسن', 'Iflissen', 'Iflissen', 15),
(526, '0526', 'إيلـيــلتـن', 'Illilten', 'Illilten', 15),
(527, '0527', 'إيلولة أومـــالو', 'Illoula Oumalou', 'Illoula Oumalou', 15),
(528, '0528', 'إمســوحال', 'Imsouhal', 'Imsouhal', 15),
(529, '0529', 'إيرجـــن', 'Irdjen', 'Irdjen', 15),
(530, '0530', 'الأربعــاء ناث إيراثن', 'Larbaa Nath Irathen', 'Larbaa Nath Irathen', 15),
(531, '0531', 'معـــاتقة', 'Maatkas', 'Maatkas', 15),
(532, '0532', 'ماكودة', 'Makouda', 'Makouda', 15),
(533, '0533', 'مشطراس', 'Mechtras', 'Mechtras', 15),
(534, '0534', 'مقــلع', 'Mekla', 'Mekla', 15),
(535, '0535', 'ميزرانـــة', 'Mizrana', 'Mizrana', 15),
(536, '0536', 'مكيرة', 'M\'kira', 'M\'kira', 15),
(537, '0537', 'واسيف', 'Ouacif', 'Ouacif', 15),
(538, '0538', 'واضية', 'Ouadhias', 'Ouadhias', 15),
(539, '0539', 'واقنون', 'Ouaguenoun', 'Ouaguenoun', 15),
(540, '0540', 'سيدي نعمان', 'Sidi Namane', 'Sidi Namane', 15),
(541, '0541', 'صوامـــع', 'Souama', 'Souama', 15),
(542, '0542', 'سوق الإثنين', 'Souk-El-Tenine', 'Souk-El-Tenine', 15),
(543, '0543', 'تادمايت', 'Tadmait', 'Tadmait', 15),
(544, '0544', 'تيقـزيرت', 'Tigzirt', 'Tigzirt', 15),
(545, '0545', 'تيمـيزار', 'Timizart', 'Timizart', 15),
(546, '0546', 'تيرمتين', 'Tirmitine', 'Tirmitine', 15),
(547, '0547', 'تيزي نثلاثة', 'Tizi N\'tleta', 'Tizi N\'tleta', 15),
(548, '0548', 'تيزي غنيف', 'Tizi-Gheniff', 'Tizi-Gheniff', 15),
(549, '0549', 'تيزي وزو', 'Tizi-Ouzou', 'Tizi-Ouzou', 15),
(550, '0550', 'تيزي راشد', 'Tizi-Rached', 'Tizi-Rached', 15),
(551, '0551', 'إعــكورن', 'Yakourene', 'Yakourene', 15),
(552, '0552', 'يطــافن', 'Yatafene', 'Yatafene', 15),
(553, '0553', 'زكري', 'Zekri', 'Zekri', 15),
(554, '0554', 'عين بنيان', 'Ain Benian', 'Ain Benian', 16),
(555, '0555', 'عين طاية', 'Ain Taya', 'Ain Taya', 16),
(556, '0556', 'الجزائر الوسطى', 'Alger Centre', 'Alger Centre', 16),
(557, '0557', 'باب الوادي', 'Bab El Oued', 'Bab El Oued', 16),
(558, '0558', 'باب الزوار', 'Bab Ezzouar', 'Bab Ezzouar', 16),
(559, '0559', 'بابا حسن', 'Baba Hassen', 'Baba Hassen', 16),
(560, '0560', 'باش جراح', 'Bachedjerah', 'Bachedjerah', 16),
(561, '0561', 'براقي', 'Baraki', 'Baraki', 16),
(562, '0562', 'ابن عكنون', 'Ben Aknoun', 'Ben Aknoun', 16),
(563, '0563', 'بني مسوس', 'Beni Messous', 'Beni Messous', 16),
(564, '0564', 'بئر مراد رايس', 'Bir Mourad Rais', 'Bir Mourad Rais', 16),
(565, '0565', 'بئر خادم', 'Birkhadem', 'Birkhadem', 16),
(566, '0566', 'بئر توتة', 'Bir Touta', 'Bir Touta', 16),
(567, '0567', 'بولوغين بن زيري', 'Bologhine Ibnou Ziri', 'Bologhine Ibnou Ziri', 16),
(568, '0568', 'برج البحري', 'Bordj El Bahri', 'Bordj El Bahri', 16),
(569, '0569', 'برج الكيفان', 'Bordj El Kiffan', 'Bordj El Kiffan', 16),
(570, '0570', 'بوروبة', 'Bourouba', 'Bourouba', 16),
(571, '0571', 'بوزريعة', 'Bouzareah', 'Bouzareah', 16),
(572, '0572', 'القصبة', 'Casbah', 'Casbah', 16),
(573, '0573', 'الشراقة', 'Cheraga', 'Cheraga', 16),
(574, '0574', 'الدار البيضاء', 'Dar El Beida', 'Dar El Beida', 16),
(575, '0575', 'دالي ابراهيم', 'Dely Ibrahim', 'Dely Ibrahim', 16),
(576, '0576', 'جسر قسنطينة', 'Djasr Kasentina', 'Djasr Kasentina', 16),
(577, '0577', 'الدويرة', 'Douira', 'Douira', 16),
(578, '0578', 'الدرارية', 'Draria', 'Draria', 16),
(579, '0579', 'العاشور', 'El Achour', 'El Achour', 16),
(580, '0580', 'الابيار', 'El Biar', 'El Biar', 16),
(581, '0581', 'الحراش', 'El Harrach', 'El Harrach', 16),
(582, '0582', 'المدنية', 'El Madania', 'El Madania', 16),
(583, '0583', 'المغارية', 'El Magharia', 'El Magharia', 16),
(584, '0584', 'المرسى', 'El Marsa', 'El Marsa', 16),
(585, '0585', 'المرادية', 'El Mouradia', 'El Mouradia', 16),
(586, '0586', 'الحمامات', 'Hammamet', 'Hammamet', 16),
(587, '0587', 'هراوة', 'Herraoua', 'Herraoua', 16),
(588, '0588', 'حسين داي', 'Hussein Dey', 'Hussein Dey', 16),
(589, '0589', 'حيدرة', 'Hydra', 'Hydra', 16),
(590, '0590', 'الخرايسية', 'Khraissia', 'Khraissia', 16),
(591, '0591', 'القبة', 'Kouba', 'Kouba', 16),
(592, '0592', 'الكاليتوس', 'Les Eucalyptus', 'Les Eucalyptus', 16),
(593, '0593', 'المعالمة', 'Maalma', 'Maalma', 16),
(594, '0594', 'محمد بلوزداد', 'Mohamed Belouzdad', 'Mohamed Belouzdad', 16),
(595, '0595', 'المحمدية', 'Mohammadia', 'Mohammadia', 16),
(596, '0596', 'وادي قريش', 'Oued Koriche', 'Oued Koriche', 16),
(597, '0597', 'وادي السمار', 'Oued Smar', 'Oued Smar', 16),
(598, '0598', 'اولاد شبل', 'Ouled Chebel', 'Ouled Chebel', 16),
(599, '0599', 'اولاد فايت', 'Ouled Fayet', 'Ouled Fayet', 16),
(600, '0600', 'الرحمانية', 'Rahmania', 'Rahmania', 16),
(601, '0601', 'الرايس حميدو', 'Rais Hamidou', 'Rais Hamidou', 16),
(602, '0602', 'رغاية', 'Reghaia', 'Reghaia', 16),
(603, '0603', 'الرويبة', 'Rouiba', 'Rouiba', 16),
(604, '0604', 'السحاولة', 'Sehaoula', 'Sehaoula', 16),
(605, '0605', 'سيدي امحمد', 'Sidi M\'hamed', 'Sidi M\'hamed', 16),
(606, '0606', 'سيدي موسى', 'Sidi Moussa', 'Sidi Moussa', 16),
(607, '0607', 'سويدانية', 'Souidania', 'Souidania', 16),
(608, '0608', 'سطاوالي', 'Staoueli', 'Staoueli', 16),
(609, '0609', 'تسالة المرجة', 'Tessala El Merdja', 'Tessala El Merdja', 16),
(610, '0610', 'زرالدة', 'Zeralda', 'Zeralda', 16),
(611, '0611', 'عين الشهداء', 'Ain Chouhada', 'Ain Chouhada', 17),
(612, '0612', 'عين الإبل', 'Ain El Ibel', 'Ain El Ibel', 17),
(613, '0613', 'عين فقه', 'Ain Fekka', 'Ain Fekka', 17),
(614, '0614', 'عين معبد', 'Ain Maabed', 'Ain Maabed', 17),
(615, '0615', 'عين وسارة', 'Ain Oussera', 'Ain Oussera', 17),
(616, '0616', 'عمورة', 'Amourah', 'Amourah', 17),
(617, '0617', 'بنهار', 'Benhar', 'Benhar', 17),
(618, '0618', 'بن يعقوب', 'Benyagoub', 'Benyagoub', 17),
(619, '0619', 'بيرين', 'Birine', 'Birine', 17),
(620, '0620', 'بويرة الأحداب', 'Bouira Lahdab', 'Bouira Lahdab', 17),
(621, '0621', 'الشارف', 'Charef', 'Charef', 17),
(622, '0622', 'دار الشيوخ', 'Dar Chioukh', 'Dar Chioukh', 17),
(623, '0623', 'دلدول', 'Deldoul', 'Deldoul', 17),
(624, '0624', 'الجلفة', 'Djelfa', 'Djelfa', 17),
(625, '0625', 'دويس', 'Douis', 'Douis', 17),
(626, '0626', 'القديد', 'El Guedid', 'El Guedid', 17),
(627, '0627', 'الادريسية', 'El Idrissia', 'El Idrissia', 17),
(628, '0628', 'الخميس', 'El Khemis', 'El Khemis', 17),
(629, '0629', 'فيض البطمة', 'Faidh El Botma', 'Faidh El Botma', 17),
(630, '0630', 'قرنيني', 'Guernini', 'Guernini', 17),
(631, '0631', 'قطارة', 'Guettara', 'Guettara', 17),
(632, '0632', 'حد الصحاري', 'Had Sahary', 'Had Sahary', 17),
(633, '0633', 'حاسي بحبح', 'Hassi Bahbah', 'Hassi Bahbah', 17),
(634, '0634', 'حاسي العش', 'Hassi El Euch', 'Hassi El Euch', 17),
(635, '0635', 'حاسي فدول', 'Hassi Fedoul', 'Hassi Fedoul', 17),
(636, '0636', 'مسعد', 'Messaad', 'Messaad', 17),
(637, '0637', 'مليليحة', 'M\'liliha', 'M\'liliha', 17),
(638, '0638', 'مجبارة', 'Moudjebara', 'Moudjebara', 17),
(639, '0639', 'أم العظام', 'Oum Laadham', 'Oum Laadham', 17),
(640, '0640', 'سد الرحال', 'Sed Rahal', 'Sed Rahal', 17),
(641, '0641', 'سلمانة', 'Selmana', 'Selmana', 17),
(642, '0642', 'سيدي بايزيد', 'Sidi Baizid', 'Sidi Baizid', 17),
(643, '0643', 'سيدي لعجال', 'Sidi Laadjel', 'Sidi Laadjel', 17),
(644, '0644', 'تعظميت', 'Taadmit', 'Taadmit', 17),
(645, '0645', 'زعفران', 'Zaafrane', 'Zaafrane', 17),
(646, '0646', 'زكار', 'Zaccar', 'Zaccar', 17),
(647, '0647', 'برج الطهر', 'Bordj T\'har', 'Bordj T\'har', 18),
(648, '0648', 'بودريعة بني  ياجيس', 'Boudria Beniyadjis', 'Boudria Beniyadjis', 18),
(649, '0649', 'بوراوي بلهادف', 'Bouraoui Belhadef', 'Bouraoui Belhadef', 18),
(650, '0650', 'بوسيف أولاد عسكر', 'Boussif Ouled Askeur', 'Boussif Ouled Askeur', 18),
(651, '0651', 'الشحنة', 'Chahna', 'Chahna', 18),
(652, '0652', 'الشقفة', 'Chekfa', 'Chekfa', 18),
(653, '0653', 'الجمعة بني حبيبي', 'Djemaa Beni Habibi', 'Djemaa Beni Habibi', 18),
(654, '0654', 'جيملة', 'Djimla', 'Djimla', 18),
(655, '0655', 'العنصر', 'El Ancer', 'El Ancer', 18),
(656, '0656', 'العوانة', 'El Aouana', 'El Aouana', 18),
(657, '0657', 'القنار نشفي', 'El Kennar Nouchfi', 'El Kennar Nouchfi', 18),
(658, '0658', 'الميلية', 'El Milia', 'El Milia', 18),
(659, '0659', 'الامير عبد القادر', 'Emir Abdelkader', 'Emir Abdelkader', 18),
(660, '0660', 'أراقن سويسي', 'Erraguene Souissi', 'Erraguene Souissi', 18),
(661, '0661', 'غبالة', 'Ghebala', 'Ghebala', 18),
(662, '0662', 'جيجل', 'Jijel', 'Jijel', 18),
(663, '0663', 'قاوس', 'Kaous', 'Kaous', 18),
(664, '0664', 'خيري واد عجول', 'Khiri Oued Adjoul', 'Khiri Oued Adjoul', 18),
(665, '0665', 'وجانة', 'Oudjana', 'Oudjana', 18),
(666, '0666', 'أولاد رابح', 'Ouled Rabah', 'Ouled Rabah', 18),
(667, '0667', 'أولاد يحيى خدروش', 'Ouled Yahia Khadrouch', 'Ouled Yahia Khadrouch', 18),
(668, '0668', 'سلمى بن زيادة', 'Selma Benziada', 'Selma Benziada', 18),
(669, '0669', 'السطارة', 'Settara', 'Settara', 18),
(670, '0670', 'سيدي عبد العزيز', 'Sidi Abdelaziz', 'Sidi Abdelaziz', 18),
(671, '0671', 'سيدي معروف', 'Sidi Marouf', 'Sidi Marouf', 18),
(672, '0672', 'الطاهير', 'Taher', 'Taher', 18),
(673, '0673', 'تاكسنة', 'Texenna', 'Texenna', 18),
(674, '0674', 'زيامة منصورية', 'Ziama Mansouriah', 'Ziama Mansouriah', 18),
(675, '0675', 'عين عباسة', 'Ain Abessa', 'Ain Abessa', 19),
(676, '0676', 'عين أرنات', 'Ain Arnat', 'Ain Arnat', 19),
(677, '0677', 'عين أزال', 'Ain Azel', 'Ain Azel', 19),
(678, '0678', 'عين الكبيرة', 'Ain El Kebira', 'Ain El Kebira', 19),
(679, '0679', 'عين الحجر', 'Ain Lahdjar', 'Ain Lahdjar', 19),
(680, '0680', 'عين ولمان', 'Ain Oulmene', 'Ain Oulmene', 19),
(681, '0681', 'عين لقراج', 'Ain-Legradj', 'Ain-Legradj', 19),
(682, '0682', 'عين الروى', 'Ain-Roua', 'Ain-Roua', 19),
(683, '0683', 'عين السبت', 'Ain-Sebt', 'Ain-Sebt', 19),
(684, '0684', 'أيت نوال مزادة', 'Ait Naoual Mezada', 'Ait Naoual Mezada', 19),
(685, '0685', 'ايت تيزي', 'Ait-Tizi', 'Ait-Tizi', 19),
(686, '0686', 'عموشة', 'Amoucha', 'Amoucha', 19),
(687, '0687', 'بابور', 'Babor', 'Babor', 19),
(688, '0688', 'بازر سكرة', 'Bazer-Sakra', 'Bazer-Sakra', 19),
(689, '0689', 'بيضاء برج', 'Beidha Bordj', 'Beidha Bordj', 19),
(690, '0690', 'بلاعة', 'Bellaa', 'Bellaa', 19),
(691, '0691', 'بني شبانة', 'Beni Chebana', 'Beni Chebana', 19),
(692, '0692', 'بني فودة', 'Beni Fouda', 'Beni Fouda', 19),
(693, '0693', 'بني ورتيلان', 'Beni Ourtilane', 'Beni Ourtilane', 19),
(694, '0694', 'بني وسين', 'Beni Oussine', 'Beni Oussine', 19),
(695, '0695', 'بني عزيز', 'Beni-Aziz', 'Beni-Aziz', 19),
(696, '0696', 'بني موحلي', 'Beni-Mouhli', 'Beni-Mouhli', 19),
(697, '0697', 'بئر حدادة', 'Bir Haddada', 'Bir Haddada', 19),
(698, '0698', 'بئر العرش', 'Bir-El-Arch', 'Bir-El-Arch', 19),
(699, '0699', 'بوعنداس', 'Bouandas', 'Bouandas', 19),
(700, '0700', 'بوقاعة', 'Bougaa', 'Bougaa', 19),
(701, '0701', 'بوسلام', 'Bousselam', 'Bousselam', 19),
(702, '0702', 'بوطالب', 'Boutaleb', 'Boutaleb', 19),
(703, '0703', 'الدهامشة', 'Dehamcha', 'Dehamcha', 19),
(704, '0704', 'جميلة', 'Djemila', 'Djemila', 19),
(705, '0705', 'ذراع قبيلة', 'Draa-Kebila', 'Draa-Kebila', 19),
(706, '0706', 'العلمة', 'El Eulma', 'El Eulma', 19),
(707, '0707', 'أوريسيا', 'El Ouricia', 'El Ouricia', 19),
(708, '0708', 'الولجة', 'El-Ouldja', 'El-Ouldja', 19),
(709, '0709', 'قلال', 'Guellal', 'Guellal', 19),
(710, '0710', 'قلتة زرقاء', 'Guelta Zerka', 'Guelta Zerka', 19),
(711, '0711', 'قنزات', 'Guenzet', 'Guenzet', 19),
(712, '0712', 'قجال', 'Guidjel', 'Guidjel', 19),
(713, '0713', 'حمام السخنة', 'Hamam Soukhna', 'Hamam Soukhna', 19),
(714, '0714', 'الحامة', 'Hamma', 'Hamma', 19),
(715, '0715', 'حمام قرقور', 'Hammam Guergour', 'Hammam Guergour', 19),
(716, '0716', 'حربيل', 'Harbil', 'Harbil', 19),
(717, '0717', 'قصر الابطال', 'Kasr El Abtal', 'Kasr El Abtal', 19),
(718, '0718', 'معاوية', 'Maaouia', 'Maaouia', 19),
(719, '0719', 'ماوكلان', 'Maouaklane', 'Maouaklane', 19),
(720, '0720', 'مزلوق', 'Mezloug', 'Mezloug', 19),
(721, '0721', 'واد البارد', 'Oued El Bared', 'Oued El Bared', 19),
(722, '0722', 'أولاد عدوان', 'Ouled Addouane', 'Ouled Addouane', 19),
(723, '0723', 'أولاد صابر', 'Ouled Sabor', 'Ouled Sabor', 19),
(724, '0724', 'أولاد سي أحمد', 'Ouled Si Ahmed', 'Ouled Si Ahmed', 19),
(725, '0725', 'أولاد تبان', 'Ouled Tebben', 'Ouled Tebben', 19),
(726, '0726', 'الرصفة', 'Rosfa', 'Rosfa', 19),
(727, '0727', 'صالح باي', 'Salah Bey', 'Salah Bey', 19),
(728, '0728', 'سرج الغول', 'Serdj-El-Ghoul', 'Serdj-El-Ghoul', 19),
(729, '0729', 'سطيف', 'Setif', 'Setif', 19),
(730, '0730', 'تاشودة', 'Tachouda', 'Tachouda', 19),
(731, '0731', 'تالة إيفاسن', 'Tala-Ifacene', 'Tala-Ifacene', 19),
(732, '0732', 'الطاية', 'Taya', 'Taya', 19),
(733, '0733', 'التلة', 'Tella', 'Tella', 19),
(734, '0734', 'تيزي نبشار', 'Tizi N\'bechar', 'Tizi N\'bechar', 19),
(735, '0735', 'عين الحجر', 'Ain El Hadjar', 'Ain El Hadjar', 20),
(736, '0736', 'عين السخونة', 'Ain Sekhouna', 'Ain Sekhouna', 20),
(737, '0737', 'عين السلطان', 'Ain Soltane', 'Ain Soltane', 20),
(738, '0738', 'دوي ثابت', 'Doui Thabet', 'Doui Thabet', 20),
(739, '0739', 'الحساسنة', 'El Hassasna', 'El Hassasna', 20),
(740, '0740', 'هونت', 'Hounet', 'Hounet', 20),
(741, '0741', 'المعمورة', 'Maamora', 'Maamora', 20),
(742, '0742', 'مولاي العربي', 'Moulay Larbi', 'Moulay Larbi', 20),
(743, '0743', 'أولاد إبراهيم', 'Ouled Brahim', 'Ouled Brahim', 20),
(744, '0744', 'أولاد خالد', 'Ouled Khaled', 'Ouled Khaled', 20),
(745, '0745', 'سعيدة', 'Saida', 'Saida', 20),
(746, '0746', 'سيدي احمد', 'Sidi Ahmed', 'Sidi Ahmed', 20),
(747, '0747', 'سيدي عمر', 'Sidi Amar', 'Sidi Amar', 20),
(748, '0748', 'سيدي بوبكر', 'Sidi Boubekeur', 'Sidi Boubekeur', 20),
(749, '0749', 'تيرسين', 'Tircine', 'Tircine', 20),
(750, '0750', 'يوب', 'Youb', 'Youb', 20),
(751, '0751', 'عين بوزيان', 'Ain Bouziane', 'Ain Bouziane', 21),
(752, '0752', 'عين شرشار', 'Ain Charchar', 'Ain Charchar', 21),
(753, '0753', 'عين قشرة', 'Ain Kechra', 'Ain Kechra', 21),
(754, '0754', 'عين زويت', 'Ain Zouit', 'Ain Zouit', 21),
(755, '0755', 'عزابة', 'Azzaba', 'Azzaba', 21),
(756, '0756', 'بكوش لخضر', 'Bekkouche Lakhdar', 'Bekkouche Lakhdar', 21),
(757, '0757', 'بن عزوز', 'Ben Azzouz', 'Ben Azzouz', 21),
(758, '0758', 'بني بشير', 'Beni Bechir', 'Beni Bechir', 21),
(759, '0759', 'بني ولبان', 'Beni Oulbane', 'Beni Oulbane', 21),
(760, '0760', 'بني زيد', 'Beni Zid', 'Beni Zid', 21),
(761, '0761', 'بين الويدان', 'Bin El Ouiden', 'Bin El Ouiden', 21),
(762, '0762', 'بوشطاطة', 'Bouchetata', 'Bouchetata', 21),
(763, '0763', 'الشرايع', 'Cheraia', 'Cheraia', 21),
(764, '0764', 'القل', 'Collo', 'Collo', 21),
(765, '0765', 'جندل سعدي محمد', 'Djendel Saadi Mohamed', 'Djendel Saadi Mohamed', 21),
(766, '0766', 'الحروش', 'El Arrouch', 'El Arrouch', 21),
(767, '0767', 'الغدير', 'El Ghedir', 'El Ghedir', 21),
(768, '0768', 'الحدائق', 'El Hadaiek', 'El Hadaiek', 21),
(769, '0769', 'المرسى', 'El Marsa', 'El Marsa', 21),
(770, '0770', 'مجاز الدشيش', 'Emjez Edchich', 'Emjez Edchich', 21),
(771, '0771', 'السبت', 'Es Sebt', 'Es Sebt', 21),
(772, '0772', 'فلفلة', 'Filfila', 'Filfila', 21),
(773, '0773', 'حمادي كرومة', 'Hammadi Krouma', 'Hammadi Krouma', 21),
(774, '0774', 'قنواع', 'Kanoua', 'Kanoua', 21),
(775, '0775', 'الكركرة', 'Kerkara', 'Kerkara', 21),
(776, '0776', 'خناق مايو', 'Khenag Maoune', 'Khenag Maoune', 21),
(777, '0777', 'وادي الزهور', 'Oued Zhour', 'Oued Zhour', 21),
(778, '0778', 'الولجة بولبلوط', 'Ouldja Boulbalout', 'Ouldja Boulbalout', 21),
(779, '0779', 'أولاد عطية', 'Ouled Attia', 'Ouled Attia', 21),
(780, '0780', 'أولاد حبابة', 'Ouled Habbaba', 'Ouled Habbaba', 21),
(781, '0781', 'أم الطوب', 'Oum Toub', 'Oum Toub', 21),
(782, '0782', 'رمضان جمال', 'Ramdane Djamel', 'Ramdane Djamel', 21),
(783, '0783', 'صالح بو الشعور', 'Salah Bouchaour', 'Salah Bouchaour', 21),
(784, '0784', 'سيدي مزغيش', 'Sidi Mezghiche', 'Sidi Mezghiche', 21),
(785, '0785', 'سكيكدة', 'Skikda', 'Skikda', 21),
(786, '0786', 'تمالوس', 'Tamalous', 'Tamalous', 21),
(787, '0787', 'زردازة', 'Zerdezas', 'Zerdezas', 21),
(788, '0788', 'الزيتونة', 'Zitouna', 'Zitouna', 21),
(789, '0789', 'عين البرد', 'Ain El Berd', 'Ain El Berd', 22),
(790, '0790', 'عين قادة', 'Ain Kada', 'Ain Kada', 22),
(791, '0791', 'عين الثريد', 'Ain Thrid', 'Ain Thrid', 22),
(792, '0792', 'عين تندمين', 'Ain Tindamine', 'Ain Tindamine', 22),
(793, '0793', 'عين أدن', 'Ain- Adden', 'Ain- Adden', 22),
(794, '0794', 'العمارنة', 'Amarnas', 'Amarnas', 22),
(795, '0795', 'بضرابين المقراني', 'Bedrabine El Mokrani', 'Bedrabine El Mokrani', 22),
(796, '0796', 'بلعربي', 'Belarbi', 'Belarbi', 22),
(797, '0797', 'بن باديس', 'Ben Badis', 'Ben Badis', 22),
(798, '0798', 'بن عشيبة شلية', 'Benachiba Chelia', 'Benachiba Chelia', 22),
(799, '0799', 'بئر الحمام', 'Bir El Hammam', 'Bir El Hammam', 22),
(800, '0800', 'بوجبهة البرج', 'Boudjebaa El Bordj', 'Boudjebaa El Bordj', 22),
(801, '0801', 'بوخنفيس', 'Boukhanefis', 'Boukhanefis', 22),
(802, '0802', 'شيطوان البلايلة', 'Chetouane Belaila', 'Chetouane Belaila', 22),
(803, '0803', 'الضاية', 'Dhaya', 'Dhaya', 22),
(804, '0804', 'الحصيبة', 'El Hacaiba', 'El Hacaiba', 22),
(805, '0805', 'حاسي دحو', 'Hassi Dahou', 'Hassi Dahou', 22),
(806, '0806', 'حاسي زهانة', 'Hassi Zahana', 'Hassi Zahana', 22),
(807, '0807', 'لمطار', 'Lamtar', 'Lamtar', 22),
(808, '0808', 'مكدرة', 'Makedra', 'Makedra', 22),
(809, '0809', 'مرحوم', 'Marhoum', 'Marhoum', 22),
(810, '0810', 'مسيد', 'M\'cid', 'M\'cid', 22),
(811, '0811', 'مرين', 'Merine', 'Merine', 22),
(812, '0812', 'مزاورو', 'Mezaourou', 'Mezaourou', 22),
(813, '0813', 'مصطفى بن ابراهيم', 'Mostefa  Ben Brahim', 'Mostefa  Ben Brahim', 22),
(814, '0814', 'مولاي سليسن', 'Moulay Slissen', 'Moulay Slissen', 22),
(815, '0815', 'وادي السبع', 'Oued Sebaa', 'Oued Sebaa', 22),
(816, '0816', 'وادي سفيون', 'Oued Sefioun', 'Oued Sefioun', 22),
(817, '0817', 'وادي تاوريرة', 'Oued Taourira', 'Oued Taourira', 22),
(818, '0818', 'راس الماء', 'Ras El Ma', 'Ras El Ma', 22),
(819, '0819', 'رجم دموش', 'Redjem Demouche', 'Redjem Demouche', 22),
(820, '0820', 'السهالة الثورة', 'Sehala Thaoura', 'Sehala Thaoura', 22),
(821, '0821', 'سفيزف', 'Sfisef', 'Sfisef', 22),
(822, '0822', 'سيدي علي بن يوب', 'Sidi Ali Benyoub', 'Sidi Ali Benyoub', 22),
(823, '0823', 'سيدي علي بوسيدي', 'Sidi Ali Boussidi', 'Sidi Ali Boussidi', 22),
(824, '0824', 'سيدي بلعباس', 'Sidi Bel-Abbes', 'Sidi Bel-Abbes', 22),
(825, '0825', 'سيدي ابراهيم', 'Sidi Brahim', 'Sidi Brahim', 22),
(826, '0826', 'سيدي شعيب', 'Sidi Chaib', 'Sidi Chaib', 22),
(827, '0827', 'سيدي دحو الزاير', 'Sidi Dahou Zairs', 'Sidi Dahou Zairs', 22),
(828, '0828', 'سيدي حمادوش', 'Sidi Hamadouche', 'Sidi Hamadouche', 22),
(829, '0829', 'سيدي خالد', 'Sidi Khaled', 'Sidi Khaled', 22),
(830, '0830', 'سيدي لحسن', 'Sidi Lahcene', 'Sidi Lahcene', 22),
(831, '0831', 'سيدي يعقوب', 'Sidi Yacoub', 'Sidi Yacoub', 22),
(832, '0832', 'طابية', 'Tabia', 'Tabia', 22),
(833, '0833', 'تاودموت', 'Taoudmout', 'Taoudmout', 22),
(834, '0834', 'تفسور', 'Tefessour', 'Tefessour', 22),
(835, '0835', 'تغاليمت', 'Teghalimet', 'Teghalimet', 22),
(836, '0836', 'تلاغ', 'Telagh', 'Telagh', 22),
(837, '0837', 'تنيرة', 'Tenira', 'Tenira', 22),
(838, '0838', 'تسالة', 'Tessala', 'Tessala', 22),
(839, '0839', 'تلموني', 'Tilmouni', 'Tilmouni', 22),
(840, '0840', 'زروالة', 'Zerouala', 'Zerouala', 22),
(841, '0841', 'عين الباردة', 'Ain El Berda', 'Ain El Berda', 23),
(842, '0842', 'عنابة', 'Annaba', 'Annaba', 23),
(843, '0843', 'برحال', 'Berrahal', 'Berrahal', 23),
(844, '0844', 'شطايبي', 'Chetaibi', 'Chetaibi', 23),
(845, '0845', 'الشرفة', 'Cheurfa', 'Cheurfa', 23),
(846, '0846', 'البوني', 'El Bouni', 'El Bouni', 23),
(847, '0847', 'العلمة', 'El Eulma', 'El Eulma', 23),
(848, '0848', 'الحجار', 'El Hadjar', 'El Hadjar', 23),
(849, '0849', 'واد العنب', 'Oued El Aneb', 'Oued El Aneb', 23),
(850, '0850', 'سرايدي', 'Seraidi', 'Seraidi', 23),
(851, '0851', 'سيدي عمار', 'Sidi Amar', 'Sidi Amar', 23),
(852, '0852', 'التريعات', 'Treat', 'Treat', 23),
(853, '0853', 'عين بن بيضاء', 'Ain Ben Beida', 'Ain Ben Beida', 24),
(854, '0854', 'عين العربي', 'Ain Larbi', 'Ain Larbi', 24),
(855, '0855', 'عين مخلوف', 'Ain Makhlouf', 'Ain Makhlouf', 24),
(856, '0856', 'عين رقادة', 'Ain Regada', 'Ain Regada', 24),
(857, '0857', 'عين صندل', 'Ain Sandel', 'Ain Sandel', 24),
(858, '0858', 'بلخير', 'Belkheir', 'Belkheir', 24),
(859, '0859', 'بن جراح', 'Bendjarah', 'Bendjarah', 24),
(860, '0860', 'بني مزلين', 'Beni Mezline', 'Beni Mezline', 24),
(861, '0861', 'برج صباط', 'Bordj Sabath', 'Bordj Sabath', 24),
(862, '0862', 'بوحشانة', 'Bou Hachana', 'Bou Hachana', 24),
(863, '0863', 'بوحمدان', 'Bou Hamdane', 'Bou Hamdane', 24),
(864, '0864', 'بوعاتي محمود', 'Bouati Mahmoud', 'Bouati Mahmoud', 24),
(865, '0865', 'بوشقوف', 'Bouchegouf', 'Bouchegouf', 24),
(866, '0866', 'بومهرة أحمد', 'Boumahra Ahmed', 'Boumahra Ahmed', 24),
(867, '0867', 'الدهوارة', 'Dahouara', 'Dahouara', 24),
(868, '0868', 'جبالة الخميسي', 'Djeballah Khemissi', 'Djeballah Khemissi', 24),
(869, '0869', 'الفجوج', 'El Fedjoudj', 'El Fedjoudj', 24),
(870, '0870', 'قلعة بوصبع', 'Guelaat Bou Sbaa', 'Guelaat Bou Sbaa', 24),
(871, '0871', 'قالمة', 'Guelma', 'Guelma', 24),
(872, '0872', 'حمام دباغ', 'Hammam Debagh', 'Hammam Debagh', 24),
(873, '0873', 'حمام النبايل', 'Hammam N\'bail', 'Hammam N\'bail', 24),
(874, '0874', 'هيليوبوليس', 'Heliopolis', 'Heliopolis', 24),
(875, '0875', 'نشماية', 'Nechmaya', 'Nechmaya', 24),
(876, '0876', 'لخزارة', 'Khezaras', 'Khezaras', 24),
(877, '0877', 'مجاز عمار', 'Medjez Amar', 'Medjez Amar', 24),
(878, '0878', 'مجاز الصفاء', 'Medjez Sfa', 'Medjez Sfa', 24),
(879, '0879', 'هواري بومدين', 'Houari Boumedienne', 'Houari Boumedienne', 24),
(880, '0880', 'وادي الشحم', 'Oued Cheham', 'Oued Cheham', 24),
(881, '0881', 'وادي فراغة', 'Oued Ferragha', 'Oued Ferragha', 24),
(882, '0882', 'وادي الزناتي', 'Oued Zenati', 'Oued Zenati', 24),
(883, '0883', 'رأس العقبة', 'Ras El Agba', 'Ras El Agba', 24),
(884, '0884', 'الركنية', 'Roknia', 'Roknia', 24),
(885, '0885', 'سلاوة عنونة', 'Sellaoua Announa', 'Sellaoua Announa', 24),
(886, '0886', 'تاملوكة', 'Tamlouka', 'Tamlouka', 24),
(887, '0887', 'عين عبيد', 'Ain Abid', 'Ain Abid', 25),
(888, '0888', 'عين السمارة', 'Ain Smara', 'Ain Smara', 25),
(889, '0889', 'أبن باديس الهرية', 'Ben Badis', 'Ben Badis', 25),
(890, '0890', 'بني حميدان', 'Beni Hamidane', 'Beni Hamidane', 25),
(891, '0891', 'قسنطينة', 'Constantine', 'Constantine', 25),
(892, '0892', 'ديدوش مراد', 'Didouche Mourad', 'Didouche Mourad', 25),
(893, '0893', 'الخروب', 'El Khroub', 'El Khroub', 25),
(894, '0894', 'حامة بوزيان', 'Hamma Bouziane', 'Hamma Bouziane', 25),
(895, '0895', 'ابن زياد', 'Ibn Ziad', 'Ibn Ziad', 25);
INSERT INTO `t_01_02_04` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(896, '0896', 'بوجريو مسعود', 'Messaoud Boudjeriou', 'Messaoud Boudjeriou', 25),
(897, '0897', 'أولاد رحمون', 'Ouled Rahmoun', 'Ouled Rahmoun', 25),
(898, '0898', 'زيغود يوسف', 'Zighoud Youcef', 'Zighoud Youcef', 25),
(899, '0899', 'عين بوسيف', 'Ain Boucif', 'Ain Boucif', 26),
(900, '0900', 'عين اقصير', 'Ain Ouksir', 'Ain Ouksir', 26),
(901, '0901', 'العيساوية', 'Aissaouia', 'Aissaouia', 26),
(902, '0902', 'عزيز', 'Aziz', 'Aziz', 26),
(903, '0903', 'بعطة', 'Baata', 'Baata', 26),
(904, '0904', 'بن شكاو', 'Ben Chicao', 'Ben Chicao', 26),
(905, '0905', 'بني سليمان', 'Beni Slimane', 'Beni Slimane', 26),
(906, '0906', 'البرواقية', 'Berrouaghia', 'Berrouaghia', 26),
(907, '0907', 'بئر بن عابد', 'Bir Ben Laabed', 'Bir Ben Laabed', 26),
(908, '0908', 'بوغار', 'Boghar', 'Boghar', 26),
(909, '0909', 'بوعيش', 'Bouaiche', 'Bouaiche', 26),
(910, '0910', 'بوعيشون', 'Bouaichoune', 'Bouaichoune', 26),
(911, '0911', 'بوشراحيل', 'Bouchrahil', 'Bouchrahil', 26),
(912, '0912', 'بوغزول', 'Boughzoul', 'Boughzoul', 26),
(913, '0913', 'بوسكن', 'Bouskene', 'Bouskene', 26),
(914, '0914', 'الشهبونية', 'Chabounia', 'Chabounia', 26),
(915, '0915', 'شلالة العذاورة', 'Chelalet El Adhaoura', 'Chelalet El Adhaoura', 26),
(916, '0916', 'شنيقل', 'Cheniguel', 'Cheniguel', 26),
(917, '0917', 'دراق', 'Derrag', 'Derrag', 26),
(918, '0918', 'جواب', 'Djouab', 'Djouab', 26),
(919, '0919', 'ذراع السمار', 'Draa Esmar', 'Draa Esmar', 26),
(920, '0920', 'العزيزية', 'El Azizia', 'El Azizia', 26),
(921, '0921', 'القلب الكبير', 'El Guelbelkebir', 'El Guelbelkebir', 26),
(922, '0922', 'الحمدانية', 'El Hamdania', 'El Hamdania', 26),
(923, '0923', 'الحوضان', 'El Haoudane', 'El Haoudane', 26),
(924, '0924', 'العمارية', 'El Omaria', 'El Omaria', 26),
(925, '0925', 'العوينات', 'El Ouinet', 'El Ouinet', 26),
(926, '0926', 'حناشة', 'Hannacha', 'Hannacha', 26),
(927, '0927', 'الكاف الاخضر', 'Kef Lakhdar', 'Kef Lakhdar', 26),
(928, '0928', 'خمس جوامع', 'Khams Djouamaa', 'Khams Djouamaa', 26),
(929, '0929', 'قصر البخاري', 'Ksar El Boukhari', 'Ksar El Boukhari', 26),
(930, '0930', 'مغراوة', 'Maghraoua', 'Maghraoua', 26),
(931, '0931', 'المدية', 'Medea', 'Medea', 26),
(932, '0932', 'مجبر', 'Medjebar', 'Medjebar', 26),
(933, '0933', 'مزغنة', 'Mezerana', 'Mezerana', 26),
(934, '0934', 'مفاتحة', 'M\'fatha', 'M\'fatha', 26),
(935, '0935', 'ميهوب', 'Mihoub', 'Mihoub', 26),
(936, '0936', 'عوامري', 'Ouamri', 'Ouamri', 26),
(937, '0937', 'وادي حربيل', 'Oued Harbil', 'Oued Harbil', 26),
(938, '0938', 'أولاد عنتر', 'Ouled Antar', 'Ouled Antar', 26),
(939, '0939', 'أولاد بوعشرة', 'Ouled Bouachra', 'Ouled Bouachra', 26),
(940, '0940', 'أولاد إبراهيم', 'Ouled Brahim', 'Ouled Brahim', 26),
(941, '0941', 'أولاد دايد', 'Ouled Deid', 'Ouled Deid', 26),
(942, '0942', 'أولاد امعرف', 'Ouled Emaaraf', 'Ouled Emaaraf', 26),
(943, '0943', 'أولاد هلال', 'Ouled Hellal', 'Ouled Hellal', 26),
(944, '0944', 'أم الجليل', 'Oum El Djellil', 'Oum El Djellil', 26),
(945, '0945', 'وزرة', 'Ouzera', 'Ouzera', 26),
(946, '0946', 'الربعية', 'Rebaia', 'Rebaia', 26),
(947, '0947', 'السانق', 'Saneg', 'Saneg', 26),
(948, '0948', 'سدراية', 'Sedraya', 'Sedraya', 26),
(949, '0949', 'سغوان', 'Seghouane', 'Seghouane', 26),
(950, '0950', 'سي المحجوب', 'Si Mahdjoub', 'Si Mahdjoub', 26),
(951, '0951', 'سيدي دامد', 'Sidi Demed', 'Sidi Demed', 26),
(952, '0952', 'سيدي نعمان', 'Sidi Naamane', 'Sidi Naamane', 26),
(953, '0953', 'سيدي الربيع', 'Sidi Rabie', 'Sidi Rabie', 26),
(954, '0954', 'سيدي زهار', 'Sidi Zahar', 'Sidi Zahar', 26),
(955, '0955', 'سيدي زيان', 'Sidi Ziane', 'Sidi Ziane', 26),
(956, '0956', 'السواقي', 'Souagui', 'Souagui', 26),
(957, '0957', 'تابلاط', 'Tablat', 'Tablat', 26),
(958, '0958', 'تفراوت', 'Tafraout', 'Tafraout', 26),
(959, '0959', 'تمسقيدة', 'Tamesguida', 'Tamesguida', 26),
(960, '0960', 'تيزي مهدي', 'Tizi Mahdi', 'Tizi Mahdi', 26),
(961, '0961', 'ثلاث دوائر', 'Tletat Ed Douair', 'Tletat Ed Douair', 26),
(962, '0962', 'الزبيرية', 'Zoubiria', 'Zoubiria', 26),
(963, '0963', 'عشعاشة', 'Achaacha', 'Achaacha', 27),
(964, '0964', 'عين بودينار', 'Ain-Boudinar', 'Ain-Boudinar', 27),
(965, '0965', 'عين نويسي', 'Ain-Nouissy', 'Ain-Nouissy', 27),
(966, '0966', 'عين سيدي الشريف', 'Ain-Sidi Cherif', 'Ain-Sidi Cherif', 27),
(967, '0967', 'عين تادلس', 'Ain-Tedles', 'Ain-Tedles', 27),
(968, '0968', 'بن عبد المالك رمضان', 'Benabdelmalek Ramdane', 'Benabdelmalek Ramdane', 27),
(969, '0969', 'بوقيراط', 'Bouguirat', 'Bouguirat', 27),
(970, '0970', 'فرناقة', 'Fornaka', 'Fornaka', 27),
(971, '0971', 'حجاج', 'Hadjadj', 'Hadjadj', 27),
(972, '0972', 'حاسي ماماش', 'Hassi Mameche', 'Hassi Mameche', 27),
(973, '0973', 'الحسيان (بني ياحي', 'Hassiane', 'Hassiane', 27),
(974, '0974', 'خضرة', 'Khadra', 'Khadra', 27),
(975, '0975', 'خير الدين', 'Kheir-Eddine', 'Kheir-Eddine', 27),
(976, '0976', 'منصورة', 'Mansourah', 'Mansourah', 27),
(977, '0977', 'مزغران', 'Mazagran', 'Mazagran', 27),
(978, '0978', 'ماسرة', 'Mesra', 'Mesra', 27),
(979, '0979', 'مستغانم', 'Mostaganem', 'Mostaganem', 27),
(980, '0980', 'نكمارية', 'Nekmaria', 'Nekmaria', 27),
(981, '0981', 'وادي الخير', 'Oued El Kheir', 'Oued El Kheir', 27),
(982, '0982', 'أولاد بوغالم', 'Ouled Boughalem', 'Ouled Boughalem', 27),
(983, '0983', 'أولاد مع الله', 'Ouled-Maalah', 'Ouled-Maalah', 27),
(984, '0984', 'صفصاف', 'Safsaf', 'Safsaf', 27),
(985, '0985', 'صيادة', 'Sayada', 'Sayada', 27),
(986, '0986', 'سيدي علي', 'Sidi Ali', 'Sidi Ali', 27),
(987, '0987', 'سيدي بلعطار', 'Sidi Belaattar', 'Sidi Belaattar', 27),
(988, '0988', 'سيدي لخضر', 'Sidi-Lakhdar', 'Sidi-Lakhdar', 27),
(989, '0989', 'سيرات', 'Sirat', 'Sirat', 27),
(990, '0990', 'السوافلية', 'Souaflia', 'Souaflia', 27),
(991, '0991', 'سور', 'Sour', 'Sour', 27),
(992, '0992', 'ستيدية', 'Stidia', 'Stidia', 27),
(993, '0993', 'تزقايت', 'Tazgait', 'Tazgait', 27),
(994, '0994', 'الطواهرية', 'Touahria', 'Touahria', 27),
(995, '0995', 'عين الحجل', 'Ain El Hadjel', 'Ain El Hadjel', 28),
(996, '0996', 'عين الملح', 'Ain El Melh', 'Ain El Melh', 28),
(997, '0997', 'عين فارس', 'Ain Fares', 'Ain Fares', 28),
(998, '0998', 'عين الخضراء', 'Ain Khadra', 'Ain Khadra', 28),
(999, '0999', 'عين الريش', 'Ain Rich', 'Ain Rich', 28),
(1000, '1000', 'بلعايبة', 'Belaiba', 'Belaiba', 28),
(1001, '1001', 'بن سرور', 'Ben Srour', 'Ben Srour', 28),
(1002, '1002', 'بني يلمان', 'Beni Ilmane', 'Beni Ilmane', 28),
(1003, '1003', 'بن زوه', 'Benzouh', 'Benzouh', 28),
(1004, '1004', 'برهوم', 'Berhoum', 'Berhoum', 28),
(1005, '1005', 'بئر فضة', 'Bir Foda', 'Bir Foda', 28),
(1006, '1006', 'بوسعادة', 'Bou Saada', 'Bou Saada', 28),
(1007, '1007', 'بوطي السايح', 'Bouti Sayeh', 'Bouti Sayeh', 28),
(1008, '1008', 'شلال', 'Chellal', 'Chellal', 28),
(1009, '1009', 'دهاهنة', 'Dehahna', 'Dehahna', 28),
(1010, '1010', 'جبل مساعد', 'Djebel Messaad', 'Djebel Messaad', 28),
(1011, '1011', 'الهامل', 'El Hamel', 'El Hamel', 28),
(1012, '1012', 'الحوامد', 'El Houamed', 'El Houamed', 28),
(1013, '1013', 'حمام الضلعة', 'Hammam Dalaa', 'Hammam Dalaa', 28),
(1014, '1014', 'خطوطي سد الجير', 'Khettouti Sed-El-Jir', 'Khettouti Sed-El-Jir', 28),
(1015, '1015', 'خبانة', 'Khoubana', 'Khoubana', 28),
(1016, '1016', 'المعاضيد', 'Maadid', 'Maadid', 28),
(1017, '1017', 'معاريف', 'Maarif', 'Maarif', 28),
(1018, '1018', 'مقرة', 'Magra', 'Magra', 28),
(1019, '1019', 'مسيف', 'M\'cif', 'M\'cif', 28),
(1020, '1020', 'امجدل', 'Medjedel', 'Medjedel', 28),
(1021, '1021', 'مناعة', 'Menaa', 'Menaa', 28),
(1022, '1022', 'محمد بوضياف', 'Mohamed Boudiaf', 'Mohamed Boudiaf', 28),
(1023, '1023', 'المسيلة', 'M\'sila', 'M\'sila', 28),
(1024, '1024', 'المطارفة', 'M\'tarfa', 'M\'tarfa', 28),
(1025, '1025', 'ونوغة', 'Ouanougha', 'Ouanougha', 28),
(1026, '1026', 'أولاد عدي لقبالة', 'Ouled Addi Guebala', 'Ouled Addi Guebala', 28),
(1027, '1027', 'أولاد دراج', 'Ouled Derradj', 'Ouled Derradj', 28),
(1028, '1028', 'أولاد ماضي', 'Ouled Madhi', 'Ouled Madhi', 28),
(1029, '1029', 'أولاد منصور', 'Ouled Mansour', 'Ouled Mansour', 28),
(1030, '1030', 'أولاد سيدي ابراهيم', 'Ouled Sidi Brahim', 'Ouled Sidi Brahim', 28),
(1031, '1031', 'أولاد سليمان', 'Ouled Slimane', 'Ouled Slimane', 28),
(1032, '1032', 'ولتام', 'Oulteme', 'Oulteme', 28),
(1033, '1033', 'سيدي عيسى', 'Sidi Aissa', 'Sidi Aissa', 28),
(1034, '1034', 'سيدي عامر', 'Sidi Ameur', 'Sidi Ameur', 28),
(1035, '1035', 'سيدي هجرس', 'Sidi Hadjeres', 'Sidi Hadjeres', 28),
(1036, '1036', 'سيدي امحمد', 'Sidi M\'hamed', 'Sidi M\'hamed', 28),
(1037, '1037', 'سليم', 'Slim', 'Slim', 28),
(1038, '1038', 'السوامع', 'Souamaa', 'Souamaa', 28),
(1039, '1039', 'تامسة', 'Tamsa', 'Tamsa', 28),
(1040, '1040', 'تارمونت', 'Tarmount', 'Tarmount', 28),
(1041, '1041', 'زرزور', 'Zarzour', 'Zarzour', 28),
(1042, '1042', 'عين فارس', 'Ain Fares', 'Ain Fares', 29),
(1043, '1043', 'عين فكان', 'Ain Fekan', 'Ain Fekan', 29),
(1044, '1044', 'عين فراح', 'Ain Ferah', 'Ain Ferah', 29),
(1045, '1045', 'عين أفرص', 'Ain Frass', 'Ain Frass', 29),
(1046, '1046', 'العلايمية', 'Alaimia', 'Alaimia', 29),
(1047, '1047', 'عوف', 'Aouf', 'Aouf', 29),
(1048, '1048', 'بنيان', 'Benian', 'Benian', 29),
(1049, '1049', 'بوهني', 'Bou Henni', 'Bou Henni', 29),
(1050, '1050', 'بوحنيفية', 'Bouhanifia', 'Bouhanifia', 29),
(1051, '1051', 'الشرفاء', 'Chorfa', 'Chorfa', 29),
(1052, '1052', 'البرج', 'El Bordj', 'El Bordj', 29),
(1053, '1053', 'القعدة', 'El Gaada', 'El Gaada', 29),
(1054, '1054', 'الغمري', 'El Ghomri', 'El Ghomri', 29),
(1055, '1055', 'القطنة', 'El Gueitena', 'El Gueitena', 29),
(1056, '1056', 'الحشم', 'El Hachem', 'El Hachem', 29),
(1057, '1057', 'القرط', 'El Keurt', 'El Keurt', 29),
(1058, '1058', 'المأمونية', 'El Mamounia', 'El Mamounia', 29),
(1059, '1059', 'المنور', 'El Menaouer', 'El Menaouer', 29),
(1060, '1060', 'فراقيق', 'Ferraguig', 'Ferraguig', 29),
(1061, '1061', 'فروحة', 'Froha', 'Froha', 29),
(1062, '1062', 'غروس', 'Gharrous', 'Gharrous', 29),
(1063, '1063', 'غريس', 'Ghriss', 'Ghriss', 29),
(1064, '1064', 'قرجوم', 'Guerdjoum', 'Guerdjoum', 29),
(1065, '1065', 'حسين', 'Hacine', 'Hacine', 29),
(1066, '1066', 'خلوية', 'Khalouia', 'Khalouia', 29),
(1067, '1067', 'ماقضة', 'Makhda', 'Makhda', 29),
(1068, '1068', 'ماوسة', 'Maoussa', 'Maoussa', 29),
(1069, '1069', 'معسكر', 'Mascara', 'Mascara', 29),
(1070, '1070', 'المطمور', 'Matemore', 'Matemore', 29),
(1071, '1071', 'مقطع الدوز', 'Mocta-Douz', 'Mocta-Douz', 29),
(1072, '1072', 'المحمدية', 'Mohammadia', 'Mohammadia', 29),
(1073, '1073', 'نسمط', 'Nesmot', 'Nesmot', 29),
(1074, '1074', 'عقاز', 'Oggaz', 'Oggaz', 29),
(1075, '1075', 'وادي الأبطال', 'Oued El Abtal', 'Oued El Abtal', 29),
(1076, '1076', 'وادي التاغية', 'Oued Taria', 'Oued Taria', 29),
(1077, '1077', 'رأس عين عميروش', 'Ras El Ain Amirouche', 'Ras El Ain Amirouche', 29),
(1078, '1078', 'سجرارة', 'Sedjerara', 'Sedjerara', 29),
(1079, '1079', 'السهايلية', 'Sehailia', 'Sehailia', 29),
(1080, '1080', 'سيدي عبد الجبار', 'Sidi Abdeldjebar', 'Sidi Abdeldjebar', 29),
(1081, '1081', 'سيدي عبد المومن', 'Sidi Abdelmoumene', 'Sidi Abdelmoumene', 29),
(1082, '1082', 'سيدي بوسعيد', 'Sidi Boussaid', 'Sidi Boussaid', 29),
(1083, '1083', 'سيدي قادة', 'Sidi Kada', 'Sidi Kada', 29),
(1084, '1084', 'سيق', 'Sig', 'Sig', 29),
(1085, '1085', 'تيغنيف', 'Tighennif', 'Tighennif', 29),
(1086, '1086', 'تيزي', 'Tizi', 'Tizi', 29),
(1087, '1087', 'زهانة', 'Zahana', 'Zahana', 29),
(1088, '1088', 'زلامطة', 'Zelamta', 'Zelamta', 29),
(1089, '1089', 'عين البيضاء', 'Ain Beida', 'Ain Beida', 30),
(1090, '1090', 'بن ناصر', 'Benaceur', 'Benaceur', 55),
(1091, '1091', 'بلدة اعمر', 'Blidet Amor', 'Blidet Amor', 55),
(1092, '1092', 'العالية', 'El Alia', 'El Alia', 55),
(1093, '1093', 'البرمة', 'El Borma', 'El Borma', 30),
(1094, '1094', 'الحجيرة', 'El-Hadjira', 'El-Hadjira', 55),
(1095, '1095', 'حاسي بن عبد الله', 'Hassi Ben Abdellah', 'Hassi Ben Abdellah', 30),
(1096, '1096', 'حاسي مسعود', 'Hassi Messaoud', 'Hassi Messaoud', 30),
(1097, '1097', 'المقارين', 'Megarine', 'Megarine', 55),
(1098, '1098', 'المنقر', 'M\'naguer', 'M\'naguer', 55),
(1099, '1099', 'النزلة', 'Nezla', 'Nezla', 55),
(1100, '1100', 'انقوسة', 'N\'goussa', 'N\'goussa', 30),
(1101, '1101', 'ورقلة', 'Ouargla', 'Ouargla', 30),
(1102, '1102', 'الرويسات', 'Rouissat', 'Rouissat', 30),
(1103, '1103', 'سيدي خويلد', 'Sidi Khouiled', 'Sidi Khouiled', 30),
(1104, '1104', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 55),
(1105, '1105', 'الطيبات', 'Taibet', 'Taibet', 55),
(1106, '1106', 'تبسبست', 'Tebesbest', 'Tebesbest', 55),
(1107, '1107', 'تماسين', 'Temacine', 'Temacine', 55),
(1108, '1108', 'تقرت', 'Touggourt', 'Touggourt', 55),
(1109, '1109', 'الزاوية العابدية', 'Zaouia El Abidia', 'Zaouia El Abidia', 55),
(1110, '1110', 'عين البية', 'Ain Biya', 'Ain Biya', 31),
(1111, '1111', 'عين الكرمة', 'Ain Kerma', 'Ain Kerma', 31),
(1112, '1112', 'عين الترك', 'Ain Turk', 'Ain Turk', 31),
(1113, '1113', 'أرزيو', 'Arzew', 'Arzew', 31),
(1114, '1114', 'بن فريحة', 'Ben Freha', 'Ben Freha', 31),
(1115, '1115', 'بطيوة', 'Bethioua', 'Bethioua', 31),
(1116, '1116', 'بئر الجير', 'Bir El Djir', 'Bir El Djir', 31),
(1117, '1117', 'بوفاتيس', 'Boufatis', 'Boufatis', 31),
(1118, '1118', 'بوسفر', 'Bousfer', 'Bousfer', 31),
(1119, '1119', 'بوتليليس', 'Boutlelis', 'Boutlelis', 31),
(1120, '1120', 'العنصر', 'El Ancor', 'El Ancor', 31),
(1121, '1121', 'البراية', 'El Braya', 'El Braya', 31),
(1122, '1122', 'الكرمة', 'El Kerma', 'El Kerma', 31),
(1123, '1123', 'السانية', 'Es Senia', 'Es Senia', 31),
(1124, '1124', 'قديل', 'Gdyel', 'Gdyel', 31),
(1125, '1125', 'حاسي بن عقبة', 'Hassi Ben Okba', 'Hassi Ben Okba', 31),
(1126, '1126', 'حاسي بونيف', 'Hassi Bounif', 'Hassi Bounif', 31),
(1127, '1127', 'حاسي مفسوخ', 'Hassi Mefsoukh', 'Hassi Mefsoukh', 31),
(1128, '1128', 'مرسى الحجاج', 'Marsat El Hadjadj', 'Marsat El Hadjadj', 31),
(1129, '1129', 'المرسى الكبير', 'Mers El Kebir', 'Mers El Kebir', 31),
(1130, '1130', 'مسرغين', 'Messerghin', 'Messerghin', 31),
(1131, '1131', 'وهران', 'Oran', 'Oran', 31),
(1132, '1132', 'وادي تليلات', 'Oued Tlelat', 'Oued Tlelat', 31),
(1133, '1133', 'سيدي بن يبقى', 'Sidi Ben Yebka', 'Sidi Ben Yebka', 31),
(1134, '1134', 'سيدي الشحمي', 'Sidi Chami', 'Sidi Chami', 31),
(1135, '1135', 'طفراوي', 'Tafraoui', 'Tafraoui', 31),
(1136, '1136', 'عين العراك', 'Ain El Orak', 'Ain El Orak', 32),
(1137, '1137', 'اربوات', 'Arbaouat', 'Arbaouat', 32),
(1138, '1138', 'بوعلام', 'Boualem', 'Boualem', 32),
(1139, '1139', 'بوقطب', 'Bougtoub', 'Bougtoub', 32),
(1140, '1140', 'بوسمغون', 'Boussemghoun', 'Boussemghoun', 32),
(1141, '1141', 'بريزينة', 'Brezina', 'Brezina', 32),
(1142, '1142', 'الشقيق', 'Cheguig', 'Cheguig', 32),
(1143, '1143', 'شلالة', 'Chellala', 'Chellala', 32),
(1144, '1144', 'البيض', 'El Bayadh', 'El Bayadh', 32),
(1145, '1145', 'الأبيض سيدي الشيخ', 'Labiodh Sidi Cheikh', 'Labiodh Sidi Cheikh', 32),
(1146, '1146', 'البنود', 'El Bnoud', 'El Bnoud', 32),
(1147, '1147', 'الخيثر', 'El Kheiter', 'El Kheiter', 32),
(1148, '1148', 'المحرة', 'El Mehara', 'El Mehara', 32),
(1149, '1149', 'الغاسول', 'Ghassoul', 'Ghassoul', 32),
(1150, '1150', 'الكاف الأحمر', 'Kef El Ahmar', 'Kef El Ahmar', 32),
(1151, '1151', 'كراكدة', 'Krakda', 'Krakda', 32),
(1152, '1152', 'رقاصة', 'Rogassa', 'Rogassa', 32),
(1153, '1153', 'سيدي عامر', 'Sidi Ameur', 'Sidi Ameur', 32),
(1154, '1154', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 32),
(1155, '1155', 'سيدي طيفور', 'Sidi Tiffour', 'Sidi Tiffour', 32),
(1156, '1156', 'ستيتن', 'Stitten', 'Stitten', 32),
(1157, '1157', 'توسمولين', 'Tousmouline', 'Tousmouline', 32),
(1158, '1158', 'برج الحواس', 'Bordj El Haouass', 'Bordj El Haouass', 56),
(1159, '1159', 'برج عمر إدريس', 'Bordj Omar Driss', 'Bordj Omar Driss', 33),
(1160, '1160', 'دبداب', 'Debdeb', 'Debdeb', 33),
(1161, '1161', 'جانت', 'Djanet', 'Djanet', 56),
(1162, '1162', 'إيليزي', 'Illizi', 'Illizi', 33),
(1163, '1163', 'إن أمناس', 'In Amenas', 'In Amenas', 33),
(1164, '1164', 'عين تاغروت', 'Ain Taghrout', 'Ain Taghrout', 34),
(1165, '1165', 'عين تسرة', 'Ain Tesra', 'Ain Tesra', 34),
(1166, '1166', 'برج بوعريرج', 'B. B. Arreridj', 'B. B. Arreridj', 34),
(1167, '1167', 'بليمور', 'Belimour', 'Belimour', 34),
(1168, '1168', 'بن داود', 'Ben Daoud', 'Ben Daoud', 34),
(1169, '1169', 'بئر قاصد علي', 'Bir Kasdali', 'Bir Kasdali', 34),
(1170, '1170', 'برج الغدير', 'Bordj Ghedir', 'Bordj Ghedir', 34),
(1171, '1171', 'برج زمورة', 'Bordj Zemmoura', 'Bordj Zemmoura', 34),
(1172, '1172', 'القلة', 'Colla', 'Colla', 34),
(1173, '1173', 'جعافرة', 'Djaafra', 'Djaafra', 34),
(1174, '1174', 'العش', 'El Euch', 'El Euch', 34),
(1175, '1175', 'الياشير', 'El Achir', 'El Achir', 34),
(1176, '1176', 'العناصر', 'El Annasseur', 'El Annasseur', 34),
(1177, '1177', 'الحمادية', 'Elhammadia', 'Elhammadia', 34),
(1178, '1178', 'الماين', 'El Main', 'El Main', 34),
(1179, '1179', 'المهير', 'El M\'hir', 'El M\'hir', 34),
(1180, '1180', 'غيلاسة', 'Ghailasa', 'Ghailasa', 34),
(1181, '1181', 'حرازة', 'Haraza', 'Haraza', 34),
(1182, '1182', 'حسناوة', 'Hasnaoua', 'Hasnaoua', 34),
(1183, '1183', 'خليل', 'Khelil', 'Khelil', 34),
(1184, '1184', 'القصور', 'Ksour', 'Ksour', 34),
(1185, '1185', 'المنصورة', 'Mansoura', 'Mansoura', 34),
(1186, '1186', 'مجانة', 'Medjana', 'Medjana', 34),
(1187, '1187', 'أولاد أبراهم', 'Ouled Brahem', 'Ouled Brahem', 34),
(1188, '1188', 'أولاد دحمان', 'Ouled Dahmane', 'Ouled Dahmane', 34),
(1189, '1189', 'أولاد سيدي ابراهيم', 'Ouled Sidi-Brahim', 'Ouled Sidi-Brahim', 34),
(1190, '1190', 'الرابطة', 'Rabta', 'Rabta', 34),
(1191, '1191', 'رأس الوادي', 'Ras El Oued', 'Ras El Oued', 34),
(1192, '1192', 'سيدي أمبارك', 'Sidi-Embarek', 'Sidi-Embarek', 34),
(1193, '1193', 'تفرق', 'Tefreg', 'Tefreg', 34),
(1194, '1194', 'تقلعيت', 'Taglait', 'Taglait', 34),
(1195, '1195', 'تسامرت', 'Tassamert', 'Tassamert', 34),
(1196, '1196', 'ثنية النصر', 'Teniet En Nasr', 'Teniet En Nasr', 34),
(1197, '1197', 'تيكستار', 'Tixter', 'Tixter', 34),
(1198, '1198', 'أعفير', 'Afir', 'Afir', 35),
(1199, '1199', 'عمال', 'Ammal', 'Ammal', 35),
(1200, '1200', 'بغلية', 'Baghlia', 'Baghlia', 35),
(1201, '1201', 'بن شود', 'Ben Choud', 'Ben Choud', 35),
(1202, '1202', 'بني عمران', 'Beni Amrane', 'Beni Amrane', 35),
(1203, '1203', 'برج منايل', 'Bordj Menaiel', 'Bordj Menaiel', 35),
(1204, '1204', 'بودواو', 'Boudouaou', 'Boudouaou', 35),
(1205, '1205', 'بودواو البحري', 'Boudouaou El Bahri', 'Boudouaou El Bahri', 35),
(1206, '1206', 'بومرداس', 'Boumerdes', 'Boumerdes', 35),
(1207, '1207', 'بوزقزة قدارة', 'Bouzegza Keddara', 'Bouzegza Keddara', 35),
(1208, '1208', 'شعبة العامر', 'Chabet El Ameur', 'Chabet El Ameur', 35),
(1209, '1209', 'قورصو', 'Corso', 'Corso', 35),
(1210, '1210', 'دلس', 'Dellys', 'Dellys', 35),
(1211, '1211', 'جنات', 'Djinet', 'Djinet', 35),
(1212, '1212', 'الخروبة', 'El Kharrouba', 'El Kharrouba', 35),
(1213, '1213', 'حمادي', 'Hammedi', 'Hammedi', 35),
(1214, '1214', 'يسر', 'Isser', 'Isser', 35),
(1215, '1215', 'خميس الخشنة', 'Khemis El Khechna', 'Khemis El Khechna', 35),
(1216, '1216', 'الاربعطاش', 'Larbatache', 'Larbatache', 35),
(1217, '1217', 'لقاطة', 'Leghata', 'Leghata', 35),
(1218, '1218', 'الناصرية', 'Naciria', 'Naciria', 35),
(1219, '1219', 'أولاد عيسى', 'Ouled Aissa', 'Ouled Aissa', 35),
(1220, '1220', 'أولاد هداج', 'Ouled Hedadj', 'Ouled Hedadj', 35),
(1221, '1221', 'أولاد موسى', 'Ouled Moussa', 'Ouled Moussa', 35),
(1222, '1222', 'سي مصطفى', 'Si Mustapha', 'Si Mustapha', 35),
(1223, '1223', 'سيدي داود', 'Sidi Daoud', 'Sidi Daoud', 35),
(1224, '1224', 'سوق الحد', 'Souk El Had', 'Souk El Had', 35),
(1225, '1225', 'تاورقة', 'Taourga', 'Taourga', 35),
(1226, '1226', 'الثنية', 'Thenia', 'Thenia', 35),
(1227, '1227', 'تيجلابين', 'Tidjelabine', 'Tidjelabine', 35),
(1228, '1228', 'تيمزريت', 'Timezrit', 'Timezrit', 35),
(1229, '1229', 'زموري', 'Zemmouri', 'Zemmouri', 35),
(1230, '1230', 'عين العسل', 'Ain El Assel', 'Ain El Assel', 36),
(1231, '1231', 'عين الكرمة', 'Ain Kerma', 'Ain Kerma', 36),
(1232, '1232', 'عصفور', 'Asfour', 'Asfour', 36),
(1233, '1233', 'بن مهيدي', 'Ben M Hidi', 'Ben M Hidi', 36),
(1234, '1234', 'بريحان', 'Berrihane', 'Berrihane', 36),
(1235, '1235', 'البسباس', 'Besbes', 'Besbes', 36),
(1236, '1236', 'بوقوس', 'Bougous', 'Bougous', 36),
(1237, '1237', 'بوحجار', 'Bouhadjar', 'Bouhadjar', 36),
(1238, '1238', 'بوثلجة', 'Bouteldja', 'Bouteldja', 36),
(1239, '1239', 'شبيطة مختار', 'Chebaita Mokhtar', 'Chebaita Mokhtar', 36),
(1240, '1240', 'الشافية', 'Chefia', 'Chefia', 36),
(1241, '1241', 'شحاني', 'Chihani', 'Chihani', 36),
(1242, '1242', 'الذرعـان', 'Drean', 'Drean', 36),
(1243, '1243', 'الشط', 'Echatt', 'Echatt', 36),
(1244, '1244', 'العيون', 'El Aioun', 'El Aioun', 36),
(1245, '1245', 'القالة', 'El Kala', 'El Kala', 36),
(1246, '1246', 'الطارف', 'El Tarf', 'El Tarf', 36),
(1247, '1247', 'حمام بني صالح', 'Hammam Beni Salah', 'Hammam Beni Salah', 36),
(1248, '1248', 'بحيرة الطيور', 'Lac Des Oiseaux', 'Lac Des Oiseaux', 36),
(1249, '1249', 'وادي الزيتون', 'Oued Zitoun', 'Oued Zitoun', 36),
(1250, '1250', 'رمل السوق', 'Raml Souk', 'Raml Souk', 36),
(1251, '1251', 'السوارخ', 'Souarekh', 'Souarekh', 36),
(1252, '1252', 'زريزر', 'Zerizer', 'Zerizer', 36),
(1253, '1253', 'الزيتونة', 'Zitouna', 'Zitouna', 36),
(1254, '1254', 'أم العسل', 'Oum El Assel', 'Oum El Assel', 37),
(1255, '1255', 'تندوف', 'Tindouf', 'Tindouf', 37),
(1256, '1256', 'عماري', 'Ammari', 'Ammari', 38),
(1257, '1257', 'بني شعيب', 'Beni Chaib', 'Beni Chaib', 38),
(1258, '1258', 'بني لحسن', 'Beni Lahcene', 'Beni Lahcene', 38),
(1259, '1259', 'برج بونعامة', 'Bordj Bounaama', 'Bordj Bounaama', 38),
(1260, '1260', 'برج الأمير عبد القادر', 'Bordj El Emir Abdelkader', 'Bordj El Emir Abdelkader', 38),
(1261, '1261', 'بوقائد', 'Boucaid', 'Boucaid', 38),
(1262, '1262', 'خميستي', 'Khemisti', 'Khemisti', 38),
(1263, '1263', 'الأربعاء', 'Larbaa', 'Larbaa', 38),
(1264, '1264', 'لرجام', 'Lardjem', 'Lardjem', 38),
(1265, '1265', 'العيون', 'Layoune', 'Layoune', 38),
(1266, '1266', 'الأزهرية', 'Lazharia', 'Lazharia', 38),
(1267, '1267', 'المعاصم', 'Maacem', 'Maacem', 38),
(1268, '1268', 'الملعب', 'Melaab', 'Melaab', 38),
(1269, '1269', 'أولاد بسام', 'Ouled Bessam', 'Ouled Bessam', 38),
(1270, '1270', 'سيدي عابد', 'Sidi Abed', 'Sidi Abed', 38),
(1271, '1271', 'سيدي بوتوشنت', 'Sidi Boutouchent', 'Sidi Boutouchent', 38),
(1272, '1272', 'سيدي العنتري', 'Sidi Lantri', 'Sidi Lantri', 38),
(1273, '1273', 'سيدي سليمان', 'Sidi Slimane', 'Sidi Slimane', 38),
(1274, '1274', 'تملاحت', 'Tamellahet', 'Tamellahet', 38),
(1275, '1275', 'ثنية الاحد', 'Theniet El Had', 'Theniet El Had', 38),
(1276, '1276', 'تيسمسيلت', 'Tissemsilt', 'Tissemsilt', 38),
(1277, '1277', 'اليوسفية', 'Youssoufia', 'Youssoufia', 38),
(1278, '1278', 'البياضة', 'Bayadha', 'Bayadha', 39),
(1279, '1279', 'بن  قشة', 'Ben Guecha', 'Ben Guecha', 39),
(1280, '1280', 'الدبيلة', 'Debila', 'Debila', 39),
(1281, '1281', 'جامعة', 'Djamaa', 'Djamaa', 57),
(1282, '1282', 'دوار الماء', 'Douar El Maa', 'Douar El Maa', 39),
(1283, '1283', 'العقلة', 'El Ogla', 'El Ogla', 39),
(1284, '1284', 'المغير', 'El-M\'ghaier', 'El-M\'ghaier', 57),
(1285, '1285', 'الوادي', 'El-Oued', 'El-Oued', 39),
(1286, '1286', 'قمار', 'Guemar', 'Guemar', 39),
(1287, '1287', 'الحمراية', 'Hamraia', 'Hamraia', 39),
(1288, '1288', 'حساني عبد الكريم', 'Hassani Abdelkrim', 'Hassani Abdelkrim', 39),
(1289, '1289', 'حاسي خليفة', 'Hassi Khalifa', 'Hassi Khalifa', 39),
(1290, '1290', 'كوينين', 'Kouinine', 'Kouinine', 39),
(1291, '1291', 'المقرن', 'Magrane', 'Magrane', 39),
(1292, '1292', 'اميه وانسة', 'Mih Ouansa', 'Mih Ouansa', 39),
(1293, '1293', 'المرارة', 'M\'rara', 'M\'rara', 57),
(1294, '1294', 'النخلة', 'Nakhla', 'Nakhla', 39),
(1295, '1295', 'وادي العلندة', 'Oued El Alenda', 'Oued El Alenda', 39),
(1296, '1296', 'أم الطيور', 'Oum Touyour', 'Oum Touyour', 57),
(1297, '1297', 'ورماس', 'Ourmes', 'Ourmes', 39),
(1298, '1298', 'الرقيبة', 'Reguiba', 'Reguiba', 39),
(1299, '1299', 'الرباح', 'Robbah', 'Robbah', 39),
(1300, '1300', 'سيدي عمران', 'Sidi Amrane', 'Sidi Amrane', 57),
(1301, '1301', 'سيدي عون', 'Sidi Aoun', 'Sidi Aoun', 39),
(1302, '1302', 'سيدي خليل', 'Sidi Khelil', 'Sidi Khelil', 57),
(1303, '1303', 'سطيل', 'Still', 'Still', 57),
(1304, '1304', 'تغزوت', 'Taghzout', 'Taghzout', 39),
(1305, '1305', 'الطالب العربي', 'Taleb Larbi', 'Taleb Larbi', 39),
(1306, '1306', 'تندلة', 'Tenedla', 'Tenedla', 57),
(1307, '1307', 'الطريفاوي', 'Trifaoui', 'Trifaoui', 39),
(1308, '1308', 'عين الطويلة', 'Ain Touila', 'Ain Touila', 40),
(1309, '1309', 'بابار', 'Babar', 'Babar', 40),
(1310, '1310', 'بغاي', 'Baghai', 'Baghai', 40),
(1311, '1311', 'بوحمامة', 'Bouhmama', 'Bouhmama', 40),
(1312, '1312', 'ششار', 'Chechar', 'Chechar', 40),
(1313, '1313', 'شلية', 'Chelia', 'Chelia', 40),
(1314, '1314', 'جلال', 'Djellal', 'Djellal', 40),
(1315, '1315', 'الحامة', 'El Hamma', 'El Hamma', 40),
(1316, '1316', 'المحمل', 'El Mahmal', 'El Mahmal', 40),
(1317, '1317', 'الولجة', 'El Oueldja', 'El Oueldja', 40),
(1318, '1318', 'انسيغة', 'Ensigha', 'Ensigha', 40),
(1319, '1319', 'قايس', 'Kais', 'Kais', 40),
(1320, '1320', 'خنشلة', 'Khenchela', 'Khenchela', 40),
(1321, '1321', 'خيران', 'Khirane', 'Khirane', 40),
(1322, '1322', 'مصارة', 'M\'sara', 'M\'sara', 40),
(1323, '1323', 'متوسة', 'M\'toussa', 'M\'toussa', 40),
(1324, '1324', 'أولاد رشاش', 'Ouled Rechache', 'Ouled Rechache', 40),
(1325, '1325', 'الرميلة', 'Remila', 'Remila', 40),
(1326, '1326', 'طامزة', 'Tamza', 'Tamza', 40),
(1327, '1327', 'تاوزيانت', 'Taouzianat', 'Taouzianat', 40),
(1328, '1328', 'يابوس', 'Yabous', 'Yabous', 40),
(1329, '1329', 'عين سلطان', 'Ain Soltane', 'Ain Soltane', 41),
(1330, '1330', 'عين الزانة', 'Ain Zana', 'Ain Zana', 41),
(1331, '1331', 'بئر بوحوش', 'Bir Bouhouche', 'Bir Bouhouche', 41),
(1332, '1332', 'الدريعة', 'Drea', 'Drea', 41),
(1333, '1333', 'الحدادة', 'Haddada', 'Haddada', 41),
(1334, '1334', 'الحنانشة', 'Hanencha', 'Hanencha', 41),
(1335, '1335', 'الخضارة', 'Khedara', 'Khedara', 41),
(1336, '1336', 'خميسة', 'Khemissa', 'Khemissa', 41),
(1337, '1337', 'المشروحة', 'Machroha', 'Machroha', 41),
(1338, '1338', 'مداوروش', 'M\'daourouche', 'M\'daourouche', 41),
(1339, '1339', 'المراهنة', 'Merahna', 'Merahna', 41),
(1340, '1340', 'وادي الكبريت', 'Oued Kebrit', 'Oued Kebrit', 41),
(1341, '1341', 'أولاد إدريس', 'Ouled Driss', 'Ouled Driss', 41),
(1342, '1342', 'أولاد مومن', 'Ouled Moumen', 'Ouled Moumen', 41),
(1343, '1343', 'أم العظايم', 'Oum El Adhaim', 'Oum El Adhaim', 41),
(1344, '1344', 'ويلان', 'Ouillen', 'Ouillen', 41),
(1345, '1345', 'الراقوبة', 'Ragouba', 'Ragouba', 41),
(1346, '1346', 'سافل الويدان', 'Safel El Ouiden', 'Safel El Ouiden', 41),
(1347, '1347', 'سدراتة', 'Sedrata', 'Sedrata', 41),
(1348, '1348', 'سيدي فرج', 'Sidi Fredj', 'Sidi Fredj', 41),
(1349, '1349', 'سوق أهراس', 'Souk Ahras', 'Souk Ahras', 41),
(1350, '1350', 'تاورة', 'Taoura', 'Taoura', 41),
(1351, '1351', 'ترقالت', 'Terraguelt', 'Terraguelt', 41),
(1352, '1352', 'تيفاش', 'Tiffech', 'Tiffech', 41),
(1353, '1353', 'الزعرورية', 'Zaarouria', 'Zaarouria', 41),
(1354, '1354', 'الزوابي', 'Zouabi', 'Zouabi', 41),
(1355, '1355', 'أغبال', 'Aghbal', 'Aghbal', 42),
(1356, '1356', 'أحمر العين', 'Ahmer El Ain', 'Ahmer El Ain', 42),
(1357, '1357', 'عين تاقورايت', 'Ain Tagourait', 'Ain Tagourait', 42),
(1358, '1358', 'الحطاطبة', 'Attatba', 'Attatba', 42),
(1359, '1359', 'بني ميلك', 'Beni Mileuk', 'Beni Mileuk', 42),
(1360, '1360', 'بوهارون', 'Bou Haroun', 'Bou Haroun', 42),
(1361, '1361', 'بواسماعيل', 'Bou Ismail', 'Bou Ismail', 42),
(1362, '1362', 'بورقيقة', 'Bourkika', 'Bourkika', 42),
(1363, '1363', 'الشعيبة', 'Chaiba', 'Chaiba', 42),
(1364, '1364', 'شرشال', 'Cherchell', 'Cherchell', 42),
(1365, '1365', 'الداموس', 'Damous', 'Damous', 42),
(1366, '1366', 'دواودة', 'Douaouda', 'Douaouda', 42),
(1367, '1367', 'فوكة', 'Fouka', 'Fouka', 42),
(1368, '1368', 'قوراية', 'Gouraya', 'Gouraya', 42),
(1369, '1369', 'حجوط', 'Hadjout', 'Hadjout', 42),
(1370, '1370', 'حجرة النص', 'Hadjret Ennous', 'Hadjret Ennous', 42),
(1371, '1371', 'خميستي', 'Khemisti', 'Khemisti', 42),
(1372, '1372', 'القليعة', 'Kolea', 'Kolea', 42),
(1373, '1373', 'الأرهاط', 'Larhat', 'Larhat', 42),
(1374, '1374', 'مناصر', 'Menaceur', 'Menaceur', 42),
(1375, '1375', 'مراد', 'Merad', 'Merad', 42),
(1376, '1376', 'مسلمون', 'Messelmoun', 'Messelmoun', 42),
(1377, '1377', 'الناظور', 'Nador', 'Nador', 42),
(1378, '1378', 'سيدي غيلاس', 'Sidi Ghiles', 'Sidi Ghiles', 42),
(1379, '1379', 'سيدي راشد', 'Sidi Rached', 'Sidi Rached', 42),
(1380, '1380', 'سيدي سميان', 'Sidi Semiane', 'Sidi Semiane', 42),
(1381, '1381', 'سيدي عامر', 'Sidi-Amar', 'Sidi-Amar', 42),
(1382, '1382', 'تيبازة', 'Tipaza', 'Tipaza', 42),
(1383, '1383', 'أحمد راشدي', 'Ahmed Rachedi', 'Ahmed Rachedi', 43),
(1384, '1384', ' عين البيضاء أحريش', 'Ain Beida Harriche', 'Ain Beida Harriche', 43),
(1385, '1385', 'عين الملوك', 'Ain Mellouk', 'Ain Mellouk', 43),
(1386, '1386', 'عين التين', 'Ain Tine', 'Ain Tine', 43),
(1387, '1387', 'اعميرة اراس', 'Amira Arres', 'Amira Arres', 43),
(1388, '1388', 'بن يحي عبد الرحمن', 'Benyahia Abderrahmane', 'Benyahia Abderrahmane', 43),
(1389, '1389', 'بوحاتم', 'Bouhatem', 'Bouhatem', 43),
(1390, '1390', 'شلغوم العيد', 'Chelghoum Laid', 'Chelghoum Laid', 43),
(1391, '1391', 'الشيقارة', 'Chigara', 'Chigara', 43),
(1392, '1392', 'دراحي بوصلاح', 'Derrahi Bousselah', 'Derrahi Bousselah', 43),
(1393, '1393', 'العياضي برباس', 'El Ayadi Barbes', 'El Ayadi Barbes', 43),
(1394, '1394', 'مشيرة', 'El Mechira', 'El Mechira', 43),
(1395, '1395', 'فرجيوة', 'Ferdjioua', 'Ferdjioua', 43),
(1396, '1396', 'القرارم قوقة', 'Grarem Gouga', 'Grarem Gouga', 43),
(1397, '1397', 'حمالة', 'Hamala', 'Hamala', 43),
(1398, '1398', 'ميلة', 'Mila', 'Mila', 43),
(1399, '1399', 'مينار زارزة', 'Minar Zarza', 'Minar Zarza', 43),
(1400, '1400', 'وادي العثمانية', 'Oued Athmenia', 'Oued Athmenia', 43),
(1401, '1401', 'وادي النجاء', 'Oued Endja', 'Oued Endja', 43),
(1402, '1402', 'وادي سقان', 'Oued Seguen', 'Oued Seguen', 43),
(1403, '1403', 'أولاد اخلوف', 'Ouled Khalouf', 'Ouled Khalouf', 43),
(1404, '1404', 'الرواشد', 'Rouached', 'Rouached', 43),
(1405, '1405', 'سيدي خليفة', 'Sidi Khelifa', 'Sidi Khelifa', 43),
(1406, '1406', 'سيدي مروان', 'Sidi Merouane', 'Sidi Merouane', 43),
(1407, '1407', 'تاجنانت', 'Tadjenanet', 'Tadjenanet', 43),
(1408, '1408', 'تسدان حدادة', 'Tassadane Haddada', 'Tassadane Haddada', 43),
(1409, '1409', 'التلاغمة', 'Teleghma', 'Teleghma', 43),
(1410, '1410', 'ترعي باينان', 'Terrai Bainen', 'Terrai Bainen', 43),
(1411, '1411', 'تسالة لمطاعي', 'Tassala Lematai', 'Tassala Lematai', 43),
(1412, '1412', 'تيبرقنت', 'Tiberguent', 'Tiberguent', 43),
(1413, '1413', 'يحي بني قشة', 'Yahia Beniguecha', 'Yahia Beniguecha', 43),
(1414, '1414', 'زغاية', 'Zeghaia', 'Zeghaia', 43),
(1415, '1415', 'عين البنيان', 'Ain-Benian', 'Ain-Benian', 44),
(1416, '1416', 'عين بويحيى', 'Ain-Bouyahia', 'Ain-Bouyahia', 44),
(1417, '1417', 'عين الدفلى', 'Ain-Defla', 'Ain-Defla', 44),
(1418, '1418', 'عين الاشياخ', 'Ain-Lechiakh', 'Ain-Lechiakh', 44),
(1419, '1419', 'عين السلطان', 'Ain-Soltane', 'Ain-Soltane', 44),
(1420, '1420', 'عين التركي', 'Ain-Torki', 'Ain-Torki', 44),
(1421, '1421', 'عريب', 'Arib', 'Arib', 44),
(1422, '1422', 'بربوش', 'Birbouche', 'Birbouche', 44),
(1423, '1423', 'بطحية', 'Bathia', 'Bathia', 44),
(1424, '1424', 'بلعاص', 'Belaas', 'Belaas', 44),
(1425, '1425', 'بن علال', 'Ben Allal', 'Ben Allal', 44),
(1426, '1426', 'بئر ولد خليفة', 'Bir-Ould-Khelifa', 'Bir-Ould-Khelifa', 44),
(1427, '1427', 'برج الأمير خالد', 'Bordj-Emir-Khaled', 'Bordj-Emir-Khaled', 44),
(1428, '1428', 'بومدفع', 'Boumedfaa', 'Boumedfaa', 44),
(1429, '1429', 'بوراشد', 'Bourached', 'Bourached', 44),
(1430, '1430', 'جليدة', 'Djelida', 'Djelida', 44),
(1431, '1431', 'جمعة أولاد الشيخ', 'Djemaa Ouled Cheikh', 'Djemaa Ouled Cheikh', 44),
(1432, '1432', 'جندل', 'Djendel', 'Djendel', 44),
(1433, '1433', 'العبادية', 'El-Abadia', 'El-Abadia', 44),
(1434, '1434', 'العامرة', 'El-Amra', 'El-Amra', 44),
(1435, '1435', 'العطاف', 'El-Attaf', 'El-Attaf', 44),
(1436, '1436', 'الماين', 'El-Maine', 'El-Maine', 44),
(1437, '1437', 'حمام ريغة', 'Hammam-Righa', 'Hammam-Righa', 44),
(1438, '1438', 'الحسانية', 'Hassania', 'Hassania', 44),
(1439, '1439', 'الحسينية', 'Hoceinia', 'Hoceinia', 44),
(1440, '1440', 'خميس مليانة', 'Khemis-Miliana', 'Khemis-Miliana', 44),
(1441, '1441', 'المخاطرية', 'Mekhatria', 'Mekhatria', 44),
(1442, '1442', 'مليانة', 'Miliana', 'Miliana', 44),
(1443, '1443', 'وادي الشرفاء', 'Oued Chorfa', 'Oued Chorfa', 44),
(1444, '1444', 'واد الجمعة', 'Oued Djemaa', 'Oued Djemaa', 44),
(1445, '1445', 'الروينة', 'Rouina', 'Rouina', 44),
(1446, '1446', 'سيدي الأخضر', 'Sidi-Lakhdar', 'Sidi-Lakhdar', 44),
(1447, '1447', 'تاشتة زقاغة', 'Tacheta Zegagha', 'Tacheta Zegagha', 44),
(1448, '1448', 'طارق بن زياد', 'Tarik-Ibn-Ziad', 'Tarik-Ibn-Ziad', 44),
(1449, '1449', 'تبركانين', 'Tiberkanine', 'Tiberkanine', 44),
(1450, '1450', 'زدين', 'Zeddine', 'Zeddine', 44),
(1451, '1451', 'عين بن خليل', 'Ain Ben Khelil', 'Ain Ben Khelil', 45),
(1452, '1452', 'عين الصفراء', 'Ain Sefra', 'Ain Sefra', 45),
(1453, '1453', 'عسلة', 'Asla', 'Asla', 45),
(1454, '1454', 'جنين بورزق', 'Djenienne Bourezg', 'Djenienne Bourezg', 45),
(1455, '1455', 'البيوض', 'El Biodh', 'El Biodh', 45),
(1456, '1456', 'القصدير', 'Kasdir', 'Kasdir', 45),
(1457, '1457', 'مكمن بن عمار', 'Makmen Ben Amar', 'Makmen Ben Amar', 45),
(1458, '1458', 'المشرية', 'Mecheria', 'Mecheria', 45),
(1459, '1459', 'مغرار', 'Moghrar', 'Moghrar', 45),
(1460, '1460', 'النعامة', 'Naama', 'Naama', 45),
(1461, '1461', 'سفيسيفة', 'Sfissifa', 'Sfissifa', 45),
(1462, '1462', 'تيوت', 'Tiout', 'Tiout', 45),
(1463, '1463', 'أغلال', 'Aghlal', 'Aghlal', 46),
(1464, '1464', 'عين الأربعاء', 'Ain El Arbaa', 'Ain El Arbaa', 46),
(1465, '1465', 'عين الكيحل', 'Ain Kihal', 'Ain Kihal', 46),
(1466, '1466', 'عين تموشنت', 'Ain Temouchent', 'Ain Temouchent', 46),
(1467, '1467', 'عين الطلبة', 'Ain Tolba', 'Ain Tolba', 46),
(1468, '1468', 'عقب الليل', 'Aoubellil', 'Aoubellil', 46),
(1469, '1469', 'بني صاف', 'Beni Saf', 'Beni Saf', 46),
(1470, '1470', 'بوزجار', 'Bouzedjar', 'Bouzedjar', 46),
(1471, '1471', 'شعبة اللحم', 'Chaabat El Ham', 'Chaabat El Ham', 46),
(1472, '1472', 'شنتوف', 'Chentouf', 'Chentouf', 46),
(1473, '1473', 'العامرية', 'El Amria', 'El Amria', 46),
(1474, '1474', 'المالح', 'El Maleh', 'El Maleh', 46),
(1475, '1475', 'المساعيد', 'El Messaid', 'El Messaid', 46),
(1476, '1476', 'الأمير عبد القادر', 'Emir Abdelkader', 'Emir Abdelkader', 46),
(1477, '1477', 'حمام بوحجر', 'Hammam Bou Hadjar', 'Hammam Bou Hadjar', 46),
(1478, '1478', 'الحساسنة', 'Hassasna', 'Hassasna', 46),
(1479, '1479', 'حاسي الغلة', 'Hassi El Ghella', 'Hassi El Ghella', 46),
(1480, '1480', 'وادي برقش', 'Oued Berkeche', 'Oued Berkeche', 46),
(1481, '1481', 'وادي الصباح', 'Oued Sebbah', 'Oued Sebbah', 46),
(1482, '1482', 'أولاد بوجمعة', 'Ouled Boudjemaa', 'Ouled Boudjemaa', 46),
(1483, '1483', 'أولاد الكيحل', 'Ouled Kihal', 'Ouled Kihal', 46),
(1484, '1484', 'ولهاصة الغرابة', 'Oulhaca El Gheraba', 'Oulhaca El Gheraba', 46),
(1485, '1485', 'سيدي بن عدة', 'Sidi Ben Adda', 'Sidi Ben Adda', 46),
(1486, '1486', 'سيدي بومدين', 'Sidi Boumediene', 'Sidi Boumediene', 46),
(1487, '1487', 'سيدي ورياش', 'Sidi Ouriache', 'Sidi Ouriache', 46),
(1488, '1488', 'سيدي صافي', 'Sidi Safi', 'Sidi Safi', 46),
(1489, '1489', 'تامزورة', 'Tamzoura', 'Tamzoura', 46),
(1490, '1490', 'تارقة', 'Terga', 'Terga', 46),
(1491, '1491', 'بريان', 'Berriane', 'Berriane', 47),
(1492, '1492', 'بونورة', 'Bounoura', 'Bounoura', 47),
(1493, '1493', 'ضاية بن ضحوة', 'Dhayet Bendhahoua', 'Dhayet Bendhahoua', 47),
(1494, '1494', 'العطف', 'El Atteuf', 'El Atteuf', 47),
(1495, '1495', 'المنيعة', 'El Meniaa', 'El Meniaa', 58),
(1496, '1496', 'غرداية', 'Ghardaia', 'Ghardaia', 47),
(1497, '1497', 'القرارة', 'El Guerrara', 'El Guerrara', 47),
(1498, '1498', 'حاسي الفحل', 'Hassi Fehal', 'Hassi Fehal', 58),
(1499, '1499', 'حاسي القارة', 'Hassi Gara', 'Hassi Gara', 58),
(1500, '1500', 'المنصورة', 'Mansoura', 'Mansoura', 47),
(1501, '1501', 'متليلي', 'Metlili', 'Metlili', 47),
(1502, '1502', 'سبسب', 'Sebseb', 'Sebseb', 47),
(1503, '1503', 'زلفانة', 'Zelfana', 'Zelfana', 47),
(1504, '1504', 'عين الرحمة', 'Ain Rahma', 'Ain Rahma', 48),
(1505, '1505', 'عين طارق', 'Ain-Tarek', 'Ain-Tarek', 48),
(1506, '1506', 'عمي موسى', 'Ammi Moussa', 'Ammi Moussa', 48),
(1507, '1507', 'بلعسل بوزقزة', 'Belaassel Bouzagza', 'Belaassel Bouzagza', 48),
(1508, '1508', 'بن داود', 'Bendaoud', 'Bendaoud', 48),
(1509, '1509', 'بني درقن', 'Beni Dergoun', 'Beni Dergoun', 48),
(1510, '1510', 'بني زنطيس', 'Beni Zentis', 'Beni Zentis', 48),
(1511, '1511', 'دار بن عبد الله', 'Dar Ben Abdelah', 'Dar Ben Abdelah', 48),
(1512, '1512', 'جديوية', 'Djidiouia', 'Djidiouia', 48),
(1513, '1513', 'الحاسي', 'El Hassi', 'El Hassi', 48),
(1514, '1514', 'الحمادنة', 'El H\'madna', 'El H\'madna', 48),
(1515, '1515', 'الولجة', 'El Ouldja', 'El Ouldja', 48),
(1516, '1516', 'القطار', 'El-Guettar', 'El-Guettar', 48),
(1517, '1517', 'المطمر', 'El-Matmar', 'El-Matmar', 48),
(1518, '1518', 'حد الشكالة', 'Had Echkalla', 'Had Echkalla', 48),
(1519, '1519', 'حمري', 'Hamri', 'Hamri', 48),
(1520, '1520', 'القلعة', 'Kalaa', 'Kalaa', 48),
(1521, '1521', 'لحلاف', 'Lahlef', 'Lahlef', 48),
(1522, '1522', 'مازونة', 'Mazouna', 'Mazouna', 48),
(1523, '1523', 'مديونة', 'Mediouna', 'Mediouna', 48),
(1524, '1524', 'منداس', 'Mendes', 'Mendes', 48),
(1525, '1525', 'مرجة سيدي عابد', 'Merdja Sidi Abed', 'Merdja Sidi Abed', 48),
(1526, '1526', 'واريزان', 'Ouarizane', 'Ouarizane', 48),
(1527, '1527', 'وادي الجمعة', 'Oued El Djemaa', 'Oued El Djemaa', 48),
(1528, '1528', 'وادي السلام', 'Oued Essalem', 'Oued Essalem', 48),
(1529, '1529', 'وادي رهيو', 'Oued-Rhiou', 'Oued-Rhiou', 48),
(1530, '1530', 'أولاد يعيش', 'Ouled Aiche', 'Ouled Aiche', 48),
(1531, '1531', 'أولاد سيدي الميهوب', 'Ouled Sidi Mihoub', 'Ouled Sidi Mihoub', 48),
(1532, '1532', 'الرمكة', 'Ramka', 'Ramka', 48),
(1533, '1533', 'غليزان', 'Relizane', 'Relizane', 48),
(1534, '1534', 'سيدي  خطاب', 'Sidi Khettab', 'Sidi Khettab', 48),
(1535, '1535', 'سيدي لزرق', 'Sidi Lazreg', 'Sidi Lazreg', 48),
(1536, '1536', 'سيدي أمحمد بن علي', 'Sidi M\'hamed Benali', 'Sidi M\'hamed Benali', 48),
(1537, '1537', 'سيدي امحمد بن عودة', 'Sidi M\'hamed Benaouda', 'Sidi M\'hamed Benaouda', 48),
(1538, '1538', 'سيدي سعادة', 'Sidi Saada', 'Sidi Saada', 48),
(1539, '1539', 'سوق الحد', 'Souk El Had', 'Souk El Had', 48),
(1540, '1540', 'يلل', 'Yellel', 'Yellel', 48),
(1541, '1541', 'زمورة', 'Zemmoura', 'Zemmoura', 48);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_02_05`
--

CREATE TABLE `t_01_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_05` double DEFAULT NULL,
  `f_03` double NOT NULL,
  `f_04` double NOT NULL,
  `f_02` varchar(10) NOT NULL,
  `f_01` int(11) NOT NULL,
  `f_06` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_02_05`
--

INSERT INTO `t_01_02_05` (`f_00`, `f_05`, `f_03`, `f_04`, `f_02`, `f_01`, `f_06`) VALUES
(1, 2, 35.80539, -0.24097, 'Bethioua', 0, NULL),
(2, 2, 35.8619, -0.3445, 'Mers El Ha', 0, NULL),
(3, 2, 35.80739, -0.34313, 'Ain Bya', 0, NULL),
(4, 575, 35.2345, 6.1234, 'Sefiane', 0, NULL),
(5, 52, 36.7509, 5.0586, 'Béjaïa', 0, NULL),
(6, 495, 34.8333, 6.071, 'Tolga', 0, NULL),
(7, 495, 35.0341, 5.5952, 'Ferme Omar', 0, NULL),
(8, -1.6, 34.6863, 6.0942, 'Ain Naga', 0, NULL),
(9, 450, 36.3361, 4.3559, 'Béni Manso', 0, NULL),
(10, 1100, 34.5066, 3.4704, 'Moudjebara', 0, NULL),
(11, 965.96, 32.91, 0.545, 'Labiodh Si', 0, NULL),
(12, 280, 33.9327, 5.9304, 'Djamaa', 0, NULL),
(13, 496, 32.4775, 3.7478, 'Al Atteuf', 0, NULL),
(14, 420, 28.040833, 9.543056, 'In Amenas', 0, NULL),
(15, 252, 29.7402, 6.7022, 'Rhourde No', 0, NULL),
(16, 350, 29.967215, 9.422346, 'Deb Deb', 0, NULL),
(17, 380, 27, 8.5, 'Mederba', 0, NULL),
(18, 470, 28.1464, 6.8256, 'TFT', 0, NULL),
(19, 600, 28.6422, 2.8251, 'Krechba', 0, NULL),
(20, 880, 33.952, 3.921, 'Laghouat', 0, NULL),
(21, 885, 33.8028, 2.875, 'Milok', 0, NULL),
(22, 715, 33.4174, 3.5502, 'Hassi Dela', 0, NULL),
(23, 856, 33.7891, 2.5834, 'M\'seka A', 0, NULL),
(24, 875, 33.8089, 2.5903, 'M\'seka B', 0, NULL),
(25, 835, 33.7689, 2.5761, 'M\'seka C', 0, NULL),
(26, 890, 33.6245, 2.7389, 'Timzhert A', 0, NULL),
(27, 912, 33.6412, 2.7412, 'Timzhert B', 0, NULL),
(28, 875, 33.6078, 2.7361, 'Timzhert C', 0, NULL),
(29, 778, 32.935, 3.2684, 'Hassi R\'me', 0, NULL),
(30, 777, 32.9472, 3.1706, 'Hassi R\'me', 0, NULL),
(31, 926, 36.1234, 6.789, 'Mechtatine', 0, NULL),
(32, 600, 35.7186, 4.5233, 'Ksob Dam', 0, NULL),
(33, 10, 35.82992, -0.32427, 'Mohgoun pl', 0, NULL),
(34, 5, 35.8349, -0.2384, 'Arzew', 0, NULL),
(35, 973, 35.372, 1.325, 'Djebila', 0, NULL),
(36, 1018, 35.2876, 1.4521, 'Nador A', 0, NULL),
(37, 1045, 35.3078, 1.4567, 'Nador B', 0, NULL),
(38, 995, 35.2678, 1.4475, 'Nador C', 0, NULL),
(39, 971, 35.1027, 1.7056, 'Medarreg A', 0, NULL),
(40, 995, 35.1211, 1.7089, 'Medarreg B', 0, NULL),
(41, 950, 35.0845, 1.702, 'Medarreg C', 0, NULL),
(42, 1032, 35.3453, 1.3187, 'Zaaroura', 0, NULL),
(43, 1080, 35.1835, 1.4959, 'Sougueur', 0, NULL),
(44, 185, 35.1927, 1.7056, 'Faïdja', 0, NULL),
(45, 983, 35.425, 1.425, 'Zaaroura H', 0, NULL),
(46, 5, 36.8767, 6.9056, 'Skikda', 0, NULL),
(47, 18, 36.8854, 6.924, 'Skikda', 0, NULL),
(48, 8, 36.8654, 6.92, 'Flifla', 0, NULL),
(49, 80, 35.7373, 0.556, 'Bourmadia', 0, NULL),
(50, 81, 35.7322, 0.6125, 'Bourmadia ', 0, NULL),
(51, 650, 35.6546, 0.8211, 'Kenenda A', 0, NULL),
(52, 680, 35.6698, 0.8267, 'Kenenda B', 0, NULL),
(53, 620, 35.6388, 0.8152, 'Kenenda C', 0, NULL),
(54, 397, 34.4167, 5.0667, 'Ouled Djel', 0, NULL),
(55, 389, 34.5, 5.25, 'Chaiba', 0, NULL),
(56, 246, 31.006256, 8.133856, 'El Borma', 0, NULL),
(57, 267, 30.2714, 6.4155, 'Gassi Toui', 0, NULL),
(58, 182, 33.08333, 6.05139, 'Nezla', 0, NULL),
(59, 245, 31.951, 5.331, 'Ouargla', 0, NULL),
(60, 260, 31.9326, 5.3268, 'Ouargla', 0, NULL),
(61, 185, 30.5588, 5.4531, 'Hassi Berk', 0, NULL),
(62, 156, 31.941, 6.071, 'Haoud El H', 0, NULL),
(63, 151, 31.8894, 5.9731, 'Haoud El H', 0, NULL),
(64, 166, 31.7876, 6.0518, 'Zina', 0, NULL),
(65, 160, 31.6295, 6.19, 'Hassi Mess', 0, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_03_01`
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
-- Déchargement des données de la table `t_01_03_01`
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
(19, 'DGFG', 'إدارة تسيير التدفق', 'Flow Management Department', 'Département Getion Flux (Gestion Flux)', 10, 8),
(20, 'DOG', 'إدارة عمليات خط أنابيب الغاز', 'Gas Pipeline Operations Department', 'Département Opérations Gasoduc', 10, 8);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_03_02`
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
-- Déchargement des données de la table `t_01_03_02`
--

INSERT INTO `t_01_03_02` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`, `f_05`) VALUES
(1, 'VP-EP', 'VP-EP', 'VP-EP', 'VP-EP', 2),
(2, 'CDGF', 'Chef Département Gestion Flux', 'Cheef Of Stream Management Department ', 'Chef Département Gestion Flux', 19);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_03_03`
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
-- Déchargement des données de la table `t_01_03_03`
--

INSERT INTO `t_01_03_03` (`f_00`, `f_08`, `f_09`, `f_05`, `f_06`, `f_07`, `f_02`, `f_04`, `f_01`, `f_03`, `f_11`, `f_10`, `f_12`, `f_13`) VALUES
(1, NULL, NULL, '1986-06-26 00:00:00.000000', NULL, 'Houcine Day', 'ABIR', 'ABIR', 'MEDJERAB', 'MEDJERAB', NULL, NULL, 4, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `t_01_03_04`
--

CREATE TABLE `t_01_03_04` (
  `f_14` varchar(50) DEFAULT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_15` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_01_03_04`
--

INSERT INTO `t_01_03_04` (`f_14`, `f_00`, `f_15`) VALUES
('6001', 1, 2);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_01_01`
--

CREATE TABLE `t_02_01_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_01`
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
-- Structure de la table `t_02_01_02`
--

CREATE TABLE `t_02_01_02` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_02`
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
-- Structure de la table `t_02_01_03`
--

CREATE TABLE `t_02_01_03` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_03`
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
-- Structure de la table `t_02_01_04`
--

CREATE TABLE `t_02_01_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_04`
--

INSERT INTO `t_02_01_04` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'PUMP-STN', 'محطة ضخ', 'Pumping Station', 'Station de Pompage'),
(2, 'COMP-STN', 'محطة ضغط', 'Compression Station', 'Station de Compression'),
(3, 'PR-STN', 'محطة خفض الضغط', 'Pressure Reduction Station', 'Station de Détente'),
(4, 'PREG-STN', 'محطة تنظيم الضغط', 'Pressure Regulation Station', 'Station de Régulation de Pression'),
(5, 'METER-STN', 'محطة قياس', 'Metering Station', 'Station de Comptage'),
(6, 'CUTOFF-STN', 'محطة قطع', 'Cut-Off Station', 'Poste de Coupure'),
(7, 'SECT-STN', 'محطة تقسيم', 'Sectionalizing Station', 'Poste de Sectionnement'),
(8, 'EXPORT-TERM', 'محطة تصدير', 'Export Terminal', 'Terminal d\'Exportation'),
(9, 'IMPORT-TERM', 'محطة استيراد', 'Import Terminal', 'Terminal d\'Importation'),
(10, 'STORAGE-TERM', 'محطة تخزين', 'Storage Terminal', 'Terminal de Stockage'),
(11, 'TRANSFER-TERM', 'محطة نقل', 'Transfer Terminal', 'Terminal de Transfert'),
(12, 'DIST-TERM', 'محطة توزيع', 'Distribution Terminal', 'Terminal de Distribution'),
(13, 'REFINERY-TERM', 'محطة واجهة المصفاة', 'Refinery Interface Terminal', 'Terminal Interface Raffinerie'),
(14, 'LNG-TERM', 'محطة الغاز الطبيعي المسال', 'LNG Terminal', 'Terminal GNL'),
(15, 'LPG-TERM', 'محطة غاز البترول المسال', 'LPG Terminal', 'Terminal GPL'),
(16, 'COND-TERM', 'محطة المكثفات', 'Condensate Terminal', 'Terminal de Condensat'),
(17, 'OIL-FIELD', 'حقل نفطي', 'Oil Field', 'Champ Pétrolier'),
(18, 'GAS-FIELD', 'حقل غاز', 'Gas Field', 'Champ Gazier'),
(19, 'COND-FIELD', 'حقل مكثفات', 'Condensate Field', 'Champ de Condensat');

-- --------------------------------------------------------

--
-- Structure de la table `t_02_01_05`
--

CREATE TABLE `t_02_01_05` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_05`
--

INSERT INTO `t_02_01_05` (`f_00`) VALUES
(1),
(2),
(3),
(4),
(5),
(6),
(7);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_01_06`
--

CREATE TABLE `t_02_01_06` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_06`
--

INSERT INTO `t_02_01_06` (`f_00`) VALUES
(8),
(9),
(10),
(11),
(12),
(13),
(14),
(15),
(16);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_01_07`
--

CREATE TABLE `t_02_01_07` (
  `f_00` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_07`
--

INSERT INTO `t_02_01_07` (`f_00`) VALUES
(17),
(18),
(19);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_01_08`
--

CREATE TABLE `t_02_01_08` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL,
  `f_02` varchar(100) DEFAULT NULL,
  `f_03` varchar(100) DEFAULT NULL,
  `f_04` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_01_08`
--

INSERT INTO `t_02_01_08` (`f_00`, `f_01`, `f_02`, `f_03`, `f_04`) VALUES
(1, 'T', 'توربين', 'Turbine', 'Turbine'),
(2, 'M', 'محرك', 'Motor', 'Moteur'),
(3, 'EM', 'محرك كهربائي', 'Electric Motor', 'Moteur Électrique'),
(4, 'P', 'مضخة', 'Pump', 'Pompe'),
(5, 'TP', 'مضخة توربينية', 'Turbo-Pump', 'Turbo-Pompe'),
(6, 'EP', 'مضخة كهربائية', 'Electro-Pump', 'Électro-Pompe'),
(7, 'B', 'مضخة تعزيز', 'Booster', 'Booster'),
(8, 'C', 'ضاغط', 'Compressor', 'Compresseur'),
(9, 'TC', 'ضاغط توربيني', 'Turbo-Compressor', 'Turbo-Compresseur'),
(10, 'EC', 'ضاغط كهربائي', 'Electric Compressor', 'Compresseur Électrique'),
(11, 'TA', 'مولد توربيني', 'Turbo-Alternator', 'Turbo-Alternateur'),
(12, 'GE', 'مجموعة كهربائية', 'Generator Set', 'Groupe Électrogène'),
(13, 'HX', 'مبادل حراري', 'Heat Exchanger', 'Échangeur Thermique'),
(14, 'SEP', 'فاصل', 'Separator', 'Séparateur'),
(15, 'TK', 'خزان تخزين', 'Storage Tank', 'Réservoir de Stockage'),
(16, 'PV', 'وعاء ضغط', 'Pressure Vessel', 'Réservoir sous Pression'),
(17, 'CV', 'صمام تحكم', 'Control Valve', 'Vanne de Régulation'),
(18, 'IV', 'صمام عزل', 'Isolation Valve', 'Vanne d\'Isolement'),
(19, 'SV', 'صمام أمان', 'Safety Valve', 'Soupape de Sûreté'),
(20, 'CHV', 'صمام فحص', 'Check Valve', 'Clapet Anti-Retour'),
(21, 'FM', 'مقياس تدفق', 'Flow Meter', 'Débitmètre'),
(22, 'PT', 'مرسل ضغط', 'Pressure Transmitter', 'Transmetteur de Pression'),
(23, 'TT', 'مرسل حرارة', 'Temperature Transmitter', 'Transmetteur de Température'),
(24, 'LT', 'مرسل مستوى', 'Level Transmitter', 'Transmetteur de Niveau'),
(25, 'ANA', 'محلل', 'Analyzer', 'Analyseur'),
(26, 'PLC', 'متحكم منطقي قابل للبرمجة', 'PLC', 'Automate Programmable'),
(27, 'RTU', 'وحدة طرفية بعيدة', 'RTU', 'Unité Terminale Distante'),
(28, 'DCS', 'نظام تحكم موزع', 'DCS', 'Système de Contrôle Distribué'),
(29, 'SCADA', 'نظام SCADA', 'SCADA System', 'Système SCADA'),
(30, 'CLR', 'مبرد', 'Cooler', 'Refroidisseur'),
(31, 'HTR', 'سخان', 'Heater', 'Réchauffeur'),
(32, 'FLT', 'مرشح', 'Filter', 'Filtre'),
(33, 'PIG', 'مطلق/مستقبل كاشطة', 'Scraper Launcher/Receiver', 'Lanceur/Récepteur de Racleur');

-- --------------------------------------------------------

--
-- Structure de la table `t_02_02_01`
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
-- Déchargement des données de la table `t_02_02_01`
--

INSERT INTO `t_02_02_01` (`f_00`, `f_01`, `f_05`, `f_02`, `f_03`, `f_04`, `f_07`, `f_09`, `f_08`, `f_06`) VALUES
(1, 'GN', 0.7, 'الغاز الطبيعي', 'Natural Gas', 'Gaz naturel', -188, b'1', 0.01, 0.01),
(2, 'PB', 0.85, 'النفط الخام', 'Crude Oil', 'Pétrole brut', -30, b'1', 1.5, 10.5),
(3, 'COND', 0.75, 'المكثفات', 'Condensate', 'Condensat', -40, b'1', 0.05, 0.8),
(4, 'GPL', 0.55, 'غاز البترول المسال', 'Liquefied Petroleum Gas', 'Gaz de Pétrole Liquéfié (GPL)', -104, b'1', 0.02, 0.15);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_02_02`
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
-- Déchargement des données de la table `t_02_02_02`
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
-- Structure de la table `t_02_02_03`
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
-- Déchargement des données de la table `t_02_02_03`
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
-- Structure de la table `t_02_02_04`
--

CREATE TABLE `t_02_02_04` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(20) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_02_04`
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
-- Structure de la table `t_02_02_05`
--

CREATE TABLE `t_02_02_05` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(100) DEFAULT NULL,
  `f_02` varchar(20) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_02_05`
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
-- Structure de la table `t_02_03_01`
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
-- Déchargement des données de la table `t_02_03_01`
--

INSERT INTO `t_02_03_01` (`f_00`, `f_01`, `f_04`, `f_05`, `f_03`, `f_02`, `f_06`, `f_07`) VALUES
(1, 'CNDG', '1985-01-01', NULL, '1983-01-01', 'Centre National de Dispatching Gaz', 1, 10),
(2, 'CDHL', '1982-01-01', NULL, '1980-01-01', 'Centre de Dispatching des Hydrocarbures Liquides', 1, 10),
(3, 'PEHZ', '1965-01-01', NULL, '1964-01-01', 'Port d\'Exportation des Hydrocarbures d\'Arzew', 1, 10),
(4, 'PEHK', '1971-01-01', NULL, '1970-01-01', 'Port d\'Exportation des Hydrocarbures de Skikda', 1, 10),
(5, 'PEHB', '1961-01-01', NULL, '1960-01-01', 'Port d\'Exportation des Hydrocarbures de Bejaia', 1, 10),
(6, 'ULGNZ', '1964-01-01', NULL, '1964-01-01', 'Usine de Liquéfaction de Gaz Naturel d\'Arzew', 1, 10),
(7, 'ULGNK', '1972-01-01', NULL, '1970-01-01', 'Usine de Liquéfaction de Gaz Naturel de Skikda', 1, 10),
(8, 'TEO', '1972-01-01', NULL, '1970-01-01', 'Terminal d\'Exportation d\'Oued Saf Saf', 1, 10),
(9, 'TED', '1972-01-01', NULL, '1970-01-01', 'Terminal d\'Exportation du DEB DEB', 1, 10),
(10, 'RA1G', '1964-01-01', NULL, '1961-01-01', 'Raffinerie d\'Alger', 1, 10),
(11, 'RA1Z', '1972-01-01', NULL, '1970-01-01', 'Raffinerie d\'Arzew', 1, 10),
(12, 'RA2K', '2009-01-01', NULL, '2009-01-01', 'Raffinerie de Skikda', 1, 10),
(13, 'RA1K', '1981-01-01', NULL, '1980-01-01', 'Raffinerie de Skikda', 1, 10),
(14, 'RHMD2', '2007-01-01', NULL, '2006-01-01', 'Raffinerie de Hassi Messaoud', 1, 10),
(15, 'RA1D', '1980-01-01', NULL, '1978-01-01', 'Raffinerie d\'Adrar', 1, 10),
(16, 'CP2K', '2004-01-01', NULL, '2004-01-01', 'Complexe Pétrochimique de Skikda', 1, 10),
(17, 'SP1-OZ1/OZ2', '2003-01-01', NULL, '2003-01-01', 'Station de Pompage de Haoud El Hamra', 1, 10),
(18, 'SP2-OZ1/OZ2', '2004-01-01', NULL, '2004-01-01', 'Station de Pompage d\'Ouargla', 1, 10),
(19, 'SP3-OZ1/OZ2', '2003-01-01', NULL, '2003-01-01', 'Station de Pompage d\'Al Atteuf', 1, 10),
(20, 'SP4-OZ1/OZ2', '2004-01-01', NULL, '2004-01-01', 'Station de Pompage de Hassi R\'mel', 1, 10),
(21, 'SP5-OZ1/OZ2', '2003-01-01', NULL, '2003-01-01', 'Station de Pompage de Milok', 1, 10),
(22, 'SP6-OZ1/OZ2', '2004-01-01', NULL, '2004-01-01', 'Station de Pompage de Djebil', 1, 10),
(23, 'SP1-OK1', '1972-01-01', NULL, '1972-01-01', 'Station de Pompage de Haoud El Hamra', 1, 10),
(24, 'SP2-OK1', '1976-01-01', NULL, '1976-01-01', 'Station de Pompage de Djamaa', 1, 10),
(25, 'SP3-OK1', '1972-01-01', NULL, '1972-01-01', 'Station de Pompage de Selga', 1, 10),
(26, 'SP3 Bis-OK1', '2005-01-01', NULL, '2005-01-01', 'Station de Pompage de Selga', 1, 10),
(27, 'SP4-OK1', '1976-01-01', NULL, '1976-01-01', 'Station de Pompage de Boumagueur', 1, 10),
(28, 'SP1-ROB1', '2019-07-01', NULL, '2019-07-01', 'Station de Pompage de Haoud El Hamra', 1, 10),
(29, 'SP1 Bis-OB1', '2007-01-01', NULL, '2007-01-01', 'Station de Pompage de Djamaa', 1, 10),
(30, 'SP2-OB1', '2008-08-24', NULL, '2008-08-24', 'Station de Pompage d\'El Outaya', 1, 10),
(31, 'SP3-OB1', '2008-01-01', NULL, '2008-01-01', 'Station de Pompage de M\'sila', 1, 10),
(32, 'SP1-OG1', '2006-06-27', NULL, '2006-06-27', 'Station de Pompage de Beni Mansour', 1, 10),
(33, 'SP1-OT1', '1960-01-01', NULL, '1960-01-01', 'Station de Pompage d\'In Amenas', 1, 10),
(34, 'SP2-OT1', '1960-01-01', NULL, '1960-01-01', 'Station de Pompage du Deb-Deb', 1, 10),
(35, 'OPS1-OH1', '1975-01-01', NULL, '1975-01-01', 'Station de Pompage d\'Ohanet', 2, 10),
(36, 'OPS2-OH1', '1975-01-01', NULL, '1975-01-01', 'Station de Pompage de Mederba', 2, 10),
(37, 'OPS3-OH1', '1975-01-01', NULL, '1975-01-01', 'Station de Pompage de Gassi Touil', 2, 10),
(38, 'OPS1-OD1', '1980-01-01', NULL, '1980-01-01', 'Station de Pompage d\'El Borma', 1, 10),
(39, 'OPS1-OH2', '2003-03-11', NULL, '2003-03-11', 'Station de Pompage de Mesdar', 1, 10),
(40, 'SP1-OH3/OH4', '2003-11-02', NULL, '2003-11-02', 'Station de Pompage de Hassi Berkine', 1, 10),
(41, 'SP2-OH3/OH4', '2008-01-01', NULL, '2003-04-10', 'Station de Pompage de Nezla', 1, 10),
(42, 'SP4-NZ1', '1978-11-01', NULL, '1978-11-01', 'Station de Pompage de Hassi R\'mel', 1, 10),
(43, 'SP5-NZ1', '1978-11-01', NULL, '1978-11-01', 'Station de Pompage de Milok', 1, 10),
(44, 'SP6-NZ1', '1978-11-01', NULL, '1978-11-01', 'Station de Pompage de Medareg', 1, 10),
(45, 'SP1-NK1', '2009-01-01', NULL, '2009-01-01', 'Station de Pompage de Haoud El Hamra', 1, 10),
(46, 'SP3-NK1', '2009-01-01', NULL, '2009-01-01', 'Station de Pompage d\'El Outaya', 1, 10),
(47, 'SP1-LZ1', '1984-02-02', NULL, '1984-02-02', 'Station de Pompage de Hassi R\'mel', 1, 10),
(48, 'SP2-LZ1', '2006-10-07', NULL, '2006-10-07', 'Station de Pompage de Milok', 1, 10),
(49, 'SP1-LZ2', '2007-11-01', NULL, '2007-11-01', 'Station de Pompage de Hassi R\'mel', 1, 10),
(50, 'SPRN-LR1/DLR1/ELR1', '1999-10-29', NULL, '1999-10-29', 'Station de Pompage de Rhoud Nouss', 1, 10),
(51, 'SP1-LR1/DLR1/ELR1', '2018-02-01', NULL, '2018-02-01', 'Station de Pompage de Haoud El Hamra', 1, 10),
(52, 'SP2-LR1/DLR1/ELR1', '1996-01-01', NULL, '1996-01-01', 'Station de Pompage d\'Ouargla', 1, 10),
(53, 'SC1-GZ0', '1965-01-01', '2015-09-01', '1965-01-01', 'Station de Compression de Laghouat', 8, 10),
(54, 'SC2-GZ0', '1972-01-01', '2015-09-01', '1972-01-01', 'Station de Compression de Medarreg', 8, 10),
(55, 'SC3-GZ0', '1965-01-01', '2015-09-01', '1965-01-01', 'Station de Compression de Tiaret', 8, 10),
(56, 'SC4-GZ0', '1972-01-01', '2015-09-01', '1972-01-01', 'Station de Compression de Relizane', 8, 10),
(57, 'SC1-GZ1/GZ2/GZ3', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Timzhert', 1, 10),
(58, 'SC2-GZ1/GZ2/GZ3', '1978-01-01', NULL, '1978-01-01', 'Station de Compression de M\'seka', 1, 10),
(59, 'SC3-GZ1/GZ2/GZ3', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Medarreg', 1, 10),
(60, 'SC4-GZ1/GZ2/GZ3', '1978-01-01', NULL, '1978-01-01', 'Station de Compression de Djebel Nador', 1, 10),
(61, 'SC5-GZ1/GZ2/GZ3', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Kenenda', 1, 10),
(62, 'SC3-GPDF/EGPDF', '2004-02-14', NULL, '2004-02-14', 'Station de Compression de Sidi Cheikh', 1, 10),
(63, 'SCA-GK1/GK2', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Hassi Dellaa', 1, 10),
(64, 'SCB-GK1/GK2', '1978-01-01', NULL, '1978-01-01', 'Station de Compression de Ras El Miaad', 1, 10),
(65, 'SCC-GK1/GK2', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Chaiba', 1, 10),
(66, 'SCD-GK1/GK2', '1978-01-01', NULL, '1978-01-01', 'Station de Compression de Sefiane', 1, 10),
(67, 'SCE-GK1/GK2', '1979-01-01', NULL, '1979-01-01', 'Station de Compression de Mechtatine', 1, 10),
(68, 'SC2-GO1/GO2/GO3', '1996-01-01', NULL, '1996-01-01', 'Station de Compression d\'Ain Naga', 1, 10),
(69, 'SC3-GG1', '2006-09-01', NULL, '2006-09-01', 'Station de Compression de Moudjebara', 1, 10),
(70, 'SCTFT-GR1/GR2', '2007-06-01', NULL, '2007-06-01', 'Station de Compression de TFT', 1, 10),
(71, 'SC1-GR1/GR2', '2006-07-01', NULL, '2006-07-01', 'Station de Compression de Rhoude Nouss', 1, 10),
(72, 'SC2-GR1/GR2', '2003-10-04', NULL, '2003-10-04', 'Station de Compression de Zina', 1, 10),
(73, 'SC3-GR1/GR2', '2006-01-01', NULL, '2006-01-01', 'Station de Compression d\'Ouargla', 1, 10),
(74, 'SC4-GR1/GR2', '1986-01-01', '2000-01-01', '1986-01-01', 'Station de Compression de Hassi R\'mel', 8, 10),
(75, 'SCHR-GR1/GR2', '2018-01-01', NULL, '2018-01-01', 'Station de Compression de Hassi R\'mel', 1, 10),
(76, 'SCHR-GR5/GR7', '2017-01-01', NULL, '2017-01-01', 'Station de Compression de Hassi R\'mel', 1, 10),
(77, 'TEA', '1996-01-01', NULL, '1996-01-01', 'Terminal d\'Exportation d\'El Aricha', 1, 10),
(78, 'TEB', '2021-01-01', NULL, '2021-01-01', 'Terminal d\'Exportation de Beni Saf', 1, 10),
(79, 'TABM', '1961-01-01', NULL, '1961-01-01', 'Terminal d\'Arrivé Bordj Menail', 1, 10),
(80, 'SCLR', '1961-01-01', NULL, '1961-01-01', 'Station de Collecte de Reggane', 1, 10),
(81, 'OZ1', '1965-11-07', NULL, '1965-11-07', 'Pipeline OZ1', 1, 10),
(82, 'OZ2', '2003-08-18', NULL, '2003-08-18', 'Pipeline OZ2', 1, 10),
(83, 'OK1', '1972-01-01', NULL, '1972-01-01', 'Pipeline OK1', 1, 10),
(84, 'OB1', '1959-11-10', NULL, '1959-11-10', 'Pipeline OB1', 1, 10),
(85, 'OG1', '2005-07-04', NULL, '2005-07-04', 'Pipeline OG1', 1, 10),
(86, 'OT1', '1960-01-01', NULL, '1960-01-01', 'Pipeline OT1', 1, 10),
(87, 'OH1', '1982-01-01', NULL, '1982-01-01', 'Pipeline OH1', 1, 10),
(88, 'OD1', '1977-03-23', NULL, '1977-03-23', 'Pipeline OD1', 1, 10),
(89, 'OD3', '1999-12-11', NULL, '1999-12-11', 'Pipeline OD3', 1, 10),
(90, 'OH2', '1972-04-10', NULL, '1972-04-10', 'Pipeline OH2', 1, 10),
(91, 'OH3', '1999-01-01', NULL, '1999-01-01', 'Pipeline OH3', 1, 10),
(92, 'OH4', '2006-01-01', NULL, '2006-01-01', 'Pipeline OH4', 1, 10),
(93, 'NZ1', '1978-11-01', NULL, '1978-11-01', 'Pipeline NZ1', 1, 10),
(94, 'NK1', '2009-08-04', NULL, '2009-08-04', 'Pipeline NK1', 1, 10),
(95, 'NH2', '1960-01-01', NULL, '1960-01-01', 'Pipeline NH2', 1, 10),
(96, 'LZ1', '1984-02-08', NULL, '1984-02-08', 'Pipeline LZ1', 1, 10),
(97, 'LZ2', '2010-11-01', NULL, '2010-11-01', 'Pipeline LZ2', 1, 10),
(98, 'LR1', '1996-01-01', NULL, '1996-01-01', 'Pipeline LR1', 1, 10),
(99, 'DLR1', '2004-10-04', NULL, '2004-10-04', 'Pipeline DLR1', 1, 10),
(100, 'ELR1', '2014-01-01', NULL, '2014-01-01', 'Pipeline ELR1', 1, 10),
(101, 'GZ0', '1961-01-01', '2022-01-01', '1961-01-01', 'Pipeline GZ0', 8, 10),
(102, 'GZ1', '1976-01-01', NULL, '1976-01-01', 'Pipeline GZ1', 1, 10),
(103, 'GZ0 Bis', '2022-09-01', NULL, '2022-09-01', 'Pipeline GZ0 Bis', 1, 10),
(104, 'GZ2', '1982-01-01', NULL, '1982-01-01', 'Pipeline GZ2', 1, 10),
(105, 'GZ3', '1989-01-01', NULL, '1989-01-01', 'Pipeline GZ3', 1, 10),
(106, 'RGZ3', '2018-01-01', NULL, '2018-01-01', 'Pipeline RGZ3', 1, 10),
(107, 'GZ4', '2010-04-16', NULL, '2010-04-16', 'Pipeline GZ4', 1, 10),
(108, 'GPDF', '1996-01-01', NULL, '1996-01-01', 'Pipeline GPDF', 1, 10),
(109, 'EGPDF', '2021-01-01', NULL, '2021-01-01', 'Pipeline EGPDF', 1, 10),
(110, 'GK1', '1978-01-01', NULL, '1971-01-01', 'Pipeline GK1', 1, 10),
(111, 'GK2', '2001-01-01', NULL, '1998-01-01', 'Pipeline GK2', 1, 10),
(112, 'GK3', '2012-01-01', NULL, '2012-01-01', 'Pipeline GK3', 1, 10),
(113, 'GO1', '1982-01-01', NULL, '1982-01-01', 'Pipeline GO1', 1, 10),
(114, 'GO2', '1987-01-01', NULL, '1987-01-01', 'Pipeline GO2', 1, 10),
(115, 'GO3', '2010-01-01', NULL, '2008-01-01', 'Pipeline GO3', 1, 10),
(116, 'GG1', '1981-01-01', NULL, '1981-01-01', 'Pipeline GG1', 1, 10),
(117, 'RGG1', '2019-01-01', NULL, '2019-01-01', 'Pipeline RGG1', 1, 10),
(118, 'GR1', '1986-01-01', NULL, '1986-01-01', 'Pipeline GR1', 1, 10),
(119, 'GR2', '1999-01-01', NULL, '1999-01-01', 'Pipeline GR2', 1, 10),
(120, 'GR4', '2014-01-01', NULL, '2014-01-01', 'Pipeline GR4', 1, 10),
(121, 'GR6', '2017-01-01', NULL, '2017-01-01', 'Pipeline GR6', 1, 10),
(122, 'GR5', '2017-06-01', NULL, '2017-06-01', 'Pipeline GR5', 1, 10),
(123, 'GR7', '2019-12-01', NULL, '2019-12-01', 'Pipeline GR7', 1, 10),
(124, 'HRM', '1962-07-05', NULL, '1962-07-05', 'Hassi R’Mel Field', 1, 1),
(125, 'RN', '1962-07-05', NULL, '1962-07-05', 'Rhourde Nouss Field', 1, 1),
(126, 'Alrar', '1962-07-05', NULL, '1962-07-05', 'Alrar Field', 1, 1),
(127, 'TFT', '1962-07-05', NULL, '1962-07-05', 'Tin Fouyé Tabankort (TFT) Field', 1, 1),
(128, 'OHN', '1962-07-05', NULL, '1962-07-05', 'Ohanet Field', 1, 1),
(129, 'TGN', '1962-07-05', NULL, '1962-07-05', 'In Amenas (Tiguentourine) Field', 1, 1),
(130, 'Krechba', '1962-07-05', NULL, '1962-07-05', 'In Salah (Krechba group) Field', 1, 1),
(131, 'TMM', '1962-07-05', NULL, '1962-07-05', 'Timimoun Field', 1, 1),
(132, 'AT', '1962-07-05', NULL, '1962-07-05', 'Aïn T’sila Field', 1, 1),
(133, 'GT', '1962-07-05', NULL, '1962-07-05', 'Gassi Touil Field', 1, 1),
(134, 'HMD', '1962-07-05', NULL, '1962-07-05', 'Hassi Messaoud Field', 1, 1),
(135, 'HB', '1962-07-05', NULL, '1962-07-05', 'Haoud Berkaoui Field', 1, 1),
(136, 'ON', '1962-07-05', NULL, '1962-07-05', 'Oued Noumer Field', 1, 1),
(137, 'REB', '1962-07-05', NULL, '1962-07-05', 'Rhourde El Baguel Field', 1, 1),
(138, 'OUR', '1962-07-05', NULL, '1962-07-05', 'Ourhoud Field', 1, 1),
(139, 'HBC', '1962-07-05', NULL, '1962-07-05', 'Hassi Berkine Complex Field', 1, 1),
(140, 'ZRZ', '1962-07-05', NULL, '1962-07-05', 'Zarzaitine Field', 1, 1),
(141, 'HDLE', '1962-07-05', NULL, '1962-07-05', 'HDLE / HDLS Field', 1, 1),
(142, 'Tim', '1962-07-05', NULL, '1962-07-05', 'Timrhert / Tinrhert Field', 1, 1),
(143, 'GR3', '2019-12-01', NULL, '2019-12-01', 'Pipeline GR3', 1, 10);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_02`
--

CREATE TABLE `t_02_03_02` (
  `f_00` bigint(20) NOT NULL,
  `f_09` bigint(20) NOT NULL,
  `f_08` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_03`
--

CREATE TABLE `t_02_03_03` (
  `f_00` bigint(20) NOT NULL,
  `f_11` bigint(20) DEFAULT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_04`
--

CREATE TABLE `t_02_03_04` (
  `f_00` bigint(20) NOT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_05`
--

CREATE TABLE `t_02_03_05` (
  `f_00` bigint(20) NOT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_06`
--

CREATE TABLE `t_02_03_06` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(50) NOT NULL,
  `f_02` varchar(100) NOT NULL,
  `f_04` bigint(20) NOT NULL,
  `f_03` bigint(20) NOT NULL,
  `f_05` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_03_06`
--

INSERT INTO `t_02_03_06` (`f_00`, `f_01`, `f_02`, `f_04`, `f_03`, `f_05`) VALUES
(1, 'OZ1/OZ2', 'STC OZ1/OZ2', 1, 2, 2),
(2, 'OK1', 'STC OK1', 1, 2, 4),
(3, 'OB1/OG1', 'STC OB1/OG1', 1, 2, 3),
(4, 'OT1', 'STC OT1', 1, 2, 9),
(5, 'OH1', 'STC OH1', 1, 2, 9),
(6, 'OD1/OD3/OH2', 'STC OD1/OD3/OH2', 1, 2, 7),
(7, 'OH3/OH4', 'STC OH3/OH4', 1, 2, 7),
(8, 'NZ1', 'STC NZ1', 1, 3, 2),
(9, 'NK1', 'STC NK1', 1, 3, 4),
(10, 'NH2', 'STC NH2', 1, 3, 9),
(11, 'LZ1/LZ2', 'STC LZ1/LZ2', 1, 4, 2),
(12, 'LR1/DLR1/ELR1', 'STC LR1/DLR1/ELR1', 1, 4, 7),
(13, 'GZ0', 'STC GZ0', 1, 1, 2),
(14, 'GZ1/GZ2/GZ3', 'STC GZ1/GZ2/GZ3', 1, 1, 2),
(15, 'GZ4', 'STC GZ4', 1, 1, 2),
(16, 'GPDF', 'STC GPDF', 1, 1, 5),
(17, 'GK1/GK2', 'STC GK1/GK2', 1, 1, 4),
(18, 'GK3', 'STC GK3', 1, 1, 4),
(19, 'GO1/GO2/GO3', 'STC GO1/GO2/GO3', 1, 1, 6),
(20, 'GG1', 'STC GG1', 1, 1, 3),
(21, 'GR1/GR2/GR4/GR6', 'STC GR1/GR2/GR4/GR6', 1, 1, 9),
(22, 'GR5', 'STC GR5/GR7', 1, 1, 8);

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_07`
--

CREATE TABLE `t_02_03_07` (
  `f_16` double NOT NULL,
  `f_12` double NOT NULL,
  `f_14` double NOT NULL,
  `f_09` double NOT NULL,
  `f_08` double NOT NULL,
  `f_11` double NOT NULL,
  `f_10` double NOT NULL,
  `f_17` double NOT NULL,
  `f_13` double NOT NULL,
  `f_15` double NOT NULL,
  `f_00` bigint(20) NOT NULL,
  `f_24` bigint(20) NOT NULL,
  `f_23` bigint(20) NOT NULL,
  `f_18` bigint(20) NOT NULL,
  `f_19` bigint(20) NOT NULL,
  `f_20` bigint(20) NOT NULL,
  `f_22` bigint(20) NOT NULL,
  `f_21` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_03_08`
--

CREATE TABLE `t_02_03_08` (
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
-- Structure de la table `t_02_03_09`
--

CREATE TABLE `t_02_03_09` (
  `f_00` bigint(20) NOT NULL,
  `f_02` varchar(50) NOT NULL,
  `f_07` date DEFAULT NULL,
  `f_09` date DEFAULT NULL,
  `f_06` date DEFAULT NULL,
  `f_08` date DEFAULT NULL,
  `f_05` date DEFAULT NULL,
  `f_03` varchar(50) DEFAULT NULL,
  `f_01` varchar(100) NOT NULL,
  `f_04` varchar(100) DEFAULT NULL,
  `f_11` bigint(20) NOT NULL,
  `f_12` bigint(20) NOT NULL,
  `f_13` bigint(20) NOT NULL,
  `f_10` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_01`
--

CREATE TABLE `t_02_04_01` (
  `f_00` bigint(20) NOT NULL,
  `f_01` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `t_02_04_01`
--

INSERT INTO `t_02_04_01` (`f_00`, `f_01`) VALUES
(1, '00:00'),
(2, '04:00'),
(3, '08:00'),
(4, '12:00'),
(5, '16:00'),
(6, '20:00');

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_02`
--

CREATE TABLE `t_02_04_02` (
  `f_00` bigint(20) NOT NULL,
  `f_02` date NOT NULL,
  `f_01` double NOT NULL,
  `f_03` bigint(20) NOT NULL,
  `f_04` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_03`
--

CREATE TABLE `t_02_04_03` (
  `f_00` bigint(20) NOT NULL,
  `f_01` double NOT NULL,
  `f_02` date NOT NULL,
  `f_03` bigint(20) NOT NULL,
  `f_04` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_04`
--

CREATE TABLE `t_02_04_04` (
  `f_00` bigint(20) NOT NULL,
  `f_03` date NOT NULL,
  `f_01` double NOT NULL,
  `f_02` double DEFAULT NULL,
  `f_04` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_05`
--

CREATE TABLE `t_02_04_05` (
  `f_00` bigint(20) NOT NULL,
  `f_03` date NOT NULL,
  `f_01` double NOT NULL,
  `f_02` double DEFAULT NULL,
  `f_04` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `t_02_04_06`
--

CREATE TABLE `t_02_04_06` (
  `f_00` bigint(20) NOT NULL,
  `f_03` date NOT NULL,
  `f_02` double DEFAULT NULL,
  `f_01` double NOT NULL,
  `f_04` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `r_t000201_t000203`
--
ALTER TABLE `r_t000201_t000203`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000201_T000203_FK_02` (`f_02`);

--
-- Index pour la table `r_t000202_t000201`
--
ALTER TABLE `r_t000202_t000201`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000202_T000201_FK_02` (`f_02`);

--
-- Index pour la table `r_t000202_t000203`
--
ALTER TABLE `r_t000202_t000203`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000202_T000203_FK_02` (`f_02`);

--
-- Index pour la table `r_t000203_t000204`
--
ALTER TABLE `r_t000203_t000204`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T000203_T000204_FK_02` (`f_02`);

--
-- Index pour la table `r_t020303_t020307`
--
ALTER TABLE `r_t020303_t020307`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020303_T020307_FK_02` (`f_02`);

--
-- Index pour la table `r_t020304_t020307`
--
ALTER TABLE `r_t020304_t020307`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020304_T020307_FK_02` (`f_02`);

--
-- Index pour la table `r_t020305_t020201`
--
ALTER TABLE `r_t020305_t020201`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020201_FK_02` (`f_02`);

--
-- Index pour la table `r_t020305_t020204`
--
ALTER TABLE `r_t020305_t020204`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020204_FK_02` (`f_02`);

--
-- Index pour la table `r_t020305_t020307`
--
ALTER TABLE `r_t020305_t020307`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020305_T020307_FK_02` (`f_02`);

--
-- Index pour la table `r_t020307_t010205`
--
ALTER TABLE `r_t020307_t010205`
  ADD PRIMARY KEY (`f_01`,`f_02`),
  ADD KEY `R_T020307_T010205_FK_02` (`f_02`);

--
-- Index pour la table `t_00_01_01`
--
ALTER TABLE `t_00_01_01`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_00_02_01`
--
ALTER TABLE `t_00_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_01_UK_01` (`f_01`);

--
-- Index pour la table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_00_02_02_UK_02` (`f_02`),
  ADD KEY `T_00_02_02_FK_01` (`f_08`);

--
-- Index pour la table `t_00_02_03`
--
ALTER TABLE `t_00_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_03_UK_01` (`f_01`);

--
-- Index pour la table `t_00_02_04`
--
ALTER TABLE `t_00_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_04_UK_01` (`f_01`);

--
-- Index pour la table `t_00_02_05`
--
ALTER TABLE `t_00_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_02_05_UK_01` (`f_01`);

--
-- Index pour la table `t_00_03_01`
--
ALTER TABLE `t_00_03_01`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_00_04_01_UK_01` (`f_03`);

--
-- Index pour la table `t_01_01_01`
--
ALTER TABLE `t_01_01_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_01_01_UK_01` (`f_03`);

--
-- Index pour la table `t_01_02_01`
--
ALTER TABLE `t_01_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_02_01_UK_02` (`f_04`);

--
-- Index pour la table `t_01_02_02`
--
ALTER TABLE `t_01_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_02_UK_01` (`f_04`);

--
-- Index pour la table `t_01_02_03`
--
ALTER TABLE `t_01_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_03_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_02_03_UK_02` (`f_04`);

--
-- Index pour la table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_02_04_UK_01` (`f_01`),
  ADD KEY `T_01_02_04_FK_01` (`f_05`);

--
-- Index pour la table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_02_05_FK_02` (`f_06`);

--
-- Index pour la table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_03_01_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_03_01_UK_02` (`f_04`),
  ADD KEY `T_01_03_01_FK_02` (`f_06`),
  ADD KEY `T_01_03_01_FK_01` (`f_05`);

--
-- Index pour la table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_01_03_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_01_03_02_UK_02` (`f_04`),
  ADD KEY `T_01_03_02_FK_01` (`f_05`);

--
-- Index pour la table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_03_03_FK_02` (`f_11`),
  ADD KEY `T_01_03_03_FK_01` (`f_10`),
  ADD KEY `T_01_03_03_FK_03` (`f_12`),
  ADD KEY `T_01_03_03_FK_04` (`f_13`);

--
-- Index pour la table `t_01_03_04`
--
ALTER TABLE `t_01_03_04`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_01_03_04_FK_01` (`f_15`);

--
-- Index pour la table `t_02_01_01`
--
ALTER TABLE `t_02_01_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_01_UK_01` (`f_03`);

--
-- Index pour la table `t_02_01_02`
--
ALTER TABLE `t_02_01_02`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_02_01_03`
--
ALTER TABLE `t_02_01_03`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_02_01_04`
--
ALTER TABLE `t_02_01_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_04_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_01_04_UK_02` (`f_04`);

--
-- Index pour la table `t_02_01_05`
--
ALTER TABLE `t_02_01_05`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_02_01_06`
--
ALTER TABLE `t_02_01_06`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_02_01_07`
--
ALTER TABLE `t_02_01_07`
  ADD PRIMARY KEY (`f_00`);

--
-- Index pour la table `t_02_01_08`
--
ALTER TABLE `t_02_01_08`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_01_08_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_01_08_UK_02` (`f_04`);

--
-- Index pour la table `t_02_02_01`
--
ALTER TABLE `t_02_02_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_01_UK_01` (`f_01`,`f_04`);

--
-- Index pour la table `t_02_02_02`
--
ALTER TABLE `t_02_02_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_02_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_02_02_UK_02` (`f_04`);

--
-- Index pour la table `t_02_02_03`
--
ALTER TABLE `t_02_02_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_03_UK_01` (`f_01`),
  ADD UNIQUE KEY `T_02_02_03_UK_02` (`f_04`);

--
-- Index pour la table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_04_UK_01` (`f_02`),
  ADD KEY `T_02_02_04_FK_02` (`f_04`),
  ADD KEY `T_02_02_04_FK_01` (`f_03`);

--
-- Index pour la table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_02_05_UK_01` (`f_02`),
  ADD KEY `T_02_02_05_FK_02` (`f_04`),
  ADD KEY `T_02_02_05_FK_01` (`f_03`);

--
-- Index pour la table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_01_UK_01` (`f_01`),
  ADD KEY `T_02_03_01_FK_01` (`f_06`),
  ADD KEY `T_02_03_01_FK_02` (`f_07`);

--
-- Index pour la table `t_02_03_02`
--
ALTER TABLE `t_02_03_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `UK4ym3d8ch54hfwugyr9n4f0v9c` (`f_09`),
  ADD KEY `T_02_03_02_FK_01` (`f_08`);

--
-- Index pour la table `t_02_03_03`
--
ALTER TABLE `t_02_03_03`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_03_FK_02` (`f_11`),
  ADD KEY `T_02_03_03_FK_01` (`f_10`);

--
-- Index pour la table `t_02_03_04`
--
ALTER TABLE `t_02_03_04`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_04_FK_01` (`f_10`);

--
-- Index pour la table `t_02_03_05`
--
ALTER TABLE `t_02_03_05`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_05_FK_01` (`f_10`);

--
-- Index pour la table `t_02_03_06`
--
ALTER TABLE `t_02_03_06`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_06_UK_01` (`f_01`),
  ADD KEY `T_02_03_06_FK_02` (`f_04`),
  ADD KEY `T_02_03_06_FK_01` (`f_03`),
  ADD KEY `T_02_03_06_FK_03` (`f_05`);

--
-- Index pour la table `t_02_03_07`
--
ALTER TABLE `t_02_03_07`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_07_FK_07` (`f_24`),
  ADD KEY `T_02_03_07_FK_06` (`f_23`),
  ADD KEY `T_02_03_07_FK_01` (`f_18`),
  ADD KEY `T_02_03_07_FK_02` (`f_19`),
  ADD KEY `T_02_03_07_FK_03` (`f_20`),
  ADD KEY `T_02_03_07_FK_05` (`f_22`),
  ADD KEY `T_02_03_07_FK_04` (`f_21`);

--
-- Index pour la table `t_02_03_08`
--
ALTER TABLE `t_02_03_08`
  ADD PRIMARY KEY (`f_00`),
  ADD KEY `T_02_03_08_FK_01` (`f_14`),
  ADD KEY `T_02_03_08_FK_02` (`f_15`),
  ADD KEY `T_02_03_08_FK_03` (`f_16`),
  ADD KEY `T_02_03_08_FK_04` (`f_17`);

--
-- Index pour la table `t_02_03_09`
--
ALTER TABLE `t_02_03_09`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_03_09_UK_01` (`f_02`),
  ADD KEY `T_02_03_09_FK_02` (`f_11`),
  ADD KEY `T_02_03_09_FK_03` (`f_12`),
  ADD KEY `T_02_03_09_FK_04` (`f_13`),
  ADD KEY `T_02_03_09_FK_01` (`f_10`);

--
-- Index pour la table `t_02_04_01`
--
ALTER TABLE `t_02_04_01`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_01_UK_01` (`f_01`);

--
-- Index pour la table `t_02_04_02`
--
ALTER TABLE `t_02_04_02`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_02_UK_01` (`f_02`,`f_03`,`f_04`),
  ADD KEY `T_02_04_02_FK_01` (`f_03`),
  ADD KEY `T_02_04_02_FK_02` (`f_04`);

--
-- Index pour la table `t_02_04_03`
--
ALTER TABLE `t_02_04_03`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_03_UK_01` (`f_02`,`f_03`,`f_04`),
  ADD KEY `T_02_04_03_FK_01` (`f_03`),
  ADD KEY `T_02_04_03_FK_02` (`f_04`);

--
-- Index pour la table `t_02_04_04`
--
ALTER TABLE `t_02_04_04`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_04_UK_01` (`f_03`,`f_04`),
  ADD KEY `T_02_04_04_FK_01` (`f_04`);

--
-- Index pour la table `t_02_04_05`
--
ALTER TABLE `t_02_04_05`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_05_UK_01` (`f_03`,`f_04`),
  ADD KEY `T_02_04_05_FK_01` (`f_04`);

--
-- Index pour la table `t_02_04_06`
--
ALTER TABLE `t_02_04_06`
  ADD PRIMARY KEY (`f_00`),
  ADD UNIQUE KEY `T_02_04_06_UK_01` (`f_03`,`f_04`),
  ADD KEY `T_02_04_06_FK_01` (`f_04`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `t_00_01_01`
--
ALTER TABLE `t_00_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_00_02_01`
--
ALTER TABLE `t_00_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `t_00_02_03`
--
ALTER TABLE `t_00_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT pour la table `t_00_02_04`
--
ALTER TABLE `t_00_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=177;

--
-- AUTO_INCREMENT pour la table `t_00_02_05`
--
ALTER TABLE `t_00_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT pour la table `t_00_03_01`
--
ALTER TABLE `t_00_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `t_01_01_01`
--
ALTER TABLE `t_01_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT pour la table `t_01_02_01`
--
ALTER TABLE `t_01_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `t_01_02_02`
--
ALTER TABLE `t_01_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=250;

--
-- AUTO_INCREMENT pour la table `t_01_02_03`
--
ALTER TABLE `t_01_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT pour la table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1542;

--
-- AUTO_INCREMENT pour la table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- AUTO_INCREMENT pour la table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `t_02_01_01`
--
ALTER TABLE `t_02_01_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `t_02_01_04`
--
ALTER TABLE `t_02_01_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT pour la table `t_02_01_08`
--
ALTER TABLE `t_02_01_08`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT pour la table `t_02_02_01`
--
ALTER TABLE `t_02_02_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `t_02_02_02`
--
ALTER TABLE `t_02_02_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `t_02_02_03`
--
ALTER TABLE `t_02_02_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT pour la table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT pour la table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT pour la table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=144;

--
-- AUTO_INCREMENT pour la table `t_02_03_06`
--
ALTER TABLE `t_02_03_06`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `t_02_03_09`
--
ALTER TABLE `t_02_03_09`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_02_04_01`
--
ALTER TABLE `t_02_04_01`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `t_02_04_02`
--
ALTER TABLE `t_02_04_02`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_02_04_03`
--
ALTER TABLE `t_02_04_03`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_02_04_04`
--
ALTER TABLE `t_02_04_04`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_02_04_05`
--
ALTER TABLE `t_02_04_05`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `t_02_04_06`
--
ALTER TABLE `t_02_04_06`
  MODIFY `f_00` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `r_t000201_t000203`
--
ALTER TABLE `r_t000201_t000203`
  ADD CONSTRAINT `R_T000201_T000203_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_01` (`f_00`),
  ADD CONSTRAINT `R_T000201_T000203_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_03` (`f_00`);

--
-- Contraintes pour la table `r_t000202_t000201`
--
ALTER TABLE `r_t000202_t000201`
  ADD CONSTRAINT `R_T000202_T000201_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_02` (`f_00`),
  ADD CONSTRAINT `R_T000202_T000201_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_01` (`f_00`);

--
-- Contraintes pour la table `r_t000202_t000203`
--
ALTER TABLE `r_t000202_t000203`
  ADD CONSTRAINT `R_T000202_T000203_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_02` (`f_00`),
  ADD CONSTRAINT `R_T000202_T000203_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_03` (`f_00`);

--
-- Contraintes pour la table `r_t000203_t000204`
--
ALTER TABLE `r_t000203_t000204`
  ADD CONSTRAINT `R_T000203_T000204_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_00_02_03` (`f_00`),
  ADD CONSTRAINT `R_T000203_T000204_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_00_02_04` (`f_00`);

--
-- Contraintes pour la table `r_t020303_t020307`
--
ALTER TABLE `r_t020303_t020307`
  ADD CONSTRAINT `R_T020303_T020307_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_03` (`f_00`),
  ADD CONSTRAINT `R_T020303_T020307_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `r_t020304_t020307`
--
ALTER TABLE `r_t020304_t020307`
  ADD CONSTRAINT `R_T020304_T020307_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_04` (`f_00`),
  ADD CONSTRAINT `R_T020304_T020307_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `r_t020305_t020201`
--
ALTER TABLE `r_t020305_t020201`
  ADD CONSTRAINT `R_T020305_T020201_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020201_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_01` (`f_00`);

--
-- Contraintes pour la table `r_t020305_t020204`
--
ALTER TABLE `r_t020305_t020204`
  ADD CONSTRAINT `R_T020305_T020204_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020204_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_02_04` (`f_00`);

--
-- Contraintes pour la table `r_t020305_t020307`
--
ALTER TABLE `r_t020305_t020307`
  ADD CONSTRAINT `R_T020305_T020307_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_05` (`f_00`),
  ADD CONSTRAINT `R_T020305_T020307_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `r_t020307_t010205`
--
ALTER TABLE `r_t020307_t010205`
  ADD CONSTRAINT `R_T020307_T010205_FK_01` FOREIGN KEY (`f_01`) REFERENCES `t_02_03_07` (`f_00`),
  ADD CONSTRAINT `R_T020307_T010205_FK_02` FOREIGN KEY (`f_02`) REFERENCES `t_01_02_05` (`f_00`);

--
-- Contraintes pour la table `t_00_02_02`
--
ALTER TABLE `t_00_02_02`
  ADD CONSTRAINT `T_00_02_02_FK_01` FOREIGN KEY (`f_08`) REFERENCES `t_01_03_04` (`f_00`);

--
-- Contraintes pour la table `t_00_04_01`
--
ALTER TABLE `t_00_04_01`
  ADD CONSTRAINT `T_00_04_01_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_00_02_02` (`f_00`);

--
-- Contraintes pour la table `t_01_02_04`
--
ALTER TABLE `t_01_02_04`
  ADD CONSTRAINT `T_01_02_04_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_02_03` (`f_00`);

--
-- Contraintes pour la table `t_01_02_05`
--
ALTER TABLE `t_01_02_05`
  ADD CONSTRAINT `T_01_02_05_FK_02` FOREIGN KEY (`f_06`) REFERENCES `t_01_02_04` (`f_00`);

--
-- Contraintes pour la table `t_01_03_01`
--
ALTER TABLE `t_01_03_01`
  ADD CONSTRAINT `T_01_03_01_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_01_01` (`f_00`),
  ADD CONSTRAINT `T_01_03_01_FK_02` FOREIGN KEY (`f_06`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Contraintes pour la table `t_01_03_02`
--
ALTER TABLE `t_01_03_02`
  ADD CONSTRAINT `T_01_03_02_FK_01` FOREIGN KEY (`f_05`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Contraintes pour la table `t_01_03_03`
--
ALTER TABLE `t_01_03_03`
  ADD CONSTRAINT `T_01_03_03_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_01_02_03` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_01_02_03` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_03` FOREIGN KEY (`f_12`) REFERENCES `t_01_02_02` (`f_00`),
  ADD CONSTRAINT `T_01_03_03_FK_04` FOREIGN KEY (`f_13`) REFERENCES `t_00_01_01` (`f_00`);

--
-- Contraintes pour la table `t_01_03_04`
--
ALTER TABLE `t_01_03_04`
  ADD CONSTRAINT `FKqwl61as0e81pno1umh9vggf43` FOREIGN KEY (`f_00`) REFERENCES `t_01_03_03` (`f_00`),
  ADD CONSTRAINT `T_01_03_04_FK_01` FOREIGN KEY (`f_15`) REFERENCES `t_01_03_02` (`f_00`);

--
-- Contraintes pour la table `t_02_01_02`
--
ALTER TABLE `t_02_01_02`
  ADD CONSTRAINT `FK2sy2ie5990swc3lb6m5moxy2x` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_01` (`f_00`);

--
-- Contraintes pour la table `t_02_01_03`
--
ALTER TABLE `t_02_01_03`
  ADD CONSTRAINT `FKrh84hkfhd8x8xe1kc6cpked1b` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_01` (`f_00`);

--
-- Contraintes pour la table `t_02_01_05`
--
ALTER TABLE `t_02_01_05`
  ADD CONSTRAINT `FKn9m6ss3iv18e0u6lrg1gxcah2` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Contraintes pour la table `t_02_01_06`
--
ALTER TABLE `t_02_01_06`
  ADD CONSTRAINT `FKobaswmff3gsi6wxe1eg79n7jw` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Contraintes pour la table `t_02_01_07`
--
ALTER TABLE `t_02_01_07`
  ADD CONSTRAINT `FK6oo45wyvijcd2hs8bg191q1va` FOREIGN KEY (`f_00`) REFERENCES `t_02_01_04` (`f_00`);

--
-- Contraintes pour la table `t_02_02_04`
--
ALTER TABLE `t_02_02_04`
  ADD CONSTRAINT `T_02_02_04_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_01_02` (`f_00`),
  ADD CONSTRAINT `T_02_02_04_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_01_02_02` (`f_00`);

--
-- Contraintes pour la table `t_02_02_05`
--
ALTER TABLE `t_02_02_05`
  ADD CONSTRAINT `T_02_02_05_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_01_03` (`f_00`),
  ADD CONSTRAINT `T_02_02_05_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_01_02_02` (`f_00`);

--
-- Contraintes pour la table `t_02_03_01`
--
ALTER TABLE `t_02_03_01`
  ADD CONSTRAINT `T_02_03_01_FK_01` FOREIGN KEY (`f_06`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_01_FK_02` FOREIGN KEY (`f_07`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Contraintes pour la table `t_02_03_02`
--
ALTER TABLE `t_02_03_02`
  ADD CONSTRAINT `FK528v9tp63m3qkysss3ly812km` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_02_FK_01` FOREIGN KEY (`f_08`) REFERENCES `t_02_02_05` (`f_00`),
  ADD CONSTRAINT `T_02_03_02_FK_02` FOREIGN KEY (`f_09`) REFERENCES `t_01_02_05` (`f_00`);

--
-- Contraintes pour la table `t_02_03_03`
--
ALTER TABLE `t_02_03_03`
  ADD CONSTRAINT `FK3prwkpmcb0tf6omeskbrpyog5` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_03_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_01_05` (`f_00`),
  ADD CONSTRAINT `T_02_03_03_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_02_03_06` (`f_00`);

--
-- Contraintes pour la table `t_02_03_04`
--
ALTER TABLE `t_02_03_04`
  ADD CONSTRAINT `FK3u3eey5edtk15v6fs0fqv4hwq` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_04_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_01_06` (`f_00`);

--
-- Contraintes pour la table `t_02_03_05`
--
ALTER TABLE `t_02_03_05`
  ADD CONSTRAINT `FKmtws377tu5byvkxv5dkrthye6` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_05_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_01_07` (`f_00`);

--
-- Contraintes pour la table `t_02_03_06`
--
ALTER TABLE `t_02_03_06`
  ADD CONSTRAINT `T_02_03_06_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_02_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_06_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_06_FK_03` FOREIGN KEY (`f_05`) REFERENCES `t_01_03_01` (`f_00`);

--
-- Contraintes pour la table `t_02_03_07`
--
ALTER TABLE `t_02_03_07`
  ADD CONSTRAINT `FK31skr07k7679di6xev3vodvra` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_01` FOREIGN KEY (`f_18`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_02` FOREIGN KEY (`f_19`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_03` FOREIGN KEY (`f_20`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_04` FOREIGN KEY (`f_21`) REFERENCES `t_02_02_05` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_05` FOREIGN KEY (`f_22`) REFERENCES `t_02_03_06` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_06` FOREIGN KEY (`f_23`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_07_FK_07` FOREIGN KEY (`f_24`) REFERENCES `t_02_03_02` (`f_00`);

--
-- Contraintes pour la table `t_02_03_08`
--
ALTER TABLE `t_02_03_08`
  ADD CONSTRAINT `FKq3014e4w4xvkk4lvb0139mv59` FOREIGN KEY (`f_00`) REFERENCES `t_02_03_01` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_01` FOREIGN KEY (`f_14`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_02` FOREIGN KEY (`f_15`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_03` FOREIGN KEY (`f_16`) REFERENCES `t_02_02_03` (`f_00`),
  ADD CONSTRAINT `T_02_03_08_FK_04` FOREIGN KEY (`f_17`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `t_02_03_09`
--
ALTER TABLE `t_02_03_09`
  ADD CONSTRAINT `T_02_03_09_FK_01` FOREIGN KEY (`f_10`) REFERENCES `t_02_02_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_02` FOREIGN KEY (`f_11`) REFERENCES `t_02_01_08` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_03` FOREIGN KEY (`f_12`) REFERENCES `t_02_03_02` (`f_00`),
  ADD CONSTRAINT `T_02_03_09_FK_04` FOREIGN KEY (`f_13`) REFERENCES `t_02_02_05` (`f_00`);

--
-- Contraintes pour la table `t_02_04_02`
--
ALTER TABLE `t_02_04_02`
  ADD CONSTRAINT `T_02_04_02_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_04_01` (`f_00`),
  ADD CONSTRAINT `T_02_04_02_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `t_02_04_03`
--
ALTER TABLE `t_02_04_03`
  ADD CONSTRAINT `T_02_04_03_FK_01` FOREIGN KEY (`f_03`) REFERENCES `t_02_04_01` (`f_00`),
  ADD CONSTRAINT `T_02_04_03_FK_02` FOREIGN KEY (`f_04`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `t_02_04_04`
--
ALTER TABLE `t_02_04_04`
  ADD CONSTRAINT `T_02_04_04_FK_01` FOREIGN KEY (`f_04`) REFERENCES `t_02_03_07` (`f_00`);

--
-- Contraintes pour la table `t_02_04_05`
--
ALTER TABLE `t_02_04_05`
  ADD CONSTRAINT `T_02_04_05_FK_01` FOREIGN KEY (`f_04`) REFERENCES `t_02_03_05` (`f_00`);

--
-- Contraintes pour la table `t_02_04_06`
--
ALTER TABLE `t_02_04_06`
  ADD CONSTRAINT `T_02_04_06_FK_01` FOREIGN KEY (`f_04`) REFERENCES `t_02_03_04` (`f_00`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
