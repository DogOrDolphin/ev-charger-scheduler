package edu.sjsu.cmpe172.ev_charger_scheduler.service;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.Appointment;
import edu.sjsu.cmpe172.ev_charger_scheduler.model.AvailabilitySlot;
import edu.sjsu.cmpe172.ev_charger_scheduler.repository.AppointmentRepository;
import edu.sjsu.cmpe172.ev_charger_scheduler.repository.AvailabilitySlotRepository;
import edu.sjsu.cmpe172.ev_charger_scheduler.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingTxService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final UserRepository userRepository;

    public BookingTxService(AppointmentRepository appointmentRepository,
                            AvailabilitySlotRepository slotRepository,
                            UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Appointment bookOnce(long userId, long slotId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        AvailabilitySlot slot = slotRepository.findById(slotId);

        if (!"OPEN".equals(slot.getStatus())) {
            throw new IllegalStateException("Slot is not available");
        }

        boolean reserved = slotRepository.reserveSlot(slotId, slot.getVersion());

        if (!reserved) {
            throw new IllegalStateException("Concurrent booking conflict");
        }

        return appointmentRepository.create(userId, slotId);
    }
}