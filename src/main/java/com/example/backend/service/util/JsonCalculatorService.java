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

    private final double LITRY_NA_METR = 12;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> calculate(Map<String, Object> itemMap) {
        Map<String, Object> resultMap = new HashMap<>();

        // Konwersja Map -> JsonNode
        JsonNode itemJson = objectMapper.valueToTree(itemMap);

        // Przetwarzanie "mapper" (każdy obiekt może zawierać listę sklepów)
        List<ObjectNode> apiShopList = new ArrayList<>();
        JsonNode jsonNodeMapper = itemJson.get("mapper");

        if (jsonNodeMapper != null && jsonNodeMapper.isArray()) {
            for (JsonNode shopNode : jsonNodeMapper) {
                if (shopNode.isObject()) {
                    ObjectNode objNode = (ObjectNode) shopNode;
                    JsonNode titleNode = objNode.get("title");
                    if (titleNode != null) {
                        String title = titleNode.asText();
                        Double liters = calculateTitleToDouble(title);
                        Double quantity = calcuteHowMany(
                                actualRequest.getValue("meters",Double.class)
                                ,LITRY_NA_METR,
                                liters);
                        Double prize = Double.parseDouble(objNode.get("price_value").asText());                        objNode.put("liters", liters);
                        objNode.put("m^2", calcutetom(liters));
                        objNode.put("ilosc",quantity);
                        objNode.put("ourCost",round(quantity,prize));
                    }
                    apiShopList.add(objNode);
                }
            }
        }

        // Przetwarzanie "scraping" (pojedyncza lista produktów)
        List<ObjectNode> productsList = new ArrayList<>();
        JsonNode jsonNodeScraping = itemJson.get("scraping");

        if (jsonNodeScraping != null && jsonNodeScraping.isArray()) {
            for (JsonNode productNode : jsonNodeScraping) {
                ObjectNode objNode = (ObjectNode) productNode;
                JsonNode titleNode = objNode.get("title");
                if (titleNode != null) {
                    String title = titleNode.asText();
                    Double liters = calculateTitleToDouble(title);
                    Double quantity = calcuteHowMany(
                            actualRequest.getValue("meters",Double.class)
                            ,LITRY_NA_METR,
                            liters);
                    Double prize = Double.parseDouble(objNode.get("price_value").asText());
                    objNode.put("liters", liters);
                    objNode.put("m^2", calcutetom(liters));
                    objNode.put("ilosc",quantity);
                    objNode.put("ourCost",round(quantity,prize));
                }
                productsList.add(objNode);
            }
        }

        // Dodanie list do mapy wynikowej
        resultMap.put("mapper", apiShopList);
        resultMap.put("scraping", productsList);

        return resultMap;
    }

    public boolean containsNumber(String title) {
        return title.matches(".*\\d.*");
    }

    private double extractNumber(String title) {
        // Wyciągnięcie pierwszej liczby z tytułu
        Matcher matcher = Pattern.compile("\\d+[.,]?\\d*").matcher(title);
        if (matcher.find()) {
            String number = matcher.group().replace(",", ".");
            return Double.parseDouble(number);
        }
        throw new IllegalArgumentException("Nie znaleziono liczby w tytule: " + title);
    }

    private String extractUnit(String title) {
        String t = title.toLowerCase();
        if (t.contains("ml")) return "ml";
        if (t.contains("gal")) return "gal";
        if (t.contains("l")) return "l";
        return null;
    }

    public Double calculateTitleToDouble(String title) {
        title = title.toLowerCase();
        if (!containsNumber(title)) {
            return null;
        }

        String unit = extractUnit(title);
        if (unit == null) {
            return null;
        }

        double value = extractNumber(title);

        switch (unit) {
            case "ml":
                return value / 1000.0;
            case "l":
                return value;
            case "gal":
                return value * 3.78541;
            default:
                return null;
        }
    }

    private String calcutetom(Double l){
        if (l == null){
            return null ;
        }
        Double m2 = l * LITRY_NA_METR ;
        return  m2.toString();
    }

    private double calcuteHowMany(double metres,double wydajnosc,double liters){
        double ilosc =  metres / ( liters * wydajnosc)  ;
        return ilosc;
    }
    private double round(double value, double prize) {
        return Math.ceil(value) * prize;
    }
}
