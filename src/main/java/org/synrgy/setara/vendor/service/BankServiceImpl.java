package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.vendor.model.Bank;
import org.synrgy.setara.vendor.repository.BankRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);
    private final BankRepository bankRepository;

    @Override
    public void seedBank() {
        List<Bank> banks = Collections.singletonList(
                Bank.builder()
                        .name("Tahapan BCA")
                        .build()
        );

        for (Bank bank : banks) {
            if (!bankRepository.existsByName(bank.getName())) {
                bankRepository.save(bank);
                log.info("Bank {} has been added to the database", bank.getName());
            }
        }
    }
}
