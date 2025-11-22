package com.example.backend.maper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Maper {
    public static Map<String, Object> map(JsonNode node, String[] stream) {
        Map<String, Object> data = new HashMap<>();

        if (node == null || stream == null) {
            throw new IllegalArgumentException("Input node or stream cannot be null");
        }

        for (String s : stream) {
            List<String> pathSegments = Arrays.asList(s.split("/"));
            System.out.println(pathSegments);
            JsonNode currentNode = node;

            for (String segment : pathSegments) {
                if (currentNode.isMissingNode()) {
                    data.put("missingNode", currentNode);
                    break;
                }

                currentNode = currentNode.path(segment);

                // Check if the current node is an array and add it to the map
               if (currentNode.isArray()) {
                    data.put(segment, currentNode);
                    break;
                }

                // Add the value to the map only for the last segment
                if (segment.equals(pathSegments.get(pathSegments.size() - 1))) {
                    data.put(segment, currentNode);
                }
            }
        }

        return data;
    }
}