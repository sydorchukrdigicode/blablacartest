package com.kyivrush.service.impl;

import com.kyivrush.service.RatingService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RatingServiceImpl implements RatingService {

  @Override
  public Mono<Integer> getRating(String userId, Integer defaultValue) {
    //rating call emulation
    return Mono.just(userId.hashCode());
  }
}
