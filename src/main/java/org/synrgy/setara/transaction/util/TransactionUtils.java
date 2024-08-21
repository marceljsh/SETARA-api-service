package org.synrgy.setara.transaction.util;

import org.apache.commons.lang3.RandomStringUtils;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TransactionUtils {

    private static final int REFERENCE_NUMBER_LENGTH = 5;

    private TransactionUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String generateReferenceNumber(String typePrefix) {
        String randomDigits = RandomStringUtils.randomNumeric(REFERENCE_NUMBER_LENGTH);
        return String.format("%s-%s", typePrefix, randomDigits);
    }

    public static String generateUniqueCode(String referenceNumber) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM"));
        return String.format("%s/%s/%s", datePart, referenceNumber, RandomStringUtils.randomAlphanumeric(8).toUpperCase());
    }

    public static String getMonthNameInIndonesian(Month month) {
        return switch (month) {
            case JANUARY -> "Januari";
            case FEBRUARY -> "Februari";
            case MARCH -> "Maret";
            case APRIL -> "April";
            case MAY -> "Mei";
            case JUNE -> "Juni";
            case JULY -> "Juli";
            case AUGUST -> "Agustus";
            case SEPTEMBER -> "September";
            case OCTOBER -> "Oktober";
            case NOVEMBER -> "November";
            case DECEMBER -> "Desember";
        };
    }
}
