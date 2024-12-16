package com.tekup.gatherwise.business.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;

public interface EventService {
    // Read operations
    List<Event> getAllEvents();
    Event getEventById(Long id);
    List<Event> getEventsByTitle(String title);
    List<Event> getEventsSortedByStartDate(String order);
    Page<Event> getAllEventsPagination(Pageable pageable);
    Page<Event> getEventsSortedByDatePagination(String order, Pageable pageable);
    List<Event> getEventsByPublicStatus(Boolean isPublic);
    List<Event> getEventsByEventType(EventType eventType);
    Page<Event> getEventsByTitle(String title, Pageable pageable);
    Page<Event> getEventsByPublicStatusPagination(Boolean isPublic, Pageable pageable);
    Page<Event> getEventsByArchiveStatusPagination(Boolean isArchived, Pageable pageable);
    Page<Event> getEventsByEventTypePagination(EventType eventType, Pageable pageable);
    Page<Event> getEventsByPublicStatusAndEventTypePagination(Boolean isPublic, EventType eventType, Pageable pageable);

    // Create
    Event addEvent(Event event);

    // Update
    Event updateEvent(Event event);

    // Delete
    void deleteEventById(Long id);
}