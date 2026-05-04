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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Likes`
--

LOCK TABLES `Likes` WRITE;
/*!40000 ALTER TABLE `Likes` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Likes` VALUES
(1,1,4,'2026-04-16 10:44:06'),
(2,2,3,'2026-04-16 10:44:06'),
(3,3,1,'2026-04-16 10:44:06');
/*!40000 ALTER TABLE `Likes` ENABLE KEYS */;
UNLOCK TABLES;
commit;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Matches`
--

LOCK TABLES `Matches` WRITE;
/*!40000 ALTER TABLE `Matches` DISABLE KEYS */;
set autocommit=0;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios`
--

LOCK TABLES `Usuarios` WRITE;
/*!40000 ALTER TABLE `Usuarios` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `Usuarios` VALUES
(1,'Pablo Pedrón','pablopedron@lovecode.com','123','Me encanta Java, el diseño web y las bases de datos',NULL,'activo','2026-04-16 12:44:06'),
(2,'Pablo el Greus','pbalo@pbot.com','123','Soy un poco bot en programación, especialmente me gusta python',NULL,'activo','2026-04-16 12:44:06'),
(3,'Alejandro el Gutis','gutiloveguti@gmail.com','123','Buscando a alguien para hacer una web juntos.',NULL,'activo','2026-04-16 12:44:06'),
(4,'Sergio el Chill','nosequeponer@gmail.com','123','Intentando descifrar el código del amor. Me gusta el XML y se algo de Java.',NULL,'activo','2026-04-16 12:44:06');
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
(1,1),
(4,1),
(3,2),
(3,3),
(3,4),
(1,5),
(2,5),
(2,6),
(4,7);
/*!40000 ALTER TABLE `Usuarios_Tecnologias` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-04-23  8:50:14