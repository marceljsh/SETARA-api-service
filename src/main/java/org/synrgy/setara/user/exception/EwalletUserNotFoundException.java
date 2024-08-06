package org.synrgy.setara.user.exception;

public class EwalletUserNotFoundException extends RuntimeException {

  public EwalletUserNotFoundException(String message) {
    super(message);
  }

  public EwalletUserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
