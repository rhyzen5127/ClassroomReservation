package org.catcom.classreserver.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource()
    {
        var builder = DataSourceBuilder.create();
        builder.driverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        builder.url("jdbc:mysql://remotemysql.com/selaYvh08m");
        builder.username("selaYvh08m");
        builder.password("1JTuietqDz");
        return builder.build();
    }
}
