package org.synrgy.setara.transaction.exception;

public class InvalidTransactionAmountException extends RuntimeException {

  public InvalidTransactionAmountException(String message) {
    super(message);
  }

  public InvalidTransactionAmountException(String message, Throwable cause) {
    super(message, cause);
  }

}
