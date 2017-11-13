package com.xianle.common.db.mybatis.plugin.table.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.xianle.common.db.mybatis.plugin.table.enums.DynamicTableNameLocationStrategy;
import com.xianle.common.db.mybatis.plugin.table.enums.SplitTableStrategy;

/**
 * 需要分表的注解
 *
 * @author yunan.zheng
 * @since 27 十月 2017
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SplitTable {

	/**
	 * 是否自定义表名
	 * 如果是：需要实现CustomizeTableName接口
	 * @see com.xianle.common.db.mybatis.plugin.table.customize.CustomizeTableName
	 * @return
	 */
	boolean customize() default false;

	/**
	 * 自定义表名的实现类名
	 * @return
	 */
	String customizeImplClass() default "";

	/**
	 * 表名固定部分
	 * @return
	 */
	String fixedTableName();

	/**
	 * 生成动态表名部分的策略
	 * @return
	 */
	SplitTableStrategy dynamicStrategy() default SplitTableStrategy.MONTH;

	/**
	 * 动态表名位置策略(即后缀/前缀)
	 * @return
	 */
	DynamicTableNameLocationStrategy dynamicTableNameLocationStrategy() default DynamicTableNameLocationStrategy.PREFIX;

	/**
	 * 策略的字段名
	 * 基于此字段来生成动态表名部分，可以支持多个，找到有值的一个，如果多个都有值，那么以第一个为准。
	 * @return
	 */
	String[] strategyFieldName();

	/**
	 * 表名的字段名
	 * 给此字段设置表名值
	 * @return
	 */
    String tableNameFieldName() default "tableName";
}
