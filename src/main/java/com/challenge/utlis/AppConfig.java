package com.challenge.utlis;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	
	@Bean
    public DataSource primaryDataSource() {
		ApplicationProperties appProp = new ApplicationProperties();
        return DataSourceBuilder.create()
                .username(appProp.getApplicationProperties("spring.datasource.data-username"))
                .password("")
                .url(appProp.getApplicationProperties("spring.datasource.url"))
                .driverClassName(appProp.getApplicationProperties("spring.datasource.driver-class-name"))
                .build();
    }
}
