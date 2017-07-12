package com.challenge.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.challenge.utlis.AppConfig;
import com.challenge.utlis.ApplicationProperties;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(new AppConfig().primaryDataSource())
		.usersByUsernameQuery("select username,password,enabled from users where username=?")
		.authoritiesByUsernameQuery("select username, authority from authorities where username=?");
		auth.inMemoryAuthentication()
		.withUser(new ApplicationProperties().getApplicationProperties("admin.username"))
		.password(new ApplicationProperties().getApplicationProperties("admin.password"))
		.authorities("ADMIN");
	} 
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/h2-console").hasAnyAuthority("ADMIN")
            .and()
            .authorizeRequests()
            .antMatchers("/challenge/**").hasAnyAuthority("USER")
            .anyRequest().authenticated()
            .and().httpBasic()
    		.authenticationEntryPoint(authEntryPoint);

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}