SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `address`
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `addressid` int(255) NOT NULL AUTO_INCREMENT,
  `addrLine1` varchar(255) DEFAULT NULL,
  `addrLine2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `usState` varchar(2) DEFAULT NULL,
  `zip` varchar(5) DEFAULT NULL,
  `xStreet` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`email` varchar(255) NOT NULL,
#  `userid` int(11) NOT NULL AUTO_INCREMENT,
#  `username` varchar(255) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
#  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `contactMethod` varchar(255) DEFAULT NULL,
  `address_addressid` int(255) NOT NULL,
#  PRIMARY KEY (`userid`),
	PRIMARY KEY (`email`),
  KEY `fk_user_address1` (`address_addressid`),
  CONSTRAINT `fk_user_address1` FOREIGN KEY (`address_addressid`) REFERENCES `address` (`addressid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Donation status table
DROP TABLE IF EXISTS `donationStatus`;
CREATE TABLE `donationStatus` (
	`statusid` int(1) NOT NULL AUTO_INCREMENT,
    `statusDescription` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`statusid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `donationStatus` VALUES (0, 'Scheduled for pickup');
INSERT INTO `donationStatus` VALUES (0, 'Completed');
INSERT INTO `donationStatus` VALUES (0, 'Failed');
INSERT INTO `donationStatus` VALUES (0, 'Cancelled');

-- Donation table
DROP TABLE IF EXISTS `donation`;
CREATE TABLE `donation` (
	`donationid` int(255) NOT NULL AUTO_INCREMENT,
    `statusid` int(1) DEFAULT NULL,
    `useremail` varchar(255) DEFAULT NULL,
    `addressid` int(255) DEFAULT NULL,
    `refMethod` varchar(255) DEFAULT NULL,
    `pickupDate` datetime DEFAULT NULL,
    `size` varchar(255) DEFAULT NULL,
    `comments` varchar(1024) DEFAULT NULL,
    PRIMARY KEY (`donationid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
