package org.synrgy.setara.transaction.exception;

public class InvalidMpinException extends RuntimeException {

  public InvalidMpinException(String message) {
    super(message);
  }

  public InvalidMpinException(String message, Throwable cause) {
    super(message, cause);
  }

}
