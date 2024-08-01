package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EwalletServiceImpl implements EwalletService {

  private final Logger log = LoggerFactory.getLogger(EwalletServiceImpl.class);

  private final EwalletRepository ewalletRepo;

  @Override
  @Transactional
  public void populate() {
    String imgDir = Constants.IMAGE_PATH + "/ewallets";
    List<String> names = List.of("OVO", "Dana", "GoPay", "ShopeePay", "LinkAja");

    names.forEach(name -> {
      if (!ewalletRepo.existsByName(name)) {
        ewalletRepo.save(Ewallet.builder()
            .name(name)
            .imagePath(imgDir + "/" +
                name.replaceAll("\\s+", ""))  // in case of multi-word names
            .build());
        log.info("Ewallet {} is now operational", name);
      }
    });
  }

  @Override
  @Transactional(readOnly = true)
  public List<EwalletResponse> fetchAll() {
    return ewalletRepo.findAll()
        .stream()
        .map(EwalletResponse::from)
        .toList();
  }

}
