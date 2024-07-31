package org.synrgy.setara.vendor.util;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGenerator {

  private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(SecureRandom::new);

  private static final int NMID_LENGTH = 13;

  private static final int TERMINAL_ID_LENGTH = 3;

  private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private CodeGenerator() {
    // prevent instantiation
  }

  public static String generateUniqueNmid() {
    StringBuilder nmid = new StringBuilder("ID");
    for (int i = 0; i < NMID_LENGTH; i++) {
      nmid.append(RANDOM.get().nextInt(10));
    }
    return nmid.toString();
  }

  public static String generateUniqueTerminalId() {
    StringBuilder terminalId = new StringBuilder();
    for (int i = 0; i < TERMINAL_ID_LENGTH; i++) {
      terminalId.append(ALPHANUMERIC.charAt(RANDOM.get().nextInt(ALPHANUMERIC.length())));
    }
    return terminalId.toString();
  }

}
