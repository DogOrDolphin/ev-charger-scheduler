package edu.sjsu.cmpe172.ev_charger_scheduler.service;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.Appointment;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.NotificationRequest;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationClientService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClientService.class);
    private final RestTemplate restTemplate;

    @Value("${notification.service.base-url}")
    private String baseUrl;

    public NotificationClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NotificationResponse sendBookingConfirmation(Appointment appointment) {
        NotificationRequest request = new NotificationRequest();
        request.setAppointmentId(appointment.getAppointmentId());
        request.setUserId(appointment.getUserId());
        request.setSlotId(appointment.getSlotId());
        request.setMessage("Your EV charging appointment has been confirmed.");

        String url = baseUrl + "/mock-notifications/send";

        logger.info("Sending notification for appointmentId={}, userId={}, slotId={}",
                appointment.getAppointmentId(),
                appointment.getUserId(),
                appointment.getSlotId());

        try {
            NotificationResponse response =
                    restTemplate.postForObject(url, request, NotificationResponse.class);

            if (response != null && response.isSent()) {
                logger.info("Notification sent successfully for appointmentId={}",
                        appointment.getAppointmentId());
            } else {
                logger.warn("Notification service returned an unsuccessful response for appointmentId={}",
                        appointment.getAppointmentId());
            }

            return response;
        } catch (Exception e) {
            logger.warn("Notification failed for appointmentId={}: {}",
                    appointment.getAppointmentId(), e.getMessage());

            NotificationResponse failed = new NotificationResponse();
            failed.setSent(false);
            failed.setProvider("mock-notification-service");
            failed.setStatus("Notification failed: " + e.getMessage());
            return failed;
        }
    }
}