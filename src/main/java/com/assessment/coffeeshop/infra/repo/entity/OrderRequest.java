package com.assessment.coffeeshop.infra.repo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderRequest extends AbstractEntity {

  @Id
  private String orderId;

  private String queueId;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private Integer queuePosition;

  private Integer waitingTime;

  private BigDecimal totalPrice;

  private String referenceNumber;

  private Boolean inQueue;

  // TODO add items
}
