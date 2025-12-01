package com.example.backend.service.util;


import com.example.backend.maper.Maper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PatternChecker  {

    public HashMap<String,Object> processPattern(JsonNode jsonNode, String[] args){
        HashMap<String,Object> hashMap = new HashMap<>();

            for(int i = 0;i< jsonNode.size();i++){
                JsonNode jsonNode1 = jsonNode.get(i);
            }
        HashMap<String,Object> hashMap1 = new HashMap<>();;
            jsonNode.forEach(
                    node -> {

                        Maper.flatten("",node,hashMap1);
                    });

        System.out.println(jsonNode.size());



        return hashMap;
    }

    private boolean pattern(String[] args, Map<String,Object>map){
           String test = (String) map.get("title");
        return true;
    }


}
