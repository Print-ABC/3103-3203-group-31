-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 13, 2018 at 10:16 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ncshare`
--

-- --------------------------------------------------------

--
-- Table structure for table `card_owned`
--

CREATE TABLE `card_owned` (
  `co_user_id_owner` binary(16) NOT NULL,
  `co_user_id_owned` binary(16) NOT NULL,
  `co_username` varchar(50) NOT NULL,
  `co_date_added` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `card_owned`
--

INSERT INTO `card_owned` (`co_user_id_owner`, `co_user_id_owned`, `co_username`, `co_date_added`) VALUES
(0x78982067c8c211e8a31a3c970ea38bf7, 0xd03b5549c8ca11e8a31a3c970ea38bf7, 'xjustus', '2018-10-06 02:59:43'),
(0xd03b5549c8ca11e8a31a3c970ea38bf7, 0x78982067c8c211e8a31a3c970ea38bf7, 'barackObama', '2018-10-06 02:59:43');

-- --------------------------------------------------------

--
-- Table structure for table `organization_card`
--

CREATE TABLE `organization_card` (
  `org_user_id` binary(16) NOT NULL,
  `org_card_name` varchar(50) NOT NULL,
  `org_card_organization` varchar(100) NOT NULL,
  `org_card_job_title` varchar(50) NOT NULL,
  `org_card_contact` int(11) NOT NULL,
  `org_card_email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `organization_card`
--

INSERT INTO `organization_card` (`org_user_id`, `org_card_name`, `org_card_organization`, `org_card_job_title`, `org_card_contact`, `org_card_email`) VALUES
(0x78982067c8c211e8a31a3c970ea38bf7, 'Justus Chua', 'Legit Company Pte Ltd.', 'CEO', 98765432, 'justus@legitcompany.com');

-- --------------------------------------------------------

--
-- Table structure for table `relationship`
--

CREATE TABLE `relationship` (
  `friend_one_id` int(11) NOT NULL,
  `friend_two_id` int(11) NOT NULL,
  `friend_status` tinyint(4) NOT NULL,
  `friend_action_user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `relationship`
--

INSERT INTO `relationship` (`friend_one_id`, `friend_two_id`, `friend_status`, `friend_action_user_id`) VALUES
(6, 8, 1, 8);

-- --------------------------------------------------------

--
-- Table structure for table `student_card`
--

CREATE TABLE `student_card` (
  `stu_user_id` binary(16) NOT NULL,
  `stu_card_name` varchar(50) NOT NULL,
  `stu_card_email` varchar(50) NOT NULL,
  `stu_card_contact` int(11) NOT NULL,
  `stu_card_course` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student_card`
--

INSERT INTO `student_card` (`stu_user_id`, `stu_card_name`, `stu_card_email`, `stu_card_contact`, `stu_card_course`) VALUES
(0xd03b5549c8ca11e8a31a3c970ea38bf7, 'Barack Obama', 'barackobama@usa.com', 87654321, 'Bachelors in Government');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_username` varchar(50) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(256) NOT NULL,
  `user_salt` varchar(256) NOT NULL,
  `user_contact` varchar(50) NOT NULL,
  `user_role` int(11) NOT NULL,
  `user_friend_id` int(11) NOT NULL,
  `user_id` binary(16) NOT NULL,
  `user_email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_username`, `user_name`, `user_password`, `user_salt`, `user_contact`, `user_role`, `user_friend_id`, `user_id`, `user_email`) VALUES
('xjustus', 'Justus Chua', '$2y$10$kNmphAu22Lw7VikxbPphSuXmPbmZHllHauQJRUV8Nr0Y1JXGHZGIO', '21072faead37d83443a0f9b022a3224fc5aa8360548720b1161ec95086fe8213', '98765432', 1, 6, 0x78982067c8c211e8a31a3c970ea38bf7, 'justus@ncshare.co'),
('barackObama', 'Barack Obama', '$2y$10$Lg9UAPhpFkiz7Ll96wIpMuhjF8crH7o.xW5uK/Xkr6YNs0saudimC', '91720ea2e3b17833ad8d68be89aa58114003be5d67ea91d806e291baa80cd3ec', '87654332', 0, 8, 0xd03b5549c8ca11e8a31a3c970ea38bf7, 'barackobama@usa.com');

--
-- Triggers `user`
--
DELIMITER $$
CREATE TRIGGER `before_insert_user` BEFORE INSERT ON `user` FOR EACH ROW SET new.user_id = unhex(replace(uuid(),'-',''))
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `card_owned`
--
ALTER TABLE `card_owned`
  ADD UNIQUE KEY `co_user_id` (`co_user_id_owner`,`co_user_id_owned`),
  ADD KEY `co_user_id_two` (`co_user_id_owned`);

--
-- Indexes for table `organization_card`
--
ALTER TABLE `organization_card`
  ADD PRIMARY KEY (`org_user_id`);

--
-- Indexes for table `relationship`
--
ALTER TABLE `relationship`
  ADD UNIQUE KEY `friend_one_id` (`friend_one_id`,`friend_two_id`);

--
-- Indexes for table `student_card`
--
ALTER TABLE `student_card`
  ADD PRIMARY KEY (`stu_user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_friend_id` (`user_friend_id`),
  ADD UNIQUE KEY `user_username` (`user_username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_friend_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `card_owned`
--
ALTER TABLE `card_owned`
  ADD CONSTRAINT `card_owned_ibfk_1` FOREIGN KEY (`co_user_id_owner`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `card_owned_ibfk_2` FOREIGN KEY (`co_user_id_owned`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `organization_card`
--
ALTER TABLE `organization_card`
  ADD CONSTRAINT `organization_card_ibfk_1` FOREIGN KEY (`org_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `relationship`
--
ALTER TABLE `relationship`
  ADD CONSTRAINT `relationship_ibfk_1` FOREIGN KEY (`friend_one_id`) REFERENCES `user` (`user_friend_id`) ON DELETE CASCADE;

--
-- Constraints for table `student_card`
--
ALTER TABLE `student_card`
  ADD CONSTRAINT `student_card_ibfk_1` FOREIGN KEY (`stu_user_id`) REFERENCES `user` (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
