package com.example.eventplanner.models;

public class Subcategory {
    private String categoryName;
    private String name;
    private String description;
    private int type;//0 -service; 1 - product

    public Subcategory() {
    }

    public Subcategory(String categoryName, String name, String description, int type) {
        this.categoryName = categoryName;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
