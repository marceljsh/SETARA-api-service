package org.synrgy.setara.vendor.exception;

public class VendorExceptions {

    private VendorExceptions() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class MerchantNotFoundException extends RuntimeException {
        public MerchantNotFoundException(String message) {
            super(message);
        }
    }

    public static class QrCodeGenerationException extends RuntimeException {
        public QrCodeGenerationException(String message) {
            super(message);
        }
    }
}
