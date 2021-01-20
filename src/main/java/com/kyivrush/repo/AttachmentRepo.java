package com.kyivrush.repo;

import com.kyivrush.domain.Attachment;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AttachmentRepo extends R2dbcRepository<Attachment, UUID> {

  Flux<Attachment> findByTaskId(UUID taskId);
}
