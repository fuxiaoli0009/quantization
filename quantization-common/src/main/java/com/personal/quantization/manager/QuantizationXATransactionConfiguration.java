package com.personal.quantization.manager;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;

/**
 * 配置多数据源事务
 */
//@Configuration
//@MapperScan(basePackages = "com.personal.quantization.mapper", sqlSessionFactoryRef = "quantizationSqlSessionFactory")
public class QuantizationXATransactionConfiguration {
	
	@Bean(name = "quantizationDataSource")
	public DataSource quantizationDataSource2(QuantizationDBConfig dbConfig) {
		AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
		atomikosDataSourceBean.setUniqueResourceName("quantizationDataSource");
		atomikosDataSourceBean.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
		Properties properties = new Properties();
		properties.put("URL", dbConfig.getJdbcUrl());
		properties.put("user", dbConfig.getUsername());
		properties.put("password", dbConfig.getPassword());
		atomikosDataSourceBean.setXaProperties(properties);
		return atomikosDataSourceBean;
	}
	
	/**
	 * Atomikos数据源
	 * @param dbConfig
	 */
	//@Bean(name = "quantizationDataSource")
    public DataSource testDataSource(QuantizationDBConfig dbConfig) throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(dbConfig.getJdbcUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(dbConfig.getPassword());
        mysqlXaDataSource.setUser(dbConfig.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
 
        // 将本地事务注册到创 Atomikos全局事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("quantizationDataSource");
        xaDataSource.setMinPoolSize(dbConfig.getMinPoolSize());
        xaDataSource.setMaxPoolSize(dbConfig.getMaxPoolSize());
        xaDataSource.setMaxLifetime(dbConfig.getMaxLifetime());
        xaDataSource.setBorrowConnectionTimeout(dbConfig.getBorrowConnectionTimeout());
        xaDataSource.setLoginTimeout(dbConfig.getLoginTimeout());
        xaDataSource.setMaintenanceInterval(dbConfig.getMaintenanceInterval());
        return xaDataSource;
    }
	
	/**
	 * 不需要默认事务管理器
	 * @param dataSource
	 */
	//@Primary 
    //@Bean("quantizationTransactionManager")
    public DataSourceTransactionManager txManager(@Qualifier("quantizationDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
	@Bean("quantizationJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("quantizationDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean("quantizationSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("quantizationDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // mapper的xml形式文件位置必须要配置，不然将报错：no statement （这种错误也可能是mapper的xml中，namespace与项目的路径不一致导致）
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/Quantization*.xml"));
        return bean.getObject();
    }

    @Bean("quantizationSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("quantizationSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
	
}