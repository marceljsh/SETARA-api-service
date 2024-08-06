package org.synrgy.setara.vendor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.vendor.dto.MerchantRequest;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.MerchantRepository;
import org.synrgy.setara.vendor.util.CodeGenerator;
import org.synrgy.setara.vendor.util.QRCodeGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

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
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355549/SETARA_FC-8/kctoxb5wzj3bu73durn2.png")
                        .build()
        );

        for (Merchant merchant : merchants) {
            Optional<Merchant> existingMerchant = merchantRepository.findByName(merchant.getName());
            if (existingMerchant.isEmpty()) {
                Merchant savedMerchant = merchantRepository.save(merchant);

                String qrisData = savedMerchant.getId().toString();
                int qrCodeWidth = 400;
                int qrCodeHeight = 400;

                String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(qrisData, qrCodeWidth, qrCodeHeight);

                Path path = Paths.get(QR_CODE_DIR);
                if (!Files.exists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String qrCodeImagePath = QR_CODE_DIR + savedMerchant.getName() + "-qrcode.png";
                QRCodeGenerator.generateQRCodeImage(qrisData, qrCodeWidth, qrCodeHeight, qrCodeImagePath);

                savedMerchant.setQrisCode(qrCodeBase64);
                merchantRepository.save(savedMerchant);

                System.out.println("Merchant with QRIS code " + savedMerchant.getName() + " has been saved.");
            } else {
                System.out.println("Merchant with QRIS code " + merchant.getName() + " already exists.");
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
    public BaseResponse<MerchantResponse> getQrisData(MerchantRequest requestDTO) {
        Optional<Merchant> optionalMerchant = merchantRepository.findById(UUID.fromString(requestDTO.getIdQris()));
        if (optionalMerchant.isPresent()) {
            Merchant merchant = optionalMerchant.get();
            MerchantResponse merchantResponse = MerchantResponse.builder()
                    .name(merchant.getName())
                    .nmid(merchant.getNmid())
                    .terminalId(merchant.getTerminalId())
                    .address(merchant.getAddress())
                    .imagePath(merchant.getImagePath())
                    .qrisCode(merchant.getQrisCode())
                    .build();

            return BaseResponse.success(HttpStatus.OK, merchantResponse, "Merchant found.");
        } else {
            return BaseResponse.failure(HttpStatus.NOT_FOUND, "Merchant not found.");
        }
    }
}
