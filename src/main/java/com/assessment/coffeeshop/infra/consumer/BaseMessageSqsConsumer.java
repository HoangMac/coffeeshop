package com.assessment.coffeeshop.infra.consumer;


import static com.assessment.coffeeshop.infra.exception.ErrorCode.INVALID_MESSAGE;

import com.assessment.coffeeshop.infra.exception.DomainException;
import com.assessment.coffeeshop.infra.exception.SqsException;
import com.assessment.coffeeshop.infra.exception.SystemException;
import com.assessment.coffeeshop.infra.producer.message.BaseMessage;
import com.assessment.coffeeshop.infra.producer.message.MessageType;
import com.assessment.coffeeshop.service.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;

@Log4j2
@RequiredArgsConstructor
public abstract class BaseMessageSqsConsumer<D> extends RetryableSqsConsumer {

  private final Validator validator;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected MessageProcessor<D> messageProcessor;

  protected abstract List<MessageType> getMessageTypeSupport();

  protected abstract JavaType javaType();

  protected void processEvent(Message<String> sqsMsg) {
    var payload = sqsMsg.getPayload();
    var message = deserialize(payload);
    var messageId = message.getId();
    var type = message.getType();
    try {
      validateMessage(message);
      if (isSupportType(message)) {
          handleEvent(message);
      } else {
        log.warn("The message-id = {} - message-type {} not support", messageId, type);
      }
    } catch (Exception ex) {
      log.error("Failed to consume the message-id: {} - message-type: {} exception: {}",
          messageId, type, ex);
      throw ex;
    }
  }

  protected void handleEvent(BaseMessage<D> message) {
    try {
      messageProcessor.processEvent(message);
    } catch (Exception e) {
      throw new SqsException(message.getId(), message.getType().name(), e);
    }
  }

  protected BaseMessage<D> deserialize(String message) {
    try {
      return objectMapper.readValue(message, javaType());
    } catch (JsonProcessingException ex) {
      throw new SystemException("Cannot deserialize the message", ex);
    }
  }

  private boolean isSupportType(BaseMessage<D> message) {
    return getMessageTypeSupport().contains(message.getType());
  }

  private void validateMessage(BaseMessage<D> message) {
    var violations = validator.validate(message);
    if (!CollectionUtils.isEmpty(violations)) {
      var errorMessages = violations.stream()
          .map(violation -> String.join(" - ", violation.getPropertyPath().toString(),
              violation.getMessage()))
          .collect(Collectors.toList());
      throw new DomainException(INVALID_MESSAGE, String.join(", ", errorMessages));
    }
  }
}