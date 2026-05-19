/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.3-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: lovecode_db
-- ------------------------------------------------------
-- Server version	11.8.3-MariaDB-0+deb13u1 from Debian

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `Historial_Logs`
--

DROP TABLE IF EXISTS `Historial_Logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Historial_Logs` (
  `id_log` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `resultado` enum('EXITO','FALLO') NOT NULL,
  `fecha` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_log`),
  KEY `email` (`email`),
  CONSTRAINT `Historial_Logs_ibfk_1` FOREIGN KEY (`email`) REFERENCES `Usuarios` (`email`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Historial_Logs`
--

LOCK TABLES `Historial_Logs` WRITE;
/*!40000 ALTER TABLE `Historial_Logs` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `Historial_Logs` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Likes`
--

DROP TABLE IF EXISTS `Likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Likes` (
  `id_like` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario_da` int(11) DEFAULT NULL,
  `id_usuario_recibe` int(11) DEFAULT NULL,
  `fecha_like` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_like`),
  UNIQUE KEY `id_usuario_da` (`id_usuario_da`,`id_usuario_recibe`),
  KEY `id_usuario_recibe` (`id_usuario_recibe`),
  CONSTRAINT `Likes_ibfk_1` FOREIGN KEY (`id_usuario_da`) REFERENCES `Usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `Likes_ibfk_2` FOREIGN KEY (`id_usuario_recibe`) REFERENCES `Usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Likes`
--

LOCK TABLES `Likes` WRITE;
/*!40000 ALTER TABLE `Likes` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Likes` VALUES
(35,34,35,'2026-05-12 09:57:10'),
(38,36,34,'2026-05-12 10:50:31'),
(39,34,36,'2026-05-12 10:53:35'),
(42,34,37,'2026-05-14 07:12:11'),
(43,34,39,'2026-05-14 09:44:45'),
(44,39,35,'2026-05-14 09:50:12'),
(46,35,39,'2026-05-14 09:56:35'),
(47,35,34,'2026-05-14 10:07:14'),
(48,35,36,'2026-05-14 10:07:16'),
(49,35,37,'2026-05-14 10:07:18'),
(50,35,38,'2026-05-14 10:07:19'),
(51,35,40,'2026-05-14 10:07:20'),
(52,36,37,'2026-05-14 11:11:15'),
(55,37,52,'2026-05-15 09:46:18'),
(56,37,42,'2026-05-15 09:46:20'),
(57,37,43,'2026-05-15 09:46:21');
/*!40000 ALTER TABLE `Likes` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`administrador`@`%`*/ /*!50003 TRIGGER tr_after_insert_like
AFTER INSERT ON Likes
FOR EACH ROW
BEGIN
    -- 1. Comprobar si el otro usuario ya le había dado like al usuario actual (Like Mutuo)
    -- Buscamos un registro donde:
    -- id_usuario_da = usuario que recibe el nuevo like
    -- id_usuario_recibe = usuario que acaba de dar el like
    IF EXISTS (SELECT 1 FROM Likes WHERE id_usuario_da = NEW.id_usuario_recibe AND id_usuario_recibe = NEW.id_usuario_da) THEN
        
        -- 2. Comprobar si comparten al menos una tecnología
        -- Unimos la tabla Usuarios_Tecnologias consigo misma por el id_tecnologia
        IF EXISTS (
            SELECT 1 
            FROM Usuarios_Tecnologias ut1
            JOIN Usuarios_Tecnologias ut2 ON ut1.id_tecnologia = ut2.id_tecnologia
            WHERE ut1.id_usuario = NEW.id_usuario_da AND ut2.id_usuario = NEW.id_usuario_recibe
        ) THEN
            
            -- 3. Crear el match en la tabla Matches
            -- Usamos INSERT IGNORE para prevenir errores si ya existe (aunque el trigger solo salta en inserción)
            -- Ordenamos los IDs (LEAST/GREATEST) para mantener consistencia y evitar duplicados (id1, id2) vs (id2, id1)
            INSERT IGNORE INTO Matches (id_usuario1, id_usuario2, fecha_match)
            VALUES (
                LEAST(NEW.id_usuario_da, NEW.id_usuario_recibe), 
                GREATEST(NEW.id_usuario_da, NEW.id_usuario_recibe), 
                NOW()
            );
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`administrador`@`%`*/ /*!50003 TRIGGER tr_after_delete_like
AFTER DELETE ON Likes
FOR EACH ROW
BEGIN
    DELETE FROM Matches 
    WHERE id_usuario1 = LEAST(OLD.id_usuario_da, OLD.id_usuario_recibe)
      AND id_usuario2 = GREATEST(OLD.id_usuario_da, OLD.id_usuario_recibe);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Matches`
--

DROP TABLE IF EXISTS `Matches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Matches` (
  `id_match` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario1` int(11) DEFAULT NULL,
  `id_usuario2` int(11) DEFAULT NULL,
  `fecha_match` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_match`),
  UNIQUE KEY `id_usuario1` (`id_usuario1`,`id_usuario2`),
  KEY `id_usuario2` (`id_usuario2`),
  CONSTRAINT `Matches_ibfk_1` FOREIGN KEY (`id_usuario1`) REFERENCES `Usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `Matches_ibfk_2` FOREIGN KEY (`id_usuario2`) REFERENCES `Usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matches`
--

LOCK TABLES `Matches` WRITE;
/*!40000 ALTER TABLE `Matches` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Matches` VALUES
(2,35,39,'2026-05-14 09:56:35');
/*!40000 ALTER TABLE `Matches` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Tecnologias`
--

DROP TABLE IF EXISTS `Tecnologias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tecnologias` (
  `id_tecnologia` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id_tecnologia`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tecnologias`
--

LOCK TABLES `Tecnologias` WRITE;
/*!40000 ALTER TABLE `Tecnologias` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Tecnologias` VALUES
(3,'CSS'),
(2,'HTML'),
(1,'Java'),
(4,'JavaScript'),
(6,'Python'),
(5,'SQL'),
(7,'XML');
/*!40000 ALTER TABLE `Tecnologias` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Usuarios`
--

DROP TABLE IF EXISTS `Usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL,
  `estado_usuario` varchar(50) DEFAULT 'activo',
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios`
--

LOCK TABLES `Usuarios` WRITE;
/*!40000 ALTER TABLE `Usuarios` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Usuarios` VALUES
(34,'Pablo Pedrón López','pablo@pedron.com','$2a$10$iv0sBSqRdg920WhzwzZ6UOZXpoRxfj3EV4APTVHQy2DCSvRH6VKL.','Me gusta HTML CSS y Java','Valencia','activo','2026-05-07 16:35:51'),
(35,'Carlos Rubinyo','Carlitos@lovecode.es','$2a$10$w1o3dp47HRZqqlawtAn85OrAxG.2OxXot9JXXPpmP6y31659gKzO.','Me gusta HTML y CSS','Valencia','activo','2026-05-12 10:43:36'),
(36,'Alejandro Guti','alejandro@gmail.com','$2a$10$Dyp19fs9fwn/kf5DGIQX3Otk/6q/qf3GXZhbIbUsJQx5Dh.dRvl9G','Me gusta Python y SQL','Valencia','activo','2026-05-12 12:49:29'),
(37,'Alba','alba@gmail.com','$2a$10$wLYuU/TV.5BhcH9kLYIcL.qDZ4Vz3sKCqUUSO.fhhXxhEWtYcXSOm','Me gusta Python, SQL y además diseñar en CSS','Valencia','activo','2026-05-12 13:31:07'),
(38,'Sergio Chill','sergio@gmail.com','$2a$10$rC5ZOHjs.S9zHajoaUj3Qu33ckvEYvlC9jP5EgSZR5DxmDvyZYL9C','Me gusta JavaScript y XML','Valencia','activo','2026-05-13 13:37:20'),
(39,'Oscar Sasi','oscar@gmail.com','$2a$10$Tu3VoDwQTiKzZpph.Ux98eARKI3TiKMZI2878mJvjlguhgcPNLoBC','Me gusta todo','Vinalesa','activo','2026-05-13 13:40:05'),
(40,'Jose Luis','joseluis@gmail.com','$2a$10$Fr4FjYp8oitdq8H7eyeTv.LNZfHKcLqNBHCjD/SqRv9eAWswtOHuS','Fan de Java','Torrente','activo','2026-05-13 13:41:25'),
(42,'Esteban','esteban@gmail.com','$2a$10$qRgSYCPp2Dk1VoQCJQ2L3uFfKmu9XiueD1zfEhMdXNcDCDwggJ8Jy','Soy el dios del SQL','Valencia','activo','2026-05-13 13:42:56'),
(43,'Pedro','pedro@gmail.com','$2a$10$FF5/.6LnVmP01hpkmYvveu4cHYqaef7IYlYvQgz0repAaF8QWETBK','Me gusta el frontend','Valencia','activo','2026-05-13 13:44:04'),
(45,'Sara López','sara@gmail.com','$2a$10$nxRK1uc/9RsgpXMXncvrGuA.nEJozvrkl8Zz.40LWT6m1VWuY6pse','Me gusta Java y Python','Madrid','activo','2026-05-14 09:15:50'),
(52,'Marta G','marta@gmail.com','$2a$10$F6WvmFF934zfP3X5z9q8d.fFkFdRnT8zgicuMBiUvsvxyYOXJEDcK','Me gusta Java, JavaScript y hacer consultas SQL','Barcelona','activo','2026-05-14 09:18:31');
/*!40000 ALTER TABLE `Usuarios` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Usuarios_Tecnologias`
--

DROP TABLE IF EXISTS `Usuarios_Tecnologias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Usuarios_Tecnologias` (
  `id_usuario` int(11) NOT NULL,
  `id_tecnologia` int(11) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_tecnologia`),
  KEY `id_tecnologia` (`id_tecnologia`),
  CONSTRAINT `Usuarios_Tecnologias_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuarios` (`id_usuario`) ON DELETE CASCADE,
  CONSTRAINT `Usuarios_Tecnologias_ibfk_2` FOREIGN KEY (`id_tecnologia`) REFERENCES `Tecnologias` (`id_tecnologia`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios_Tecnologias`
--

LOCK TABLES `Usuarios_Tecnologias` WRITE;
/*!40000 ALTER TABLE `Usuarios_Tecnologias` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Usuarios_Tecnologias` VALUES
(39,1),
(40,1),
(45,1),
(52,1),
(35,2),
(43,2),
(35,3),
(37,3),
(39,3),
(43,3),
(38,4),
(39,4),
(43,4),
(52,4),
(36,5),
(37,5),
(39,5),
(42,5),
(52,5),
(36,6),
(37,6),
(39,6),
(45,6),
(38,7),
(39,7);
/*!40000 ALTER TABLE `Usuarios_Tecnologias` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Dumping routines for database 'lovecode_db'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `borrar_usuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `borrar_usuario`(IN p_id_usuario INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Usuarios WHERE id_usuario = p_id_usuario) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: el usuario no existe';
    ELSE
        DELETE FROM Usuarios WHERE id_usuario = p_id_usuario;
        SELECT 'Usuario eliminado correctamente' AS mensaje;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `contar_matches` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `contar_matches`(IN p_id_usuario INT)
BEGIN
    SELECT COUNT(*) AS total_matches
    FROM Matches
    WHERE id_usuario1 = p_id_usuario 
       OR id_usuario2 = p_id_usuario;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `crear_usuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `crear_usuario`(
    IN p_nombre       VARCHAR(100),
    IN p_email        VARCHAR(100),
    IN p_password     VARCHAR(255),
    IN p_descripcion  TEXT,
    IN p_ciudad       VARCHAR(100),
    IN p_estado       VARCHAR(50)
)
BEGIN
    IF EXISTS (SELECT 1 FROM Usuarios WHERE email = p_email) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: el email ya está registrado';
    ELSE
        INSERT INTO Usuarios (nombre, email, password, descripcion, ciudad, estado_usuario)
        VALUES (p_nombre, p_email, p_password, p_descripcion, p_ciudad, p_estado);
        SELECT LAST_INSERT_ID() AS id_usuario_creado;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_asignar_tecnologia_usuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_asignar_tecnologia_usuario`(IN p_id_usuario INT, IN p_id_tecnologia INT)
BEGIN
    INSERT INTO Usuarios_Tecnologias (id_usuario, id_tecnologia) VALUES (p_id_usuario, p_id_tecnologia);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_buscar_usuario_por_email` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_buscar_usuario_por_email`(IN p_email VARCHAR(100))
BEGIN
    SELECT id_usuario, nombre, password FROM Usuarios WHERE email = p_email;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_comparten_tecnologia` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_comparten_tecnologia`(IN p_id1 INT, IN p_id2 INT, OUT p_comparten INT)
BEGIN
    SELECT COUNT(*) INTO p_comparten 
    FROM Usuarios_Tecnologias ut1 
    JOIN Usuarios_Tecnologias ut2 ON ut1.id_tecnologia = ut2.id_tecnologia 
    WHERE ut1.id_usuario = p_id1 AND ut2.id_usuario = p_id2;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_comprobar_like_mutuo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_comprobar_like_mutuo`(IN p_yo INT, IN p_otro INT, OUT p_existe INT)
BEGIN
    SELECT COUNT(*) INTO p_existe 
    FROM Likes 
    WHERE id_usuario_da = p_otro AND id_usuario_recibe = p_yo;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_comprobar_si_hay_match` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_comprobar_si_hay_match`(
    IN p_id1 INT,
    IN p_id2 INT,
    OUT p_resultado INT
)
BEGIN
    SELECT COUNT(*) INTO p_resultado
    FROM Matches
    WHERE (id_usuario1 = p_id1 AND id_usuario2 = p_id2)
       OR (id_usuario1 = p_id2 AND id_usuario2 = p_id1);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_crear_match` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_crear_match`(IN p_id1 INT, IN p_id2 INT)
BEGIN
    DECLARE v_menor INT;
    DECLARE v_mayor INT;
    SET v_menor = LEAST(p_id1, p_id2);
    SET v_mayor = GREATEST(p_id1, p_id2);
    INSERT IGNORE INTO Matches (id_usuario1, id_usuario2) VALUES (v_menor, v_mayor);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_dar_like` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_dar_like`(IN p_id_da INT, IN p_id_recibe INT)
BEGIN
    INSERT INTO Likes (id_usuario_da, id_usuario_recibe) VALUES (p_id_da, p_id_recibe);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_listar_likes_dados` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_listar_likes_dados`(IN p_id_actual INT)
BEGIN
    SELECT u.id_usuario as id, u.nombre, u.email, u.ciudad, u.descripcion 
    FROM Usuarios u 
    INNER JOIN Likes l ON u.id_usuario = l.id_usuario_recibe 
    WHERE l.id_usuario_da = p_id_actual;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_listar_matches` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_listar_matches`(IN p_id_actual INT)
BEGIN
    SELECT u.id_usuario as id, u.nombre, u.email, u.ciudad, u.descripcion 
    FROM Usuarios u 
    INNER JOIN Matches m ON (u.id_usuario = m.id_usuario1 OR u.id_usuario = m.id_usuario2) 
    WHERE (m.id_usuario1 = p_id_actual OR m.id_usuario2 = p_id_actual) 
    AND u.id_usuario != p_id_actual;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_listar_tecnologias` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_listar_tecnologias`()
BEGIN
    SELECT id_tecnologia, nombre FROM Tecnologias ORDER BY nombre;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_listar_todos_usuarios` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_listar_todos_usuarios`()
BEGIN
    SELECT id_usuario as id, nombre, email, ciudad, descripcion FROM Usuarios;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_listar_usuarios_dashboard` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_listar_usuarios_dashboard`(IN p_id_actual INT)
BEGIN
    SELECT id_usuario as id, nombre, email, ciudad, descripcion 
    FROM Usuarios 
    WHERE id_usuario != p_id_actual 
    AND id_usuario NOT IN (SELECT id_usuario_recibe FROM Likes WHERE id_usuario_da = p_id_actual);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_obtener_tecnologias_usuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_obtener_tecnologias_usuario`(IN p_id_usuario INT)
BEGIN
    SELECT t.id_tecnologia, t.nombre 
    FROM Tecnologias t 
    INNER JOIN Usuarios_Tecnologias ut ON t.id_tecnologia = ut.id_tecnologia 
    WHERE ut.id_usuario = p_id_usuario 
    ORDER BY t.nombre;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_registrar_usuario` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`administrador`@`%` PROCEDURE `sp_registrar_usuario`(
    IN p_nombre VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_ciudad VARCHAR(100),
    IN p_descripcion TEXT,
    OUT p_id_generado INT
)
BEGIN
    INSERT INTO Usuarios (nombre, email, password, ciudad, descripcion)
    VALUES (p_nombre, p_email, p_password, p_ciudad, p_descripcion);
    SET p_id_generado = LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-05-19 12:01:16