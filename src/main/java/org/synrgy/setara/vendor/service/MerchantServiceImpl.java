package org.synrgy.setara.vendor.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.synrgy.setara.app.util.Constants;
import org.synrgy.setara.vendor.exception.MerchantNotFoundException;
import org.synrgy.setara.vendor.exception.NmidGenerationException;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.exception.TerminalIdGenerationException;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.MerchantRepository;
import org.synrgy.setara.vendor.util.CodeGenerator;
import org.synrgy.setara.vendor.util.QRCodeGenerator;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  private final Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

  private static final String IMAGE_PATH = System.getProperty("user.dir") + "/images";
  private static final int QR_CODE_SIZE = 400;

  private final MerchantRepository merchantRepo;

  @Override
  @Transactional
  public void populate() {
    log.debug("Populating merchant data");

    createInitialMerchants().forEach(this::processMerchant);
  }

  @Override
  @Transactional(readOnly = true)
  public MerchantResponse fetchById(UUID id) {
    log.debug("Fetching merchant by id: {}", id);

    Merchant merchant = merchantRepo.findById(id).orElseThrow(() -> {
      log.error("Merchant with id {} not found", id);
      return new MerchantNotFoundException(Constants.ERR_MERCHANT_NOT_FOUND);
    });

    log.info("Merchant(id={}) found", id);

    return MerchantResponse.from(merchant);
  }

  private String generateUniqueNmid() {
    String nmid;
    int attempts = 0;

    do {
      if (attempts == Constants.MAX_GENERATION_ATTEMPTS) {
        log.error("Failed to generate unique nmid");
        throw new NmidGenerationException("Failed to generate unique nmid");
      }

      nmid = CodeGenerator.generateUniqueNmid();
      attempts++;

    } while (merchantRepo.existsByNmid(nmid));

    return nmid;
  }

  private String generateUniqueTerminalId() {
    String terminalId;
    int attempts = 0;

    do {
      if (attempts == Constants.MAX_GENERATION_ATTEMPTS) {
        log.error("Failed to generate unique terminal id");
        throw new TerminalIdGenerationException("Failed to generate unique terminal id");
      }

      terminalId = CodeGenerator.generateUniqueTerminalId();
      attempts++;

    } while (merchantRepo.existsByTerminalId(terminalId));

    return terminalId;
  }

  private List<Merchant> createInitialMerchants() {
    return List.of(
      createMerchant("Los Pollos Hermanos", "12100 Coors Rd SW, Albuquerque", "/images/los_pollos_hermanos.jpg"),
      createMerchant("Binco Clothing Store", "14200 Ganton Bld, Los Santos", "/images/binco_clothing_store.jpg")
    );
  }

  private Merchant createMerchant(String name, String address, String imagePath) {
    return Merchant.builder()
        .name(name)
        .nmid(generateUniqueNmid())
        .terminalId(generateUniqueTerminalId())
        .address(address)
        .imagePath(imagePath)
        .build();
  }

  private void processMerchant(Merchant merchant) {
    if (!merchantRepo.existsByQrisCode(merchant.getQrisCode())) {
      Merchant savedMerchant = merchantRepo.save(merchant);
      generateAndSetQRCode(savedMerchant);
      merchantRepo.save(savedMerchant);
      log.info("Merchant {} is now operational", savedMerchant.getName());
    }
  }

  private void generateAndSetQRCode(Merchant merchant) {
    String qrData = merchant.getId().toString();
    String merchantName = merchant.getName().replace(" ", "-");
    String qrCodeImagePath = IMAGE_PATH + "/qrcode_" + merchantName + ".png";

    QRCodeGenerator.generateQRCodeImage(qrData, QR_CODE_SIZE, QR_CODE_SIZE, qrCodeImagePath);
    String qrisCode = QRCodeGenerator.generateQRCodeBase64(qrData, QR_CODE_SIZE, QR_CODE_SIZE);
    merchant.setQrisCode(qrisCode);
    merchant.setImagePath(qrCodeImagePath);
  }

}
