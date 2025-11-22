package com.example.backend.service;

import com.example.backend.constants.COLOR;
import com.example.backend.constants.Constants;
import com.example.backend.maper.Maper;
import com.example.backend.model.paint.PaintMapper;
import com.example.backend.model.paint.PaintReturnFormat;
import com.example.backend.security.EbayTokenSecurity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiShopService {

    private final Constants constants;
    private final AccesService accesService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String apiToken;
    private String urlItem;

    public ApiShopService(Constants constants, AccesService accesService) {
        this.constants = constants;
        this.accesService = accesService;
    }

    public ResponseEntity<?> requestToApiShop(double meters, COLOR color) {
        if (meters < 0) {
            return ResponseEntity.badRequest().body("Nieprawidłowa wartość metrów");
        }

        if(this.apiToken == null){
            setAccessToken();
        }



        String searchQuery = "sexyl";

        String url = constants.getEbayBestCategoryUrl(searchQuery);

        try {
            HttpEntity<Void> request = createRequestHeaders();
            EbayTokenSecurity<ResponseEntity<String>> ebayTokenSecurity = new EbayTokenSecurity<>(
                    () -> restTemplate.exchange(url, HttpMethod.GET, request, String.class)
            );

            ResponseEntity<String> response = ebayTokenSecurity.execute();

            response = checkResponse(response,url);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            JsonNode items = jsonNode.path("itemSummaries");

            if (!items.isArray() || items.size() == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brak przedmiotów");
            }

            String itemId = items.get(0).path("itemId").asText();
            urlItem = constants.getURL_ITEM_DETAILS(itemId);

            JsonNode itemJson = getItemJson();
            if (itemJson == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie udało się pobrać szczegółów przedmiotu");
            }
            Map<String, Object> flatMap = new LinkedHashMap<>();
            long start = System.nanoTime();
          //  flatMap= Maper.flattenFor( itemJson);
            Maper.flatten("",jsonNode,flatMap);
            long end = System.nanoTime();
            System.out.println("Czas " +(end-start) + " ms");
            //PaintReturnFormat paintReturnFormat = PaintMapper.map(itemJson);
            Map<String,Object> wynik = Maper.map(flatMap, constants.getS());
            return ResponseEntity.ok(wynik);
           // return ResponseEntity.ok(paintReturnFormat);

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
}