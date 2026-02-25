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
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            Map<String, String> body = Map.of("value", query);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            // LOG PRZED WYSŁANIEM
            System.out.println(">>> Wysyłam zapytanie do Scraping Service: " + url + " z body: " + body);

            ResponseEntity<List> response = restTemplate.postForEntity(url, request, List.class);
            List<Map<String,Object>> products = response.getBody();

            // LOG PO ODEBRANIU - To jest to, czego Ci brakowało!
            System.out.println(">>> Otrzymano produkty z Pythona: " + products);

            return Map.of("products", products != null ? products : List.of());

        } catch (Exception e) {
            System.err.println(">>> BŁĄD SCRAPINGU: " + e.getMessage());
            e.printStackTrace();
        }
        return Map.of("error", "Failed to scrape website");
    }


//    public static void main(String[] args) {
//        WebScrapingService service = new WebScrapingService();
//        service.scrapeWebsite();
//    }
}
