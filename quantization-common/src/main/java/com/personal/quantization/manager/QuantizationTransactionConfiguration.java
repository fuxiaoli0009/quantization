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
 * 配置单数据源及事务
 */
@Configuration
@MapperScan(basePackages = "com.personal.quantization.mapper", sqlSessionFactoryRef = "quantizationSqlSessionFactory")
public class QuantizationTransactionConfiguration {
	
	/**
	 * 必须有一个主数据源Primary
	 */
	@Primary
	@Bean(name="quantizationDataSource")
	//@ConfigurationProperties(prefix="spring.datasource.quantization")
	@ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
    	return DataSourceBuilder.create().build();
    }
    
	/**
	 * 默认事务管理器
	 * @param dataSource
	 */
	@Primary 
    @Bean("quantizationTransactionManager")
    public DataSourceTransactionManager txManager(@Qualifier("quantizationDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
	@Primary
	@Bean("quantizationJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("quantizationDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	@Primary
    @Bean("quantizationSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("quantizationDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/Quantization*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean("quantizationSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("quantizationSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
	
}