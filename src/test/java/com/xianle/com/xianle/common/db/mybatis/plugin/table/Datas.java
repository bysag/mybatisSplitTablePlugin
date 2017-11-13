package com.xianle.com.xianle.common.db.mybatis.plugin.table;

import java.io.Serializable;
import java.util.Date;

import com.xianle.common.db.mybatis.plugin.table.annotate.SplitTable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据Mode
 *
 * @author yunan.zheng
 * @since 10 十一月 2017
 */
@Data
@NoArgsConstructor
@SplitTable(fixedTableName="datas_",strategyFieldName={"createTime"})
public class Datas extends ParentModel implements Serializable  {

	private static final long serialVersionUID = -6959830940484058493L;

	private long id;

	private Date createTime;

	private String name;

	private String value;

}
