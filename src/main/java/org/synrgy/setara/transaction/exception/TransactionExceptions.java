package org.synrgy.setara.transaction.exception;

public class TransactionExceptions {

    public static class InvalidMpinException extends RuntimeException {
        public InvalidMpinException(String message) {
            super(message);
        }
    }

    public static class InsufficientBalanceException extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    public static class DestinationEwalletUserNotFoundException extends RuntimeException {
        public DestinationEwalletUserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
