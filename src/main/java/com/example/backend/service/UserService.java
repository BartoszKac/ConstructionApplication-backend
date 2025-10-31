package com.example.backend.service;

import com.example.backend.model.LoginResponse;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserService(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.
                    badRequest().
                    body("Username is already taken");

        }

        if (userRepository.findByEmail((user.getEmail())).isPresent()) {
            return ResponseEntity.
                    badRequest().
                    body("Email is already taken");

        }

        String token = jwtService.generateToken(user.getUsername(),
                Map.of("userId", String.valueOf(user.getId())));

        // 4. zwróć token w body (lub w header)
        Map<String, Object> stringObjectMap = Map.of("token", token,
                "user", userRepository.save(user));
        return ResponseEntity.ok(stringObjectMap);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User userLogin(LoginResponse loginResponse) {
        return userRepository.findByUsername(loginResponse.getEmail())
                .filter(user -> user.getPassword().equals(loginResponse.getPassword()))
                .orElse(null);
    }

}





