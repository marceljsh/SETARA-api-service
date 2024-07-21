package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

@Service
@RequiredArgsConstructor
public class EwalletServiceImpl implements EwalletService {

    private final EwalletRepository ewalletRepository;

    @Override
    public void seedEwallet() {
        Ewallet ewallet = Ewallet.builder()
                .name("OVO")
                .imagePath("OVO.jpg")
                .build();
        ewalletRepository.save(ewallet);

        Ewallet ewallet2 = Ewallet.builder()
                .name("DANA")
                .imagePath("DANA.jpg")
                .build();
        ewalletRepository.save(ewallet2);
    }
}
