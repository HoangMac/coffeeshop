package com.assessment.coffeeshop.service;


import com.assessment.coffeeshop.infra.producer.message.BaseMessage;

public interface MessageProcessor<D> {

  void processEvent(BaseMessage<D> event);
}
