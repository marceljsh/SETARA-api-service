package org.synrgy.setara.vendor.exception;

public class VendorException extends RuntimeException {
    public VendorException(String message) {
        super(message);
    }

    public VendorException(String message, Throwable cause) {
        super(message, cause);
    }

    public static VendorException qrCodeGenerationException(String message, Throwable cause) {
        return new VendorException(message, cause);
    }
}
