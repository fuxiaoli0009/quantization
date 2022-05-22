package com.personal.quantization.manager;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 配置多数据源、多事务、
 *
 */
@Configuration
@MapperScan(basePackages = "com.personal.quantization.mapperauth", sqlSessionFactoryRef = "authSqlSessionFactory")
public class AuthTransactionConfiguration {
	
	@Bean(name="authDataSource")
	@ConfigurationProperties(prefix="spring.datasource.auth")
    public DataSource dataSource() {
    	return DataSourceBuilder.create().build();
    }
    
    @Bean("authTransactionManager")
    public DataSourceTransactionManager txManager(@Qualifier("authDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean("authJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("quantizationDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean("authSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("authDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/AuthTestMapper.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean("authSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("authSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}