package com.tekup.gatherwise.business.servicesImpl;

import com.tekup.gatherwise.business.services.TicketService;
import com.tekup.gatherwise.dao.entities.Ticket;
import com.tekup.gatherwise.dao.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ticket> getTicketsByTicketType(String ticketType) {
        return ticketRepository.findByTicketTypeContaining(ticketType);
    }

    @Override
    public Page<Ticket> getTicketsByEventId(Long eventId, Pageable pageable) {
        return ticketRepository.findByEventId(eventId, pageable);
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId, Pageable.unpaged()).getContent();
    }

    @Override
    public Ticket addTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }
}