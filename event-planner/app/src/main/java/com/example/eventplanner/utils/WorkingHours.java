package com.example.eventplanner.utils;

import androidx.annotation.NonNull;

import java.time.LocalTime;

public class WorkingHours {

    private LocalTime startTime;
    private LocalTime endTime;

    public WorkingHours(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    @NonNull
    public String toString() {
        return "WorkingHours{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

}
