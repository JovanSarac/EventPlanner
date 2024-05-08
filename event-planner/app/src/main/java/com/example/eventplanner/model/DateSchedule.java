package com.example.eventplanner.model;

import com.example.eventplanner.utils.DateRange;
import com.example.eventplanner.utils.Days;
import com.example.eventplanner.utils.WorkingHours;

import java.util.HashMap;

public class DateSchedule {

    private Long id;
    private Long workerId;
    private DateRange dateRange;
    private HashMap<Days, WorkingHours> schedule;

    public DateSchedule() {
        this.dateRange = null;

        this.schedule = new HashMap<>();
        for (Days day : Days.values()) {
            this.schedule.put(day, null);
        }
    }

    public DateSchedule(Long workerId, DateRange dateRange, HashMap<Days, WorkingHours> schedule) {
        this.workerId = workerId;
        this.schedule = schedule;
        this.dateRange = dateRange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public HashMap<Days, WorkingHours> getSchedule() {
        return schedule;
    }

    public void setSchedule(HashMap<Days, WorkingHours> schedule) {
        this.schedule = schedule;
    }
}
