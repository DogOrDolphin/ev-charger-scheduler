package edu.sjsu.cmpe172.ev_charger_scheduler.model;

public class NotificationResponse {
    private boolean sent;
    private String provider;
    private String status;

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}