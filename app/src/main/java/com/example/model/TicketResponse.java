package com.example.model;
public class TicketResponse {

    private String ticketId;
    private String entryTime;
    private String qrPayload;
    public TicketResponse(String ticketId, String entryTime, String qrPayload) {
        this.ticketId = ticketId;
        this.entryTime = entryTime;
        this.qrPayload = qrPayload;
    }

    public String getTicketId() {
        return ticketId;
    }
   public String getEntryTime() {
        return entryTime;
    }
    public String getQrPayload() {
        return qrPayload;
    }

    // --- Setters (Optional, but often included for completeness) ---

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public void setQrPayload(String qrPayload) {
        this.qrPayload = qrPayload;
    }
}