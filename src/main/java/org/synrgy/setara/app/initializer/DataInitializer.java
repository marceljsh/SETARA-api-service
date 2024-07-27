package org.synrgy.setara.app.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.synrgy.setara.contact.service.SavedEwalletUserService;
import org.synrgy.setara.user.service.EwalletUserService;
import org.synrgy.setara.user.service.UserService;
import org.synrgy.setara.vendor.service.BankService;
import org.synrgy.setara.vendor.service.EwalletService;
import org.synrgy.setara.vendor.service.MerchantService;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final UserService userService;
  private final EwalletService ewalletService;
  private final EwalletUserService ewalletUserService;
  private final SavedEwalletUserService savedEwalletUserService;
  private final BankService bankService;
  private final MerchantService merchantService;

  @Override
  public void run(String... args) throws Exception {
    bankService.seedBank();
    userService.seedUser();
    ewalletService.seedEwallet();
    ewalletUserService.seedEwalletUsers();
    savedEwalletUserService.seedSavedEwalletUsers();
    merchantService.seedMerchant();
  }

}
