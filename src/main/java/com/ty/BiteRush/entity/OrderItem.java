package com.ty.BiteRush.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne @JoinColumn(name = "order_id")
  private Order order;
  @ManyToOne
  private FoodItem foodItem;
  private int quantity;
  private double priceAtPurchase;
}
