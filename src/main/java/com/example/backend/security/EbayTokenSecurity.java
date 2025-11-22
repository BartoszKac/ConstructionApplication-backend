package com.example.backend.security;

import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;



import org.springframework.http.ResponseEntity;
import java.util.function.Supplier;

public class EbayTokenSecurity<T extends ResponseEntity<?>> {
    private final Supplier<T> methods;

    public EbayTokenSecurity(Supplier<T> methods) {
        this.methods = methods;
    }

    public T execute() {
        try {
            T response = methods.get();
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Błąd HTTP: " + response.getStatusCode());
                return null;
            }
            return response;
        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wykonywania metody: " + e.getMessage());
            return null;
        }
    }
}
