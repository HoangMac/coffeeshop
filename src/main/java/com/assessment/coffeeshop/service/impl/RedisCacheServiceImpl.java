package com.assessment.coffeeshop.service.impl;

import com.assessment.coffeeshop.infra.exception.SystemException;
import com.assessment.coffeeshop.service.RedisCacheService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import lombok.extern.log4j.Log4j2;
import org.hibernate.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RedisCacheServiceImpl implements RedisCacheService {

  private static final String LOCKED_VALUE = "locked";

  private RedisTemplate<String, String> redisTemplate;

  @Override
  public boolean acquireLockForKey(String lockKey, int lockTime, TimeUnit lockTimeUnit) {
    return execute(
        () -> Boolean.TRUE.equals(redisTemplate.opsForValue()
            .setIfAbsent(lockKey, LOCKED_VALUE, lockTime, lockTimeUnit)),
        true
    );
  }

  @Override
  public void releaseLockForKey(String lockKey) {
    execute(() -> Boolean.TRUE.equals(redisTemplate.delete(lockKey)), true);
  }

  private <V> V execute(Supplier<V> function, V defaultValue) {
    try {
      return function.get();
    } catch (Exception ex) {
      resolveRedisException(ex);
      return defaultValue;
    }
  }

  private void resolveRedisException(Exception ex) {
    if (ex instanceof RedisConnectionFailureException || ex instanceof QueryTimeoutException) {
      log.error("Exception when execute action on Redis", ex);
    } else {
      throw new SystemException("Connect to redis failed", ex);
    }
  }
}
