package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void seedUser() {

    User user = User.builder()
            .email("andhika157@gmail.com")
            .signature("ADTP604T")
            .accountNumber("2891376451")
            .name("Kendrick Lamar")
            .password(passwordEncoder.encode("andika12345"))
            .imagePath("kendrick.jpg")
            .nik("1272051706870001")
            .phoneNumber("081234567890")
            .address("Bandung")
            .balance(BigDecimal.valueOf(1000000))
            .mpin(passwordEncoder.encode("170687"))
            .build();

    userRepo.save(user);
  }
}
