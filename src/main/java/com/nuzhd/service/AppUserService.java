package com.nuzhd.service;

import java.util.List;
import java.util.Random;

import com.nuzhd.model.AppUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;


@Service
public class AppUserService {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private static final String DEFAULT_PASSWORD = "password";
    private final Random rand = new Random();

    public AppUserService(JdbcUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username) {
        var id = rand.nextLong();
        String password = passwordEncoder.encode(DEFAULT_PASSWORD);

        AppUser appUser = new AppUser(
            id,
            username,
            password,
            List.of(new SimpleGrantedAuthority("PERMISSION_ROUTE1"))
        );

        userDetailsManager.createUser(appUser);
    }

}
