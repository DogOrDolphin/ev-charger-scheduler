package edu.sjsu.cmpe172.ev_charger_scheduler.service;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.Appointment;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.BookingResponse;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.NotificationResponse;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final BookingTxService bookingTxService;
    private final NotificationClientService notificationClientService;

    public AppointmentService(BookingTxService bookingTxService,
                              NotificationClientService notificationClientService) {
        this.bookingTxService = bookingTxService;
        this.notificationClientService = notificationClientService;
    }

    public Appointment book(long userId, long slotId) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                return bookingTxService.bookOnce(userId, slotId);
            } catch (IllegalStateException ex) {
                if (attempt == 3) {
                    throw ex;
                }
            }
        }
        throw new IllegalStateException("Booking failed");
    }

    public BookingResponse bookAndNotify(long userId, long slotId) {
        Appointment appointment = book(userId, slotId);

        NotificationResponse notificationResponse =
                notificationClientService.sendBookingConfirmation(appointment);

        BookingResponse response = new BookingResponse();
        response.setAppointment(appointment);
        response.setNotificationSent(notificationResponse.isSent());
        response.setNotificationStatus(notificationResponse.getStatus());

        return response;
    }
}