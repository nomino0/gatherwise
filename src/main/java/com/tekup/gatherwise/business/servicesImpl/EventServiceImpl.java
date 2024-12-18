package com.tekup.gatherwise.business.servicesImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tekup.gatherwise.dao.entities.Reservation;
import com.tekup.gatherwise.dao.entities.Ticket;
import com.tekup.gatherwise.dao.repositories.ReservationRepository;
import com.tekup.gatherwise.dao.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tekup.gatherwise.business.services.EventService;
import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;
import com.tekup.gatherwise.dao.repositories.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findByTitleContaining(title);
    }

    @Override
    public List<Event> getEventsSortedByStartDate(String order) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return eventRepository.findAll(Sort.by(direction, "startDate"));
    }

    @Override
    public Page<Event> getAllEventsPagination(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Page<Event> getEventsSortedByDatePagination(String order, Pageable pageable) {
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, "startDate"));
        return eventRepository.findAll(sortedPageable);
    }

    @Override
    public List<Event> getEventsByPublicStatus(Boolean isPublic) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE); // or any other default Pageable
        return eventRepository.findByIsPublic(isPublic, pageable).getContent();
    }

    @Override
    public List<Event> getEventsByEventType(EventType eventType) {
        return eventRepository.findByEventType(eventType);
    }


    @Override
    public Page<Event> getEventsByTitle(String title, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public Page<Event> getEventsByPublicStatusPagination(Boolean isPublic, Pageable pageable) {
        return eventRepository.findByIsPublic(isPublic, pageable);
    }

    @Override
    public Page<Event> getEventsByArchiveStatusPagination(Boolean isArchived, Pageable pageable) {
        return eventRepository.findByIsArchived(isArchived, pageable);
    }

    @Override
    public Page<Event> getEventsByEventTypePagination(EventType eventType, Pageable pageable) {
        return eventRepository.findByEventType(eventType, pageable);
    }

    @Override
    public Page<Event> getEventsByPublicStatusAndEventTypePagination(Boolean isPublic, EventType eventType, Pageable pageable) {
        return eventRepository.findByIsPublicAndEventType(isPublic, eventType, pageable);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteEventById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getEventsByType(EventType eventType) {
        return eventRepository.findByEventType(eventType);
    }

    @Override
    public Event getEventByNameAndType(String eventName, EventType eventType) {
        return eventRepository.findByTitleAndEventType(eventName, eventType);
    }

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Map<Event, Long> getEventReservationCounts() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getTicket().getEvent(), Collectors.counting()));
    }



}