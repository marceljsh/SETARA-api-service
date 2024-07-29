package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EwalletServiceImpl implements EwalletService {
    private final Logger log = LoggerFactory.getLogger(EwalletServiceImpl.class);
    private final EwalletRepository ewalletRepo;

    @Override
    public void seedEwallet() {
        List<Ewallet> ewallets = Arrays.asList(
                Ewallet.builder().name("Ovo").imagePath("https://media.licdn.com/dms/image/C5612AQEFlpyvi7iEtg/article-cover_image-shrink_600_2000/0/1564509786670?e=2147483647&v=beta&t=hZQb7qxKSWq66flOI2_uWoylvG6UXfGpXbnaYopoezI").build(),
                Ewallet.builder().name("ShopeePay").imagePath("https://down-id.img.susercontent.com/file/id-11134207-7r98o-lq2po2xwc44ua2").build(),
                Ewallet.builder().name("GoPay").imagePath("https://static.vecteezy.com/system/resources/previews/028/766/371/original/gopay-payment-icon-symbol-free-png.png").build(),
                Ewallet.builder().name("DANA").imagePath("https://i.pinimg.com/originals/2b/1f/11/2b1f11dec29fe28b5137b46fffa0b25f.png").build(),
                Ewallet.builder().name("LinkAja").imagePath("https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/LinkAja.svg/2048px-LinkAja.svg.png").build()
        );

        for (Ewallet ewallet : ewallets) {
            if (!ewalletRepo.existsByName(ewallet.getName())) {
                ewalletRepo.save(ewallet);
                log.info("Ewallet {} has been added to the database", ewallet.getName());
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
                .collect(Collectors.toList());
    }
}
