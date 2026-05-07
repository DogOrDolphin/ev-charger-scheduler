package edu.sjsu.cmpe172.ev_charger_scheduler.service;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.AvailabilitySlot;
import edu.sjsu.cmpe172.ev_charger_scheduler.repository.AvailabilitySlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotService {
    private final AvailabilitySlotRepository slotRepo;

    public SlotService(AvailabilitySlotRepository slotRepo) {
        this.slotRepo = slotRepo;
    }

    public List<AvailabilitySlot> listOpenSlots(LocalDate date) {
        return slotRepo.findOpenSlotsByDate(date);
    }
}