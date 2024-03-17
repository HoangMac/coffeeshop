package com.assessment.coffeeshop.util;

import lombok.NonNull;

public class QueryBuilder {

  public static final String QUEUE_ID_PARAM = ":queueId";
  public static final String ORDER_ID_PARAM = ":orderId";
  public static final String AND_OPERATOR = " AND ";
  public static final String EQUAL_OPERATOR = " = ";


  private final StringBuilder queryString = new StringBuilder();

  public QueryBuilder equalParam(@NonNull String param) {
    buildQuery(param, EQUAL_OPERATOR);
    return this;
  }

  public QueryBuilder and() {
    queryString.append(AND_OPERATOR);
    return this;
  }

  private void buildQuery(String param, String operator) {
    var separateComma = param.split(":");
    queryString.append(separateComma[1]);
    queryString.append(operator);
    queryString.append(param);
  }

  public String build() {
    return queryString.toString();
  }

}
