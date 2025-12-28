package com.example.backend.service.util;

import com.example.backend.constants.ActualRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JsonCalculatorService {

    @Autowired
    private ActualRequest actualRequest;

    // Wydajność: ile m2 pomalujemy z 1 litra (wg Twojego kodu 12)
    private final double LITRY_NA_METR = 12.0;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> calculate(Map<String, Object> itemMap) {
        Map<String, Object> resultMap = new HashMap<>();

        // Konwersja wejściowej Mapy na drzewo JSON dla łatwiejszej nawigacji
        JsonNode itemJson = objectMapper.valueToTree(itemMap);

        // Pobieramy metraż z żądania (zabezpieczenie przed nullem)
        Double meters = actualRequest.getValue("meters", Double.class);
        double targetMeters = (meters != null) ? meters : 0.0;

        // Przetwarzamy obie listy (mapper i scraping) tą samą logiką
        resultMap.put("mapper", processList(itemJson.get("mapper"), targetMeters));
        resultMap.put("scraping", processList(itemJson.get("scraping"), targetMeters));

        return resultMap;
    }

    /**
     * Główna pętla przetwarzająca listę produktów
     */
    private List<ObjectNode> processList(JsonNode listNode, double targetMeters) {
        List<ObjectNode> processedList = new ArrayList<>();

        if (listNode != null && listNode.isArray()) {
            for (JsonNode node : listNode) {
                if (node.isObject()) {
                    processedList.add(enrichProductData((ObjectNode) node, targetMeters));
                }
            }
        }
        return processedList;
    }

    /**
     * Wzbogacanie pojedynczego obiektu o obliczenia
     */
    private ObjectNode enrichProductData(ObjectNode objNode, double targetMeters) {
        JsonNode titleNode = objNode.get("title");
        JsonNode priceNode = objNode.get("price_value");

        if (titleNode != null && priceNode != null) {
            String title = titleNode.asText();

            // Pobieramy litraż jako obiekt Double (może być null!)
            Double liters = calculateTitleToDouble(title);

            // Kluczowe zabezpieczenie przed: Cannot invoke "java.lang.Double.doubleValue()"
            if (liters != null && liters > 0) {
                try {
                    // Parsowanie ceny z obsługą przecinka
                    double price = Double.parseDouble(priceNode.asText().replace(",", "."));

                    // 1. Ile m2 pomalujemy jedną puszką
                    double coveragePerCan = liters * LITRY_NA_METR;

                    // 2. Ile puszek potrzebujemy (target / wydajność jednej puszki)
                    double cansNeeded = (coveragePerCan > 0) ? targetMeters / coveragePerCan : 0;

                    // 3. Koszt całkowity (zaokrąglamy puszki w górę * cena)
                    double totalCost = Math.ceil(cansNeeded) * price;

                    // Dodajemy wyniki do obiektu JSON
                    objNode.put("liters", liters);
                    objNode.put("coverage_m2", String.format("%.2f", coveragePerCan));
                    objNode.put("cans_needed", cansNeeded);
                    objNode.put("calculated_cost", totalCost);

                } catch (NumberFormatException e) {
                    objNode.put("error", "Błędny format ceny");
                }
            } else {
                objNode.put("info", "Nie wykryto litrażu w tytule");
            }
        }
        return objNode;
    }

    public Double calculateTitleToDouble(String title) {
        if (title == null) return null;
        String t = title.toLowerCase();

        if (!containsNumber(t)) return null;

        String unit = extractUnit(t);
        if (unit == null) return null;

        try {
            double value = extractNumber(t);
            switch (unit) {
                case "ml": return value / 1000.0;
                case "l":  return value;
                case "gal": return value * 3.78541;
                default: return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private boolean containsNumber(String title) {
        return title.matches(".*\\d.*");
    }

    private double extractNumber(String title) {
        Matcher matcher = Pattern.compile("\\d+[.,]?\\d*").matcher(title);
        if (matcher.find()) {
            String number = matcher.group().replace(",", ".");
            return Double.parseDouble(number);
        }
        return 0.0;
    }

    private String extractUnit(String title) {
        // Bardziej precyzyjne dopasowanie jednostek
        if (title.matches(".*\\d\\s?ml.*")) return "ml";
        if (title.contains("gal")) return "gal";
        // Szukamy "l" jako osobnego słowa lub na końcu liczby, aby uniknąć pomyłek
        if (title.matches(".*\\d\\s?l(\\s|$|[^a-z]).*") || title.contains("litr")) return "l";
        return null;
    }
}