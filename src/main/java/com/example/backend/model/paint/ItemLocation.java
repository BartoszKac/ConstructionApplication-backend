package com.example.backend.model.paint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ItemLocation {
           private String city;
           private String stateOrProvince;
           private String country ;

    public ItemLocation() {
    }

    public String getCity() {
        return city;
    }

    public ItemLocation(String city, String stateOrProvince, String country) {
        this.city = city;
        this.stateOrProvince = stateOrProvince;
        this.country = country;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
