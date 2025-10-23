package com.ty.BiteRush.service.impl;
import com.ty.BiteRush.entity.*;
import com.ty.BiteRush.repository.*;
import com.ty.BiteRush.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepo;
  private final FoodItemRepository foodRepo;

  @Override
  public Cart getActiveCart(User user){
    return cartRepo.findByUser(user).orElseGet(() -> cartRepo.save(Cart.builder().user(user).build()));
  }

  @Override
  public void addToCart(User user, Long foodId, int qty){
    Cart cart = getActiveCart(user);
    FoodItem fi = foodRepo.findById(foodId).orElseThrow();
    Optional<CartItem> existing = cart.getItems().stream()
      .filter(ci -> ci.getFoodItem().getId().equals(foodId)).findFirst();
    if(existing.isPresent()){
      CartItem ci = existing.get(); ci.setQuantity(ci.getQuantity()+qty);
    } else {
      CartItem ci = CartItem.builder().cart(cart).foodItem(fi).quantity(qty).build();
      cart.getItems().add(ci);
    }
  }

  @Override
  public void updateItem(User user, Long cartItemId, int qty){
    Cart cart = getActiveCart(user);
    cart.getItems().stream().filter(ci -> ci.getId().equals(cartItemId)).findFirst()
      .ifPresent(ci -> { if(qty<=0) cart.getItems().remove(ci); else ci.setQuantity(qty); });
  }

  @Override
  public void removeItem(User user, Long cartItemId){
    Cart cart = getActiveCart(user);
    cart.getItems().removeIf(ci -> ci.getId().equals(cartItemId));
  }

  @Override
  public double cartTotal(Cart cart){
    return cart.getItems().stream().mapToDouble(CartItem::getLineTotal).sum();
  }

  @Override
  public void clear(Cart cart){ cart.getItems().clear(); }
}
