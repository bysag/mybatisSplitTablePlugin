package com.xianle.common.db.mybatis.plugin.table.strategy;

/**
 * 表名后缀/前缀策略
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
public interface TableNameStrategy<T> {
	/**
	 * 获取后缀
	 * @return
	 */
	String getSuffix(T t);

	/**
	 * 获取前缀
	 * @return
	 */
	String getPrefix(T t);
}
