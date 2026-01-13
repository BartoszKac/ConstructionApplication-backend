package com.example.backend.service.util;

import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.*;

@Service
public class PaintCoverageService {

    private static final double DEFAULT_EFFICIENCY = 12.0;

    private static final Map<String, Double> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("magnat", 16.0);
        KEYWORDS.put("ceramic", 16.0);
        KEYWORDS.put("sniezka", 13.0);
        KEYWORDS.put("dekoral", 14.0);
        KEYWORDS.put("akrylit", 14.0);
        KEYWORDS.put("tikkurila", 12.0);

        KEYWORDS.put("texture", 6.0);
        KEYWORDS.put("roll-on", 8.0);
        KEYWORDS.put("sand", 8.0);
        KEYWORDS.put("dulux", 14.0);
        KEYWORDS.put("trade", 17.0);
    }

    public double getM2PerLiter(String title) {
        if (title == null || title.isEmpty()) return DEFAULT_EFFICIENCY;

        String cleanTitle = normalize(title.toLowerCase());
        String[] words = cleanTitle.split("[\\s,._/+-]+");

        double totalEfficiency = 0;
        int matches = 0;

        for (String word : words) {
            if (KEYWORDS.containsKey(word)) {
                totalEfficiency += KEYWORDS.get(word);
                matches++;
            }
        }

        if (matches > 0) {
            return totalEfficiency / matches;
        }

        if (cleanTitle.contains("texture") || cleanTitle.contains("struktural")) return 6.0;

        return DEFAULT_EFFICIENCY;
    }

    private String normalize(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        return nfdNormalizedString.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replace("ł", "l");
    }
}