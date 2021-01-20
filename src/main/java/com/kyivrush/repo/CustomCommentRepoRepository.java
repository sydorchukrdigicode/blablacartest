package com.kyivrush.repo;

import com.kyivrush.domain.CommentWithDetails;
import java.util.UUID;
import reactor.core.publisher.Flux;

public interface CustomCommentRepoRepository {

  Flux<CommentWithDetails> findWithUserByTaskId(UUID taskId);
}
