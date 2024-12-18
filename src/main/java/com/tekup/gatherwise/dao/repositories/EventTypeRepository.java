package com.tekup.gatherwise.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.gatherwise.dao.entities.EventType;

import java.util.Optional;

public interface EventTypeRepository extends JpaRepository<EventType,Long> {

    Optional<Object> findByTypeName(String typeName);
}