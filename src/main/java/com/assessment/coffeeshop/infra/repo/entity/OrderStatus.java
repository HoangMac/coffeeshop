package com.assessment.coffeeshop.infra.repo.entity;

public enum OrderStatus {
  PENDING,
  IN_QUEUE,
  PROCESSING,
  PROCESSED,
  READY_FOR_PICKUP,
  COMPLETED,
  CANCELLED

}
