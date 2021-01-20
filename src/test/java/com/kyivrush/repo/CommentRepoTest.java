package com.kyivrush.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kyivrush.domain.Comment;
import com.kyivrush.domain.CommentWithDetails;
import com.kyivrush.domain.User;
import io.r2dbc.spi.ConnectionFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.ConnectionFactoryUtils;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class CommentRepoTest extends GenericRepoIntegrationTest {

  private static final UUID DEPARTMENT_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID TASK_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b910");
  private static final UUID TASK_ID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b900");
  private static final UUID USER_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID COMMENT_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b916");
  private static final UUID COMMENT_ID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b917");
  private static final UUID COMMENT_ID_3 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b918");
  private static final UUID COMMENT_ID_4 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b919");

  @Autowired
  private CommentRepo commentRepo;

  @Autowired
  private ConnectionFactory connectionFactory;

  @BeforeEach
  void setUp() {
    ConnectionFactoryUtils.getConnection(connectionFactory)
        .flatMap(connection -> ScriptUtils
            .executeSqlScript(connection, new ClassPathResource("data.sql")))
        .block();
  }

  @Test
  void shouldSaveComment() {
    //given
    Instant currentTime = Instant.ofEpochSecond(1000L);
    new MockUp<Instant>() {
      @Mock
      Instant now(Clock clock) {
        return currentTime;
      }
    };

    Comment expected = Comment.builder()
        .authorId(USER_ID)
        .value("comment")
        .taskId(TASK_ID)
        .build();

    //when
    commentRepo.save(expected)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();

    //then
    commentRepo.findAll()
        .collectList()
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .assertNext(actual -> {
          List<UUID> commentIds = List.of(COMMENT_ID, COMMENT_ID_2, COMMENT_ID_3, COMMENT_ID_4);
          Comment newComment = actual.stream()
              .filter(comment -> !commentIds.contains(comment.getCommentId()))
              .findFirst()
              .get();
          assertNotNull(newComment.getCommentId());
          assertEquals(expected.getAuthorId(), newComment.getAuthorId());
          assertEquals(expected.getValue(), newComment.getValue());
          assertEquals(expected.getTaskId(), newComment.getTaskId());
          assertEquals(currentTime, newComment.getCreationTime());
        })
        .verifyComplete();
  }

  @Test
  void shouldDeleteComment() {
    //given

    //when
    commentRepo.deleteById(COMMENT_ID)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .verifyComplete();

    //then
    commentRepo.findAll()
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void shouldFindByTaskId() {
    //given
    Instant currentTime = Instant.ofEpochSecond(1000L);
    CommentWithDetails expected = CommentWithDetails.builder()
        .commentId(COMMENT_ID_4)
        .taskId(TASK_ID_2)
        .authorId(USER_ID)
        .value("comment-4")
        .creationTime(currentTime)
        .author(User.builder()
            .userId(USER_ID)
            .name("user-1")
            .departmentId(DEPARTMENT_ID)
            .build())
        .build();

    //when
    Mono<List<CommentWithDetails>> result = commentRepo.findWithUserByTaskId(TASK_ID_2)
        .log()
        .collectList();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .assertNext(comments -> {
          assertEquals(1, comments.size());
          assertEquals(expected, comments.get(0));
        })
        .verifyComplete();
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
      ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
      initializer.setConnectionFactory(connectionFactory);
      return initializer;
    }
  }
}
