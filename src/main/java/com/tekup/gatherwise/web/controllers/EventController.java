package com.tekup.gatherwise.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;
import com.tekup.gatherwise.web.models.EventForm;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;

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
        EventForm eventForm = new EventForm();
        model.addAttribute("eventForm", eventForm);
        model.addAttribute("eventTypes", eventTypeService.getAllEventTypes());
        return "event/add-event"; // Name of the HTML/Thymeleaf file for the event addition form
    }

    @PostMapping("/create")
    public String saveEvent(@Valid @ModelAttribute("eventForm") EventForm eventForm,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "event/add-event";
        }
        Event event = new Event();
        // Map fields from eventForm to event
        event.setTitle(eventForm.getTitle());
        event.setTicketsNumber(eventForm.getTicketsNumber());
        event.setDate(eventForm.getDate());
        event.setCoverPhoto(eventForm.getCoverPhoto());
        event.setLocationName(eventForm.getLocationName());
        event.setLocationAddress(eventForm.getLocationAddress());
        event.setLocationLatitude(eventForm.getLocationLatitude());
        event.setLocationLongitude(eventForm.getLocationLongitude());
        event.setLocationPhone(eventForm.getLocationPhone());
        event.setLocationEmail(eventForm.getLocationEmail());
        event.setIsPublic(eventForm.getIsPublic());
        event.setEventType(eventForm.getEventType());

        eventService.addEvent(event);
        return "redirect:/events"; // Redirect to the list of events after adding
    }

    @GetMapping("/{id}/edit")
    public String updateEvent(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "event/eventEditForm"; // Name of the HTML/Thymeleaf file for the event update form
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                              @Valid @ModelAttribute("event") Event event,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "event/eventEditForm";
        }
        eventService.updateEvent(event);
        return "redirect:/events"; // Redirect to the list of events after updating
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEventById(id);
        return "redirect:/events"; // Redirect to the list of events after deleting
    }

    @GetMapping("/filter")
    public String filterEvents(@RequestParam(required = false) Boolean isPublic,
                               @RequestParam(required = false) Long eventTypeId,
                               @RequestParam(required = false, defaultValue = "date") String sortBy,
                               @RequestParam(required = false, defaultValue = "asc") String order,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               Model model) {
        Page<Event> eventPage;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.fromString(order), sortBy));

        if (isPublic != null) {
            eventPage = eventService.getEventsByPublicStatusPagination(isPublic, pageable);
        } else if (eventTypeId != null) {
            EventType eventType = eventTypeService.getEventTypeById(eventTypeId);
            eventPage = eventService.getEventsByEventTypePagination(eventType, pageable);
        } else {
            eventPage = eventService.getAllEventsPagination(pageable);
        }

        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", order);
        model.addAttribute("isPublic", isPublic);
        model.addAttribute("eventTypeId", eventTypeId);

        return "event/eventList"; // Name of the HTML/Thymeleaf file to display the filtered list of events
    }
}