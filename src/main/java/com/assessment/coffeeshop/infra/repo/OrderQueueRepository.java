package com.assessment.coffeeshop.infra.repo;

import com.assessment.coffeeshop.infra.repo.entity.OrderQueue;
import java.util.Optional;

public interface OrderQueueRepository {

  void save(OrderQueue orderQueue);

  void delete(OrderQueue orderQueue);

  Optional<OrderQueue> findByPartitionKeyAndSortKey(String queueId, String orderId);

}
