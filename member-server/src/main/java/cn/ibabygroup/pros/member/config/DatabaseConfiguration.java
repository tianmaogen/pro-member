/**
 *
 */
package cn.ibabygroup.pros.member.config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;

/**
 * Spring Cloud Database Config
 *
 * @author taoping
 * @since 2016年8月10日
 */
@Configuration
@MapperScan(basePackages = "cn.ibabygroup.pros.member.dao.mysql", sqlSessionFactoryRef = "sqlSessionFactory")
public class DatabaseConfiguration {

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("cn.ibabygroup.pros.dvoice.biz.dao");
        //添加XML目录
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
//            bean.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
        return sqlSessionFactory.openSession();
    }

    @Bean
    public MapperHelper mapperHelper(SqlSession sqlSession) {
        MapperHelper mapperHelper = new MapperHelper();
        //特殊配置
        Config config = new Config();
        //具体支持的参数看后面的文档
//        config.setXXX(XXX);
        //设置配置
        mapperHelper.setConfig(config);
        // 注册自己项目中使用的通用Mapper接口，这里没有默认值，必须手动注册
        mapperHelper.registerMapper(Mapper.class);
        //配置完成后，执行下面的操作
        mapperHelper.processConfiguration(sqlSession.getConfiguration());
        return mapperHelper;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("cn.ibabygroup.pros.member.mapper");
        return configurer;
    }

}
