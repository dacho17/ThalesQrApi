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
--   CREATING TABLES & RELATIONSHIPS
-- --------------------------------------
CREATE TABLE IF NOT EXISTS `qr_codes` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT null,
  `content_type` tinyint NOT NULL,
  `byte_content` blob NOT NULL,
  `text_content` longtext DEFAULT null,
  `vcard_id` int DEFAULT NULL,
  `is_deleted` tinyint DEFAULT 0,
  `created_by` varchar(255) NOT NULL,
  `created_date` datetime NOT NULL,
  `deleted_date` datetime DEFAULT null
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
  -- CONSTRAINT UC_ONE_NON_DELETED UNIQUE (`content`, `is_deleted`)
  
CREATE TABLE IF NOT EXISTS `vcards` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  	`xml_content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- adding relationship between vcards and qr_codes
ALTER TABLE `qr_codes` ADD FOREIGN KEY (`vcard_id`) REFERENCES `vcards` (`id`);

CREATE TABLE IF NOT EXISTS `users` (
  `username` varchar(255) PRIMARY KEY NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` tinyint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `authorities` (
  `username` varchar(255) NOT NULL,
  `authority` varchar(255) NOT NULL,

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

INSERT INTO `vcards`
VALUES
(1,'<?xml version="1.0" encoding="UTF-8" standalone="no"?><vcards xmlns="urn:ietf:params:xml:ns:vcard-4.0"><vcard><prodid><text>ez-vcard 0.11.3</text></prodid><n><surname>Second</surname><given>User2</given><additional/><prefix/><suffix/></n><fn><text>User2 Second</text></fn><org><text>Thales</text></org><title><text>Engineer</text></title><adr><pobox/><ext/><street>12 Ayer Rajah Crescent</street><locality>Singapore</locality><region/><code>139941</code><country>Singapore</country></adr><tel><parameters><type><text>CELL</text></type></parameters><text>+6510001000</text></tel><email><parameters><type><text>WORK</text><text>INTERNET</text></type></parameters><text>user@second.sg</text></email></vcard></vcards>');

INSERT INTO `qr_codes`
VALUES
(1, 'first_file', 0, 'x11111', 'This is the first file created.', null, 0, 'SQL script on init', now(), null),
(2, 'second_file', 1, 'https://en.wikipedia.org', 'This is the second file created.', null, 0, 'SQL script on init', now(), null),
(3, 'Third file', 2, '12345678', 'This is the third file created', 1, 0, 'Manually inserted', now(), null);

-- --------------------------------------
select 'initDb script finished executing' AS '';
