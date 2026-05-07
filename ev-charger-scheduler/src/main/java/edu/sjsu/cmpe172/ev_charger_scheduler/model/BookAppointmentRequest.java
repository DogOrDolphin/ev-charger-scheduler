package edu.sjsu.cmpe172.ev_charger_scheduler.model;

public class BookAppointmentRequest {
    private long userId;
    private long slotId;

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getSlotId() { return slotId; }
    public void setSlotId(long slotId) { this.slotId = slotId; }
}