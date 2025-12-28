package com.example.backend.maper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class Maper {


    public static Map<String, Object> map(Map<String, Object> node, String[] stream) {
        if (node == null || stream == null) {
            throw new IllegalArgumentException("Input node or stream cannot be null");
        }

        Map<String, Object> returnValue = new HashMap<>();

        for (String s : stream) {
            if(s.contains("&")){
                String key = s.replace("&", "");
                System.out.println(key);
                node.entrySet().stream().
                        filter(e -> e.getValue().equals(key))
                        .forEach(e -> returnValue.put(e.getKey(),e.getValue()));
            }else {
                String key = s.replace("/", "_");

                node.entrySet().stream()
                        .filter(e -> e.getKey().equals(key))
                        .forEach(e -> returnValue.put(e.getKey(), e.getValue()));
            }

        }

        return returnValue;
    }
    public static Map<String, Object> flattenFor(JsonNode root) {
        Map<String, Object> out = new HashMap<>();
        Deque<Map.Entry<String, JsonNode>> stack = new ArrayDeque<>();
        stack.push(new AbstractMap.SimpleEntry<>("", root));

        while (!stack.isEmpty()) {
            Map.Entry<String, JsonNode> current = stack.pop();
            String prefix = current.getKey();
            JsonNode node = current.getValue();

            if (node.isObject()) {
                StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), Spliterator.ORDERED), false)
                        .forEach(entry -> stack.push(new AbstractMap.SimpleEntry<>(prefix + entry.getKey() + "_", entry.getValue())));
            } else if (node.isArray()) {
                IntStream.range(0, node.size())
                        .forEach(i -> stack.push(new AbstractMap.SimpleEntry<>(prefix + i + "_", node.get(i))));
            } else {
                out.put(prefix.endsWith("_") ? prefix.substring(0, prefix.length() - 1) : prefix, node.asText());
            }
        }

        return out;
    }

    public static void flatten(String prefix, JsonNode node, Map<String, Object> out) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                flatten(prefix + entry.getKey() + "_", entry.getValue(), out);
            }
        } else if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
                flatten(prefix + i + "_", node.get(i), out);
            }
        } else {
            out.put(prefix.substring(0, prefix.length() - 1), node.asText());
        }
    }


    @Deprecated
    public static Map<String, Object> mapOld(JsonNode node, String[] stream) {
        ObjectMapper mapper = new ObjectMapper(); // obiekt Jacksona
        Map<String, Object> data = new HashMap<>();

        if (node == null || stream == null) {
            throw new IllegalArgumentException("Input node or stream cannot be null");
        }

        for (String s : stream) {
            List<String> pathSegments = Arrays.asList(s.split("/"));
            JsonNode currentNode = node;

            for (int i = 0; i < pathSegments.size(); i++) {
                String segment = pathSegments.get(i);
                boolean isLast = (i == pathSegments.size() - 1);

                // Jeżeli segment zawiera dwukropek, traktujemy go specjalnie
                if (segment.contains(":") && currentNode.isArray()) {
                    // Rozdziel segment na klucz i oczekiwaną wartość
                    List<String> pathInsideSegment = Arrays.asList(segment.split(":", 2)); // limit 2, żeby nie dzielić przypadkowo po dodatkowych dwukropkach
                    String key = pathInsideSegment.get(0);
                    String expectedValue = pathInsideSegment.get(1);

                    try {
                        // Parsujemy aktualny węzeł jako listę map
                        List<Map<String, Object>> list = mapper.readValue(
                                currentNode.toString(),
                                new TypeReference<List<Map<String, Object>>>() {}
                        );

                        // Filtrujemy listę: wybieramy tylko te mapy, gdzie wartość pod kluczem `key` równa się `expectedValue`
                        List<Map<String, Object>> filteredList = list.stream()
                                .filter(map -> expectedValue.equals(String.valueOf(map.get(key))))
                                .toList();

                        Map<String, Object>   externel   = filteredList.getLast() ;
                        Object retur = externel.get(key);
                        // Dodajemy przefiltrowaną listę do wyniku
                        data.put(segment, retur);

                        if (isLast) break;
                        currentNode = currentNode.path(segment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    continue; // przejdź do następnego segmentu
                }


                // Normalna ścieżka bez dwukropka
                currentNode = currentNode.path(segment);

                if (currentNode.isMissingNode()) {
                    data.put("missingNode", currentNode);
                    break;
                }

                // Jeśli tablica na ostatnim segmencie
                if (currentNode.isArray() && isLast) {
                    data.put(segment, currentNode);
                    break;
                }

                // Dodajemy wartość do mapy tylko dla ostatniego segmentu
                if (isLast) {
                    data.put(segment, currentNode);
                }
            }
        }

        return data;
    }
}

