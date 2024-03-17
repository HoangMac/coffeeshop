package com.assessment.coffeeshop.config;

import static com.fasterxml.jackson.databind.cfg.CoercionAction.AsNull;
import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.EmptyString;
import static com.fasterxml.jackson.databind.type.LogicalType.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

  @Bean
  public ObjectMapper om() {
    ObjectMapper om = new ObjectMapper();
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    om.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    om.coercionConfigFor(Map).setCoercion(EmptyString, AsNull);
    return om;
  }
}
