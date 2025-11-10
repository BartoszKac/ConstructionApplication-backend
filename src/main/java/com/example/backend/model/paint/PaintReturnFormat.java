package com.example.backend.model.paint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class PaintReturnFormat {
    private String itemId;
    private String title;
    private String shortDescription;
    private Price   price;
    private String  categoryPath;
    private ItemLocation itemLocation;
    private String    itemWebUrl;
    private PaintProduct paintProduct;

    public PaintReturnFormat() {
    }

    public PaintReturnFormat(String itemId, String title, String shortDescription, Price price, String categoryPath, ItemLocation itemLocation, String itemWebUrl, PaintProduct paintProduct) {
        this.itemId = itemId;
        this.title = title;
        this.shortDescription = shortDescription;
        this.price = price;
        this.categoryPath = categoryPath;
        this.itemLocation = itemLocation;
        this.itemWebUrl = itemWebUrl;
        this.paintProduct = paintProduct;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void setCategoryPath(String categoryPath) {
        this.categoryPath = categoryPath;
    }

    public ItemLocation getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(ItemLocation itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemWebUrl() {
        return itemWebUrl;
    }

    public void setItemWebUrl(String itemWebUrl) {
        this.itemWebUrl = itemWebUrl;
    }

    public PaintProduct getPaintProduct() {
        return paintProduct;
    }

    public void setPaintProduct(PaintProduct paintProduct) {
        this.paintProduct = paintProduct;
    }
}

