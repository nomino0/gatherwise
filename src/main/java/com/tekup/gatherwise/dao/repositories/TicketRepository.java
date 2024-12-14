package com.tekup.gatherwise.dao.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.gatherwise.dao.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByTicketTypeContaining(String ticketType);
    Page<Ticket> findByEventId(Long eventId, Pageable pageable);
}