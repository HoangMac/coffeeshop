package com.assessment.coffeeshop.infra.consumer;

import static java.lang.Math.pow;

import com.assessment.coffeeshop.infra.exception.SqsException;
import io.awspring.cloud.sqs.listener.QueueMessageVisibility;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;

@Log4j2
public abstract class RetryableSqsConsumer {

  protected IntUnaryOperator nextRetryCalculator = count -> (int) Math.ceil(pow(count, 3) * 0.5);

  @MessageExceptionHandler(SqsException.class)
  public void exceptionHandler(SqsException ex, @Headers Map<String, Object> headers) {
    log.error(
        "Exception while processing event type {} with id {}",
        ex.getEventType(), ex.getEventId(), ex
    );
    QueueMessageVisibility visibility = (QueueMessageVisibility) headers.get("Visibility");
    var receiveCount = Integer.parseInt((String) headers.get("ApproximateReceiveCount"));
    var nextRetryInSeconds = nextRetryCalculator.applyAsInt(receiveCount);
    visibility.changeToAsync(nextRetryInSeconds);
    log.warn(
        "Message is processed failed {} times already, delay {} seconds until the next execution",
        receiveCount, nextRetryInSeconds);
  }
}
