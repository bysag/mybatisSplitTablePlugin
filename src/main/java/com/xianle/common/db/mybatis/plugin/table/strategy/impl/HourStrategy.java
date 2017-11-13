package com.xianle.common.db.mybatis.plugin.table.strategy.impl;

import java.util.Date;

import com.xianle.common.db.mybatis.plugin.table.strategy.TableNameStrategy;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 按时策略来分表
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
public class HourStrategy implements TableNameStrategy<Date> {

	private String FORMAT="yyyy_MM_dd_HH";

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
