package com.validator.web.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(
    basePackages = {
        "com.validator.web.mapper"
    },
    sqlSessionFactoryRef = "defaultSqlSessionFactory"
    // annotationClass = Mapper.class
)
public class DefaultSqlSessionConfig {
    
    // @Autowired
    // private ApplicationContext context;
    /**
     * 기본 데이터 소스
     * @return
     */
    @Primary
    @ConfigurationProperties("spring.datasource.default.hikari")
    @Bean(name = "defaultDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 기본 트랜젝션 매니저
     * @param dataSource
     * @return
     */
    @Primary
    @Bean(name = "defaultTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("defaultDataSource") final DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }

    @Primary
    @Bean(name = "defaultSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("defaultDataSource") DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis/mybatis-config.xml"));
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/mapper/*.xml"));

        return sqlSessionFactory.getObject();
    }

    // @Primary
    // @Bean("defaultSqlSessionTemplate")
    // public SqlSessionTemplate sqlSessionTemplate(@Qualifier("defaultSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
    //     SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    //     return sqlSessionTemplate;
    // }
}
