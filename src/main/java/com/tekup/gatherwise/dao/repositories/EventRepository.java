package com.tekup.gatherwise.dao.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.gatherwise.dao.entities.Event;
import com.tekup.gatherwise.dao.entities.EventType;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTitleContaining(String title);
    List<Event> findByEventType(EventType eventType);
    Page<Event> findByIsPublic(Boolean isPublic, Pageable pageable);
    Page<Event> findByIsArchived(Boolean isArchived, Pageable pageable);
    Page<Event> findByEventType(EventType eventType, Pageable pageable);
    Page<Event> findByIsPublicAndEventType(Boolean isPublic, EventType eventType, Pageable pageable);
    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}