package com.assessment.coffeeshop.infra.repo.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DynamoDbBean
public class OrderQueue extends AbstractDynamoEntity {

  public String queueId;

  public String orderId;

  private String customerName;

  @DynamoDbPartitionKey
  public String getQueueId() {
    return queueId;
  }

  @DynamoDbSortKey
  public String getOrderId() {
    return orderId;
  }

  @DynamoDbAttribute("customerName")
  public String getCustomerName() {
    return customerName;
  }
}
