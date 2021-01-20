package com.kyivrush.controller;

import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithIdDto;
import com.kyivrush.service.CommentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping(value = "/comment",
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<CommentWithIdDto> createComment(@Valid @RequestBody CommentDto commentDto) {
    log.info("Handled creation comment = {}", commentDto);
    return commentService.addComment(commentDto);
  }

  @DeleteMapping(value = "/comment/{commentId}")
  public Mono<Boolean> createComment(@PathVariable("commentId") String commentId) {
    log.info("Handled deletion comment by id = {}", commentId);
    return commentService.deleteComment(commentId);
  }
}
