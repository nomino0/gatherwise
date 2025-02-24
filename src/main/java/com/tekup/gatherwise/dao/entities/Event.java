package com.tekup.gatherwise.dao.entities;

import com.tekup.gatherwise.business.services.TicketService;
import jakarta.persistence.*;
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

@Entity
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="event_title", nullable=false)
    private String title;
    private String description;
    private Date creationDate;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;
    private String coverPhoto;
    private String locationName;
    private String locationAddress;
    private String locationLatitude;
    private String locationLongitude;
    private String locationPhone;
    private String locationEmail;
    private Boolean isPublic;
    private Boolean isArchived;



    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<Ticket> tickets;


   @ManyToOne(optional = true)
    @JoinColumn(name = "event_type_id", nullable = true)


    private EventType eventType;

    public Event(Long id, String title, String description, Date creationDate, Date startDate, Date endDate, String startTime, String endTime, String coverPhoto, String locationName, String locationAddress, String locationLatitude, String locationLongitude, String locationPhone, String locationEmail, Boolean isPublic, Boolean isArchived, EventType EventType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.coverPhoto = coverPhoto;
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.locationPhone = locationPhone;
        this.locationEmail = locationEmail;
        this.isPublic = isPublic;
        this.isArchived = isArchived;
        this.eventType = EventType;
    }

    public int getTotalTicketTypes() {
        return tickets != null ? tickets.size() : 0;
    }

    public int getTotalTickets() {
        return tickets != null ? tickets.stream().mapToInt(Ticket::getQuantity).sum() : 0;
    }

    public int getSmallestTicketPrice() {
        return tickets != null && !tickets.isEmpty() ? tickets.stream().mapToInt(Ticket::getPrice).min().orElse(0) : 0;
    }

    public int getTotalSoldTickets(TicketService ticketService) {
        return tickets != null ? tickets.stream().mapToInt(ticketService::getSoldCount).sum() : 0;
    }

    public String getDuration() {
        long durationInMillis = (endDate.getTime() + endTimeInMillis(endTime)) - (startDate.getTime() + startTimeInMillis(startTime));
        long durationInMinutes = durationInMillis / (1000 * 60);
        long durationInHours = durationInMinutes / 60;
        long durationInDays = durationInHours / 24;

        if (durationInDays > 0) {
            return durationInDays + " days " + (durationInHours % 24) + " hours " + (durationInMinutes % 60) + " minutes";
        } else if (durationInHours > 0) {
            if (durationInMinutes % 60 == 0) {
                return durationInHours == 1 ? "1 hour" : durationInHours + " hours";
            } else {
                return durationInHours + " hours " + (durationInMinutes % 60) + " minutes";
            }
        } else {
            return durationInMinutes + " minutes";
        }
    }

    private long startTimeInMillis(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }

    private long endTimeInMillis(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }
}