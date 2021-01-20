package com.kyivrush.service.impl;

import com.kyivrush.service.CacheService;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CacheServiceImpl implements CacheService {

  @Override
  public Mono<Integer> getCachedValue(String requestName,
      String param,
      Integer defaultValue,
      BiFunction<String, Integer, Mono<Integer>> function) {
    //todo implement cache redis for example

    //cache call...... requestName + param
    //if found
    // then return from cache
    //if no value
    return function.apply(param, defaultValue);
  }
}
