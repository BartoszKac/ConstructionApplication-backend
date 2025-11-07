package com.example.backend.service;

import com.example.backend.model.Area;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PainService {

    public ResponseEntity<?> processPainData(List<Area> painData) {
        List<Area> AddScore = painData.stream().
                filter((v)->v.getName().
                        equals("Add")).toList();

         double add =  AddScore.stream()
                .mapToDouble(v -> Double.parseDouble(v.getHeight()) * Double.parseDouble(v.getWidth())).
                  sum();

        List<Area> DeleteScore = painData.stream().
                filter((v)->v.getName().
                        equals("Delete")).toList();

        double delete =  DeleteScore.stream()
                .mapToDouble(v -> Double.parseDouble(v.getHeight()) * Double.parseDouble(v.getWidth())).
                sum();
        double score = add-delete;
       return ResponseEntity.ok(score);
    }
}
