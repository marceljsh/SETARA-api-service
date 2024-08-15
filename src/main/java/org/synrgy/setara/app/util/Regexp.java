package org.synrgy.setara.app.util;

public class Regexp {

  private Regexp() {}

  public static final String BANK_ACCOUNT_NUMBER = "^[0-9]+$";

  public static final String DISPLAY_NAME = "^[a-zA-Z0-9 ]+$";

  public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  /**
   * <ul>
   * <li>Must contain at least one digit (0-9)</li>
   * <li>Must contain at least one lowercase letter (a-z)</li>
   * <li>Must contain at least one uppercase letter (A-Z)</li>
   * <li>Must contain at least one special character from the set (@#$%^&+=)</li>
   * <li>Must not contain any whitespace characters (spaces, tabs, etc.)</li>
   * <li>Must be at least 8 characters long</li>
   * </ul>
   */
  public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

  public static final String PHONE_NUMBER = "^[0-9]+$";

  public static final String SIGNATURE = "^[a-zA-Z0-9]+$";

  public static final String USERNAME = "^[a-zA-Z0-9]*$";

  public static final String UUID = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$";

}
