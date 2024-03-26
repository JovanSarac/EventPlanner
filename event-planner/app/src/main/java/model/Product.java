package model;

import java.util.ArrayList;
import java.util.UUID;

public class Product {
    private Long id;
    private String category;
    private String subcategory;
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private Integer imageId;
    private ArrayList<String> events;
    private Boolean available;
    private Boolean visible;

    public Product() {
    }

    public Product(Long id, String category, String subcategory, String name, String description, Double price, Double discount, Integer imageId,ArrayList<String> events, Boolean available, Boolean visible) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageId = imageId;
        this.events = events;
        this.available = available;
        this.visible = visible;
    }

    public Product(String category, String subcategory, String name, String description, Double price, Double discount, Integer imageId, ArrayList<String> events, Boolean available, Boolean visible) {
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.imageId = imageId;
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

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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
