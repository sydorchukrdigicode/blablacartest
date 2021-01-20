package com.kyivrush.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kyivrush.domain.Comment;
import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithIdDto;
import com.kyivrush.mapper.CommentMapper;
import com.kyivrush.repo.CommentRepo;
import com.kyivrush.service.CommentService;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
  @Mock
  private CommentRepo commentRepo;
  private CommentService commentService;

  @BeforeEach
  void setUp() {
    commentService = new CommentServiceImpl(commentMapper, commentRepo);
  }

  @Test
  void shouldCreateComment() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    CommentDto input = CommentDto.builder()
        .authorId(UUID_1.toString())
        .value("comment-1")
        .build();
    Comment inputDomain = Comment.builder()
        .authorId(UUID_1)
        .value("comment-1")
        .build();

    CommentWithIdDto expected = CommentWithIdDto.builder()
        .authorId(UUID_1.toString())
        .value("comment-1")
        .creationTime(time)
        .commentId(UUID_2.toString())
        .build();

    Comment repoResult = inputDomain.toBuilder()
        .commentId(UUID_2)
        .creationTime(time)
        .build();

    when(commentRepo.save(inputDomain))
        .thenReturn(Mono.just(repoResult));

    //when
    Mono<CommentWithIdDto> result = commentService.addComment(input)
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
    verify(commentRepo, times(1)).save(inputDomain);
  }

  @Test
  void shouldDeleteComment() {
    //given
    when(commentRepo.deleteById(UUID_1))
        .thenReturn(Mono.empty());

    //when
    Mono<Boolean> result = commentService.deleteComment(UUID_1.toString())
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(true)
        .verifyComplete();
    verify(commentRepo, times(1))
        .deleteById(UUID_1);
  }

  @Test
  void shouldFailDeleteComment() {
    //given

    //when
    Mono<Boolean> result = commentService.deleteComment("")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .verifyError(RuntimeException.class);
  }
}