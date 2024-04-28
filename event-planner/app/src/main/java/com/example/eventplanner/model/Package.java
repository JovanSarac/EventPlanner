package com.example.eventplanner.model;

import java.util.ArrayList;

public class Package {
    private Long id;
    private String name;
    private String description;
    private Double discount;
    private Boolean available;
    private Boolean visible;
    private String category;
    private String subCategory;
    private ArrayList<Product> products;
    private ArrayList<Service> services;
    private ArrayList<String> events;
    private Double price;
    private ArrayList<Integer> images;
    private String reservationDue;
    private String cancelationDue;
    private Boolean automaticAffirmation;

    public Package() {
    }

    public Package(Long id, String name, String description, Double discount, Boolean available, Boolean visible, String category, String subCategory, ArrayList<Product> products, ArrayList<Service> services, ArrayList<String> events, Double price, ArrayList<Integer> images, String reservationDue, String cancelationDue, Boolean automaticAffirmation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.available = available;
        this.visible = visible;
        this.category = category;
        this.subCategory = subCategory;
        this.products = products;
        this.services = services;
        this.events = events;
        this.price = price;
        this.images = images;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
    }

    public Package(String name, String description, Double discount, Boolean available, Boolean visible, String category, String subCategory, ArrayList<Product> products, ArrayList<Service> services, ArrayList<String> events, Double price, ArrayList<Integer> images, String reservationDue, String cancelationDue, Boolean automaticAffirmation) {
        this.name = name;
        this.description = description;
        this.discount = discount;
        this.available = available;
        this.visible = visible;
        this.category = category;
        this.subCategory = subCategory;
        this.products = products;
        this.services = services;
        this.events = events;
        this.price = price;
        this.images = images;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> imageTypes) {
        this.images = imageTypes;
    }

    public String getReservationDue() {
        return reservationDue;
    }

    public void setReservationDue(String reservationDue) {
        this.reservationDue = reservationDue;
    }

    public String getCancelationDue() {
        return cancelationDue;
    }

    public void setCancelationDue(String cancelationDue) {
        this.cancelationDue = cancelationDue;
    }

    public Boolean getAutomaticAffirmation() {
        return automaticAffirmation;
    }

    public void setAutomaticAffirmation(Boolean automaticAffirmation) {
        this.automaticAffirmation = automaticAffirmation;
    }
}
