package com.ty.BiteRush.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // ✅ Deletes automatically if food is deleted
    private FoodItem foodItem;

    private int quantity;
    private double priceAtPurchase;
}
