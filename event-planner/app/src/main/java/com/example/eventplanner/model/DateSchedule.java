package com.example.eventplanner.model;

import com.example.eventplanner.utils.DateRange;

public class DateSchedule {

    private Long id;
    private Long workerId;
    private DateRange dateRange;

    public DateSchedule() {
        this.dateRange = null;
    }

    public DateSchedule(Long workerId, DateRange dateRange) {
        this.workerId = workerId;
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
}
