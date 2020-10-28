/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.Libreria2;

import com.libreria.Libreria2.Servicios.ClienteS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author emiliano
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class Config extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public ClienteS clienteS;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth
            .userDetailsService(clienteS)
            .passwordEncoder(new BCryptPasswordEncoder())
                ;
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/css/*", "/js/*", "/img/**")
                .permitAll()
            .and().formLogin()
                .loginPage("/login")
                    .loginProcessingUrl("/logincheck")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/inicio")
                    .failureUrl("/login?error=error")
                    .permitAll()
            .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            .and().csrf()
                .disable();
    }

    
}
