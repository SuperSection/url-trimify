package com.supersection.trimify.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.supersection.trimify.dto.LoginRequest;
import com.supersection.trimify.model.User;
import com.supersection.trimify.repository.UserRepository;
import com.supersection.trimify.security.jwt.JwtAuthenticationResponse;
import com.supersection.trimify.security.jwt.JwtUtils;

@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  public UserService(
      PasswordEncoder passwordEncoder, UserRepository userRepository,
      AuthenticationManager authenticationManager, JwtUtils jwtUtils
  ) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
  }

  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword())
        );
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwtToken = jwtUtils.generateJwtToken(userDetails);
    return new JwtAuthenticationResponse(jwtToken);
  }

  public boolean isUsernameAlreadyTaken(String username) {
    return userRepository.existsByUsername(username);
  }

  public boolean isEmailAlreadyRegistered(String email) {
    return userRepository.existsByEmail(email);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
  }
}
