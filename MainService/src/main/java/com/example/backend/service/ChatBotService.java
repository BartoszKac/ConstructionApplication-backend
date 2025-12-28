package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatBotService {

    @Value("${OPENROUTER_API_KEY}")
    private String API_KEY;

    @Value("${OPENROUTER_URL}")
    private String URL ;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> getResponsetoAi(String userInput) {
        if (userInput == null || userInput.isEmpty()) {
        }
        System.out.println("Tutaj doszedlem do service pzoiom 1 " + userInput);

        try {
            String prompt = "To jest zapytanie użytkownika zwróć w formacie JSON (" + userInput + ")";
            HttpEntity<Map<String, Object>> request = createRequest(prompt);


            System.out.println("URL: " + URL);
            System.out.println("Metoda HTTP: POST");
            System.out.println("Nagłówki żądania: " + request.getHeaders());
            System.out.println("Ciało żądania: " + request.getBody());

            ResponseEntity<Map> response = restTemplate.exchange(URL, HttpMethod.POST, request, Map.class);

            System.out.println("Status odpowiedzi: " + response.getStatusCode());
            System.out.println("Ciało odpowiedzi: " + response.getBody());


            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseResponse(response.getBody());
            }
            System.out.println("Tutaj doszedlem do service pzoiom 3 " + userInput);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Error", "Nie udało się uzyskać odpowiedzi od modelu."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Error", "Wystąpił błąd podczas komunikacji z API.", "Details", e.getMessage()));
        }
    }

    private HttpEntity<Map<String, Object>> createRequest(String prompt) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", List.of(message));
        body.put("max_tokens", 150);
        body.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(API_KEY);

        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<?> parseResponse(Map<String, Object> responseBody) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
            if (messageMap != null) {
                String content = (String) messageMap.get("content");
                return ResponseEntity.ok(Map.of("Response", content));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("Error", "Nie udało się sparsować odpowiedzi od modelu."));
    }
}
