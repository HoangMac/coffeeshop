package com.assessment.coffeeshop.infra.producer.message;

import com.assessment.coffeeshop.validation.UUIDFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProcessing {

  @UUIDFormat
  @NotBlank(message = "orderId is required")
  private String orderId;
}
