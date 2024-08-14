package org.synrgy.setara.vendor.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import javax.imageio.ImageIO;
import org.synrgy.setara.vendor.exception.VendorExceptions;

public class QRCodeGenerator {

    private QRCodeGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    public static String generateQRCodeBase64(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(pngData);
        } catch (WriterException | IOException e) {
            throw new VendorExceptions.QrCodeGenerationException("Error generating QR code: " + e);
        }
    }

    public static void generateQRCodeImage(String data, int width, int height, String filePath) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            Path path = FileSystems.getDefault().getPath(filePath).getParent();
            if (path != null && !Files.exists(path)) {
                Files.createDirectories(path);
            }

            ImageIO.write(bufferedImage, "PNG", new File(filePath));
        } catch (WriterException | IOException e) {
            throw new VendorExceptions.QrCodeGenerationException("Error generating QR code: " + e);
        }
    }
}
