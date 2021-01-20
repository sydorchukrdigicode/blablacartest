package com.kyivrush.repo;

import com.kyivrush.domain.Comment;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends R2dbcRepository<Comment, UUID>, CustomCommentRepoRepository {

}
