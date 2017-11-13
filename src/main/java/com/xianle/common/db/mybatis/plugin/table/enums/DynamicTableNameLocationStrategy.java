package com.xianle.common.db.mybatis.plugin.table.enums;

/**
 *
 * 动态表名部分的策略
 *
 * @author yunan.zheng
 * @since 27 十月 2017
 */
public enum DynamicTableNameLocationStrategy {
	PREFIX("前缀"),SUFFIX("后缀");
	String desc;
	DynamicTableNameLocationStrategy(String desc){
		this.desc = desc;
	}
}
