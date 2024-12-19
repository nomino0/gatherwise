package com.tekup.gatherwise.business.servicesImpl;

import com.tekup.gatherwise.business.services.ReservationService;
import com.tekup.gatherwise.dao.entities.Reservation;
import com.tekup.gatherwise.dao.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reservation> getReservationsByAttendeeEmail(String email) {
        return reservationRepository.findByAttendeeEmailContaining(email);
    }

    @Override
    public List<Reservation> getReservationsByPhoneNumber(String phoneNumber) {
        return reservationRepository.findByPhoneNumberContaining(phoneNumber);
    }

    @Override
    public Page<Reservation> getAllReservationsPagination(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    @Override
    public List<Reservation> getReservationsByTimestampRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByReservationTimeStampBetween(start, end);
    }

    @Override
    public Reservation getReservationByBarcode(String barcode) {
        return reservationRepository.findByBarcode(barcode).orElse(null);
    }

    @Override
    public Page<Reservation> getReservationsByTicketId(Long ticketId, Pageable pageable) {
        return reservationRepository.findByTicketId(ticketId, pageable);
    }

    @Override
    public List<Reservation> getReservationsByTicketId(Long ticketId) {
        return reservationRepository.findByTicketId(ticketId, Pageable.unpaged()).getContent();
    }



    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }


    @Override
    public List<Reservation> getReservationsByReservationTimeStampBetween(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByReservationTimeStampBetween(start, end); // Delegating properly to the repository
    }
}
