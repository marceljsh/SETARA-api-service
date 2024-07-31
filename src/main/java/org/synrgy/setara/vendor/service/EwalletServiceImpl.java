package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    Map<String, String> ewallets = Map.of(
        "OVO", "/images/ewallet/ovo.png",
        "Dana", "/images/ewallet/dana.png",
        "GoPay", "/images/ewallet/gopay.png",
        "ShopeePay", "/images/ewallet/shopeepay.png",
        "LinkAja", "/images/ewallet/linkaja.png"
    );

    ewallets.forEach((name, logo) -> {
      if (!ewalletRepo.existsByName(name)) {
        ewalletRepo.save(Ewallet.builder()
            .name(name)
            .imagePath(logo)
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
