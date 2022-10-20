select 'initDb script started executing' AS '';

-- --------------------------------------
--        CREATE A DB IF NONE
-- --------------------------------------
CREATE DATABASE IF NOT EXISTS `rest_api_db`;
USE `rest_api_db`;

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
  
CREATE TABLE IF NOT EXISTS `vcards` (
	`id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  	`xml_content` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- adding relationship between vcards and qr_codes
ALTER TABLE `qr_codes` ADD FOREIGN KEY (`vcard_id`) REFERENCES `vcards` (`id`);

CREATE TABLE IF NOT EXISTS `users` (
  `id` int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_type` tinyint NOT NULL,
  UNIQUE (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- adding relationship between qr_codes and users
ALTER TABLE `qr_codes` ADD FOREIGN KEY (`created_by`) REFERENCES `users` (`username`);
-- --------------------------------------
--        POPULATING TABLES
-- --------------------------------------

-- --------------------------------------
select 'initDb script finished executing' AS '';
