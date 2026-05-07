package edu.sjsu.cmpe172.ev_charger_scheduler.model;

import java.time.LocalDateTime;

public class Appointment {
    private long appointmentId;
    private long userId;
    private long slotId;
    private String status;
    private LocalDateTime createdAt;

    public long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(long appointmentId) { this.appointmentId = appointmentId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getSlotId() { return slotId; }
    public void setSlotId(long slotId) { this.slotId = slotId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}