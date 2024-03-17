package com.assessment.coffeeshop.service.steps.processorder;

import com.assessment.coffeeshop.infra.repo.OrderRequestRepository;
import com.assessment.coffeeshop.infra.repo.entity.OrderRequest;
import com.assessment.coffeeshop.infra.repo.entity.OrderStatus;
import com.assessment.coffeeshop.service.steps.StepExecute;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractOrderProcessStep implements StepExecute<OrderRequest> {

  private final OrderRequestRepository orderRepository;

  protected abstract Set<OrderStatus> previousStatuses();
  protected abstract OrderStatus successStatus();

  protected abstract void processStep(OrderRequest stepContext);

  @Transactional
  public final void execute(OrderRequest stepContext) {
    if (isExecute(stepContext)) {
      log.info("Processing step {} - with status: {}",
          stepName(), stepContext.getStatus());
      processStep(stepContext);
      log.info("Processed step {} with status: {}",
          stepName(), stepContext.getStatus());

      stepContext.setStatus(successStatus());
      orderRepository.saveAndFlush(stepContext);
    } else {
      log.info("Ignored - Step {} is ignored for status {}.",
          stepName(), stepContext.getStatus());
    }
  }

  private boolean isExecute(OrderRequest stepContext) {
    return previousStatuses().contains(stepContext.getStatus());
  }

  private String stepName() {
    return this.getClass().getSimpleName();
  }
}
