package com.tekup.gatherwise.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String ticketType;
  private String description;
  private int price;
  private int quantity;

  @ManyToOne(optional = true)
  @JoinColumn(name = "event_id", nullable = true)
  private Event event;

  public Ticket(Long id, String ticketType,String description , int price, int quantity, Event event) {
    this.id = id;
    this.ticketType = ticketType;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
    this.event = event;
  }
}