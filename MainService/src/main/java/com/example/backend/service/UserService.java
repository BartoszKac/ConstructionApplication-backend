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
        // 1. Sprawdzenie czy login zajęty
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        // 2. Sprawdzenie czy email zajęty
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken");
        }

        // 3. NAJPIERW zapisz użytkownika do bazy, aby otrzymać jego ID
        User savedUser = userRepository.save(user);

        // 4. TERAZ wygeneruj token, używając ID z zapisanego obiektu
        String token = jwtService.generateToken(savedUser.getUsername(),
                Map.of("userId", String.valueOf(savedUser.getId())));

        // 5. Zwróć token i dane użytkownika
        Map<String, Object> response = Map.of(
                "token", token,
                "user", savedUser
        );

        return ResponseEntity.ok(response);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Zmień sygnaturę metody na taką:
    public ResponseEntity<Map<String, Object>> userLogin(LoginResponse loginResponse) {
        return userRepository.findByUsername(loginResponse.getEmail())
                .filter(user -> user.getPassword().equals(loginResponse.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getUsername(),
                            Map.of("userId", String.valueOf(user.getId())));

                    return ResponseEntity.ok(Map.of(
                            "token", token,
                            "user", user
                    ));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid username or password")));
    }
}





