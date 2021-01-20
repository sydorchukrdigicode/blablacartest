package com.kyivrush.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithIdDto;
import com.kyivrush.service.CommentService;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@Slf4j
@WebFluxTest(CommentController.class)
class CommentControllerTest {

  @MockBean
  private CommentService commentService;

  @Autowired
  private WebTestClient webClient;

  @Test
  void shouldCreateComment() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    CommentDto input = CommentDto.builder()
        .authorId("5f1aedefc0b6cb2698e2a160")
        .taskId("5f1aedefc0b6cb2698e2a163")
        .value("comment")
        .build();

    CommentWithIdDto expected = CommentWithIdDto.builder()
        .commentId("5f1aedefc0b6cb2698e2a161")
        .authorId("5f1aedefc0b6cb2698e2a160")
        .taskId("5f1aedefc0b6cb2698e2a163")
        .value("comment")
        .creationTime(time)
        .build();

    when(commentService.addComment(input)).thenReturn(Mono.just(expected));

    //when
    webClient.post()
        .uri("/comment")
        .bodyValue(input)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CommentWithIdDto.class).isEqualTo(expected);

    //then
    verify(commentService, times(1)).addComment(input);
  }

  @Test
  void shouldDeleteComment() {
    //given
    String commentId = "1";

    when(commentService.deleteComment(commentId)).thenReturn(Mono.just(true));

    //when
    webClient.delete()
        .uri("/comment/" + commentId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Boolean.class).isEqualTo(true);

    //then
    verify(commentService, times(1)).deleteComment(commentId);
  }

}