package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.business.services.ReservationService;
import com.tekup.gatherwise.dao.entities.EventType;
import com.tekup.gatherwise.dao.entities.Reservation;
import com.tekup.gatherwise.dao.entities.Ticket;
import com.tekup.gatherwise.web.models.TicketForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.business.services.TicketService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.web.models.EventForm;

import jakarta.validation.Valid;

import java.sql.Date;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private static final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private TicketService ticketService;


    @Autowired
    private ReservationService reservationService;

    @GetMapping("")
    public String listEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "4") int pageSize,
                             @RequestParam(defaultValue = "creationDate") String sortBy,
                             @RequestParam(defaultValue = "asc") String order,
                             @RequestParam(required = false) Boolean isPublic,
                             @RequestParam(required = false) Boolean isArchived,
                             Model model) {
        Sort.Direction sortDirection = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<Event> eventPage = this.eventService.getAllEventsPagination(PageRequest.of(page, pageSize, Sort.by(sortDirection, sortBy)));
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("isPublic", isPublic);
        model.addAttribute("isArchived", isArchived);
        model.addAttribute("ticketService", ticketService); // Add this line

        return "ticket/event-list";
    }

    @GetMapping("/filter")
    public String filterEvents(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Boolean isPublic,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) Long eventTypeId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model) {

        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy != null ? sortBy : "startDate"));

        Page<Event> eventPage;
        if (search != null && !search.isEmpty()) {
            eventPage = eventService.getEventsByTitle(search, pageable);
        } else {
            eventPage = eventService.getAllEventsPagination(pageable);
        }

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("isPublic", isPublic);
        model.addAttribute("isArchived", isArchived);
        model.addAttribute("search", search);

        return "ticket/event-list";
    }




    @GetMapping("/{id}/list")
    public String listTickets(@PathVariable Long id, Model model) {
        List<Ticket> tickets = ticketService.getTicketsByEventId(id);
        Event event = eventService.getEventById(id);

        model.addAttribute("tickets", tickets);
        model.addAttribute("eventName", event.getTitle());
        model.addAttribute("ticketService", ticketService); // Add this line

        return "ticket/ticket-list";
    }




    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        TicketForm ticketForm = new TicketForm();
        ticketForm.setTicketType(ticket.getTicketType());
        ticketForm.setDescription(ticket.getDescription());
        ticketForm.setPrice(ticket.getPrice());
        ticketForm.setQuantity(ticket.getQuantity());
        ticketForm.setEvent(ticket.getEvent());
        ticketForm.setEventId(ticket.getEvent().getId()); // Set the event ID
        model.addAttribute("ticketForm", ticketForm);
        return "ticket/edit-ticket";
    }

    @RequestMapping(path = "/{id}/edit", method = RequestMethod.POST)
    public String updateTicket(@PathVariable Long id,
                               @Valid @ModelAttribute TicketForm ticketForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {

            return "ticket/edit-ticket";
        }
        Ticket ticket = ticketService.getTicketById(id);
        ticket.setTicketType(ticketForm.getTicketType());
        ticket.setDescription(ticketForm.getDescription());
        ticket.setPrice(ticketForm.getPrice());
        ticket.setQuantity(ticketForm.getQuantity());

        ticketService.updateTicket(ticket);
        return "redirect:/tickets";
    }



    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
        return "redirect:/tickets";
    }
}