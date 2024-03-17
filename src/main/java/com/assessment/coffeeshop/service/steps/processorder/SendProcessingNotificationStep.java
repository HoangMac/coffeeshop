package com.assessment.coffeeshop.service.steps.processorder;

import static com.assessment.coffeeshop.constant.Constant.DATE_FORMATTER;
import static com.assessment.coffeeshop.constant.Constant.SEND_PROCESSING_NOTI_STEP;
import static com.assessment.coffeeshop.infra.repo.entity.OrderStatus.IN_QUEUE;
import static com.assessment.coffeeshop.infra.repo.entity.OrderStatus.PROCESSING;

import com.assessment.coffeeshop.infra.producer.SqsMessageProducer;
import com.assessment.coffeeshop.infra.producer.message.OrderUpdateNotification;
import com.assessment.coffeeshop.infra.repo.OrderRequestRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderRequest;
import com.assessment.coffeeshop.infra.repo.entity.OrderStatus;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(SEND_PROCESSING_NOTI_STEP)
public class SendProcessingNotificationStep extends AbstractOrderProcessStep {

  private final SqsMessageProducer<OrderUpdateNotification> messageProducer;

  public SendProcessingNotificationStep(
      OrderRequestRepository orderRepository,
      SqsMessageProducer<OrderUpdateNotification> messageProducer) {
    super(orderRepository);
    this.messageProducer = messageProducer;
  }

  @Override
  protected Set<OrderStatus> previousStatuses() {
    return Set.of(IN_QUEUE);
  }

  @Override
  protected OrderStatus successStatus() {
    return PROCESSING;
  }

  @Override
  protected void processStep(OrderRequest stepContext) {
    messageProducer.sendMessage(OrderUpdateNotification.builder()
        .orderRefNo(stepContext.getReferenceNumber())
        .orderStatus(successStatus())
        .timeUpdated(DATE_FORMATTER.format(LocalDateTime.now()))
        .build());
  }
}
