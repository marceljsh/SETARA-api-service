package org.synrgy.setara.common.utils;

import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionUtils {

    private static final int REFERENCE_NUMBER_LENGTH = 5;

    public static String generateReferenceNumber() {
        return RandomStringUtils.randomNumeric(REFERENCE_NUMBER_LENGTH);
    }

    public static String generateUniqueCode(String referenceNumber) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM"));
        return String.format("%s/%s/%s", datePart, referenceNumber, RandomStringUtils.randomAlphanumeric(8).toUpperCase());
    }
}
