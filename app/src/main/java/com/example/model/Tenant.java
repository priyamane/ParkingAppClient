package com.example.model;

import com.google.gson.annotations.SerializedName;

public class Tenant {

    @SerializedName("tenantId")
    private Long tenantId;
    @SerializedName("tenantName")
    private String tenantName;
    @SerializedName("baseRate")
    private double baseRate;
    @SerializedName("hourlyRate")
    private double hourlyRate;

    // 1. Default Constructor (for Gson)
    public Tenant() {}

    // 2. Parameterized Constructor
    public Tenant(Long tenantId, String tenantName, double baseRate, double hourlyRate) {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
        this.baseRate = baseRate;
        this.hourlyRate = hourlyRate;
    }

    // --- Getters ---
    public Long getTenantId() { return tenantId; }
    public String getTenantName() { return tenantName; }
    public double getBaseRate() { return baseRate; }
    public double getHourlyRate() { return hourlyRate; }

    // --- Setters ---
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    public void setBaseRate(double baseRate) { this.baseRate = baseRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
}