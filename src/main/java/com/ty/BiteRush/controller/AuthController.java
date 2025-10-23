package com.ty.BiteRush.controller;

import com.ty.BiteRush.dto.RegistrationDto;
import com.ty.BiteRush.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller @RequiredArgsConstructor
public class AuthController {
  private final UserService userService;

  @GetMapping("/login") public String login(){ return "login"; }

  @GetMapping("/register") public String registerForm(Model m){ m.addAttribute("dto", new RegistrationDto()); return "register"; }

  @PostMapping("/register") public String register(@Valid @ModelAttribute("dto") RegistrationDto dto, BindingResult br){
    if(br.hasErrors()) return "register";
    userService.register(dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getFullName());
    return "redirect:/login?registered";
  }
}
