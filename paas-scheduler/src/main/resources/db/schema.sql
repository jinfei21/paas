CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(45) DEFAULT NULL,
  `app_name` varchar(45) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `cpu` double DEFAULT NULL,
  `memory` int(11) DEFAULT NULL,
  `ports` int(11) DEFAULT NULL,
  `disk` int(11) DEFAULT NULL,
  `owner` varchar(45) DEFAULT NULL,
  `depart` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `app_desc` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
CREATE TABLE `cleanup_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deploy_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `cmd` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `deploy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(45) DEFAULT NULL,
  `package_id` varchar(45) DEFAULT NULL,
  `package_uri` varchar(512) DEFAULT NULL,
  `instance_count` int(11) DEFAULT NULL,
  `tag` varchar(45) DEFAULT NULL,
  `health_path` varchar(45) DEFAULT NULL,
  `slb_path` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `cmd` varchar(512) DEFAULT NULL,
  `keep_alive` tinyint(4) DEFAULT NULL,
  `quartz` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `deploy_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deploy_id` int(11) DEFAULT NULL,
  `fail_count` int(11) DEFAULT NULL,
  `success_count` int(11) DEFAULT NULL,
  `fail_seqcount` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `deploy_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(45) DEFAULT NULL,
  `deploy_id` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `sandbox` varchar(45) DEFAULT NULL,
  `schedule_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `deploy_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deploy_id` int(11) DEFAULT NULL,
  `app_id` varchar(45) DEFAULT NULL,
  `host` varchar(45) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `instance_no` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `slave_id` varchar(45) DEFAULT NULL,
  `schedule_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

CREATE TABLE `rack` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rackid` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `slave` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `slaveid` varchar(45) DEFAULT NULL,
  `rackid` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `host` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `task_health` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `task_slb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;







