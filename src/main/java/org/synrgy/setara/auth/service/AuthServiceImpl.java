package org.synrgy.setara.auth.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
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
    User user = findUserBySignatureOrThrow(request.getSignature());

    validatePassword(request.getPassword(), user.getPassword());

    String token = jwtService.generateToken(user);

    return AuthResponse.of(user, token);
  }

  private User findUserBySignatureOrThrow(String signature) {
    return userRepo.findBySignature(signature)
        .orElseThrow(() -> new AuthenticationException(Constants.ERR_INVALID_AUTHENTICATION));
  }

  private void validatePassword(String rawPassword, String encodedPassword) {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      log.error("Invalid password");
      throw new AuthenticationException(Constants.ERR_INVALID_AUTHENTICATION);
    }
  }

}
