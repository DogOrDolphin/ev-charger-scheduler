package edu.sjsu.cmpe172.ev_charger_scheduler.controller;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.NotificationRequest;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/mock-notifications")
public class MockNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(MockNotificationController.class);

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> send(@RequestBody NotificationRequest request) {
        logger.info("Mock notification service received request for appointmentId={}, userId={}, slotId={}",
                request.getAppointmentId(),
                request.getUserId(),
                request.getSlotId());

        NotificationResponse response = new NotificationResponse();
        response.setSent(true);
        response.setProvider("mock-notification-service");
        response.setStatus("Confirmation sent successfully");

        logger.info("Mock notification service responded successfully for appointmentId={}",
                request.getAppointmentId());

        return ResponseEntity.ok(response);
    }
}