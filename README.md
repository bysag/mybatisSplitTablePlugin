# mybatisSplitTablePlugin

开发者QQ:214994708

mybatis分表插件(此插件只负责定位到具体的表)
1.按照月/日/时 来分
2.自定义规则 来分


使用例子参考(test目录下)：
com.xianle.common.db.mybatis.plugin.table.SplitTableMybatisInterceptorTests

关键点：
mapper里面方法的参数必须有能定位到表的时间。
