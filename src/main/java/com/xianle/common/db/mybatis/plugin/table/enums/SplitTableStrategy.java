package com.xianle.common.db.mybatis.plugin.table.enums;

/**
 * 分表的策略枚举
 *
 * @author yunan.zheng
 * @since 27 十月 2017
 */
public enum SplitTableStrategy {
	MONTH("一个月一张表,格式:yyyy-MM"),DAY("一天一张表,格式:yyyy-MM-dd"),HOUR("一个小时一张表,格式:yyyy-MM-dd-HH");
	String desc;
	SplitTableStrategy(String desc){
		this.desc = desc;
	}
}
