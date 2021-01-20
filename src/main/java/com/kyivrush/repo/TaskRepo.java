package com.kyivrush.repo;

import com.kyivrush.domain.Task;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepo extends R2dbcRepository<Task, UUID>, CustomTaskRepoRepository {

  @Modifying
  @Query("update tasks set status = :status where task_id = :taskId")
  Mono<Task> updateStatus(@Param(value = "taskId") UUID id, @Param(value = "status") String status);

  @Modifying
  @Query("update tasks set assigner_id = :assignerId where task_id = :taskId")
  Mono<Task> updateAssignerId(@Param(value = "taskId") UUID id,
      @Param(value = "assignerId") UUID assignerId);
}
