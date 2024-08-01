package org.synrgy.setara.transaction.model;

public enum TransactionType {

  TRANSFER("Transfer"),
  TOP_UP("Top Up"),
  DEPOSIT("Deposit"),
  QRPAYMENT("QR PAYMENT");

  private final String name;

  TransactionType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static TransactionType fromString(String text) {
    return switch (text) {
      case "Transfer" -> TRANSFER;
      case "Top Up" -> TOP_UP;
      case "Deposit" -> DEPOSIT;
      case "QR PAYMENT" -> QRPAYMENT;
      default -> null;
    };
  }

}
