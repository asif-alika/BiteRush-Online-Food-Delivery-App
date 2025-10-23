package com.ty.BiteRush.config;

import com.ty.BiteRush.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepo;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepo.findByUsername(username)
      .map(u -> org.springframework.security.core.userdetails.User // fully qualified to avoid conflict
          .withUsername(u.getUsername())
          .password(u.getPassword())
          .roles(u.getRole().name().replace("ROLE_", "")) // convert ROLE_ADMIN → ADMIN
          .build()
      )
      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/css/**", "/img/**", "/register", "/login").permitAll()
        .requestMatchers("/admin/**", "/admin").hasRole("ADMIN")
        .anyRequest().authenticated())
      .formLogin(fl -> fl.loginPage("/login").permitAll()
        .successHandler(new CustomAuthenticationSuccessHandler()))
      .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
      .csrf(Customizer.withDefaults());

    return http.build();
  }
}
