package org.synrgy.setara.transaction.exception;

public class InvalidTransactionDestinationException extends RuntimeException {

  public InvalidTransactionDestinationException(String message) {
    super(message);
  }

  public InvalidTransactionDestinationException(String message, Throwable cause) {
    super(message, cause);
  }

}
