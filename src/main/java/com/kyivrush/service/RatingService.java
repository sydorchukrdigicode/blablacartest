package com.kyivrush.service;

import reactor.core.publisher.Mono;

public interface RatingService {

  Mono<Integer> getRating(String userId, Integer defaultValue);
}
