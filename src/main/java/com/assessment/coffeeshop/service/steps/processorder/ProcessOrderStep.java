package com.assessment.coffeeshop.service.steps.processorder;

import static com.assessment.coffeeshop.constant.Constant.PROCESS_ORDER_STEP;
import static com.assessment.coffeeshop.infra.repo.entity.OrderStatus.PROCESSED;
import static com.assessment.coffeeshop.infra.repo.entity.OrderStatus.PROCESSING;

import com.assessment.coffeeshop.infra.repo.OrderRequestRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderRequest;
import com.assessment.coffeeshop.infra.repo.entity.OrderStatus;
import java.util.Set;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Order(PROCESS_ORDER_STEP)
public class ProcessOrderStep extends AbstractOrderProcessStep {

  private final OrderRequestRepository orderRequestRepository;

  public ProcessOrderStep(
      OrderRequestRepository orderRepository, OrderRequestRepository orderRequestRepository) {
    super(orderRepository);
    this.orderRequestRepository = orderRequestRepository;
  }

  @Override
  protected Set<OrderStatus> previousStatuses() {
    return Set.of(PROCESSING);
  }

  @Override
  protected OrderStatus successStatus() {
    return PROCESSED;
  }

  @Override
  @Transactional
  protected void processStep(OrderRequest stepContext) {
    stepContext.setInQueue(false);
    orderRequestRepository.save(stepContext);

    var orders = orderRequestRepository.findByQueueIdAndInQueueTrue(
        stepContext.getQueueId());
    orders.forEach(orderRequest -> {
      orderRequest.setQueuePosition(orderRequest.getQueuePosition() - 1);
      orderRequest.setWaitingTime(orderRequest.getWaitingTime() - stepContext.getWaitingTime());
    });
    orderRequestRepository.saveAll(orders);
  }
}
