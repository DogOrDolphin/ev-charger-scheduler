package edu.sjsu.cmpe172.ev_charger_scheduler.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsService {

    private final AtomicLong successfulBookings = new AtomicLong(0);
    private final AtomicLong failedBookings = new AtomicLong(0);
    private final AtomicLong totalBookingLatencyMs = new AtomicLong(0);

    public void recordSuccessfulBooking(long latencyMs) {
        successfulBookings.incrementAndGet();
        totalBookingLatencyMs.addAndGet(latencyMs);
    }

    public void recordFailedBooking(long latencyMs) {
        failedBookings.incrementAndGet();
        totalBookingLatencyMs.addAndGet(latencyMs);
    }

    public long getSuccessfulBookings() {
        return successfulBookings.get();
    }

    public long getFailedBookings() {
        return failedBookings.get();
    }

    public double getAverageBookingLatencyMs() {
        long totalOperations = successfulBookings.get() + failedBookings.get();
        if (totalOperations == 0) {
            return 0.0;
        }
        return (double) totalBookingLatencyMs.get() / totalOperations;
    }
}