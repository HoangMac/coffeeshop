package com.assessment.coffeeshop.infra.exception;

import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class SqsException extends RuntimeException {

  private final String eventId;

  private final String eventType;

  public SqsException(String eventId, String eventType, Throwable throwable) {
    super(
        Optional.ofNullable(throwable).map(Throwable::getMessage).orElse(StringUtils.EMPTY),
        throwable
    );
    this.eventId = eventId;
    this.eventType = eventType;
  }
}
