package com.example.backend.service;

import com.example.backend.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

@Service
public class WebScrapingService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final Constants constants;

    public WebScrapingService(Constants constants) {
        this.constants = constants;
    }



    public Map<String,Object> scrapeWebsite(String query) {
        try {
            String url = constants.getURL_WEBSRAPING_SERVICE();

            // Tworzymy nagłówki HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Tworzymy body żądania jako Map (można też JSON jako String)
            Map<String, String> body = Map.of("value", query);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            // Wysyłamy POST
            ResponseEntity<List> response = restTemplate.postForEntity(url, request, List.class);
            List<Map<String,Object>> products = response.getBody();
            return Map.of("products", products);


        } catch (Exception e) {
            e.printStackTrace();

        }
        return Map.of("error", "Failed to scrape website");
    }


//    public static void main(String[] args) {
//        WebScrapingService service = new WebScrapingService();
//        service.scrapeWebsite();
//    }
}
