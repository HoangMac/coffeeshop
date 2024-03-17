package com.assessment.coffeeshop.infra.consumer;

import com.assessment.coffeeshop.infra.producer.message.BaseMessage;
import com.assessment.coffeeshop.infra.producer.message.MessageType;
import com.assessment.coffeeshop.infra.producer.message.OrderProcessing;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingConsumer extends BaseMessageSqsConsumer<OrderProcessing> {

  private final JavaType orderProcessType = TypeFactory.defaultInstance()
      .constructParametricType(BaseMessage.class, OrderProcessing.class);

  public OrderProcessingConsumer(Validator validator) {
    super(validator);
  }

  @SqsListener(value = "${cloud.aws.sqs.bindings.order-process-queue-name}")
  public void consume(Message<String> message) {
    processEvent(message);
  }

  @Override
  protected List<MessageType> getMessageTypeSupport() {
    return List.of(MessageType.ORDER_PROCESSING);
  }

  @Override
  protected JavaType javaType() {
    return orderProcessType;
  }
}
