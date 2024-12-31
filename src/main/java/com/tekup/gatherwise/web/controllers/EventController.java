package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.dao.entities.EventType;
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
import java.util.Map;
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
        return "event/event-list";
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
        Sort sort = Sort.by(direction, sortBy != null ? sortBy : "startDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> eventPage;
        if (search != null && !search.isEmpty()) {
            eventPage = eventService.getEventsByTitle(search, pageable);
        } else if (isPublic != null && eventTypeId != null) {
            eventPage = eventService.getEventsByPublicStatusAndEventTypePagination(isPublic, new EventType(eventTypeId), pageable);
        } else if (isPublic != null) {
            eventPage = eventService.getEventsByPublicStatusPagination(isPublic, pageable);
        } else if (isArchived != null) {
            eventPage = eventService.getEventsByArchiveStatusPagination(isArchived, pageable);
        } else if (eventTypeId != null) {
            eventPage = eventService.getEventsByEventTypePagination(new EventType(eventTypeId), pageable);
        } else {
            eventPage = eventService.getAllEventsPagination(pageable);
        }

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("isPublic", isPublic);
        model.addAttribute("isArchived", isArchived);
        model.addAttribute("search", search);
        model.addAttribute("eventTypeId", eventTypeId);

        return "event/event-list";
    }

    @GetMapping("/create")
    public String showAddEventForm(Model model) {
        model.addAttribute("eventForm", new EventForm());
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        return "event/add-event";
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String addEvent(@Valid @ModelAttribute EventForm eventForm,
                           BindingResult bindingResult,
                           Model model,
                           @RequestParam MultipartFile coverPhotoFile) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
            model.addAttribute("error", "Invalid input");
            return "event/add-event";
        }
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        eventForm.setCreationDate(new Date(System.currentTimeMillis()));

        Event event = new Event(null, eventForm.getTitle(), eventForm.getDescription(), eventForm.getCreationDate(),
                eventForm.getStartDate(), eventForm.getEndDate(), eventForm.getStartTime(), eventForm.getEndTime(),
                coverPhotoFile.isEmpty() ? null : coverPhotoFile.getOriginalFilename(),
                eventForm.getLocationName(), eventForm.getLocationAddress(), eventForm.getLocationLatitude(),
                eventForm.getLocationLongitude(), eventForm.getLocationPhone(), eventForm.getLocationEmail(),
                eventForm.getIsPublic(), false, eventForm.getEventType());

        if (!coverPhotoFile.isEmpty()) {
            StringBuilder fileName = new StringBuilder();
            fileName.append(coverPhotoFile.getOriginalFilename());
            Path newFilePath = Paths.get(uploadDirectory, fileName.toString());
            try {
                Files.write(newFilePath, coverPhotoFile.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        event = this.eventService.addEvent(event);

        for (TicketForm ticketForm : eventForm.getTickets()) {
            Ticket ticket = new Ticket(null, ticketForm.getTicketType(), ticketForm.getDescription(),
                    ticketForm.getPrice(), ticketForm.getQuantity(), event, 0);
            this.ticketService.addTicket(ticket);
        }

        return "redirect:/events";
    }

    private List<TicketForm> convertToTicketFormList(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticket -> new TicketForm(ticket.getTicketType(), ticket.getDescription(), ticket.getPrice(), ticket.getQuantity(), ticket.getEvent(), ticket.getId()))
                .collect(Collectors.toList());
    }

    @RequestMapping("/{id}/edit")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        Ticket ticket = ticketService.getTicketsByEventId(id, PageRequest.of(0, 1)).getContent().get(0);
        List<TicketForm> ticketForms = convertToTicketFormList(event.getTickets());
        model.addAttribute("eventForm", new EventForm(event.getTitle(), event.getDescription(),event.getCreationDate() ,  event.getStartDate(), event.getEndDate(), event.getStartTime(), event.getEndTime(), event.getCoverPhoto(), event.getLocationName(), event.getLocationAddress(), event.getLocationLatitude(), event.getLocationLongitude(), event.getLocationPhone(), event.getLocationEmail(), event.getIsPublic(), event.getIsArchived(), event.getEventType(), ticketForms));
        model.addAttribute("ticketForm", new TicketForm(ticket.getTicketType(), ticket.getDescription(), ticket.getPrice(), ticket.getQuantity(), ticket.getEvent(), ticket.getId()));
        model.addAttribute("id", id);
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());

        return "event/edit-event";
    }

    @RequestMapping(path = "/{id}/edit" , method = RequestMethod.POST)
    public String updateEvent(@PathVariable Long id,
                              @Valid @ModelAttribute EventForm eventForm,
                              BindingResult bindingResult,
                              @RequestParam MultipartFile coverPhotoFile,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
            return "event/edit-event";
        }

        Event event = eventService.getEventById(id);
        event.setTitle(eventForm.getTitle());
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
        event.setIsArchived(eventForm.getIsArchived());
        event.setEventType(eventForm.getEventType());

        if (!coverPhotoFile.isEmpty()) {
            // upload photo
            StringBuilder fileName = new StringBuilder();
            Path newFilePath = Paths.get(uploadDirectory, coverPhotoFile.getOriginalFilename());
            fileName.append(coverPhotoFile.getOriginalFilename());
            try {
                Files.write(newFilePath, coverPhotoFile.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // delete old cover photo
            if (event.getCoverPhoto() != null) {
                Path filePath = Paths.get(uploadDirectory, event.getCoverPhoto());
                try {
                    Files.deleteIfExists(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            event.setCoverPhoto(fileName.toString());
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