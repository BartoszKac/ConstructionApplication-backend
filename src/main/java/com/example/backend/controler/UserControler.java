package com.example.backend.controler;


import com.example.backend.model.LoginResponse;
import com.example.backend.model.User;
import com.example.backend.security.JwtService;
import com.example.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserControler {

    public final UserService userService;
    private final JwtService jwtService;

    public UserControler(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/login")
    public User createUser(@RequestBody LoginResponse loginResponse) {

        return userService.userLogin(loginResponse);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        return userService.saveUser(user);
    }



}
