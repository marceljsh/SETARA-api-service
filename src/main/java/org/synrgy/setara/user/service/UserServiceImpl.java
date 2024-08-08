package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.user.exception.SearchExceptions.SearchNotFoundException;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final BankRepository bankRepo;
  private final UserRepository userRepository;

  private static final String DEFAULT_IMAGE_PATH = "https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355550/SETARA_FC-8/kvc4rknrwpbpga67syko.png";

  @Override
  public void seedUser() {
    Bank tahapanBCA = bankRepo.findByName("Tahapan BCA")
            .orElseThrow(() -> new RuntimeException("Bank Tahapan BCA not found"));

    createUserIfNotExists(
            tahapanBCA,
            "kdot@tde.com",
            "KDOT604T",
            "1122334455",
            "1272051706870001",
            "081234567890",
            "Kendrick Lamar",
            "itsjustbigme",
            DEFAULT_IMAGE_PATH,
            "Compton, CA",
            BigDecimal.valueOf(1000000),
            "170687"
    );
    createUserIfNotExists(
            tahapanBCA,
            "jane.doe@example.com",
            "JANE1234",
            "2233445566",
            "1272051706870002",
            "089876543210",
            "Jane Doe",
            "jane123",
            "https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355550/SETARA_FC-8/nwc0lfqaauew258nreqt.png",
            "Los Angeles, CA",
            BigDecimal.valueOf(50000),
            "987654"
    );
    createUserIfNotExists(
            tahapanBCA,
            "john.smith@example.com",
            "JOHN5678",
            "3344556677",
            "1272051706870003",
            "081230987654",
            "John Smith",
            "john123",
            DEFAULT_IMAGE_PATH,
            "New York, NY",
            BigDecimal.valueOf(100000),
            "123456"
    );
    createUserIfNotExists(
            tahapanBCA,
            "andhika157@gmail.com",
            "ADTP604T",
            "2891376451",
            "1272051706870004",
            "081234567891",
            "Andhika Putra",
            "andika12345",
            DEFAULT_IMAGE_PATH,
            "New York, NY",
            BigDecimal.valueOf(999999999),
            "120951"
    );
  }

  private void createUserIfNotExists(Bank bank, String email, String signature, String accountNumber, String nik,
                                     String phoneNumber, String name, String password, String imagePath,
                                     String address, BigDecimal balance, String mpin) {
    boolean userExists = userRepo.existsByEmail(email) ||
            userRepo.existsBySignature(signature) ||
            userRepo.existsByAccountNumber(accountNumber) ||
            userRepo.existsByNik(nik) ||
            userRepo.existsByPhoneNumber(phoneNumber);

    if (userExists) {
      log.info("{} already exists in the system", email);
      return;
    }

    User user = User.builder()
            .bank(bank)
            .email(email)
            .signature(signature)
            .accountNumber(accountNumber)
            .name(name)
            .password(passwordEncoder.encode(password))
            .imagePath(imagePath)
            .nik(nik)
            .phoneNumber(phoneNumber)
            .address(address)
            .balance(balance)
            .mpin(passwordEncoder.encode(mpin))
            .build();

    userRepo.save(user);
    log.info("{} has been added to the system", email);
  }

  @Override
  public UserBalanceResponse getBalance(User user) {
    return UserBalanceResponse.builder()
            .checkTime(LocalDateTime.now())
            .balance(user.getBalance())
            .build();
  }

  @Override
  public SearchResponse searchUserByNorek(String no) {
    return userRepository.findByAccountNumber(no)
            .map(user -> SearchResponse.builder()
                    .no(no)
                    .name(user.getName())
                    .bank(user.getBank().getName())
                    .imagePath(user.getImagePath())
                    .build())
            .orElseThrow(() -> new SearchNotFoundException("No rekening " + no + " not found"));
  }

}