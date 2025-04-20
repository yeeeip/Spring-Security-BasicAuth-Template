package com.nuzhd.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;


@Configuration
public class WebSecurityConfig {

    private final DataSource dataSource;

    @Value("${spring.security.remember-me.secret-key}")
    private String rememberMeKey;

    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            """                                   
            ROLE_ADMIN > PERMISSION_INDEX
            ROLE_ADMIN > PERMISSION_ROUTE1
            ROLE_ADMIN > PERMISSION_ROUTE2
            ROLE_USER > PERMISSION_INDEX
            ROLE_USER > PERMISSION_ROUTE2
            """
        );
    }

    @Bean
    public JdbcUserDetailsManager userDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(CsrfConfigurer::disable)
            .formLogin(conf -> conf.defaultSuccessUrl("/", true))
            .rememberMe(conf -> {
                var repository = new JdbcTokenRepositoryImpl();
                repository.setDataSource(dataSource);
                conf.tokenRepository(repository);
                conf.key(rememberMeKey);
            })
            .build();
    }

}
