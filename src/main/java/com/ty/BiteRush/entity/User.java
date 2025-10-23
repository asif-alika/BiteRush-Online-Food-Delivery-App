package com.ty.BiteRush.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private String email;
  private String fullName;

  @Enumerated(EnumType.STRING)
  private Role role = Role.ROLE_USER;

  // address snapshot fields
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String zip;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Cart cart;
}
