package com.assessment.coffeeshop.constant;

import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

  // Request
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String CUSTOMER_ID_HEADER = "customerId";
  public static final String PROFILE_ATTRIBUTE = "customer_profile";


  // Response fields
  public static final String ERRORS = "errors";
  public static final String ERROR_CODE = "errorCode";
  public static final String ERROR_MSG = "errorMessage";

  // Error Message
  public static final String FROM_DATE_INVALID_MESSAGE = "From time is invalid format";
  public static final String TO_DATE_INVALID_MESSAGE = "To time is invalid format";
  public static final String STATUS_INVALID_MESSAGE = "Status is invalid format";


  // Others
  public static final int SEND_PROCESSING_NOTI_STEP = 1;
  public static final int PROCESS_ORDER_STEP = 2;
  public static final int SEND_READY_NOTI_STEP = 3;

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
}
