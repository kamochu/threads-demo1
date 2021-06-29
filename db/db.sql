

CREATE USER 'demo'@'%' IDENTIFIED BY 'Demo12$1';
CREATE DATABASE demo1; 
GRANT ALL PRIVILEGES ON demo1.* TO 'demo'@'%';
FLUSH PRIVILEGES;

CREATE TABLE `demo1`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `product` VARCHAR(50) NULL,
  `quantity` INT NULL,
  `name` VARCHAR(255) NULL,
  `phonenumber` VARCHAR(20) NULL,
  `price` INT NULL,
  `total` INT NULL,
  `status` VARCHAR(45) NULL DEFAULT 'NEW',
  `created_on` DATETIME NULL,
  `last_updated_on` DATETIME NULL,
  PRIMARY KEY (`id`));