package org.synrgy.setara.transaction.exception;

public class MerchantNotFoundException extends RuntimeException {

  public MerchantNotFoundException(String message) {
    super(message);
  }

  public MerchantNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
