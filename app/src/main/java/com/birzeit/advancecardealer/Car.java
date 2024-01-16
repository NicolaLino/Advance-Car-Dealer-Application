package com.birzeit.advancecardealer;

public class Car {
    private int id;
    private String factoryName;
    private String model;
    private int price;
    private String fuelType;
    private String transmissionType;
    private String mileage;
    private String url;

    private boolean special;

    // Constructors
    public void CarDetails() {
    }

    public void CarDetails(int id, String factoryName, String model, int price, String fuelType, String transmissionType, String mileage, String url, boolean special ) {
        this.id = id;
        this.factoryName = factoryName;
        this.model = model;
        this.price = price;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.mileage = mileage;
        this.url = url;
        this.special=special;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
}
