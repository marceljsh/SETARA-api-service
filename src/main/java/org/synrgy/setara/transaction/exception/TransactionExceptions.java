package org.synrgy.setara.transaction.exception;

public class TransactionExceptions {

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class EwalletNotFoundException extends RuntimeException {
        public EwalletNotFoundException(String message) {
            super(message);
        }
    }

    public static class DestinationEwalletUserNotFoundException extends RuntimeException {
        public DestinationEwalletUserNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidMpinException extends RuntimeException {
        public InvalidMpinException(String message) {
            super(message);
        }
    }

    public static class InvalidTopUpAmountException extends RuntimeException {
        public InvalidTopUpAmountException(String message) {
            super(message);
        }
    }

    public static class InsufficientBalanceException extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    public static class DestinationAccountNotFoundException extends RuntimeException {
        public DestinationAccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidTransferAmountException extends RuntimeException {
        public InvalidTransferAmountException(String message) {
            super(message);
        }
    }

    public static class InvalidTransferDestinationException extends RuntimeException {
        public InvalidTransferDestinationException(String message) {
            super(message);
        }
    }

    public static class MerchantNotFoundException extends RuntimeException {
        public MerchantNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidMonthException extends RuntimeException {
        public InvalidMonthException(String message) {
            super(message);
        }
    }

    public static class InvalidYearException extends RuntimeException {
        public InvalidYearException(String message) {
            super(message);
        }
    }
}
