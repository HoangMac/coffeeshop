package com.assessment.coffeeshop.service;

import java.util.concurrent.TimeUnit;

public interface RedisCacheService {

  boolean acquireLockForKey(String lockKey, int lockTime, TimeUnit lockTimeUnit);

  void releaseLockForKey(String lockKey);
}
