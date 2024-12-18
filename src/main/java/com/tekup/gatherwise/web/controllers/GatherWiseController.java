package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
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

    @GetMapping("/faq")
    public String getFaqPage(Model model) {
        var eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventType.getEvents().stream()
                        .anyMatch(event -> event.getIsPublic() && !event.getIsArchived()))
                .collect(Collectors.toList());

        model.addAttribute("eventTypes", eventTypes);
        return "faq";
    }

    @GetMapping("/explore")
    public String getExplorePage(Model model) {
        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        model.addAttribute("eventTypes", eventTypes);
        return "explore";
    }

    @GetMapping("/explore/archive")
    public String getArchivePage(@RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "category", required = false) Long categoryId,
                                 Model model) {
        List<Event> archivedEvents = eventService.getAllEvents().stream()
                .filter(event -> event.getIsArchived() && event.getIsPublic())
                .filter(event -> (query == null || event.getTitle().toLowerCase().contains(query.toLowerCase())))
                .filter(event -> (categoryId == null || event.getEventType().getId().equals(categoryId)))
                .collect(Collectors.toList());

        List<EventType> eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventType.getEvents().stream()
                        .anyMatch(event -> event.getIsArchived() && event.getIsPublic()))
                .collect(Collectors.toList());


        model.addAttribute("archivedEvents", archivedEvents);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("query", query);
        model.addAttribute("categoryId", categoryId);
        return "archive";
    }

    @GetMapping("/search")
    public String searchArchivedEvents(@RequestParam(value = "query", required = false) String query,
                                       @RequestParam(value = "category", required = false) Long categoryId,
                                       Model model) {
        List<Event> archivedEvents = eventService.getAllEvents().stream()
                .filter(event -> event.getIsArchived() && event.getIsPublic())
                .filter(event -> (query == null || event.getTitle().toLowerCase().contains(query.toLowerCase())))
                .filter(event -> (categoryId == null || event.getEventType().getId().equals(categoryId)))
                .collect(Collectors.toList());

        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        model.addAttribute("archivedEvents", archivedEvents);
        model.addAttribute("eventTypes", eventTypes);
        return "archive";
    }

    @GetMapping("/explore/{typeName}")
    public String getEventsByType(@PathVariable String typeName, Model model) {
        EventType eventType = eventTypeService.getEventTypeByName(typeName);
        List<Event> events = eventService.getEventsByType(eventType);
        model.addAttribute("events", events);
        model.addAttribute("eventType", eventType);
        return "events-by-type";
    }

    @GetMapping("/explore/{typeName}/{eventName}")
    public String getEventDetails(@PathVariable String typeName, @PathVariable String eventName, Model model) {
        EventType eventType = eventTypeService.getEventTypeByName(typeName);
        Event event = eventService.getEventByNameAndType(eventName, eventType);
        model.addAttribute("event", event);
        model.addAttribute("eventType", eventType);
        return "event-details";
    }



    @ModelAttribute
    public void addAttributes(Model model) {
        List<Event> archivedEvents = eventService.getAllEvents().stream()
                .filter(event -> event.getIsArchived() && event.getIsPublic())
                .collect(Collectors.toList());

        List<EventType> eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventType.getEvents().stream()
                        .anyMatch(event -> event.getIsPublic() && !event.getIsArchived()))
                .collect(Collectors.toList());

        List<EventType> topEventTypes = eventTypes.stream()
                .sorted(Comparator.comparingInt((EventType e) -> e.getEvents().size()).reversed())
                .limit(3)
                .collect(Collectors.toList());

        model.addAttribute("archivedEvents", archivedEvents);
        model.addAttribute("topEventTypes", topEventTypes);
    }
}