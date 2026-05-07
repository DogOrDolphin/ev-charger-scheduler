package edu.sjsu.cmpe172.ev_charger_scheduler.controller;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.Appointment;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.BookAppointmentRequest;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.BookingResponse;
import edu.sjsu.cmpe172.ev_charger_scheduler.repository.AppointmentRepository;
import edu.sjsu.cmpe172.ev_charger_scheduler.service.AppointmentService;
import edu.sjsu.cmpe172.ev_charger_scheduler.service.MetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final MetricsService metricsService;
    private final AppointmentRepository appointmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    public AppointmentController(AppointmentService appointmentService,
                                 AppointmentRepository appointmentRepository,
                                 MetricsService metricsService) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.metricsService = metricsService;
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> book(@RequestBody BookAppointmentRequest req) {
        long startTime = System.currentTimeMillis();

        logger.info("Received booking request: userId={}, slotId={}", req.getUserId(), req.getSlotId());

        if (req.getUserId() <= 0 || req.getSlotId() <= 0) {
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordFailedBooking(latency);

            logger.warn("Invalid booking request received: userId={}, slotId={}",
                    req.getUserId(), req.getSlotId());
            return ResponseEntity.badRequest().body("userId and slotId must be provided (> 0)");
        }

        try {
            BookingResponse result = appointmentService.bookAndNotify(req.getUserId(), req.getSlotId());

            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordSuccessfulBooking(latency);

            logger.info("Booking completed successfully for userId={}, slotId={}, latencyMs={}",
                    req.getUserId(), req.getSlotId(), latency);
            return ResponseEntity.status(201).body(result);

        } catch (IllegalArgumentException e) {
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordFailedBooking(latency);

            logger.warn("Booking failed because user or slot was not found: userId={}, slotId={}, error={}, latencyMs={}",
                    req.getUserId(), req.getSlotId(), e.getMessage(), latency);
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (IllegalStateException e) {
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordFailedBooking(latency);

            logger.warn("Booking conflict for userId={}, slotId={}: {}, latencyMs={}",
                    req.getUserId(), req.getSlotId(), e.getMessage(), latency);
            return ResponseEntity.status(409).body(e.getMessage());

        } catch (Exception e) {
            long latency = System.currentTimeMillis() - startTime;
            metricsService.recordFailedBooking(latency);

            logger.error("Unexpected booking failure for userId={}, slotId={}, latencyMs={}",
                    req.getUserId(), req.getSlotId(), latency, e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/appointments")
    public List<Appointment> list(@RequestParam("userId") long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    @GetMapping("/appointments/{id}")
    public Appointment getOne(@PathVariable("id") long id) {
        return appointmentRepository.findById(id);
    }
}