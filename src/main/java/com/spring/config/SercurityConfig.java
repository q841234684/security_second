package com.spring.config;

import com.spring.Service.CustomDetailService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

/**
 * Created by mac on 2017/9/22.
 */
@Configuration
public class SercurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomDetailService customDetailService;
    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("login").permitAll()
                .antMatchers("admin").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/success")
                .permitAll()
                .and()
                .rememberMe()
                .tokenValiditySeconds(1200)
                .tokenRepository(tokenRepository())
                .key("security_key")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll();
    }


    public JdbcTokenRepositoryImpl tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}