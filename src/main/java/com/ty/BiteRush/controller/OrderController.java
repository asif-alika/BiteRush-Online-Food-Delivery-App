package com.ty.BiteRush.controller;

import com.ty.BiteRush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class OrderController {

  private final UserService userService;

  // ✅ Only show checkout page — no POST mapping here
  @GetMapping
  public String form(@AuthenticationPrincipal UserDetails ud, Model model) {
    var user = userService.getByUsername(ud.getUsername());
    model.addAttribute("user", user);
    return "checkout"; // loads checkout.html
  }
}
