package org.catcom.classreserver.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource()
    {
        /*
        Remote DB
        var builder = DataSourceBuilder.create();
        builder.driverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        builder.url("jdbc:mysql://remotemysql.com/selaYvh08m");
        builder.username("selaYvh08m");
        builder.password("1JTuietqDz");
        return builder.build();

         */
        var builder = DataSourceBuilder.create();
        builder.driverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        builder.url("jdbc:mysql://localhost:3306/classreserver");
        builder.username("root");
        builder.password("493463");
        return builder.build();
    }

    @Bean
    public Dialect dialect()
    {
        return new MySQLDialect();
    }
}
