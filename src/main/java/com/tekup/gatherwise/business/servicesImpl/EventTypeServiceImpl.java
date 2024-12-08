package com.tekup.gatherwise.business.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.tekup.gatherwise.business.services.EventTypeService;
import com.tekup.gatherwise.dao.entities.EventType;
import com.tekup.gatherwise.dao.repositories.EventTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventTypeServiceImpl implements EventTypeService {
    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Override
    public EventType addEventType(EventType eventType) {
        if(eventType == null){
            return null;
        }
        EventType newEventType = new EventType();
        try {
            newEventType = eventTypeRepository.save(eventType);
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
        }
        return newEventType;
    }

    @Override
    public EventType getEventTypeById(Long id) {
        if(id == null){
            return null;
        }
        return this.eventTypeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event Type with id: " + id + " not found"));
    }

    @Override
    public List<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }

    @Override
    public void deleteEventType(Long id) {
        if(id == null){
            return;
        } else if(this.eventTypeRepository.existsById(id)){
            this.eventTypeRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Event Type with id: " + id + " not found");
        }
    }

    @Override
    public EventType updateEventType(Long id, EventType eventType) {
        EventType existingEventType = this.getEventTypeById(id);
        existingEventType.setTypeName(eventType.getTypeName());
        existingEventType.setDescription(eventType.getDescription());
        return eventTypeRepository.save(existingEventType);
    }
}