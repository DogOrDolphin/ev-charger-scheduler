package edu.sjsu.cmpe172.ev_charger_scheduler.controller;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.AvailabilitySlot;
import edu.sjsu.cmpe172.ev_charger_scheduler.service.SlotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/slots")
    public List<AvailabilitySlot> openSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return slotService.listOpenSlots(date);
    }
}