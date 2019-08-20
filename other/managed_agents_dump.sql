CREATE TABLE `appointments` (
  `appointment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `company_id` int(10) unsigned DEFAULT NULL,
  `start_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `end_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `appointment_notes` text NOT NULL,
  `appointment_status` varchar(16) NOT NULL DEFAULT 'CREATED',
  PRIMARY KEY (`appointment_id`),
  KEY `fk_appointment_user_id` (`user_id`),
  KEY `fk_appointment_company_id` (`company_id`),
  CONSTRAINT `fk_appointment_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_appointment_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `appointments` VALUES (1,1,2,'2019-08-14 11:00:00','2019-08-14 11:30:00','None','CREATED'),(2,1,3,'2019-06-12 13:00:00','2019-06-12 15:00:00','None','CREATED'),(3,2,2,'2019-06-20 14:00:00','2019-06-20 14:15:00','Important meeting.','CREATED'),(4,3,3,'2019-06-28 10:00:00','2019-06-28 11:00:00','None','CREATED'),(5,4,2,'2019-06-28 09:00:00','2019-06-28 10:00:00','None','CREATED'),(6,2,3,'2019-06-19 13:30:00','2019-06-19 14:00:00','Testing a second appointment to check what it looks like in app','CREATED'),(7,2,2,'2019-06-28 14:00:00','2019-06-28 14:30:00','Edit from App','ACCEPTED'),(8,4,2,'2019-07-25 12:00:00','2019-07-25 12:30:00','None','CREATED'),(9,2,2,'2019-08-29 00:00:00','2019-08-29 00:00:00','None','CREATED'),(10,2,4,'2019-07-12 12:00:00','2019-07-12 12:30:00','None','CREATED');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `companies` (
  `company_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `company_name` varchar(256) NOT NULL DEFAULT 'None',
  `phone_number` varchar(24) NOT NULL DEFAULT '000-000-0000',
  `cell_number` varchar(24) NOT NULL DEFAULT '000-000-0000',
  `website_address` text NOT NULL,
  `physical_address` text NOT NULL,
  `postal_address` text NOT NULL,
  `vat_number` varchar(32) NOT NULL DEFAULT 'NONE',
  `modification_date` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `location_latitude` decimal(10,8) NOT NULL DEFAULT '0.00000000',
  `location_longitude` decimal(11,8) NOT NULL DEFAULT '0.00000000',
  `company_status` varchar(16) NOT NULL DEFAULT 'NEW',
  `image_uri` text NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `companies` VALUES (1,'Kalbe International','0115551234','0825551234','www.home.com','12 Wolmarans St, Eden Glen, Edenvale, 1613, South Africa','Edenvale','123456','2011-01-01 00:00:00',-26.12972280,28.17031150,'OPEN','NONE'),(2,'Makro','0115551234','0825551234','https://www.makro.co.za','33 Heidelberg Rd, Newmarket Park, Alberton, 1449, South Africa','Meadowdale, Edenvale','123456789','2011-01-01 00:00:00',-26.28086840,28.12733670,'OPEN','e172a49d113d4f7db8f85f2b18dcc508'),(3,'Joe\'s Spaza Shop','0105501000','082','None','15 Alberton Blvd, New Redruth, Alberton, 1449, South Africa','None','None','2019-06-06 07:20:59',-26.26146777,28.11765946,'OPEN','NONE'),(4,'Pick n Pay','0105551234','0725551234','www.picknpay.co.za','124 Commissioner St, Marshalltown, Johannesburg, 2107, South Africa','Something','1234567','2019-06-22 09:10:25',-26.20532898,28.04381490,'REMOVED','NONE');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `company_groups` (
  `company_group_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent_company_id` int(10) unsigned NOT NULL,
  `company_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`company_group_id`),
  KEY `parent_company_id` (`parent_company_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `fk_group_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_parent_company_id` FOREIGN KEY (`parent_company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `company_users` (
  `company_user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `company_id` int(10) unsigned NOT NULL,
  `change_settings` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`company_user_id`),
  KEY `user_id` (`user_id`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `fk_company_users_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_company_users_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `company_users` VALUES (1,2,1,1),(3,2,3,1),(4,2,4,1),(5,1,1,1),(6,1,2,1),(7,1,3,1),(8,1,4,1),(9,3,3,0),(10,4,3,0),(25,3,1,1),(26,4,2,1),(34,2,2,1);
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `order_items` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL DEFAULT '0',
  `product_id` int(10) unsigned NOT NULL DEFAULT '0',
  `user_id` int(10) unsigned DEFAULT NULL,
  `item_description` text NOT NULL,
  `item_discount` double NOT NULL DEFAULT '0',
  `item_price` double NOT NULL DEFAULT '0',
  `item_quantity` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`item_id`),
  KEY `fk_order_id` (`order_id`),
  KEY `fk_product_id` (`product_id`),
  KEY `fk_order_items_user_id` (`user_id`),
  CONSTRAINT `fk_items_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_items_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_items_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `order_items` VALUES (1,1,4,2,'Samsung Galaxy s10',0,12999,1),(2,1,1,2,'Pixel 3',0,2999,1),(3,2,3,NULL,'iPad Air',0,6999,1),(4,2,2,NULL,'iPhone 8',0,12999,5);
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `orders` (
  `order_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `company_id` int(10) unsigned DEFAULT NULL,
  `order_date` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `order_comments` text NOT NULL,
  `editing_user_id` int(10) unsigned DEFAULT NULL,
  `modification_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status_type` varchar(32) NOT NULL DEFAULT 'NEW',
  PRIMARY KEY (`order_id`),
  KEY `fk_order_user` (`user_id`),
  KEY `fk_order_editing_user` (`editing_user_id`),
  KEY `fk_order_company_id` (`company_id`),
  CONSTRAINT `fk_order_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_editing_user` FOREIGN KEY (`editing_user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `orders` VALUES (1,2,2,'2019-07-26 06:51:00','New',NULL,'2019-07-26 06:51:16','NEW'),(2,2,4,'2019-07-26 06:52:21','New Order',NULL,'2019-07-26 06:52:21','NEW');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `product_images` (
  `product_image_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned NOT NULL DEFAULT '0',
  `image_description` varchar(256) NOT NULL DEFAULT 'None',
  `image_uri` text NOT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `fk_image_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `product_images` VALUES (5,4,'Samsung Galaxy s10','cbcb2b9779724c7fae7664d31bce0bd3'),(6,3,'iPad Air','62a868c2aa1444dbaa789a1c7923dfa2'),(7,2,'iPhone 8','aef180e10f2a4e16a1b368cf5a647b43'),(9,1,'Pixel 3','a1459e7719e449f583f425d8d899383a'),(12,5,'iPhone XR','0d8e3a6f9e8046ebaa2bc1f0104feb62');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `products` (
  `product_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_description` text NOT NULL,
  `product_maximum` int(10) unsigned NOT NULL DEFAULT '0',
  `product_price` double NOT NULL DEFAULT '0',
  `product_valid_from` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `product_valid_until` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `fk_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `products` VALUES (1,'Pixel 3',150,2999,'2018-11-19 00:02:00','2019-11-19 00:00:00'),(2,'iPhone 8',100,12999,'2019-05-24 07:56:00','2019-11-19 00:00:00'),(3,'iPad Air',150,6999,'2019-05-30 11:47:00','2020-01-01 00:00:00'),(4,'Samsung Galaxy s10',100,12999,'2019-06-18 04:10:00','2020-05-15 00:00:00'),(5,'iPhone XR',100,15999,'2019-06-19 06:45:00','2021-06-19 06:45:00');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_pass` varchar(28) NOT NULL DEFAULT 'None',
  `user_salt` varchar(16) NOT NULL DEFAULT 'None',
  `title` varchar(6) NOT NULL DEFAULT 'Mr.',
  `first_name` varchar(32) NOT NULL DEFAULT 'None',
  `surname` varchar(32) NOT NULL DEFAULT 'None',
  `other_name` varchar(32) NOT NULL DEFAULT 'None',
  `gender` varchar(16) NOT NULL DEFAULT 'MALE',
  `email_address` varchar(128) NOT NULL DEFAULT 'none',
  `phone_number` varchar(24) NOT NULL DEFAULT 'None',
  `cell_number` varchar(24) NOT NULL DEFAULT 'None',
  `postal_address` text NOT NULL,
  `date_of_birth` date NOT NULL DEFAULT '1970-01-01',
  `first_registered` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `user_status` varchar(16) NOT NULL DEFAULT 'NEW',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_address` (`email_address`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `users` VALUES (1,'i8k7Swd9cVI/cQIS75gcrdUldd8=','rpP9ezAiVgI=','Mr.','James','Ostrowick','Lawrence','MALE','james@ostrowick.co.za','+27827757851','+27','None','2019-05-07','2016-10-05 10:07:56','ADMIN'),(2,'afRmGazxV7z7lKE8gfCGdo6hv+A=','j67x23eSLyg=','Dr.','Luke','Engelbrecht','None','MALE','luke@home.com','+275551234','+275551122','None','1969-12-20','2019-05-24 14:09:04','ADMIN'),(3,'dHecyvGQP2ZtbmTI8gvsJTSDikE=','H8+tm2e/WR8=','Mr.','Jim','Bob','None','MALE','jim@bob.com','+27','+27','South Africa','2019-06-12','2019-06-12 06:49:22','CUSTOMER'),(4,'y2tyT2m9K2rLIiMTOKTCREfMiQk=','DRty7s8U/ak=','Miss.','Sue','Smith','None','FEMALE','sue@smith.com','+27','+27','South Africa','2019-06-12','2019-06-12 06:49:46','USER');