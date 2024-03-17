package com.assessment.coffeeshop.service.impl;

import static com.assessment.coffeeshop.infra.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.assessment.coffeeshop.infra.exception.ErrorCode.QUEUE_BUSY;

import com.assessment.coffeeshop.infra.exception.DomainException;
import com.assessment.coffeeshop.infra.producer.SqsMessageProducer;
import com.assessment.coffeeshop.infra.producer.message.OrderProcessing;
import com.assessment.coffeeshop.infra.repo.OrderQueueRepository;
import com.assessment.coffeeshop.service.QueueManagementService;
import com.assessment.coffeeshop.service.RedisCacheService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class QueueManagementServiceImpl implements QueueManagementService {

  public static final int KEY_LOCK_TIMEOUT = 10;

  private final OrderQueueRepository orderQueueRepository;

  private final SqsMessageProducer<OrderProcessing> messageProducer;

  private final RedisCacheService redisCacheService;

  @Override
  public void dequeueOrder(String queueId, String orderId) {
    if (!redisCacheService.acquireLockForKey(queueId, KEY_LOCK_TIMEOUT, TimeUnit.MINUTES)) {
      log.error("Queue {} is being served.", queueId);
      throw new DomainException(QUEUE_BUSY,queueId);
    }

    // Find order
    var orderQueue = orderQueueRepository.findByPartitionKeyAndSortKey(queueId, orderId)
        .orElseThrow(() -> new DomainException(ORDER_NOT_FOUND, orderId));

    // Send message to SQS
    messageProducer.sendMessage(OrderProcessing.builder().orderId(orderQueue.getOrderId()).build());

    // Dequeue order
    orderQueueRepository.delete(orderQueue);
  }
}
