package com.kyivrush.repo.impl;

import com.kyivrush.domain.CommentWithDetails;
import com.kyivrush.domain.User;
import com.kyivrush.repo.CustomCommentRepoRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomCommentRepoRepositoryImpl implements CustomCommentRepoRepository {

  private final DatabaseClient databaseClient;

  @Override
  public Flux<CommentWithDetails> findWithUserByTaskId(UUID taskId) {
    //todo jooq for sql generation
    //todo create mappers/convertors
    return databaseClient.sql(
        "select c.comment_id, c.task_id, c.value, c.author_id, c.creation_time, "
            + " u.user_id, u.name, u.department_id "
            + "from comments c left join users u on u.user_id = c.author_id "
            + "where c.task_id = :task_id ")
        .bind("task_id", taskId)
        .map((row, rowMetadata) ->
            CommentWithDetails.builder()
                .commentId(row.get("comment_id", UUID.class))
                .taskId(row.get("task_id", UUID.class))
                .value(row.get("value", String.class))
                .authorId(row.get("author_id", UUID.class))
                .creationTime(row.get("creation_time", Instant.class))
                .author(User.builder()
                    .userId(row.get("user_id", UUID.class))
                    .name(row.get("name", String.class))
                    .departmentId(row.get("department_id", UUID.class))
                    .build())
                .build())
        .all()
        .cast(CommentWithDetails.class);
  }

}
