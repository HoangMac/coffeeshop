package com.assessment.coffeeshop.service.impl;

import static com.assessment.coffeeshop.infra.exception.ErrorCode.ORDER_NOT_FOUND;
import static com.assessment.coffeeshop.infra.exception.ErrorCode.QUEUE_BUSY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.assessment.coffeeshop.infra.exception.DomainException;
import com.assessment.coffeeshop.infra.producer.SqsMessageProducer;
import com.assessment.coffeeshop.infra.producer.message.OrderProcessing;
import com.assessment.coffeeshop.infra.repo.OrderQueueRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderQueue;
import com.assessment.coffeeshop.service.RedisCacheService;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QueueManagementServiceImplTest {

  private static final String QUEUE_ID = "fad10990-e424-11ee-bd3d-0242ac120002";
  private static final String ORDER_ID = "0361a7e0-e425-11ee-bd3d-0242ac120002";

  @Mock
  private OrderQueueRepository orderQueueRepository;

  @Mock
  private SqsMessageProducer<OrderProcessing> messageProducer;

  @Mock
  private RedisCacheService redisCacheService;


  @InjectMocks
  private QueueManagementServiceImpl testClazz;

  @Test
  void testDequeueOrder_givenQueueLocked_shouldThrowException() {
    when(redisCacheService.acquireLockForKey(QUEUE_ID, 10, TimeUnit.MINUTES)).thenReturn(false);

    var exception = assertThrows(DomainException.class, () -> testClazz.dequeueOrder(QUEUE_ID, ORDER_ID));
    assertEquals(QUEUE_BUSY, exception.getErrorCode());
  }

  @Test
  void testDequeueOrder_givenNoOrder_shouldThrowException() {
    when(redisCacheService.acquireLockForKey(QUEUE_ID, 10, TimeUnit.MINUTES)).thenReturn(true);
    when(orderQueueRepository.findByPartitionKeyAndSortKey(QUEUE_ID, ORDER_ID)).thenReturn(Optional.empty());

    var exception = assertThrows(DomainException.class, () -> testClazz.dequeueOrder(QUEUE_ID, ORDER_ID));
    assertEquals(ORDER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void testDequeueOrder_successfully() {
    when(redisCacheService.acquireLockForKey(QUEUE_ID, 10, TimeUnit.MINUTES)).thenReturn(true);
    var orderQueue = OrderQueue.builder().orderId(ORDER_ID).build();
    when(orderQueueRepository.findByPartitionKeyAndSortKey(QUEUE_ID, ORDER_ID))
        .thenReturn(Optional.of(orderQueue));

    testClazz.dequeueOrder(QUEUE_ID, ORDER_ID);

    verify(messageProducer, timeout(1)).sendMessage(OrderProcessing.builder()
        .orderId(ORDER_ID).build());
    verify(orderQueueRepository, timeout(1)).delete(orderQueue);
  }
}