package com.nuzhd.controller;

import com.nuzhd.service.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableMethodSecurity
public class SecuredEndpointsController {

    private final AppUserService appUserService;

    public SecuredEndpointsController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PreAuthorize("hasAuthority('PERMISSION_INDEX')")
    @GetMapping("/")
    public ResponseEntity<String> basePath() {
        return ResponseEntity
            .ok("Welcome to the secured base path");
    }

    @PreAuthorize("hasAuthority('PERMISSION_ROUTE1')")
    @GetMapping("/route1")
    public ResponseEntity<String> route1() {
        return ResponseEntity
            .ok("Welcome to the secured route 1!");
    }

    @PreAuthorize("hasAuthority('PERMISSION_ROUTE2')")
    @GetMapping("/route2")
    public ResponseEntity<String> route2() {
        return ResponseEntity
            .ok("Welcome to the secured route 2!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<String> createUser() {
        appUserService.createUser("Joe");

        return ResponseEntity
            .ok("User with login Joe and default password created!");
    }

}
