package com.assessment.coffeeshop.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.assessment.coffeeshop.service.QueueManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/coffee-shop/1.0.0/owner")
public class ShowOwnerController {

  private final QueueManagementService queueManagementService;

  @Operation(summary = "Take an order off the queue and process")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order processed successfully"),
      @ApiResponse(responseCode = "400", description = "Request is invalid")
  })
  @PostMapping(value = "/queues/{queueId}/orders/{orderId}/process", produces = APPLICATION_JSON_VALUE)
  public void processOrder(@PathVariable String queueId, @PathVariable String orderId) {
    queueManagementService.dequeueOrder(queueId, orderId);
  }

}
