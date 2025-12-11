package com.example.backend.service.util;


import com.example.backend.maper.Maper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PatternChecker  {

    public List<HashMap<String,Object>> processPattern(JsonNode jsonNode, String[] args){
        List<HashMap<String,Object>> list = new ArrayList<>();


        try {
            jsonNode.forEach(
                    node -> {
                        HashMap<String,Object> hashMap1 = new HashMap<>();

                        Maper.flatten("", node, hashMap1);


                        if (pattern(args, hashMap1)) {
                            list.add(hashMap1);
                        }
                    });
        }catch (Exception e){
            System.out.println("Blad w filturawniu dannych: " + e.getMessage());
        }
        System.out.println(jsonNode.size());



        return list;
    }

    private boolean pattern(String[] args, Map<String,Object>map){
           String title = ((String) map.get("title")).toLowerCase();
        System.out.println(title);
                for(String s : args){
                    if(!title.contains(s)){
                        return false;
                    }
                }
        return true;
    }


}
