package com.supersection.trimify.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Email(message = "Email should be valid")
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String username;

  @Size(min = 8, message = "Password must be at least 8 characters")
  @Column(nullable = false)
  private String password;

  private String role = "ROLE_USER";
}
