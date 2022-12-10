package com.example.mobilele.model.dto;

import com.example.mobilele.model.enums.EngineEnum;
import com.example.mobilele.model.enums.TransmissionEnum;
import java.math.BigDecimal;

public class CardListingOfferDTO {

    private String imageUrl;
    private Integer year;
    private String brand;
    private String model;
    private Integer mileage;
    private BigDecimal price;
    private EngineEnum engine;
    private TransmissionEnum transmission;

    public String getImageUrl() {
        return imageUrl;
    }

    public CardListingOfferDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public CardListingOfferDTO setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public CardListingOfferDTO setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public CardListingOfferDTO setModel(String model) {
        this.model = model;
        return this;
    }

    public Integer getMileage() {
        return mileage;
    }

    public CardListingOfferDTO setMileage(Integer mileage) {
        this.mileage = mileage;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CardListingOfferDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public EngineEnum getEngine() {
        return engine;
    }

    public CardListingOfferDTO setEngine(EngineEnum engine) {
        this.engine = engine;
        return this;
    }

    public TransmissionEnum getTransmission() {
        return transmission;
    }

    public CardListingOfferDTO setTransmission(TransmissionEnum transmission) {
        this.transmission = transmission;
        return this;
    }

    public String getOfferHighlight() {
        return this.year + " " + this.brand + " " + this.model;
    }
}
