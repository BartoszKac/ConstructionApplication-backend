package com.example.backend;

import com.example.backend.service.WebScrapingService;
import com.example.backend.constants.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "URL_WEBSRAPING_SERVICE=http://localhost:5000/compute",
        "eureka.client.enabled=false"
})
public class FinalScrapingTest {

    @Autowired
    private WebScrapingService webScrapingService;

    @Test
    @DisplayName("Test dopasowany do MapperUrl: wysyłamy klucz 'farba'")
    void shouldScrapeUsingValidKey() {
        // MUSIMY wysłać "farba", bo tylko to masz w słowniku _URLS w Pythonie
        String validQuery = "farba";

        System.out.println(">>> [START] Test wysyła klucz: " + validQuery);

        // Wykonujemy żądanie
        Map<String, Object> result = webScrapingService.scrapeWebsite(validQuery);

        // Logujemy co przyszło
        System.out.println(">>> [ODPOWIEDŹ]: " + result);

        // Sprawdzamy czy nie ma błędu i czy są produkty
        assertThat(result).doesNotContainKey("error");
        assertThat(result.get("products")).isNotNull();

        List<?> products = (List<?>) result.get("products");
        System.out.println(">>> [SUKCES] Znaleziono produktów: " + products.size());
    }
}