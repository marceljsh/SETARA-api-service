package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.vendor.dto.BankResponse;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

  private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

  private final BankRepository bankRepo;

  @Override
  @Transactional
  public void populate() {
    List<String> bankNames = List.of("Tahapan BCA", "Bank BNI", "Bank BRI",
        "Bank Mandiri", "Bank BTN", "Seabank","Bank Jago", "CIMB Niaga");

    bankNames.forEach(name -> {
      if (!bankRepo.existsByName(name)) {
        bankRepo.save(Bank.builder().name(name).build());
      }
      log.info("Bank {} is now operational", name);
    });
  }

  @Override
  @Transactional(readOnly = true)
  public List<BankResponse> fetchAll() {
    return bankRepo.findAll()
        .stream()
        .map(BankResponse::from)
        .toList();
  }

}
