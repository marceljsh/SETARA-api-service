package org.synrgy.setara.transaction.model;

import lombok.Getter;

@Getter
public enum TransactionType {

  TRANSFER("Transfer"),
  TOP_UP("Top Up"),
  DEPOSIT("Deposit"),
  QRPAYMENT("QR PAYMENT");

  private final String name;

  TransactionType(String name) {
    this.name = name;
  }

}
