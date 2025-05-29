package com.supersection.trimify.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.supersection.trimify.service.UserDetailsImpl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;


  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public String generateJwtToken(UserDetailsImpl userDetails) {
    String username = userDetails.getUsername();
    String roles = userDetails.getAuthorities().stream()
        .map(authority -> authority.getAuthority())
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .subject(username)
        .claim("roles", roles)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 1 day expiration
        .signWith(getSecretKey())
        .compact();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser()
          .verifyWith((SecretKey) getSecretKey())
          .build().parseSignedClaims(authToken);
      return true;
    } catch (JwtException e) {
      throw new JwtException("Invalid JWT token: " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new JwtException("JWT token is empty: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new JwtException("JWT token validation failed: " + e.getMessage(), e);
    }
  }

  public String getUsernameFromJwtToken(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) getSecretKey())
        .build().parseSignedClaims(token)
        .getPayload().getSubject();
  }

  private Key getSecretKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
