package com.example.backend.constants;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;

@Component
@RequestScope  // nowa instancja dla każdego requestu
public class ActualRequest {

    private final Map<String, Object> values = new HashMap<>();

    // Dodawanie wartości
    public void addValue(String key, Object value) {
        values.put(key, value);
    }

    // Pobieranie wartości
    public <T> T getValue(String key, Class<T> type) {
        Object o = values.get(key);
        if (o == null) return null;

        try {
            return type.cast(o);
        } catch (ClassCastException e) {
            System.out.println("Błąd: Wartość pod kluczem '" + key + "' nie jest typu " + type.getSimpleName());
            return null;
        }
    }
}
