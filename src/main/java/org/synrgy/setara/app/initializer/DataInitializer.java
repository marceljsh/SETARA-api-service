package org.synrgy.setara.app.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.synrgy.setara.transaction.dto.TransactionRequest;
import org.synrgy.setara.transaction.service.TransactionService;
import org.synrgy.setara.user.service.EwalletUserService;
import org.synrgy.setara.user.service.UserService;
import org.synrgy.setara.vendor.service.EwalletService;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserService userService;
  private final EwalletService ewalletService;
  private final EwalletUserService ewalletUserService;

  @Override
  public void run(String... args) throws Exception {
    userService.seedUser();
    ewalletService.seedEwallet();
    ewalletUserService.seedEwalletUser();
  }

}
