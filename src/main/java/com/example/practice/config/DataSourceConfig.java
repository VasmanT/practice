package com.example.practice.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Bean
//    @Profile("dev")
//    public DataSource devDataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:postgresql://host.docker.internal:5436/postgres")
////                .url("jdbc:postgresql://localhost:5436/postgres")
//                .username("postgres")
//                .password("root")
//                .driverClassName("org.postgresql.Driver")
//                .build();
//    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://host.docker.internal:5436/postgres")
                .username("postgres")
                .password("root")
//                .url("jdbc:postgresql://production-db:5432/prod_db")
//                .username("prod_user")
//                .password(System.getenv("DB_PASSWORD"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }

//    @Bean
//    @Profile("local")
//    public DataSource localDataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:h2:mem:testdb")
//                .username("sa")
//                .password("password")
//                .driverClassName("org.h2.Driver")
//                .build();
//    }
}