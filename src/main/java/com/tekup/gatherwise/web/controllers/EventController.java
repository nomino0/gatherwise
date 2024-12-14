package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.dao.entities.Ticket;
import com.tekup.gatherwise.web.models.TicketForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);
    private static final String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("")
    public String listEvents(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int pageSize,
                             Model model) {
        Page<Event> eventPage = this.eventService.getAllEventsPagination(PageRequest.of(page, pageSize));
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        return "event/event-list";
    }

    @GetMapping("/create")
    public String showFormAddEvent(Model model) {
        model.addAttribute("eventForm", new EventForm());
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        return "event/add-event";
    }

    @PostMapping("/create")
    public String saveEvent(@Valid @ModelAttribute("eventForm") EventForm eventForm,
                            BindingResult bindingResult,
                            @RequestParam("coverPhotoFile") MultipartFile coverPhotoFile,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
            return "event/add-event";
        }

        Event event = new Event();
        event.setTitle(eventForm.getTitle());
        event.setTicketsNumber(eventForm.getTicketsNumber());
        event.setStartDate(eventForm.getStartDate());
        event.setEndDate(eventForm.getEndDate());
        event.setStartTime(eventForm.getStartTime());
        event.setEndTime(eventForm.getEndTime());
        event.setLocationName(eventForm.getLocationName());
        event.setLocationAddress(eventForm.getLocationAddress());
        event.setLocationLatitude(eventForm.getLocationLatitude());
        event.setLocationLongitude(eventForm.getLocationLongitude());
        event.setLocationPhone(eventForm.getLocationPhone());
        event.setLocationEmail(eventForm.getLocationEmail());
        event.setIsPublic(eventForm.getIsPublic());
        event.setEventType(eventForm.getEventType());

        if (!coverPhotoFile.isEmpty()) {
            String fileName = coverPhotoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            try {
                Files.write(filePath, coverPhotoFile.getBytes());
                event.setCoverPhoto(fileName);
            } catch (IOException e) {
                logger.error("Error saving cover photo", e);
            }
        } else {
            event.setCoverPhoto(eventForm.getCoverPhoto());
        }

        eventService.addEvent(event);

        List<Ticket> tickets = eventForm.getTickets().stream().map(ticketForm -> {
            Ticket ticket = new Ticket();
            ticket.setTicketType(ticketForm.getTicketType());
            ticket.setDescription(ticketForm.getDescription());
            ticket.setPrice(ticketForm.getPrice());
            ticket.setQuantity(ticketForm.getQuantity());
            ticket.setEvent(event);
            return ticket;
        }).collect(Collectors.toList());

        tickets.forEach(ticketService::addTicket);

        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        EventForm eventForm = new EventForm();
        eventForm.setTitle(event.getTitle());
        eventForm.setTicketsNumber(event.getTicketsNumber());
        eventForm.setStartDate(event.getStartDate());
        eventForm.setEndDate(event.getEndDate());
        eventForm.setStartTime(event.getStartTime());
        eventForm.setEndTime(event.getEndTime());
        eventForm.setDescription(event.getDescription());
        eventForm.setCoverPhoto(event.getCoverPhoto());
        eventForm.setLocationName(event.getLocationName());
        eventForm.setLocationAddress(event.getLocationAddress());
        eventForm.setLocationLatitude(event.getLocationLatitude());
        eventForm.setLocationLongitude(event.getLocationLongitude());
        eventForm.setLocationPhone(event.getLocationPhone());
        eventForm.setLocationEmail(event.getLocationEmail());
        eventForm.setIsPublic(event.getIsPublic());
        eventForm.setEventType(event.getEventType());
        eventForm.setTickets(event.getTickets().stream().map(ticket -> {
            TicketForm ticketForm = new TicketForm();
            ticketForm.setTicketType(ticket.getTicketType());
            ticketForm.setDescription(ticket.getDescription());
            ticketForm.setPrice(ticket.getPrice());
            ticketForm.setQuantity(ticket.getQuantity());
            ticketForm.setEventId(ticket.getEvent().getId());
            return ticketForm;
        }).collect(Collectors.toList()));

        model.addAttribute("eventForm", eventForm);
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        model.addAttribute("id", id);
        return "event/edit-event";
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                              @Valid @ModelAttribute("eventForm") EventForm eventForm,
                              BindingResult bindingResult,
                              @RequestParam("coverPhotoFile") MultipartFile coverPhotoFile,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
            return "event/edit-event";
        }

        Event event = eventService.getEventById(id);
        event.setTitle(eventForm.getTitle());
        event.setTicketsNumber(eventForm.getTicketsNumber());
        event.setStartDate(eventForm.getStartDate());
        event.setEndDate(eventForm.getEndDate());
        event.setStartTime(eventForm.getStartTime());
        event.setEndTime(eventForm.getEndTime());
        event.setLocationName(eventForm.getLocationName());
        event.setLocationAddress(eventForm.getLocationAddress());
        event.setLocationLatitude(eventForm.getLocationLatitude());
        event.setLocationLongitude(eventForm.getLocationLongitude());
        event.setLocationPhone(eventForm.getLocationPhone());
        event.setLocationEmail(eventForm.getLocationEmail());
        event.setIsPublic(eventForm.getIsPublic());
        event.setEventType(eventForm.getEventType());

        if (!coverPhotoFile.isEmpty()) {
            String fileName = coverPhotoFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            try {
                Files.write(filePath, coverPhotoFile.getBytes());
                if (event.getCoverPhoto() != null) {
                    Path oldFilePath = Paths.get(uploadDirectory, event.getCoverPhoto());
                    Files.deleteIfExists(oldFilePath);
                }
                event.setCoverPhoto(fileName);
            } catch (IOException e) {
                logger.error("Error saving cover photo", e);
            }
        } else {
            event.setCoverPhoto(eventForm.getCoverPhoto());
        }

        eventService.updateEvent(event);
        return "redirect:/events";
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event.getCoverPhoto() != null) {
            Path filePath = Paths.get(uploadDirectory, event.getCoverPhoto());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                logger.error("Error deleting cover photo", e);
            }
        }
        eventService.deleteEventById(id);
        return "redirect:/events";
    }
}