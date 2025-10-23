package com.ty.BiteRush.service.impl;

import com.ty.BiteRush.entity.*;
import com.ty.BiteRush.repository.*;
import com.ty.BiteRush.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepo;
  private final PasswordEncoder encoder;
  private final CartRepository cartRepo;

  @Override
  public User register(String username, String rawPassword, String email, String fullName) {
    User u = User.builder()
        .username(username)
        .password(encoder.encode(rawPassword))
        .email(email)
        .fullName(fullName)
        .role(Role.ROLE_USER)
        .build();
    u = userRepo.save(u);
    Cart c = Cart.builder().user(u).build();
    cartRepo.save(c);
    return u;
  }

  @Override
  public User getByUsername(String username) {
    return userRepo.findByUsername(username).orElse(null);
  }
}
