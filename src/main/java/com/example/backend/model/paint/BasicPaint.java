package com.example.backend.model.paint;

public class BasicPaint {
    private String name;
    private Double coveragePerLiter; // in square meters per liter
    private Double pricePerLiter; // in currency units per liter

    public BasicPaint() {
    }

    public BasicPaint(String name, Double coveragePerLiter, Double pricePerLiter) {
        this.name = name;
        this.coveragePerLiter = coveragePerLiter;
        this.pricePerLiter = pricePerLiter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCoveragePerLiter() {
        return coveragePerLiter;
    }

    public void setCoveragePerLiter(Double coveragePerLiter) {
        this.coveragePerLiter = coveragePerLiter;
    }

    public Double getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(Double pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }
}
