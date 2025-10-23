package com.ty.BiteRush.controller;

import com.ty.BiteRush.entity.FoodItem;
import com.ty.BiteRush.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final FoodService foodService;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("foods", foodService.listAll());
        return "admin";
    }

    /**
     * ✅ Adds new food item with uploaded image (stored in /uploads/ directory)
     */
    @PostMapping("/food")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam double price,
                         @RequestParam("image") MultipartFile image) throws IOException {

        // Create uploads folder if not exists
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        // Save file with timestamped name to avoid conflicts
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);

        if (!image.isEmpty()) {
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Store accessible image URL
        String imageUrl = "/images/" + fileName;

        // Save to DB
        FoodItem foodItem = FoodItem.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        foodService.create(foodItem);
        return "redirect:/admin?created";
    }

    /**
     * ✅ Deletes food item from DB and also removes image file if present
     */
    @PostMapping("/food/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            foodService.findById(id).ifPresent(food -> {
                // Delete the associated image if exists
                if (food.getImageUrl() != null && food.getImageUrl().startsWith("/images/")) {
                    String fileName = food.getImageUrl().replace("/images/", "");
                    Path imagePath = Paths.get("uploads", fileName);
                    try {
                        Files.deleteIfExists(imagePath);
                        System.out.println("✅ Image deleted: " + imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Delete food from DB
                foodService.delete(id);
                System.out.println("✅ Food item deleted: ID " + id);
            });
            return "redirect:/admin?deleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error";
        }
    }


}
