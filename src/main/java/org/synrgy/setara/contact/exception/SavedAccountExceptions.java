package org.synrgy.setara.contact.exception;

public class SavedAccountExceptions {

    private SavedAccountExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class FavoriteUpdateException extends RuntimeException {
        public FavoriteUpdateException(String message) {
            super(message);
        }
    }

    public static class SavedAccountNotFoundException extends RuntimeException {
        public SavedAccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
