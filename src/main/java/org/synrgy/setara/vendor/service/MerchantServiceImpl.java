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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public void seedMerchant() {
        List<Merchant> merchants = Arrays.asList(
                Merchant.builder()
                        .merchant_name("SIOMAY MBA YU")
                        .name("Ruko Summarecon Bekasi")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("Ruko Summarecon Bekasi")
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355549/SETARA_FC-8/kctoxb5wzj3bu73durn2.png")
                        .build(),
                Merchant.builder()
                        .merchant_name("Batagor Mba Sri")
                        .name("Ruko Summarecon Bandung")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("Ruko Summarecon Bandung")
                        .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722355549/SETARA_FC-8/kctoxb5wzj3bu73durn2.png")
                        .build()
        );

        for (Merchant merchant : merchants) {
            Optional<Merchant> existingMerchant = merchantRepository.findByNmid(merchant.getNmid());
            if (existingMerchant.isEmpty()) {
                // Save merchant first to get the id_qris
                Merchant savedMerchant = merchantRepository.save(merchant);

                // Generate QR code using id_qris
                String qrisData = savedMerchant.getId().toString();
                int qrCodeWidth = 400;  // Set the desired width
                int qrCodeHeight = 400; // Set the desired height

                String qrCodeBase64 = QRCodeGenerator.generateQRCodeBase64(qrisData, qrCodeWidth, qrCodeHeight);
                String qrCodeImagePath = "D:\\QRCode\\" + savedMerchant.getMerchant_name() + "-qrcode.png";
                QRCodeGenerator.generateQRCodeImage(qrisData, qrCodeWidth, qrCodeHeight, qrCodeImagePath);

                // Update the merchant with the generated QR code and image path
                savedMerchant.setQrisCode(qrCodeBase64);
                savedMerchant.setImagePath(qrCodeImagePath);  // Assuming you want to store the image path
                merchantRepository.save(savedMerchant);

                System.out.println("Merchant with QRIS code " + savedMerchant.getQrisCode() + " has been saved.");
            } else {
                System.out.println("Merchant with QRIS code " + merchant.getQrisCode() + " already exists.");
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
        Optional<Merchant> optionalMerchant = merchantRepository.findById(UUID.fromString(requestDTO.getId_qris()));
        if (optionalMerchant.isPresent()) {
            Merchant merchant = optionalMerchant.get();
            MerchantResponse merchantResponse = MerchantResponse.builder()
                    .merchant_name(merchant.getMerchant_name())
                    .name(merchant.getName())
                    .nmid(merchant.getNmid())
                    .terminalId(merchant.getTerminalId())
                    .address(merchant.getAddress())
                    .image_path(merchant.getImagePath())
                    .qris_code(merchant.getQrisCode())
                    .build();

            return BaseResponse.success(HttpStatus.OK, merchantResponse, "Merchant found.");
        } else {
            return BaseResponse.failure(HttpStatus.NOT_FOUND, "Merchant not found.");
        }
    }
}
