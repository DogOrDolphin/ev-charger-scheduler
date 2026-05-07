package edu.sjsu.cmpe172.ev_charger_scheduler.controller;

import edu.sjsu.cmpe172.ev_charger_scheduler.service.HealthService;
import edu.sjsu.cmpe172.ev_charger_scheduler.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    private final HealthService healthService;
    private final MetricsService metricsService;

    public HealthController(HealthService healthService, MetricsService metricsService) {
        this.healthService = healthService;
        this.metricsService = metricsService;
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        boolean dbUp = healthService.isDatabaseUp();

        String overallStatus = dbUp ? "UP" : "DOWN";

        return ResponseEntity.ok(Map.of(
                "status", overallStatus,
                "timestamp", LocalDateTime.now().toString(),
                "database", dbUp ? "UP" : "DOWN",
                "successfulBookings", metricsService.getSuccessfulBookings(),
                "failedBookings", metricsService.getFailedBookings(),
                "averageBookingLatencyMs", metricsService.getAverageBookingLatencyMs()
        ));
    }
}