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
                        .merchant_name("Merchant One")
                        .name("Merchant One Name")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("123 Merchant Street, City, Country")
                        .imagePath("/images/merchant1.png")
                        .qrisCode(QRCodeGenerator.generateQRCodeBase64(UUID.randomUUID().toString()))
                        .build(),
                Merchant.builder()
                        .merchant_name("Merchant Two")
                        .name("Merchant Two Name")
                        .nmid(generateUniqueNmid())
                        .terminalId(generateUniqueTerminalId())
                        .address("456 Merchant Avenue, City, Country")
                        .imagePath("/images/merchant2.png")
                        .qrisCode(QRCodeGenerator.generateQRCodeBase64(UUID.randomUUID().toString()))
                        .build()
        );

        for (Merchant merchant : merchants) {
            Optional<Merchant> existingMerchant = merchantRepository.findByQrisCode(merchant.getQrisCode());
            if (existingMerchant.isEmpty()) {
                merchantRepository.save(merchant);
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
