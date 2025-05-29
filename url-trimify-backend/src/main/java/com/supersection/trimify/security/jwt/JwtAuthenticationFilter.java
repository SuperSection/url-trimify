package com.supersection.trimify.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtTokenProvider;

  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(
      JwtUtils jwtTokenProvider, UserDetailsService userDetailsService
  ) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
  ) throws ServletException, IOException {

    try {
      // Get JWT from the request header
      String jwtToken = jwtTokenProvider.getJwtFromHeader(request);

      // Validate Token
      if (jwtToken == null || !jwtTokenProvider.validateJwtToken(jwtToken)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
      }

      // If valid, get user details
      String username = jwtTokenProvider.getUsernameFromJwtToken(jwtToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // Load the user & set authentication in the context
      if (userDetails != null) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
          );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (IOException | UsernameNotFoundException e) {
      throw new ServletException("JWT authentication failed: " + e.getMessage(), e);
    } finally {
      // Continue the filter chain
      filterChain.doFilter(request, response);
    }
  }

}
