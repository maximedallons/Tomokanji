package com.tomokanji.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    @Value("${dbPropertiesPath}")
    private String dbPropertiesPath;

    private Properties dbProperties = new Properties();

    @PostConstruct
    private void loadDbProperties() throws IOException {
        dbProperties.load(Files.newBufferedReader(Paths.get(dbPropertiesPath)));
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbProperties.getProperty("datasource.driver-class-name"));
        dataSource.setUrl(dbProperties.getProperty("datasource.url"));
        dataSource.setUsername(dbProperties.getProperty("datasource.username"));
        dataSource.setPassword(dbProperties.getProperty("datasource.password"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
