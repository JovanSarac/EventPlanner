package model;

import java.util.ArrayList;
import java.util.UUID;

public class Product {
    private Long id;
    private String category;
    private String subCategory;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    //private ?? pictures;
    private ArrayList<String> events;
    private Boolean available;
    private Boolean visible;

    public Product() {
    }

    public Product(Long id, String category, String subCategory, String name, String description, Double price, Double discount, ArrayList<String> events, Boolean available, Boolean visible) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.events = events;
        this.available = available;
        this.visible = visible;
    }

    public Product(String category, String subCategory, String name, String description, Double price, Double discount, ArrayList<String> events, Boolean available, Boolean visible) {
        this.category = category;
        this.subCategory = subCategory;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.events = events;
        this.available = available;
        this.visible = visible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
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
}
