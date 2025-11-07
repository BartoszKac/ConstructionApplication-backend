package com.example.backend.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${EBAY_BEST_CATEGORY_URL}")
    private String EBAY_BEST_CATEGORY_URL;

    @Value("${URL_ITEM_DETAILS}")
    private String URL_ITEM_DETAILS;

    private String defaultQuest="acrylic paint ";

    public String getEbayBestCategoryUrl(String quest) {

        return EBAY_BEST_CATEGORY_URL+quest;
    }

    public String getEbayBestCategoryUrl(Enum Color,double area) {
        return EBAY_BEST_CATEGORY_URL+defaultQuest+String.valueOf(area) + Color.toString();
    }

    public String getURL_ITEM_DETAILS(String itemId) {
        return URL_ITEM_DETAILS+itemId;
    }


}
