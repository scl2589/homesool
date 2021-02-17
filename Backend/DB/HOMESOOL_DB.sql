-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
-- ��������� ���̺������ start_time / end_time �������� �߰���
-- ���� : https://zepherstory.tistory.com/339
SET sql_mode = '';
-- -----------------------------------------------------
-- Schema HOMESOOL
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema HOMESOOL
-- -----------------------------------------------------


CREATE SCHEMA IF NOT EXISTS `HOMESOOL` DEFAULT CHARACTER SET utf8 ;
USE `HOMESOOL` ;

-- -----------------------------------------------------
-- Table `HOMESOOL`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`user` (
  `id` BIGINT NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`room` (
  `room_id` BIGINT NOT NULL AUTO_INCREMENT,
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `end_time` TIMESTAMP NULL DEFAULT NULL,
  `host_id` BIGINT NOT NULL,
  `code` VARCHAR(10) NOT NULL,
  `room_name` VARCHAR(100) NULL DEFAULT NULL,
  `is_public` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`room_id`),
  INDEX `fk_room_user1_idx` (`host_id` ASC) VISIBLE,
  CONSTRAINT `fk_room_user1`
    FOREIGN KEY (`host_id`)
    REFERENCES `HOMESOOL`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`member` (
  `user_id` BIGINT NOT NULL,
  `room_id` BIGINT NOT NULL,
  `nickname` VARCHAR(50) NULL DEFAULT NULL,
  `ishost` INT NOT NULL,
  PRIMARY KEY (`user_id`, `room_id`),
  INDEX `fk_member_room1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_member_room1`
    FOREIGN KEY (`room_id`)
    REFERENCES `HOMESOOL`.`room` (`room_id`),
  CONSTRAINT `fk_member_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `HOMESOOL`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`photo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`photo` (
  `photo_id` BIGINT NOT NULL AUTO_INCREMENT,
  `room_id` BIGINT NOT NULL,
  `src` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`photo_id`),
  INDEX `fk_photo_room1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_photo_room1`
    FOREIGN KEY (`room_id`)
    REFERENCES `HOMESOOL`.`room` (`room_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`user_drink`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`user_drink` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `liquor_name` VARCHAR(45) NULL DEFAULT NULL,
  `liquor_limit` INT NULL DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_drink_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_drink_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `HOMESOOL`.`user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`user_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`user_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `room_id` BIGINT NOT NULL,
  `liquor_name` VARCHAR(45) NULL DEFAULT NULL,
  `liquor_limit` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user..._room1_idx` (`room_id` ASC) VISIBLE,
  INDEX `fk_user..._user1` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user..._room1`
    FOREIGN KEY (`room_id`)
    REFERENCES `HOMESOOL`.`room` (`room_id`),
  CONSTRAINT `fk_user..._user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `HOMESOOL`.`user` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `HOMESOOL`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HOMESOOL`.`tag` (
  `tag_id` BIGINT NOT NULL AUTO_INCREMENT,
  `room_id` BIGINT NOT NULL,
  `tag_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`tag_id`),
  INDEX `fk_tag_room1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_tag_room1`
    FOREIGN KEY (`room_id`)
    REFERENCES `HOMESOOL`.`room` (`room_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
