-- -----------------------------------------------------
-- Create schema. Create user 'logiweb' with pass '12345'. 
-- Populate with some mock data. 
-- -----------------------------------------------------

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';


-- -----------------------------------------------------
-- Table `cities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cities` ;

CREATE TABLE IF NOT EXISTS `cities` (
  `city_id` INT NOT NULL AUTO_INCREMENT,
  `city_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`city_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `delivery_orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `delivery_orders` ;

CREATE TABLE IF NOT EXISTS `delivery_orders` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `order_status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`order_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `trucks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trucks` ;

CREATE TABLE IF NOT EXISTS `trucks` (
  `truck_id` INT NOT NULL AUTO_INCREMENT,
  `truck_license_plate_UQ` VARCHAR(7) NOT NULL,
  `truck_crew_size` INT NOT NULL,
  `truck_cargo_capacity` FLOAT NOT NULL,
  `truck_status` VARCHAR(45) NOT NULL,
  `truck_current_location_city_FK` INT NOT NULL,
  `truck_delivery_order_FK_UQ` INT NULL,
  `truck_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`truck_id`),
  INDEX `fk_delivery_trucks_2_idx` (`truck_current_location_city_FK` ASC),
  UNIQUE INDEX `truck_license_plate_UNIQUE` (`truck_license_plate_UQ` ASC),
  INDEX `fk_trucks_1_idx` (`truck_delivery_order_FK_UQ` ASC),
  UNIQUE INDEX `truck_delivery_order_FK_UNIQUE` (`truck_delivery_order_FK_UQ` ASC),
  CONSTRAINT `fk_delivery_trucks_2`
    FOREIGN KEY (`truck_current_location_city_FK`)
    REFERENCES `cities` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_trucks_1`
    FOREIGN KEY (`truck_delivery_order_FK_UQ`)
    REFERENCES `delivery_orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `drivers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drivers` ;

CREATE TABLE IF NOT EXISTS `drivers` (
  `driver_id` INT NOT NULL AUTO_INCREMENT,
  `driver_employee_id_UQ` INT NOT NULL,
  `driver_name` VARCHAR(45) NOT NULL,
  `driver_surname` VARCHAR(45) NOT NULL,
  `driver_status` VARCHAR(45) NOT NULL,
  `driver_current_location_city_FK` INT NOT NULL,
  `driver_current_truck_FK` INT NULL,
  `driver_deleted` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`driver_id`),
  INDEX `fk_drivers_2_idx` (`driver_current_location_city_FK` ASC),
  INDEX `fk_drivers_3_idx` (`driver_current_truck_FK` ASC),
  UNIQUE INDEX `driver_employee_id_UNIQUE` (`driver_employee_id_UQ` ASC),
  CONSTRAINT `fk_drivers_2`
    FOREIGN KEY (`driver_current_location_city_FK`)
    REFERENCES `cities` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_drivers_3`
    FOREIGN KEY (`driver_current_truck_FK`)
    REFERENCES `trucks` (`truck_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cargoes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `cargoes` ;

CREATE TABLE IF NOT EXISTS `cargoes` (
  `cargo_id` INT NOT NULL AUTO_INCREMENT,
  `cargo_weight` FLOAT NOT NULL,
  `cargo_title` VARCHAR(45) NOT NULL,
  `cargo_status` VARCHAR(45) NOT NULL,
  `cargo_origin_city_FK` INT NOT NULL,
  `cargo_destination_city_FK` INT NOT NULL,
  `cargo_from_order_FK` INT NOT NULL,
  PRIMARY KEY (`cargo_id`),
  INDEX `fk_cargoes_1_idx` (`cargo_from_order_FK` ASC),
  INDEX `fk_cargoes_4_idx` (`cargo_origin_city_FK` ASC),
  INDEX `fk_cargoes_3_idx` (`cargo_destination_city_FK` ASC),
  CONSTRAINT `fk_cargoes_1`
    FOREIGN KEY (`cargo_from_order_FK`)
    REFERENCES `delivery_orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cargoes_4`
    FOREIGN KEY (`cargo_origin_city_FK`)
    REFERENCES `cities` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cargoes_3`
    FOREIGN KEY (`cargo_destination_city_FK`)
    REFERENCES `cities` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `drivers_shift_journal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drivers_shift_journal` ;

CREATE TABLE IF NOT EXISTS `drivers_shift_journal` (
  `driver_shift_record_id` INT NOT NULL AUTO_INCREMENT,
  `shift_record_for_driver_FK` INT NOT NULL,
  `driver_shift_beggined` DATETIME NOT NULL,
  `driver_shift_ended` DATETIME NULL,
  PRIMARY KEY (`driver_shift_record_id`),
  INDEX `fk_drivers_shifts_1_idx` (`shift_record_for_driver_FK` ASC),
  CONSTRAINT `fk_drivers_shifts_1`
    FOREIGN KEY (`shift_record_for_driver_FK`)
    REFERENCES `drivers` (`driver_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users` ;

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `user_mail` VARCHAR(60) NOT NULL,
  `user_role` VARCHAR(45) NOT NULL,
  `md5_pass` CHAR(32) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_mail_UNIQUE` (`user_mail` ASC))
ENGINE = InnoDB;

SET SQL_MODE = '';
GRANT USAGE ON *.* TO javaschool;
 DROP USER javaschool;
SET SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';
CREATE USER 'javaschool' IDENTIFIED BY '12345';

GRANT ALL ON * TO 'javaschool';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



-- -----------------------------------------------------
-- Mock data
-- -----------------------------------------------------



--
-- Dumping data for table `users`
-- (passwords for both users are 12345
INSERT INTO `users` (`user_id`, `user_mail`, `user_role`, `md5_pass`) VALUES
(1, 'manager@logiweb.com', 'MANAGER', '827ccb0eea8a706c4c34a16891f84e7b'),
(2, 'driver@logiweb.com', 'DRIVER', '827ccb0eea8a706c4c34a16891f84e7b');


--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`city_id`, `city_name`) VALUES
(1, 'Arroyo'),
(2, 'Broken Hills'),
(3, 'San Francisco'),
(4, 'Den');

--
-- Dumping data for table `delivery_orders`
--

INSERT INTO `delivery_orders` (`order_id`, `order_status`) VALUES
(1, 'NOT_READY');

--
-- Dumping data for table `drivers`
--

INSERT INTO `drivers` (`driver_id`, `driver_employee_id_UQ`, `driver_name`, `driver_surname`, `driver_status`, `driver_current_location_city_FK`, `driver_current_truck_FK`, `driver_deleted`) VALUES
(1, 1, 'Max', 'Rockatansky', 'FREE', 1, NULL, 0),
(2, 2, 'Imperator', 'Furiosa', 'FREE', 1, NULL, 0),
(3, 3, 'Frank', 'Martin', 'FREE', 1, NULL, 0),
(4, 4, 'Jack', 'Burton', 'FREE', 1, 1, 0);

--
-- Dumping data for table `trucks`
--

INSERT INTO `trucks` (`truck_id`, `truck_license_plate_UQ`, `truck_crew_size`, `truck_cargo_capacity`, `truck_status`, `truck_current_location_city_FK`, `truck_delivery_order_FK_UQ`, `truck_deleted`) VALUES
(1, '12345', 2, 5, 'OK', 1, NULL, 0),
(2, '2', 2, 5, 'OK', 1, NULL, 0),
(3, '3', 4, 3, 'OK', 1, 1, 0),
(4, '4', 2, 1, 'FAULTY', 1, NULL, 0),
(5, '5', 2, 1, 'OK', 1, NULL, 0);

