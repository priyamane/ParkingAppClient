package com.example.model;
import com.google.gson.annotations.SerializedName;
public class Vehicle {

    @SerializedName("id")
    private Long id;

    @SerializedName("vehicleNumber")
    private String vehicleNumber;

    // ADD THESE FIELDS
    @SerializedName("vehicleType")
    private String vehicleType;

    @SerializedName("tenantName")
    private String tenantName;

    @SerializedName("entryTime")
    private String entryTime;
    public Vehicle() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVehicleNumber()
    { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber)
    { this.vehicleNumber = vehicleNumber; }
    public String getVehicleType()
    { return vehicleType; }
    public void setVehicleType(String vehicleType)
    { this.vehicleType = vehicleType; }

    public String getTenantName()
    { return tenantName; }
    public void setTenantName(String tenantName)
    { this.tenantName = tenantName; }
    public String getEntryTime()
    { return entryTime; }
    public void setEntryTime(String entryTime)
    { this.entryTime = entryTime; }
}