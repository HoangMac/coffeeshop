package com.assessment.coffeeshop.infra.producer.impl;

import com.assessment.coffeeshop.infra.producer.message.MessageType;
import com.assessment.coffeeshop.infra.producer.message.OrderProcessing;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class OrderProcessingProducer extends AbstractMessageProducer<OrderProcessing> {

  @Value("${cloud.aws.sqs.bindings.order-process-queue-name}")
  private String orderProcessSqsQueueName;

  public OrderProcessingProducer(SqsTemplate sqsTemplate,
      Validator validator, ObjectMapper objectMapper) {
    super(sqsTemplate, validator, objectMapper);
  }


  @Override
  public MessageType withType() {
    return MessageType.ORDER_PROCESSING;
  }

  @Override
  public String withQueue() {
    return orderProcessSqsQueueName;
  }
}
