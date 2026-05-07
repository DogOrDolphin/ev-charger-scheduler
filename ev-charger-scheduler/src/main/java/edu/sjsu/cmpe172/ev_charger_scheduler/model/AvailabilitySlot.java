package edu.sjsu.cmpe172.ev_charger_scheduler.model;

import java.time.LocalDateTime;

public class AvailabilitySlot {
    private long slotId;
    private long chargerId;
    private String chargerLabel;   // from JOIN (not stored in slots table)
    private String location;       // from JOIN
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getSlotId() { return slotId; }
    public void setSlotId(long slotId) { this.slotId = slotId; }

    public long getChargerId() { return chargerId; }
    public void setChargerId(long chargerId) { this.chargerId = chargerId; }

    public String getChargerLabel() { return chargerLabel; }
    public void setChargerLabel(String chargerLabel) { this.chargerLabel = chargerLabel; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}