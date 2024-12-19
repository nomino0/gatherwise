package com.tekup.gatherwise.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attendee_name", nullable = false)
    private String attendeeName;

    @Column(name = "attendee_email", nullable = false)
    private String attendeeEmail;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "reservation_time_stamp", nullable = false)
    private LocalDateTime reservationTimeStamp;

    @Column(name = "barcode", nullable = false, unique = true)
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    public Reservation(Long id, String attendeeName, String attendeeEmail, String phoneNumber, String barcode, LocalDateTime reservationTimeStamp) {
        this.id = id;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
        this.phoneNumber = phoneNumber;
        this.barcode = barcode;
        this.reservationTimeStamp = reservationTimeStamp;
    }
}
