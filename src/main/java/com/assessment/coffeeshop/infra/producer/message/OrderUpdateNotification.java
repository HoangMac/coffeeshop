package com.assessment.coffeeshop.infra.producer.message;

import com.assessment.coffeeshop.infra.repo.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateNotification {

  @NotBlank(message = "orderRefNo is required")
  private String orderRefNo;

  @NotNull(message = "orderStatus is required")
  private OrderStatus orderStatus;

  @NotBlank(message = "timeUpdated is required")
  private String timeUpdated;
}
