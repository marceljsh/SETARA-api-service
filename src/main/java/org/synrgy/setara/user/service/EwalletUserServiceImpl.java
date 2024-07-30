package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.exception.SearchExceptions;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EwalletUserServiceImpl implements EwalletUserService {
    private final Logger log = LoggerFactory.getLogger(EwalletUserServiceImpl.class);
    private final EwalletUserRepository ewalletUserRepo;
    private final EwalletRepository ewalletRepo;

    @Override
    public void seedEwalletUsers() {
        // Daftar pengguna dengan e-wallet "Ovo"
        List<EwalletUser> ewalletUsers = Arrays.asList(
                EwalletUser.builder()
                        .name("FARAH CANTIKA")
                        .phoneNumber("081234567890")
                        .balance(BigDecimal.valueOf(10000))
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355550/SETARA_FC-8/fiabjleyerwpgt5jxjfj.png")
                        .build(),
                EwalletUser.builder()
                        .name("AURLYN PUSPITA")
                        .phoneNumber("081234567891")
                        .balance(BigDecimal.valueOf(20000))
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355550/SETARA_FC-8/nwc0lfqaauew258nreqt.png")
                        .build(),
                EwalletUser.builder()
                        .name("KEVIN ATMAJAYA")
                        .phoneNumber("081234567892")
                        .balance(BigDecimal.valueOf(30000))
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355550/SETARA_FC-8/kvc4rknrwpbpga67syko.png")
                        .build()
        );

        // Ambil e-wallet "Ovo"
        Optional<Ewallet> ewalletOpt = ewalletRepo.findByName("Ovo");

        if (ewalletOpt.isPresent()) {
            Ewallet ewallet = ewalletOpt.get();
            ewalletUsers.forEach(ewalletUser -> {
                // Cek apakah EwalletUser dengan nama dan nomor telepon yang sama sudah ada
                boolean exists = ewalletUserRepo.existsByNameAndPhoneNumber(ewalletUser.getName(), ewalletUser.getPhoneNumber());

                if (exists) {
                    log.info("EwalletUser with name {} and phone number {} already exists in the database.", ewalletUser.getName(), ewalletUser.getPhoneNumber());
                } else {
                    ewalletUser.setEwallet(ewallet);
                    ewalletUserRepo.save(ewalletUser);
                    log.info("EwalletUser {} has been added to the database with e-wallet {}", ewalletUser.getName(), ewallet.getName());
                }
            });
        } else {
            log.warn("E-wallet 'Ovo' not found in the database.");
        }
    }

    @Override
    public SearchResponse searchEwalletUser(String no_ewallet, UUID ewallet) {
        Optional<EwalletUser> ewalletUser = ewalletUserRepo.findByPhoneNumber(no_ewallet);
        if(ewalletUser.isPresent()) {
            if (Objects.equals(ewalletUser.get().getEwallet().getId(), ewallet)) {
                SearchResponse searchResponse = SearchResponse.builder()
                        .no(no_ewallet)
                        .name(ewalletUser.get().getName())
                        .serviceName(ewalletUser.get().getEwallet().getName())
                        .build();
                return searchResponse;
            }
        }
        throw new SearchExceptions.SearchNotFoundException("not found number " + no_ewallet);
    }
}
