package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EwalletServiceImpl implements EwalletService {
    private final Logger log = LoggerFactory.getLogger(EwalletServiceImpl.class);
    private final EwalletRepository ewalletRepo;

    @Override
    public void seedEwallet() {
        List<Ewallet> ewallets = Arrays.asList(
                Ewallet.builder().name("Ovo").imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860518/SETARA_FC-8/NewOvo.png").build(),
                Ewallet.builder().name("ShopeePay").imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860519/SETARA_FC-8/NewShopee.png").build(),
                Ewallet.builder().name("GoPay").imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860520/SETARA_FC-8/Newgopay.png").build(),
                Ewallet.builder().name("DANA").imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860518/SETARA_FC-8/NewDana.png").build(),
                Ewallet.builder().name("LinkAja").imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860517/SETARA_FC-8/Newlinkaja.png").build()
        );

        for (Ewallet ewallet : ewallets) {
            if (!ewalletRepo.existsByName(ewallet.getName())) {
                ewalletRepo.save(ewallet);
                log.info("Ewallet {} has been added to the database", ewallet.getName());
            } else {
                log.info("Ewallet {} already exists in the database", ewallet.getName());
            }
        }
    }

    @Override
    public List<EwalletResponse> getAllEwallets() {
        return ewalletRepo.findAll().stream()
                .map(ewallet -> EwalletResponse.builder()
                        .id(ewallet.getId().toString())
                        .name(ewallet.getName())
                        .imagePath(ewallet.getImagePath())
                        .build())
                .toList();
    }
}
