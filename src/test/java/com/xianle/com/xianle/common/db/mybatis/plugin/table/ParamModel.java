package com.xianle.com.xianle.common.db.mybatis.plugin.table;

import java.io.Serializable;
import java.util.Date;

import com.xianle.common.db.mybatis.plugin.table.annotate.SplitTable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询的参数Mode
 *
 * @author yunan.zheng
 * @since 10 十一月 2017
 */
@Data
@NoArgsConstructor
@SplitTable(fixedTableName="datas_",strategyFieldName={"createTime"})
public class ParamModel extends ParentModel implements Serializable  {

	private static final long serialVersionUID = -7923647191052079343L;

	private Date createTime;

	private String name;

}
