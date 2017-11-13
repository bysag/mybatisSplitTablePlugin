package com.xianle.com.xianle.common.db.mybatis.plugin.table;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 数据操作Mapper
 *
 * @author yunan.zheng
 * @since 10 十一月 2017
 */
public interface DatasMapper {

	List<Datas> lists(ParamModel params);

	int insert(Datas data);

	int updateValue(@Param("paramDatas") ParamModel params,@Param("newValue") String newValue);

	void deleteAll(ParamModel params);
}
