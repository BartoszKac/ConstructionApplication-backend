package com.example.backend.service.util;


import com.example.backend.constants.Constants;
import com.example.backend.maper.Maper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PatternChecker {

    private final Constants constants;
    ObjectMapper objectMapper;

    public PatternChecker(Constants constants, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.constants = constants;
    }

    public JsonNode processPattern(JsonNode jsonNode) {

        ArrayNode arrayNode = objectMapper.createArrayNode();

        try {
            jsonNode.forEach(node -> {
                HashMap<String, Object> hashMap1 = new HashMap<>();
                Maper.flatten("", node, hashMap1);

                if (pattern(constants.getFilterByString(), hashMap1)) {

                    arrayNode.add(objectMapper.valueToTree(hashMap1));
                }
            });
        } catch (Exception e) {
            System.err.println("Błąd w filtrowaniu danych: " + e.getMessage());
        }
        return arrayNode;
    }

    private boolean pattern(String[] args, Map<String, Object> map) {

        Object titleObj = map.get("title");
        if (titleObj == null) return false;

        String title = titleObj.toString().toLowerCase();


        if (args == null || args.length == 0) return true;

        for (String s : args) {
            if (s == null || s.isEmpty()) continue;


            if (!title.contains(s.toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}
