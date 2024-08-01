package org.synrgy.setara.contact.exception;

public class SavedAccountExceptions {

    public static class SavedAccountNotFoundException extends RuntimeException {
        public SavedAccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class FavoriteUpdateException extends RuntimeException {
        public FavoriteUpdateException(String message) {
            super(message);
        }
    }
}
