package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.Reservation;
import com.tekup.gatherwise.dao.entities.Ticket;
import com.tekup.gatherwise.web.models.ReservationForm;
import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.ReservationService;
import com.tekup.gatherwise.business.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EventService eventService;

    @PostMapping(path = "/checkout/{eventId}/{ticketId}/{quantity}")
    public String goToCheckout(@PathVariable Long eventId, @PathVariable Long ticketId, @PathVariable int quantity, Model model) {
        // Fetch event details
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            model.addAttribute("error", "Invalid event ID");
            return "errors";
        }

        // Fetch ticket details
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            model.addAttribute("error", "Invalid ticket ID");
            return "errors";
        }

        // Add attributes to the model
        model.addAttribute("event", event);
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("quantity", quantity);
        model.addAttribute("ticket", ticket);

        return "checkout";
    }

    @PostMapping(path = "/checkout/create")
    public String createReservation(@RequestParam int quantity, @Valid @ModelAttribute ReservationForm reservationForm,
                                    BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "event-checkout";
        }

        Ticket ticket = ticketService.getTicketById(reservationForm.getTicketId());
        if (ticket == null) {
            model.addAttribute("error", "Invalid ticket ID");
            return "event-checkout";
        }

        for (int i = 0; i < quantity; i++) {
            Reservation reservation = new Reservation();
            reservation.setAttendeeName(reservationForm.getAttendeeName());
            reservation.setAttendeeEmail(reservationForm.getAttendeeEmail());
            reservation.setPhoneNumber(reservationForm.getPhoneNumber());
            reservation.setReservationTimeStamp(LocalDateTime.now());
            reservation.setBarcode(UUID.randomUUID().toString());
            reservation.setTicket(ticket);

            reservationService.addReservation(reservation);
        }

        model.addAttribute("success", "Reservations created successfully");
        return "redirect:/checkout/" + ticket.getEvent().getId() + "/" + ticket.getId() + "/" + quantity;
    }
}