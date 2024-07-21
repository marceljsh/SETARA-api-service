package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EwalletUserServiceImpl implements EwalletUserService{

    private final EwalletUserRepository ewalletUserRepository;
    private final EwalletRepository ewalletRepository;

    @Override
    public void seedEwalletUser() {
        Ewallet ovo = (Ewallet) ewalletRepository.findByName("OVO").orElseThrow(() -> new RuntimeException("Ewallet OVO not found"));
        Ewallet dana = (Ewallet) ewalletRepository.findByName("DANA").orElseThrow(() -> new RuntimeException("Ewallet DANA not found"));

        EwalletUser user1 = EwalletUser.builder()
                .name("John Doe")
                .ewallet(ovo)
                .balance(new BigDecimal("100000"))
                .phoneNumber("0821234567891")
                .imagePath("johndoe.jpg")
                .build();
        ewalletUserRepository.save(user1);

        EwalletUser user2 = EwalletUser.builder()
                .name("Jane Anang")
                .ewallet(dana)
                .balance(new BigDecimal("200000"))
                .phoneNumber("081234567892")
                .imagePath("janedoe.jpg")
                .build();
        ewalletUserRepository.save(user2);
    }
}
