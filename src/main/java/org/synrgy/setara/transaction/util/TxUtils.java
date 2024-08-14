package org.synrgy.setara.transaction.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TxUtils {

  private TxUtils() {
  }

  private static final int REFERENCE_NUMBER_LENGTH = 5;

  public static String generateReferenceNumber(String prefix) {
    String randomDigits = RandomStringUtils.randomNumeric(REFERENCE_NUMBER_LENGTH);
    return String.format("%s-%s", prefix, randomDigits);
  }

  public static String generateUniqueCode(String referenceNumber) {
    String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM"));
    return String.format("%s/%s/%s", datePart, referenceNumber,
        RandomStringUtils.randomAlphanumeric(8).toUpperCase());
  }

}
