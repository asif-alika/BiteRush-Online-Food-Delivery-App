package com.ty.BiteRush.controller;

import com.ty.BiteRush.service.OrderService;
import com.ty.BiteRush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequiredArgsConstructor
@RequestMapping("/checkout")
public class OrderController {
  private final OrderService orderService;
  private final UserService userService;

  @GetMapping
  public String form(@AuthenticationPrincipal UserDetails ud, Model m){
    var u = userService.getByUsername(ud.getUsername());
    m.addAttribute("user", u); return "checkout";
  }

  @PostMapping
  public String place(@AuthenticationPrincipal UserDetails ud,
                      @RequestParam String addressLine1,
                      @RequestParam(required=false) String addressLine2,
                      @RequestParam String city,
                      @RequestParam String state,
                      @RequestParam String zip){
    var u = userService.getByUsername(ud.getUsername());
    orderService.checkout(u, addressLine1, addressLine2, city, state, zip);
    return "redirect:/menu?ordered";
  }
}
