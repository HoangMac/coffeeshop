package com.assessment.coffeeshop.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  // Generic
  UNKNOWN_ERROR("000", "Unknown error"),
  INVALID_REQUEST("001", "Request is invalid"),
  INVALID_MESSAGE("001", "Message has invalid fields : %s"),

  // Business
  PROFILE_NOT_FOUND("010", "Profile not found"),
  PROFILE_MISSING("011", "Profile id missing"),
  ORDER_NOT_FOUND("012", "Order %s not found"),
  QUEUE_BUSY("0123", "Queue %s is being served"),

  ;

  private final String value;

  private final String message;

  public String toUniversalCode() {
    return String.format("%s%s%s", SystemIdentifier.CORE_SYSTEM.getCode(),
        ServiceIdentifier.SHOP_SERVICE.getCode(), value);
  }
}
