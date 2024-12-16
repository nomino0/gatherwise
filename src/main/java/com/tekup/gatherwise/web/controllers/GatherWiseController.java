package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GatherWiseController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        List<Event> events = eventService.getAllEvents().stream()
                .filter(event -> event.getIsPublic() && !event.getIsArchived())
                .collect(Collectors.toList());

        List<EventType> eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventType.getEvents().stream()
                        .anyMatch(event -> event.getIsPublic() && !event.getIsArchived()))
                .collect(Collectors.toList());

        model.addAttribute("events", events);
        model.addAttribute("eventTypes", eventTypes);
        return "home";
    }
}