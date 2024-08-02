package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.dto.SearchNoEwalletRequest;
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
        // Daftar pengguna e-wallet
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

        // Daftar nama e-wallet
        List<String> ewalletNames = Arrays.asList("Ovo", "ShopeePay", "GoPay", "DANA", "LinkAja");

        for (String ewalletName : ewalletNames) {
            Optional<Ewallet> ewalletOpt = ewalletRepo.findByName(ewalletName);

            if (ewalletOpt.isPresent()) {
                Ewallet ewallet = ewalletOpt.get();
                ewalletUsers.forEach(user -> {
                    // Buat instance baru dari setiap pengguna e-wallet
                    EwalletUser ewalletUser = EwalletUser.builder()
                            .name(user.getName())
                            .phoneNumber(user.getPhoneNumber())
                            .balance(user.getBalance())
                            .imagePath(user.getImagePath())
                            .ewallet(ewallet)
                            .build();

                    // Cek apakah EwalletUser dengan nama dan nomor telepon yang sama sudah ada untuk ewallet tertentu
                    boolean exists = ewalletUserRepo.existsByNameAndPhoneNumberAndEwallet(ewalletUser.getName(), ewalletUser.getPhoneNumber(), ewallet);

                    if (exists) {
                        log.info("EwalletUser with name {} and phone number {} already exists in the database for ewallet {}.", ewalletUser.getName(), ewalletUser.getPhoneNumber(), ewallet.getName());
                    } else {
                        ewalletUserRepo.save(ewalletUser);
                        log.info("EwalletUser {} has been added to the database with e-wallet {}", ewalletUser.getName(), ewallet.getName());
                    }
                });
            } else {
                log.warn("E-wallet '{}' not found in the database.", ewalletName);
            }
        }
    }


    @Override
    public SearchResponse searchEwalletUser(SearchNoEwalletRequest request) {
        System.out.println(request.getNoEwallet() + " | " + request.getEwalletId());

        EwalletUser ewalletUser = ewalletUserRepo.findByPhoneNumber(request.getNoEwallet())
                .orElseThrow(() -> new SearchExceptions.SearchNotFoundException("not found number " + request.getNoEwallet()));

        if (!Objects.equals(ewalletUser.getEwallet().getId(), request.getEwalletId())) {
            throw new SearchExceptions.SearchNotFoundException("Ewallet ID mismatch for number " + request.getNoEwallet());
        }

        return SearchResponse.builder()
                .no(request.getNoEwallet())
                .name(ewalletUser.getName())
                .bank(ewalletUser.getEwallet().getName())
                .build();
    }
}
