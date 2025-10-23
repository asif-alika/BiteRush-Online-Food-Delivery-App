package com.ty.BiteRush.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne
  private FoodItem foodItem;

  private int quantity;

  public double getLineTotal() { return foodItem.getPrice() * quantity; }
}
