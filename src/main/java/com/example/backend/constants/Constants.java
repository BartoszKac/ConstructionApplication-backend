package com.example.backend.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${EBAY_BEST_CATEGORY_URL}")
    private String EBAY_BEST_CATEGORY_URL;

    @Value("${URL_ITEM_DETAILS}")
    private String URL_ITEM_DETAILS;

    @Value("${URL_ACCESS_TOKEN}")
    private String URL_ACCESS_TOKEN;

    private String defaultQuest="acrylic paint ";

    private String[] s = new String[]{
         "itemId",
         "price_value",
            "&forever-signs-of-scottsdale"
    };

    public String[] getS() {
        return s;
    }

    private String Scope = "https://api.ebay.com/oauth/api_scope";

    public String getScope() {
        return Scope;
    }
    public String getEbayBestCategoryUrl(String quest) {

        return EBAY_BEST_CATEGORY_URL+quest;
    }

    public String getEbayBestCategoryUrl(Enum Color,double area) {
        return EBAY_BEST_CATEGORY_URL+defaultQuest+String.valueOf(area) + Color.toString();
    }

    public String getURL_ITEM_DETAILS(String itemId) {
        return URL_ITEM_DETAILS+itemId;
    }

    public String getURL_ACCESS_TOKEN() {
        return URL_ACCESS_TOKEN;
    }
}
