package com.tekup.gatherwise.web.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationForm {

    @NotBlank(message = "Attendee name is required")
    private String attendeeName;

    @NotBlank(message = "Attendee email is required")
    @Email(message = "Email should be valid")
    private String attendeeEmail;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private LocalDateTime reservationTimeStamp;
    private String barcode;
    private Long ticketId;
}