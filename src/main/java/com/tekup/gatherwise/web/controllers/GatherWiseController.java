package com.tekup.gatherwise.web.controllers;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.business.services.TicketService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;
import com.tekup.gatherwise.dao.entities.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class GatherWiseController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private TicketController ticketController;

    //for home page
    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        Map<Event, Long> eventReservationCounts = eventService.getEventReservationCounts();

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

    //for faq page

    @GetMapping("/faq")
    public String getFaqPage(Model model) {
        var eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventType.getEvents().stream()
                        .anyMatch(event -> event.getIsPublic() && !event.getIsArchived()))
                .collect(Collectors.toList());

        model.addAttribute("eventTypes", eventTypes);
        return "faq";
    }


    //for explore page

    @GetMapping("/explore")
    public String getExplorePage(@RequestParam(value = "query", required = false) String query,
                                 @RequestParam(value = "category", required = false) Long categoryId,
                                 @RequestParam(value = "sort", required = false, defaultValue = "1") int sort,
                                 Model model) {
        List<Event> events = eventService.getAllEvents().stream()
                .filter(event -> event.getIsPublic() && !event.getIsArchived())
                .filter(event -> (query == null || event.getTitle().toLowerCase().contains(query.toLowerCase())))
                .filter(event -> (categoryId == null || event.getEventType().getId().equals(categoryId)))
                .sorted(getComparator(sort))
                .collect(Collectors.toList());

        Map<EventType, List<Event>> eventsByType = events.stream()
                .collect(Collectors.groupingBy(Event::getEventType));

        List<EventType> eventTypes = eventTypeService.getAllEventTypes().stream()
                .filter(eventType -> eventsByType.containsKey(eventType))
                .collect(Collectors.toList());

        model.addAttribute("eventsByType", eventsByType);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("query", query);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        return "explore";
    }

    private Comparator<Event> getComparator(int sort) {
        return switch (sort) {
            case 2 -> Comparator.comparing(Event::getSmallestTicketPrice);
            case 3 -> Comparator.comparing(Event::getSmallestTicketPrice).reversed();
            default -> Comparator.comparing(Event::getStartDate).thenComparing(Event::getStartTime);
        };
    }

    @GetMapping("/search-explore")
    public String searchExploreEvents(@RequestParam(value = "query", required = false) String query,
                                      @RequestParam(value = "category", required = false) Long categoryId,
                                      @RequestParam(value = "sort", required = false, defaultValue = "1") int sort,
                                      Model model) {
        List<Event> events = eventService.getAllEvents().stream()
                .filter(event -> event.getIsPublic() && !event.getIsArchived())
                .filter(event -> (query == null || event.getTitle().toLowerCase().contains(query.toLowerCase())))
                .filter(event -> (categoryId == null || event.getEventType().getId().equals(categoryId)))
                .sorted(getComparator(sort))
                .collect(Collectors.toList());

        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        model.addAttribute("events", events);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("query", query);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
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
                .map(eventType -> {
                    List<Event> filteredEvents = eventType.getEvents().stream()
                            .filter(event -> event.getIsArchived() && event.getIsPublic())
                            .collect(Collectors.toList());
                    eventType.setEvents(filteredEvents);
                    return eventType;
                })
                .filter(eventType -> !eventType.getEvents().isEmpty())
                .collect(Collectors.toList());

        model.addAttribute("archivedEvents", archivedEvents);
        model.addAttribute("eventTypes", eventTypes);
        model.addAttribute("query", query);
        model.addAttribute("categoryId", categoryId);
        return "archive";
    }

    @GetMapping("/search-archive")
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


    @Autowired
    private TicketService ticketService;

    @GetMapping("/explore/{id}")
    public String showEventDetails(@PathVariable Long id, Model model) {
        // Fetch event details
        Event event = eventService.getEventById(id);
        if (event == null) {
            return "errors"; // Return an error page if the event is not found
        }

        // Fetch ticket details
        List<Ticket> tickets = ticketService.getTicketsByEventId(id);
        Map<Long, Integer> ticketQuantities = tickets.stream()
                .collect(Collectors.toMap(Ticket::getId, ticket -> 0));

        // Add attributes to the model
        model.addAttribute("event", event);
        model.addAttribute("tickets", tickets);
        model.addAttribute("ticketQuantities", ticketQuantities);

        return "event-details";
    }





}