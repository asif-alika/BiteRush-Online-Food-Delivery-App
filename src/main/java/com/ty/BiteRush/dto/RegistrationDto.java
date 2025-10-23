package com.ty.BiteRush.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
public class RegistrationDto {
  @NotBlank private String username;
  @NotBlank private String password;
  @Email private String email;
  @NotBlank private String fullName;
}
