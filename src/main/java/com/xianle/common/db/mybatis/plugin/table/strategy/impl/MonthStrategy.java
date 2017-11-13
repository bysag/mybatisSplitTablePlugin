package com.xianle.common.db.mybatis.plugin.table.strategy.impl;

import java.util.Date;

import com.google.common.base.Preconditions;
import com.xianle.common.db.mybatis.plugin.table.strategy.TableNameStrategy;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 按月策略来分表
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
public class MonthStrategy implements TableNameStrategy<Date> {

	private String FORMAT="yyyy_MM";

	@Override
	public String getSuffix(Date date) {
		Preconditions.checkNotNull(date);
		return DateFormatUtils.format(date,FORMAT);
	}

	@Override
	public String getPrefix(Date date) {
		Preconditions.checkNotNull(date);
		return DateFormatUtils.format(date,FORMAT);
	}
}
