package org.synrgy.setara.user.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.user.dto.SearchNoEwalletRequest;
import org.synrgy.setara.user.dto.SearchResponse;
import org.synrgy.setara.user.exception.SearchExceptions;
import org.synrgy.setara.user.model.EwalletUser;
import org.synrgy.setara.user.repository.EwalletUserRepository;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EwalletUserServiceImpl implements EwalletUserService {
    private final Logger log = LoggerFactory.getLogger(EwalletUserServiceImpl.class);
    private final EwalletUserRepository ewalletUserRepo;
    private final EwalletRepository ewalletRepo;

    @Override
    public void seedEwalletUsers() {
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

        List<String> ewalletNames = Arrays.asList("Ovo", "ShopeePay", "GoPay", "DANA", "LinkAja");

        for (String ewalletName : ewalletNames) {
            Optional<Ewallet> ewalletOpt = ewalletRepo.findByName(ewalletName);

            if (ewalletOpt.isPresent()) {
                Ewallet ewallet = ewalletOpt.get();
                ewalletUsers.forEach(user -> {
                    EwalletUser ewalletUser = EwalletUser.builder()
                            .name(user.getName())
                            .phoneNumber(user.getPhoneNumber())
                            .balance(user.getBalance())
                            .imagePath(user.getImagePath())
                            .ewallet(ewallet)
                            .build();

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
        Ewallet ewallet = ewalletRepo.findById(request.getEwalletId())
                .orElseThrow(() -> new SearchExceptions.SearchNotFoundException(
                        "not found Ewallet with ID " + request.getEwalletId()));

        EwalletUser ewalletUser = ewalletUserRepo.findByPhoneNumberAndEwallet(request.getNoEwallet(), ewallet)
                .orElseThrow(() -> new SearchExceptions.SearchNotFoundException("not found eWalletUser with number " + request.getNoEwallet() + " and eWalletId " + request.getEwalletId()));

        return SearchResponse.builder()
                .no(request.getNoEwallet())
                .name(ewalletUser.getName())
                .bank(ewalletUser.getEwallet().getName())
                .imagePath(ewalletUser.getImagePath())
                .build();
    }
}
