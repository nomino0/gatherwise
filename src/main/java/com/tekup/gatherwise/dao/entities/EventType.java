package com.tekup.gatherwise.dao.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="event_types")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="type_name", nullable = false,unique = true)
    private String typeName;
    private String description;

    @OneToMany(mappedBy = "eventType",cascade = CascadeType.ALL)

    private List<Event> events;

    public EventType(Long id, String typeName, String description) {
        this.id = id;
        this.typeName = typeName;
        this.description = description;
    }


    
}