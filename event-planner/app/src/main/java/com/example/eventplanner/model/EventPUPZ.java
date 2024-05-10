package com.example.eventplanner.model;

public class EventPUPZ {

    private Long id;
    private String startHours;
    private String endHours;
    private String occurenceDate;
    private Long dateScheduleId;
    private String day;
    private String type;

    public EventPUPZ() {
    }

    public EventPUPZ(Long id, String startHours, String endHours, String occurenceDate, Long dateScheduleId, String day, String type) {
        this.id = id;
        this.startHours = startHours;
        this.endHours = endHours;
        this.occurenceDate = occurenceDate;
        this.dateScheduleId = dateScheduleId;
        this.day = day;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartHours() {
        return startHours;
    }

    public void setStartHours(String startHours) {
        this.startHours = startHours;
    }

    public String getEndHours() {
        return endHours;
    }

    public void setEndHours(String endHours) {
        this.endHours = endHours;
    }

    public String getOccurenceDate() {
        return occurenceDate;
    }

    public void setOccurenceDate(String occurenceDate) {
        this.occurenceDate = occurenceDate;
    }

    public Long getDateScheduleId() {
        return dateScheduleId;
    }

    public void setDateScheduleId(Long dateScheduleId) {
        this.dateScheduleId = dateScheduleId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
