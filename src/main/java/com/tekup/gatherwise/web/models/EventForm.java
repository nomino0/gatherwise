package com.tekup.gatherwise.web.models;

import com.tekup.gatherwise.dao.entities.EventType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventForm {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull
    @Min(value = 1, message = "Tickets number must be at least 1")
    private int ticketsNumber;
    @NotNull(message = "Date is required")
    private Date startDate;
    @NotNull(message = "Start time is required")
    private Date endDate;
    @NotNull(message = "End date is required")
    private String startTime;
    @NotNull(message = "End time is required")
    private String endTime;
    @NotNull(message = "Description is required")
    private String description;
    private String coverPhoto;
    private String locationName;
    private String locationAddress;
    private String locationLatitude;
    private String locationLongitude;
    private String locationPhone;
    private String locationEmail;
    private Boolean isPublic;
    @NotNull(message = "Event type is required")
    private EventType eventType;
    private List<TicketForm> tickets;
}