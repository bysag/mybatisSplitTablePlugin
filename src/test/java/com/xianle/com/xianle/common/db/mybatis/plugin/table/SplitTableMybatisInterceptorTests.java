package com.xianle.com.xianle.common.db.mybatis.plugin.table;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;

/**
 * mybatis分表插件的Test
 *
 * @author yunan.zheng
 * @since 10 十一月 2017
 */
public class SplitTableMybatisInterceptorTests {

    private static InputStream inputStream = null;
    private static SqlSessionFactoryBuilder builder = null;
    private static SqlSessionFactory factory = null;

    private Date currentDate=new Date();
    private String name="Jobs";

    @BeforeClass
    public static void init() {
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            builder = new SqlSessionFactoryBuilder();
            factory = builder.build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void destroy() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增数据
     * 定位到表，关键在于：
     * @see Datas.class 的createTime
     */
    @Test
    public void insertTest() {
        SqlSession sqlSession = factory.openSession();
        DatasMapper mapper = sqlSession.getMapper(DatasMapper.class);
        Datas datas = new Datas();
        datas.setName(name);
        datas.setCreateTime(currentDate);
        datas.setValue("Good");
        int result = mapper.insert(datas);
        sqlSession.commit();
        sqlSession.close();
        Assert.assertTrue(result>0);
    }

    /**
     * 获取数据
     * 定位到表，关键在于：
     * @see ParamModel.class 的createTime
     */
    @Test
    public void getTest() {
        SqlSession sqlSession = factory.openSession();
        DatasMapper mapper = sqlSession.getMapper(DatasMapper.class);
        ParamModel paramData = new ParamModel();
        paramData.setName(name);
        paramData.setCreateTime(currentDate);
        List<Datas> results = mapper.lists(paramData);
        sqlSession.commit();
        sqlSession.close();
        Assert.assertTrue(results.size()>0);
    }

    /**
     * 修改数据
     * 定位到表，关键在于：
     * @see ParamModel.class 的createTime
     */
    @Test
    public void updateTest() {
        SqlSession sqlSession = factory.openSession();
        DatasMapper mapper = sqlSession.getMapper(DatasMapper.class);
        ParamModel paramData = new ParamModel();
        paramData.setCreateTime(currentDate);
        paramData.setName(name);
        int i = mapper.updateValue(paramData, "Double-GOOD");
        List<Datas> lists = mapper.lists(paramData);
        lists.forEach(e->{
            Assert.assertTrue(e.getValue().equalsIgnoreCase("Double-GOOD"));
        });
        sqlSession.commit();
        sqlSession.close();
        Assert.assertTrue(i>0);
    }
}
