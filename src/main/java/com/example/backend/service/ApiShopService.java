package com.example.backend.service;

import com.example.backend.constants.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiShopService {

    Constants constants;
    public ApiShopService(Constants constants) {
        this.constants = constants;

    }
    @Value("${API_EBAY_KEY}")
    private String API_TOKEN;

    private String URL;

    private String URL_ITEM;

    ObjectMapper objectMapper = new ObjectMapper();

    private RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> RequestToApiShop(double meters) {
        if(meters < 0){
            return  null;
        }
        URL = constants.getEbayBestCategoryUrl("acrylic paint white 1L ");
        try {

            HttpEntity<Map<String,Object>> request = createRequest();

            ResponseEntity<String> response = restTemplate.exchange(URL,HttpMethod.GET,request,String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String jsonNodItemId = jsonNode.get("itemSummaries").
                    get(0).
                        get("itemId").asText();

            URL_ITEM = constants.getURL_ITEM_DETAILS(jsonNodItemId);

            JsonNode itemJson = GetItemJson();

            return ResponseEntity.ok(itemJson);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private HttpEntity<Map<String,Object>> createRequest(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tutaj ustawiasz Bearer token, nie Basic!
        headers.set("Authorization", "Bearer " + API_TOKEN);
        headers.set("Accept", "application/json");

        return new HttpEntity<>(headers);
    }
    private JsonNode GetItemJson(){

        try {
            HttpEntity<Map<String,Object>> request = createRequest();

            ResponseEntity<String> response = restTemplate.exchange(URL_ITEM,HttpMethod.GET,request,String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            return jsonNode;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

}
