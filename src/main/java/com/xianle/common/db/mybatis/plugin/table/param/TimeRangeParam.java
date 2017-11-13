package com.xianle.common.db.mybatis.plugin.table.param;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间区间
 *
 * @author yunan.zheng
 * @since 02 十一月 2017
 */
public class TimeRangeParam implements Serializable{

	private static final long serialVersionUID = -244047680112237120L;

	private Date startDate;

	private Date endDate;

	public TimeRangeParam(Date startDate,Date endDate){
		this.endDate=endDate;
		this.startDate=startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
