package com.customer.auth.controller;

import com.customer.auth.model.User;
import com.customer.auth.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        System.out.println(user.getUsername());
        String token = authenticationService.registerUser(user.getUsername(), user.getPassword());
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.badRequest().body("UserName already exist!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = authenticationService.loginUser(user.getUsername(), user.getPassword());
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyToken(@RequestParam String token) {
        boolean isValid = authenticationService.verifyToken(token);
        return isValid ? ResponseEntity.ok("Token is valid") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }


}
