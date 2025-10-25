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
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // ✅ If FoodItem is deleted, remove this too
    private FoodItem foodItem;

    private int quantity;

    public double getLineTotal() {
        return foodItem.getPrice() * quantity;
    }
}
