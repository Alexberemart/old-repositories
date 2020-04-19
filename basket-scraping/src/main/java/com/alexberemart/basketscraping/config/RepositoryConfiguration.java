package com.alexberemart.basketscraping.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class RepositoryConfiguration {

    @Bean
    @ConfigurationProperties(prefix="app.datasource")
    public DataSource dataSource() {
        return new BasicDataSource();
    }

}
