package com.tekup.gatherwise.dao.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
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
  private int price;
  private int quantity;

  @ManyToOne(optional = true)
  @JoinColumn(name = "event_id", nullable = true)
  private Event event;

  public Ticket(Long id, String ticketType, int price, int quantity) {
    this.id = id;
    this.ticketType = ticketType;
    this.price = price;
    this.quantity = quantity;
  }
}