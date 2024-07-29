package org.synrgy.setara.user.exception;

public class SearchExceptions {

    public static class SearchNotFoundException extends RuntimeException {
        public SearchNotFoundException(String message) {
            super(message);
        }
    }
}
