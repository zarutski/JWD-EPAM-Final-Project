DROP TABLE IF EXISTS `test_payment_app`.`account_states`, `test_payment_app`.`accounts`, `test_payment_app`.`accounts_has_credit_cards`, `test_payment_app`.`credit_card_states`, `test_payment_app`.`credit_cards`, `test_payment_app`.`currences`, `test_payment_app`.`operation_details`, `test_payment_app`.`operation_types`, `test_payment_app`.`operations`, `test_payment_app`.`user_details`, `test_payment_app`.`user_roles`, `test_payment_app`.`users`;

-- MySQL Workbench Forward Engineering
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `test_payment_app` DEFAULT CHARACTER SET utf8 ;
USE `test_payment_app` ;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`user_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `surname` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `patronymic` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(20) NOT NULL,
  `passport_series` VARCHAR(10) NOT NULL,
  `passport_number` VARCHAR(10) NOT NULL,
  `date_birth` DATE NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `post_code` VARCHAR(20) NOT NULL,
  `user_photo_link` VARCHAR(80) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`user_roles` (
  `role_code` INT NOT NULL,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_code`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `role_code` INT NOT NULL,
  `user_details_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_user_status1_idx` (`role_code` ASC),
  INDEX `fk_users_user_details1_idx` (`user_details_id` ASC),
  CONSTRAINT `fk_user_user_status1`
    FOREIGN KEY (`role_code`)
    REFERENCES `test_payment_app`.`user_roles` (`role_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_user_details1`
    FOREIGN KEY (`user_details_id`)
    REFERENCES `test_payment_app`.`user_details` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`credit_card_states` (
  `state_code` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`state_code`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`credit_cards` (
  `credit_card_id` INT NOT NULL AUTO_INCREMENT,
  `credit_card_number` VARCHAR(19) NOT NULL,
  `expiration_date` DATE NOT NULL,
  `owner` VARCHAR(100) NOT NULL,
  `cvv_code` VARCHAR(10) NOT NULL,
  `state_code` INT NOT NULL,
  PRIMARY KEY (`credit_card_id`),
  INDEX `fk_credit_cards_credit_card_states1_idx` (`state_code` ASC),
  CONSTRAINT `fk_credit_cards_credit_card_states1`
    FOREIGN KEY (`state_code`)
    REFERENCES `test_payment_app`.`credit_card_states` (`state_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`account_states` (
  `state_code` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`state_code`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`currences` (
  `currences_id` INT NOT NULL AUTO_INCREMENT,
  `currency` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`currences_id`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`accounts` (
  `account_id` INT NOT NULL AUTO_INCREMENT,
  `number` VARCHAR(40) NOT NULL,
  `amount` BIGINT(10) NOT NULL,
  `opening_date` DATE NOT NULL,
  `currences_id` INT NOT NULL,
  `state_code` INT NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`account_id`),
  INDEX `fk_account_account_status1_idx` (`state_code` ASC),
  INDEX `fk_accounts_users1_idx` (`users_id` ASC),
  INDEX `fk_accounts_currences1_idx` (`currences_id` ASC),
  CONSTRAINT `fk_account_account_status1`
    FOREIGN KEY (`state_code`)
    REFERENCES `test_payment_app`.`account_states` (`state_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_accounts_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `test_payment_app`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_accounts_currences1`
    FOREIGN KEY (`currences_id`)
    REFERENCES `test_payment_app`.`currences` (`currences_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`operation_types` (
  `operation_code` INT NOT NULL AUTO_INCREMENT,
  `operation_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`operation_code`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`operations` (
  `operation_id` INT NOT NULL AUTO_INCREMENT,
  `operation_date` TIMESTAMP(6) NOT NULL,
  `allowed` TINYINT(1) NOT NULL,
  `operation_type` INT NOT NULL,
  `account_id` INT NOT NULL,
  `credit_card_id` INT NOT NULL,
  PRIMARY KEY (`operation_id`),
  INDEX `fk_operation_account1_idx` (`account_id` ASC),
  INDEX `fk_operation_operation_code1_idx` (`operation_type` ASC),
  INDEX `fk_operations_credit_cards1_idx` (`credit_card_id` ASC),
  CONSTRAINT `fk_operation_account1`
    FOREIGN KEY (`account_id`)
    REFERENCES `test_payment_app`.`accounts` (`account_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_operation_operation_code1`
    FOREIGN KEY (`operation_type`)
    REFERENCES `test_payment_app`.`operation_types` (`operation_code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_operations_credit_cards1`
    FOREIGN KEY (`credit_card_id`)
    REFERENCES `test_payment_app`.`credit_cards` (`credit_card_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`operation_details` (
  `details_id` VARCHAR(45) NOT NULL,
  `amount` VARCHAR(45) NOT NULL,
  `destination_account_number` VARCHAR(45) NULL,
  `operations_operation_id` INT NOT NULL,
  PRIMARY KEY (`details_id`),
  INDEX `fk_operation_details_operations1_idx` (`operations_operation_id` ASC),
  CONSTRAINT `fk_operation_details_operations1`
    FOREIGN KEY (`operations_operation_id`)
    REFERENCES `test_payment_app`.`operations` (`operation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test_payment_app`.`accounts_has_credit_cards` (
  `account_id` INT NOT NULL,
  `credit_card_id` INT NOT NULL,
  PRIMARY KEY (`account_id`, `credit_card_id`),
  INDEX `fk_accounts_has_credit_cards_credit_cards1_idx` (`credit_card_id` ASC),
  INDEX `fk_accounts_has_credit_cards_accounts1_idx` (`account_id` ASC),
  CONSTRAINT `fk_accounts_has_credit_cards_accounts1`
    FOREIGN KEY (`account_id`)
    REFERENCES `test_payment_app`.`accounts` (`account_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_accounts_has_credit_cards_credit_cards1`
    FOREIGN KEY (`credit_card_id`)
    REFERENCES `test_payment_app`.`credit_cards` (`credit_card_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT INTO `test_payment_app`.`account_states` (`state_code`, `description`) VALUES ('1', 'активен');
INSERT INTO `test_payment_app`.`account_states` (`state_code`, `description`) VALUES ('2', 'заблокирован');
INSERT INTO `test_payment_app`.`user_roles` (`role_code`, `role_name`) VALUES ('1', 'user');
INSERT INTO `test_payment_app`.`user_roles` (`role_code`, `role_name`) VALUES ('2', 'admin');

INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('1', 'Кравченко', 'Андрей', 'Витальевич', '+375254122678', 'АВ', '2325115', '1992-03-21', 'Минская обл., г Борисов, ул. Чапаева, 1, кв. 32', '222120');
INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('2', 'Гуща', 'Сергей', 'Андреевич', '+375337612643', 'АВ', '2489831', '1987-11-01', 'Гродненская обл., г. Лида, ул. Калинина, 45, кв. 15', '231300');
INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('3', 'Дуда', 'Наталья', 'Викторовна', '+375254631578', 'АВ', '1251235', '1979-06-25', 'Гомельская область, г. Хойники, ул.Лермонотова, д.9, кв.41', '247618');
INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('4', 'Левкович', 'Михаил', 'Георгиевич', '+375299852166', 'АВ', '4529332', '1996-10-16', 'Минская область, г. Минск, ул. Сурганова, 24, кв. 15', '220012');
INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('5', 'Лиходиевская', 'Татьяна', 'Анатольевна', '+375331213648', 'АВ', '3892112', '1991-07-20', 'Минская обл., г. Борисов, ул. 50 лет БССР, 1, кв. 218', '222526');
INSERT INTO `test_payment_app`.`user_details` (`id`, `surname`, `name`, `patronymic`, `phone_number`, `passport_series`, `passport_number`, `date_birth`, `address`, `post_code`) VALUES ('6', 'Стойчевски', 'Александр', 'Эдвардович', '+375291245519', 'АВ', '2345167', '1983-09-08', 'Минская область, г. Молодечно,  2-ой пер. М.Горького, 2а, кв. 11', '222310');

INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('1', 'testus', '$2a$10$eh8x4FITIt46KVBWQVjITuklHBry5/v8v1fNVjEw8z0e8ZwV5MWtq', 'test@gmail.com', '1', '1');
INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('2', 'dbtest', '$2a$10$ZrKeczFUf2Y4LIFP/p0pHOCbCE44s9linZ6PkKW2Ej8Q9T94KTRUS', 'dbtest@gmail.com', '1', '2');
INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('3', 'testadmin', '$2a$10$iCql7q0h6dOXGVmkDlQrWu.h7tlFHydYsOtbjUS0kzfYXNbmi5fzu', 'testadmin@gmail.com', '2', '3');
INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('4', 'testuser', '$2a$10$kQlvkjKxfufhK9QNJB5WJezFupNPQTaiWsgUjyClbfj1vAHLB6L9O', 'testuser@gmail.com', '1', '4');
INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('5', 'usertest', '$2a$10$HzOG2KbfSQjPhw4RD4HPduu1ERYurnS2wmZlUzYSNziKvdxjdxuOm', 'usertest@mail.ru', '1', '5');
INSERT INTO `test_payment_app`.`users` (`id`, `login`, `password`, `email`, `role_code`, `user_details_id`) VALUES ('6', 'admintest', '$2a$10$pmG4dGxHYocCkygwZH6pe.OROQdEOV5bQCf.qmUQ4DnIv5.BhP9tq', 'admintest@mail.ru', '2', '6');

INSERT INTO `test_payment_app`.`currences` (`currences_id`, `currency`) VALUES ('1', 'BYN');
INSERT INTO `test_payment_app`.`currences` (`currences_id`, `currency`) VALUES ('2', 'USD');
INSERT INTO `test_payment_app`.`currences` (`currences_id`, `currency`) VALUES ('3', 'EUR');

INSERT INTO `test_payment_app`.`accounts` (`account_id`, `number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) VALUES ('1', 'BY10UNBS3135301412345678', '40000', ' 2019-10-23', '1', '1', '1');
INSERT INTO `test_payment_app`.`accounts` (`account_id`, `number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) VALUES ('2', 'BY13UNBS3012301487654321', '20000', ' 2019-10-30', '2', '1', '1');
INSERT INTO `test_payment_app`.`accounts` (`account_id`, `number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) VALUES ('3', 'BY11UNBS3048301456781234', '4000', ' 2019-11-02', '1', '1', '2');
INSERT INTO `test_payment_app`.`accounts` (`account_id`, `number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) VALUES ('4', 'BY08UNBS3146301443218765', '230000', ' 2019-11-05', '1', '1', '4');
INSERT INTO `test_payment_app`.`accounts` (`account_id`, `number`, `amount`, `opening_date`, `currences_id`, `state_code`, `users_id`) VALUES ('5', 'BY12UNBS3078301434561278', '50000', '2019-11-25', '1', '1', '5');

INSERT INTO `test_payment_app`.`credit_card_states` (`state_code`, `description`) VALUES ('1', 'активна');
INSERT INTO `test_payment_app`.`credit_card_states` (`state_code`, `description`) VALUES ('2', 'заблокирована');

INSERT INTO `test_payment_app`.`operation_types` (`operation_code`, `operation_name`) VALUES ('1', 'пополнение');
INSERT INTO `test_payment_app`.`operation_types` (`operation_code`, `operation_name`) VALUES ('2', 'перевод');

INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('1', '4539804056541904', '2022-10-01', 'Andrei Kravchenko', '332', '1');
INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('2', '5190967013969042', '2021-10-01', 'Siarhei Gushcha', '321', '1');
INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('3', '4024007195998173', '2023-10-01', 'Siarhei Gushcha', '145', '1');
INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('4', '4556936653057328', '2023-10-01', 'Mikhail Levkovich', '122', '1');
INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('5', '5185553343850289', '2021-10-01', 'Aleksandr Stoichevski', '322', '1');
INSERT INTO `test_payment_app`.`credit_cards` (`credit_card_id`, `credit_card_number`, `expiration_date`, `owner`, `cvv_code`, `state_code`) VALUES ('6', '5328530337680409', '2022-10-01', 'Aleksandr Stoichevski', '456', '1');

INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('1', '1');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('2', '1');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('3', '2');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('3', '3');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('4', '4');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('5', '5');
INSERT INTO `test_payment_app`.`accounts_has_credit_cards` (`account_id`, `credit_card_id`) VALUES ('5', '6');

INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('1', '2019-11-15 23:40:10 ', '1', '1', '1', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('2', '2019-11-16 13:26:05 ', '1', '1', '2', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('3', '2019-11-16 15:28:11 ', '0', '2', '1', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('4', '2019-11-16 16:56:08 ', '1', '1', '1', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('5', '2019-11-17 08:23:04 ', '1', '2', '1', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('6', '2019-11-17 10:16:18 ', '1', '2', '1', '1');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('7', '2019-11-17 12:36:55 ', '1', '2', '3', '2');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('8', '2019-11-19 19:11:41 ', '1', '1', '4', '4');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('9', '2019-11-19 20:26:03 ', '0', '2', '3', '3');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('10', '2019-11-20 15:53:45 ', '1', '1', '5', '5');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('11', '2019-11-20 16:17:38 ', '1', '2', '5', '6');
INSERT INTO `test_payment_app`.`operations` (`operation_id`, `operation_date`, `allowed`, `operation_type`, `account_id`, `credit_card_id`) VALUES ('12', '2019-11-21 09:21:09 ', '1', '1', '5', '5');

INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('1', '20000', '1');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('2', '10000', '2');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('3', '45000', 'BY12UNBS3025301412345678', '3');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('4', '50000', '4');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('5', '45000', 'BY12UNBS3025301412345678', '5');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('6', '20000', 'BY12UNBS3025301412345678', '6');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('7', '8000', 'BY13UNBS3016301487654321', '7');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('8', '10000', '8');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('9', '5000', 'BY09UNBS3018301456781234', '9');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('10', '5000', '10');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `destination_account_number`, `operations_operation_id`) VALUES ('11', '12000', 'BY11UNBS3113301443218765', '11');
INSERT INTO `test_payment_app`.`operation_details` (`details_id`, `amount`, `operations_operation_id`) VALUES ('12', '5000', '12');

ALTER TABLE `test_payment_app`.`users` 
ADD UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE;

UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '1');
UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '2');
UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '3');
UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '4');
UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '5');
UPDATE `test_payment_app`.`user_details` SET `passport_series` = 'AB' WHERE (`id` = '6');

UPDATE `test_payment_app`.`credit_cards` SET `owner` = 'Likhodievskaya Tatyana' WHERE (`credit_card_id` = '5');
UPDATE `test_payment_app`.`credit_cards` SET `owner` = 'Likhodievskaya Tatyana' WHERE (`credit_card_id` = '6');

ALTER TABLE `test_payment_app`.`accounts` 
ADD UNIQUE INDEX `number_UNIQUE` (`number` ASC) VISIBLE;

ALTER TABLE `test_payment_app`.`credit_cards` 
ADD UNIQUE INDEX `credit_card_number_UNIQUE` (`credit_card_number` ASC) VISIBLE;