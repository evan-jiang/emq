CREATE TABLE `msg` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(32) DEFAULT NULL COMMENT '标题',
  `url` VARCHAR(128) DEFAULT NULL COMMENT '执行url',
  `params` VARCHAR(512) DEFAULT NULL COMMENT '执行参数',
  `thread_no` INT(11) DEFAULT 0 COMMENT '线程编号',
  `match_value` VARCHAR(32) DEFAULT NULL COMMENT '执行结果匹配值',
  `plan_times` INT(11) DEFAULT NULL COMMENT '允许执行次数',
  `yet_times` INT(11) DEFAULT NULL COMMENT '已经执行次数',
  `interval` BIGINT(20) DEFAULT NULL COMMENT '重复执行的时间间隔(毫秒)',
  `next_time` BIGINT(20) DEFAULT NULL COMMENT '下次执行时间(毫秒)',
  PRIMARY KEY (`id`),
  KEY `idx_thread_no` (`thread_no`),
  KEY `idx_next_time` (`next_time`)
) ENGINE=INNODB AUTO_INCREMENT=1601 DEFAULT CHARSET=utf8;