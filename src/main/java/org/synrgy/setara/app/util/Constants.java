package org.synrgy.setara.app.util;

public class Constants {

  private Constants() {}

  public static final int MAX_GENERATION_ATTEMPTS = 10;

  public static final String IMAGE_PATH = System.getProperty("user.dir") + "/images";

  public static final String ERR_BANK_NOT_FOUND = "Bank not found";

  public static final String ERR_EWALLET_NOT_FOUND = "E-Wallet not found";

  public static final String ERR_EWALLET_USER_NOT_FOUND = "E-Wallet User not found";

  public static final String ERR_INCORRECT_CREDENTIAL = "Incorrect credential, authentication failed";

  public static final String ERR_INSUFFICIENT_BALANCE = "User balance is insufficient";

  public static final String ERR_INVALID_AUTHENTICATION = "Signature or password is incorrect";

  public static final String ERR_INVALID_TIMESPAN = "Month and year must be in the present or past";

  public static final String ERR_MERCHANT_NOT_FOUND = "Merchant not found";

  public static final String ERR_MINIMUM_AMOUNT = "Amount has not reached the minimum required amount";

  public static final String ERR_REFERENCE_NUMBER_GENERATION = "Failed to generate reference number";

  public static final String ERR_SAME_ACCOUNT_TRANSFER = "Cannot transfer to the same account";

  public static final String ERR_TRANSACTION_NOT_FOUND = "Transaction not found";

  public static final String ERR_USER_NOT_FOUND = "User not found";

}
