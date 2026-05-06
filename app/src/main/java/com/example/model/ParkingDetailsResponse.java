package com.example.model;

import com.google.gson.annotations.SerializedName;

public class ParkingDetailsResponse {

    private String ticketId;
    private String vehicleNumber;
    private String visitingTenant;
    private String entryTime; // Use String for easy JSON transport

    private double durationHours;
    private double totalChargesDue;

    // Default constructor (required by Gson)
    public ParkingDetailsResponse() {}

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    public String getVisitingTenant() {
        return visitingTenant;
    }
    public void setVisitingTenant(String visitingTenant) {
        this.visitingTenant = visitingTenant;
    }
    public String getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }
    public double getDurationHours() {
        return durationHours;
    }
    public void setDurationHours(double durationHours) {
        this.durationHours = durationHours;
    }
    public double getTotalChargesDue() {
        return totalChargesDue;
    }
    public void setTotalChargesDue(double totalChargesDue) {
        this.totalChargesDue = totalChargesDue;
    }
}