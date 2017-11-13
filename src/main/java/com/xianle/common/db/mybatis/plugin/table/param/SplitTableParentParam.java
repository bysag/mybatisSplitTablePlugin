package com.xianle.common.db.mybatis.plugin.table.param;

/**
 * 分表父param
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
public class SplitTableParentParam {

	public String tableName;

	public String orderByClause;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}
}
