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

    // ✅ Show Admin Dashboard
    @GetMapping
    public String page(Model model) {
        model.addAttribute("foods", foodService.listAll()); // always refetch from DB
        return "admin";
    }


    // ✅ Add new food item
    @PostMapping("/food")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam double price,
                         @RequestParam("image") MultipartFile image) throws IOException {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Files.createDirectories(Paths.get(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        if (!image.isEmpty()) {
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Image saved to: " + filePath.toAbsolutePath());
        }

        String imageUrl = "/images/" + fileName;

        FoodItem foodItem = FoodItem.builder()
                .name(name)
                .description(description)
                .price(price)
                .imageUrl(imageUrl)
                .available(true)
                .build();

        foodService.create(foodItem);
        return "redirect:/admin?created";
    }

    // ✅ Delete food item + image safely
    @PostMapping("/food/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            foodService.findById(id).ifPresent(food -> {
                // Delete image if exists
                if (food.getImageUrl() != null && food.getImageUrl().startsWith("/images/")) {
                    String fileName = food.getImageUrl().replace("/images/", "");
                    Path imagePath = Paths.get(System.getProperty("user.dir"), "uploads", fileName);
                    try {
                        boolean deleted = Files.deleteIfExists(imagePath);
                        System.out.println(deleted
                                ? "🗑️ Deleted image: " + imagePath
                                : "⚠️ Image not found: " + imagePath);
                    } catch (IOException e) {
                        System.err.println("❌ Error deleting image file: " + e.getMessage());
                    }
                }

                // Delete DB record
                try {
                    foodService.delete(id);
                    System.out.println("✅ Food item deleted from DB: " + id);
                } catch (Exception e) {
                    System.err.println("❌ Error deleting DB record: " + e.getMessage());
                }
            });

            return "redirect:/admin?deleted";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error";
        }
    }
}
