package com.example.eventplanner.model;

import com.example.eventplanner.utils.WorkingHours;

public class WeeklySchedule {

    private Long id;
    private Long dateScheduleId;
    private Long workerId;
    private WorkingHours workingHours;

    public WeeklySchedule(Long dateScheduleId, Long workerId, WorkingHours workingHours) {
        this.dateScheduleId = dateScheduleId;
        this.workerId = workerId;
        this.workingHours = workingHours;
    }

    public WeeklySchedule() {
        this.workingHours = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateScheduleId() {
        return dateScheduleId;
    }

    public void setDateScheduleId(Long dateScheduleId) {
        this.dateScheduleId = dateScheduleId;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }
}
