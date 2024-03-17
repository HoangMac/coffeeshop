package com.assessment.coffeeshop.infra.producer.impl;

import static com.assessment.coffeeshop.infra.producer.message.MessageType.ORDER_UPDATE_NOTIFICATION;

import com.assessment.coffeeshop.infra.producer.message.MessageType;
import com.assessment.coffeeshop.infra.producer.message.OrderUpdateNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderUpdateNotificationProducer extends AbstractMessageProducer<OrderUpdateNotification> {

  @Value("${cloud.aws.sqs.bindings.order-update-noti-queue-name}")
  private String orderUpdateNotiSqsQueueName;

  public OrderUpdateNotificationProducer(SqsTemplate sqsTemplate,
      Validator validator,
      ObjectMapper objectMapper) {
    super(sqsTemplate, validator, objectMapper);
  }

  @Override
  public MessageType withType() {
    return ORDER_UPDATE_NOTIFICATION;
  }

  @Override
  public String withQueue() {
    return orderUpdateNotiSqsQueueName;
  }
}
