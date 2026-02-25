package com.example.backend;

import com.example.backend.constants.COLOR;
import com.example.backend.service.ApiShopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EbayApiDirectTest {

    @Autowired
    private ApiShopService apiShopService;

    @Test
    @DisplayName("Szybki test: Czy API eBay odpowiada i dane są mapowane")
    void shouldReturnDataFromEbay() {
        // Wywołujemy tylko moduł eBaya
        Map<String, Object> result = apiShopService.getEbayDataOnly(COLOR.WHITE);

        // Wyświetlamy w konsoli, żebyś widział co przyszło
        System.out.println(">>> WYNIK TESTU EBAY: " + result);

        // Asercje - sprawdzamy czy połączenie przeszło
        assertThat(result).as("Mapa wynikowa nie powinna być nullem").isNotNull();
        assertThat(result).as("API zwróciło błąd! Sprawdź AccesService/Token").doesNotContainKey("error");

        List<?> items = (List<?>) result.get("apiShop");
        assertThat(items).as("Lista produktów z eBay jest pusta!").isNotEmpty();

        System.out.println(">>> SUKCES: Otrzymano " + items.size() + " przedmiotów.");
    }
}