package com.ty.BiteRush.config;

import com.ty.BiteRush.entity.*;
import com.ty.BiteRush.repository.CartRepository;
import com.ty.BiteRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final CartRepository cartRepo;

    @Value("${app.admin.username}")
    private String adminUser;

    @Value("${app.admin.password}")
    private String adminPass;

    @Override
    public void run(String... args) {
        userRepo.findByUsername(adminUser).orElseGet(() -> {
            // ✅ No Lombok builder() usage — plain setters
            User admin = new User();
            admin.setUsername(adminUser);
            admin.setPassword(encoder.encode(adminPass));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setFullName("Administrator");
            admin.setEmail("admin@biterush.local");

            userRepo.save(admin);

            Cart c = new Cart();
            c.setUser(admin);
            cartRepo.save(c);

            return admin;
        });
    }
}
