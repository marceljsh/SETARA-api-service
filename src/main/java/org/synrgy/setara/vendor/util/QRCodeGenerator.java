package org.synrgy.setara.vendor.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class QRCodeGenerator {

  private static final String DEFAULT_IMAGE_FORMAT = "PNG";

  private QRCodeGenerator() {
    // Prevent instantiation
  }

  public static String generateQRCodeBase64(String data, int width, int height) throws QRCodeGenerationException {
    try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
      BitMatrix bitMatrix = encodeQRCode(data, width, height);
      MatrixToImageWriter.writeToStream(bitMatrix, DEFAULT_IMAGE_FORMAT, pngOutputStream);
      return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
    } catch (IOException e) {
      throw new QRCodeGenerationException("Failed to generate QR code as base64 string", e);
    }
  }

  public static void generateQRCodeImage(String data, int width, int height, Path path) throws QRCodeGenerationException {
    try {
      BitMatrix bitMatrix = encodeQRCode(data, width, height);
      BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
      ImageIO.write(image, DEFAULT_IMAGE_FORMAT, Files.newOutputStream(path));
    } catch (IOException e) {
      throw new QRCodeGenerationException("Failed to write QR code image to file", e);
    }
  }

  private static BitMatrix encodeQRCode(String data, int width, int height) throws QRCodeGenerationException {
    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      return qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
    } catch (WriterException e) {
      throw new QRCodeGenerationException("Error encoding QR code", e);
    }
  }

}
