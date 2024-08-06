package org.synrgy.setara.transaction.exception;

public class ReferenceNumberGenerationException extends RuntimeException {

  public ReferenceNumberGenerationException(String message) {
    super(message);
  }

  public ReferenceNumberGenerationException(String message, Throwable cause) {
    super(message, cause);
  }

}
