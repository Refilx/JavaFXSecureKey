-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema javafxsecurekey
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema javafxsecurekey
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `javafxsecurekey` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `javafxsecurekey` ;

-- -----------------------------------------------------
-- Table `javafxsecurekey`.`chaves`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`chaves` (
  `idChave` INT NOT NULL AUTO_INCREMENT,
  `numeroChave` INT NOT NULL,
  `sala` VARCHAR(60) NOT NULL,
  `bloco_predio` VARCHAR(60) NOT NULL,
  `observacoes` VARCHAR(60) NOT NULL,
  `quantChave` INT NOT NULL,
  `status` VARCHAR(30) NOT NULL,
  `possuiReserva` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`idChave`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `javafxsecurekey`.`pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`pessoa` (
  `idPessoa` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(240) NOT NULL,
  `cpf` VARCHAR(14) NOT NULL,
  `email` VARCHAR(240) NOT NULL,
  `telefone` VARCHAR(14) NOT NULL,
  `empresa` VARCHAR(240) NOT NULL,
  `cargo` VARCHAR(240) NOT NULL,
  `dtRegistro` TIMESTAMP NOT NULL,
  PRIMARY KEY (`idPessoa`),
  UNIQUE INDEX `cpf` (`cpf` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `javafxsecurekey`.`historico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`historico` (
  `idHistorico` INT NOT NULL AUTO_INCREMENT,
  `idChave` INT NOT NULL,
  `idPessoa` INT NOT NULL,
  `observacoes` VARCHAR(500) NOT NULL,
  `status` VARCHAR(30) NOT NULL,
  `dataAbertura` TIMESTAMP NOT NULL,
  `dataFechamento` TIMESTAMP NULL,
  PRIMARY KEY (`idHistorico`),
  INDEX `idPessoa` (`idPessoa` ASC) VISIBLE,
  INDEX `idChave` (`idChave` ASC) VISIBLE,
  CONSTRAINT `historico_chave`
    FOREIGN KEY (`idChave`)
    REFERENCES `javafxsecurekey`.`chaves` (`idChave`),
  CONSTRAINT `historico_pessoa`
    FOREIGN KEY (`idPessoa`)
    REFERENCES `javafxsecurekey`.`pessoa` (`idPessoa`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `javafxsecurekey`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`usuario` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(120) NOT NULL,
  `password` VARCHAR(240) NOT NULL,
  `role` VARCHAR(30) NOT NULL,
  `dtRegistro` DATE NOT NULL,
  `idPessoa` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  INDEX `idPessoa` (`idPessoa` ASC) VISIBLE,
  CONSTRAINT `user_pessoa`
    FOREIGN KEY (`idPessoa`)
    REFERENCES `javafxsecurekey`.`pessoa` (`idPessoa`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `javafxsecurekey`.`log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`log` (
  `idLog` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `dtLogin` TIMESTAMP NOT NULL,
  `dtLogout` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`idLog`),
  INDEX `idUsuario` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `logs_user`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `javafxsecurekey`.`usuario` (`idUsuario`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `javafxsecurekey` ;

-- -----------------------------------------------------
-- Placeholder table for view `javafxsecurekey`.`consulta_histórico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`consulta_histórico` (`idHistorico` INT, `idChave` INT, `idPessoa` INT, `observacoes` INT, `status` INT, `dataAbertura` INT, `dataFechamento` INT, `numeroChave` INT, `nome` INT, `cargo` INT);

-- -----------------------------------------------------
-- Placeholder table for view `javafxsecurekey`.`lista_meses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`lista_meses` (`ano_mes` INT);

-- -----------------------------------------------------
-- Placeholder table for view `javafxsecurekey`.`ultimo_logado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`ultimo_logado` (`idLog` INT, `idUsuario` INT, `username` INT, `role` INT);

-- -----------------------------------------------------
-- Placeholder table for view `javafxsecurekey`.`usuario_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `javafxsecurekey`.`usuario_list` (`username` INT, `role` INT, `telefone` INT, `email` INT);

-- -----------------------------------------------------
-- procedure verificaPessoaJaPegou
-- -----------------------------------------------------

DELIMITER $$
USE `javafxsecurekey`$$
CREATE PROCEDURE verificaPessoaJaPegou(IN chave int, IN pessoa int)
BEGIN
    IF EXISTS (SELECT 1 FROM historico H WHERE idChave = chave AND idPessoa = pessoa AND status = 'EM ABERTO') THEN
        SELECT 'false' as result;
	ELSE
        SELECT 'true' as result;
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `javafxsecurekey`.`consulta_histórico`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `javafxsecurekey`.`consulta_histórico`;
USE `javafxsecurekey`;
CREATE  OR REPLACE VIEW consulta_histórico AS
	SELECT H.idHistorico, H.idChave, H.idPessoa, H.observacoes, H.status, H.dataAbertura, H.dataFechamento, C.numeroChave, P.nome, P.cargo
    FROM historico H
    JOIN chaves C ON (H.idChave = C.idChave)
    JOIN pessoa P ON (H.idPessoa = P.idPessoa)
    ORDER BY H.idHistorico DESC;

-- -----------------------------------------------------
-- View `javafxsecurekey`.`lista_meses`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `javafxsecurekey`.`lista_meses`;
USE `javafxsecurekey`;
CREATE  OR REPLACE VIEW lista_meses AS
	SELECT DISTINCT DATE_FORMAT(dataAbertura,'%Y-%m') AS ano_mes 
    FROM historico;

-- -----------------------------------------------------
-- View `javafxsecurekey`.`ultimo_logado`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `javafxsecurekey`.`ultimo_logado`;
USE `javafxsecurekey`;
CREATE  OR REPLACE VIEW ultimo_logado AS
	SELECT L.idLog, L.idUsuario, U.username, U.role 
	FROM log L
	JOIN usuario U ON (L.idUsuario = U.idUsuario)
	ORDER BY idLog DESC
	LIMIT 1;

-- -----------------------------------------------------
-- View `javafxsecurekey`.`usuario_list`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `javafxsecurekey`.`usuario_list`;
USE `javafxsecurekey`;
CREATE  OR REPLACE VIEW usuario_list AS
	SELECT U.username, U.role, P.telefone, P.email 
    FROM usuario U
    JOIN pessoa P ON (U.idPessoa = P.idPessoa);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
