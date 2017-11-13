package com.xianle.common.db.mybatis.plugin.table.customize;

/**
 * 自定义表名接口
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
public interface CustomizeTableName {
	/**
	 * 获取表名
	 * @return
	 */
	<T> String getTableName(T model);
}
