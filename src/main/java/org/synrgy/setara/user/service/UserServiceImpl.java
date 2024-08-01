package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.dto.UserProfileResponse;
import org.synrgy.setara.user.exception.UserNotFoundException;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.exception.BankNotFoundException;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(15_000_000);

  private final UserRepository userRepo;

  private final BankRepository bankRepo;

  private final PasswordEncoder passwordEncoder;

  private String generateImagePath(String name) {
    return Constants.IMAGE_PATH +
        "/users/" +
        name.replaceAll("\\s+", "") +
        ".jpg";
  }

  private List<User> createInitialUsers() {
    return List.of(
        createUser("kdot@tde.com", "KDOT604T", "1122334455", "Kendrick Lamar", "itsjustbigme", "1272051706870001",
            "081234567890", "Compton, CA", "170687"),
        createUser("jane.doe@example.com", "JANE1234", "2233445566", "Jane Doe", "jane123", "1272051706870002",
            "089876543210", "Los Angeles, CA", "987654"),
        createUser("john.smith@example.com", "JOHN5678", "3344556677", "John Smith", "john123", "1272051706870003",
            "081230987654", "New York, NY", "123456"));
  }

  private User createUser(String email, String signature, String accountNumber, String name, String password,
      String nik, String phoneNumber, String address, String mpin) {
    return User.builder()
        .email(email)
        .signature(signature)
        .accountNumber(accountNumber)
        .name(name)
        .password(passwordEncoder.encode(password))
        .nik(nik)
        .phoneNumber(phoneNumber)
        .address(address)
        .mpin(mpin)
        .build();
  }

  private void populateUserDetails(User user, Bank bank) {
    user.setBank(bank);
    user.setImagePath(generateImagePath(user.getName()));
    user.setBalance(INITIAL_BALANCE);
    userRepo.save(user);
  }

  @Override
  @Transactional
  public void populate() {
    Bank bca = bankRepo.findByName("Tahapan BCA").orElseThrow(() -> {
      log.error("Bank with name Tahapan BCA not found");
      return new BankNotFoundException(Constants.BANK_NOT_FOUND);
    });

    List<User> users = createInitialUsers();
    users.forEach(user -> populateUserDetails(user, bca));
  }

  @Override
  @Transactional(readOnly = true)
  public UserBalanceResponse fetchUserBalance(User user) {

    return UserBalanceResponse.of(LocalDateTime.now(), user.getBalance());
  }

  @Override
  @Transactional(readOnly = true)
  public UserProfileResponse searchByAccNumber(String accNumber) {
    log.trace("Searching user with account number {}", accNumber);

    User user = userRepo.findByAccountNumber(accNumber).orElseThrow(() -> {
      log.error("User with account number {} not found", accNumber);
      return new UserNotFoundException(Constants.USER_NOT_FOUND);
    });

    log.info("User(accNum={}) found", accNumber);

    return UserProfileResponse.from(user);
  }
}
