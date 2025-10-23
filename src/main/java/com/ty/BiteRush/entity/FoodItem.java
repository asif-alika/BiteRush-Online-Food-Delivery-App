package com.ty.BiteRush.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FoodItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(length = 1000)
  private String description;
  private double price;
  private String imageUrl;
  private boolean available = true;
}
