package com.tekup.gatherwise.business.services;

import com.tekup.gatherwise.dao.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    List<Ticket> getTicketsByTicketType(String ticketType);
    Page<Ticket> getTicketsByEventId(Long eventId, Pageable pageable);
    List<Ticket> getTicketsByEventId(Long eventId);
    Ticket addTicket(Ticket ticket);
    Ticket updateTicket(Ticket ticket);
    void deleteTicketById(Long id);
    int getSoldCount(Ticket ticket);

}