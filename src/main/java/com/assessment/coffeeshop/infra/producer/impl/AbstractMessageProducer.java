package com.assessment.coffeeshop.infra.producer.impl;

import static com.assessment.coffeeshop.infra.exception.ErrorCode.INVALID_MESSAGE;

import com.assessment.coffeeshop.infra.exception.DomainException;
import com.assessment.coffeeshop.infra.exception.SystemException;
import com.assessment.coffeeshop.infra.producer.SqsMessageProducer;
import com.assessment.coffeeshop.infra.producer.message.BaseMessage;
import com.assessment.coffeeshop.infra.producer.message.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.validation.Validator;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public abstract class AbstractMessageProducer<T> implements SqsMessageProducer<T> {

  private final SqsTemplate sqsTemplate;

  private final Validator validator;

  private final ObjectMapper objectMapper;

  @Override
  public void sendMessage(T message) {
    sqsTemplate.send(to -> to.queue(withQueue())
        .payload(buildMessage(message)));
  }

  public abstract MessageType withType();

  public abstract String withQueue();

  private String buildMessage(T message) {
    validateContent(message);
    return toMessage(message);
  }

  private void validateContent(T message) {
    var violations = validator.validate(message);
    if (!CollectionUtils.isEmpty(violations)) {
      var errorMessages = violations.stream()
          .map(violation -> String.join(" - ", violation.getPropertyPath().toString(),
              violation.getMessage()))
          .collect(Collectors.toList());
      throw new DomainException(INVALID_MESSAGE, String.join(", ", errorMessages));
    }
  }

  private String toMessage(T message) {
    var sqsMsg = BaseMessage.<T>builder()
        .id(UUID.randomUUID().toString())
        .time(Instant.now().toEpochMilli())
        .type(withType())
        .detail(message)
        .build();

    try {
      return objectMapper.writeValueAsString(sqsMsg);
    } catch (JsonProcessingException ex) {
      throw new SystemException("Deserialize command message failed", ex);
    }
  }
}
