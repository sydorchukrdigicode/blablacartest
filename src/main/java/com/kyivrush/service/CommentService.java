package com.kyivrush.service;

import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithIdDto;
import reactor.core.publisher.Mono;

public interface CommentService {
  Mono<CommentWithIdDto> addComment(CommentDto newComment);
  Mono<Boolean> deleteComment(String commentId);
}
