package org.synrgy.setara.vendor.util;

public class QRCodeGenerationException extends RuntimeException {

  public QRCodeGenerationException(String message) {
    super(message);
  }

  public QRCodeGenerationException(String message, Throwable cause) {
    super(message, cause);
  }

}
