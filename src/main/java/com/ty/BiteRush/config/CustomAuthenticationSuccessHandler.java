package com.ty.BiteRush.config;

import com.ty.BiteRush.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
      throws IOException, ServletException {
    boolean isAdmin = auth.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .anyMatch(a -> a.equals(Role.ROLE_ADMIN.name()));
    res.sendRedirect(isAdmin ? "/admin" : "/menu");
  }
}
