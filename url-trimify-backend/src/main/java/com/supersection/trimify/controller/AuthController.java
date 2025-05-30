package com.supersection.trimify.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supersection.trimify.dto.LoginRequest;
import com.supersection.trimify.dto.RegisterRequest;
import com.supersection.trimify.model.User;
import com.supersection.trimify.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/public/login")
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.ok(userService.authenticate(loginRequest));
  }

  @PostMapping("/public/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
    if (userService.isUsernameAlreadyTaken(registerRequest.getUsername())) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }
    if (userService.isEmailAlreadyRegistered(registerRequest.getEmail())) {
      return ResponseEntity.badRequest().body("Email is already registered");
    }
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(registerRequest.getPassword());
    user.setEmail(registerRequest.getEmail());
    userService.registerUser(user);
    return ResponseEntity.ok("User registered successful");
  }
}
