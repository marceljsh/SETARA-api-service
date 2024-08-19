package org.synrgy.setara.user.exception;

public class SearchExceptions {

    private SearchExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class SearchNotFoundException extends RuntimeException {
        public SearchNotFoundException(String message) {
            super(message);
        }
    }
}
