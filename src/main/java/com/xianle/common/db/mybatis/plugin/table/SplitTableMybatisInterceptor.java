package com.xianle.common.db.mybatis.plugin.table;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.google.common.base.Preconditions;
import com.xianle.common.db.mybatis.plugin.table.annotate.SplitTable;

import lombok.extern.slf4j.Slf4j;


/**
 * mybatis分表的拦截器
 *
 * @author yunan.zheng
 * @since 30 十月 2017
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(method = "query", type = Executor.class, args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class , CacheKey.class, BoundSql.class}),
        @Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class })})
@Slf4j
public class SplitTableMybatisInterceptor implements Interceptor {

    private String SPLIT_TABLE_CONTEXT_BEAN_NAME = "splitTableContext";

    private SplitTableContext splitTableContext;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (ArrayUtils.isNotEmpty(invocation.getArgs()) && invocation.getArgs().length >= 2
                && Objects.nonNull(invocation.getArgs()[1])) {
            Object param = invocation.getArgs()[1];
            /**
             *  当mapper方法的参数是多个的话，处理其中一个参数带有SplitTable注解的对象
             */
            if(param instanceof org.apache.ibatis.binding.MapperMethod.ParamMap){
                final AtomicBoolean hasDone = new AtomicBoolean();
                ((MapperMethod.ParamMap) param).forEach((key,value)->{
                    if(hasDone.get()==false&&splitTableHandler(value)){
                        hasDone.set(true);
                        return;
                    }
                });
            }else {
                splitTableHandler(param);
            }
        }
        return invocation.proceed();
    }

    /**
     * 分表处理
     * @param param
     * @return
     */
    private boolean splitTableHandler(Object param){
        SplitTable splitTable = param.getClass().getAnnotation(SplitTable.class);
        if (Objects.nonNull(splitTable)) {
            Preconditions.checkNotNull(splitTableContext);
            // 分表处理
            splitTableContext.setTableName(param, splitTable);
            log.info("需要分表处理,参数对象为{}", param);
            return true;
        }
        return false;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String splitTableContextBeanName = (String) properties.get(SPLIT_TABLE_CONTEXT_BEAN_NAME);
        try {
            splitTableContext = (SplitTableContext) Class.forName(splitTableContextBeanName).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
