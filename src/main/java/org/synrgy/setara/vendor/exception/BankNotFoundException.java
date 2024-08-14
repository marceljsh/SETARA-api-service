package org.synrgy.setara.vendor.exception;

public class BankNotFoundException extends RuntimeException {

  public BankNotFoundException(String message) {
    super(message);
  }

  public BankNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
