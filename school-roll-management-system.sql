-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: school_roll_management_system
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `award_punish`
--

DROP TABLE IF EXISTS `award_punish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `award_punish` (
  `apno` char(10) NOT NULL,
  `asno` char(10) DEFAULT NULL,
  `aplevel` varchar(30) NOT NULL,
  `aproject` varchar(100) NOT NULL,
  `adate` date NOT NULL,
  PRIMARY KEY (`apno`),
  KEY `asno` (`asno`),
  CONSTRAINT `award_punish_ibfk_1` FOREIGN KEY (`asno`) REFERENCES `student` (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `award_punish`
--

LOCK TABLES `award_punish` WRITE;
/*!40000 ALTER TABLE `award_punish` DISABLE KEYS */;
INSERT INTO `award_punish` VALUES ('0001','230001','学习进步奖','为鼓励东雪莲同学学习进步，特颁发此奖','2023-03-27');
/*!40000 ALTER TABLE `award_punish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `cno` char(8) NOT NULL,
  `cname` varchar(40) NOT NULL,
  `cnumber` smallint NOT NULL,
  `mno` char(8) DEFAULT NULL,
  PRIMARY KEY (`cno`),
  KEY `mno` (`mno`),
  CONSTRAINT `class_ibfk_1` FOREIGN KEY (`mno`) REFERENCES `major` (`mno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` VALUES ('0001','软件工程1班',8,'0001'),('0002','软件工程2班',2,'0001'),('0003','计算机网络技术1班',0,'0005'),('0004','计算机网络技术2班',0,'0005'),('0005','人工智能1班',0,'0002');
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `kno` char(8) NOT NULL,
  `kname` varchar(20) NOT NULL,
  `tno` char(10) DEFAULT NULL,
  `kperiod` tinyint NOT NULL,
  `kcredit` tinyint NOT NULL,
  PRIMARY KEY (`kno`),
  KEY `tno` (`tno`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`tno`) REFERENCES `teacher` (`tno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('0001','数据库应用','1001',40,3),('0002','数据结构','1001',40,3),('0003','密码学应用','1002',40,3),('0004','JAVA语言','1002',40,3),('0005','C语言','1001',40,3),('0006','javaWeb开发','1002',50,3),('0007','信息安全概述','1002',50,3),('0008','Web开发','1001',45,3);
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `depart`
--

DROP TABLE IF EXISTS `depart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `depart` (
  `dno` char(8) NOT NULL,
  `dname` varchar(30) NOT NULL,
  KEY `idx_depart_dno` (`dno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `depart`
--

LOCK TABLES `depart` WRITE;
/*!40000 ALTER TABLE `depart` DISABLE KEYS */;
INSERT INTO `depart` VALUES ('001','计算机与大数据学院'),('002','海峡学院'),('003','新华都商学院'),('004','地海学院');
/*!40000 ALTER TABLE `depart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade` (
  `kno` char(8) NOT NULL,
  `sno` char(10) NOT NULL,
  `grade` tinyint DEFAULT NULL,
  KEY `kno` (`kno`),
  KEY `sno` (`sno`),
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`kno`) REFERENCES `course` (`kno`),
  CONSTRAINT `grade_ibfk_2` FOREIGN KEY (`sno`) REFERENCES `student` (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grade`
--

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;
INSERT INTO `grade` VALUES ('0001','230001',79),('0002','230001',88),('0003','230001',91),('0001','230012',65);
/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `mno` char(8) NOT NULL,
  `mname` varchar(30) NOT NULL,
  `dno` char(8) DEFAULT NULL,
  PRIMARY KEY (`mno`),
  KEY `dno` (`dno`),
  CONSTRAINT `major_ibfk_1` FOREIGN KEY (`dno`) REFERENCES `depart` (`dno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `major`
--

LOCK TABLES `major` WRITE;
/*!40000 ALTER TABLE `major` DISABLE KEYS */;
INSERT INTO `major` VALUES ('0001','软件工程','001'),('0002','人工智能','001'),('0003','大数据应用','001'),('0004','计算机科学','001'),('0005','计算机网络','001');
/*!40000 ALTER TABLE `major` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `sno` char(10) NOT NULL,
  `sname` varchar(10) NOT NULL,
  `ssex` char(2) NOT NULL,
  `sage` tinyint NOT NULL,
  `sbirth` date NOT NULL,
  `sadmission` date NOT NULL,
  `cno` char(8) DEFAULT NULL,
  `mno` char(8) DEFAULT NULL,
  PRIMARY KEY (`sno`),
  KEY `cno` (`cno`),
  KEY `mno` (`mno`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`cno`) REFERENCES `class` (`cno`),
  CONSTRAINT `student_ibfk_2` FOREIGN KEY (`mno`) REFERENCES `major` (`mno`),
  CONSTRAINT `student_chk_1` CHECK ((`ssex` in (_gbk'��',_gbk'Ů')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('230001','东雪莲','女',19,'2004-05-03','2022-09-01','0001','0001'),('230006','南风儿','女',18,'2004-12-20','2022-09-01','0001','0001'),('230007','北雨梦','女',19,'2003-05-10','2022-09-01','0001','0001'),('230008','东林子','男',18,'2004-10-03','2022-09-01','0001','0001'),('230009','北风儿','女',18,'2004-12-20','2022-09-01','0001','0001'),('230010','南雨梦','女',19,'2003-05-10','2022-09-01','0001','0001'),('230011','昊京','男',18,'2004-10-03','2022-09-01','0001','0001'),('230012','孙笑川','男',19,'2004-06-01','2022-09-01','0001','0001');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = gbk */ ;
/*!50003 SET character_set_results = gbk */ ;
/*!50003 SET collation_connection  = gbk_chinese_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `insert_student_update_class_cnumber` AFTER INSERT ON `student` FOR EACH ROW BEGIN
    
    UPDATE class
    SET cnumber = cnumber + 1
    WHERE cno = NEW.cno;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = gbk */ ;
/*!50003 SET character_set_results = gbk */ ;
/*!50003 SET collation_connection  = gbk_chinese_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_class_cnumber` BEFORE UPDATE ON `student` FOR EACH ROW BEGIN
    
    IF NEW.cno <> OLD.cno THEN
        
        UPDATE class
        SET cnumber = cnumber - 1
        WHERE cno = OLD.cno;

        
        UPDATE class
        SET cnumber = cnumber + 1
        WHERE cno = NEW.cno;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `tno` char(10) NOT NULL,
  `tname` varchar(30) NOT NULL,
  `ttitle` char(20) NOT NULL,
  PRIMARY KEY (`tno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('1001','张三','讲师'),('1002','李四','讲师'),('1003','王五','讲师'),('1004','少昊','讲师'),('1005','赵六','讲师');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `role` enum('student','teacher','admin') NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('1001','111111','teacher'),('1002','111111','teacher'),('1003','111111','teacher'),('1231','111111','student'),('1233','111111','student'),('230001','111111','student'),('admin','111111','admin'),('s01','111111','student'),('t01','111111','teacher');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `view_student_info`
--

DROP TABLE IF EXISTS `view_student_info`;
/*!50001 DROP VIEW IF EXISTS `view_student_info`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_student_info` AS SELECT 
 1 AS `学号`,
 1 AS `姓名`,
 1 AS `院系`,
 1 AS `专业`,
 1 AS `班级`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_student_info`
--

/*!50001 DROP VIEW IF EXISTS `view_student_info`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = gbk */;
/*!50001 SET character_set_results     = gbk */;
/*!50001 SET collation_connection      = gbk_chinese_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_student_info` AS select `student`.`sno` AS `ѧ��`,`student`.`sname` AS `����`,`depart`.`dname` AS `Ժϵ`,`major`.`mname` AS `רҵ`,`class`.`cname` AS `�༶` from (((`student` join `class` on((`student`.`cno` = `class`.`cno`))) join `major` on((`student`.`mno` = `major`.`mno`))) join `depart` on((`major`.`dno` = `depart`.`dno`))) order by `student`.`sno` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-20  4:09:07
