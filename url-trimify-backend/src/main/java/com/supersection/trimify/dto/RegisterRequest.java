package com.supersection.trimify.dto;

import java.util.Set;

import lombok.Data;

@Data
public class RegisterRequest {
  private String username;
  private String email;
  private Set<String> roles;
  private String password;
}
