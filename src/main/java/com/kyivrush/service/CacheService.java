package com.kyivrush.service;

import java.util.function.BiFunction;
import reactor.core.publisher.Mono;

public interface CacheService {

  String USER_RATING_REQUEST = "USER_RATING_REQUEST";

  Mono<Integer> getCachedValue(String requestName,
      String param,
      Integer defaultValue,
      BiFunction<String, Integer, Mono<Integer>> function
  );
}
