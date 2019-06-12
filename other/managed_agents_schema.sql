
/*
Sample query to find things within 25 km of the co-ordinates
37, -122

SELECT id, ( 6371 * acos( cos( radians(37) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(-122) ) + sin( radians(37) ) * sin( radians( lat ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 20;


INSERT INTO `users` VALUES (1,'ELG6W7Apqg/wd+0YRySfUWa70p4=','miE/pjqxy74=','Mr.','James','Ostrowick','Lawrence','MALE','james@ostrowick.co.za','South Africa','Johannesburg','None','0115551234','082551234','Just changing other details\nMore detail','1974-10-23','2017-04-26 18:56:04','ENABLED');
INSERT INTO companies (company_id, company_name, website_address,physical_address, postal_address, company_logo ) values (1,'Main Company', 'https://www.google.com', 'None','None','None');
INSERT INTO company_users values (0,1,1,1,1);

*/

--
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `company_logo` text NOT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
--
-- Table structure for table `company_users`
--

DROP TABLE IF EXISTS `company_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `company_groups`
--

DROP TABLE IF EXISTS `company_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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



DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_images` (
  `product_image_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) unsigned NOT NULL DEFAULT '0',
  `image_description` varchar(256) NOT NULL DEFAULT 'None',
  `image_uri` text NOT NULL,
  PRIMARY KEY (`product_image_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `fk_image_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*
status_types : NEW, COMPLETE, INCOMPLETE
COMPLETE - All items are in an invoice
INCOMPLETE - Some items missing from an invoice
*/
DROP TABLE IF EXISTS `orders`;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
Status can be CREATED, ACCEPTED, DECLINED, MISSED, CANCELED
*/
CREATE TABLE `appointments` (
  `appointment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `company_id` int(10) unsigned DEFAULT NULL,
  `start_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `end_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
  `appointment_notes` text not null,
  `appointment_status` varchar(16) not null default 'CREATED',
  PRIMARY KEY (`appointment_id`),
  CONSTRAINT `fk_appointment_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_appointment_company_id` FOREIGN KEY (`company_id`) REFERENCES `companies` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `appointment_log` (
    `log_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `appointment_id` int(10) unsigned NOT NULL,
    `user_id` int(10) unsigned DEFAULT NULL,
    `modification_date` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
    `start_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
    `end_time` datetime NOT NULL DEFAULT '2011-01-01 00:00:00',
    `log_comment` text not null,
    `appointment_status` varchar(16) not null default 'CREATED',
    PRIMARY KEY (`log_id`),
    CONSTRAINT `fk_appointment_log_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_appointment_log_appointment_id` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appointment_id`) ON DELETE CASCADE ON UPDATE CASCADE    
);