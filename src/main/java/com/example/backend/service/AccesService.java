package com.example.backend.service;

import com.example.backend.constants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccesService {

    @Value("${BASE64_CLIENT_PASSWORD}")
    private String Base64ClientPassword;

    private Constants Constats;
    private RestTemplate restTemplate = new RestTemplate();

    public AccesService(Constants constats) {
        Constats = constats;
    }

    public String getAccessToken() {
        HttpEntity<String> httpEntity = createRequest();


        try {


            ResponseEntity<Map> response = restTemplate.exchange(
                    Constats.getURL_ACCESS_TOKEN(),
                    HttpMethod.POST,
                    httpEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {

                return (String) responseBody.get("access_token");
            } else {
                return "Błąd: body odpowiedzi jest puste";
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            return "Błąd HTTP: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString();
        } catch (Exception ex) {
            return "Inny błąd: " + ex.getMessage();
        }
    }


    private HttpEntity<String> createRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + Base64ClientPassword);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Tworzymy body w formacie x-www-form-urlencoded
        String body = "grant_type=client_credentials&scope=" + Constats.getScope();

        return new HttpEntity<>(body, headers);
    }



}
