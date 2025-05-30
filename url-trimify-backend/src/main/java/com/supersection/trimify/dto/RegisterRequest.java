package com.supersection.trimify.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

  @NotBlank(message = "Username is mandatory")
  private String username;

  @NotBlank(message = "Email is mandatory")
  @Email(message = "Email must be valid")
  private String email;

  @NotBlank(message = "Password is mandatory")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

  private Set<String> role;
}
