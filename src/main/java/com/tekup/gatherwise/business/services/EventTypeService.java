package com.tekup.gatherwise.business.services;

import java.util.List;

import com.tekup.gatherwise.dao.entities.EventType;

public interface EventTypeService {
    EventType addEventType(EventType eventType);
    EventType getEventTypeById(Long id);
    List<EventType> getAllEventTypes();
    void deleteEventType(Long id);
    EventType updateEventType(Long id, EventType eventType);
}