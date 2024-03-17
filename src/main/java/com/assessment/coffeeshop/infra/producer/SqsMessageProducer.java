package com.assessment.coffeeshop.infra.producer;

public interface SqsMessageProducer<T> {

  void sendMessage(T message);
}
