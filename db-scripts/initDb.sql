select 'initDb script started executing' AS '';

-- --------------------------------------
--        CREATE A DB IF NONE
-- --------------------------------------
CREATE DATABASE IF NOT EXISTS `rest_api_db`;
USE `rest_api_db`;

-- --------------------------------------
--        CREATE AN API DB USER
-- --------------------------------------
CREATE USER IF NOT EXISTS 'qrapi'@'localhost' IDENTIFIED BY 'adam';
GRANT ALL PRIVILEGES ON rest_api_db.* TO 'qrapi'@'localhost';

-- --------------------------------------
--        CREATING TABLES
-- --------------------------------------
CREATE TABLE IF NOT EXISTS`qr_codes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(128) DEFAULT NULL,
  `content_type` varchar(64) DEFAULT NULL,
  `byte_content` blob NOT NULL,
  `decoded_content` text NOT NULL,
  `is_deleted` tinyint DEFAULT 0,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime NOT NULL,
  `deleted_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
  -- CONSTRAINT UC_ONE_NON_DELETED UNIQUE (`content`, `is_deleted`)

CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(68) NOT NULL,
  `enabled` tinyint NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- DROP TABLE IF EXISTS `authorities`;

CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,

  UNIQUE KEY `authorities_idx_1` (`username`, `authority`),
  CONSTRAINT `authorities_ibfk_1`
  FOREIGN KEY (`username`)
  REFERENCES `users` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------
--        POPULATING TABLES
-- --------------------------------------
INSERT IGNORE INTO `users`
VALUES
('Thales', '{noop}TheFirstPhilosopher', 1),
('Socrates', '{noop}PlatosFriend', 1),
('Aristotle', '{noop}ThePhilosopher', 1),
('Steiner', '{bcrypt}$2a$12$ojewJqLBGvdafvNO5Pxjp.21ua2SvN/zGbIvZTO2RWYtSaNxdihQ6', 1);

INSERT IGNORE INTO `authorities`
VALUES
('Thales', 'ROLE_STUDENT'),
('Thales', 'ROLE_TEACHER'),
('Socrates', 'ROLE_STUDENT'),
('Socrates', 'ROLE_TEACHER'),
('Aristotle', 'ROLE_STUDENT'),
('Aristotle', 'ROLE_TEACHER'),
('Steiner', 'ROLE_STUDENT'),
('Steiner', 'ROLE_TEACHER'),
('Thales', 'ROLE_ADMIN'),
('Aristotle', 'ROLE_ADMIN'),
('Steiner', 'ROLE_ADMIN');
-- --------------------------------------
select 'initDb script finished executing' AS '';
