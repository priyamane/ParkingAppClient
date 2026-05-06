

package com.example.model;
public class VehicleEntryRequest

{
    private String vehicleNumber;
    private String mobileNumber;
    private String gateEntered;
    private String vehicleType;
    private String visitingTenant;
    private String rateStructureId;
    public VehicleEntryRequest(String vehicleNumber, String mobileNumber, String gateEntered, String vehicleType, String visitingTenant, String rateStructureId) {
        this.vehicleNumber = vehicleNumber;
        this.mobileNumber = mobileNumber;
        this.gateEntered = gateEntered;
        this.vehicleType = vehicleType; // <-- New field initialization
        this.visitingTenant = visitingTenant;
        this.rateStructureId = rateStructureId;
    }
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public String getGateEntered() {
        return gateEntered;
    }

    public String getVehicleType() {
        return vehicleType;
    }
    public String getVisitingTenant() {
        return visitingTenant;
    }
    public String getRateStructureId() {
        return rateStructureId;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public void setGateEntered(String gateEntered) {
        this.gateEntered = gateEntered;
    }
    public void setVehicleType(String vehicleType) {this.vehicleType = vehicleType;}
    public void setVisitingTenant(String visitingTenant) {this.visitingTenant = visitingTenant;}
    public void setRateStructureId(String rateStructureId) {this.rateStructureId = rateStructureId;}
}