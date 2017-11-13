package com.xianle.common.db.mybatis.plugin.table;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.xianle.common.db.mybatis.plugin.table.annotate.SplitTable;
import com.xianle.common.db.mybatis.plugin.table.customize.CustomizeTableName;
import com.xianle.common.db.mybatis.plugin.table.enums.DynamicTableNameLocationStrategy;
import com.xianle.common.db.mybatis.plugin.table.enums.SplitTableStrategy;
import com.xianle.common.db.mybatis.plugin.table.strategy.TableNameStrategy;
import com.xianle.common.db.mybatis.plugin.table.strategy.impl.DayStrategy;
import com.xianle.common.db.mybatis.plugin.table.strategy.impl.HourStrategy;
import com.xianle.common.db.mybatis.plugin.table.strategy.impl.MonthStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 分表上下文
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
@Slf4j
public class SplitTableContext {

    private static Map<SplitTableStrategy, TableNameStrategy<Date>> strategyMap = Maps.newHashMap();

    /**
     * 给表名赋值
     *
     * @param model
     * @param <T>
     */
    public <T> void setTableName(T model, SplitTable splitTable) {
        Preconditions.checkNotNull(splitTable);
        String tableName = getTableName(model, splitTable);
        Preconditions.checkState(StringUtils.isNotBlank(tableName), "表名不能为空!");
        setFieldValue(splitTable.tableNameFieldName(), model, tableName);
    }

    /**
     * 获取表名
     *
     * @param model
     * @param splitTable
     * @param <T>
     * @return
     */
    public <T> String getTableName(T model, SplitTable splitTable) {
        boolean customize = splitTable.customize();
        // 自定义表名
        if (customize) {
            return invokeCustomize(model, splitTable);
        }
        String fixedTableName = splitTable.fixedTableName();// 固定表名部分
        Preconditions.checkState(StringUtils.isNotBlank(fixedTableName), "fixedTableName 不能为空!");
        String dynamicTableName = "";
        DynamicTableNameLocationStrategy dynamicTableNameLocationStrategy = splitTable
                .dynamicTableNameLocationStrategy();// 前缀/后缀策略
        String[] fieldNames = splitTable.strategyFieldName();// 字段名，基于此字段来生成动态表名部分
        Preconditions.checkState(ArrayUtils.isNotEmpty(fieldNames), "strategyFieldName 不能为空!");
        Object param = getFieldValue(fieldNames, model);
        Preconditions.checkState(Objects.nonNull(param), "strategyFieldName 必须一个有值!");
        // 获取动态表名部分
        TableNameStrategy<Date> dateTableNameStrategy = strategyMap.get(splitTable.dynamicStrategy());
        Preconditions.checkNotNull(dateTableNameStrategy);
        dynamicTableName = getPrefixOrSuffix(dynamicTableNameLocationStrategy, param, dateTableNameStrategy);
        Preconditions.checkState(StringUtils.isNotBlank(dynamicTableName),
                "dynamicStrategy或DynamicTableNameLocationStrategy有误");
        return fixedTableName + dynamicTableName;
    }

    /**
     *
     * 自定义表名实现处理
     *
     * @param model
     * @param splitTable
     * @param <T>
     * @return
     */
    private <T> String invokeCustomize(T model, SplitTable splitTable) {
        String implClassName = splitTable.customizeImplClass();
        try {
            CustomizeTableName customizeTableName = (CustomizeTableName) Class.forName(implClassName).newInstance();
            Preconditions.checkNotNull(customizeTableName);
            String tableName = customizeTableName.getTableName(model);
            Preconditions.checkState(StringUtils.isNotBlank(tableName), "CustomizeTableName不能为空");
            log.info("CustomizeTableName is {}.", tableName);
            return tableName;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字段赋值
     *
     * @param fieldName
     * @param model
     * @param fieldValue
     * @param <T>
     */
    private <T> void setFieldValue(String fieldName, T model, String fieldValue) {
        boolean isDone = false;
        for (Class<?> clazz = model.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field declaredField = clazz.getDeclaredField(fieldName);
                if (Objects.nonNull(declaredField)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(model, fieldValue);
                        isDone = true;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            } catch (NoSuchFieldException e) {
               continue;
            }
        }

        if (!isDone){
            throw  new RuntimeException(" fieldName setValue 失败");
        }
    }

    /**
     * 获取字段值
     *
     * @param fieldNames
     * @param model
     * @param <T>
     * @return
     */
    private <T> Object getFieldValue(String[] fieldNames, T model) {
        for (String fieldName : fieldNames) {
            for (Class<?> clazz = model.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    Field declaredField = clazz.getDeclaredField(fieldName);
                    if (Objects.nonNull(declaredField)) {
                        declaredField.setAccessible(true);
                        try {
                            Object fieldValue = declaredField.get(model);
                            if (Objects.nonNull(fieldValue)) {
                                log.info("fieldName is {},fieldValue is {}.", fieldName, fieldValue);
                                return fieldValue;
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (NoSuchFieldException e) {
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * 获取前缀或后缀
     *
     * @param dynamicTableNameLocationStrategy
     * @param strategy
     * @return
     */
    private <T> String getPrefixOrSuffix(DynamicTableNameLocationStrategy dynamicTableNameLocationStrategy, T param,
            TableNameStrategy strategy) {
        String prePrefixOrSuffix = "";
        switch (dynamicTableNameLocationStrategy) {
        case PREFIX:
            prePrefixOrSuffix = strategy.getPrefix(param);
            break;
        case SUFFIX:
            prePrefixOrSuffix = strategy.getSuffix(param);
            break;
        default:
            break;
        }
        return prePrefixOrSuffix;
    }

    static {
        strategyMap.put(SplitTableStrategy.MONTH, new MonthStrategy());
        strategyMap.put(SplitTableStrategy.DAY, new DayStrategy());
        strategyMap.put(SplitTableStrategy.HOUR, new HourStrategy());
    }
}
