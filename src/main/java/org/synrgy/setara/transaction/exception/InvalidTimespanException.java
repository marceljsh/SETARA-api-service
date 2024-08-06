package org.synrgy.setara.transaction.exception;

public class InvalidTimespanException extends RuntimeException {

  public InvalidTimespanException(String message) {
    super(message);
  }

  public InvalidTimespanException(String message, Throwable cause) {
    super(message, cause);
  }

}
