package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.exception.VendorExceptions;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.MerchantRepository;
import org.synrgy.setara.vendor.util.CodeGenerator;
import org.synrgy.setara.vendor.util.QRCodeGenerator;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private static final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);
    private final MerchantRepository merchantRepository;

    private static final String QR_CODE_DIR = "src/main/resources/static/qrcodes/";

    @Override
    public void seedMerchant() {
        List<Merchant> merchants = Arrays.asList(
                Merchant.builder()
                        .name("SIOMAY MBA YU")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("Ruko Summarecon Bekasi")
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355549/SETARA_FC-8/kctoxb5wzj3bu73durn2.png")
                        .build(),
                Merchant.builder()
                        .name("Batagor Mba Sri")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("Ruko Summarecon Bandung")
                        .amount(BigDecimal.valueOf(15500))
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355549/SETARA_FC-8/kctoxb5wzj3bu73durn2.png")
                        .build()
        );

        for (Merchant merchant : merchants) {
            Optional<Merchant> existingMerchant = merchantRepository.findByName(merchant.getName());
            if (existingMerchant.isEmpty()) {
                try {
                    Merchant savedMerchant = merchantRepository.save(merchant);

                    String qrisData = savedMerchant.getId().toString();
                    int qrCodeWidth = 400;
                    int qrCodeHeight = 400;

                    String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(qrisData, qrCodeWidth, qrCodeHeight);

                    Path path = Paths.get(QR_CODE_DIR);
                    if (!Files.exists(path)) {
                        Files.createDirectories(path);
                    }

                    String qrCodeImagePath = QR_CODE_DIR + savedMerchant.getName() + "-qrcode.png";
                    QRCodeGenerator.generateQRCodeImage(qrisData, qrCodeWidth, qrCodeHeight, qrCodeImagePath);

                    savedMerchant.setQrisCode(qrCodeBase64);
                    merchantRepository.save(savedMerchant);

                    log.info("Merchant with name {} has been saved with QR code.", savedMerchant.getName());
                } catch (IOException e) {
                    throw new VendorExceptions.QrCodeGenerationException("Failed to save QR code for merchant " + merchant.getName());
                }
            } else {
                log.info("Merchant with name {} already exists.", merchant.getName());
            }
        }
    }

    private String generateUniqueNmid() {
        String nmid;
        do {
            nmid = CodeGenerator.generateUniqueNmid();
        } while (merchantRepository.findByNmid(nmid).isPresent());
        return nmid;
    }

    private String generateUniqueTerminalId() {
        String terminalId;
        do {
            terminalId = CodeGenerator.generateUniqueTerminalId();
        } while (merchantRepository.findByTerminalId(terminalId).isPresent());
        return terminalId;
    }

    @Override
    public MerchantResponse getQrisData(UUID idQris) {
        Merchant merchant = merchantRepository.findById(idQris)
                .orElseThrow(() -> new VendorExceptions.MerchantNotFoundException("Merchant with idQris: " + idQris + " not found"));

        return MerchantResponse.builder()
                .name(merchant.getName())
                .nmid(merchant.getNmid())
                .terminalId(merchant.getTerminalId())
                .address(merchant.getAddress())
                .imagePath(merchant.getImagePath())
                .amount(merchant.getAmount())
                .qrisCode(merchant.getQrisCode())
                .build();
    }
}
