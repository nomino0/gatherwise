package com.tekup.gatherwise.business.services;

import com.tekup.gatherwise.dao.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<Reservation> getAllReservations();

    Reservation getReservationById(Long id);

    List<Reservation> getReservationsByAttendeeEmail(String email);

    List<Reservation> getReservationsByPhoneNumber(String phoneNumber);

    Page<Reservation> getAllReservationsPagination(Pageable pageable);

    List<Reservation> getReservationsByTimestampRange(LocalDateTime start, LocalDateTime end);

    Reservation getReservationByBarcode(String barcode);

    Page<Reservation> getReservationsByTicketId(Long ticketId, Pageable pageable);

    List<Reservation> getReservationsByTicketId(Long ticketId);

    Reservation addReservation(Reservation reservation);

    Reservation updateReservation(Reservation reservation);

    void deleteReservationById(Long id);

    List<Reservation> getReservationsByReservationTimeStampBetween(LocalDateTime start, LocalDateTime end); // Updated method signature
}
