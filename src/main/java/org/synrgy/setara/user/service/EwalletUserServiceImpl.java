package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.vendor.exception.EwalletNotFoundException;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EwalletUserServiceImpl implements EwalletUserService {

  private final Logger log = LoggerFactory.getLogger(EwalletUserServiceImpl.class);

  private final EwalletUserRepository euRepo;

  private final EwalletRepository ewalletRepo;

  private String generateImagePath(String name) {
    return Constants.IMAGE_PATH +
        "/ewallet-user/" +
        name.replaceAll("\\s+", "") +
        ".jpg";
  }

  @Override
  @Transactional
  public void populate() {
    Map<String, String> userData = Map.of(
        "Jermaine Cole", "081234567890",
        "Tyler Okonma", "085712345678",
        "Andre Benjamin", "081398765432");

    Ewallet ovo = ewalletRepo.findByName("OVO").orElseThrow(() -> {
      log.error("Ewallet with name OVO not found");
      return new EwalletNotFoundException(Constants.ERR_EWALLET_NOT_FOUND);
    });

    userData.forEach((name, phoneNumber) -> {
      if (!euRepo.existsByPhoneNumberAndEwallet(phoneNumber, ovo)) {
        euRepo.save(EwalletUser.builder()
            .name(name)
            .phoneNumber(phoneNumber)
            .ewallet(ovo)
            .imagePath(generateImagePath(name))
            .build());
      }
      log.info("EwalletUser(ew={}, no={}) is now operational", ovo.getName(), phoneNumber);
    });
  }

}
