package com.kyivrush.repo;

import com.kyivrush.domain.TaskFilter;
import com.kyivrush.domain.TaskWithDetails;
import java.util.UUID;
import reactor.core.publisher.Flux;

public interface CustomTaskRepoRepository {

  Flux<TaskWithDetails> findByFilter(TaskFilter taskFilter);

  Flux<TaskWithDetails> findWithUserById(UUID taskId);
}
