package com.tekup.gatherwise.dao.repositories;

import com.tekup.gatherwise.dao.entities.Reservation;
import com.tekup.gatherwise.dao.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAttendeeEmailContaining(String email);

    List<Reservation> findByPhoneNumberContaining(String phoneNumber);

    Optional<Reservation> findByBarcode(String barcode);

    Page<Reservation> findByTicketId(Long ticketId, Pageable pageable);

    List<Reservation> findByReservationTimeStampBetween(LocalDateTime start, LocalDateTime end);

    List<Reservation> findByTicket(Ticket ticket);
}
