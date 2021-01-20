package com.kyivrush.service.impl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithIdDto;
import com.kyivrush.mapper.CommentMapper;
import com.kyivrush.repo.CommentRepo;
import com.kyivrush.service.CommentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentMapper commentMapper;
  private final CommentRepo commentRepo;

  @Override
  public Mono<CommentWithIdDto> addComment(CommentDto newComment) {
    log.debug("Adding new comment = {}", newComment);

    return commentRepo.save(commentMapper.map(newComment))
        .map(commentMapper::map)
        .doOnSuccess(createdTask -> log.debug("Added comment = {}", createdTask));
  }

  @Override
  public Mono<Boolean> deleteComment(String commentId) {
    log.debug("Deleting comment by id {}", commentId);

    if (isEmpty(commentId)) {
      return Mono.error(new RuntimeException("Invalid commentId = " + commentId));
    }

    return commentRepo.deleteById(UUID.fromString(commentId))
        .thenReturn(true)
        .doOnSuccess(
            ignore -> log.debug("Deleted comment by id {}", commentId));
  }
}
