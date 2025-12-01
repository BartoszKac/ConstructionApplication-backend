package com.example.backend.model.paint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

public class PaintProduct {

    private String unitCount;
    private String specificUsesForProduct;
    private String size;
    private String includedComponents;
    private String fullCureTime;
    private String productDimensions;
    private String specialFeatures;
    private String indoorOutdoorUsage;
    private String itemPackageQuantity;
    private boolean batteriesRequired;
    private String style;
    private String finishType;
    private String finish;
    private String packageInformation;
    private String color;
    private String colorCode;
    private boolean isWaterproof;
    private String coverage;
    private String material;
    private String surfaceRecommendation;
    private String brand;
    private String itemVolume;
    private String volume;
    private String itemForm;
    private String waterResistanceLevel;
    private String paintType;
    private String itemWeight;

    public PaintProduct() {
    }

    public PaintProduct(String unitCount, String specificUsesForProduct, String size, String includedComponents, String fullCureTime, String productDimensions, String specialFeatures, String indoorOutdoorUsage, String itemPackageQuantity, boolean batteriesRequired, String style, String finishType, String finish, String packageInformation, String color, String colorCode, boolean isWaterproof, String coverage, String material, String surfaceRecommendation, String brand, String itemVolume, String volume, String itemForm, String waterResistanceLevel, String paintType, String itemWeight) {
        this.unitCount = unitCount;
        this.specificUsesForProduct = specificUsesForProduct;
        this.size = size;
        this.includedComponents = includedComponents;
        this.fullCureTime = fullCureTime;
        this.productDimensions = productDimensions;
        this.specialFeatures = specialFeatures;
        this.indoorOutdoorUsage = indoorOutdoorUsage;
        this.itemPackageQuantity = itemPackageQuantity;
        this.batteriesRequired = batteriesRequired;
        this.style = style;
        this.finishType = finishType;
        this.finish = finish;
        this.packageInformation = packageInformation;
        this.color = color;
        this.colorCode = colorCode;
        this.isWaterproof = isWaterproof;
        this.coverage = coverage;
        this.material = material;
        this.surfaceRecommendation = surfaceRecommendation;
        this.brand = brand;
        this.itemVolume = itemVolume;
        this.volume = volume;
        this.itemForm = itemForm;
        this.waterResistanceLevel = waterResistanceLevel;
        this.paintType = paintType;
        this.itemWeight = itemWeight;
    }

    public String getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(String unitCount) {
        this.unitCount = unitCount;
    }

    public String getSpecificUsesForProduct() {
        return specificUsesForProduct;
    }

    public void setSpecificUsesForProduct(String specificUsesForProduct) {
        this.specificUsesForProduct = specificUsesForProduct;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getIncludedComponents() {
        return includedComponents;
    }

    public void setIncludedComponents(String includedComponents) {
        this.includedComponents = includedComponents;
    }

    public String getFullCureTime() {
        return fullCureTime;
    }

    public void setFullCureTime(String fullCureTime) {
        this.fullCureTime = fullCureTime;
    }

    public String getProductDimensions() {
        return productDimensions;
    }

    public void setProductDimensions(String productDimensions) {
        this.productDimensions = productDimensions;
    }

    public String getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(String specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public String getIndoorOutdoorUsage() {
        return indoorOutdoorUsage;
    }

    public void setIndoorOutdoorUsage(String indoorOutdoorUsage) {
        this.indoorOutdoorUsage = indoorOutdoorUsage;
    }

    public String getItemPackageQuantity() {
        return itemPackageQuantity;
    }

    public void setItemPackageQuantity(String itemPackageQuantity) {
        this.itemPackageQuantity = itemPackageQuantity;
    }

    public boolean isBatteriesRequired() {
        return batteriesRequired;
    }

    public void setBatteriesRequired(boolean batteriesRequired) {
        this.batteriesRequired = batteriesRequired;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFinishType() {
        return finishType;
    }

    public void setFinishType(String finishType) {
        this.finishType = finishType;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getPackageInformation() {
        return packageInformation;
    }

    public void setPackageInformation(String packageInformation) {
        this.packageInformation = packageInformation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isWaterproof() {
        return isWaterproof;
    }

    public void setWaterproof(boolean waterproof) {
        isWaterproof = waterproof;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSurfaceRecommendation() {
        return surfaceRecommendation;
    }

    public void setSurfaceRecommendation(String surfaceRecommendation) {
        this.surfaceRecommendation = surfaceRecommendation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemVolume() {
        return itemVolume;
    }

    public void setItemVolume(String itemVolume) {
        this.itemVolume = itemVolume;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getItemForm() {
        return itemForm;
    }

    public void setItemForm(String itemForm) {
        this.itemForm = itemForm;
    }

    public String getWaterResistanceLevel() {
        return waterResistanceLevel;
    }

    public void setWaterResistanceLevel(String waterResistanceLevel) {
        this.waterResistanceLevel = waterResistanceLevel;
    }

    public String getPaintType() {
        return paintType;
    }

    public void setPaintType(String paintType) {
        this.paintType = paintType;
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight;
    }
}
