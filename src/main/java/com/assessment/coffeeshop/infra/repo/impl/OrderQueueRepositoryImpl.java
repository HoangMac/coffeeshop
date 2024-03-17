package com.assessment.coffeeshop.infra.repo.impl;

import com.assessment.coffeeshop.infra.repo.OrderQueueRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderQueue;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
@RequiredArgsConstructor
public class OrderQueueRepositoryImpl implements OrderQueueRepository {

  private final DynamoDbTemplate dynamoDBTemplate;

  @Override
  public void save(OrderQueue orderQueue) {
    dynamoDBTemplate.save(orderQueue);
  }

  @Override
  public void delete(OrderQueue orderQueue) {
    dynamoDBTemplate.delete(orderQueue);
  }

  @Override
  public Optional<OrderQueue> findByPartitionKeyAndSortKey(String queueId, String orderId) {
    var queryPartitionKey = QueryConditional
        .keyEqualTo(Key.builder()
            .partitionValue(queueId)
            .build());
    var querySortKey = QueryConditional
        .keyEqualTo(
            Key.builder()
                .sortValue(orderId)
                .build());

    var queryRequest = QueryEnhancedRequest.builder()
        .queryConditional(queryPartitionKey)
        .queryConditional(querySortKey)
        .build();

    var pageIterable = dynamoDBTemplate.query(queryRequest, OrderQueue.class);
    return pageIterable.items().stream().findFirst();
  }
}
