package com.assessment.coffeeshop.infra.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

  private final ErrorCode errorCode;

  private final transient Object[] args;

  public DomainException(ErrorCode errorCode, Object... args) {
    super(String.format(errorCode.getMessage(), args));
    this.errorCode = errorCode;
    this.args = args;
  }
}
