package com.assessment.coffeeshop.service.impl;

import static com.assessment.coffeeshop.infra.exception.ErrorCode.ORDER_NOT_FOUND;

import com.assessment.coffeeshop.infra.exception.DomainException;
import com.assessment.coffeeshop.infra.producer.message.BaseMessage;
import com.assessment.coffeeshop.infra.producer.message.OrderProcessing;
import com.assessment.coffeeshop.infra.repo.OrderRequestRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderRequest;
import com.assessment.coffeeshop.service.MessageProcessor;
import com.assessment.coffeeshop.service.RedisCacheService;
import com.assessment.coffeeshop.service.steps.StepExecute;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderProcessingProcessor implements MessageProcessor<OrderProcessing> {

  private final List<StepExecute<OrderRequest>> stepExecutes;

  private final OrderRequestRepository orderRequestRepository;

  private final RedisCacheService redisCacheService;

  @Override
  public void processEvent(BaseMessage<OrderProcessing> event) {
    var orderRequest = orderRequestRepository.findById(event.getDetail().getOrderId())
        .orElseThrow(() -> new DomainException(ORDER_NOT_FOUND));

    stepExecutes.forEach(step -> step.execute(orderRequest));

    redisCacheService.releaseLockForKey(orderRequest.getQueueId());
  }
}
