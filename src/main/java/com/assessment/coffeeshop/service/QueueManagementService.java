package com.assessment.coffeeshop.service;

public interface QueueManagementService {

  void dequeueOrder(String queueId, String orderId);
}
