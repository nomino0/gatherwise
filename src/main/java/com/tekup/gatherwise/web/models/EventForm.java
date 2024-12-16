package com.tekup.gatherwise.web.models;

import com.tekup.gatherwise.dao.entities.EventType;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Short Description is required")
    private String description;

    private Date creationDate;

    @NotNull(message = "Date is required")
    private Date startDate;
    @NotNull(message = "End date is required")
    private Date endDate;
    @NotBlank(message = "Starting time is required")
    private String startTime;
    @NotBlank(message = "Ending time is required")
    private String endTime;
    private String coverPhoto;
    @NotBlank(message = "Location name is required")
    private String locationName;
    @NotBlank(message = "Location address is required")
    private String locationAddress;
    @NotBlank(message = "Location latitude is required")
    private String locationLatitude;

    @NotBlank(message = "Location longitude is required")
    private String locationLongitude;

    private String locationPhone;
    private String locationEmail;
    private Boolean isPublic;
    private Boolean isArchived;
    @NotNull(message = "Event type is required")
    private EventType eventType;
    private List<TicketForm> tickets;
}