package com.pthana.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

@Slf4j
@Configuration
public class DataSourceConfiguration  {

    public static final String DATE_FORMAT = "uuuuMMdd";

    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(DATE_FORMAT)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

    public static final String ZONED_DATETIME_FORMAT = "uuuuMMddHHmmssSSS[VV]";
    public static final DateTimeFormatter ZONED_DATETIME_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    /**
     * hikari cp config 명시
     * conf prop -> application.yml
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }


    @Primary
    @Bean //(name="TargetDataSource")
    public DataSource batchDataSource() throws Exception
    {
        HikariDataSource ds = new HikariDataSource( hikariConfig() );
        return new HikariDataSource(ds);
    }


    

}
