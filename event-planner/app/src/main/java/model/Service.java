package model;

import java.util.ArrayList;

public class Service {

    private Long id;
    private String category;
    private String subcategory;
    private String name;
    private String description;
    private Integer imageId;
    private String specific;
    private Double pricePerHour;
    private Double fullPrice;
    private Double duration;
    private Double durationMin;
    private Double durationMax;
    private String location;
    private Double discount;
    private ArrayList<String> providers;
    private ArrayList<String> events;
    private String reservationDue;
    private String cancelationDue;
    private Boolean automaticAffirmation;
    private Boolean available;
    private Boolean visible;

    public Service() {
    }

    public Service(Long id, String category, String subcategory, String name, String description, Integer imageId, String specific, Double pricePerHour, Double fullPrice, Double duration, String location, Double discount, ArrayList<String> providers, ArrayList<String> events, String reservationDue, String cancelationDue, Boolean automaticAffirmation, Boolean available, Boolean visible) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.specific = specific;
        this.pricePerHour = pricePerHour;
        this.fullPrice = fullPrice;
        this.duration = duration;
        this.location = location;
        this.discount = discount;
        this.providers = providers;
        this.events = events;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
        this.available = available;
        this.visible = visible;
    }

    public Service(String category, String subcategory, String name, String description, Integer imageId, String specific, Double pricePerHour, Double fullPrice, Double duration, String location, Double discount, ArrayList<String> providers, ArrayList<String> events, String reservationDue, String cancelationDue, Boolean automaticAffirmation, Boolean available, Boolean visible) {
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.specific = specific;
        this.pricePerHour = pricePerHour;
        this.fullPrice = fullPrice;
        this.duration = duration;
        this.location = location;
        this.discount = discount;
        this.providers = providers;
        this.events = events;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
        this.available = available;
        this.visible = visible;
    }

    public Service(Long id, String category, String subcategory, String name, String description, Integer imageId, String specific, Double pricePerHour, Double fullPrice, Double duration, Double durationMin, Double durationMax, String location, Double discount, ArrayList<String> providers, ArrayList<String> events, String reservationDue, String cancelationDue, Boolean automaticAffirmation, Boolean available, Boolean visible) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.specific = specific;
        this.pricePerHour = pricePerHour;
        this.fullPrice = fullPrice;
        this.duration = duration;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.location = location;
        this.discount = discount;
        this.providers = providers;
        this.events = events;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
        this.available = available;
        this.visible = visible;
    }

    public Service(String category, String subcategory, String name, String description, Integer imageId, String specific, Double pricePerHour, Double fullPrice, Double duration, Double durationMin, Double durationMax, String location, Double discount, ArrayList<String> providers, ArrayList<String> events, String reservationDue, String cancelationDue, Boolean automaticAffirmation, Boolean available, Boolean visible) {
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.imageId = imageId;
        this.specific = specific;
        this.pricePerHour = pricePerHour;
        this.fullPrice = fullPrice;
        this.duration = duration;
        this.durationMin = durationMin;
        this.durationMax = durationMax;
        this.location = location;
        this.discount = discount;
        this.providers = providers;
        this.events = events;
        this.reservationDue = reservationDue;
        this.cancelationDue = cancelationDue;
        this.automaticAffirmation = automaticAffirmation;
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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(Double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Double durationMin) {
        this.durationMin = durationMin;
    }

    public Double getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Double durationMax) {
        this.durationMax = durationMax;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ArrayList<String> getProviders() {
        return providers;
    }

    public void setProviders(ArrayList<String> providers) {
        this.providers = providers;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
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
