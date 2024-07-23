package org.synrgy.setara.auth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.auth.dto.AuthResponse;
import org.synrgy.setara.auth.dto.LoginRequest;
import org.synrgy.setara.auth.exception.AuthenticationException;
import org.synrgy.setara.security.service.JwtService;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
  private final UserRepository userRepo;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public AuthResponse authenticate(LoginRequest request) {
    User user = findUserBySignature(request.getSignature());
    validatePassword(request.getPassword(), user.getPassword());

    String token = generateToken(user);

    return AuthResponse.of(user, token);
  }

  private User findUserBySignature(String signature) {
    return userRepo.findBySignature(signature)
            .orElseThrow(() -> {
              log.error("User with signature {} not found", signature);
              throw new AuthenticationException("User not found");
            });
  }

  private void validatePassword(String rawPassword, String encodedPassword) {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      log.error("Invalid password");
      throw new AuthenticationException("Invalid password");
    }
  }

  private String generateToken(User user) {
    return jwtService.generateToken(user);
  }
}
