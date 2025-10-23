package com.ty.BiteRush.service;
import com.ty.BiteRush.entity.User;

public interface UserService {
  User register(String username, String rawPassword, String email, String fullName);
  User getByUsername(String username);
}
