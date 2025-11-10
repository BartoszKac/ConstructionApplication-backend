package com.example.backend.model.paint;

import com.example.backend.model.paint.ItemLocation;
import com.example.backend.model.paint.PaintProduct;
import com.example.backend.model.paint.PaintReturnFormat;
import com.example.backend.model.paint.Price;
import com.fasterxml.jackson.databind.JsonNode;

public class PaintMapper {

    public static PaintReturnFormat map(JsonNode json) {

        // pobieramy obiekt paintProduct z JSON
        JsonNode aspects = json.path("localizedAspects");

        PaintProduct paintProduct = new PaintProduct();

        for (JsonNode aspect : aspects) {
            String name = aspect.path("name").asText().trim();
            String value = aspect.path("value").asText().trim();

            switch (name.toLowerCase()) {
                case "unit count" -> paintProduct.setUnitCount(value);
                case "specific uses for product" -> paintProduct.setSpecificUsesForProduct(value);
                case "size" -> paintProduct.setSize(value);
                case "included components" -> paintProduct.setIncludedComponents(value);
                case "full cure time" -> paintProduct.setFullCureTime(value);
                case "product dimensions" -> paintProduct.setProductDimensions(value);
                case "special features", "special feature" -> paintProduct.setSpecialFeatures(value);
                case "indoor/outdoor usage" -> paintProduct.setIndoorOutdoorUsage(value);
                case "item package quantity" -> paintProduct.setItemPackageQuantity(value);
                case "style" -> paintProduct.setStyle(value);
                case "finish type" -> paintProduct.setFinishType(value);
                case "finish" -> paintProduct.setFinish(value);
                case "package information" -> paintProduct.setPackageInformation(value);
                case "color" -> paintProduct.setColor(value);
                case "color code" -> paintProduct.setColorCode(value);
                case "is waterproof" -> paintProduct.setWaterproof(Boolean.parseBoolean(value));
                case "coverage" -> paintProduct.setCoverage(value);
                case "material" -> paintProduct.setMaterial(value);
                case "surface recommendation" -> paintProduct.setSurfaceRecommendation(value);
                case "brand" -> paintProduct.setBrand(value);
                case "item volume" -> paintProduct.setItemVolume(value);
                case "volume" -> paintProduct.setVolume(value);
                case "item form" -> paintProduct.setItemForm(value);
                case "water resistance level" -> paintProduct.setWaterResistanceLevel(value);
                case "paint type" -> paintProduct.setPaintType(value);
                case "item weight" -> paintProduct.setItemWeight(value);
            }
        }

        JsonNode loc = json.path("itemLocation");
        ItemLocation itemLocation = new ItemLocation(
                loc.path("city").asText(""),
                loc.path("stateOrProvince").asText(""),
                loc.path("country").asText("")
        );

        JsonNode priceNode = json.path("price");
        Price price = new Price(
                priceNode.path("value").asText(""),
                priceNode.path("currency").asText("")
        );


        // --- OBLICZANIE WYDAJNOŚCI ---
        double liters = convertVolumeToLiters(paintProduct.getItemVolume());

        if (liters > 0) {
            double coverageOneCoat = liters * 10;   // 10 m² / 1L
            double coverageTwoCoats = liters * 5;    // 5 m² / 1L
            paintProduct.setCoverage(String.format("~%.1f m² (1 coat) / ~%.1f m² (2 coats)", coverageOneCoat, coverageTwoCoats));
        } else if (paintProduct.getCoverage() == null || paintProduct.getCoverage().isBlank()) {
            paintProduct.setCoverage("Not provided");
        }

        return new PaintReturnFormat(
                json.path("itemId").asText(),
                json.path("title").asText(),
                json.path("shortDescription").asText(),
                price,
                json.path("categoryPath").asText(),
                itemLocation,
                json.path("itemWebUrl").asText(),
                paintProduct
        );
    }

    private static double convertVolumeToLiters(String volumeString) {
        volumeString = volumeString.toLowerCase().trim();

        try {
            if (volumeString.contains("gallon") || volumeString.contains("gal"))
                return 3.78; // 1 gal ≈ 3.78 L

            if (volumeString.contains("ml"))
                return Double.parseDouble(volumeString.replace("ml", "").trim()) / 1000.0;

            if (volumeString.contains("l"))
                return Double.parseDouble(volumeString.replace("l", "").trim());

        } catch (Exception ignored) {}

        return 0;
    }
}
