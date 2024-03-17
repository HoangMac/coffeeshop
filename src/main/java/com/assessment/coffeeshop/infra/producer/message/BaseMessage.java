package com.assessment.coffeeshop.infra.producer.message;

import com.assessment.coffeeshop.validation.UUIDFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseMessage<D> {

  @NotNull(message = "id is required")
  @UUIDFormat
  private String id;

  @NotNull(message = "time is required")
  private Long time;

  @NotNull(message = "type is required")
  private MessageType type;

  @Valid
  @NotNull(message = "detail content is required")
  private D detail;
}
