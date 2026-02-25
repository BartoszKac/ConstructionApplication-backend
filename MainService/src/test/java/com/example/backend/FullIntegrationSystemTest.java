package com.example.backend;

import com.example.backend.constants.COLOR;
import com.example.backend.service.ApiShopService;
import com.example.backend.service.WebScrapingService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "URL_WEBSRAPING_SERVICE=http://localhost:5000/compute",
        "eureka.client.enabled=false"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FullIntegrationSystemTest {

    @Autowired
    private ApiShopService apiShopService;

    @Autowired
    private WebScrapingService webScrapingService;


    @BeforeEach
    void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @Order(1)
    @DisplayName("1. Sprawdzenie połączenia z API eBay")
    void shouldVerifyEbayConnection() {
        Map<String, Object> ebayResult = apiShopService.getEbayDataOnly(COLOR.WHITE);
        assertThat(ebayResult).isNotNull().doesNotContainKey("error");
    }

    @Test
    @Order(2)
    @DisplayName("2. Sprawdzenie połączenia z Scraping Service (Python)")
    void shouldVerifyScrapingConnection() {
        Map<String, Object> scrapingResult = webScrapingService.scrapeWebsite("farba");
        assertThat(scrapingResult).isNotNull().doesNotContainKey("error");
        assertThat((List<?>) scrapingResult.get("products")).isNotEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("3. Pełny proces: eBay + Scraping + Kalkulator")
    void shouldVerifyFullProcessResponse() {
        // Teraz to zadziała, bo RequestContext jest ustawiony w @BeforeEach
        var response = apiShopService.requestToApiShop(10.0, COLOR.WHITE);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();

        System.out.println(">>> [SUCCESS] Pełna odpowiedź systemu: " + response.getBody());
    }
}

