package com.xianle.common.db.mybatis.plugin.table.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.xianle.common.db.mybatis.plugin.table.param.TimeRangeParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * 根据时间切分处理表名list
 * 
 * @author yunan.zheng
 * @since 02 十一月 2017
 */
@Slf4j
public class SplitTimeUtil {
	private static final String SHORT_PATTERN = "yyyy-MM";
    private static final String PATTERN = "yyyy-MM-dd";
    private static final String LONG_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String PLACEHOLDER = "-01 00:00:00";

    /**
     * 根据时间段 获取 TimeRangeParam 列表
     * 
     * @param startDate
     * @param endDate
     * @return List<TimeRangeParam>
     */
    public static List<TimeRangeParam> getMonthRangeTimeList(Date startDate, Date endDate) {
        Preconditions.checkNotNull(startDate);
        Preconditions.checkNotNull(endDate);
        Preconditions.checkState(startDate.compareTo(endDate) <= 0, "startDate 必须小于 endDate");
        List<TimeRangeParam> timeRangeParams = Lists.newArrayList();
        try {
            while (true) {
                Date curDate = DateUtils.parseDate(
                        DateFormatUtils.format(DateUtils.addMonths(startDate, 1), SHORT_PATTERN) + PLACEHOLDER,new String[]{LONG_PATTERN});
                if (curDate.compareTo(endDate) > 0) {
                    timeRangeParams.add(new TimeRangeParam(startDate, endDate));
                    break;
                }
                timeRangeParams.add(new TimeRangeParam(startDate, curDate));
                startDate = curDate;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return timeRangeParams;
    }

	public static void main(String[] args) {
		try {
			List<TimeRangeParam> monthRangeTimeList = SplitTimeUtil.getMonthRangeTimeList(DateUtils.parseDate("2017-12-13 00:40:50", new String[]{LONG_PATTERN}), DateUtils.parseDate("2017-12-13 00:40:50", new String[]{LONG_PATTERN}));
			monthRangeTimeList.forEach(e-> System.out.println(e));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
