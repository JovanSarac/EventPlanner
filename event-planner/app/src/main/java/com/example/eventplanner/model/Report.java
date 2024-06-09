package com.example.eventplanner.model;

public class Report {
    private String status;
    private String description;
    private String reporterPUPVId;

    public Report() {
    }

    public Report(String status, String description, String reporterPUPVId) {
        this.status = status;
        this.description = description;
        this.reporterPUPVId = reporterPUPVId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporterPUPVId() {
        return reporterPUPVId;
    }

    public void setReporterPUPVId(String reporterPUPVId) {
        this.reporterPUPVId = reporterPUPVId;
    }
}
