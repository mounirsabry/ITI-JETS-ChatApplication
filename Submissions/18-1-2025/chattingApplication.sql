-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema chattingApplicationDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema chattingApplicationDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chattingApplicationDB` DEFAULT CHARACTER SET utf8 ;
USE `chattingApplicationDB` ;

-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`Users` (
  `ID` BIGINT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `phone_number` VARCHAR(16) NOT NULL,
  `email` VARCHAR(320) NOT NULL,
  `picture` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(128) NOT NULL,
  `gender` VARCHAR(1) NOT NULL,
  `country` VARCHAR(56) NOT NULL,
  `birth_date` DATE NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `status` VARCHAR(20) NOT NULL,
  `bio` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `phone_number` (`phone_number` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 25
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`Group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`Group` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `admin_id` BIGINT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  INDEX `admin_id_idx` (`admin_id` ASC) VISIBLE,
  CONSTRAINT `fk_admin_id`
    FOREIGN KEY (`admin_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`OneToOneChat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`OneToOneChat` (
  `ID` BIGINT NOT NULL,
  `participant_one` BIGINT NOT NULL,
  `participant_two` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `particpant_one_idx` (`participant_one` ASC) VISIBLE,
  INDEX `participant_two_idx` (`participant_two` ASC) VISIBLE,
  CONSTRAINT `fk_particpant_one`
    FOREIGN KEY (`participant_one`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_participant_two`
    FOREIGN KEY (`participant_two`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`GroupChat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`GroupChat` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `group_id` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `group_id_idx` (`group_id` ASC),
  CONSTRAINT `fk_group_id`
    FOREIGN KEY (`group_id`)
    REFERENCES `chattingApplicationDB`.`Group` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`Messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`Messages` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `sender_id` BIGINT NOT NULL,
  `one_to_one_chat_id` BIGINT NOT NULL,
  `group_chat_id` BIGINT NULL,
  `sent_at` TIMESTAMP NOT NULL,
  `content` VARCHAR(400) NOT NULL,
  `file_url` VARCHAR(255) NULL DEFAULT NULL,
  `is_read` TINYINT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `chat_messages_unique` (`ID` ASC, `one_to_one_chat_id` ASC) VISIBLE,
  INDEX `sender_id_idx` (`sender_id` ASC) VISIBLE,
  INDEX `one_to_one_chat_id_idx` (`one_to_one_chat_id` ASC) VISIBLE,
  INDEX `group_chat_id_idx` (`group_chat_id` ASC) VISIBLE,
  CONSTRAINT `fk_sender_messages_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`),
  CONSTRAINT `fk_one_to_one_chat_id`
    FOREIGN KEY (`one_to_one_chat_id`)
    REFERENCES `chattingApplicationDB`.`OneToOneChat` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `messages_fk_group_chat_id`
    FOREIGN KEY (`group_chat_id`)
    REFERENCES `chattingApplicationDB`.`GroupChat` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
    CONSTRAINT `check_one_fk_not_null`
    CHECK (
      (`one_to_one_chat_id` IS NOT NULL AND `group_chat_id` IS NULL) OR
      (`one_to_one_chat_id` IS NULL AND `group_chat_id` IS NOT NULL)
    )
    )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`Notifications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`Notifications` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `receiver_id` BIGINT NOT NULL,
  `type` ENUM("NewStatus", "NewMessage") NOT NULL,
  `contact_id` BIGINT NULL DEFAULT NULL,
  `message_id` BIGINT NULL DEFAULT NULL,
  `is_read` TINYINT NOT NULL,
  `sent_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  INDEX `receiver_id_idx` (`receiver_id` ASC) VISIBLE,
  INDEX `contact_id_idx` (`contact_id` ASC) VISIBLE,
  INDEX `message_id_idx` (`message_id` ASC) VISIBLE,
  CONSTRAINT `fk_receiver_notifications_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_contact_notifications_id`
    FOREIGN KEY (`contact_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_message_id`
    FOREIGN KEY (`message_id`)
    REFERENCES `chattingApplicationDB`.`Messages` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
     CONSTRAINT `chk_type_contact_message`
    CHECK (
      (type = 'NewStatus' AND contact_id IS NOT NULL AND message_id IS NULL) OR
      (type = 'NewMessage' AND message_id IS NOT NULL AND contact_id IS NULL)
    )

    )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`GroupMembers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`GroupMembers` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `group_id` BIGINT NOT NULL,
  `member_id` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `group_id_idx` (`group_id` ASC) VISIBLE,
  INDEX `memeber_id_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_members_group_id`
    FOREIGN KEY (`group_id`)
    REFERENCES `chattingApplicationDB`.`Group` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_member_id`
    FOREIGN KEY (`member_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
    CONSTRAINT `unique_group_member`
    UNIQUE (`group_id`, `member_id`)
    )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`ContactInvitation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`ContactInvitation` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `sender_id` BIGINT NOT NULL,
  `receiver_id` BIGINT NOT NULL,
  `status` TINYINT NULL DEFAULT NULL,
  `sent_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  INDEX `receiver_id_idx` (`receiver_id` ASC) VISIBLE,
  INDEX `sender_id_idx` (`sender_id` ASC) VISIBLE,
  CONSTRAINT `fk_receiver_contact_invitation_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`),
  CONSTRAINT `fk_sender_contact_invitation_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`))
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `chattingApplicationDB`.`Contacts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chattingApplicationDB`.`Contacts` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `contact_id` BIGINT NOT NULL,
  `category` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  INDEX `contact_id_idx` (`contact_id` ASC) VISIBLE,
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_contact_contacts_id`
    FOREIGN KEY (`contact_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`),
  CONSTRAINT `fk_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `chattingApplicationDB`.`Users` (`ID`),
    CONSTRAINT `unique_contacts`
    UNIQUE (`user_id`, `contact_id`)
    )
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
