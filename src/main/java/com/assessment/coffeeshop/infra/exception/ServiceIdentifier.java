package com.assessment.coffeeshop.infra.exception;

public enum ServiceIdentifier {
  SHOP_SERVICE("22");

  private final String code;

  ServiceIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
