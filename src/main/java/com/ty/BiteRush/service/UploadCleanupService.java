package com.ty.BiteRush.service;

import com.ty.BiteRush.repository.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UploadCleanupService implements CommandLineRunner {

    private final FoodItemRepository foodRepo;

    @Override
    public void run(String... args) throws Exception {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        Path uploadsPath = Paths.get(uploadDir);

        if (!Files.exists(uploadsPath)) {
            System.out.println("🟡 No uploads directory found — skipping cleanup.");
            return;
        }

        System.out.println("🧹 Starting upload folder cleanup...");

        // ✅ Get all valid image filenames from DB
        List<String> validFileNames = foodRepo.findAll().stream()
                .map(food -> {
                    if (food.getImageUrl() != null && food.getImageUrl().startsWith("/images/")) {
                        return food.getImageUrl().replace("/images/", "").trim();
                    }
                    return null;
                })
                .filter(name -> name != null && !name.isEmpty())
                .collect(Collectors.toList());

        // ✅ Iterate through all files in /uploads/
        try (Stream<Path> paths = Files.list(uploadsPath)) {
            paths.forEach(path -> {
                File file = path.toFile();
                if (file.isFile()) {
                    String filename = file.getName();
                    if (!validFileNames.contains(filename)) {
                        // Orphaned file — delete it
                        boolean deleted = file.delete();
                        System.out.println(deleted
                                ? "🗑️ Deleted orphan file: " + filename
                                : "⚠️ Could not delete: " + filename);
                    }
                }
            });
        }

        System.out.println("✅ Upload folder cleanup completed!");
    }
}
