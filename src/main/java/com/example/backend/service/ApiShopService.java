package com.example.backend.service;

import com.example.backend.constants.COLOR;
import com.example.backend.constants.Constants;
import com.example.backend.constants.ImportantQuest;
import com.example.backend.maper.Maper;
import com.example.backend.model.paint.PaintMapper;
import com.example.backend.model.paint.PaintReturnFormat;
import com.example.backend.security.EbayTokenSecurity;
import com.example.backend.service.util.JsonCalculatorService;
import com.example.backend.service.util.PatternChecker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ApiShopService {

    private final JsonCalculatorService jsonCalculatorService;
    private final WebScrapingService webScrapingService;
    private final Constants constants;
    private final AccesService accesService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiToken;
    private String urlItem;
    private PatternChecker patternChecker;

    public ApiShopService(JsonCalculatorService jsonCalculatorService, Constants constants, AccesService accesService, PatternChecker patternChecker, WebScrapingService webScrapingService) {
        this.jsonCalculatorService = jsonCalculatorService;
        this.webScrapingService = webScrapingService;
        this.constants = constants;
        this.accesService = accesService;
        this.patternChecker = patternChecker;
    }

    public ResponseEntity<?> requestToApiShop(double meters, COLOR color) {
        if (meters < 0) {
            return ResponseEntity.badRequest().body("Nieprawidłowa wartość metrów");
        }

        if (this.apiToken == null) {
            setAccessToken();
        }
        constants.AddFilter(color);


        try {
            Map<String, Object> wynik = formatJsonForResponse();

            Map<String, Object> wynik1 = webScrapingService.scrapeWebsite("farba");

            Map<String, Object> mainResult = new HashMap<>();

            mainResult.put("mapper", wynik.get("apiShop"));
            mainResult.put("scraping", wynik1.get("products"));
            Map<String, Object> main = jsonCalculatorService.calculate(mainResult);

    return ResponseEntity.ok(main);


        } catch (Exception e) {
            System.out.println("Błąd w requestToApiShop: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd serwera");
        }
    }

    private HttpEntity<Void> createRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }


    private void setAccessToken() {
        System.out.println("Pobieram nowy token...");
        this.apiToken = accesService.getAccessToken();
    }

    public ResponseEntity<String> checkResponse(ResponseEntity<String> response, String url) {
        if (response == null) {
            setAccessToken();
            HttpEntity<Void> requestRetry = createRequestHeaders();
            EbayTokenSecurity<ResponseEntity<String>> retrySecurity = new EbayTokenSecurity<>(
                    () -> restTemplate.exchange(url, HttpMethod.GET, requestRetry, String.class)
            );
            response = retrySecurity.execute();
            if (response == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błędne ID lub brak dostępu");
            }

        }
        return response;
    }

    private Map<String, Object> formatJsonForResponse() {
        String searchQuery = constants.getImportantQuest(ImportantQuest.INTERIOR_WALL_PAINT);
        int limit = 50;
        int offset = 0;

        List<Map<String, Object>> list = new ArrayList<>();

            for(int i =0;i<constants.getSize_Of_Page();i++) {
                String url = constants.getEbayBestCategoryUrl(searchQuery) + "&limit=" + limit + "&offset=" + offset;

                try {
                    HttpEntity<Void> request = createRequestHeaders();
                    EbayTokenSecurity<ResponseEntity<String>> ebayTokenSecurity = new EbayTokenSecurity<>(
                            () -> restTemplate.exchange(url, HttpMethod.GET, request, String.class)
                    );

                    ResponseEntity<String> response = ebayTokenSecurity.execute();
                    response = checkResponse(response, url);

                    JsonNode jsonNode = objectMapper.readTree(response.getBody());
                    JsonNode items = jsonNode.path("itemSummaries");
                    JsonNode itemsToPatern =  patternChecker.processPattern(items);

//                if (!items.isArray() || items.size() == 0) {
//                    break; // brak więcej przedmiotów
//                }

                    for (JsonNode itemJson : itemsToPatern) {
                        Map<String, Object> flatMap = new LinkedHashMap<>();
                        Maper.flatten("", itemJson, flatMap);
                        Map<String, Object> wynik = Maper.map(flatMap, constants.getItem_Field_Names());
                        list.add(wynik);
                    }
                    offset += limit;


                } catch (Exception e) {
                    e.printStackTrace();
                    return Map.of("error", "Nie udało się pobrać danych z eBay");
                }
            }

        return Map.of("apiShop", list);
    }



    @Deprecated
    private JsonNode getItemJson() {
        try {
            HttpEntity<Void> request = createRequestHeaders();
            ResponseEntity<String> response = restTemplate.exchange(urlItem, HttpMethod.GET, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Błąd pobierania szczegółów: " + response.getStatusCode());
                return null;
            }
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            System.out.println("Błąd getItemJson: " + e.getMessage());
            return null;
        }
    }

}