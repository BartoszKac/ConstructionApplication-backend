package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login",
                                "/register",
                                "/allUsers",
                                "/aichat",
                                "/sendAreaSet")
                                    .permitAll() // te endpointy są publiczne
                                      .anyRequest().authenticated()                        // reszta wymaga JWT
                )
                .addFilterBefore(jwtAuthenticationFilter,
                      UsernamePasswordAuthenticationFilter.class)


        ;

        return http.build();
    }
}
/*
.oauth2Login(oauth2 -> oauth2
                       .defaultSuccessUrl("/me", true)) ;
 */