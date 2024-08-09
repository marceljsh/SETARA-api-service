package org.synrgy.setara.contact.exception;

public class SavedEwalletExceptions {

    private SavedEwalletExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class EwalletUserNotFoundException extends RuntimeException {
        public EwalletUserNotFoundException(String message) {
            super(message);
        }
    }

    public static class FavoriteUpdateException extends RuntimeException {
        public FavoriteUpdateException(String message) {
            super(message);
        }
    }
}
