package com.example.eventplanner.model;

import java.util.List;

public class EventType {
    private String typeName;
    private String typeDescription;
    private List<Subcategory> recomendedSubcategories;
    public EventType() {
    }

    public EventType(String typeName, String typeDescription, List<Subcategory> recomendedSubcategories) {
        this.typeName = typeName;
        this.typeDescription = typeDescription;
        this.recomendedSubcategories = recomendedSubcategories;
    }

    public List<Subcategory> getRecomendedSubcategories() {
        return recomendedSubcategories;
    }

    public void setRecomendedSubcategories(List<Subcategory> recomendedSubcategories) {
        this.recomendedSubcategories = recomendedSubcategories;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }
}
