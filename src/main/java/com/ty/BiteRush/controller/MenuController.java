package com.ty.BiteRush.controller;

import com.ty.BiteRush.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequiredArgsConstructor
@RequestMapping("/")
public class MenuController {
  private final FoodService foodService;
  @GetMapping({"","menu"})
  public String menu(Model m){ m.addAttribute("foods", foodService.listAll()); return "menu"; }
}
