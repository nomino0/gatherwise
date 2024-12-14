package com.tekup.gatherwise.web.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketForm {
    @NotBlank(message = "Ticket type is required")
    private String ticketType;
    @NotBlank(message = "Short Description is required")
    private String description;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be at least 0")
    private int price;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    private Long eventId;
}