package com.tekup.gatherwise.dao.entities;

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
    @Column(name="event_title",length = 30, nullable=false)
    private String title;
    private String description;
    private int ticketsNumber;
    private Date startDate;
    private Date endDate;
    private String startTime;
    private String endTime;
    private Date creationDate;
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

    public Event(Long id, String title, String description, Date startDate, Date endDate, String startTime, String endTime, String coverPhoto, String locationName, String locationAddress, String locationLatitude, String locationLongitude, String locationPhone, String locationEmail, Boolean isPublic, Boolean isArchived) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    }

}