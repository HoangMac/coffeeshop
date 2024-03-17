package com.assessment.coffeeshop.infra.exception;


import static com.assessment.coffeeshop.constant.Constant.ERRORS;
import static com.assessment.coffeeshop.constant.Constant.ERROR_CODE;
import static com.assessment.coffeeshop.constant.Constant.ERROR_MSG;
import static com.assessment.coffeeshop.infra.exception.ErrorCode.UNKNOWN_ERROR;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class ExceptionAdviceHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, Object> onMethodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e) {
    var errorDetailList = e
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .filter(defaultMessage -> !Objects.isNull(defaultMessage))
        .collect(Collectors.toSet());

    var errors = errorDetailList.stream()
        .map(error -> Map.of(
            ERROR_CODE, ErrorCode.INVALID_REQUEST.toUniversalCode(),
            ERROR_MSG, error
        )).collect(Collectors.toList());

    log.error(
        "method: onMethodArgumentNotValidException, endpoint: {}, queryString: {}, errorDetailList: {}",
        request.getRequestURI(), request.getQueryString(), errors, e
    );
    return Map.of(ERRORS, errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DomainException.class)
  public Map<String, Object> handleDomainException(HttpServletRequest request, DomainException e) {
    var errorCode = e.getErrorCode();
    var errors = List.of(
        Map.of(
            ERROR_CODE, errorCode.toUniversalCode(),
            ERROR_MSG, errorCode.getMessage()
        )
    );
    log.error(
        "method: handleStepException, endpoint: {}, queryString: {}, errorDetailList: {}",
        request.getRequestURI(), request.getQueryString(), errors, e
    );
    return Map.of(ERRORS, errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  protected Map<String, Object> onConstraintViolationException(HttpServletRequest request,
      ConstraintViolationException ex) {
    var errors = ex.getConstraintViolations().stream()
        .map(violation -> Map.of(
            ERROR_CODE, ErrorCode.INVALID_REQUEST.toUniversalCode(),
            ERROR_MSG, violation.getMessage()))
        .collect(Collectors.toList());

    log.error(
        "method: onConstraintViolationException, endpoint: {}, queryString: {}, errorDetailList: {}",
        request.getRequestURI(), request.getQueryString(), errors, ex);
    return Map.of(ERRORS, errors);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(SystemException.class)
  public Map<String, Object> onSystemErrorException(HttpServletRequest request, SystemException ex) {
    var errors = List.of(
        Map.of(
            ERROR_CODE, UNKNOWN_ERROR.toUniversalCode(),
            ERROR_MSG, ex.getMessage()
        )
    );

    log.error(
        "method: onSystemErrorException, endpoint: {}, queryString: {}, errorDetailList: {}",
        request.getRequestURI(), request.getQueryString(), errors, ex);
    return Map.of(ERRORS, errors);
  }

}
