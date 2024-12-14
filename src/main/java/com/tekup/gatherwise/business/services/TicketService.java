package com.tekup.gatherwise.business.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekup.gatherwise.dao.entities.Ticket;

public interface TicketService {
    // Read operations
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    List<Ticket> getTicketsByType(String ticketType);
    Page<Ticket> getAllTicketsPagination(Pageable pageable);
    Page<Ticket> getTicketsByEventId(Long eventId, Pageable pageable);

    // Create
    Ticket addTicket(Ticket ticket);

    // Update
    Ticket updateTicket(Ticket ticket);

    // Delete
    void deleteTicketById(Long id);
}