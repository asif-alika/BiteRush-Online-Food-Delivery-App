package com.ty.BiteRush.controller;

import com.ty.BiteRush.entity.User;
import com.ty.BiteRush.service.CartService;
import com.ty.BiteRush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
  private final CartService cartService;
  private final UserService userService;

  private User currentUser(String username){ return userService.getByUsername(username); }

  @PostMapping("/add/{foodId}")
  public String add(@AuthenticationPrincipal UserDetails ud, @PathVariable Long foodId, @RequestParam(defaultValue = "1") int qty){
    cartService.addToCart(currentUser(ud.getUsername()), foodId, qty); return "redirect:/menu?added";
  }

  @GetMapping
  public String view(@AuthenticationPrincipal UserDetails ud, Model m){
    var user = currentUser(ud.getUsername());
    var cart = cartService.getActiveCart(user);
    m.addAttribute("cart", cart);
    m.addAttribute("total", cartService.cartTotal(cart));
    return "cart";
  }

  @PostMapping("/update/{itemId}")
  public String update(@AuthenticationPrincipal UserDetails ud, @PathVariable Long itemId, @RequestParam int qty){
    cartService.updateItem(currentUser(ud.getUsername()), itemId, qty); return "redirect:/cart";
  }

  @PostMapping("/remove/{itemId}")
  public String remove(@AuthenticationPrincipal UserDetails ud, @PathVariable Long itemId){
    cartService.removeItem(currentUser(ud.getUsername()), itemId); return "redirect:/cart";
  }
}
