package org.synrgy.setara.contact.exception;

public class BankContactNotFoundException extends RuntimeException {

  public BankContactNotFoundException(String message) {
    super(message);
  }

  public BankContactNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
